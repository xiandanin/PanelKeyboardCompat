package com.dyhdyh.widget.panelkeyboard.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * @author dengyuhan
 * created 2019/1/21 10:24
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean translucentStatus = getIntent().getBooleanExtra("translucent_status", false);
        boolean fullscreen = getIntent().getBooleanExtra("fullscreen", false);
        boolean fitSystemWindows = getIntent().getBooleanExtra("fit_system_windows", false);

        if (translucentStatus) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (fullscreen) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(getLayoutId());

        if (fitSystemWindows) {
            ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).setFitsSystemWindows(true);
        }
    }

    protected abstract int getLayoutId();
}
