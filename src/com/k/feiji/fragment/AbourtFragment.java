package com.k.feiji.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.k.feiji.FeiJi_Menu;
import com.k.feiji.R;

/**
 * Created by zybang on 2016/3/17.
 */
public class AbourtFragment extends Fragment {
    FeiJi_Menu ctx = null;
    @Override
    public void onAttach(Activity activity) {
        ctx = (FeiJi_Menu) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.abourt , null);
        if (getActivity() != null){
            Button abourtBack= (Button) v.findViewById(R.id.abourt_back);
            abourtBack.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    ctx.getSupportFragmentManager().beginTransaction().remove(AbourtFragment.this).commit();
                    ctx.showLL();
                }
            });
        }
        return v;
    }
}