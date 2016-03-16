package com.k.feiji;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.mobstat.StatService;
import com.k.feiji.util.SoundPlayer;

public class FeiJi_Menu extends FeiJi_BaseAc implements OnClickListener{

	private Button _FeiJi_Button_New, _FeiJi_Button_Score,
			_FeiJi_Button_Exit,_FeiJi_Button_Guanka;
	SoundPlayer soundPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feiji_menu);
		Init();
	}

	private void Init() {
		_FeiJi_Button_New = (Button) findViewById(R.id.feiji_bu_new);
		_FeiJi_Button_Score = (Button) findViewById(R.id.feiji_bu_score);
		_FeiJi_Button_Exit = (Button) findViewById(R.id.feiji_bu_exit);
		_FeiJi_Button_Guanka = (Button) findViewById(R.id.feiji_guanka_btn);

		_FeiJi_Button_New.setOnClickListener(this);
		_FeiJi_Button_Score.setOnClickListener(this);
		_FeiJi_Button_Exit.setOnClickListener(this);
		_FeiJi_Button_Guanka.setOnClickListener(this);
		
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

		case R.id.feiji_bu_exit:
			finish();
			break;

		case R.id.feiji_guanka_btn:
			Intent intentGk = new Intent(FeiJi_Menu.this, FeiJi_Main.class);
			intentGk.putExtra("isgk", true);
			startActivity(intentGk);
			break;
	}
}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		soundPlayer.releseMusic();
	}
}