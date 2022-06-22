package com.csp.lib.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日志打印器 - java
 * Created by csp on 2022/06/23
 * Modified by csp on 2022/06/23
 *
 * @author csp
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class LoggerJava implements ILog {

    private final static DateFormat DATE_FORMAT;

    static {
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault());
    }

    /**
     * 日志开关
     * true：打印所有日志
     * false：除了非 debug 级别的异常日志，其余日志都不打印
     */
    private boolean mDebug;

    public LoggerJava(boolean debug) {
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
     * 打印日志，格式：yyyy-MM-dd HH:mm:ss:SSS [error]tag: message \n throwable
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
        String levelStr;
        boolean isError = false;
        switch (level) {
            case LogCat.ASSERT:
                levelStr = "assert";
                isError = true;
                break;
            case LogCat.ERROR:
                levelStr = "error";
                isError = true;
                break;
            case LogCat.WARN:
                levelStr = "warn";
                break;
            case LogCat.INFO:
                levelStr = "info";
                break;
            case LogCat.DEBUG:
                levelStr = "debug";
                break;
            case LogCat.VERBOSE:
            default:
                levelStr = "verbose";
                break;
        }
        String dateTime = DATE_FORMAT.format(new Date());
        String stackTrace = LogCat.getStackTraceString(throwable);
        String newline = message.isEmpty() || stackTrace.isEmpty() ? "" : "\n";
        message = dateTime + " [" + levelStr + "]" + tag + ": " +
                message + newline + stackTrace;

        if (isError) {
            System.err.println(message);
        } else {
            System.out.println(message);
        }
    }
}
