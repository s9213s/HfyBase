package com.deyi.hfybase.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.deyi.hfybase.baseModule.util.ParseJsonUtil;

import java.util.List;

/**
 * @author hfy
 * @description
 * @time 2020/10/13
 */
public class SpUtil {
    private static final String USER = "user";
    private Context mContext;
    private SharedPreferences sp;
    public SpUtil(Context context) {
        this.mContext = context;
        sp = context.getSharedPreferences(USER, 0);
    }

    public void setToken(String token) {
        sp.edit().putString("token", token).commit();
    }

    public String getToken() {
        return sp.getString("token", "");
    }
    /**
     * 设置/获取是否已登录
     */
    public boolean isLogin() {
        return !TextUtils.isEmpty(getToken());
    }

    /**
     * 判断是否是初次打开App
     */
    public boolean isFirstOpen() {
        SharedPreferences sp = mContext.getSharedPreferences(USER, Context.MODE_PRIVATE);
        return sp.getBoolean("open", false);
    }

    public void setFirstOpen() {
        SharedPreferences sp = mContext.getSharedPreferences(USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("open", true);
        editor.apply();
    }
    public void setList(List<String> mlist) {
        String list_str = ParseJsonUtil.toJson(mlist);
        sp.edit().putString("mlist", list_str).commit();
    }

    public List<String> getList() {
        List<String> aipaiDraft = ParseJsonUtil.getBeanList(sp.getString("mlist", ""), String.class);
        return aipaiDraft;
    }

//    //个人信息设置界面
//    public void setUserInfo2(String info) {
//        //方式一
//        sp.edit().putString("UserInfo2Bean", info).apply();
//        //方式二
//        SharedPreferences.Editor edit = mContext.getSharedPreferences(USER, Context.MODE_PRIVATE).edit();
//        edit.putString("UserInfo2Bean", info).apply();
//    }
//
//    public UserInfo2Bean getUserInfo2() {
//        SharedPreferences edit = mContext.getSharedPreferences(USER, Context.MODE_PRIVATE);
//        return ParseJsonUtil.getBean(edit.getString("UserInfo2Bean", ""), UserInfo2Bean.class);
//    }
}
