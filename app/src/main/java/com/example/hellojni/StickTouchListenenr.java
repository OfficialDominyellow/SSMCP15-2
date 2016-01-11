package com.example.hellojni;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListPopupWindow;

/**
 * Created by SECMEM-DY on 2016-01-09.
 */
public class StickTouchListenenr implements View.OnTouchListener {
    private PointingStickController mPointingStickController;
    private WindowManager.LayoutParams mParams;
    private ListPopupWindow mList;
    private WindowManager mWindowManager;
    private Button pointingStick;
    private Context mContext;

    private static VirtualMouseDriverController virtualMouseDriverController=PointingStickService.virtualMouseDriverController;

    public StickTouchListenenr(PointingStickController mPointingStickController,WindowManager.LayoutParams mParams,ListPopupWindow mList,WindowManager mWindowManager, Button pointingStick,
                               Context mContext)
    {
        this.mPointingStickController=mPointingStickController;
        this.mParams=mParams;
        this.mList=mList;
        this.mWindowManager=mWindowManager;
        this.pointingStick=pointingStick;
        this.mContext=mContext;
    }

    public boolean onTouch(View v, MotionEvent event) {
        int xdiff=0;
        int ydiff=0;
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:				//사용자 터치 다운이면
                mPointingStickController.setSTART_X(event.getRawX());//터치 시작 점
                mPointingStickController.setSTART_Y(event.getRawY());//터치 시작 점

                mPointingStickController.setPREV_X(mParams.x);//뷰의 시작 점
                mPointingStickController.setPREV_Y(mParams.y);//뷰의 시작

                mPointingStickController.setIsMouseMove(false);
                mList.dismiss();//리스트 숨김
                Log.e("Service", "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                final int MAXdp = 63;

                xdiff = (int)(event.getRawX() - mPointingStickController.getSTART_X());	//이동한 거리
                ydiff = (int)(event.getRawY() - mPointingStickController.getSTART_Y());	//이동한 거리

                double distance = Math.sqrt(xdiff*xdiff+ydiff*ydiff);
                float dpDistance = convertPixelsToDp((float)distance,mContext.getApplicationContext());
                if (dpDistance>MAXdp && !mPointingStickController.getIsMoveMode()) {
                    xdiff=(int)(xdiff/dpDistance*MAXdp);
                    ydiff=(int)(ydiff/dpDistance*MAXdp);
                }

                //터치해서 이동한 만큼 이동 시킨다
                mParams.x = mPointingStickController.getPREV_X() + xdiff;
                mParams.y = mPointingStickController.getPREV_Y() + ydiff;

                if(mPointingStickController.getIsMoveMode())//Move mode일때
                {
                    mPointingStickController.setPxWidth(mParams.x);
                    mPointingStickController.setPxHeight(mParams.y);
                }
                mWindowManager.updateViewLayout(pointingStick, mParams);	//뷰 업데이트
                mPointingStickController.setIsMouseMove(true);
                Log.e("Service","ACTION_MOVE");
                virtualMouseDriverController.setDifference(xdiff,ydiff);
                if(virtualMouseDriverController.getmPause() &&!mPointingStickController.getIsMoveMode()) {
                    virtualMouseDriverController.onResume();
                } else {

                }
                break;
                /* reset position */
            case MotionEvent.ACTION_UP:
                if(mPointingStickController.getIsLongMouseClick())
                {
                    //isLongMouseClick=false;
                    mPointingStickController.setIsLongMouseClick(false);
                }//롱클릭이 우선순위가 기본 클릭보다 높게 둠
                else if(!mPointingStickController.getIsMouseMove() && !mPointingStickController.getIsMoveMode())
                {
                    clickLeftMouse();//bug있음
                    Log.e("Service", "left mouse clicked");
                }
                mPointingStickController.setIsMouseMove(true);
                Log.e("Service", "ACTION_UP");
                if(!mPointingStickController.getIsMoveMode()) {
                    virtualMouseDriverController.onPause();
                    mParams.x = mPointingStickController.getPxWidth();
                    mParams.y = mPointingStickController.getPxHeight();//상대적으로 좌표 설정 ,원위치로 변경
                    mWindowManager.updateViewLayout(pointingStick, mParams);
                }
                break;
        }
        return false;
    }
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    static {
        System.loadLibrary("hello-jni");
    }
    public native void clickLeftMouse();
}
