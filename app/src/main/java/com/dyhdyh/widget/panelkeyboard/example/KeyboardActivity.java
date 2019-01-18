package com.dyhdyh.widget.panelkeyboard.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.dyhdyh.widget.panelkeyboard.KeyboardPanelLayout;
import com.dyhdyh.widget.panelkeyboard.KeyboardRootLayout;
import com.dyhdyh.widget.panelkeyboard.KeyboardUtils;
import com.dyhdyh.widget.panelkeyboard.example.adapter.ExampleItemAdapter;

public class KeyboardActivity extends AppCompatActivity {
    private final String TAG = "KeyboardActivity";

    private KeyboardRootLayout keyboardLayout;
    private KeyboardPanelLayout panelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        setContentView(R.layout.activity_keyboard);

        if (fitSystemWindows) {
            ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).setFitsSystemWindows(true);
        }

        panelLayout = findViewById(R.id.panel_layout);
        keyboardLayout = findViewById(R.id.keyboard_layout);

        final EditText editText = findViewById(R.id.ed);
        findViewById(R.id.iv_emoji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panelLayout.togglePanelView(R.id.panel_emoji, keyboardLayout.isKeyboardShow(), editText);
            }
        });
        findViewById(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panelLayout.togglePanelView(R.id.panel_func, keyboardLayout.isKeyboardShow(), editText);
            }
        });

        panelLayout.setOnPanelChangeListener(new KeyboardPanelLayout.OnPanelChangeListener() {
            @Override
            public void onPanelChange(int key) {
                Log.d(TAG, "面板切换 - key: " + key);
            }
        });
        panelLayout.setOnPanelStatusListener(new KeyboardPanelLayout.OnPanelStatusListener() {
            @Override
            public void onPanelStatus(boolean isShowing, int height) {
                Log.d(TAG, "面板" + (isShowing ? "弹起 - height: " + height : "关闭"));
            }
        });
        keyboardLayout.setOnKeyboardStatusListener(new KeyboardRootLayout.OnKeyboardStatusListener() {
            @Override
            public void onKeyboardStatus(boolean isShowing, int height) {
                Log.d(TAG, "键盘" + (isShowing ? "弹起 - height: " + height : "关闭"));
            }
        });

        initExampleView();
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyboardUtils.isFullScreen(this) && keyboardLayout.dispatchKeyEventInFullScreen(event)) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    private void initExampleView() {
        RecyclerView rv = findViewById(R.id.rv_func);
        rv.setLayoutManager(new GridLayoutManager(this, 4));
        rv.setAdapter(new ExampleItemAdapter());
    }
}
