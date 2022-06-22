# 日志打印工具

## 前言

日志打印工具库
- 定位：工具库
- core：纯 java 代码
- android：针对 Android 的日志打印方式

使用说明：
1. 需要完成一次初始化，否则默认仅打印异常日志，具体见章节【库的基本用法】
2. 会在 tag 中打印日志调用类与方法信息，同时可以通过调整调用栈索引，修改调用信息，具体见章节【库的基本用法】
3. 本库自带的 Sample 将对象打印的输出方式重写为：未重写 toString() 时使用 Json 格式，否则使用 toString()，具体参考章节【库的基本用法】

## 集成步骤

``` gradle
implementation 'io.github.jefshi:lib-log-core:1.0.0'
implementation 'io.github.jefshi:lib-log-android:1.0.0'
```

如果仅用于 java 库或项目，则只需依赖 `lib-log-core` 就行

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

其他参考 Sample

## 历史版本

2022.04.26
- 首次发版
