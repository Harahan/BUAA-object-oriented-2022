public class Lexer {
    private final String input; // 初始化后不变
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        this.next(); // 处理字符串开头（数字|符号）
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }

        return sb.toString();
    }

    public void next() { // 读掉当前位置符号或者数字
        if (pos == input.length()) {
            return;
        }

        char c = input.charAt(pos);
        if (Character.isDigit(c)) { 
            curToken = getNumber();
        } else if ("()+*".indexOf(c) != -1) {
            pos += 1;
            curToken = String.valueOf(c); // 返回字符串表示形式
        }
    }

    public String peek() {
        return this.curToken;
    }
}
