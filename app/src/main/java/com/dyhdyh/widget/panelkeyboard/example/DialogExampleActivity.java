package com.dyhdyh.widget.panelkeyboard.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.dyhdyh.widget.panelkeyboard.example.dialog.ExampleDialog;

/**
 * @author dengyuhan
 * created 2019/1/21 10:16
 */
public class DialogExampleActivity extends BaseActivity {
    private ExampleDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = new ExampleDialog(this);

        clickDialog(null);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dialog_example;
    }

    public void clickDialog(View view) {
        mDialog.show();
    }
}
