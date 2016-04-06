package com.k.feiji.myccanim;

import org.cocos2d.actions.interval.CCBezierTo;
import org.cocos2d.types.CCBezierConfig;
import org.cocos2d.types.CGPoint;

/**
 * Created by zybang on 2016/4/6.
 * 自定义Bezier曲线的动画，支持旋转
 */
public class MyCCBezierTo extends CCBezierTo{
    private boolean isRotate = true;
    protected MyCCBezierTo(float t, CCBezierConfig c, boolean isRotate) {
        super(t, c);
        this.isRotate = isRotate;
    }
    public static CCBezierTo action(float t, CCBezierConfig c, boolean isRotate) {
        return new MyCCBezierTo(t, c, isRotate);
    }
    public static CCBezierTo action(float t, CCBezierConfig c) {
        return new MyCCBezierTo(t, c, true);
    }
    public void setIsRotate(boolean isRotate){
        this.isRotate = isRotate;
    }
    @Override
    public void update(float t) {
        if (isRotate){
            CGPoint oldpos = this.target.getPosition();
            super.update(t);
            CGPoint newpos = this.target.getPosition();
            float angle = (float) (Math.atan2(-newpos.y + oldpos.y, newpos.x - oldpos.x) * 180  / Math.PI - 90);
            this.target.setRotation(angle);
        }else {
            super.update(t);
        }

    }


}
