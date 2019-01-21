package com.dyhdyh.widget.panelkeyboard.example.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.dyhdyh.widget.panelkeyboard.KeyboardPanelLayout;
import com.dyhdyh.widget.panelkeyboard.KeyboardRootLayout;
import com.dyhdyh.widget.panelkeyboard.example.R;

/**
 * @author dengyuhan
 * created 2019/1/21 10:17
 */
public class ExampleDialog extends Dialog {

    public ExampleDialog(Context context) {
        super(context, R.style.Theme_Dialog_NoTitle);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.dialog_example);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;


        final KeyboardRootLayout keyboardLayout = findViewById(R.id.keyboard_layout);
        final KeyboardPanelLayout panelLayout = findViewById(R.id.panel_layout);
        final EditText editText = findViewById(R.id.ed);

        findViewById(R.id.iv_emoji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //panel_emoji就是表情面板的id
                panelLayout.togglePanelView(getWindow(), R.id.panel_emoji, keyboardLayout.isKeyboardShow(), editText);
            }
        });
    }
}
