package com.deyi.hfybase.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.deyi.hfybase.R;
import com.deyi.hfybase.base.BaseActiivty;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hfy
 * @description
 * @time 2020/10/13
 */
public class TestActivity extends BaseActiivty {
    @BindView(R.id.text)
    TextView text;

    @Override
    public int bindLayout() {
        return R.layout.activity_test;
    }

    @Override
    public void initView() {
        /*
        型号  高度  - 宽度 - 厚度
        12pro 146.7 - 71.5 - 7.4  187g
        12    146.7 - 71.5 - 7.4  162g
        11pro 144.0 - 71.4 - 8.1  188g
        12min 131.5 - 64.2 - 7.4  133g
        11    150.9 - 75.7 - 8.3  194g
        XS    143.6 - 70.9 - 7.7  177g
        8     138.4 - 67.3 - 7.3  148g
        SE    138.4 - 67.3 - 7.3  148g
         */
    }

    @Override
    public void loadData() {

    }

}
