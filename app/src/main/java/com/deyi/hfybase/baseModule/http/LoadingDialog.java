package com.deyi.hfybase.baseModule.http;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.widget.TextView;

import com.deyi.hfybase.R;
import com.deyi.hfybase.baseModule.config.BaseModule;
import com.wang.avi.AVLoadingIndicatorView;

import net.frakbot.jumpingbeans.JumpingBeans;

/**
 * @author hfy
 * @description
 * @time 2020/10/12
 */
public class LoadingDialog extends Dialog {
    private Context mContext;
    //dialog 样式名称
    private String name;

    private String info;

    private TextView info_tv;

    private AVLoadingIndicatorView view;

    private JumpingBeans bean;

    private boolean textAnimate;

    private boolean animateAtLast;

    public LoadingDialog(Context context, String name) {
        super(context);
        mContext = context;
        this.name = name;
    }

    public LoadingDialog setInfo(String info) {
        this.info = info;
        return this;
    }

    public LoadingDialog setTextAnimate(boolean textAnimate) {
        this.textAnimate = textAnimate;
        return this;
    }

    public LoadingDialog setAnimateAtLast(boolean animateAtLast) {
        this.animateAtLast = animateAtLast;
        return this;
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_loading_dialog_base);
        view = findViewById(R.id.avi);
        info_tv = findViewById(R.id.info);
        view.setIndicator(name);
        if (BaseModule.getBaseModuleConfig().getDialogColor() != -1) {
            view.setIndicatorColor(ContextCompat.getColor(mContext, BaseModule.getBaseModuleConfig().getDialogColor()));
        } else {
            view.setIndicatorColor(Color.BLUE);
        }

        if (info != null) {
            info_tv.setText(info);
        }
        if (info_tv.getText().toString().contains("...") && textAnimate) {
            bean = JumpingBeans.with(info_tv)
                    .makeTextJump(animateAtLast ? info_tv.getText().toString().indexOf(".") : 0, info_tv.getText().toString().length())
                    .setIsWave(true)
                    .setLoopDuration(1500)
                    .build();
        }
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bean != null) bean.stopJumping();
    }

}
