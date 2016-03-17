package com.k.feiji;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.k.feiji.fragment.SettingFragment;
import com.k.feiji.util.SoundPlayer;

public class FeiJi_Menu extends FeiJi_BaseAc implements OnClickListener{

	private Button _FeiJi_Button_New, _FeiJi_Button_Score,
			_FeiJi_Button_Setting,_FeiJi_Button_Guanka,_FeiJi_Button_Abourt;
	SoundPlayer soundPlayer;
	/**
	 * 容器
	 */
	private LinearLayout mll;
	private SettingFragment settFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feiji_menu);
		Init();
	}

	private void Init() {
		mll = (LinearLayout) findViewById(R.id.menu_ll);
		_FeiJi_Button_New = (Button) findViewById(R.id.feiji_bu_new);
		_FeiJi_Button_Score = (Button) findViewById(R.id.feiji_bu_score);
		_FeiJi_Button_Setting = (Button) findViewById(R.id.feiji_bu_setting);
		_FeiJi_Button_Guanka = (Button) findViewById(R.id.feiji_guanka_btn);
		_FeiJi_Button_Abourt = (Button) findViewById(R.id.feiji_abrout);

		_FeiJi_Button_New.setOnClickListener(this);
		_FeiJi_Button_Score.setOnClickListener(this);
		_FeiJi_Button_Setting.setOnClickListener(this);
		_FeiJi_Button_Guanka.setOnClickListener(this);
		_FeiJi_Button_Abourt.setOnClickListener(this);

		
		soundPlayer = SoundPlayer.getInstance();
		soundPlayer.init(this);
		soundPlayer.startMusic();
	}

	
	public void onResume() {
		super.onResume();
		/**
		 * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		StatService.onResume(this);
		soundPlayer.startMusic();
		
	}

	public void onPause() {
		super.onPause();

		/**
		 * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		StatService.onPause(this);
		soundPlayer.pauseMusic();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.feiji_bu_new:
			Intent intent = new Intent(FeiJi_Menu.this, FeiJi_Main.class);
			startActivity(intent);
			break;

		case R.id.feiji_bu_score:
			Intent i = new Intent(FeiJi_Menu.this, FeiJi_Score.class);
			startActivity(i);
			break;

		case R.id.feiji_bu_setting:
			mll.setVisibility(View.INVISIBLE);
			if (settFragment == null) {
				settFragment = new SettingFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,settFragment).commit();
			}
			if (settFragment.isHidden()){
				getSupportFragmentManager().beginTransaction().show(settFragment).commit();
			}
			settFragment.setMyClickListener(new SettingFragment.MyClickListener() {
				@Override
				public void click(View view) {
					switch (view.getId()) {
						case R.id.setting_back:
							mll.setVisibility(View.VISIBLE);
							getSupportFragmentManager().beginTransaction().hide(settFragment).commit();
							break;
						case R.id.setting_music_on:
							Button v = (Button) view;
							if (v.getText().toString().equals(getResources().getString(R.string.setting_music_on))) {
								v.setText(getString(R.string.setting_music_off));
							} else {
								v.setText(getString(R.string.setting_music_on));
							}
							break;
						case R.id.setting_sound_on:
							Button v1 = (Button) view;
							if (v1.getText().toString().equals(getResources().getString(R.string.setting_sound_on))) {
								v1.setText(getString(R.string.setting_sound_off));
							} else {
								v1.setText(getString(R.string.setting_sound_on));
							}
							break;
						case R.id.setting_music_style:
							addChoiceDialog();
							break;
				}
				}
			});
			break;

		case R.id.feiji_guanka_btn:
			Intent intentGk = new Intent(FeiJi_Menu.this, FeiJi_Main.class);
			intentGk.putExtra("isgk", true);
			startActivity(intentGk);
			break;

		case R.id.feiji_abrout:

			break;
	}
}
	private void addChoiceDialog(){
		Dialog alertDialog = new AlertDialog.Builder(this).
				setTitle("背景音主题")
				.setItems(SoundPlayer.MUSICS, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch(which){
							case 0:
								soundPlayer.changeAndPlayMusic(SoundPlayer.musicId);
								break;
							case 1:
								soundPlayer.changeAndPlayMusic(SoundPlayer.musicHDL);
								break;
						}
						Toast.makeText(FeiJi_Menu.this, "当前主题： "+SoundPlayer.MUSICS[which], Toast.LENGTH_SHORT).show();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).
						create();
		alertDialog.show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		soundPlayer.releseMusic();
	}

	private void addDialog(){
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle("飞机大战");
		dialog.setMessage("确定退出吗？");
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			addDialog();
		}
		return super.onKeyDown(keyCode, event);
	}
}