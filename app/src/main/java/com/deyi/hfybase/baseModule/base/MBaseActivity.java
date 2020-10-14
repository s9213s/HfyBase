package com.deyi.hfybase.baseModule.base;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.deyi.hfybase.baseModule.event.NullBean;
import com.deyi.hfybase.baseModule.inter.IBaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author hfy
 * @description
 * @time 2020/10/12
 */
public abstract class MBaseActivity extends AppCompatActivity implements IBaseActivity {
    private final String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(bindLayout());
        bindButterKnife();
        EventBus.getDefault().register(this);
        init();
        initView();
        loadData();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindButterknife();
    }

    /**
     * 默认不使用部分手机自带的手机字号大小
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        //非默认值
        if (res.getConfiguration().fontScale != 1) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    /**
     * 显示短消息
     *
     * @param info 消息内容
     **/
    public void showToast(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    /**
     * 隐藏输入法
     */
    public void hideInput() {
        if ((this).getCurrentFocus() != null) {
            if ((this).getCurrentFocus().getWindowToken() != null) {
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow((this).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 如果不在基类进行注册
     *
     * @param bean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void NullEvent(NullBean bean) {

    }

}
