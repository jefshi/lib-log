package com.csp.sample.log;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author csp
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String versionInfo = String.format(getString(R.string.version_info),
                BuildConfig.ASSEMBLE_TIME,
                BuildConfig.VERSION_CODE + "",
                BuildConfig.VERSION_NAME);

        ((TextView) findViewById(R.id.tv_version)).setText(versionInfo);
    }

    private Context getContext() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_version:
                break;
            default:
                break;
        }
    }
}
