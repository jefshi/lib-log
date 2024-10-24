package com.csp.lib.log;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志打印器 - Android
 * Created by csp on 2017/07/14
 * Modified by csp on 2021/08/10
 *
 * @author csp
 * @version 1.0.7
 */
@SuppressWarnings("unused")
public class LoggerAndroid implements ILog {

    /**
     * Android 能够打印的最大日志长度（单位 byte）：
     * 1. 经测试最多可输出 4043 byte，故最多可输出 4043 个纯字母，或 1347 个纯汉字（一个汉字 3 byte）
     */
    private final static int LOG_MAX_LENGTH = 4001;

    /**
     * 日志开关
     * true：打印所有日志
     * false：除了非 debug 级别的异常日志，其余日志都不打印
     */
    private boolean mDebug;

    public LoggerAndroid(boolean debug) {
        mDebug = debug;
    }

    public void setDebug(boolean debug) {
        mDebug = debug;
    }

    @Override
    public String toString(Object obj) {
        return String.valueOf(obj);
    }

    @Override
    public void log(int level, String invoke, String log) {
        if (mDebug) {
            printLog(level, invoke, log, null);
        }
    }

    @Override
    public void printStackTrace(boolean onlyDebug, int level, String invoke, String explain, Throwable throwable) {
        boolean debugNotPrint = onlyDebug && !mDebug;
        boolean hasLog = throwable != null || explain != null;
        if (hasLog && !debugNotPrint) {
            printLog(level, invoke, explain, throwable);
        }
    }

    /**
     * 打印日志
     *
     * @param level     日志等级
     * @param tag       日志标签
     * @param message   日志内容
     * @param throwable 异常
     */
    private void printLog(int level, String tag, String message, Throwable throwable) {
        if (message == null) {
            message = "";
        }
        String stackTrace = LogCat.getStackTraceString(throwable);
        String newline = message.isEmpty() || stackTrace.isEmpty() ? "" : "\n";
        String[] messages = divideMessages(message + newline + stackTrace);
        for (String msg : messages) {
            switch (level) {
                case LogCat.ASSERT:
                case LogCat.ERROR:
                    Log.e(tag, msg);
                    break;
                case LogCat.WARN:
                    Log.w(tag, msg);
                    break;
                case LogCat.INFO:
                    Log.i(tag, msg);
                    break;
                case LogCat.DEBUG:
                    Log.d(tag, msg);
                    break;
                case LogCat.VERBOSE:
                default:
                    Log.v(tag, msg);
                    break;
            }
        }
    }

    /**
     * 日志内容分割
     *
     * @param message 日志内容
     * @return 分割后的日志
     */
    private static String[] divideMessages(@NonNull String message) {
        if (message.getBytes().length <= LOG_MAX_LENGTH) {
            return new String[]{message};
        }

        int index;
        String part;
        List<String> list = new ArrayList<>();
        while (message.getBytes().length > LOG_MAX_LENGTH) {
            part = message.length() <= LOG_MAX_LENGTH ? message : message.substring(0, LOG_MAX_LENGTH);
            do {
                index = part.lastIndexOf('\n');
                if (index > -1 && index != part.length() - 1) {
                    part = part.substring(0, index + 1);
                } else if (part.getBytes().length > LOG_MAX_LENGTH) {
                    part = subPart(part, 0, part.length());
                }
            } while (part.getBytes().length > LOG_MAX_LENGTH);

            list.add(part);
            message = message.substring(part.length());
        }
        if (!message.isEmpty()) {
            list.add(message);
        }

        String[] strings = new String[list.size()];
        return list.toArray(strings);
    }

    @NonNull
    private static String subPart(String str, int small, int big) {
        int index = (small + big) >> 1;
        if (index == small) {
            return str.substring(0, small);
        }
        String sub = str.substring(0, index);
        if (sub.getBytes().length > LOG_MAX_LENGTH) {
            return subPart(str, small, index);
        } else {
            return subPart(str, index, big);
        }
    }
}
