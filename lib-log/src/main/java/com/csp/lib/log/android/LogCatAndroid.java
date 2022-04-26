package com.csp.lib.log.android;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.csp.lib.log.core.ILog;
import com.csp.lib.log.core.LogCat;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志打印
 * Created by csp on 2017/07/14
 * Modified by csp on 2021/08/10
 *
 * @author csp
 * @version 1.0.7
 */
@SuppressWarnings("unused")
public class LogCatAndroid implements ILog {

    /**
     * Android 能够打印的最大日志长度
     */
    private final static int LOG_MAX_LENGTH = 3072;

    /**
     * 日志开关，true：可以打印日志
     */
    private boolean mDebug;

    public LogCatAndroid(boolean debug) {
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
        String stackTrace = getStackTraceString(throwable);
        String newline = message.isEmpty() || stackTrace.isEmpty() ? "" : "\n";
        String[] messages = divideMessages(message + newline + stackTrace);
        for (String msg : messages) {
            switch (level) {
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
                default:
                    Log.v(tag, msg);
                    break;
            }
        }
    }

    /**
     * 获取异常栈信息
     *
     * @param throwable 异常错误对象
     * @return 异常栈信息
     */
    @NonNull
    public static String getStackTraceString(@Nullable Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        pw.flush();
        String result = sw.toString();
        try {
            sw.close();
            pw.close();
        } catch (IOException e) {
            LogCat.printStackTraceForDebug(e);
        }
        return result;
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
