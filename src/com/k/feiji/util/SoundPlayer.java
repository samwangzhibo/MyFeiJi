package com.k.feiji.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.k.feiji.R;

import java.util.HashMap;
import java.util.Map;
 
/**
 * 声音类
 * @author wyf
 *
 */
public class SoundPlayer {
 
    private static MediaPlayer music;
    private static SoundPool soundPool;
     
    private static boolean musicSt = true; //音乐开关
    private static boolean soundSt = true; //音效开关
    private static Context context;
     
    public static final int musicId = R.raw.game_music;
    public static final int musicHDL = R.raw.background;
    public static final String[] MUSICS = new String[] { "传统音", "魂斗罗音"};

    private static Map<Integer,Integer> soundMap; //音乐池
    public static SoundPlayer instance;
    private static boolean isInit = false;
    public synchronized static SoundPlayer getInstance(){
    	 if(instance == null){
    		 instance = new SoundPlayer();
    	 }
    	 return instance;
     }

    public boolean isInit(){
        return isInit;
    }

    public static void init(Context c)
    {
        isInit = true;

        context = c;
 
        initMusic(musicId);
         
        initSound();
    }
     
    //初始化sound
    private static void initSound()
    {
        soundPool = new SoundPool(10,AudioManager.STREAM_MUSIC,100);
         
        soundMap = new HashMap<Integer,Integer>();
        soundMap.put(R.raw.bullet, soundPool.load(context, R.raw.bullet, 1));
        soundMap.put(R.raw.game_over, soundPool.load(context, R.raw.game_over, 1));
        soundMap.put(R.raw.get_bomb, soundPool.load(context, R.raw.get_bomb, 1));
        soundMap.put(R.raw.get_double_bullet, soundPool.load(context, R.raw.get_double_bullet, 1));
        soundMap.put(R.raw.big_spaceship_flying, soundPool.load(context, R.raw.big_spaceship_flying, 1));
    }
     
    //初始化音乐
    private static void initMusic(int sourseId)
    {
        music = MediaPlayer.create(context,sourseId);
        music.setLooping(true);
    }
     
    /**
     *  播放sound
     * @param resId 资源ID
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
        try{
        if(music != null && music.isPlaying() && musicSt)
            music.pause();
        }catch (Exception e){
        e.printStackTrace();
    }
    }
     

    public static void startMusic()
    {
        try {
        if(musicSt && music != null)
            music.start();
         }catch (Exception e){
    }

    }

    /**
     * 改变音乐
     */
    public static void changeAndPlayMusic(int sourseId)
    {
        if(music != null)
            music.release();
        initMusic(sourseId);
        startMusic();
    }
     
    /**
     * 是否播放背景音
     * @return
     */
    public static boolean isMusicSt() {
        return musicSt;
    }
     
    /**
     * 设置背景音开关
     * @param musicSt
     */
    public static void setMusicSt(boolean musicSt) {
        SoundPlayer.musicSt = musicSt;
        if(musicSt)
            music.start();
        else
            music.stop();
    }
 

    public static boolean isSoundSt() {
        return soundSt;
    }

    public void releseMusic(){
    	if(musicSt && music != null)
            music.stop();
    	music.release();
    }
 
    /**
     * 设置sound开关
     * @param soundSt
     */
    public static void setSoundSt(boolean soundSt) {
        SoundPlayer.soundSt = soundSt;
    }
     
    /**
     * biu一声
     */
    public static void biu()
    {
        playSound(R.raw.bullet);
    }
}
