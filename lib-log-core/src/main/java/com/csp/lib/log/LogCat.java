package com.csp.lib.log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * 日志打印工具
 * Created by csp on 2017/07/14
 * Modified by csp on 2021/08/10
 *
 * @author csp
 * @version 1.0.7
 */
@SuppressWarnings("unused")
public class LogCat {

    /**
     * 日志等级，抄录自安卓的 android.util.Log
     */
    public final static int VERBOSE = 2;
    public final static int DEBUG = 3;
    public final static int INFO = 4;
    public final static int WARN = 5;
    public final static int ERROR = 6;
    public final static int ASSERT = 7;

    /**
     * 占位符，{@link String#format}
     */
    private final static String PLACEHOLDER = "%s";

    /**
     * 默认调用栈序号, 用于获取本类对外 API 调用位置
     */
    public final static int DEFAULT_STACK_ID = 3;

    /**
     * 日志打印器 - 单例
     */
    private static ILog sLogger;

    public static void setLogger(ILog logger) {
        sLogger = logger;
    }

    public static ILog getLogger() {
        return sLogger;
    }

    /**
     * 调用栈信息, 例: (类名.方法名)
     *
     * @param element 追踪栈元素
     * @return 日志标签
     */
    private static String getInvoke(StackTraceElement element) {
        String className = element.getClassName();
        String methodName = element.getMethodName();
        String simpleClassName = className.substring(className.lastIndexOf('.') + 1);
        return "(" + simpleClassName + "." + methodName + ')';
    }

    /**
     * @see ILog#printStackTrace(boolean, int, String, String, Throwable)
     */
    public static void printStackTrace(boolean onlyDebug, int level, int stackId, String explain, Throwable throwable) {
        if (sLogger != null) {
            // 调用栈信息
            StackTraceElement[] stackTrace = new Exception().getStackTrace();
            String tag = getInvoke(stackTrace[stackId]);

            sLogger.printStackTrace(onlyDebug, level, tag, explain, throwable);
        } else if (!onlyDebug && throwable != null) {
            throwable.printStackTrace();
        }
    }

    /**
     * @see #printStackTrace(boolean, int, int, String, Throwable)
     */
    private static void printStackTrace(boolean onlyDebug, int level, String explain, Throwable throwable) {
        printStackTrace(onlyDebug, level, DEFAULT_STACK_ID, explain, throwable);
    }

    /**
     * @see #printStackTrace(boolean, int, String, Throwable)
     */
    public static void printStackTrace(int level, String explain, Throwable throwable) {
        printStackTrace(false, level, explain, throwable);
    }

    /**
     * @see #printStackTrace(int, String, Throwable)
     */
    public static void printStackTrace(String explain, Throwable throwable) {
        printStackTrace(false, ERROR, explain, throwable);
    }

    /**
     * @see #printStackTrace(int, String, Throwable)
     */
    public static void printStackTrace(Throwable throwable) {
        printStackTrace(false, ERROR, null, throwable);
    }

    /**
     * @see #printStackTrace(boolean, int, String, Throwable)
     */
    public static void printStackTraceForDebug(int level, String explain, Throwable throwable) {
        printStackTrace(true, level, explain, throwable);
    }

    /**
     * @see #printStackTraceForDebug(int, String, Throwable)
     */
    public static void printStackTraceForDebug(String explain, Throwable throwable) {
        printStackTrace(true, ERROR, explain, throwable);
    }

    /**
     * @see #printStackTraceForDebug(int, String, Throwable)
     */
    public static void printStackTraceForDebug(Throwable throwable) {
        printStackTrace(true, ERROR, null, throwable);
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
        String result = "";
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            pw.flush();
            result = sw.toString();
        } catch (IOException e) {
            LogCat.printStackTraceForDebug(e);
        }
        return result;
    }

    /**
     * 普通日志打印
     *
     * @param level            日志等级，{@link LogCat#ERROR}
     * @param invokeStackIndex 调用栈索引，用于日志打印位置（调用栈）信息
     * @param explain          {@link String#format(String, Object...)}
     * @param messages         {@link String#format(String, Object...)}
     * @see ILog#log(int, String, String)
     */
    public static void log(int level, int invokeStackIndex, String explain, Object... messages) {
        if (sLogger == null) {
            return;
        }

        // 将任意对象转换成 String
        if (messages != null) {
            for (int i = 0; i < messages.length; i++) {
                messages[i] = sLogger.toString(messages[i]);
            }
        }

        // 是否使用占位符
        String log;
        explain = explain == null ? "" : explain;
        if (!explain.contains(PLACEHOLDER)) {
            log = explain.isEmpty() ? "" : explain + "：";
            log += messages == null ? "null"
                    : messages.length == 1 ? String.valueOf(messages[0])
                    : Arrays.toString(messages);
        } else {
            log = messages == null ? explain : String.format(explain, messages);
        }

        // 调用栈信息
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        String tag = getInvoke(stackTrace[invokeStackIndex]);

        sLogger.log(level, tag, log);
    }

    /**
     * @see #log(int, int, String, Object...)
     */
    private static void log(int level, String explain, Object... messages) {
        log(level, DEFAULT_STACK_ID, explain, messages);
    }

    /**
     * @see #log(int, String, Object...)
     */
    public static void e(String explain, Object... messages) {
        log(ERROR, explain, messages);
    }

    /**
     * @see #log(int, String, Object...)
     */
    public static void e(Object message) {
        log(ERROR, null, message);
    }

    /**
     * @see #log(int, String, Object...)
     */
    public static void w(String explain, Object... messages) {
        log(WARN, explain, messages);
    }

    /**
     * @see #log(int, String, Object...)
     */
    public static void w(Object message) {
        log(WARN, null, message);
    }

    /**
     * @see #log(int, String, Object...)
     */
    public static void i(String explain, Object... messages) {
        log(INFO, explain, messages);
    }

    /**
     * @see #log(int, String, Object...)
     */
    public static void i(Object message) {
        log(INFO, null, message);
    }

    /**
     * @see #log(int, String, Object...)
     */
    public static void d(String explain, Object... messages) {
        log(DEBUG, explain, messages);
    }

    /**
     * @see #log(int, String, Object...)
     */
    public static void d(Object message) {
        log(DEBUG, null, message);
    }

    /**
     * @see #log(int, String, Object...)
     */
    public static void v(String explain, Object... messages) {
        log(VERBOSE, explain, messages);
    }

    /**
     * @see #log(int, String, Object...)
     */
    public static void v(Object message) {
        log(VERBOSE, null, message);
    }
}
