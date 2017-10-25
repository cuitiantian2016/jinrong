package com.honglu.future.widget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基本校验工具类
 * Created by tie on 2017/2/17.
 */

public class CheckUtils {

    /**
     * 校验手机号码是否匹配
     *
     * @param num 手机号码
     * @return true  成功
     */
    public static boolean checkPhoneNum(String num) {
        String pattern = "^1(3|4|5|7|8)\\d{9}";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(num);

        return m.matches();
    }
}
