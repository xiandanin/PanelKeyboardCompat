package com.dyhdyh.compat.panelkeyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import sj.keyboard.utils.EmoticonsKeyboardUtils;
import sj.keyboard.widget.AutoHeightLayout;
import sj.keyboard.widget.FuncLayout;

/**
 * @author dengyuhan
 * created 2019/1/17 15:17
 */
public class PanelKeyboardLayout extends AutoHeightLayout implements FuncLayout.OnFuncChangeListener {

    private FuncLayout mFuncLayout;
    protected boolean mDispatchKeyEventPreImeLock = false;

    public PanelKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);

        if (mFuncLayout != null) {
            return;
        }
        mFuncLayout = findSuitableFuncLayout(child);
        if (mFuncLayout != null) {
            mFuncLayout.setOnFuncChangeListener(this);
        }
    }

    public FuncLayout findSuitableFuncLayout(View child) {
        if (child instanceof FuncLayout) {
            return (FuncLayout) child;
        } else {
            //递归寻找FuncLayout
            if (child instanceof ViewGroup) {
                final int childCount = ((ViewGroup) child).getChildCount();
                for (int i = childCount - 1; i >= 0; i--) {
                    final View childAt = ((ViewGroup) child).getChildAt(i);
                    final FuncLayout childFuncLayout = findSuitableFuncLayout(childAt);
                    if (childFuncLayout != null) {
                        return childFuncLayout;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void onSoftKeyboardHeightChanged(int height) {
        if (mFuncLayout != null) {
            mFuncLayout.updateHeight(height);
        }
    }

    @Override
    public void onFuncChange(int key) {

    }

    @Override
    public void OnSoftPop(int height) {
        super.OnSoftPop(height);
        Log.d("-------->", "键盘弹起----->" + height + "---->" + isSoftKeyboardPop());
        if (mFuncLayout != null) {
            mFuncLayout.setVisibility(true);
            onFuncChange(mFuncLayout.DEF_KEY);
        }
    }

    @Override
    public void OnSoftClose() {
        super.OnSoftClose();
        Log.d("-------->", "键盘关闭----->" + isSoftKeyboardPop());
        if (mFuncLayout != null) {
            if (mFuncLayout.isOnlyShowSoftKeyboard()) {
                reset();
            } else {
                /*if (mFuncLayout.getVisibility() == View.INVISIBLE) {
                    mFuncLayout.setVisibility(false);
                } else {
                    onFuncChange(mFuncLayout.getCurrentFuncKey());
                }*/
                onFuncChange(mFuncLayout.getCurrentFuncKey());
            }
        }
    }

    public void reset() {
        EmoticonsKeyboardUtils.closeSoftKeyboard(this);
        if (mFuncLayout != null) {
            mFuncLayout.hideAllFuncView();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (mDispatchKeyEventPreImeLock) {
                    mDispatchKeyEventPreImeLock = false;
                    return true;
                }
                if (mFuncLayout != null && mFuncLayout.isShown()) {
                    reset();
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext())) {
            return false;
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext())) {
            return;
        }
        super.requestChildFocus(child, focused);
    }

    public boolean dispatchKeyEventInFullScreen(KeyEvent event) {
        if (event == null) {
            return false;
        }
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext()) && mFuncLayout != null && mFuncLayout.isShown()) {
                    reset();
                    return true;
                }
        }
        return false;
    }
}
