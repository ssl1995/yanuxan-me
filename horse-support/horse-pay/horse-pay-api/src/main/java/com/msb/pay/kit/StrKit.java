package com.msb.pay.kit;

/**
 * 字符串工具类
 *
 * @author peng.xy
 * @date 2022/6/9
 */
public class StrKit {

    private StrKit() {

    }

    /**
     * 对字符加星号处理：除前面几位和后面几位外，其他的字符以星号代替
     *
     * @param content  传入的字符串
     * @param frontNum 保留前面字符的位数
     * @param endNum   保留后面字符的位数
     * @param starNum  指定star的数量
     * @return 带星号的字符串
     */
    public static String str2Star(String content, int frontNum, int endNum, int starNum) {
        if (frontNum >= content.length() || frontNum < 0) {
            return content;
        }
        if (endNum >= content.length() || endNum < 0) {
            return content;
        }
        if (frontNum + endNum >= content.length()) {
            return content;
        }
        String starStr = "";
        for (int i = 0; i < starNum; i++) {
            starStr = starStr + "*";
        }
        return content.substring(0, frontNum) + starStr
                + content.substring(content.length() - endNum);
    }

}
