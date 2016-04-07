package com.k.feiji.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zybang on 2016/3/18.
 */
public class SharedPrefUtil {
    private static SharedPrefUtil instance = null;
    private SharedPreferences sharedPreferences = null;
    private final String TAG = "feiji";
    private SharedPrefUtil(){
    }
    public synchronized static SharedPrefUtil getInstance(){
        if (instance == null){
            instance = new SharedPrefUtil();
        }
        return instance;
    }
    public void init(Context context){
        if (sharedPreferences == null)
        sharedPreferences =  context.getSharedPreferences(TAG , Context.MODE_PRIVATE);
    }

    public void putBoolean(String name , boolean value){
        if (sharedPreferences != null){
            sharedPreferences.edit().putBoolean(name, value).commit();
        }
    }
    public boolean getBoolean(String name){
        if (sharedPreferences != null){
            return sharedPreferences.getBoolean(name , true);
        }
        return true;
    }
    public int getBgMusic(String name){
        if (sharedPreferences != null){
            return sharedPreferences.getInt(name, SoundPlayer.musicId);
        }
        return SoundPlayer.musicId;
    }
    public void putBgMusic(String name , int value){
        if (sharedPreferences != null){
            sharedPreferences.edit().putInt(name, value).commit();
        }
    }
    public int getBgIm(String name){
        if (sharedPreferences != null){
            return sharedPreferences.getInt(name, 0);
        }
        return SoundPlayer.musicId;
    }
}
