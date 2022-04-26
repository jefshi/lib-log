package com.csp.lib.log.core;

/**
 * 日志打印接口
 * Created by csp on 2022/04/25
 * Modified by csp on 2022/04/25
 *
 * @author csp
 * @version 1.0.0
 */
public interface ILog {

    /**
     * 将任意对象（含基本类型），转换成字符串，以便打印类对象日志
     *
     * @param obj 任意对象，允许为 null
     * @return 字符串
     */
    String toString(Object obj);

    /**
     * 普通日志打印
     *
     * @param level  日志等级，{@link LogCat#ERROR}
     * @param invoke 日志打印位置（调用栈）信息
     * @param log    日志
     */
    void log(int level, String invoke, String log);

    /**
     * 打印异常信息
     *
     * @param onlyDebug true：仅 debug 模式打印异常信息
     * @param level     日志等级
     * @param invoke    日志打印位置（调用栈）信息
     * @param explain   异常说明，{@link String#format}
     * @param throwable 异常错误对象
     */
    void printStackTrace(boolean onlyDebug, int level, String invoke, String explain, Throwable throwable);
}
