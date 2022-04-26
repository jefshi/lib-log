package com.csp.sample.log;

import android.app.Application;

import com.csp.lib.log.android.LogCatAndroid;
import com.csp.lib.log.core.LogCat;
import com.csp.sample.log.util.GsonUtil;

/**
 * @author csp
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogCat.setLogger(new LogCatAndroid(BuildConfig.DEBUG) {
            @Override
            public String toString(Object obj) {
                String log;
                try {
                    // 纯 String 用 Json 会导致 \n 失效，所以要额外处理
                    if (obj instanceof String) {
                        log = obj + "";
                    } else {
                        log = GsonUtil.getGson().toJson(obj);
                    }
                } catch (Exception e) {
                    log = super.toString(obj);
                }
                return log;
            }
        });
    }
}
