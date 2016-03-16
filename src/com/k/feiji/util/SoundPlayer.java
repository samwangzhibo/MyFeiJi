package com.k.feiji.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import com.k.feiji.R;

import java.util.HashMap;
import java.util.Map;
 
/**
 * ����������
 * @author wyf
 *
 */
public class SoundPlayer {
 
    private static MediaPlayer music;
    private static SoundPool soundPool;
     
    private static boolean musicSt = true; //���ֿ���
    private static boolean soundSt = true; //��Ч����
    private static Context context;
     
    private static final int musicId = R.raw.game_music;
    private static Map<Integer,Integer> soundMap; //��Ч��Դid����ع�����Դid��ӳ���ϵ��
    public static SoundPlayer instance;
    public synchronized static SoundPlayer getInstance(){
    	 if(instance == null){
    		 instance = new SoundPlayer();
    	 }
    	 return instance;
     }
    /**
     * ��ʼ������
     * @param c
     */
    public static void init(Context c)
    {
        context = c;
 
        initMusic();
         
        initSound();
    }
     
    //��ʼ����Ч������
    private static void initSound()
    {
        soundPool = new SoundPool(10,AudioManager.STREAM_MUSIC,100);
         
        soundMap = new HashMap<Integer,Integer>();
        soundMap.put(R.raw.bullet, soundPool.load(context, R.raw.bullet, 1));
        soundMap.put(R.raw.game_over, soundPool.load(context, R.raw.game_over, 1));
        soundMap.put(R.raw.get_bomb, soundPool.load(context, R.raw.get_bomb, 1));
    }
     
    //��ʼ�����ֲ�����
    private static void initMusic()
    {
        music = MediaPlayer.create(context,musicId);
        music.setLooping(true);
    }
     
    /**
     * ������Ч
     * @param resId ��Ч��Դid
     */
    public static void playSound(int resId)
    {
        if(soundSt == false)
            return;
         
        Integer soundId = soundMap.get(resId);
        if(soundId != null)
            soundPool.play(soundId, 1, 1, 1, 0, 1);
    }
 
    /**
     * ��ͣ����
     */
    public static void pauseMusic()
    {   try{
        if(music != null && music.isPlaying())
            music.pause();
        }catch (Exception e){
        e.printStackTrace();
    }
    }
     

    public static void startMusic()
    {   try {
        if(musicSt && music != null)
            music.start();
         }catch (Exception e){
        Log.e("SoundPlay : 100 ---- ",e.toString());
    }

    }
     

    public static void changeAndPlayMusic()
    {
        if(music != null)
            music.release();
        initMusic();
        startMusic();
    }
     
    /**
     * ������ֿ���״̬
     * @return
     */
    public static boolean isMusicSt() {
        return musicSt;
    }
     
    /**
     * �������ֿ���
     * @param musicSt
     */
    public static void setMusicSt(boolean musicSt) {
        SoundPlayer.musicSt = musicSt;
        if(musicSt)
            music.start();
        else
            music.stop();
    }
 
    /**
     * �����Ч����״̬
     * @return
     */
    public static boolean isSoundSt() {
        return soundSt;
    }
    public void releseMusic(){
    	if(musicSt && music != null)
            music.stop();
    	music.release();
    }
 
    /**
     * ������Ч����
     * @param soundSt
     */
    public static void setSoundSt(boolean soundSt) {
        SoundPlayer.soundSt = soundSt;
    }
     
    /**
     * ������biu��������
     */
    public static void biu()
    {
        playSound(R.raw.bullet);
    }
}
