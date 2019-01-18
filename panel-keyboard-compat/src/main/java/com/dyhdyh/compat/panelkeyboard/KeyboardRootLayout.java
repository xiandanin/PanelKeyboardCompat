package com.dyhdyh.compat.panelkeyboard;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author dengyuhan
 * created 2019/1/17 15:17
 */
public class KeyboardRootLayout extends AutoHeightLayout{

    protected KeyboardPanelLayout mPanelLayout;
    protected OnKeyboardStatusListener mOnKeyboardStatusListener;

    protected boolean mDispatchKeyEventPreImeLock = false;

    public KeyboardRootLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);

        if (mPanelLayout != null) {
            return;
        }
        mPanelLayout = findSuitablePanelLayout(child);
    }

    public KeyboardPanelLayout findSuitablePanelLayout(View child) {
        if (child instanceof KeyboardPanelLayout) {
            return (KeyboardPanelLayout) child;
        } else {
            //递归寻找FuncLayout
            if (child instanceof ViewGroup) {
                final int childCount = ((ViewGroup) child).getChildCount();
                for (int i = childCount - 1; i >= 0; i--) {
                    final View childAt = ((ViewGroup) child).getChildAt(i);
                    final KeyboardPanelLayout childFuncLayout = findSuitablePanelLayout(childAt);
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
        if (mPanelLayout != null) {
            mPanelLayout.updateHeight(height);
        }
    }

    @Override
    protected void onKeyboardShow(int keyboardHeight) {
        super.onKeyboardShow(keyboardHeight);
        if (mOnKeyboardStatusListener != null) {
            mOnKeyboardStatusListener.onKeyboardStatus(true, keyboardHeight);
        }
        if (mPanelLayout != null) {
            mPanelLayout.setVisibility(View.INVISIBLE);

            mPanelLayout.callPanelChange(KeyboardPanelLayout.DEF_KEY);
        }
    }

    @Override
    protected void onKeyboardClose() {
        super.onKeyboardClose();
        if (mOnKeyboardStatusListener != null) {
            mOnKeyboardStatusListener.onKeyboardStatus(false, 0);
        }
        if (mPanelLayout != null) {
            if (mPanelLayout.isOnlyShowSoftKeyboard()) {
                reset();
            } else {
                if (mPanelLayout.getVisibility() == View.INVISIBLE) {
                    mPanelLayout.setVisibility(View.GONE);
                } else {
                    mPanelLayout.callPanelChange(mPanelLayout.getCurrentFuncKey());
                }
            }
        }
    }

    public void reset() {
        KeyboardUtils.closeSoftKeyboard(this);
        if (mPanelLayout != null) {
            mPanelLayout.hideAllFuncView();
        }
    }

    public void setOnKeyboardStatusListener(OnKeyboardStatusListener listener) {
        this.mOnKeyboardStatusListener = listener;
    }

    @Override
    public void setFitsSystemWindows(boolean fitSystemWindows) {
        if (fitSystemWindows && getParent() != null) {
            ((View) getParent()).setFitsSystemWindows(true);
        } else {
            super.setFitsSystemWindows(fitSystemWindows);
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
                if (mPanelLayout != null && mPanelLayout.isShown()) {
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
        if (KeyboardUtils.isFullScreen(getContext())) {
            return false;
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (KeyboardUtils.isFullScreen(getContext())) {
            return;
        }
        super.requestChildFocus(child, focused);
    }

    /**
     * 供全屏Activity调用
     *
     * @param event
     * @return
     */
    public boolean dispatchKeyEventInFullScreen(KeyEvent event) {
        if (event == null) {
            return false;
        }
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (KeyboardUtils.isFullScreen(getContext()) && mPanelLayout != null && mPanelLayout.isShown()) {
                    reset();
                    return true;
                }
        }
        return false;
    }


    public interface OnKeyboardStatusListener {
        void onKeyboardStatus(boolean isShowing, int height);
    }

}
