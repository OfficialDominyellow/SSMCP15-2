package com.example.hellojni;

import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by SECMEM-DY on 2016-01-14.
 */

public class TabGestureListener implements GestureDetector.OnGestureListener {

    private PointingStickController mPointingStickController;
    private static String TAG = "TabGestureListener";

    private int mPrevType;
    private int mCurrType;

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
        mPrevType = mCurrType = getTypeOfSixDividedCircle(GlobalVariable.stickHeight/2, GlobalVariable.stickWidth, (int)e.getX(), (int)e.getY());
        Log.i(TAG, "Start E : (" + e.getX() + ", " + e.getY() + "), start Type : " + mCurrType);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        inputEnterKey();
        Log.e("Ges","SingleTap");
        return false;
    }

    //시계방향 : 0, 반시계방향 : 1, 방향 없어 : -1
    private int getClockDirection(){
        if(mCurrType-mPrevType == 1 || mCurrType - mPrevType == -5){
            return 1;
        }
        else if(mCurrType - mPrevType == -1 || mCurrType - mPrevType == 5){
            return 2;
        }
        return -1;
    }

    //축 방향이 0도, 반시계방향이 각도의 양의 방향이라고한다.
    // 0~60도 : 0, 60~120도 : 1, .... 300~360도 : 5
    private int getTypeOfSixDividedCircle(int cenX, int cenY, int x, int y){
        double tan = MyMath.getTangent(cenX, cenY, x, y);
        int relaX = x-cenX;
        int relaY = y-cenY;
        Log.i(TAG, "TAN : " + tan);
        //1사분면
        if(relaX >= 0 && relaY >= 0) {
            if(0 <= tan && tan < Math.sqrt(3.0)){
                return 0;
            }
            else{
                return 1;
            }
        }
        //2사분면
        else if(relaX < 0 && relaY >= 0){
            if(tan < -Math.sqrt(3.0)){
                return 1;
            }
            else{
                return 2;
            }
        }
        //3사분면
        else if(relaX < 0 && relaY < 0){
            if(0 <= tan && tan < Math.sqrt(3.0)){
                return 3;
            }
            else{
                return 4;
            }
        }
        //4사분면
        else{
            if(tan < -Math.sqrt(3.0)){
                return 4;
            }
            else{
                return 5;
            }
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        int centerY = GlobalVariable.stickHeight/2;
        int centerX = GlobalVariable.stickWidth/2;

        Log.i(TAG, "Center X : " + centerX + " Center Y : " + centerY);
        Log.i(TAG, "e1 : (" + e1.getX() + ", " + e1.getY() + ") e2 : (" + e2.getX() + ", " + e2.getY() + ")");

        mCurrType = getTypeOfSixDividedCircle(centerX, centerY, (int)e2.getX(), (int)e2.getY());
        Log.i(TAG, "mPrevType(" + mPrevType + ") -> mCurrType(" + mCurrType + ")");

        int dir = getClockDirection();
        if(dir != -1) {
            mPrevType = mCurrType;
            if(dir == 1){
                Log.i(TAG, "ClockWise");

                inputTabKey();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                Log.i(TAG, "CounterClickWise");

                inputBackTabKey();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /*
        if(e1.getY()-e2.getY()>0)//up
        {
            Log.e("Ges", "e1");

        }
        else if(e1.getY()-e2.getY()<0)//down
        {
            Log.e("Ges", "e2");

        }
        if((e1.getX()-e2.getX()<0))//right
        {
            //right
            Log.e("Ges", "Right");
            inputTabKey();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if(e1.getX()-e2.getX()>0)//left
        {
            //left
            Log.e("Ges","Left");
            inputBackTabKey();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        */
        return true;
    }

    public void onLongPress(MotionEvent e)
    {
        Log.e("Ges","Long");
        mPointingStickController.setTabMode(false);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            /*if()//시계방향
            {

            }
            else if()//반시계방향
              */
            if(e1.getY()-e2.getY()>0)//up
            {
                Log.e("Ges", "e1");

            }
            else if(e1.getY()-e2.getY()<0)//down
            {
                Log.e("Ges", "e2");

            }
            if((e1.getX()-e2.getX()<0))//right
            {
                //right
                Log.e("Ges", "Right");
                inputTabKey();
            }
            else if(e1.getX()-e2.getX()>0)//left
            {
                //left
                Log.e("Ges","Left");
                inputBackTabKey();
            }
        return false;
    }
    static {
        System.loadLibrary("hello-jni");
    }
    public native void inputTabKey();
    public native void inputEnterKey();
    public native  void inputBackTabKey();
}
