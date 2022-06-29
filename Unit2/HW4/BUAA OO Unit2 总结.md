# BUAA OO Unit2 总结

第二单元的作业是多线程模拟北航新主楼的电梯接人，由第一次作业到第三次作业，编码难度并不大，架构选用也没什么好纠结的，但是由于自己作死，过分迷信助教的实验代码，前两次把该踩的坑，不该踩的坑基本都踩了，体验极差，只有第三次状态正常

## 第一次作业

### 作业思路

#### 架构

* **总框架：** 第一次作业是简单的每栋楼只有一部电梯互不干扰的运行，此次作业没有写调度器，采取的是从输入线程直接将请求发给每一部电梯的方式，同时也只是使用了简单的生产者消费者模式，一个简单的关系如下图：

  ```mermaid
  graph LR
  A[InputThread] --> |输入请求|B((requestQueue)) --> |取出请求并处理| C[ElevatorThread]
  ```
* **电梯框架：** 具体的电梯则是将其分为行为和策略两个部分，即 ``Elevator``，``Strategy``两个类，将电梯和其采取的方法解耦合，每部电梯都有一个自己的策略，在第一次作业中我选取的是 ``look``算法作为策略，让其继承一个策略接口，策略接口有四个关键方法：

  ```java
  public interface Strategy {
      ArrayList<PersonRequest> whoToPickUp(ArrayList<PersonRequest> inElevator, int dir, int cur);
  
      int whichDir(ArrayList<PersonRequest> inElevator, int dir, int cur);
  
      boolean whetherToOpen(ArrayList<PersonRequest> inElevator, int dir, int cur);
  
      boolean whetherToMove(ArrayList<PersonRequest> inElevator);
  }
  ```

#### 同步块的设置和锁块的选择

首先关于此次作业只有一个共享对象就是 ``requestQueue``，因此所有锁和同步块的选择都和它有关

* ``requestQueue``仿照实验代码的 ``requestQueue``，将每个方法比如 ``addRequest``、``removeRequest``、``isEmpty``。。。都加锁，**都 ``notifyAll``了一遍（为第二次作业埋了一个大雷之后再讲。。。）**
* ``Elevator``中是最关键的部分，就是基本的生产者消费者问题，只要还有请求没处理，并且输入没结束就接着循环，进入循环后如果发现请求为空就 ``wait``在 ``requestQueue``上不然就去处理，而 ``wait``被唤醒后如果发现没有请求且输入结束就 ``break``反之就可以去处理了，代码如下：

  ```java
   public void run() {
          while (!(requests.isEnd() && requests.isEmpty() && inElevator.isEmpty())) {
              synchronized (requests) {
                  if (requests.isEmpty() && inElevator.isEmpty() && !requests.isEnd()) {
                      try {
                          requests.wait();
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                  }
                  if (requests.isEnd() && inElevator.isEmpty() && requests.isEmpty()) {
                      break;
                  }
              }
              // do something
          }
      }
  ```

  注意判断条件一定要写全，比如第一个 ``if``处不写 ``!requests.isEnd()``那么有可能在进入 ``while``后，进入同步块前 ``requests``结束了，但是由于没有判断就进入了 ``wait``，于是线程就结束不了了，同时注意此处的 ``wait``只会被 ``requestQueue``的 ``setEnd``或者 ``addRequest``方法唤醒，而之前说的我在 ``requestQueue``的所有方法中均加了 ``notifyAll``，在这次只有一部电梯的时候其实不会有事，最多 ``cpu``时间多一点，但是也不是轮询，所以不会超时，不过第二次作业就惨了。。。
* ``LookStrategy``中，遍历 ``requestQueue``中的 ``ArrayList``的代码块都加了锁，防止在遍历过程中加入请求引起线程不安全问题

#### 性能

* **电梯采取纵向 ``look``策略（相信我在这个单元绝对比ALS快）**：

  * **电梯运行方向（初始方向向上）：**

    * 电梯中有乘客时：为电梯中乘客的目的地方向
    * 电梯中没有乘客时：如果在当前方向上电梯外部（包括当前层）有请求，则电梯保持原方向不变，反之则调转方向
  * **电梯接人策略：**

    只接目的地与电梯当前运行方向相同的外部乘客
* **$400ms$内随时接人**，先看代码吧：

  ```java
  public void executeClose() {
      while (System.currentTimeMillis() - lastOpenTime < 400) {
          synchronized (requests) {
              long tmpTime = System.currentTimeMillis() - lastOpenTime;
              if (tmpTime >= 400) {
                  break;
              }
              try {
                  requests.wait(400 - tmpTime);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              pickUp();
          }
      }
      close();
  }
  ```

  严格来讲就是使用一个 ``wait(timeout)``，使电梯在开关门的$400ms$内可以随时接人，否则只能 ``sleep(400L)``后接一次人，这样花费时间就大于 ``400ms``了
* **可接两次人**，还是先看代码吧：

  ```java
  public void execute() {
          if (strategy.whetherToOpen(inElevator, dir, curFloor)) {
              executeOpen();
          }
          dir = strategy.whichDir(inElevator, dir, curFloor);
          if (strategy.whetherToOpen(inElevator, dir, curFloor)) {
              executeOpen();
          }
          if (isOpen) {
              executeClose();
          }
      }
  ```

  理解起来很简单，如果只有一次判断开门处理开门，那么如果开门后人下光了，当前层没有同方向请求（但是有反方向的请求）并且沿当前方向也没有请求，那么按照标准 ``look``策略就应该掉头走掉，而当前层与掉头后方向相同的请求也不会理了，所以判断两次即可解决这个问题
* **排序**，策略类提供可接的人列表时按照目的地距当前层从远到近或者从近到远排序，排序性能一定比不排序好
* **电梯瞬移一层**，刚开始还做了另一个优化，但其实这个可能不怎么会影响性能，就是：

  ```java
  public void run() {
          while (!(requests.isEnd() && requests.isEmpty() && inElevator.isEmpty())) {
              synchronized (requests) {
                  if (requests.isEmpty() && inElevator.isEmpty() && !requests.isEnd()) {
                      try {
                          requests.wait();
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                  }
                  if (requests.isEnd() && inElevator.isEmpty() && requests.isEmpty()) {
                      break;
                  }
              }
              // do something
          }
      }
  ```

  这个代码块中的 ``wait()``其实等待也浪费了不少时间，而如果我在等待的时候获取一个时间戳，等待结束再获取一个，记录等待了多少时间，由于等待结束会有请求要处理，如果要处理的请求不在当前层并且等待时间超过了$400ms$，那么就可以瞬移一层，而不用再等待电梯移动一层的时间，不满$400ms$也可以少等待一点时间，但是和舍友对比了一下这个优化其实不需要，因为其实如果你瞬移了，那么如果当前层过了几毫秒来的请求就接不到了，这里注意是我统计了强测$20$个点的数据跑出来的情况得出来的结论（在 ``bug``修复阶段反复试探。。。）

### 代码结构分析

#### 类图

![HW1.jpg](https://s2.loli.net/2022/04/27/2FDvHCr6TkVZobI.jpg)

#### 时序图

![diagram-11751567058654997404.png](https://s2.loli.net/2022/04/29/CbGA4fTFztu2Ydp.png)

### 测试

由于此次作业非常简单于是并未做任何测试，也没有写评测机，写完就交了不管了，这其实也是在为我的强测埋雷（当然由于第一次作业还是比较简单，强测互测没有出除了输出线程安全外的功能性错误）

### 评测

互测被输出安问题刀烂了，强测由于太闲作死瞎优化 ``TLE``$3$个点，其它点有的性能拉满，也有几个慢得离谱。。。

* 首先就是输出的线程安全问题，由于官方的输出包不是线程安全的，比如说有两部电梯要输出，其中一部电梯的输出函数刚获得了时间戳，``cpu``就去执行另一部电梯的输出函数，执行完了再接着执行该电梯的输出，于是该电梯的时间戳早于刚刚执行完输出的电梯的输出的时间戳，于是后一条输出的时间戳早于前一条，将官方输入封装一下就好了

  ```java
  public class MyOutPut {
      public static synchronized long myPrintln(String str) {
          return TimableOutput.println(str);
      }
  }
  ```
* 发现自己 ``TLE``了，于是将之前课下第一次写完的版本交到了 ``bug``修复那里，全过了，和一个同学对了一下运行时间，发现基本都比他块，他用的是 ``ALS``策略拿了$96$分

  究其原因在于写第一次作业太顺了，开始优化策略，但是又没有写评测机，也就是没有大数据的测试，而是对着中测那几组给出来的数据开始对着数据编程，比如同方向和反方向的人我都接，判断电梯当前运行方向要根据外部请求数与方向与内部请求数与方向来共同判定，这样对着数据再加上简单的大脑yy做完后中测几个点倒是肉眼可见的快了，但是强测就。。。所以为什么我放着好好的$95$以上的分数不要，非要乱优化导致 ``TLE``三个点，其实就是闲得蛋疼。。。

至于其它人的 ``bug``我没怎么管，因为就下了一个人的代码，发现他接一个人开关一次门就知道自己这次可能凉了，于是很伤心随便乱交了几发，``Hack``到了线程输出安全问题，程序停不下来（出结果才知道，并未看代码）等奇怪问题就不管了。。。

## 第二次作业

### 作业思路

#### 架构

* **总框架：** 这次要求是添加了横向电梯，但是一个请求中出发地和目的地楼栋和楼层一定有一个是一致的，所以我在思考后加了调度器，横向每层楼一个调度器，纵向每栋楼一个调度器，即实例化了$15$个调度器，由输入线程将请求分给各个调度器，再由调度器分给每一个电梯，共享对象变成了调度器和电梯之间的 ``requestQueue``，而调度器和输入线程之间没有共享对象看，其实严格来讲这次作业的调度器也可以放在输入线程里面，只是被我单独抽象了一个类出来，注意这里调度器并非一个线程，相当于一个缓冲的作用，下图展示了其中一个调度器控制两个电梯的结构

  ```mermaid
  graph LR
  A[InputThread]--> |输入请求|B{Dispatcher} -->|分发请求| C((RequestQueue)) -->|取出请求并处理|D[ElevatorThread1]
  C((RequestQueue)) -->|取出请求并处理|E[ElevatorThread2]
  ```
* **电梯框架：** 同上次，有电梯类和策略类，不过由于这次电梯有两种因此我建立了一个电梯接口，由 ``LongitudinalElevator``（即第一次作业的 ``Elevator``）、``CrosswiseElevator``两个电梯类继承它，而策略接口由 ``LongitudinalLookStrategy``（即第一次作业的 ``LookStrategy``）、``CrosswiseLookStrategy``实现它，此次在纵向电梯中我采取的是自由竞争策略，即同一栋楼的所有 ``Elevator``是该栋楼的所有纵向电梯共享，而横向电梯采取的是平均分配，即每个电梯有自己的 ``requestQueue``，就和第一次的纵向电梯一样，

  此次作业没有将电梯抽象为一个抽象类，让纵向和横向电梯去继承它，主要是 ``CheckStyle``不允许将变量设置为 ``protected``，子类只能使用一堆 ``get``、``set``去访问父类中的变量，同时我认为不同的电梯在行为的实现细节上或多或少会有差异，不过它们所具有的方法应该是一致的，而且使用接口在日后更容易去维护，添加或者删除方法，抽象类更倾向于充当公共类的角色，不适用于日后重新对里面的代码进行修改：

  ```java
  public interface Elevator {
  
      // 调用executeClose, exeOpen，到达一层后对请求做出处理
      void execute()
  
      // 调用调用电梯关键行为函数以及输出函数，用于封装电梯许多细碎的方法
      void executeClose();
  
      void executeOpen();
  
      // 以下为关键行为
      void pickUp();
  
      void dropOff();
  
      void moveTo();
  
      // 以下为5个输出函数
      void open();
  
      void close();
  
      void arrive();
  
      void in(PersonRequest request);
  
      void out(PersonRequest request);
  }
  
  ```

#### 同步块的设置和锁块的选择

基本锁块和同步块和上次完全一样，除了由于自由竞争导致纵向电梯处理接人时多加了锁：

```java
 @Override
    public void execute() {
        synchronized (requests) {
            if (strategy.whetherToOpen(inElevator, dir, curFloor)) {
                executeOpen();
            }
            dir = strategy.whichDir(inElevator, dir, curFloor);
            if (strategy.whetherToOpen(inElevator, dir, curFloor)) {
                executeOpen();
            }
        }
        if (isOpen) {
            executeClose();
        }
    }
```

主要是防止同一个人上两部电梯以及两部电梯同时开了门但是只有一部电梯上了人，而加了锁将处理开门这里变成同步块即同一时刻同一个位置只有一部电梯开门处理请求，当它处理完请求，另一部电梯的策略类才会给出建议，此时请求队列已经更新，因此不会出现上述问题

#### 性能

* **横向Look（记A、B、C、D、E座编号为0、1、2、3、4）**

  **乘客目的方向**为能够完成这个请求的最短路线所走的方向

  **当前方向**为电梯运行方向的第一部和第二部电梯，第三部和第四部电梯为反方向，相当于将环抽象为一条链，当前楼座位于链中间

  * **电梯运行方向：**
    * 当电梯中有乘客时：为电梯中乘客目的地的方向
    * 电梯中没有乘客时：仍然按照纵向的策略寻找，纵向的上限与下限为$10$和$1$，横向为$(now + 2) \% 5$和$(now+3)\%5$，查看当前方向上（包括当前层）是否有请求，如果有则电梯方向保持不变，反之则调转
  * **电梯接人策略：**
    * **如果当前电梯中没人且当前座外部没有当前同方向的请求但是有当前反方向的请求，那么电梯接人!!!**
    * 其它情况只接目的方向与电梯当前运行方向相同的外部乘客

  $tips:$注意**如果当前电梯中没人且当前座外部没有当前同方向的请求但是有当前反方向的请求，那么电梯接人**，如果我们看看如果我们发现当前电梯中没人且当前座外部没有当前同方向的请求但是有当前反方向的请求时不接人，而是像纵向电梯那样判断同方向有没有请求，如果有就跑去接的话就又可能造成死循环，比如：

  ```
  [2.0]1-FROM-A-1-TO-E-1
  [2.0]2-FROM-B-1-TO-A-1
  [2.0]3-FROM-C-1-TO-A-1
  [2.0]4-FROM-E-1-TO-C-1
  ```

  由于电梯刚开始是空且是顺时针方向，$1$号请求方向与电梯运行方向相反因此并不会接它，并且此时顺时针方向有请求$2$、$3$，所以它会往顺时针方向跑，但是由于到了$B$、$C$后发现请求其实是反方向的，因此它不接而会发现有$4$号请求，于是接着走，但是到了$E$发现$4$号也接不了，于是又。。。所以死循环了
* **纵向采取自由竞争策略**，即有人来了，所有电梯就去抢，看谁抢得到，**横向**由于是环形并且电梯速度较快距离短于是**采取的是平均分配**，事实证明横向用什么策略对于性能压根没什么影响

### 代码结构分析

#### 类图

![HW2.jpg](https://s2.loli.net/2022/04/27/NqHzyBJk5G7OLia.jpg)

#### 时序图

![diagram-6404967725643480425.png](https://s2.loli.net/2022/04/29/gduvxWb2hc4TJqM.png)

### 测试

此次由于第一次的经验教训，于是宿舍$3$个哥们儿一起写了评测机，当然最难的 ``spj``还是落到了码力最强的cjy头上，我和另一个舍友主要是负责数据生成，解析输入等等，投喂什么的可以使用官方包，我们写了$3$个数据生成器，一个是纯随机的一次生成$400$条左右进行测试，还有就是横向电梯和纵向电梯分别的压力测试（比如第一次强测那样$70s$来一堆，什么电梯刚走这层就来一堆请求这种。。。）

### 评测

关于此次强测互测我只想说呵呵。。。因为由于之前说的模仿助教的实验代码，我此次寄的透透的，互测都没进，喜提强测不及格，就一个 ``bug``，只要删除几个 ``notifyAll``就有$99$的强测分。。。

首先我们来看看助教的这个代码（``RequestQueue``里面全都 ``notifyAll``了）：

```java
// RequestQueue

// ...
    public synchronized boolean isEnd() {
        notifyAll();
        return isEnd;
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return requests.isEmpty();
    }
// ...
//还有其它方法也是各各都notifyAll了
```

而助教的生产者消费者模式中的框架基本就和我作业里判断逻辑一样，一个循环，如果队列是空就 ``wait``

```java
// Process类
	@Override
    public void run() {
        while (true) {
            if (processingQueue.isEnd() && processingQueue.isEmpty()) {
                System.out.println("P " + type + " over");
                return;
            }
            Request request = processingQueue.getOneRequest(); 
            if (request == null) continue;
        }
        //...
    }
// RequestQueue类
	public synchronized Request getOneRequest() {
        if (requests.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (requests.isEmpty()) {
            return null;
        }
        Request request = requests.get(0);
        requests.remove(0);
        notifyAll();
        return request;
    }
```

这种写法在只有一部电梯或者说一个消费者的情况下不会出错，但是有多个消费者呢？

首先假设这样一个场景（参考如下代码），就是有两部电梯共享同一个队列（当前队列里面没有请求），当恰好一部电梯进入循环拿到了 ``requestQueue``的锁之后（另外两部此时在 ``request.wait()``），它就会进入第二个 ``while()``块判断，而此时注意什么 ``isEmpty()``、``isEnd()``都是 ``requestQueue``里面的方法，都 ``notifyAll()``了，所以它唤醒了另一部电梯和输入线程，而它就进入了 ``wait``，另一部电梯从 ``wait``醒来还抢到了锁，于是它又进入 ``while``的判断于是又将第一部电梯唤醒，这样就可能进入死循环（输入线程一直抢不到锁），就算没有进入死循环都相当于在一直轮询（因为每次 ``isEmpty``、``isEnd``唤醒后输入线程和电梯线程就开始抢锁，经测试有些情况输入线程抢到锁的概率会很低，所以会循环挺长一段时间），于是只要将这里的两处 ``notifyAll()``删了就好，而考虑什么时候需要 ``notifyAll()``，其实只有两种情况一个是 ``addRequest``另一个是 ``setEnd``

```java
public void run() {
        while (!(requests.isEnd() && requests.isEmpty() && inElevator.isEmpty())) {
            synchronized (requests) {
                while (requests.isEmpty() && inElevator.isEmpty() && !requests.isEnd()) {
                    // 注意此处由于可能被唤醒后再抢到锁的时候请求已经被另一部电梯接走，所以要接着wait，即必须用while
                    // 不能再用if
                    try {
                        requests.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (requests.isEnd() && inElevator.isEmpty() && requests.isEmpty()) {
                    break;
                }
            }
            // do something
        }
    }
```

首先吧在第一次作业时候我考虑过助教的代码里面 ``notifyAll``加的不合适，就算只有一个生产者也不应该加如此多 ``notifyAll``开销太大了且没必要，但是由于当时刚学多线程不敢质疑助教代码于是基本照抄，到了第二次作业本着对于助教代码深信不疑的心理，压根没有检查 ``RequestQueue``这个类且评测机本地也没有跑出问题，主要是没有想到去单独检查一下 ``cpu``时间，于是这次就寄了。。。记得那天早上八点被舍友叫起来看了一眼平台，然后发现全宿舍都没进互测还以为是卡 ``bug``了，非常难受

## 第三次作业

### 作业思路

#### 架构

* **总框构：** 此次作业由于不像第二次作业，支持了请求的目的地，出发地，楼层，楼栋完全不相同，即支持了换乘，于是我加了一个总控制器 ``Controller``线程，它采取了单例模式，由它和输入线程以及 ``ElevatorThread``对接，共享一个 ``WAITING_QUEUE``，构成一个生产者（``InputThread``、``ElevatorThread``）消费者模式，然后 ``Controller``和 ``Dispatcher``对接，``Dispatcher``（像上次作业一样有$15$个）和 ``Elevator``同上次作业，相当于 ``Controller``和 ``Elevator``构成生产者消费者模式，``Dispatcher``缓冲，如下图：

  ```mermaid
  graph LR
  F[InputThread]-.->|输入请求|G((WAITING_QUEUE)) -.->|取出请求|A
  
  A[ControllerThread]--> |分配请求到dispatcher|B{Dispatcher1} -->|放请求| C((RequestQueue1)) -->|拿请求并处理|D[ElevatorThread1]
  C((RequestQueue)) -->|拿请求并处理|E[ElevatorThread2]
  
  A--> |分配请求到dispatcher|H{Dispatcher2} -->|放请求| I((RequestQueue2)) -->|拿请求并处理|J[ElevatorThread3]
  I((RequestQueue2)) -->|拿请求并处理|K[ElevatorThread4]
  
  D -.->|放回还要换乘的请求|G
  E -.->|放回还要换乘的请求|G
  J -.->|放回还要换乘的请求|G
  K -.->|放回还要换乘的请求|G
  ```
* **电梯框架：** 和上次一样没有什么改变，只在电梯接口中加了一个 ``default``方法，支持换乘：

  ```java
  default void transfer(PersonRequest request) {
          MyPersonRequest person = (MyPersonRequest) request;
          person.addCnt();
          if (!person.hasArrived()) {
              Controller.getInstance().getWaitingQueue().addRequest(person);
          } else {
              Controller.getInstance().addFinishNum();
          }
      }
  ```
* **调度器框架：** 调度器改为了一个调度器接口，由 ``LongitudinalDispatcher``、``CrosswiseDispatcher``实现它：

  ```java
  public interface Dispatcher {
      void addPersonRequest(PersonRequest request);
  
      void addElevatorRequest(ElevatorRequest request);
  
      void setEnd(boolean end);
  }
  ```
* **线程结束框架：** 此次作业我发现线程结束是一个难点，其实按照我上述框架可以很轻易解决这个问题，首先输入结束那么输入线程就结束了，然后它通知 ``Controller``自己结束了，当输入结束且 ``Controller``发现此时输入的总的请求等于完成的请求，那么它将调用每一个调度器的 ``setEnd``方法（就是将调度器控制的每个电梯的 ``requestQueue``结束，即通知电梯线程结束）去结束所有电梯线程，最后自己再结束

#### 同步块的设置和锁的选择

这次作业只有两处地方加了锁，都在 ``Controller``里面

* 类似电梯，经典的生产者消费者模型，之前已经解释过，但还是要再说两句，为了避免像上次一样 ``notifyAll``轮询，于是在 ``Controller``中采取 ``addRequest``就 ``requestQueue.notifyAll()``，当输入线程结束并且完成请求数与输入请求数相同就调用 ``requestQueue``的 ``setEnd``（该函数自带 ``notifyAll()``）去 ``notifyAll``，而不是每完成一个请求或者只有输入线程结束时都去 ``notifyAll``一遍：

  ```java
  @Override
      public void run() {
          currentThread().setName("Controller");
          while (!(WAITING_QUEUE.isEnd() && WAITING_QUEUE.isEmpty())) {
              synchronized (WAITING_QUEUE) {
                  while (WAITING_QUEUE.isEmpty() && !WAITING_QUEUE.isEnd()) {
                      try {
                          WAITING_QUEUE.wait();
                      } catch (InterruptedException e) {
                          throw new RuntimeException(e);
                      }
                  }
                  if (WAITING_QUEUE.isEnd() && WAITING_QUEUE.isEmpty()) {
                      break;
                  }
              }
             // do something
          }
          setDispatcherEnd(true);
      }
  ```
* 这处同步块要十分小心，当完成请求数加一时要锁起来，主要是如果有两步电梯同时完成了请求，那么就会出现经典的 ``finishNum``只加了一个$1$的情况：

  ```java
  public synchronized void addFinishNum() {
          finishNum += 1;
          waitingQueueSetEnd(true);
      }
  ```

#### 性能

还是横向平均分配，纵向自由竞争，换乘采取基准策略

* **输入时拆分请求**，即写一个 ``MyPerSonRequest``继承官方包里面的 ``PersonRequest``，同时记录换乘楼层，换乘次数和已经搭乘电梯的次数，在被完成一次电梯运送后自动改变自己的 ``FromFloor``、``FromBuilding``...（通过重写一系列 ``get``方法实现）
* **动态拆分请求**，这个优化我没有做，但是也好做，就是每来一部横向电梯，就将在 ``WAITING_QUEUE``中的请求全部重新获取一遍换乘楼层、换乘次数，清空已搭乘次数，主要是经历了前两次打击后佛系了，不想再搞性能了，事实证明也就一个点性能分要高几分，总分也就多个$0.2$左右，和前面丢的比起来无所谓了

### 代码结构分析

#### 类图

![HW3.jpg](https://s2.loli.net/2022/04/27/jmp5zKU4Xfdi8Rx.jpg)

#### 时序图

<img src="https://s2.loli.net/2022/04/29/ezat2iu5PR6HbS7.png" alt="diagram-12869497244338227479.png" style="zoom:150%;" />

### 测试

这次是我和cjy写评测机，当然他还是最难的 ``spj``，我就数据生成，输入解析，让所有代码耦合起来同时做一下校验

评测正确性什么的就不说了，经历了上次的打击，发现只有评测机也不行了，还得看看 ``cpu``时间，😔``oo``还是必须要做好完全的准备才可以，于是乎我用了助教推荐的 ``JProfiler``观测 ``cpu``时间，线程运行情况，放两张图给大家看看：

* 这次的作业：

  <img src="https://s2.loli.net/2022/04/26/rbpBIAGTSkXdZPW.png" alt="image.png" style="zoom: 33%;" />

  在上图中黄色代表等待的线程，绿色代表就绪线程，红色代表阻塞线程

  <img src="https://s2.loli.net/2022/04/26/an6iGQbO9cH4qge.png" alt="image.png" style="zoom: 33%;" />

  可以看到每个方法的 ``cpu``时间
* 上次 ``notifyAll``滥用的作业：

  <img src="https://s2.loli.net/2022/04/26/9moBG3eVXDalMPK.png" alt="image.png" style="zoom: 33%;" />

  可以看到阻塞线程被塞爆了，并且这还是只有十几个线程，而这次作业的图里面是开了五十多个线程的但是基本是没有阻塞的，阻塞线程其实就是一直在向 ``cpu``要锁，所以如果出现了上图的情况就很不正常

  <img src="https://s2.loli.net/2022/04/26/BkcxuJnZDHIYSoa.png" alt="image.png" style="zoom: 33%;" />

  同时可以看到方法占用 ``cpu``的时间，一看果然 ``notifyAll``具体就是 ``isEmpty``、``isEnd``这种不该 ``notifyAll``的方法，简直😶

### 评测

强测互测均未出 ``bug``，性能分基本拉满，终于回归了，感动。。。

就刀中一个 ``bug``，就是如果当前有个横向请求，但是没有对应可以到达的横向电梯，他直接把人加在了当前层的等待队列里面，于是该请求始终没有处理，其实只要加一条判断，让这个人换乘就好，不过助教这次也太善良了吧，$20$个强测点竟然一个这样的数据都没有，~~是我就起码让他像我上次那样寄个十几个点~~

## 心得体会与收获

* 总结一下这个单元其实代码难度以及架构选择难度并不大，基本第一遍写完就是最后交的版本（除了第一次代码里有一个明显的轮询没看出来，其实是当时甚至不知道轮询什么意思，也不知道这是个什么样的概念。。。）主要难在刚接触多线程什么都不知道，当然我这个单元也是够背的，看了有些同学也没注意 ``notifyAll``那个问题，都是仿照的助教实验代码，他挂了一两个点我挂了十几个点，还有就是第一次作业是真的离谱，想不到还有自己把自己优化 ``TLE``的，😶，唉，太伤心了
* 当然最大的体会就是多线程细节是真的多，哪里多锁了一点少锁了一点就可能有质的差别，同时在一定要好好留意什么时候 ``notifyAll``，并且不只是细节多，关键是还不好测出 ``bug``来，不像第一单元，我随便做一下小的单元测试，再用评测机压力测试个几百组就什么问题都没有了，**建议大家一定要评测机（大随机数据以及卡人的阴间数据），``JProfiler``一起用起来**，并且评测机一定要一直跑着，不要测个几组就停了，记得最后一次作业，我第一版有个 ``bug``就是方向判断太多次了，有极低概率会让电梯跑到$0$楼（这 ``bug``可能之前作业也有，不过就是测不出罢了🙂），其实只要加个在$1$楼只能方向向上，在$10$楼就只能向下的特判就解决一切问题，但是从第一次作业我就觉得这样不美观因此没做，事实证明强测测不出（我拿它在 ``bug``修复那里交了好几发都没事）并且估计互测大概率也测不出，毕竟在覆盖性极强的数据（每次$50$部电梯，几百条数据）下跑了快一个下午才有一组出了问题，所以综上多线程一定要小心，细心
* 这个月属实倒霉，当然也是自己学艺不精，不过总算过完第二单元了😫
