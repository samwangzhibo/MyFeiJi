package com.k.feiji;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.k.feiji.util.MyCCGLSurfaceView;
import com.k.feiji.util.SharedPrefUtil;
import com.k.feiji.util.SoundPlayer;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.ccColor4B;

public class FeiJi_Main extends FeiJi_BaseAc{

	private MyCCGLSurfaceView _FeiJi_Surface;
	private long lastTime = 0;
	private CCScene _FeiJi_Scene;
	SoundPlayer soundPlayer;
	SharedPrefUtil sharedPrefUtil = SharedPrefUtil.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_FeiJi_Surface = new MyCCGLSurfaceView(this);
		// TODO: 2016/4/3 del
		//_FeiJi_Surface.setBackgroundResource(R.drawable.feiji_background);
		_FeiJi_Surface.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		_FeiJi_Surface.getHolder().setFormat(PixelFormat.TRANSPARENT);
		_FeiJi_Surface.setZOrderOnTop(true);//这句不能少

		//_FeiJi_Surface.setEGLContextClientVersion(2);

		Log.e("wzb", _FeiJi_Surface.getContentDescription()+"");

		ActivityManager am =(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		Log.e("wzb", "gl版本" + info.getGlEsVersion());

		setContentView(_FeiJi_Surface);

		soundPlayer = SoundPlayer.getInstance();
		if (!soundPlayer.isInit()){
			int resourse = sharedPrefUtil.getBgMusic("BgMusic");
			soundPlayer.init(this,resourse);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CCDirector.sharedDirector().end();
		CCTextureCache.sharedTextureCache().removeAllTextures();
	}

	@Override
	protected void onPause() {
		super.onPause();
		CCDirector.sharedDirector().pause();
		StatService.onPause(this);
		soundPlayer.pauseMusic();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!FeiJi_Play.getIsClickPause()){
			CCDirector.sharedDirector().resume();
		}
		StatService.onResume(this);
		soundPlayer.startMusic();

	}

	@Override
	protected void onStart() {
		super.onStart();
		// cocos2d里习惯以左下角作为原点,时间单位是秒
		CCDirector.sharedDirector().attachInView(_FeiJi_Surface);// 把cocos2d绑定在GLSurfaceView这个载体上

		CCDirector.sharedDirector().setDeviceOrientation(
				CCDirector.kCCDeviceOrientationLandscapeLeft);
		//CCDirector.sharedDirector().setDisplayFPS(true);//显示 FPS
		CCDirector.sharedDirector().setAnimationInterval(1.0f / 60.0f);
		// 每秒的贞数
		_FeiJi_Scene = CCScene.node();
		
		Bundle bundle = getIntent().getExtras();
		boolean iskg = false;
		if (bundle != null) {
			iskg = bundle.getBoolean("isgk");
		}
		FeiJi_Play _Layer1 = new FeiJi_Play(ccColor4B.ccc4(255, 255, 255, 255),iskg);
		//FeiJi_Play2 _Layer = new FeiJi_Play2(ccColor4B.ccc4(255, 255, 255, 255));
		//_Layer1.GetContext(FeiJi_Main.this);
		//_FeiJi_Scene.addChild(_Layer);
		_FeiJi_Scene.addChild(_Layer1);
		
		CCDirector.sharedDirector().runWithScene(_FeiJi_Scene);// 运行场景

		//CCDirector.sharedDirector().pause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		CCDirector.sharedDirector().end();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                 && event.getRepeatCount() == 0) {
			 long nowTime = System.currentTimeMillis();
			 if (nowTime - lastTime < 1000){
				 return super.onKeyDown(keyCode, event);
			 }
			Toast.makeText(FeiJi_Main.this, "再次点击回到主菜单", Toast.LENGTH_SHORT).show();
			lastTime = System.currentTimeMillis();
             return true;
         }
         return super.onKeyDown(keyCode, event);
     }

}
