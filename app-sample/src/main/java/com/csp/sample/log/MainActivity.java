package com.csp.sample.log;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.csp.lib.log.core.LogCat;
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
        switch (v.getId()) {
            case R.id.bt_test:
                LogCat.e("========== 不带占位符 ==========");
                LogCat.e("string");
                LogCat.e(true);
                LogCat.e('c');
                LogCat.e(1.2);
                LogCat.e("数组", "string", true, 'c', 1.2);
                LogCat.e("集合", mList);
                LogCat.e("任意对象", new Bean());

                LogCat.e("========== 带占位符 ==========");
                LogCat.e("String = %s，Boolean = %s，Char = %s，Double = %s", "string", true, 'c', 1.2);
                LogCat.e("Bean = %s\n%s\n%s", new Bean(), new Bean(), new Bean());
                break;
            default:
                break;
        }
    }
}
