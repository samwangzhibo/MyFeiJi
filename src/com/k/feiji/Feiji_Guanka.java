package com.k.feiji;

import android.graphics.SweepGradient;

public class Feiji_Guanka {

	private static  Feiji_Guanka instanceFeiji_Guanka;
	
	private int mLevels = 1;
	private int mTargerScore = 40000;
	
	private Feiji_Guanka(){
		
	}
	
	public static Feiji_Guanka getInstance(){
		synchronized (Feiji_Guanka.class) {
			if (instanceFeiji_Guanka == null) {
				instanceFeiji_Guanka = new Feiji_Guanka();
			}
		}
		return instanceFeiji_Guanka;
	}
	
	protected int getLevel(int score){
		return score/40000+1;
	}
	protected int getTargetScore(int score){
		return (score/40000+1)*40000;
	}
}
