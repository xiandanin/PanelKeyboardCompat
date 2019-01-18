package com.dyhdyh.compat.panelkeyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

public class KeyboardPanelLayout extends LinearLayout {

    public static final int DEF_KEY = Integer.MIN_VALUE;
    public static final int SINGLE_KEY = 0;

    private final SparseArray<View> mFuncViewArrayMap = new SparseArray<>();

    private int mCurrentFuncKey = DEF_KEY;

    protected int mHeight = 0;

    private OnPanelStatusListener mOnPanelStatusListener;
    private OnPanelChangeListener mOnPanelChangeListener;

    public KeyboardPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    private void addFuncView(int key, View view) {
        if (mFuncViewArrayMap.get(key) != null) {
            return;
        }
        mFuncViewArrayMap.put(key, view);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        super.addView(view, params);
        view.setVisibility(GONE);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        mFuncViewArrayMap.put(SINGLE_KEY, child);
        super.addView(child, index, params);
        child.setVisibility(GONE);

    }

    public void hideAllFuncView() {
        for (int i = 0; i < mFuncViewArrayMap.size(); i++) {
            int keyTemp = mFuncViewArrayMap.keyAt(i);
            mFuncViewArrayMap.get(keyTemp).setVisibility(GONE);
        }
        mCurrentFuncKey = DEF_KEY;
        setVisibility(View.GONE);
    }

    public void toggleFuncView(boolean isKeyboardShow, EditText editText) {
        toggleFuncView(SINGLE_KEY, isKeyboardShow, editText);
    }

    public void toggleFuncView(int key, boolean isKeyboardShow, EditText editText) {
        if (getVisibility() == View.VISIBLE && getCurrentFuncKey() == key) {
            if (isKeyboardShow) {
                if (KeyboardUtils.isFullScreen(getContext())) {
                    KeyboardUtils.closeSoftKeyboard(editText);
                } else {
                    KeyboardUtils.closeSoftKeyboard(getContext());
                }
            } else {
                KeyboardUtils.openSoftKeyboard(editText);
            }
        } else {
            if (isKeyboardShow) {
                if (KeyboardUtils.isFullScreen(getContext())) {
                    KeyboardUtils.closeSoftKeyboard(editText);
                } else {
                    KeyboardUtils.closeSoftKeyboard(getContext());
                }
            }
            showFuncView(key);
        }
    }

    public void showFuncView(int key) {
        if (mFuncViewArrayMap.get(key) == null) {
            return;
        }
        for (int i = 0; i < mFuncViewArrayMap.size(); i++) {
            int keyTemp = mFuncViewArrayMap.keyAt(i);
            if (keyTemp == key) {
                mFuncViewArrayMap.get(keyTemp).setVisibility(VISIBLE);
            } else {
                mFuncViewArrayMap.get(keyTemp).setVisibility(GONE);
            }
        }
        mCurrentFuncKey = key;
        setVisibility(View.VISIBLE);

        callPanelChange(key);
    }

    public void callPanelChange(int key) {
        if (mOnPanelChangeListener != null) {
            mOnPanelChangeListener.onPanelChange(key);
        }
    }

    public int getCurrentFuncKey() {
        return mCurrentFuncKey;
    }

    public void updateHeight(int height) {
        this.mHeight = height;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        LayoutParams params = (LayoutParams) getLayoutParams();
        if (View.VISIBLE == visibility) {
            params.height = mHeight;
            if (mOnPanelStatusListener != null) {
                mOnPanelStatusListener.onPanelStatus(true, mHeight);
            }
        } else if (View.INVISIBLE == visibility) {
            params.height = mHeight;
        } else {
            params.height = 0;
            if (mOnPanelStatusListener != null) {
                mOnPanelStatusListener.onPanelStatus(false, 0);
            }
        }
        setLayoutParams(params);
    }

    public boolean isOnlyShowSoftKeyboard() {
        return mCurrentFuncKey == DEF_KEY;
    }

    public interface OnPanelStatusListener {
        void onPanelStatus(boolean isShowing, int height);
    }

    public interface OnPanelChangeListener {
        void onPanelChange(int key);
    }

    public void setOnPanelChangeListener(OnPanelChangeListener listener) {
        this.mOnPanelChangeListener = listener;
    }

    public void setOnPanelStatusListener(OnPanelStatusListener listener) {
        this.mOnPanelStatusListener = listener;
    }
}