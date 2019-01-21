package com.dyhdyh.widget.panelkeyboard;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public abstract class AutoHeightLayout extends SoftKeyboardSizeWatchLayout {

    protected Context mContext;
    protected int mMaxParentHeight;
    protected int mSoftKeyboardHeight;
    protected boolean mConfigurationChangedFlag = false;

    public AutoHeightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mSoftKeyboardHeight = KeyboardUtils.getDefKeyboardHeight(mContext);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("can host only one direct child");
        }
        super.addView(child, index, params);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        onSoftKeyboardHeightChanged(mSoftKeyboardHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mMaxParentHeight == 0) {
            mMaxParentHeight = h;
        }
    }

    public void updateMaxParentHeight(int maxParentHeight) {
        this.mMaxParentHeight = maxParentHeight;
        if(maxParentHeightChangeListener != null){
            maxParentHeightChangeListener.onMaxParentHeightChange(maxParentHeight);
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mConfigurationChangedFlag = true;
        mWindowHeight = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mConfigurationChangedFlag){
            mConfigurationChangedFlag = false;
            Rect r = new Rect();
            if (mRootView != null) {
                mRootView.getWindowVisibleDisplayFrame(r);
            }
            if (mWindowHeight == 0) {
                mWindowHeight = r.bottom;
            }
            int mNowh = mWindowHeight - r.bottom;
            mMaxParentHeight = mNowh;
        }

        if (mMaxParentHeight != 0) {
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int expandSpec = MeasureSpec.makeMeasureSpec(mMaxParentHeight, heightMode);
            super.onMeasure(widthMeasureSpec, expandSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onKeyboardShow(int keyboardHeight) {
        if (mSoftKeyboardHeight != keyboardHeight) {
            mSoftKeyboardHeight = keyboardHeight;
            KeyboardUtils.setDefKeyboardHeight(mContext, mSoftKeyboardHeight);
            onSoftKeyboardHeightChanged(mSoftKeyboardHeight);
        }
    }

    public abstract void onSoftKeyboardHeightChanged(int height);

    private OnMaxParentHeightChangeListener maxParentHeightChangeListener;

    public interface OnMaxParentHeightChangeListener {
        void onMaxParentHeightChange(int height);
    }

    public void setOnMaxParentHeightChangeListener(OnMaxParentHeightChangeListener listener) {
        this.maxParentHeightChangeListener = listener;
    }
}