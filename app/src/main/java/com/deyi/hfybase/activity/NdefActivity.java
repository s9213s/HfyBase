package com.deyi.hfybase.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.deyi.hfybase.R;
import com.deyi.hfybase.base.BaseActiivty;
import com.deyi.hfybase.baseModule.util.MTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hfy
 * @description
 * @time 2020/10/13
 */
public class NdefActivity extends BaseActiivty {
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.mTitle)
    MTitle mTitle;

    @Override
    public int bindLayout() {
        return R.layout.activity_test;
    }

    @Override
    public void initView() {
        mTitle.setCenterText("NDEF", 18, getResources().getColor(R.color.black));
    }

    @Override
    public void loadData() {

    }

}