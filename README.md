# 轻量级日志打印工具

## 前言

定位与说明
- 定位：工具库
- core：java 库，日志工具以及 java 版日志打印器
- android：针对 Android 的日志打印器
- 轻量说明：core 就 3 个类，android 就 1 个类，2个库加起来不到 8K

使用说明：
1. 需要完成一次初始化，否则默认仅打印异常日志，具体见章节【库的基本用法】
2. 会在 tag 中打印日志调用类与方法信息，同时可以通过调整调用栈索引，修改调用信息，具体见章节【库的基本用法】
3. 本库自带的 Sample 将对象打印的输出方式重写为 Json 格式，具体参考章节【库的基本用法】

## 集成步骤

Android 集成

``` gradle
implementation 'io.github.jefshi:lib-log-core:1.0.0'
implementation 'io.github.jefshi:lib-log-android:1.0.1'
```

Java 集成

``` gradle
implementation 'androidx.annotation:annotation:1.2.0'
implementation 'io.github.jefshi:lib-log-core:1.0.0'
```

目前仅发布到 Maven Central 仓库

``` gradle
repositories {
    mavenCentral()
}
```

### 库的基本用法

初始化日志
- 未初始化：仅打印异常日志
- 通过 `LogCat#setLogger(ILog)` 初始化，如：
	- Java + obj.toString() 方式：`LogCat.setLogger(new LoggerJava(true))`
	- Java + Json 方式：参考【Android + Json】
	- Android + obj.toString() 方式：`LogCat.setLogger(new LoggerAndroid(BuildConfig.DEBUG))`
	- Android + Json 方式

``` java
LogCat.setLogger(new LoggerAndroid(BuildConfig.DEBUG) {
    @Override
    public String toString(Object obj) {
        // 如果未重写 toString() 则用 Json 格式转换为字符串，否则使用 toString() 转换
        String log = String.valueOf(obj);
        try {
            boolean notOverwriteToString = log.contains("@");
            if (notOverwriteToString) {
                // 纯 String 用 Json 会导致 \n 失效，所以要额外处理
                if (obj instanceof String) {
                    log = obj + "";
                } else {
                    log = GsonUtil.getGson().toJson(obj);
                }
            }
        } catch (Exception ignored) {
        }
        return log;
    }
});
```

日志打印

``` java
LogCat.e("\n========== 使用说明 ==========");
LogCat.e("1. 需要完成一次初始化，否则默认仅打印异常日志，初始化见 Application");
LogCat.e("2. 会在 tag 中打印日志调用类与方法信息，同时可以通过调整调用栈索引，修改调用信息");
LogCat.e("3. 本 Sample 将对象打印的输出方式重写为 Json 格式（如果对象没有重写 toString 方法的话）");

LogCat.w("\n========== 不带占位符 ==========");
LogCat.w("string");
LogCat.w(true);
LogCat.w('c');
LogCat.w(1.2);
LogCat.w("数组", "string", true, 'c', 1.2);
LogCat.w("集合", mList);
LogCat.w("任意对象（未重写 toString()）", new Bean());
LogCat.w("任意对象（已重写 toString()）", Environment.getRootDirectory());

LogCat.e("\n========== 带占位符 ==========");
LogCat.e("String = %s，Boolean = %s，Char = %s，Double = %s", "string", true, 'c', 1.2);
LogCat.e("Bean = %s\n%s\n%s", new Bean(), new Bean(), new Bean());

LogCat.e("\n========== 异常 ==========");
LogCat.printStackTrace(new Exception("此处发生异常"));
LogCat.printStackTrace(LogCat.INFO, "此处发生异常", new Exception("此处发生异常"));
LogCat.printStackTraceForDebug(LogCat.INFO, "仅 debug 模式打印异常", new Exception("仅 debug 模式打印异常"));

LogCat.e("\n========== 调整调用栈索引 ==========");
LogCat.log(LogCat.WARN, 4, "调整日志打印调用栈索引，注意 tag");
LogCat.printStackTrace(false, LogCat.WARN, 4, null, new Exception("异常：调整日志打印调用栈索引，注意 tag"));
```

日志打印结果（Android + Json）

```
2022-06-23 10:11:15.118 23339-23339/com.csp.sample.log E/(MainActivity.onClick): ========== 使用说明 ==========
2022-06-23 10:11:15.119 23339-23339/com.csp.sample.log E/(MainActivity.onClick): 1. 需要完成一次初始化，否则默认仅打印异常日志，初始化见 Application
2022-06-23 10:11:15.119 23339-23339/com.csp.sample.log E/(MainActivity.onClick): 2. 会在 tag 中打印日志调用类与方法信息，同时可以通过调整调用栈索引，修改调用信息
2022-06-23 10:11:15.119 23339-23339/com.csp.sample.log E/(MainActivity.onClick): 3. 本 Sample 将对象打印的输出方式重写为：未重写 toString() 时使用 Json 格式，否则使用 toString()
2022-06-23 10:11:15.119 23339-23339/com.csp.sample.log W/(MainActivity.onClick): ========== 不带占位符 ==========
2022-06-23 10:11:15.119 23339-23339/com.csp.sample.log W/(MainActivity.onClick): string
2022-06-23 10:11:15.119 23339-23339/com.csp.sample.log W/(MainActivity.onClick): true
2022-06-23 10:11:15.119 23339-23339/com.csp.sample.log W/(MainActivity.onClick): c
2022-06-23 10:11:15.119 23339-23339/com.csp.sample.log W/(MainActivity.onClick): 1.2
2022-06-23 10:11:15.120 23339-23339/com.csp.sample.log W/(MainActivity.onClick): 数组：[string, true, c, 1.2]
2022-06-23 10:11:15.120 23339-23339/com.csp.sample.log W/(MainActivity.onClick): 集合：[aaa, bbb, ccc]
2022-06-23 10:11:15.135 23339-23339/com.csp.sample.log W/(MainActivity.onClick): 任意对象（未重写 toString()）：{"mBoolean":true,"mChar":"t","mDouble":1.6,"mLong":1,"mString":"string"}
2022-06-23 10:11:15.135 23339-23339/com.csp.sample.log W/(MainActivity.onClick): 任意对象（已重写 toString()）：Thread[main,5,main]
2022-06-23 10:11:15.135 23339-23339/com.csp.sample.log E/(MainActivity.onClick): ========== 带占位符 ==========
2022-06-23 10:11:15.135 23339-23339/com.csp.sample.log E/(MainActivity.onClick): String = string，Boolean = true，Char = c，Double = 1.2
2022-06-23 10:11:15.136 23339-23339/com.csp.sample.log E/(MainActivity.onClick): Bean = {"mBoolean":true,"mChar":"t","mDouble":1.6,"mLong":1,"mString":"string"}
    {"mBoolean":true,"mChar":"t","mDouble":1.6,"mLong":1,"mString":"string"}
    {"mBoolean":true,"mChar":"t","mDouble":1.6,"mLong":1,"mString":"string"}
2022-06-23 10:11:15.136 23339-23339/com.csp.sample.log E/(MainActivity.onClick): ========== 异常 ==========
2022-06-23 10:11:15.137 23339-23339/com.csp.sample.log E/(MainActivity.onClick): java.lang.Exception: 此处发生异常
        at com.csp.sample.log.MainActivity.onClick(MainActivity.java:70)
        at android.view.View.performClick(View.java:7312)
        at android.view.View.performClickInternal(View.java:7286)
        at android.view.View.access$3600(View.java:838)
        at android.view.View$PerformClick.run(View.java:28242)
        at android.os.Handler.handleCallback(Handler.java:900)
        at android.os.Handler.dispatchMessage(Handler.java:103)
        at android.os.Looper.loop(Looper.java:219)
        at android.app.ActivityThread.main(ActivityThread.java:8668)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:513)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1109)
2022-06-23 10:11:15.137 23339-23339/com.csp.sample.log I/(MainActivity.onClick): 此处发生异常
    java.lang.Exception: 此处发生异常
        at com.csp.sample.log.MainActivity.onClick(MainActivity.java:71)
        at android.view.View.performClick(View.java:7312)
        at android.view.View.performClickInternal(View.java:7286)
        at android.view.View.access$3600(View.java:838)
        at android.view.View$PerformClick.run(View.java:28242)
        at android.os.Handler.handleCallback(Handler.java:900)
        at android.os.Handler.dispatchMessage(Handler.java:103)
        at android.os.Looper.loop(Looper.java:219)
        at android.app.ActivityThread.main(ActivityThread.java:8668)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:513)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1109)
2022-06-23 10:11:15.137 23339-23339/com.csp.sample.log I/(MainActivity.onClick): 仅 debug 模式打印异常
    java.lang.Exception: 仅 debug 模式打印异常
        at com.csp.sample.log.MainActivity.onClick(MainActivity.java:72)
        at android.view.View.performClick(View.java:7312)
        at android.view.View.performClickInternal(View.java:7286)
        at android.view.View.access$3600(View.java:838)
        at android.view.View$PerformClick.run(View.java:28242)
        at android.os.Handler.handleCallback(Handler.java:900)
        at android.os.Handler.dispatchMessage(Handler.java:103)
        at android.os.Looper.loop(Looper.java:219)
        at android.app.ActivityThread.main(ActivityThread.java:8668)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:513)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1109)
2022-06-23 10:11:15.137 23339-23339/com.csp.sample.log E/(MainActivity.onClick): ========== 调整调用栈索引 ==========
2022-06-23 10:11:15.138 23339-23339/com.csp.sample.log W/(View.access$3600): 调整日志打印调用栈索引，注意 tag：[]
2022-06-23 10:11:15.138 23339-23339/com.csp.sample.log W/(View.access$3600): java.lang.Exception: 异常：调整日志打印调用栈索引，注意 tag
        at com.csp.sample.log.MainActivity.onClick(MainActivity.java:76)
        at android.view.View.performClick(View.java:7312)
        at android.view.View.performClickInternal(View.java:7286)
        at android.view.View.access$3600(View.java:838)
        at android.view.View$PerformClick.run(View.java:28242)
        at android.os.Handler.handleCallback(Handler.java:900)
        at android.os.Handler.dispatchMessage(Handler.java:103)
        at android.os.Looper.loop(Looper.java:219)
        at android.app.ActivityThread.main(ActivityThread.java:8668)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:513)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1109)
```

其他参考 Sample

## 历史版本

2022.06.22
- 拆分成 core（java 库）以及 android 两个库

2022.04.26
- 首次发版（改版本已废弃，请勿使用）
