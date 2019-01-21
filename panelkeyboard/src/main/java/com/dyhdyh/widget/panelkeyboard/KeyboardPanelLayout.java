package com.dyhdyh.widget.panelkeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class KeyboardPanelLayout extends RelativeLayout {

    public static final int DEF_KEY = Integer.MIN_VALUE;
    public static final int SINGLE_KEY = 0;

    private final SparseArray<View> mFuncViewArrayMap = new SparseArray<>();

    private int mCurrentFuncKey = DEF_KEY;

    protected int mHeight = 0;

    private OnPanelStatusListener mOnPanelStatusListener;
    private OnPanelChangeListener mOnPanelChangeListener;

    public KeyboardPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        mFuncViewArrayMap.put(child.getId(), child);
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

    public void togglePanelView(int key, boolean isKeyboardShow, EditText editText) {
        final Context context = getContext();
        if (context instanceof Activity) {
            togglePanelView(((Activity) getContext()).getWindow(), key, isKeyboardShow, editText);
        } else if (context instanceof ContextThemeWrapper) {
            Context baseContext = ((ContextWrapper) context).getBaseContext();
            if (baseContext instanceof Activity) {
                togglePanelView(((Activity) baseContext).getWindow(), key, isKeyboardShow, editText);
            }
        }
    }

    public void togglePanelView(Window window, int key, boolean isKeyboardShow, EditText editText) {
        if (getVisibility() == View.VISIBLE && getCurrentFuncKey() == key) {
            if (isKeyboardShow) {
                KeyboardUtils.closeSoftKeyboardCompat(window, editText);
            } else {
                KeyboardUtils.openSoftKeyboard(editText);
            }
        } else {
            if (isKeyboardShow) {
                KeyboardUtils.closeSoftKeyboardCompat(window, editText);
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
        ViewGroup.LayoutParams params = getLayoutParams();
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