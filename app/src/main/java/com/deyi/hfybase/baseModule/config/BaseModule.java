package com.deyi.hfybase.baseModule.config;

/**
 * @author hfy
 * @description
 * @time 2020/10/12
 */
public class BaseModule {
    private static BaseModuleConfig sbaseModuleConfig;

    private BaseModule() {

    }

    public static void initialize(BaseModuleConfig baseModuleConfig) {
        sbaseModuleConfig = baseModuleConfig;
    }

    public static BaseModuleConfig getBaseModuleConfig() {
        if (sbaseModuleConfig == null) {
            return BaseModuleConfig.newBuilder().build();
        } else {
            return sbaseModuleConfig;
        }
    }

}
