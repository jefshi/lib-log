package com.csp.sample.log;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.csp.lib.log.LogCat;
import com.csp.sample.log.model.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author csp
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_test).setOnClickListener(this);

        String versionInfo = String.format(getString(R.string.version_info),
                BuildConfig.ASSEMBLE_TIME,
                BuildConfig.VERSION_CODE + "",
                BuildConfig.VERSION_NAME);

        ((TextView) findViewById(R.id.tv_version)).setText(versionInfo);

        mList = new ArrayList<>();
        mList.add("aaa");
        mList.add("bbb");
        mList.add("ccc");
    }

    private Context getContext() {
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_test) {
            LogCat.e("\n========== 使用说明 ==========");
            LogCat.e("1. 需要完成一次初始化，否则默认仅打印异常日志，初始化见 Application");
            LogCat.e("2. 会在 tag 中打印日志调用类与方法信息，同时可以通过调整调用栈索引，修改调用信息");
            LogCat.e("3. 本 Sample 将对象打印的输出方式重写为：未重写 toString() 时使用 Json 格式，否则使用 toString()");

            LogCat.w("\n========== 不带占位符 ==========");
            LogCat.w("string");
            LogCat.w(true);
            LogCat.w('c');
            LogCat.w(1.2);
            LogCat.w("数组", "string", true, 'c', 1.2);
            LogCat.w("集合", mList);
            LogCat.w("任意对象（未重写 toString()）", new Bean());
            LogCat.w("任意对象（已重写 toString()）", Thread.currentThread());

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
        }
    }
}
