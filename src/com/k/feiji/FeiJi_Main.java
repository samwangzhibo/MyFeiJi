package com.k.feiji;

import android.os.Bundle;
import android.view.KeyEvent;

import com.baidu.mobstat.StatService;
import com.k.feiji.util.SharedPrefUtil;
import com.k.feiji.util.SoundPlayer;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.ccColor4B;

public class FeiJi_Main extends FeiJi_BaseAc {

	private CCGLSurfaceView _FeiJi_Surface;
	private CCScene _FeiJi_Scene;
	SoundPlayer soundPlayer;
	SharedPrefUtil sharedPrefUtil = SharedPrefUtil.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_FeiJi_Surface = new CCGLSurfaceView(this);
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
//		_Layer.GetContext(FeiJi_Main.this);
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
        	
             return true;
         }
         return super.onKeyDown(keyCode, event);
     }
}
