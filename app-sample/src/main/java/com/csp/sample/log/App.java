package com.csp.sample.log;

import android.app.Application;

import com.csp.lib.log.LoggerAndroid;
import com.csp.lib.log.LogCat;
import com.csp.sample.log.util.GsonUtil;

/**
 * @author csp
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
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
    }
}
