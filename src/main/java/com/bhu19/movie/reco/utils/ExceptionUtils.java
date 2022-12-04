package com.bhu19.movie.reco.utils;

import java.util.Objects;

/**
 * @author: luoyuanfeng@meituan.com
 * @date: 2022/12/4 5:34 下午
 * @description:
 */
public class ExceptionUtils {

    private ExceptionUtils() {

    }

    // 解析异常栈
    public static String parseException(Exception e) {
        if (Objects.isNull(e)) {
            return "";
        }
        StringBuilder cause = new StringBuilder();
        cause.append(e.getClass().getName()).append(":").append(e.getMessage()).append("\n");
        for (StackTraceElement ele : e.getStackTrace()) {
            if (ele.getClassName().contains("bhu19")) {
                String stack = ele.getClassName() + "#" + ele.getMethodName() + "#" + ele.getLineNumber();
                cause.append(stack).append("\n");
                break;
            }
        }
        return cause.toString();
    }
}
