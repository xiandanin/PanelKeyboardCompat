package com.dyhdyh.widget.panelkeyboard.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {
    CheckBox cb_translucent_status, cb_fullscreen, cb_fit_system_windows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cb_translucent_status = findViewById(R.id.cb_translucent_status);
        cb_fullscreen = findViewById(R.id.cb_fullscreen);
        cb_fit_system_windows = findViewById(R.id.cb_fit_system_windows);

    }


    public void clickActivity(View v) {
        startExampleActivity(KeyboardActivity.class);
    }

    public void clickDialog(View view) {
        startExampleActivity(DialogExampleActivity.class);
    }


    private void startExampleActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("translucent_status", cb_translucent_status.isChecked());
        intent.putExtra("fullscreen", cb_fullscreen.isChecked());
        intent.putExtra("fit_system_windows", cb_fit_system_windows.isChecked());
        startActivity(intent);
    }
}
