package com.deyi.hfybase.baseModule.event;

/**
 * @author hfy
 * @description
 * @time 2020/10/12
 */
public class MEventBean {
    public int action;

    public Object result;

    public MEventBean(int action, Object result) {
        this.action = action;
        this.result = result;
    }

}
