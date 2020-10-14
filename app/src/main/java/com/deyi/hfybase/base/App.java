package com.deyi.hfybase.base;

import com.deyi.hfybase.R;
import com.deyi.hfybase.baseModule.base.MApp;
import com.deyi.hfybase.baseModule.config.BaseModule;
import com.deyi.hfybase.baseModule.config.BaseModuleConfig;

/**
 * @author hfy
 * @description
 * @time 2020/10/12
 */
public class App extends MApp {
    @Override
    protected void init() {
        BaseModule.initialize(BaseModuleConfig.newBuilder()
                .setLoadingViewStyle("LineSpinFadeLoaderIndicator")
//                .setServerSuccessCode(10000)
//                .addInterceptor(new ParamsInterceptor())//拦截器
                .setLoadingViewColor(R.color.colorPrimary).build());
    }
}
