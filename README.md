# 日志打印工具

## 前言

日志打印工具库
- 定位：工具库
- core：纯 java 代码
- android：针对 Android 的日志打印方式

## 集成步骤

``` gradle
implementation 'io.github.jefshi:lib-log:1.0.0'
```

### 库的基本用法

初始化日志
- 未初始化：仅打印异常日志
- 通过 `LogCat#setLogger(ILog)` 初始化，如：
	- Android + obj.toString() 方式：`LogCat.setLogger(new LogCatAndroid(BuildConfig.DEBUG))`
	- Android + Json 方式

``` java
LogCat.setLogger(new LogCatAndroid(BuildConfig.DEBUG) {
    @Override
    public String toString(Object obj) {
        String log;
        try {
            // 纯 String 用 Json 会导致 \n 失效，所以要额外处理
            if (obj instanceof String) {
                log = obj + "";
            } else {
                // log = new Gson().toJson(obj);
                log = GsonUtil.getGson().toJson(obj);
            }
        } catch (Exception e) {
            log = super.toString(obj);
        }
        return log;
    }
});
```

日志打印

``` java
LogCat.e("对接 Android 日志，则会在 tag 中打印日志调用类与方法信息");

LogCat.w("\n========== 不带占位符 ==========");
LogCat.w("string");
LogCat.w(true);
LogCat.w('c');
LogCat.w(1.2);
LogCat.w("数组", "string", true, 'c', 1.2);
LogCat.w("集合", mList);
LogCat.w("任意对象", new Bean());

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
