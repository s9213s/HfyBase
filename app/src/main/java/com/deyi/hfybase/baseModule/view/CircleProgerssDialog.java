package com.deyi.hfybase.baseModule.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Window;
import android.widget.TextView;

import com.deyi.hfybase.R;
import com.github.lzyzsd.circleprogress.DonutProgress;


/**
 * @author hfy
 * @description 上传图片的Dialog
 * @time 2020/5/8
 */

public class CircleProgerssDialog extends Dialog {

    private DonutProgress dProgress;

    private String content;


    public CircleProgerssDialog(@NonNull Context context) {
        super(context);
    }

    public CircleProgerssDialog(@NonNull Context context, String content) {
        super(context);
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_progressdialog_base);
        setCanceledOnTouchOutside(false);
        dProgress = (DonutProgress) findViewById(R.id.donutprogress);
        if (!TextUtils.isEmpty(content)) {
            ((TextView) findViewById(R.id.content)).setText(content);
        }
    }

    public DonutProgress getdProgress() {
        return dProgress;
    }

}