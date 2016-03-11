package com.k.feiji;

import com.baidu.mobstat.StatService;
import com.k.feiji.util.SoundPlayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
		// TODO Auto-generated method stub
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
		 * ҳ����ʼ��ÿ��Activity�ж���Ҫ��ӣ�����м̳еĸ�Activity���Ѿ�����˸õ��ã���ô��Activity����ز�����ӣ�
		 * ������StatService.onPageStartһ��onPageEnd��������ʹ��
		 */
		StatService.onResume(this);
		soundPlayer.startMusic();
		
	}

	public void onPause() {
		super.onPause();

		/**
		 * ҳ�������ÿ��Activity�ж���Ҫ��ӣ�����м̳еĸ�Activity���Ѿ�����˸õ��ã���ô��Activity����ز�����ӣ�
		 * ������StatService.onPageStartһ��onPageEnd��������ʹ��
		 */
		StatService.onPause(this);
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