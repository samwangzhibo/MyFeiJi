package com.k.feiji.activity;

import android.app.Activity;
import android.app.Application;

import com.k.feiji.util.WebUtil;

import java.lang.ref.WeakReference;

/**
 * Created by wzb on 2016/4/26.
 */
public class BaseApp extends Application {
    private static WeakReference<Activity> topActivity;
    public static Application application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initNetWork();
    }

    private void initNetWork() {
        WebUtil.init(getApplicationContext());
    }
    public static Activity getTopActivity(){
        return topActivity == null ? null : topActivity.get();
    }
    public static Application getApplication(){
        return application;
    }
}
