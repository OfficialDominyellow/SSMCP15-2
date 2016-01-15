package com.example.hellojni;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by SECMEM-DY on 2016-01-14.
 */

public class TabGestureListener implements GestureDetector.OnGestureListener {

    private PointingStickController mPointingStickController;

    public TabGestureListener(PointingStickController mPointingStickController)
    {
        this.mPointingStickController=mPointingStickController;
    }
    @Override
    public boolean onDown(MotionEvent e) {
        Log.e("Ges","onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.e("Ges","ShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        inputEnterKey();
        Log.e("Ges","SingleTap");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    public void onLongPress(MotionEvent e)
    {
        Log.e("Ges","Long");
        mPointingStickController.setTabMode(false);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if(e1.getX()-e2.getX()<0)
            {
                //right
                Log.e("Ges", "Right");
                inputTabKey();
            }
            else if(e1.getX()-e2.getX()>0)
            {
                //left
                Log.e("Ges","Left");
                inputBackTabKey();
            }
        return true;
    }
    static {
        System.loadLibrary("hello-jni");
    }
    public native void inputTabKey();
    public native void inputEnterKey();
    public native  void inputBackTabKey();
}
