package com.k.feiji.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.k.feiji.R;

/**
 * Created by zybang on 2016/3/17.
 */
public class SettingFragment extends Fragment implements View.OnClickListener{
    MyClickListener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setting , null);
        Button settingMusicOn = (Button) v.findViewById(R.id.setting_music_on);
        Button settingSoundOn = (Button) v.findViewById(R.id.setting_sound_on);
        Button settingMusicStyle = (Button) v.findViewById(R.id.setting_music_style);
        Button settingBack = (Button) v.findViewById(R.id.setting_back);

        settingMusicOn.setOnClickListener(this);
        settingSoundOn.setOnClickListener(this);
        settingMusicStyle.setOnClickListener(this);
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