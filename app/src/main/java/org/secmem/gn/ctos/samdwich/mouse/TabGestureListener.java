package org.secmem.gn.ctos.samdwich.mouse;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.widget.Button;

import org.secmem.gn.ctos.samdwich.R;
import org.secmem.gn.ctos.samdwich.global.GlobalVariable;
import org.secmem.gn.ctos.samdwich.global.MyMath;


/**
 * Created by SECMEM-DY on 2016-01-14.
 */

public class TabGestureListener implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {

    private Animation anim=null;
    private PointingStickController mPointingStickController;
    private Button pointingStick;
    private static String TAG = "TabGestureListener";

    private int mPrevType;
    private int mCurrType;

    public TabGestureListener(PointingStickController mPointingStickController,Button pointingStick)
    {
        this.mPointingStickController=mPointingStickController;
        this.pointingStick=pointingStick;
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
            if (dir == 1) {
                Log.i(TAG, "ClockWise");
                pointingStick.setBackgroundResource(R.drawable.tab_right);

                inputTabKey();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                Log.i(TAG, "CounterClickWise");
                pointingStick.setBackgroundResource(R.drawable.tab_left);

                inputBackTabKey();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public void onLongPress(MotionEvent e)
    {
        Log.e("Ges","onLongPress");
        pointingStick.setBackgroundResource(R.drawable.pointing_stick);
        mPointingStickController.setTabMode(false);//tab모드 해제
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.e("Ges","onDoubleTap");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    static {
        System.loadLibrary("samdwich_jni");
    }
    public native void inputTabKey();
    public native void inputEnterKey();
    public native  void inputBackTabKey();

}
