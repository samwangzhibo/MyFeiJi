package com.k.feiji;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.k.feiji.util.SharedPrefUtil;
import com.k.feiji.util.SoundPlayer;

import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteSheet;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressLint("NewApi")
public class FeiJi_Play extends CCColorLayer {

	private static final String TAG = FeiJi_Play.class.getSimpleName();
	private List<FeiJi_Sprite> _Foes = new CopyOnWriteArrayList<FeiJi_Sprite>(); //中小飞机
	private List<FeiJi_Sprite> _BigFoes = new CopyOnWriteArrayList<FeiJi_Sprite>(); //大飞机
	private List<CCSprite> _Shots = new CopyOnWriteArrayList<CCSprite>();// 自己的子弹
	private List<CCSprite> _Foes_Shots = new CopyOnWriteArrayList<CCSprite>(); //敌机子弹
	private List<CCSprite> _Red_Bombs = new CopyOnWriteArrayList<CCSprite>(); //红绿炸弹
	private List<FeiJi_Sprite> _AllFoes = new CopyOnWriteArrayList<FeiJi_Sprite>(); //全部飞机
	//文本框
	private CCLabel _ScoreLabel;
	private CCLabel _TargetScoreLabel;
	private CCLabel _Red_Bomb_Num;
	private CCLabel Feiji_Life;
	//精灵
	private CCSprite _Red_Bomb;
	private CCSprite _FeiJi_Play; //主飞机
	private CCSprite _FeiJi_Pause;
	private CGPoint _Touch_Location;

	private String _Font_Path = "Cookies.ttf";
	private String _FeiJi_Back_Path1 = "images/feiji_background.png";
	private String _FeiJi_Back_Path2 = "images/map_bg0.png";
	private String _FeiJi_Back_Path3 = "images/map_bg1.png";
	private String _FeiJi_Back_Path4 = "images/map_bg2.png";
	private String _FeiJi_Actual_Back_Path;

	private String _MiddleFoe_Path_2 = "images/middlefoe_2.png";
	private String _SmallFoe_Path = "images/smallfoe.png";
	private String _MiddleFoe_Path = "images/middlefoe.png";
	private String _BigFoe_Path = "images/bigfoe.png";
	private String _BigFoe_Path2 = "images/bigfoe2.png";
	private String _BigFoe_Path_2 = "images/bigfoe_2.png"; //飞机被打状态
	private String _Play_Path = "images/play.png";
	private String _Play_Path2 = "images/play2.png";
	private String _Shot_Path = "images/shot.png";
	private String _Pause_Path = "images/pause.png";
	private String _Red_Bomb_Down_Path = "images/red_bomb_down.png";
	private String _Red_Bomb_Path = "images/red_bomb.png";
	private String _Blue_Shot_Down_Path = "images/blue_shot_down.png";
	private String _Blue_Shot = "images/blue_shot.png";
	private int _Get_Score = 0;// 获得的分数
	/**
	 * 目标的分数（关卡模式有效）
	 */
	private int _Target_Score = 40000;
	private int _Level = 1;//关卡数
	/**
	 * 子弹速度（可调控）
	 */
	private float _Shot_Du = 0.5f;
	/**
	 * 大飞机子弹速度（可调控）
	 */
	private float BigFoe_Shot_Du = 2.0f;
	/**
	 * 根据手指位置判断飞机是否可以移动
	 */
	private boolean _Can_Move = false;
	private CGSize _WinSize;
	private int _Play_Image_Chage = 1;// 飞机图片判断
	private int _BigFoe_Image_Chage = 1;// 大型飞机图片判断

	private int _Big_Life = 16, _Middle_Life = 8, _Small_life = 2;// 敌机生命
	private int _ChangeImage_Delay = -1;//图片改变延时
	private int _Pause_OR = -1;// 暂停点击判断
	private int Red_Bomb_Num = 0;//红炸弹数
	private int Feiji_Life_Num = 3;//飞机血量
	private int Big_Shot_Index = 1;
	private int Small_Foe_Down1_Index = 1;
	/**
	 * 背景是否移动
	 */
	private boolean Is_Bg_Move = false;
	/**
	 * 大飞机最大数量
	 */
	private int BigFoes_Max_Size = 2;
	/**
	 * 是否蓝子弹
	 */
	private boolean Blue_Shot_Change = false;
	private long Blue_Shot_Last_Time = 0;//蓝子弹持续时间
	private int Blue_Red_Down_Time = 5;//蓝子弹和红子弹的随机数
	private int FoeDown_Time = 8;//敌机下落速度
	private boolean _Invincible = false; //无敌模式
	private boolean isStart = false;//是否开始
	/**
	 * 是否是关卡模式
	 */
	private boolean _IsGK = false;
	private int startNum = 3;
	CCLabel _Wel_Label;
	/**
	 * 是否是自己点击的暂停，防止resume后dialog还在，背景却在播放的情况
	 */
	private static boolean isClickPause = false;
	//飞机爆炸序列
	private String _SmallFoe_Sequence_Path = "images/smallfoe_seq.png";
	private String _MiddleFoe_Sequence_Path = "images/middlefoe_seq.png";
	private String _BigFoe_Sequence_Path = "images/bigfoe_seq.png";
	private String _Play_Sequence_Path = "images/play_seq.png";

	private SoundPlayer soundPlayer;
	private SharedPreferences _Share;
	private SharedPrefUtil sharedPrefUtil = SharedPrefUtil.getInstance();
	private String ScoreList = "0;0;0;0;0;0;0;0;0;0";
	private Dialog _Dialog;
	private Feiji_Guanka feiji_Guanka;//关卡管理类

	public static boolean getIsClickPause(){
		return isClickPause;
	}

	protected FeiJi_Play(ccColor4B color) {
		super(color);
		Init();
	}

	protected FeiJi_Play(ccColor4B color,boolean isgk) {
		super(color);
		this._IsGK = isgk;
		Init();
	}
	private void Init() {
		Is_Bg_Move = sharedPrefUtil.getBoolean("bgflow");
		_Invincible = sharedPrefUtil.getBoolean("InvincibleST");

		_Share = CCDirector.sharedDirector().getActivity()
				.getSharedPreferences("Share", Context.MODE_PRIVATE);
		_WinSize = CCDirector.sharedDirector().displaySize();//获取屏幕大小
		setIsTouchEnabled(false);

		int a = new Random().nextInt(4)+1;
		switch (a){
			case 1:
				_FeiJi_Actual_Back_Path = _FeiJi_Back_Path1;
				break;
			case 2:
				_FeiJi_Actual_Back_Path = _FeiJi_Back_Path1;
				break;
			case 3:
				_FeiJi_Actual_Back_Path = _FeiJi_Back_Path1;
				break;
			case 4:
				_FeiJi_Actual_Back_Path = _FeiJi_Back_Path1;
				break;
		}

		CCSprite _FeiJi_Back = CCSprite.sprite(_FeiJi_Actual_Back_Path);
		//背景按屏幕比例放大
		_FeiJi_Back.setScaleX(_WinSize.width
				/ _FeiJi_Back.getTexture().getWidth());
		_FeiJi_Back.setScaleY(_WinSize.height
				/ _FeiJi_Back.getTexture().getHeight() * 1.1f);
		_FeiJi_Back.setPosition(CGPoint.make(_WinSize.width / 2,
				_WinSize.height / 2));// 默认中心点为左下角
		_FeiJi_Back.setTag(1);
		if(Is_Bg_Move) {
			BgMove(_FeiJi_Back);
		}
		addChild(_FeiJi_Back);// 添加背景到场景

		if(Is_Bg_Move) {
			CCSprite _FeiJi_Back2 = CCSprite.sprite(_FeiJi_Actual_Back_Path);
			//背景按屏幕比例放大
			_FeiJi_Back2.setScaleX(_WinSize.width
					/ _FeiJi_Back.getTexture().getWidth());
			_FeiJi_Back2.setScaleY(_WinSize.height
					/ _FeiJi_Back.getTexture().getHeight() * 1.1f);
			_FeiJi_Back2.setPosition(CGPoint.make(_WinSize.width / 2,
					_WinSize.height / 2 * 3));// 默认中心点为左下角
			_FeiJi_Back2.setTag(2);
			BgMove(_FeiJi_Back2);
			addChild(_FeiJi_Back2);// 添加背景到场景
		}



		_FeiJi_Pause = CCSprite.sprite(_Pause_Path);
		_FeiJi_Pause.setPosition(CGPoint.make(
				_FeiJi_Pause.getContentSize().width / 2 + 1, _WinSize.height
						- _FeiJi_Pause.getContentSize().height / 2 - 1));
		addChild(_FeiJi_Pause);// 添加暂停

		feiji_Guanka = Feiji_Guanka.getInstance();
		AddScore(); //第一次默认加载分数

		this.schedule("welLabel", 1f);

			this.schedule("GameFoes", 2f);// 0.5秒执行一次 添加敌机 包括动画
			this.schedule("GameShot", 0.2f);//0.2秒发射一次
			GamePlay();//添加飞机
			this.schedule("AddPlay", 0.2f); //主飞机精灵变化
			this.schedule("Detection", 0f);
			this.schedule("AddRedBlueDown", 2.0f); //道具下降 包括动画
			this.schedule("AddBigFoe", 0.2f); //大飞机精灵变化

			this.schedule("AddBigShot", 2.5f); //大飞机发子弹

		AddRedBomb();
		showLife();

		soundPlayer = SoundPlayer.getInstance();
		soundPlayer.startMusic();
	}
	public void showLife(){
		if (Feiji_Life != null){
			Feiji_Life.removeSelf();
		}
		Feiji_Life = CCLabel.makeLabel("血量："+Feiji_Life_Num, _Font_Path, 30);
		Feiji_Life.setColor(ccColor3B.ccRED);
		Feiji_Life.setString("血量：" + Feiji_Life_Num);
		Feiji_Life.setPosition(CGPoint.ccp(_WinSize.getWidth() -
				Feiji_Life.getTexture().getWidth() / 2 - 50, Feiji_Life.getContentSize().height / 2));
		addChild(Feiji_Life);// 将关卡目标分添加到场景
	}
	public void GameFoes(float t) {
		if(isStart) {
			AddFoes();
		}
	}
	public void welLabel(float t) {
		if (_Wel_Label != null)
			_Wel_Label.removeSelf();
		if (startNum == 0 ) {
			this.unschedule("welLabel");
			isStart = true;
			setIsTouchEnabled(true);
			return;
		}
		_Wel_Label = CCLabel.makeLabel(""+startNum, _Font_Path,
				200);
		_Wel_Label.setColor(ccColor3B.ccBLACK);
		_Wel_Label.setPosition(CGPoint.ccp(
				_WinSize.getWidth() / 2,
				_WinSize.getHeight() / 2));
		addChild(_Wel_Label);// 添加炸弹数
		startNum--;
	}
	public void GamePlay() {
		if(isStart) {
			AddPlay();
		}
	}

	public void GameShot(float t) {
		if(isStart) {
			AddShot();
		}
	}

	/**
	 * 大飞机发3次子弹
	 * @param t
	 */
	public void BigShot3(float t) {
		if (Big_Shot_Index != 4){
			for (FeiJi_Sprite bigFeiji : _BigFoes){

				//添加子弹
				CCSprite _FeiJi_Shot = CCSprite.sprite(_Shot_Path);
				CCSprite BigFoe =  bigFeiji.getCCSprite();

				float localX = 0, localY = 0;

				localX = bigFeiji.getInitX() ;
				localY = bigFeiji.getInitY() - BigFoe.getContentSize().height/2 - _FeiJi_Shot.getContentSize().height/2 - 20;


				_FeiJi_Shot.setPosition(localX, localY);
				addChild(_FeiJi_Shot);

				_Foes_Shots.add(_FeiJi_Shot);//添加子弹
				/*	float[] a =  getShotToPosition(localX, localY, _FeiJi_Play.getPosition().x, _FeiJi_Play.getPosition().y, _WinSize.width, _WinSize.height);
					CCFiniteTimeAction fs_timeAction = CCMoveTo.action(BigFoe_Shot_Du, CGPoint
							.ccp(a[0],a[1]));// CCMoveTo*/

				CCFiniteTimeAction fs_timeAction = CCMoveTo.action(BigFoe_Shot_Du, CGPoint
						.ccp(_FeiJi_Play.getPosition().x, _FeiJi_Play.getPosition().y));// CCMoveTo
				CCCallFuncN fs_Over = null;
				fs_Over = CCCallFuncN.action(this, "Foes_Shot_Over");

				CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_Over);
				_FeiJi_Shot.runAction(fs_actions);

			}
			Big_Shot_Index++;
		}else {
			unschedule("BigShot3");
			Big_Shot_Index = 1;
			return;
		}
	}
	/**
	 * 添加大敌机射击
	 * @param t
	 */
	public void AddBigShot(float t){
		if(isStart) {
			//如果没有大飞机返回
			if (_BigFoes.size() == 0)
				return;
			else{
				this.schedule("BigShot3", 0.2f); //大飞机发子弹
			}
		}
	}
	public float[] getShotToPosition(float localx, float localy, float mplayx, float mplayy, float windowx, float windowy){
		float[] a = new float[2];
		float deltax = Math.abs(mplayx - localx);
		float deltay = Math.abs(mplayy - localy);
		if (mplayx < localx){
			a[0] = localx - (deltay / deltax) * windowx;
		}else {
			a[0] = localx + (deltay / deltax) * windowx;
		}
		if (mplayy > localy) {
			a[1] = localy + (deltay / deltax) * windowy; //斜率乘以长度
		}else {
			a[1] = localy - (deltay / deltax) * windowy;
		}
		Log.e("wzb  ------ >","x  === "+a[0]+"    y ===   "+a[1]);
		return a;
	}
	/**
	 * 随机添加红绿炸弹下降
	 */
	public void AddRedBlueDown(float t) {
		if(isStart) {
			Random rand = new Random();
			int randomValue = rand.nextInt(Blue_Red_Down_Time);
			CCSprite _Red_Blue_Down = null;

			if (randomValue != 0 && randomValue != 1) {
				return;
			}
			if (_Red_Bombs.size() >= 1) {
				return;
			}

			if (randomValue == 0 && _Red_Bombs.size() < 1) {
				_Red_Blue_Down = CCSprite.sprite(_Red_Bomb_Down_Path); //红炸弹下落图片
				_Red_Blue_Down.setTag(0);
			} else if (randomValue == 1 && _Red_Bombs.size() < 1) {
				_Red_Blue_Down = CCSprite.sprite(_Blue_Shot_Down_Path); //绿炸弹下落图片
				_Red_Blue_Down.setTag(1);
			}
			int minX = (int) (_Red_Blue_Down.getContentSize().width / 2.0f);
			int maxX = (int) (_WinSize.width - _Red_Blue_Down.getContentSize().width / 2.0f);
			int rangeX = maxX - minX;
			int actualX = rand.nextInt(rangeX) + minX;

			_Red_Blue_Down
					.setPosition(actualX, _Red_Blue_Down.getContentSize().height
							/ 2.0f + _WinSize.height); //定义在屏幕外
			addChild(_Red_Blue_Down);
			_Red_Bombs.add(_Red_Blue_Down);
			CCFiniteTimeAction fs_timeAction = CCMoveTo.action(1,
					CGPoint.ccp(actualX, _WinSize.height / 3 * 2));// 掉落到 2/3

			CCCallFuncN fs_back = CCCallFuncN.action(this, "Red_Bomb_Back");
			CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_back);
			_Red_Blue_Down.runAction(fs_actions);
			Log.e(TAG, "------- RedBlue开始下落");
		}
	}

	/**
	 * 添加红色炸弹，炸弹数>0 才渲染
	 */
	private void AddRedBomb() {
		if (_Red_Bomb_Num != null)
			_Red_Bomb_Num.removeSelf();
		if (_Red_Bomb != null)
			_Red_Bomb.removeSelf();
		if (Red_Bomb_Num > 0) {
			_Red_Bomb = CCSprite.sprite(_Red_Bomb_Path);
			_Red_Bomb.setPosition(CGPoint.make(
					_Red_Bomb.getContentSize().width / 2 + 1,
					_Red_Bomb.getContentSize().height / 2 + 1));
			addChild(_Red_Bomb);

			_Red_Bomb_Num = CCLabel.makeLabel("x" + Red_Bomb_Num, _Font_Path,
					30);
			_Red_Bomb_Num.setColor(ccColor3B.ccBLACK);
			_Red_Bomb_Num.setPosition(CGPoint.ccp(
					_Red_Bomb.getContentSize().width + 5
							+ _Red_Bomb_Num.getContentSize().width / 2,
					_Red_Bomb.getContentSize().height / 2));
			addChild(_Red_Bomb_Num);// 添加炸弹数
		}

	}

	/**
	 * 炸弹上移到 5/6
	 */
	public void Red_Bomb_Back(Object sender) {
		CCSprite Red_Bomb = (CCSprite) sender;
		Red_Bomb.setPosition(Red_Bomb.getPosition().x, _WinSize.height / 3 * 2);
		CCFiniteTimeAction fs_timeAction = CCMoveTo.action(
				0.5f,
				CGPoint.ccp(Red_Bomb.getPosition().x, _WinSize.height / 3 * 2
						+ _WinSize.height / 6)); //上移到屏幕6分之5处
		CCCallFuncN fs_back = CCCallFuncN.action(this, "Red_Bomb_Back2");
		CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_back);
		Red_Bomb.runAction(fs_actions);
	}

	/**
	 * 红色炸弹第二次下落
	 */
	public void Red_Bomb_Back2(Object sender) {
		CCSprite Red_Bomb = (CCSprite) sender;
		Red_Bomb.setPosition(Red_Bomb.getPosition().x,
				_WinSize.height / 3 * 2 + 50);
		CCFiniteTimeAction fs_timeAction = CCMoveTo.action(
				1.0f,
				CGPoint.ccp(Red_Bomb.getPosition().x,
						-(Red_Bomb.getContentSize().height / 2)));//红色炸弹移动到屏幕外
		CCCallFuncN fs_Over = CCCallFuncN.action(this, "Red_Bomb_Over");
		CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_Over);
		Red_Bomb.runAction(fs_actions);
	}

	/**
	 * 添加敌机，并下落 默认30中 1的概率打飞机 2的概率中飞机
	 */
	private void AddFoes() {

		Random rand = new Random();
		int randomValue = rand.nextInt(10);
		FeiJi_Sprite _FeiJi_Foe = new FeiJi_Sprite();
		_FeiJi_Foe.setClicked_Or(false);
		if (randomValue == 0 && _BigFoes.size() < BigFoes_Max_Size) {
			_FeiJi_Foe.setLift(_Big_Life);
			_FeiJi_Foe.setMax_Life(_Big_Life);
			_FeiJi_Foe.setCCSprite(_BigFoe_Path);
			FoeDown(_FeiJi_Foe, 0);     //敌机下落  动画
			soundPlayer.playSound(R.raw.big_spaceship_flying);
		} else if (randomValue == 1 || randomValue == 2 || randomValue == 3) {
			_FeiJi_Foe.setLift(_Middle_Life);
			_FeiJi_Foe.setMax_Life(_Middle_Life);
			_FeiJi_Foe.setCCSprite(_MiddleFoe_Path);
			FoeDown(_FeiJi_Foe, 1);
		} else {
			_FeiJi_Foe.setLift(_Small_life);
			_FeiJi_Foe.setMax_Life(_Small_life);
			_FeiJi_Foe.setCCSprite(_SmallFoe_Path);
			FoeDown(_FeiJi_Foe, 2);
			SmallFejiType(1);
		}
		_AllFoes.add(_FeiJi_Foe);
	}

	/**
	 * 小飞机AI
	 * @param type
	 */
	private void SmallFejiType(int type) {
		switch (type){
			case 1:
				this.schedule("smallFoeDown1",0.2f);
				break;
			default:
				break;
		}
	}

	/**
	 * 小飞机第一种飞行效果
	 * @param t
	 */
	public void smallFoeDown1(float t) {
		if (Small_Foe_Down1_Index != 4) {
			Log.e("wzb ------ >","添加小飞机"+Small_Foe_Down1_Index);
			// TODO: 2016/3/31 添加多架飞机
			FeiJi_Sprite _FeiJi_Foe = new FeiJi_Sprite();
			_FeiJi_Foe.setClicked_Or(false);
			_FeiJi_Foe.setLift(_Small_life);
			_FeiJi_Foe.setMax_Life(_Small_life);
			_FeiJi_Foe.setCCSprite(_SmallFoe_Path);
			_FeiJi_Foe.getCCSprite().setTag(2); //小飞机

			Random rand = new Random();
			int actualX = (int) (0 -_FeiJi_Foe.getCCSprite().getContentSize().width / 2); //实际x位置

			_FeiJi_Foe.setInitX(actualX);
			_FeiJi_Foe.setInitDuration(2);
			_FeiJi_Foe.setInitY(_WinSize.height * 2 / 3); //飞机头部刚刚在外面
			CCSprite _Feiji_Sprite = _FeiJi_Foe.getCCSprite();
			_Feiji_Sprite.setPosition(
					actualX, _WinSize.height * 2 / 3);
			double a = Math.toDegrees(Math.atan (_WinSize.height / (3 * _WinSize.width)));
			Log.e("wzb", a+" degrees");
			_Feiji_Sprite.setRotation((float) -(90-a));
			addChild(_Feiji_Sprite);

			// TODO: 2016/3/17 增加飞机路径的AI

			CCFiniteTimeAction fs_timeAction = CCMoveTo.action(2,
					CGPoint.ccp(_WinSize.width + _FeiJi_Foe.getCCSprite().getContentSize().width / 2,  _WinSize.height / 3));//实践内移动，注意不是moveBy
			CCCallFuncN fs_Over = null; //动画对象

			fs_Over = CCCallFuncN.action(this, "Foe_Over");

			CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_Over);//执行完动画，执行"BigFoe_Over"方法
			_FeiJi_Foe.getCCSprite().runAction(fs_actions);

			_AllFoes.add(_FeiJi_Foe);
			_Foes.add(_FeiJi_Foe);
			Small_Foe_Down1_Index++;
		} else {
			unschedule("smallFoeDown1");
			Small_Foe_Down1_Index = 1;
			return;
		}
	}
	/**
	 * 让主飞机背景一直变化
	 */
	public void AddPlay(float t) {
		_Play_Image_Chage = -_Play_Image_Chage;
		if (_FeiJi_Play != null) {
			_FeiJi_Play.removeSelf();
		}
		if (_Play_Image_Chage == 1) {
			_FeiJi_Play = CCSprite.sprite(_Play_Path);
		} else {
			_FeiJi_Play = CCSprite.sprite(_Play_Path2);
		}
		if (_Touch_Location == null) {
			_Touch_Location = CCDirector.sharedDirector().convertToGL(
					CGPoint.ccp(_WinSize.width / 2, _WinSize.height
							- _FeiJi_Play.getContentSize().height / 2));
		}
		_FeiJi_Play.setPosition(_Touch_Location.x, _Touch_Location.y);
		addChild(_FeiJi_Play);
	}

	/**
	 * 添加主飞机
	 */
	private void AddPlay() {
		_FeiJi_Play = CCSprite.sprite(_Play_Path);
		_FeiJi_Play.setPosition(_WinSize.width / 2,
				_FeiJi_Play.getContentSize().height / 2);
		addChild(_FeiJi_Play);
	}

	/**
	 * 让大飞机背景一直变化
	 */
	public void AddBigFoe(float t) {
		if(isStart) {
			if (_BigFoes.size() > 0) {
				_BigFoe_Image_Chage = -_BigFoe_Image_Chage;
				FeiJi_Sprite BigFoe = (FeiJi_Sprite) _BigFoes.get(0);
				if (_Play_Image_Chage == 1)
					ChageSpriteBack(BigFoe, false, _BigFoe_Path, 0, BigFoe.getCCSprite().getRotation());
				else
					ChageSpriteBack(BigFoe, false, _BigFoe_Path2, 0, BigFoe.getCCSprite().getRotation());
				BigFoe.getCCSprite().removeSelf();
				_BigFoes.remove(0);
			}
		}
	}

	/**
	 * 添加飞机子弹
	 */
	private void AddShot() {
		CCSprite _FeiJi_Shot = null;

		if (Blue_Shot_Change) {
			_FeiJi_Shot = CCSprite.sprite(_Blue_Shot);
			_FeiJi_Shot.setTag(1);
			if (System.currentTimeMillis() - Blue_Shot_Last_Time > 10000) {
				Blue_Shot_Change = false;
			}
		} else {
			soundPlayer.biu();
			_FeiJi_Shot = CCSprite.sprite(_Shot_Path);
			_FeiJi_Shot.setTag(0);
		}

		float localX = 0, localY = 0;

		if (_Touch_Location == null) {
			localX = _WinSize.width / 2;
			localY = _FeiJi_Play.getContentSize().height
					+ _FeiJi_Shot.getContentSize().height / 2;
		} else {
			localX = _Touch_Location.x;
			localY = _Touch_Location.y + _FeiJi_Play.getContentSize().height
					/ 2 + _FeiJi_Shot.getContentSize().height / 2;
		}

		_FeiJi_Shot.setPosition(localX, localY);
		addChild(_FeiJi_Shot);

		_Shots.add(_FeiJi_Shot);//添加子弹

		CCFiniteTimeAction fs_timeAction = CCMoveBy.action(_Shot_Du, CGPoint
				.ccp(localX - localX, _FeiJi_Shot.getContentSize().height / 2
						+ _WinSize.height));// CCMoveBy
		CCCallFuncN fs_Over = null;
		fs_Over = CCCallFuncN.action(this, "Shot_Over");

		CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_Over);
		_FeiJi_Shot.runAction(fs_actions);
	}

	/**
	 * 添加敌机,并落下
	 */
	private void FoeDown(FeiJi_Sprite _FeiJi_Foe2, int i) {
		Random rand = new Random();
		int minX = (int) (_FeiJi_Foe2.getCCSprite().getContentSize().width / 2.0f);
		int maxX = (int) (_WinSize.width - _FeiJi_Foe2.getCCSprite()
				.getContentSize().width / 2.0f);
		int rangeX = maxX - minX;
		int actualX = rand.nextInt(rangeX) + minX; //实际x位置

		int minDuration = FoeDown_Time - 4;
		int maxDuration = FoeDown_Time;
		if (_Get_Score > 1000000) {
			maxDuration = FoeDown_Time - 2;
			minDuration = FoeDown_Time - 5;
		}
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;//真实速度

		if (actualDuration < 0) {
			actualDuration = rand.nextInt(2) + 2;
		}

		_FeiJi_Foe2.setInitX(actualX);
		_FeiJi_Foe2.setInitDuration(actualDuration);
		_FeiJi_Foe2.setInitY(_FeiJi_Foe2.getCCSprite().getContentSize().height
				/ 2.0f + _WinSize.height); //飞机头部刚刚在外面

		_FeiJi_Foe2.getCCSprite().setPosition(
				actualX,
				_FeiJi_Foe2.getCCSprite().getContentSize().height / 2.0f
						+ _WinSize.height);
		addChild(_FeiJi_Foe2.getCCSprite());

		if (i == 0) {
			_FeiJi_Foe2.getCCSprite().setTag(0);
			_BigFoes.add(_FeiJi_Foe2);
		} else {
			if (i == 1) {
				_FeiJi_Foe2.getCCSprite().setTag(1); //中飞机
			} else {
				_FeiJi_Foe2.getCCSprite().setTag(2); //小飞机
			}
			_Foes.add(_FeiJi_Foe2);
		}
		// TODO: 2016/3/17 增加飞机路径的AI

		CCFiniteTimeAction fs_timeAction = CCMoveTo.action(actualDuration,
				CGPoint.ccp(actualX, -(_FeiJi_Foe2.getCCSprite()
						.getContentSize().height / 2)));//实践内移动，注意不是moveBy
		CCCallFuncN fs_Over = null; //动画对象
		if (i == 0)
			fs_Over = CCCallFuncN.action(this, "BigFoe_Over");
		else
			fs_Over = CCCallFuncN.action(this, "Foe_Over");

		CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_Over);//执行完动画，执行"BigFoe_Over"方法
		_FeiJi_Foe2.getCCSprite().runAction(fs_actions);
	}

	/**
	 *  添加飞机背景变化后的下降动画
	 * @param _FeiJi_Foe 敌机
	 * @param i 类型
	 * @param y y坐标
	 */
	private void Down(FeiJi_Sprite _FeiJi_Foe, int i, float y) {

		_FeiJi_Foe.getCCSprite().setPosition(_FeiJi_Foe.getInitX(), y);
		addChild(_FeiJi_Foe.getCCSprite());

		if (i == 0) {
			_FeiJi_Foe.getCCSprite().setTag(0);
			_BigFoes.add(_FeiJi_Foe);
		} else {
			if (i == 1) {
				_FeiJi_Foe.getCCSprite().setTag(1);
			} else {
				_FeiJi_Foe.getCCSprite().setTag(2);
			}
			_Foes.add(_FeiJi_Foe);
		}

		CCFiniteTimeAction fs_timeAction = CCMoveTo.action(_FeiJi_Foe
				.getInitDuration(), CGPoint.ccp(_FeiJi_Foe.getInitX(),
				-(_FeiJi_Foe.getCCSprite().getContentSize().height / 2)));//moveto的位置
		CCCallFuncN fs_Over = null;
		if (i == 0)
			fs_Over = CCCallFuncN.action(this, "BigFoe_Over");
		else
			fs_Over = CCCallFuncN.action(this, "Foe_Over");

		CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_Over);
		_FeiJi_Foe.getCCSprite().runAction(fs_actions);
	}

	/**
	 * 添加动画结束，清除小中敌机
	 */
	public void Foe_Over(Object sender) {
		CCSprite foe_over = (CCSprite) sender;
		foe_over.removeSelf(); //移除节点

		for (int i = 0; i < _Foes.size(); i++) {
			FeiJi_Sprite foe = _Foes.get(i);
			if (foe.getCCSprite() == foe_over) {
				_Foes.remove(i); //数组移除
				break;
			}
		}
	}

	/**
	 * 添加动画结束清除敌机
	 */
	public void BigFoe_Over(Object sender) {
		CCSprite bigfoe_over = (CCSprite) sender;
		bigfoe_over.removeSelf();

		for (int i = 0; i < _BigFoes.size(); i++) {
			FeiJi_Sprite foe = _BigFoes.get(i);
			if (foe.getCCSprite() == bigfoe_over) {
				_BigFoes.remove(i);
				break;
			}
		}
		ReMoveAllShot();
	}

	/**
	 *   移除精灵
	 */
	public void Shot_Over(Object sender) {
		CCSprite shot_over = (CCSprite) sender;
		_Shots.remove(shot_over);
		shot_over.removeSelf();
	}
	/**
	 *   移除敌机子弹
	 */
	public void Foes_Shot_Over(Object sender) {
		CCSprite shot_over = (CCSprite) sender;
		_Foes_Shots.remove(shot_over);
		shot_over.removeSelf();
	}

	/**
	 * 红色炸弹消失
	 */
	public void Red_Bomb_Over(Object sender) {
		CCSprite bomb_over = (CCSprite) sender;
		bomb_over.removeSelf();
		_Red_Bombs.remove(bomb_over);
	}
	
	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		CGPoint Location = CCDirector.sharedDirector().convertToGL(
				CGPoint.ccp(event.getX(), event.getY()));//手指位置
		CGRect Rect = _FeiJi_Play.getBoundingBox();
		CGRect Rect2 = _FeiJi_Pause.getBoundingBox();
		CGRect Rect3 = null;
		if (_Red_Bomb != null)
			Rect3 = _Red_Bomb.getBoundingBox();
		if (CGRect.containsPoint(Rect, Location) && _Pause_OR != 1) {//点击飞机
			_Can_Move = true;
		} else {
			_Can_Move = false;
			if (CGRect.containsPoint(Rect2, Location)) {
				_Pause_OR = -_Pause_OR;
				
				if (_Pause_OR == 1) {
					ShowPauseDialog();
					//ShowPauseDialog();_Can_Move = false;_Pause_OR = -_Pause_OR;
					//CCDirector.sharedDirector().pause();
				} else {
					CCDirector.sharedDirector().resume();
				}
			}
			if (_Pause_OR != 1) {
				if (_Red_Bomb != null && CGRect.containsPoint(Rect3, Location)
						&& Red_Bomb_Num > 0) {
					//红色炸弹音
					soundPlayer.playSound(R.raw.use_bomb);
					Red_Bomb_Num--;
					AddRedBomb(); //更新红炸弹label
					ReMoveAll(); //移除所有飞机
				}
			}
		}

		return super.ccTouchesBegan(event);
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		return super.ccTouchesEnded(event);
	}

	@Override
	public boolean ccTouchesMoved(MotionEvent event) {

		if (_Can_Move) {
			_Touch_Location = CCDirector.sharedDirector().convertToGL(
					CGPoint.ccp(event.getX(), event.getY()));
			_FeiJi_Play.setPosition(_Touch_Location.x, _Touch_Location.y);
		}
		return super.ccTouchesMoved(event);
	}

	/**
	 * 控制飞机的碰撞
	 */
	private void PlayOver() {
		CGRect Rect = _FeiJi_Play.getBoundingBox();
		CGRect Rect3 = CGRect.make(Rect.origin.x + (Rect.size.width / 3.2f),
				Rect.origin.y, (Rect.size.width / 3.2f), Rect.size.height);
		//添加子弹和主机的撞击碰撞
		for(int j=0 ; j < _Foes_Shots.size(); j++){
			CCSprite BigFoeShot =  _Foes_Shots.get(j);
			CGRect Rect2 = BigFoeShot.getBoundingBox();
			if (CGRect.intersects(Rect2, Rect3)) { //相交

				_Foes_Shots.remove(BigFoeShot);
				BigFoeShot.removeSelf();

				if (!_Invincible) {
					Feiji_Life_Num--;
					showLife();
					if (Feiji_Life_Num == 0) {
						//游戏结束音效
						playEndSound();

						StopSchedule();

						AddPlaySpriteAnimal(_FeiJi_Play.getPosition(),
								_Play_Sequence_Path, 99, 123, 4);
						_FeiJi_Play.removeSelf();
					}
				}
			}
		}

		for (int j = 0; j < _BigFoes.size(); j++) {
			FeiJi_Sprite BigFoe = (FeiJi_Sprite) _BigFoes.get(j);
			CGRect Rect2 = BigFoe.getCCSprite().getBoundingBox();
			if (CGRect.intersects(Rect2, Rect3)) { //相交

				AddSpriteAnimal(BigFoe.getCCSprite().getPosition(),
						_BigFoe_Sequence_Path, 164, 245, 6, BigFoe.getCCSprite().getRotation());
				soundPlayer.playSound(R.raw.enemy2_down);
				ChageScore(30000);
				BigFoe.getCCSprite().removeSelf();
				_BigFoes.remove(j);

				if (!_Invincible) {
					Feiji_Life_Num--;
					showLife();
					if (Feiji_Life_Num == 0) {
						//游戏结束音效
						playEndSound();

						StopSchedule();

						AddPlaySpriteAnimal(_FeiJi_Play.getPosition(),
								_Play_Sequence_Path, 99, 123, 4);
						_FeiJi_Play.removeSelf();
					}
				}
			}
		}
		for (int j = 0; j < _Foes.size(); j++) {
			FeiJi_Sprite Foe = _Foes.get(j);
			CGRect Rect2 = Foe.getCCSprite().getBoundingBox();
			if (CGRect.intersects(Rect2, Rect3)) {

				if (Foe.getCCSprite().getTag() == 2) {
					AddSpriteAnimal(Foe.getCCSprite().getPosition(),
							_SmallFoe_Sequence_Path, 52, 52, 3, Foe.getCCSprite().getRotation());
					soundPlayer.playSound(R.raw.enemy1_down);
					ChageScore(1000);
				} else {
					AddSpriteAnimal(Foe.getCCSprite().getPosition(),
							_MiddleFoe_Sequence_Path, 69, 87, 4, Foe.getCCSprite().getRotation());
					soundPlayer.playSound(R.raw.enemy3_down);
					ChageScore(6000);
				}
				Foe.getCCSprite().removeSelf();
				_Foes.remove(j);

				if (!_Invincible) {
					Feiji_Life_Num--;
					showLife();
					if (Feiji_Life_Num == 0) {
						//游戏结束音效
						playEndSound();

						StopSchedule();

						AddPlaySpriteAnimal(_FeiJi_Play.getPosition(),
								_Play_Sequence_Path, 99, 123, 4);
						_FeiJi_Play.removeSelf();
					}
				}
			}
		}
	}
	private void playEndSound(){
		if (soundPlayer != null) {
			soundPlayer.pauseMusic();
			soundPlayer.playSound(R.raw.game_over);
		}
	}
	/**
	 *  停止持续的方法
	 */
	private void StopSchedule() {
		this.unschedule("GameFoes");
		this.unschedule("GameShot");
		this.unschedule("AddPlay");
		this.unschedule("Detection");
		this.unschedule("AddRedBlueDown");
	}


	/**
	 * 碰撞监听
	 *
	 * @param t
	 */
	public void Detection(float t) {

		for (int i = 0; i < _Shots.size(); i++) {
			CCSprite Shot = _Shots.get(i);
			CGRect Rect = Shot.getBoundingBox();
			for (int j = 0; j < _BigFoes.size(); j++) {
				FeiJi_Sprite BigFoe = (FeiJi_Sprite) _BigFoes.get(j);
				CGRect Rect2 = BigFoe.getCCSprite().getBoundingBox();
				if (CGRect.intersects(Rect2, Rect)) {  //边界相交
					_ChangeImage_Delay = 0;
					if (Shot.getTag() == 1) //蓝子弹-2滴血
						BigFoe.Life -= 2;
					else
						BigFoe.Life--;

					_Shots.remove(Shot);
					Shot.removeSelf();

					if (BigFoe.Life <= 0) {
						BigFoe.getCCSprite().removeSelf();
						_BigFoes.remove(j);
						ChageScore(30000);
						//大飞机毁灭音效
						soundPlayer.playSound(R.raw.enemy2_down);
						AddSpriteAnimal(BigFoe.getCCSprite().getPosition(), //精灵消失帧动画
								_BigFoe_Sequence_Path, 164, 245, 6, BigFoe.getCCSprite().getRotation());
					} else {
						ChageSpriteBack(BigFoe, true, _BigFoe_Path_2, 0, BigFoe.getCCSprite().getRotation()); //改变背景
						BigFoe.getCCSprite().removeSelf();
						_BigFoes.remove(j);
					}
				}
				else {
					_ChangeImage_Delay++;
					if (BigFoe.getClicked_Or() && _ChangeImage_Delay >= 10) {
						ChageSpriteBack(BigFoe, false, _BigFoe_Path, 0, BigFoe.getCCSprite().getRotation());
						BigFoe.getCCSprite().removeSelf();
						_BigFoes.remove(j);
					}
				}
			}

			for (int j = 0; j < _Foes.size(); j++) {
				FeiJi_Sprite Foe = _Foes.get(j);
				CGRect Rect2 = Foe.getCCSprite().getBoundingBox();
				if (CGRect.intersects(Rect2, Rect)) {
					_ChangeImage_Delay = 0;
					if (Shot.getTag() == 1)
						Foe.Life -= 2;
					else
						Foe.Life--;
					_Shots.remove(Shot);
					Shot.removeSelf();
					if (Foe.Life <= 0) {
						if (Foe.getCCSprite().getTag() == 2) {
							ChageScore(1000);
							soundPlayer.playSound(R.raw.enemy1_down);
							AddSpriteAnimal(Foe.getCCSprite().getPosition(),
									_SmallFoe_Sequence_Path, 52, 52, 3, Foe.getCCSprite().getRotation());
						} else {
							ChageScore(6000);
							soundPlayer.playSound(R.raw.enemy3_down);
							AddSpriteAnimal(Foe.getCCSprite().getPosition(),
									_MiddleFoe_Sequence_Path, 69, 87, 4, Foe.getCCSprite().getRotation());
						}
						Foe.getCCSprite().removeSelf();
						_Foes.remove(j);
					} else {
						if (Foe.getCCSprite().getTag() == 2) {

						} else {
							ChageSpriteBack(Foe, true, _MiddleFoe_Path_2, 1, Foe.getCCSprite().getRotation()); //添加被打效果 并下落
							Foe.getCCSprite().removeSelf();
							_Foes.remove(j);
						}
					}
				} else {
					_ChangeImage_Delay++;
					if (Foe.getClicked_Or() && _ChangeImage_Delay >= 10) {
						if (Foe.getCCSprite().getTag() == 2) {

						} else {
							ChageSpriteBack(Foe, false, _MiddleFoe_Path, 1, Foe.getCCSprite().getRotation());
							Foe.getCCSprite().removeSelf();
							_Foes.remove(j);
						}
					}
				}

			}
		}

		for (int i = 0; i < _Red_Bombs.size(); i++) {
			CCSprite bomb = _Red_Bombs.get(i);
			CGRect Rect = bomb.getBoundingBox();
			CGRect Rect2 = _FeiJi_Play.getBoundingBox();
			if (CGRect.intersects(Rect2, Rect)) { //红绿道具碰到
				bomb.removeSelf();
				_Red_Bombs.remove(bomb);
				if (bomb.getTag() == 0) {
					Red_Bomb_Num++;
					soundPlayer.playSound(R.raw.get_bomb);
					AddRedBomb(); //渲染红色炸弹界面
				} else {
					Blue_Shot_Change = true;
					soundPlayer.playSound(R.raw.get_double_bullet);
					Blue_Shot_Last_Time = System.currentTimeMillis();
				}
			}
		}

			PlayOver();

	}
	private void BgMove(CCSprite _FeiJi_Back){
		if (_FeiJi_Back != null){
			CCFiniteTimeAction fs_timeAction = null;
			if (_FeiJi_Back.getTag() == 1){
				fs_timeAction = CCMoveBy.action(10f,
						CGPoint.ccp(0, -_WinSize.getHeight()));//实践内移动，注意moveBy
			}else{
				fs_timeAction = CCMoveBy.action(20,
						CGPoint.ccp(0, -_WinSize.getHeight() * 2));//实践内移动，注意moveBy
			}

			/*CCCallFuncN fs_Over = null; //动画对象
			fs_Over = CCCallFuncN.action(this, "Foe_Over");
			CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_Over);//执行完动画，执行"BigFoe_Over"方法*/

			CCCallFuncN fs_Over = null; //动画对象
			fs_Over = CCCallFuncN.action(this, "Bg_Move_Down");

			CCSequence fs_actions = CCSequence.actions(fs_timeAction , fs_Over);
			_FeiJi_Back.runAction(fs_actions);
		}
	}
	public void Bg_Move_Down(Object sender) {
		CCSprite BgMove = (CCSprite) sender;
		BgMove.setPosition(_WinSize.getWidth() / 2, _WinSize.height / 2 * 3);
		CCFiniteTimeAction fs_timeAction = CCMoveBy.action(
				20f,
				CGPoint.ccp(0, -_WinSize.getHeight() * 2));
		CCCallFuncN fs_back = CCCallFuncN.action(this, "Bg_Move_Down");
		CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_back);
		BgMove.runAction(fs_actions);
	}

	/**
	 * 移除子弹
	 */
 	private void ReMoveAllShot(){
		if (_Foes_Shots !=null && _Foes_Shots.size() > 0){
			for (int j = 0; j < _Foes_Shots.size(); j++) {
				CCSprite bigShot =  _Foes_Shots.get(j);
				bigShot.removeSelf();
			}
			_Foes_Shots.clear();
		}
	}

	/**
	 *  移除所有飞机，包括动画
	 */
	private void ReMoveAll() {
		ReMoveAllShot();
		List<FeiJi_Sprite> _FoesAll = _Foes;
		List<FeiJi_Sprite> _BigFoesAll = _BigFoes;
		for (int j = 0; j < _FoesAll.size(); j++) {
			FeiJi_Sprite Foe = _FoesAll.get(j);
			Foe.Life = 0;
			if (Foe.Life <= 0) {
				if (Foe.getCCSprite().getTag() == 2) {
					ChageScore(1000);
					AddSpriteAnimal(Foe.getCCSprite().getPosition(), //添加敌机消失动画
							_SmallFoe_Sequence_Path, 52, 52, 3, Foe.getCCSprite().getRotation());
				} else {
					ChageScore(6000);
					AddSpriteAnimal(Foe.getCCSprite().getPosition(),
							_MiddleFoe_Sequence_Path, 69, 87, 4, Foe.getCCSprite().getRotation());
				}
				Foe.getCCSprite().removeSelf();
			}
		}
		_Foes.removeAll(_FoesAll);
		for (int j = 0; j < _BigFoesAll.size(); j++) {
			FeiJi_Sprite BigFoe = (FeiJi_Sprite) _BigFoesAll.get(j);
			BigFoe.Life = 0;
			if (BigFoe.Life <= 0) {
				BigFoe.getCCSprite().removeSelf();
				ChageScore(30000);
				AddSpriteAnimal(BigFoe.getCCSprite().getPosition(),
						_BigFoe_Sequence_Path, 164, 245, 6, BigFoe.getCCSprite().getRotation());
			}
		}
		_BigFoes.removeAll(_BigFoesAll);
	}

	/**
	 * 每次得分后改变分数
	 * 
	 * @param score 增加的分数
	 */
	private void ChageScore(int score) {
		_Get_Score += score;
		if(_IsGK){
			//通过得分 算出关卡
			_Level = feiji_Guanka.getLevel(_Get_Score);
			if(_Get_Score > _Target_Score){
				Log.e(TAG,"Level ---"+_Level);
				_Target_Score = feiji_Guanka.getTargetScore(_Get_Score);
				//关卡dialog
				showGKDialog();
				// TODO: 2016/3/17 增加难度算法
				//FoeDown_Time = FoeDown_Time >= 4 ? FoeDown_Time-1 : 4;
			}
		}
		AddScore();
	}

	/**
	 * 添加分数label
	 */
	private void AddScore() {

		if (_ScoreLabel != null)
			_ScoreLabel.removeSelf();
		_ScoreLabel = CCLabel.makeLabel("分数:" + _Get_Score, _Font_Path, 30);
		_ScoreLabel.setColor(ccColor3B.ccGREEN);
		_ScoreLabel.setString("分数:" + _Get_Score);
		_ScoreLabel.setPosition(CGPoint.ccp(_FeiJi_Pause.getContentSize().width
				+ 5 + _ScoreLabel.getTexture().getWidth() / 2, _WinSize.height
				- _FeiJi_Pause.getContentSize().height / 2));
		addChild(_ScoreLabel);// 将分数添加到场景

		if(_IsGK){
			if (_TargetScoreLabel != null)
				_TargetScoreLabel.removeSelf();
			_TargetScoreLabel = CCLabel.makeLabel("关卡："+_Level+"     目标分数:" + _Target_Score, _Font_Path, 30);
			_TargetScoreLabel.setColor(ccColor3B.ccRED);
			_TargetScoreLabel.setString("关卡：" + _Level + "     目标分数:" + _Target_Score);
			_TargetScoreLabel.setPosition(CGPoint.ccp(_ScoreLabel.getContentSize().width
					+ 50 + _TargetScoreLabel.getTexture().getWidth() / 2, _WinSize.height
					- _FeiJi_Pause.getContentSize().height / 2));

			addChild(_TargetScoreLabel);// 将关卡目标分添加到场景
		}

	}

	/**
	 * 添加敌机消失动画
	 * @param touchRect 敌机位置
	 * @param Path 图片序列路径
	 * @param CutW 剪切的宽度
	 * @param CutH 剪切的高度
	 * @param Cut 裁剪数
	 */
	private void AddSpriteAnimal(CGPoint touchRect, String Path, int CutW,
								 int CutH, int Cut, float rotation) {
		CCSpriteSheet boomSheet = CCSpriteSheet.spriteSheet(Path);

		//boomSheet.setRotation(0);

		this.addChild(boomSheet);

		CCSprite Sprite = CCSprite.sprite(boomSheet.getTexture(),
				CGRect.make(0, 0, CutW, CutH));
		Sprite.setRotation(rotation);// DONE: 2016/3/31 爆炸的时候旋转
		boomSheet.addChild(Sprite);
		Sprite.setPosition(touchRect.x, touchRect.y);
		int frameCount = 0;

		ArrayList<CCSpriteFrame> boomAnimFrames = new ArrayList<CCSpriteFrame>();

		for (int y = 0; y < 1; y++) {
			for (int x = 0; x < Cut; x++) {
				CCSpriteFrame frame = CCSpriteFrame.frame(
						boomSheet.getTexture(),
						CGRect.make(x * CutW, y * CutH, CutW, CutH),
						CGPoint.ccp(0, 0));
				boomAnimFrames.add(frame); //加载帧
				frameCount++;
				if (frameCount == Cut)
					break;
			}
		}
		CCAnimation boomAnimation = CCAnimation.animation("", (float) 0.1,
				boomAnimFrames);

		CCAnimate boomAction = CCAnimate.action(boomAnimation);

		CCCallFuncN actionAnimateDone = CCCallFuncN.action(this,
				"SpriteAnimationFinished");
		CCSequence actions = CCSequence.actions(boomAction, actionAnimateDone); //帧动画完成

		Sprite.runAction(actions);
	}


	/**
	 * 添加主机消失动画 结束游戏
	 */
	private void AddPlaySpriteAnimal(CGPoint touchRect, String Path, int CutW,
			int CutH, int Cut) {

		CCSpriteSheet boomSheet = CCSpriteSheet.spriteSheet(Path);

		this.addChild(boomSheet);

		CCSprite Sprite = CCSprite.sprite(boomSheet.getTexture(),
				CGRect.make(0, 0, CutW, CutH));
		boomSheet.addChild(Sprite);
		Sprite.setPosition(touchRect.x, touchRect.y);
		int frameCount = 0;

		ArrayList<CCSpriteFrame> boomAnimFrames = new ArrayList<CCSpriteFrame>();

		for (int y = 0; y < 1; y++) {
			for (int x = 0; x < Cut; x++) {
				CCSpriteFrame frame = CCSpriteFrame.frame(
						boomSheet.getTexture(),
						CGRect.make(x * CutW, y * CutH, CutW, CutH),
						CGPoint.ccp(0, 0));
				boomAnimFrames.add(frame);
				frameCount++;
				if (frameCount == Cut)
					break;
			}
		}
		CCAnimation boomAnimation = CCAnimation.animation("", (float) 0.2,
				boomAnimFrames);

		CCAnimate boomAction = CCAnimate.action(boomAnimation);

		CCCallFuncN actionAnimateDone = CCCallFuncN.action(this,
				"PlaySpriteAnimationFinished"); //主机毁灭  游戏结束
		CCSequence actions = CCSequence.actions(boomAction, actionAnimateDone);

		Sprite.runAction(actions);
	}

	/**
	 * 敌机消失后调用的方法
	 */
	public void SpriteAnimationFinished(Object sender) {
		CCSprite SpriteFinished = (CCSprite) sender;
		SpriteFinished.removeSelf();
	}

	/**
	 * 结束主机消失动画
	 */
	public void PlaySpriteAnimationFinished(Object sender) {
		if (_Share != null) {
			if (_Share != null)
				ScoreList = _Share.getString("SCORE", "0;0;0;0;0;0;0;0;0;0");
			String[] Scores = ScoreList.split(";");
			String[] Scores2 = ScoreList.split(";");
			String _Score_Value = "";
			boolean or = false;
			for (int i = 0; i < Scores.length; i++) {
				if (_Get_Score > Integer.valueOf(Scores[i]) && !or) {
					Scores[i] = _Get_Score + "";
					or = true;
					int j = i;
					while (j < Scores.length - 1) {
						Scores[j + 1] = Scores2[j];
						j++;
					}
				}
				if (i >= Scores.length - 1)
					_Score_Value += Scores[i];
				else
					_Score_Value += Scores[i] + ";";
			}
			Editor e = _Share.edit();
			e.putString("SCORE", _Score_Value);
			e.commit();
		}
		CCSprite SpriteFinished = (CCSprite) sender;
		SpriteFinished.removeSelf();
		ShowDialog();
	}

	/**
	 * 改变飞机背景  在血量没有为0时
	 * 
	 * @param Foe
	 * @param Click
	 * @param Path
	 * @param i 飞机类型
	 */
	private void ChageSpriteBack(FeiJi_Sprite Foe, boolean Click, String Path,
			int i, float rotation) {
		FeiJi_Sprite _FeiJi_Foe = new FeiJi_Sprite();
		_FeiJi_Foe.setClicked_Or(Click);
		_FeiJi_Foe.setLift(Foe.Life);
		_FeiJi_Foe.setCCSprite(Path);
		_FeiJi_Foe.getCCSprite().setRotation(rotation);
		_FeiJi_Foe.setInitX(Foe.getInitX());
		_FeiJi_Foe.setInitY(Foe.getCCSprite().getPosition().y);
		float _sudu = (Foe.getInitY() + _FeiJi_Foe.getCCSprite()
				.getContentSize().height / 2.0f)
				/ (float) Foe.getInitDuration();
		float time = ((Foe.getCCSprite().getPosition().y + _FeiJi_Foe
				.getCCSprite().getContentSize().height / 2.0f) / _sudu);
		_FeiJi_Foe.setInitDuration(time);
		Down(_FeiJi_Foe, i, Foe.getCCSprite().getPosition().y); //替换背景后 飞机下降
		Foe.getCCSprite().removeSelf();
	}

	private void ShowDialog() {
		CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {


				LayoutInflater inflater = LayoutInflater.from(CCDirector
						.sharedDirector().getActivity());
				View v = inflater.inflate(R.layout.feiji_dialog, null);//加载模板
				_Dialog = new Dialog(CCDirector.sharedDirector().getActivity(),
						R.style.Dialog_Style);
				_Dialog.setCancelable(false);
				_Dialog.setCanceledOnTouchOutside(false);
				_Dialog.setContentView(v);//设置布局
				Window window = _Dialog.getWindow();
				window.setWindowAnimations(R.style.mystyle);  //设置动画风格
				_Dialog.show();


				TextView _Score_TV = (TextView) v
						.findViewById(R.id.feiji_dialog_score);
				_Score_TV.setText(_Get_Score + "");
				Button _Return = (Button) v
						.findViewById(R.id.feiji_dialog_return);
				_Return.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						IntentToBack();
						_Dialog.dismiss();
					}
				});
			}
		});
	}

	/**
	 * 关卡dialog
	 */
	private void showGKDialog(){
		CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {

				LayoutInflater inflater = LayoutInflater.from(CCDirector
						.sharedDirector().getActivity());
				View v = inflater.inflate(R.layout.feiji_gk_dialog, null);
				_Dialog = new Dialog(CCDirector.sharedDirector().getActivity(),
						R.style.Dialog_Style);
				_Dialog.setCancelable(true);
				_Dialog.setCanceledOnTouchOutside(true);
				_Dialog.setContentView(v);
				Window window = _Dialog.getWindow();
				window.setWindowAnimations(R.style.GKDialogAnimStyle);
				_Dialog.show();

				TextView textView = (TextView) v
						.findViewById(R.id.feiji_now_level);
				textView.setText("Level: " + _Level);
				textView.postDelayed(new Runnable() {
					@Override
					public void run() {
						_Dialog.dismiss();
					}
				}, 1000);
			}
		});
	}

	private void ShowPauseDialog() {
		CCDirector.sharedDirector().pause();
		soundPlayer.pauseMusic();
		CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				LayoutInflater inflater = LayoutInflater.from(CCDirector
						.sharedDirector().getActivity());
				View v = inflater.inflate(R.layout.feiji_pause_dialog, null);
				_Dialog = new Dialog(CCDirector.sharedDirector().getActivity(),
						R.style.Dialog_Style);
				_Dialog.setCancelable(false);
				_Dialog.setCanceledOnTouchOutside(false);
				_Dialog.setContentView(v);
				Window window = _Dialog.getWindow();
				window.setWindowAnimations(R.style.mystyle);
				_Dialog.show();
				
				Button _Pause_TV = (Button) v
						.findViewById(R.id.feiji_pause_resume);
				_Pause_TV.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						_Can_Move = false;
						_Pause_OR = -_Pause_OR;
						CCDirector.sharedDirector().resume();
						_Dialog.dismiss();
						soundPlayer.startMusic();
						isClickPause = false;
					}
				});
				
				Button _Return = (Button) v
						.findViewById(R.id.feiji_dialog_return);
				_Return.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						IntentToBack();
						_Dialog.dismiss();
						soundPlayer.startMusic();
						isClickPause = false;
					}
				});
				Button feiji_pause_restart = (Button) v
						.findViewById(R.id.feiji_pause_restart);
				feiji_pause_restart.setOnClickListener(new OnClickListener() {

					@TargetApi(Build.VERSION_CODES.HONEYCOMB)
					@Override
					public void onClick(View v) {
						CCDirector.sharedDirector().getActivity().recreate();
						_Dialog.dismiss();
						soundPlayer.startMusic();
						isClickPause = false;
					}
				});

				isClickPause = true;
				
			}
		});
	}
	
	private void IntentToBack() {
		/*Intent mainScore = new Intent(
				CCDirector.sharedDirector().getActivity(), FeiJi_Menu.class);
		CCDirector.sharedDirector().getActivity().startActivity(mainScore);*/
		CCDirector.sharedDirector().getActivity().finish();
	}
}
