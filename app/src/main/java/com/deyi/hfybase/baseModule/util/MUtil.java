package com.deyi.hfybase.baseModule.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.ScreenUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hfy
 * @description
 * @time 2020/10/12
 */

public class MUtil {
    /**
     * 比较版本号
     *
     * @param oldver 旧版本号
     * @param newver 新版本号
     * @return
     */
    public static boolean versionCheck(String oldver, String newver) {
        List<String> compareOld = new ArrayList<>();
        List<String> compareNew = new ArrayList<>();
        String[] vs1 = oldver.split("\\.");
        String[] vs2 = newver.split("\\.");
        for (String a : vs1) {
            compareOld.add(a);
        }
        for (String a : vs2) {
            compareNew.add(a);
        }
        if (compareOld.size() != 3) compareOld.add("0");
        if (compareNew.size() != 3) compareNew.add("0");
        try {
            for (int i = 0; i < compareOld.size(); i++) {
                if (Integer.valueOf(compareNew.get(i)) > Integer.valueOf(compareOld.get(i))) {
                    return true;
                } else if (Integer.valueOf(compareNew.get(i)) == Integer.valueOf(compareOld.get(i))) {
                    continue;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getVersionName(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "2.0";
        }
    }

    public static void installApp(Context mContext, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".versionProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri,
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }


    /**
     * 检查判断是否有sd卡
     */
    public static boolean checkSDCard() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        return sdCardExist;

    }

    /**
     * 获取当前屏幕的密度
     */
    public static float dpTopx(float dp) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        return dp * displayMetrics.density;
    }

    /**
     * 根据750的比例转换坐标
     */
    public static int getScale(int value) {
        return ScreenUtils.getScreenWidth() * value / 750;
    }


    /**
     * 判断是否存在虚拟导航栏
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }

    /**
     * 获取虚拟导航栏的高度
     */
    /**
     * 获取虚拟功能键高度
     *
     * @param context
     * @return
     */
    public static int getNavBarHeight(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    public static int getScreenRealHeight(Context context) {
        return ScreenUtils.getScreenHeight() - getNavBarHeight(context);
    }

    public static void hideNavBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * 获取需要下载的文件大小
     */
    @SuppressLint("checkResult")
    public static void getDownFileSize(final String fileUrl, final UtilListener<Integer> listener) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                try {
                    URL url = new URL(fileUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int fileLength = connection.getContentLength();
                    Log.d("marcus", "文件大小为：" + fileLength + "btye");
                    emitter.onNext(fileLength);
                    emitter.onComplete();
                } catch (IOException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        listener.callBack(integer);
                    }
                });
    }

    public interface UtilListener<T> {
        void callBack(T t);
    }


}

