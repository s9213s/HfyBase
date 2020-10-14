package com.deyi.hfybase.baseModule.http;

import java.io.Serializable;

/**
 * @author hfy
 * @description
 * @time 2020/10/12
 */
public class SimpleResponse implements Serializable {

    private static final long serialVersionUID = -1477609349345966116L;

    public int code;
    public String msg;

    public Result toResultResponse() {
        Result result = new Result();
        result.code = code;
        result.msg = msg;
        return result;
    }
}
