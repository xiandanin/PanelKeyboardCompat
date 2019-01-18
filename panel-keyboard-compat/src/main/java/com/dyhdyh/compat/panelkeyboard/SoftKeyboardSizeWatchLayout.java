package com.dyhdyh.compat.panelkeyboard;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

public class SoftKeyboardSizeWatchLayout extends RelativeLayout {

    private int mOldHeight = -1;
    private int mNowHeight = -1;
    protected int mWindowHeight = 0;
    private boolean mKeyboardShow = false;

    private View mRootView;

    public SoftKeyboardSizeWatchLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                if (mRootView == null) {
                    mRootView = findRootLayout();
                }
                if (mRootView != null) {
                    mRootView.getWindowVisibleDisplayFrame(r);
                }
                if (mWindowHeight == 0) {
                    mWindowHeight = r.bottom;
                }
                mNowHeight = mWindowHeight - r.bottom;
                if (mOldHeight != -1 && mNowHeight != mOldHeight) {
                    if (mNowHeight > 0) {
                        mKeyboardShow = true;
                        onKeyboardShow(mNowHeight);
                    } else {
                        mKeyboardShow = false;
                        onKeyboardClose();
                    }
                }
                mOldHeight = mNowHeight;
            }
        });
    }


    /**
     * @return window 根view
     */
    private View findRootLayout() {
        View rootView = this;
        while (rootView.getParent() != null && rootView.getParent() instanceof View) {
            rootView = (View) rootView.getParent();
        }
        return rootView;
    }

    public boolean isKeyboardShow() {
        return mKeyboardShow;
    }


    protected void onKeyboardShow(int keyboardHeight) {

    }

    protected void onKeyboardClose() {

    }

}