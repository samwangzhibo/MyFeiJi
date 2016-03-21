package com.k.feiji.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.k.feiji.R;
import com.k.feiji.util.SharedPrefUtil;

/**
 * Created by zybang on 2016/3/17.
 */
public class SettingFragment extends Fragment implements View.OnClickListener{
    MyClickListener listener;
    SharedPrefUtil sharedPrefUtil = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPrefUtil = SharedPrefUtil.getInstance();

        View v = inflater.inflate(R.layout.setting , null);
        Button settingMusicOn = (Button) v.findViewById(R.id.setting_music_on);
        Button settingSoundOn = (Button) v.findViewById(R.id.setting_sound_on);
        Button settingBgflowOn = (Button) v.findViewById(R.id.setting_bgflow_on);
        if(!sharedPrefUtil.getBoolean("music")){
            settingMusicOn.setText(getString(R.string.setting_music_off));
        }
        if (!sharedPrefUtil.getBoolean("sound")){
            settingSoundOn.setText(getString(R.string.setting_sound_off));
        }
        if (!sharedPrefUtil.getBoolean("bgflow")){
            settingBgflowOn.setText(getString(R.string.setting_bgflow_off));
        }


        Button settingMusicStyle = (Button) v.findViewById(R.id.setting_music_style);
        Button settingBack = (Button) v.findViewById(R.id.setting_back);

        settingMusicOn.setOnClickListener(this);
        settingSoundOn.setOnClickListener(this);
        settingMusicStyle.setOnClickListener(this);
        settingBgflowOn.setOnClickListener(this);
        settingBack.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        listener.click(v);
    }

    public interface MyClickListener{
        void click(View v);
    }
    public void setMyClickListener(MyClickListener listener){
        this.listener = listener;
    }
}