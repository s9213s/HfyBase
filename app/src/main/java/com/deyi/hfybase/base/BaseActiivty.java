package com.deyi.hfybase.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.deyi.hfybase.R;
import com.deyi.hfybase.baseModule.base.MBaseActivity;
import com.deyi.hfybase.util.SpUtil;
import com.jaeger.library.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hfy
 * @description
 * @time 2020/10/13
 */
public abstract class BaseActiivty extends MBaseActivity {
    private Unbinder unbinder;
    public SpUtil spUtil;

    public App mApp;

    @Override
    public void bindButterKnife() {
        try {
            unbinder = ButterKnife.bind(this);
        } catch (Exception e) {

        }
    }

    @Override
    public void unbindButterknife() {
        try {
            unbinder.unbind();
        } catch (Exception e) {

        }
    }

    @Override
    public void init() {
        mApp = (App) getApplication();
        spUtil = new SpUtil(this);
    }

    /**
     * 全透状态栏
     */
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * hfy
     * 2019.6.27
     * 6.0及以上则显示白底黑字（时间，电池状态等为黑色），以下则显示黑底
     * 6.0以下无法更改电池状态为黑色，所以只能将底色设置为黑色
     */
    protected void setBlackFontStatusBarFullColor() {
        setFitSystemWindow(true);
        if (Build.VERSION.SDK_INT > 22) {
            StatusBarUtil.setTranslucentForImageView(this, 0, null);
            StatusBarUtil.setLightMode(this);
        } else {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.black));
        }

    }

    /**
     * 内容是否紧贴着StatusBar
     *
     * @param fitSystemWindow true 表示贴合，状态栏（电池电量）
     */
    protected void setBlackFontStatusBarFullColor(boolean fitSystemWindow) {
        if (fitSystemWindow) {
            setFitSystemWindow(true);
        }
        if (Build.VERSION.SDK_INT > 22) {
            StatusBarUtil.setTranslucentForImageView(this, 0, null);
            StatusBarUtil.setLightMode(this);
        } else {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.black));
        }
    }

    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private View contentViewGroup;

    protected void setFitSystemWindow(boolean fitSystemWindow) {
        if (contentViewGroup == null) {
            contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        }
        contentViewGroup.setFitsSystemWindows(fitSystemWindow);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {//点击editText控件外部
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    hideInput();
                    if (editText != null) {
                        editText.clearFocus();
                    }
                }
            } else if (isFrequentlyClick()) {
                Log.i("hfydemo", "不要频繁点击！");
                //可以在这里给点提示
//                toast("不要频繁点击！");
                return true;
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    EditText editText = null;

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            editText = (EditText) v;
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    private long lastClickTime = 0;
    //设置拦截的时间间隔 500毫秒
    private long RESTRICT_TIME = 400;

    public boolean isFrequentlyClick() {
        long clickTime = System.currentTimeMillis();
        long value = clickTime - lastClickTime;
        lastClickTime = clickTime;
        return value <= RESTRICT_TIME;
    }
}
