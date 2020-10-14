package com.deyi.hfybase.baseModule.http;

import com.deyi.hfybase.baseModule.config.BaseModule;

/**
 * @author hfy
 * @description
 * @time 2020/10/12
 */
public class Result<T> {
    public String msg;

    public int code;

    public T data;

    public boolean isSuccess() {
        return code == BaseModule.getBaseModuleConfig().getServerSuccessCode();
    }

}
