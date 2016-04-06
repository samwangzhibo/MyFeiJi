package com.k.feiji.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.k.feiji.R;

import java.util.List;

/**
 * Created by zybang on 2016/4/6.
 */
public class bgDialog extends Dialog{
    private View contentView;
    private CallBack callBack;
    /**
     * 使用contentView新建一个Dialog，这个dialog是可通过点击外部区域cancel的，而且显示在底部
     */
    public static bgDialog createDialogWithContentView(Context context) {
        bgDialog dialog = new bgDialog(context, R.style.Dialog_Style);
        final View view = View.inflate(context, R.layout.select_bg_dialog, null);

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.MyDialogAnimation);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(wlp);
        return dialog;
    }

    public static class DemoCollectionPagerAdapter extends PagerAdapter {
        List<ImageView> imageViews;
        public DemoCollectionPagerAdapter(List<ImageView> imageViews){
            this.imageViews = imageViews;
        }
        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.e("wzb","destroyItem");
            container.removeView(imageViews.get(position));
        }

        @Override
        public float getPageWidth(int position) {
            return 0.8f;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            return imageViews.get(position);
        }
    }
    private bgDialog(Context context, int theme) {
        super(context, theme);
    }

    public View getContentView() {
        return contentView;
    }

    @Override
    public void show() {
       /* contentView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                contentView.getViewTreeObserver().removeOnPreDrawListener(this);

                TranslateAnimation translateAnimation =new TranslateAnimation(0,0,-1,0);
                translateAnimation.setDuration(2000);
                contentView.setAnimation(translateAnimation);
                translateAnimation.start();
                return false;
            }
        });*/
        super.show();
    }

    @Override
    public void dismiss() {
       /* try {
            TranslateAnimation translateAnimation =new TranslateAnimation(0,1,0,0);
            translateAnimation.setDuration(2000);
            contentView.setAnimation(translateAnimation);
            translateAnimation.start();
            bgDialog.super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        bgDialog.super.dismiss();
        if (callBack != null) callBack.callback();
    }

    public void dismissImmediately() {
        super.dismiss();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        this.contentView = view;
    }
    public interface CallBack{
         void callback();
    }
    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }
}
