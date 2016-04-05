package com.k.feiji.util;

/**
 * Created by wzb on 2016/4/3.
 */
import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGSize;

import java.util.jar.Attributes;

public class MyCCGLSurfaceView extends GLSurfaceView {
    private static final int VIEWID = 4661;
    private CCTouchDispatcher mDispatcher;
    public CGSize frame;

    public MyCCGLSurfaceView(Context context) {
        super(context);
        CCDirector.theApp = (Activity)context;
        this.mDispatcher = CCTouchDispatcher.sharedDispatcher();
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setId(4661);
    }
    public MyCCGLSurfaceView(Context context,AttributeSet attrs) {
        super(context, attrs);
        CCDirector.theApp = (Activity)context;
        this.mDispatcher = CCTouchDispatcher.sharedDispatcher();
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setId(4661);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("wzb", "surfaceCreated");
        GLES20.glClearColor(0f, 0f, 0f, 0f);//alphaΪ0
        super.surfaceCreated(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e("wzb", "surfaceDestroyed");
        super.surfaceDestroyed(holder);
    }

    @Override
    protected void onAttachedToWindow() {
        Log.e("wzb", "onAttachedToWindow");
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.e("wzb", "onDetachedFromWindow");
        super.onDetachedFromWindow();
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.frame = CGSize.make((float)(right - left), (float)(bottom - top));
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.mDispatcher.queueMotionEvent(event);
        synchronized(CCDirector.sharedDirector()) {
            try {
                CCDirector.sharedDirector().wait(20L);
            } catch (InterruptedException var4) {
                ;
            }
            return true;
        }
    }
}