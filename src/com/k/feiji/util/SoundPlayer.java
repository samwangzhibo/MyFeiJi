package com.k.feiji.util;

import java.util.HashMap;
import java.util.Map;

import com.k.feiji.R;
 
 
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
 
/**
 * 声音控制类
 * @author wyf
 *
 */
public class SoundPlayer {
 
    private static MediaPlayer music;
    private static SoundPool soundPool;
     
    private static boolean musicSt = true; //音乐开关
    private static boolean soundSt = true; //音效开关
    private static Context context;
     
    private static final int musicId = R.raw.game_music;
    private static Map<Integer,Integer> soundMap; //音效资源id与加载过后的音源id的映射关系表
    public static SoundPlayer instance;
    public synchronized static SoundPlayer getInstance(){
    	 if(instance == null){
    		 instance = new SoundPlayer();
    	 }
    	 return instance;
     }
    /**
     * 初始化方法
     * @param c
     */
    public static void init(Context c)
    {
        context = c;
 
        initMusic();
         
        initSound();
    }
     
    //初始化音效播放器
    private static void initSound()
    {
        soundPool = new SoundPool(10,AudioManager.STREAM_MUSIC,100);
         
        soundMap = new HashMap<Integer,Integer>();
        soundMap.put(R.raw.bullet, soundPool.load(context, R.raw.bullet, 1));
        soundMap.put(R.raw.game_over, soundPool.load(context, R.raw.game_over, 1));
        soundMap.put(R.raw.get_bomb, soundPool.load(context,R.raw.get_bomb, 1));
    }
     
    //初始化音乐播放器
    private static void initMusic()
    {
        music = MediaPlayer.create(context,musicId);
        music.setLooping(true);
    }
     
    /**
     * 播放音效
     * @param resId 音效资源id
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
     * 暂停音乐
     */
    public static void pauseMusic()
    {
        if(music.isPlaying())
            music.pause();
    }
     
    /**
     * 播放音乐
     */
    public static void startMusic()
    {
        if(musicSt)
            music.start();
    }
     
    /**
     * 切换一首音乐并播放
     */
    public static void changeAndPlayMusic()
    {
        if(music != null)
            music.release();
        initMusic();
        startMusic();
    }
     
    /**
     * 获得音乐开关状态
     * @return
     */
    public static boolean isMusicSt() {
        return musicSt;
    }
     
    /**
     * 设置音乐开关
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
     * 获得音效开关状态
     * @return
     */
    public static boolean isSoundSt() {
        return soundSt;
    }
    public void releseMusic(){
    	if(musicSt)
            music.stop();
    	music.release();
    }
 
    /**
     * 设置音效开关
     * @param soundSt
     */
    public static void setSoundSt(boolean soundSt) {
        SoundPlayer.soundSt = soundSt;
    }
     
    /**
     * 发出‘biu’的声音
     */
    public static void biu()
    {
        playSound(R.raw.bullet);
    }
}
