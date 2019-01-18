package com.dyhdyh.compat.panelkeyboard.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.dyhdyh.compat.panelkeyboard.PanelKeyboardLayout;
import com.gyf.barlibrary.ImmersionBar;

import sj.keyboard.widget.FuncLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ImmersionBar.with(this)
                .enableAutoDarkMode(true)
                .statusBarColor(R.color.colorPrimary)
                .navigationBarColorInt(Color.WHITE).init();

        setContentView(R.layout.activity_main);

        final FuncLayout panel_container = findViewById(R.id.panel_container);
        final PanelKeyboardLayout keyboard_layout = findViewById(R.id.keyboard_layout);
        final EditText ed = findViewById(R.id.ed);

        findViewById(R.id.emoji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panel_container.toggleFuncView(keyboard_layout.isSoftKeyboardPop(), ed);
            }
        });
    }
}
