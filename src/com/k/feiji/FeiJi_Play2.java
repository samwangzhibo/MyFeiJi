package com.k.feiji;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.R.bool;
import android.content.Context;
import android.content.SharedPreferences;

public class FeiJi_Play2 extends CCColorLayer{
	private List<FeiJi_Sprite> _BigFoes = new CopyOnWriteArrayList<FeiJi_Sprite>();
	private List<FeiJi_Sprite> _AllFoes = new CopyOnWriteArrayList<FeiJi_Sprite>();
	private SharedPreferences _Share;
	private CGSize _WinSize;
	private String _FeiJi_Back_Path="images/feiji_background.png";
	private String _Pause_Path="images/pause.png";
	private CCSprite _FeiJi_Pause;
	private CCSprite _FeiJi_Play;
	private CCSprite _Red_Bomb;
	private CCLabel _ScoreLabel;
	private CCLabel _Red_Bomb_Num;
	private String _Font_Path = "Cookies.ttf";
	private int _Get_Score = 0;//��õķ���
	private String _Play_Path = "images/play.png";
	private String _Red_Bomb_Path = "images/red_bomb.png";
	private String _BigFoe_Path = "images/bigfoe.png";//��ŵл�
	private String _MiddleFoe_Path = "images/middlefoe.png";//���ͷɻ�
	private String _SmallFoe_Path = "images/smallfoe.png";//С�ͷɻ�
	private int Red_Bomb_Num = 0;//��ը��������
	private int _Big_Life = 16,Middle_Life = 8,Small_Life = 2;//�л�����
	private long Blue_Shot_Last_Time = 0;//���ӵ�����ʱ��
	private int FoeDown_Time = 8;//�л������ٶ�
	private boolean Blue_Shot_Change = false;//�Ƿ����ӵ�
	private String _Blue_Shot = "images/blue_shot.png";
	protected FeiJi_Play2(ccColor4B color) {
		// TODO Auto-generated constructor stub
		super(color);
		Init();
	}

	private void Init() {
		// TODO Auto-generated method stub
		_Share = CCDirector.sharedDirector().getActivity()
				.getSharedPreferences("Share", Context.MODE_PRIVATE);
		//��ȡ��Ļ��С
		_WinSize = CCDirector.sharedDirector().displaySize();
		//�����Ƿ��ܵ��
		setIsTouchEnabled(true);
		CCSprite _FeiJi_Back = CCSprite.sprite(_FeiJi_Back_Path);
		//��������Ļ��С�Ŵ�
		_FeiJi_Back.setScaleX(_WinSize.width
				/_FeiJi_Back.getTexture().getWidth());
		_FeiJi_Back.setScaleY(_WinSize.height
				/_FeiJi_Back.getTexture().getHeight());
		//���������ĵ�������Ļ����
		_FeiJi_Back.setPosition(CGPoint.make(_WinSize.getWidth()/2,
				_WinSize.getHeight()/2));
		addChild(_FeiJi_Back);//���ɻ��������������
		
		//�����ͣ
		_FeiJi_Pause = CCSprite.sprite(_Pause_Path);
		_FeiJi_Pause.setPosition(CGPoint.make(_FeiJi_Pause.getContentSize().width/2+1, 
				_FeiJi_Pause.getContentSize().height/2+1));
		addChild(_FeiJi_Pause);
		
		AddScore();
		
		this.schedule("GameFoes", 0.5f);//��ӵл�
		this.schedule("GameShot", 0.2f);
		GamePlay();
		this.schedule("AddPlay",0.2f);
		this.schedule("Detection",0.2f);
		this.schedule("AddRedBlueDown",2.0f);
		this.schedule("AddBigFoe",0.2f);
		
		AddRedBomb();
	}
	public void GameFoes(Float f){
		AddFoes();
	}
	private void GameShot(float t){
		AddShot();
	}
	/**
	 * ֹͣ�����ķ��� 
	 */
	private void StopSchedule(){
		this.unschedule("GameFoes");
		this.unschedule("GameShot");
		this.unschedule("AddPlay");
		this.unschedule("Detection");
		this.unschedule("AddRedBlueDown");
	}
	/**
	 * ��ӷɻ��ӵ�
	 */
	private void AddShot(){
		CCSprite _FeiJi_Shot = null;
		
		if(Blue_Shot_Change){
			_FeiJi_Shot = CCSprite.sprite(_Blue_Shot);
			_FeiJi_Shot.setTag(1);
			if(System.currentTimeMillis() - Blue_Shot_Last_Time > 10000){
				Blue_Shot_Change = false;
			}
		}
	}
	/**
	 * ��ӵл�
	 */
	private void AddFoes(){
		Random random = new Random();
		int randomValue = random.nextInt(30);
		FeiJi_Sprite _FeiJi_Foe = new FeiJi_Sprite();
		_FeiJi_Foe.setClicked_Or(false);
		if(randomValue == 0&& _BigFoes.size() <1){
			_FeiJi_Foe.setLift(_Big_Life);
			_FeiJi_Foe.setMax_Life(_Big_Life);
			_FeiJi_Foe.setCCSprite(_BigFoe_Path);
			FoeDown(_FeiJi_Foe,0);
		}else if(randomValue == 1||randomValue == 2){
			_FeiJi_Foe.setLift(Middle_Life);
			_FeiJi_Foe.setMax_Life(Middle_Life);
			_FeiJi_Foe.setCCSprite(_MiddleFoe_Path);
			FoeDown(_FeiJi_Foe, 1);
		}else{
			_FeiJi_Foe.setLift(Small_Life);
			_FeiJi_Foe.setMax_Life(Small_Life);
			_FeiJi_Foe.setCCSprite(_SmallFoe_Path);
			FoeDown(_FeiJi_Foe, 2);
		}
		_AllFoes.add(_FeiJi_Foe);
	}
	/**
	 * ��ӷɻ�����
	 * @param _FeiJi_Foe2
	 */
	public void FoeDown(FeiJi_Sprite _FeiJi_Foe2,int i){
		Random random = new Random();
		int minX = (int) (_FeiJi_Foe2.getCCSprite().getContentSize()
				.width/2.0f);
		int maxX = (int) (_FeiJi_Foe2.getCCSprite().getContentSize()
				.height/2.0f);
		int rangeX = maxX - minX;
		int actualX = random.nextInt(rangeX) + minX;
		int minDuration = FoeDown_Time-4;
		int maxDuration = FoeDown_Time;
		if(_Get_Score > 1000000){
			//�ı����ʱ��
			maxDuration = maxDuration - 2;
			minDuration = minDuration -5;
		}
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = random.nextInt(rangeDuration) + minDuration;
		if(actualDuration < 0 )
			actualDuration = random.nextInt(2)+2;
		//���÷ɻ�����
		_FeiJi_Foe2.setInitX(actualX);
		_FeiJi_Foe2.setInitDuration(actualDuration);
		_FeiJi_Foe2.setInitY(_FeiJi_Foe2.getCCSprite().getContentSize()
				.height/2.0f + _WinSize.height);
		
		_FeiJi_Foe2.getCCSprite().setPosition(actualX,_FeiJi_Foe2.
				getCCSprite().getContentSize().height/2.0f + _WinSize.height);
		addChild(_FeiJi_Foe2.getCCSprite());
		
		//���÷ɻ�tag
		if(i == 0){
			_FeiJi_Foe2.getCCSprite().setTag(0);
			_BigFoes.add(_FeiJi_Foe2);
		}
		else{
			if(i == 1){
			_FeiJi_Foe2.getCCSprite().setTag(1);	
			}else{
			_FeiJi_Foe2.getCCSprite().setTag(i);
			}
			_BigFoes.add(_FeiJi_Foe2);
		}
		
		CCFiniteTimeAction fs_timeAction = CCMoveTo.action(actualDuration
				, CGPoint.ccp(actualX, -(_FeiJi_Foe2.getCCSprite().
						getContentSize().height/2)));//ʱ�����ƶ�
		CCCallFuncN fs_Over = null;
		if(i == 0)
			fs_Over = CCCallFuncN.action(this,"BigFoe_Over");
		else
			fs_Over = CCCallFuncN.action(this,"Foe_Over");
		CCSequence fs_actions = CCSequence.actions(fs_timeAction,fs_Over);
		_FeiJi_Foe2.getCCSprite().runAction(fs_actions);
	}
	/**
	 * ��ӷ���
	 */
	private void AddScore(){
		if(_ScoreLabel != null)
		_ScoreLabel.removeSelf();
		
		_ScoreLabel = CCLabel.makeLabel("SCORE:"+_Get_Score, _Font_Path, 30);
		_ScoreLabel.setColor(ccColor3B.ccWHITE);
		_ScoreLabel.setString("SCORE:" + _Get_Score);
		_ScoreLabel.setPosition(CGPoint.make(_FeiJi_Pause.getContentSize()
				.getWidth() + 5, _FeiJi_Pause.getContentSize().getHeight() +5));
		addChild(_ScoreLabel);	
	}
	private void GamePlay(){
		AddPlay();
	}
	/**
	 * �����ҿ��Ʒɻ�
	 */
	private void AddPlay(){
		_FeiJi_Play = CCSprite.sprite(_Play_Path);
		_FeiJi_Play.setPosition(_WinSize.width/2,
				_FeiJi_Play.getContentSize().getHeight()/2);
		addChild(_FeiJi_Play);
	}
	/**
	 * ��Ӻ�ɫը��
	 */
	private void AddRedBomb(){
		if(_Red_Bomb_Num != null)
			_Red_Bomb_Num.removeSelf();
		if(_Red_Bomb != null)
			_Red_Bomb.removeSelf();
		if(Red_Bomb_Num > 0){
			_Red_Bomb = CCSprite.sprite(_Red_Bomb_Path);
			_Red_Bomb.setPosition(CGPoint.make(_Red_Bomb.getContentSize()
					.width/2+1, _Red_Bomb.getContentSize().height));
			addChild(_Red_Bomb);
			
			//�½�һ��Label
			_Red_Bomb_Num = CCLabel.makeLabel("x"+Red_Bomb_Num,_Font_Path,
					30);
			_Red_Bomb_Num.setColor(ccColor3B.ccBLACK);
			_Red_Bomb_Num.setPosition(CGPoint.ccp(_Red_Bomb.getContentSize()
					.width+5+_Red_Bomb_Num.getContentSize().width/2, _Red_Bomb
					.getContentSize().height/2));
			addChild(_Red_Bomb_Num);//���������������
		}
	}
	
}
