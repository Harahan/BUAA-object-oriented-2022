# 学习Git

[TOC]



## 简单版本管理

* 全局设置：

  ```
  git config --global user.name "xxx"
  git config --global user.email "xxxxx"
  ```

  如果希望在一个特定的项目中使用不同的名称或`e-mail`地址，可以在该项目中运行该命令而不要`--global`选项

* 建立``git``管理仓库：

  ```
  git init
  ```

  将更改提交到缓冲区：

  ```
  git add <file>
  ```

  将缓冲区导入版本库中：

  ```
  git commit -m "xxxx"
  ```

  查看当前缓冲区和工作区文件状态：

  ```
  git status
  ```

  比较文件状态：

  ```
  git diff
  ```

  当工作区有改变，缓冲区为空就是比较工作区与最后一次``commit``提交的文件；当工作区有改变，缓冲区不为空就是比较工作区与缓冲区的文件，当然也可以比较工作区与指定版本或是当前版本差异：

  ```
  git diff <commit_id>
  ```

  ```
  git diff HEAD
  ```

  比较缓冲区与版本库也可以：

  ```
  git diff --cached <commit_id>
  ```

  

* 查询版本历史记录，当前的排上方，可以加上参数``pretty=oneline``：

  ```
  git log <file>
  ```

  查看每一条记录：

  ```
  git reflog <file>
  ```

* 将缓冲区恢复到当前版本库指向的版本即默认参数(--mixed)：

  ```
  git reset <file>
  ```

  将工作区与缓冲区都恢复到当前版本库指向的版本：

  ```
  git reset --hard HEAD <file>(或者是<commit_id>的一部分也可以)
  ```

  上一个版本是``^HEAD``或者``HEAD~100``

  ``git reset``配合不同的参数，对这三个区域会产生不同的影响。

  ``reset``实际上有3个步骤，根据不同的参数可以决定执行到哪个步骤(`--soft`, `--mixed`, `--hard`)。

  1. 改变``HEAD``所指向的``commit``(`--soft`)
  2. 执行第1步，将``Index``区域更新为``HEAD``所指向的``commit``里包含的内容(`--mixed`)
  3. 执行第1、2步，将``Working Directory``区域更新为``HEAD``所指向的``commit``里包含的内容(`--hard`)

  上面讲到的``git reset``实际上不带参数的，如果带上文件参数，那么效果会是怎样的？

  1. ``HEAD``不会动
  2. 将那个``commit``的``snapshot``里的那个文件放到Index区域中

  需要注意的是带文件参数的git reset没有--hard, --soft这两个参数。只有--mixed参数。

* 丢弃工作区的修改（让它回到最近一次``git commit``或者``git add``状态）比如工作区一不小心删了：

  ```
  git checkout <file>
  ```

  ## checkout

  前面讲到checkout是会修改HEAD的指向，变更Index区域里的内容，修改Working Directory里的内容。

  这看上去很像`reset --hard`，但和`reset --hard`相比有两个重要的差别

  1. reset会把working directory里的所有内容都更新掉
  2. checkout不会去修改你在Working Directory里修改过的文件
  3. reset把branch移动到HEAD指向的地方
  4. checkout则把HEAD移动到另一个分支

  第二个区别可能有点难以理解，举例来说：假设你有两个分支master和develop，这两个分支指向不一样的commit，我们现在在develop分支上（HEAD指向的地方）

  如果我们`git reset master`，那么develop就会指向master所指向的那个commit。

  如果我们`git checkout master`，那么develop不会动，只有HEAD会移动。HEAD会指向master。看图：

  ![图片描述](https://segmentfault.com/img/bVz7pB?w=500&h=366)

  ### 带文件参数

  当执行git checkout [branch] file时，checkout干了这件事情：

  1. 更新了index区域里file文件的内容
  2. 更新了working directory里file文件的内容

  ## 总结reset和checkout

* 先手动删除文件，然后从版本库删除文件：

  ```
  git rm <file>
  git commit -m "remove <file>"
  ```


* 将缓冲区文件从缓冲区撤出：

  ```
  git restore --staged <file_name>
  ```

  

* 将工作区存在但不在缓冲区存在的内容撤销：

  ```
  git restore <file_name>
  ```

  

## 远程仓库

* 创建``SSH Key``，之前没建：

  ```
  ssh-keygen -t rsa -C "xxxx@example.com"
  ```

  **一路回车**就好，``id_rsa``是私钥，``id_rsa.pub``是公钥，在``GitHub``或者``GitLab``账号里面``Key``文本框下黏贴``id_rsa.pub``内容即可

  如果有两个账号就要配两个密钥（第二个）：

  ```
  ssh -keygen -t rsa -C "xxx@example.com"
  ```

  注意是**不要一路回车**，在第一个问题处取个名字``id_rsa_xxx``之类，否则会覆盖之前的

  然后如果没有``config``文件，就要在``~/.ssh``目录下创建``config``文件：

  ```
  # gitlab
  Host gitlab.oo.buaa.edu.cn
  	HostName gitlab.oo.buaa.edu.cn
  	User git
  	IdentityFile ~/.ssh/id_rsa
  	IdentitiesOnly yes
  # githab
  Host github.com
  	HostName github.com
  	User git
  	IdentityFile ~/.ssh/id_rsa_Harahan_Github
  	IdentitiesOnly yes
  ```

  然后是测试：

  ```
  ssh -T git@xxx.github.com
  ```

  

* 先有本地库，添加远程仓库，登录``GitHub``，新建一个仓库，可以根据提示在本地仓库下运行：

  ```
  git remote add origin git@github.com:path/repo_name.git
  ```

  之后远程库名字就是``origin``了，然后就是将本地库的东西推到``GitHub``上面：

  ```
  git push -u origin master
  ```

  加入``-u``使本地和远程关联起来，下一次就不用``-u``了

* 如果添加的时候地址写错或者想删除远程库（指删除本地库与远程库绑定关系，并不是真正的删除）：

  ```
  git remote rm <name>
  ```

  但建议先用：

  ```
  git remote -v
  ```

  查看远程库信息

* 先有远程库，那么就用，感觉它和``git remot add``之间的区别就是它将仓库拷到了自己的电脑上（都会添加远端主机）：

  ```
  git clone git@github.com:path/repo_name.git
  ```

  

## 分支管理

* 创建并切换分支：

  ```
  git checkout -b <branch_name>
  或者
  git switch -c <branch_name>
  ```

  相当于以下两条命令（第一条是创建，第二条是切换）：

  ```
  git branch <branch_name>
  git checkout <branch_name> 或者 git switch <branch_name>
  ```

* 查看当前分支：

  ```
  git branch
  ```

* 合并指定分支到当前分支

  ```
  git merge <branch_name>
  ```

* 删除指定分支：

  ```
  git branch -d <branch_name>
  ```

  

* 合并分支时，如果可能，``Git``会采用``Fast forward``模式，但在这种模式下，删除分支后会丢掉分支信息，可以强制禁用``Fast forward``模式，``Git``就会在``merge``时生成一个新的``commit``，这样，从分支历史上可以看到分支信息

  ```
  git merge --no-ff -m "xxx" <branch_name>
  ```

* 将当前工作现场储存起来（如果当前不能提交，如果不用在这个分支新建一个文件``git add``后切换到另一个分支修复``bug``时也会看到当前的新文件，就无法分开它是属于哪里的）：

  ```
  git stash
  ```

  查看：

  ```
  git stash list
  ```

  恢复：

  ```
  git stash aplly
  git stash drop
  ```

  或者：

  ```
  git stash pop
  ```

  注意可以用``git stash list``查看

* 复制一个特定的提交到当前分支：

  ```
  git cherry-pick <version_name>
  ```

* 如果想强制删除未合并分支可以用``-D``

* 如果推送失败，因为你的小伙伴的最新提交和你试图推送的提交有冲突，解决办法也很简单，``Git``已经提示我们，先用`git pull`把最新的提交从`origin/dev`抓下来，然后，在本地合并，解决冲突，再推送

  注意：``git diff``：是查看``working tree``与``index file``的差别的。
  ``git diff --cached``：是查看``index file``与``commit``的差别的。
  ``git diff HEAD``：是查看``working tree``和``commit``的差别的，在这里``HEAD``代表的是最近的一次``commit``的信息。

* ``rebase``操作可以把本地未``push``的分叉提交历史整理成直线；``rebase``的目的是使得我们在查看历史提交的变化时更容易，因为分叉的提交需要三方对比。不过建议不要用

## 标签管理

* 创建标签（可以对给定的某次提交）：

  ```
  git tag <name> <commit>
  ```

  查看：

  ```
  git tag
  ```

* 可以用``-a``指定签名，``-m``指定文字说明，标签总是和某个``commit``挂钩。如果这个``commit``既出现在``master``分支，又出现在``dev``分支，那么在这两个分支上都可以看到这个标签

* 如果标签打错了，也可以删除：

  ```
  git tag -d <name>
  ```

  

* 因为创建的标签都只存储在本地，不会自动推送到远程。所以，打错的标签可以在本地安全删除。如果要推送某个标签到远程，使用命令`git push origin <tagname>`，或者，一次性推送全部尚未推送到远程的本地标签``git push origin --tags``

*  如果标签已经推送到远程，要删除远程标签就麻烦一点，先从本地删除，然后，从远程删除。删除命令也是``push``，但是格式如下：

  ```
  git push origin :refs/tags/<name>
  ```

## 自定义Git

* 让``Git``显示颜色：

  ```
  git config --global color.ui true
  ```

  

* 忽略特殊文件，有些时候，你必须把某些文件放到Git工作目录中，但又不能提交它们，比如保存了数据库密码的配置文件啦，等等，每次`git status`都会显示`Untracked files ...`，有强迫症的童鞋心里肯定不爽。好在Git考虑到了大家的感受，这个问题解决起来也很简单，在Git工作区的根目录下创建一个特殊的`.gitignore`文件，然后把要忽略的文件名填进去，Git就会自动忽略这些文件。不需要从头写`.gitignore`文件，GitHub已经为我们准备了各种配置文件，只需要组合一下就可以使用了。

  最后一步就是把`.gitignore`也提交到Git，就完成了！当然检验`.gitignore`的标准是`git status`命令是不是说`working directory clean`。

  使用``Windows``的童鞋注意了，如果你在资源管理器里新建一个`.gitignore`文件，它会非常弱智地提示你必须输入文件名，但是在文本编辑器里“保存”或者“另存为”就可以把文件保存为`.gitignore`了。

  有些时候，你想添加一个文件到Git，但发现添加不了，原因是这个文件被`.gitignore`忽略了，如果你确实想添加该文件，可以用`-f`强制添加到``Git``

  或者你发现，可能是`.gitignore`写得有问题，需要找出来到底哪个规则写错了，可以用`git check-ignore`命令检查：

  ```
  $ git check-ignore -v App.class
  .gitignore:3:*.class	App.class
  ```

  Git会告诉我们，`.gitignore`的第3行规则忽略了该文件，于是我们就可以知道应该修订哪个规则。

  还有些时候，当我们编写了规则排除了部分文件时：

  还有些时候，当我们编写了规则排除了部分文件时：

  ```
  # 排除所有.开头的隐藏文件:
  .*
  # 排除所有.class文件:
  *.class
  ```

  但是我们发现`.*`这个规则把`.gitignore`也排除了，并且`App.class`需要被添加到版本库，但是被`*.class`规则排除了。

  虽然可以用`git add -f`强制添加进去，但有强迫症的童鞋还是希望不要破坏`.gitignore`规则，这个时候，可以添加两条例外规则：

  ```
  # 排除所有.开头的隐藏文件:
  .*
  # 排除所有.class文件:
  *.class
  
  # 不排除.gitignore和App.class:
  !.gitignore
  !App.class
  ```

  把指定文件排除在`.gitignore`规则外的写法就是`!`+文件名，所以，只需把例外文件添加进去即可。
