package com.deyi.hfybase.baseModule.http;

import android.app.Activity;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.deyi.hfybase.R;
import com.deyi.hfybase.baseModule.config.BaseModule;
import com.deyi.hfybase.baseModule.config.BaseModuleConfig;
import com.deyi.hfybase.baseModule.util.MToast;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.exception.StorageException;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author hfy
 * @description
 * @time 2020/10/12
 */
public abstract class MCallBack<T> extends JsonCallback<T> {
    LoadingDialog mDialog;

    boolean showDialog;

    Activity activity;

    public MCallBack(Activity activity) {
        this(activity, true);
    }

    public MCallBack(Activity activity, boolean showDialog) {
        this.showDialog = showDialog;
        this.activity = activity;
        initLoadingDialog(activity);
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        if (mDialog != null && showDialog && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    private void initLoadingDialog(Activity activity) {
        BaseModuleConfig config = BaseModule.getBaseModuleConfig();
        mDialog = new LoadingDialog(activity, config.getDialogStyle());
        mDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onError(Response<T> response) {
        super.onError(response);
        Throwable exception = response.getException();
        if (exception != null) exception.printStackTrace();
        if (exception instanceof ConnectException || exception instanceof UnknownHostException) {
            MToast.showToast(activity, activity.getResources().getString(R.string.error_please_check_network));
        } else if (exception instanceof SocketTimeoutException) {
            MToast.showToast(activity, activity.getResources().getString(R.string.error_timeout));
        } else if (exception instanceof HttpException) {
            MToast.showToast(activity, activity.getResources().getString(R.string.error_not_found_server));
        } else if (exception instanceof JsonSyntaxException || exception instanceof JsonIOException || exception instanceof JSONException) {
            MToast.showToast(activity, activity.getResources().getString(R.string.error_json));
        } else if (exception instanceof StorageException) {
            MToast.showToast(activity, activity.getResources().getString(R.string.download_error_storage));
        } else {
            MToast.showToast(activity, activity.getResources().getString(R.string.error_unknow));
        }
    }

}
