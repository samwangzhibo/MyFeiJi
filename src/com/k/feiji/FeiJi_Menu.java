package com.k.feiji;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.k.feiji.fragment.AbourtFragment;
import com.k.feiji.fragment.SettingFragment;
import com.k.feiji.util.SharedPrefUtil;
import com.k.feiji.util.SoundPlayer;
import com.k.feiji.util.ZoomOutPageTransformer;
import com.k.feiji.view.bgDialog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FeiJi_Menu extends FeiJi_BaseAc implements OnClickListener{

	private Button _FeiJi_Button_New, _FeiJi_Button_Score,
			_FeiJi_Button_Setting,_FeiJi_Button_Guanka,_FeiJi_Button_Abourt;
	SoundPlayer soundPlayer;
	SharedPrefUtil sharedPrefUtil = SharedPrefUtil.getInstance();
	bgDialog.DemoCollectionPagerAdapter adapter;
	List<ImageView> datas = new ArrayList<>();
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

		sharedPrefUtil.init(getApplicationContext());
		soundPlayer = SoundPlayer.getInstance();
		int resourse = sharedPrefUtil.getBgMusic("BgMusic");

		soundPlayer.init(this,resourse);
		soundPlayer.musicSt = sharedPrefUtil.getBoolean("musicST");
		soundPlayer.soundSt = sharedPrefUtil.getBoolean("sound");

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
			}
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.frag_container, settFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();

			/*if (settFragment.isHidden()){
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.show(settFragment);
				fragmentTransaction.commit();
			}*/
			settFragment.setMyClickListener(new SettingFragment.MyClickListener() {
				@Override
				public void click(View view) {
					switch (view.getId()) {
						case R.id.setting_back:
							mll.setVisibility(View.VISIBLE);
							getSupportFragmentManager().beginTransaction().remove(settFragment).commit();
							break;
						case R.id.setting_music_on:
							Button v = (Button) view;
							if (v.getText().toString().equals(getResources().getString(R.string.setting_music_on))) {
								v.setText(getString(R.string.setting_music_off));
								soundPlayer.setMusicSt(false);
								sharedPrefUtil.putBoolean("musicST",false);
							} else {
								v.setText(getString(R.string.setting_music_on));
								soundPlayer.setMusicSt(true);
								sharedPrefUtil.putBoolean("musicST", true);
							}
							break;
						case R.id.setting_sound_on:
							Button v1 = (Button) view;
							if (v1.getText().toString().equals(getResources().getString(R.string.setting_sound_on))) {
								v1.setText(getString(R.string.setting_sound_off));
								soundPlayer.setSoundSt(false);
								sharedPrefUtil.putBoolean("sound", false);
							} else {
								v1.setText(getString(R.string.setting_sound_on));
								soundPlayer.setSoundSt(true);
								sharedPrefUtil.putBoolean("sound", true);
							}
							break;
						case R.id.setting_music_style:
							addChoiceDialog();
							break;
						case R.id.setting_bgflow_on:
							Button v2 = (Button) view;
							if (v2.getText().toString().equals(getResources().getString(R.string.setting_bgflow_on))) {
								v2.setText(getString(R.string.setting_bgflow_off));
								sharedPrefUtil.putBoolean("bgflow", false);
							} else {
								v2.setText(getString(R.string.setting_bgflow_on));
								sharedPrefUtil.putBoolean("bgflow", true);
							}
							break;
						case R.id.setting_Invincible_off:
							Button v3 = (Button) view;
							if (v3.getText().toString().equals(getResources().getString(R.string.setting_Invincible_off))) {
								v3.setText(getString(R.string.setting_Invincible_on));
								sharedPrefUtil.putBoolean("InvincibleST", true);
							} else {
								v3.setText(getString(R.string.setting_Invincible_off));
								sharedPrefUtil.putBoolean("InvincibleST", false);
							}
							break;
						case R.id.setting_select_bg:
							if (!settFragment.isHidden())
							getSupportFragmentManager().beginTransaction().hide(settFragment).commit();

							bgDialog myBgDialog = bgDialog.createDialogWithContentView(FeiJi_Menu.this);
							myBgDialog.setCallBack(new bgDialog.CallBack() {
								@Override
								public void callback() {
									if (settFragment.isHidden())
										getSupportFragmentManager().beginTransaction().show(settFragment).commit();
								}
							});
							View contentview = myBgDialog.getContentView();
							ViewPager myviewpager = (ViewPager) contentview.findViewById(R.id.myviewpager);
							if (datas.size() != 4) {
								AssetManager assetManager = getAssets();
								for (int i = 0; i < 4; i++) {
									try {
										InputStream is;
										if (i == 3) {
											is = assetManager.open("images/feiji_background.png");
										} else {
											is = assetManager.open("images/map_bg" + i + ".png");
										}
										Bitmap a = BitmapFactory.decodeStream(is);
										ImageView im = new ImageView(FeiJi_Menu.this);
										im.setImageBitmap(a);
										datas.add(im);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								adapter = new bgDialog.DemoCollectionPagerAdapter(datas);
							}
							myviewpager.setAdapter(adapter);
							//viewpager添加切换效果
							myviewpager.setPageTransformer(true, new ZoomOutPageTransformer());
							myBgDialog.show();
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
			mll.setVisibility(View.INVISIBLE);
			FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
			fragmentTransaction2.replace(R.id.frag_container,  new AbourtFragment());
			fragmentTransaction2.commit();
			break;
	}
}

	/**
	 * 展示菜单
	 */
	public void showLL(){
		if (mll != null && mll.getVisibility() == View.INVISIBLE){
			mll.setVisibility(View.VISIBLE);
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
								sharedPrefUtil.putBgMusic("BgMusic",SoundPlayer.musicId);
								break;
							case 1:
								soundPlayer.changeAndPlayMusic(SoundPlayer.musicHDL);
								sharedPrefUtil.putBgMusic("BgMusic", SoundPlayer.musicHDL);
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