package com.dyhdyh.widget.panelkeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardUtils {

    private static final String EXTRA_DEF_KEYBOARD_HEIGHT = "DEF_KEYBOARDHEIGHT";
    private static int sDefKeyboardHeight = -1;

    public static int getDefKeyboardHeight(Context context) {
        if (sDefKeyboardHeight < 0) {
            sDefKeyboardHeight = context.getResources().getDimensionPixelSize(R.dimen.def_keyboard_height);
        }
        int height = PreferenceManager.getDefaultSharedPreferences(context).getInt(EXTRA_DEF_KEYBOARD_HEIGHT, 0);
        return sDefKeyboardHeight = height > 0 && sDefKeyboardHeight != height ? height : sDefKeyboardHeight;
    }

    public static void setDefKeyboardHeight(Context context, int height) {
        if (sDefKeyboardHeight != height) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(EXTRA_DEF_KEYBOARD_HEIGHT, height).apply();
            KeyboardUtils.sDefKeyboardHeight = height;
        }
    }

    public static boolean isFullScreen(final Context context) {
        if (context instanceof Activity) {
            return isFullScreen(((Activity) context).getWindow());
        } else if (context instanceof ContextThemeWrapper) {
            Context baseContext = ((ContextWrapper) context).getBaseContext();
            if (baseContext instanceof Activity) {
                return isFullScreen(((Activity) baseContext).getWindow());
            }
        }
        return false;
    }

    /**
     * 是否全屏
     *
     * @param window
     * @return
     */
    public static boolean isFullScreen(final Window window) {
        return (window.getAttributes().flags &
                WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
    }

    /**
     * 开启软键盘
     *
     * @param et
     */
    public static void openSoftKeyboard(EditText et) {
        if (et != null) {
            et.setFocusable(true);
            et.setFocusableInTouchMode(true);
            et.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(et, 0);
        }
    }


    public static void closeSoftKeyboardCompat(Window window, EditText editText) {
        if (KeyboardUtils.isFullScreen(window)) {
            KeyboardUtils.closeSoftKeyboard(editText);
        } else {
            KeyboardUtils.closeSoftKeyboard(window);
        }
    }

    /**
     * 关闭软键盘
     *
     * @param window
     */
    public static void closeSoftKeyboard(Window window) {
        if (window == null || window.getCurrentFocus() == null) {
            return;
        }
        try {
            View view = window.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            view.clearFocus();
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭软键盘
     * 当使用全屏主题的时候,XhsEmoticonsKeyBoard屏蔽了焦点.关闭软键盘时,直接指定 closeSoftKeyboard(EditView)
     *
     * @param view
     */
    public static void closeSoftKeyboard(View view) {
        if (view == null || view.getWindowToken() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
