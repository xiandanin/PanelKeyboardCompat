## 效果图
![](screenshot/1.gif)

## 开始使用
#### Gradle
```
compile 'com.dyhdyh.widget:panelkeyboard:1.0.0'
```


##### 在AndroidManifest.xml将你的Activity键盘模式设置成`adjustResize `

```
<activity
    android:name=".KeyboardActivity"
    android:windowSoftInputMode="adjustResize" />
```

##### Activity的ContentView必须是`KeyboardRootLayout`

```
<com.dyhdyh.widget.panelkeyboard.KeyboardRootLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/keyboard_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
    <!--KeyboardRootLayout只能包含一个子View-->

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        
        ...

        <com.dyhdyh.widget.panelkeyboard.KeyboardPanelLayout
            android:id="@+id/panel_layout"
            android:layout_width="match_parent"
            android:layout_height="@dimen/def_keyboard_height"
            android:background="@android:color/white"
            android:visibility="gone">

            <!--面板View一定要用KeyboardPanelLayout包裹-->
            <RelativeLayout
                android:id="@+id/panel_emoji"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:visibility="gone">
    
                ...
                
            </RelativeLayout>

        </com.dyhdyh.widget.panelkeyboard.KeyboardPanelLayout>
    </RelativeLayout>
    
</com.dyhdyh.widget.panelkeyboard.KeyboardRootLayout>
```

##### 如果Activity用了全屏主题，还需要调用`dispatchKeyEventInFullScreen`

```
@Override
public boolean dispatchKeyEvent(KeyEvent event) {
    if (keyboardLayout.dispatchKeyEventInFullScreen(event)) {
        return true;
    }
    return super.dispatchKeyEvent(event);
}
```