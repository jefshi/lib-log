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
     * Android 能够打印的最大日志长度
     */
    private final static int LOG_MAX_LENGTH = 3072;

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
    private String[] divideMessages(@NonNull String message) {
        if (message.length() <= LOG_MAX_LENGTH) {
            return new String[]{message};
        }

        String part;
        int index;
        List<String> list = new ArrayList<>();
        while (true) {
            if (message.length() <= LOG_MAX_LENGTH) {
                list.add(message);
                break;
            }

            part = message.substring(0, LOG_MAX_LENGTH);
            index = part.lastIndexOf('\n');
            if (index > -1) {
                part = message.substring(0, index + 1);
            }
            list.add(part);
            message = message.substring(part.length());
        }

        String[] strings = new String[list.size()];
        return list.toArray(strings);
    }
}
