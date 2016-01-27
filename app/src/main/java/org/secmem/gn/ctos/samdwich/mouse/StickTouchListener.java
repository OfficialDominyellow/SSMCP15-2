package org.secmem.gn.ctos.samdwich.mouse;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import org.secmem.gn.ctos.samdwich.R;
import org.secmem.gn.ctos.samdwich.global.GlobalVariable;
import org.secmem.gn.ctos.samdwich.global.VirtualMouseDriverController;


/**
 * Created by SECMEM-DY on 2016-01-09.
 */
public class StickTouchListener implements View.OnTouchListener {
    private PointingStickController mPointingStickController;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private Button pointingStick;
    private Context mContext;
    private GestureDetector mDetector;
    private TabGestureListener mGestureListener;
    Object mouseLock;

    private static VirtualMouseDriverController virtualMouseDriverController;

    public StickTouchListener(PointingStickController mPointingStickController, WindowManager.LayoutParams mParams, WindowManager mWindowManager, Button pointingStick,
                              Context mContext, VirtualMouseDriverController virtualMouseDriverController)
    {
        this.mPointingStickController=mPointingStickController;
        this.mParams=mParams;
        this.mWindowManager=mWindowManager;
        this.pointingStick=pointingStick;
        this.mContext=mContext;
        this.virtualMouseDriverController=virtualMouseDriverController;
        mGestureListener=new TabGestureListener(mPointingStickController,pointingStick);
        mDetector=new GestureDetector(mContext,mGestureListener);
    }
    public boolean onTouch(View v, MotionEvent event) {
        int xdiff=0;
        int ydiff=0;
        if(mPointingStickController.getTabMode()) {
            Log.e("Service", "tabMode");//bug있음
            return mDetector.onTouchEvent(event);//tab 모드
        }
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:				//사용자 터치 다운이면
                if(event.getToolType(0)==event.TOOL_TYPE_MOUSE) {
                    Log.e("Service", "MOUSE_ACTION_DOWN");
                    break;
                }
                mPointingStickController.setSTART_X(event.getRawX());//터치 시작 점
                mPointingStickController.setSTART_Y(event.getRawY());//터치 시작 점
                mPointingStickController.setPREV_X(mParams.x);//뷰의 시작 점
                mPointingStickController.setPREV_Y(mParams.y);//뷰의 시작

                mPointingStickController.setIsMouseMove(false);
                Log.e("Service", "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                final int MAXdp = 63;
                xdiff = (int)(event.getRawX() - mPointingStickController.getSTART_X());	//이동한 거리
                ydiff = (int)(event.getRawY() - mPointingStickController.getSTART_Y());	//이동한 거리

                //Log.e("Service", "originX: "+originX+"  originY:"+originY);

                double distance = Math.sqrt(xdiff*xdiff+ydiff*ydiff);
                float dpDistance = GlobalVariable.convertPixelsToDp((float) distance, mContext.getApplicationContext());
                if (dpDistance>MAXdp && !mPointingStickController.getIsMoveMode()) {
                    xdiff=(int)(xdiff/dpDistance*MAXdp);
                    ydiff=(int)(ydiff/dpDistance*MAXdp);
                }//포인팅 스틱의 범위를 벗어나지 않기 위한 xdiff,ydiff 설정 스틱모드일 때


                //터치해서 이동한 만큼 이동 시킨다
                mParams.x = mPointingStickController.getPREV_X() + xdiff;
                mParams.y = mPointingStickController.getPREV_Y() + ydiff;

                if(mPointingStickController.getIsMoveMode())//Move mode일때
                {
                    if(mParams.x<GlobalVariable.displayMaxLeft)
                        mParams.x=GlobalVariable.displayMaxLeft;
                    else if(mParams.x>GlobalVariable.displayMaxRight)
                        mParams.x=GlobalVariable.displayMaxRight;
                    if(mParams.y<GlobalVariable.displayMaxTop)
                        mParams.y=GlobalVariable.displayMaxTop;
                    else if(mParams.y>GlobalVariable.displayMaxBottom)
                        mParams.y=GlobalVariable.displayMaxBottom;

                    mPointingStickController.setPxWidth(mParams.x);
                    mPointingStickController.setPxHeight(mParams.y);
                    GlobalVariable.stickWidth = mParams.width;
                    GlobalVariable.stickHeight = mParams.height;
                }

                mWindowManager.updateViewLayout(pointingStick, mParams);	//뷰 업데이트
                mPointingStickController.setIsMouseMove(true);//원테이크
                Log.e("Service","ACTION_MOVE");
                virtualMouseDriverController.setDifference(xdiff,ydiff);
                if(virtualMouseDriverController.getmPause() &&!mPointingStickController.getIsMoveMode() &&!mPointingStickController.getTabMode())
                {
                    virtualMouseDriverController.onResume();
                }
                break;
                /* reset position */
            case MotionEvent.ACTION_UP:
                /*
                if(mPointingStickController.getIsLongMouseClick())
                {
                    mPointingStickController.setIsLongMouseClick(false);
                }//롱클릭이 우선순위가 기본 클릭보다 높게 둠


                else*/
                if(event.getToolType(0)==event.TOOL_TYPE_MOUSE) {
                    synchronized (mouseLock) {
                        Log.e("Service", "MOUSE_ACTION_UP");

                        /*
                        mTmpWidth = mParams.width;
                        mTmpHeight = mParams.height;
                        mParams.width = 1;
                        mParams.height = 1;
                        mWindowManager.updateViewLayout(pointingStick, mParams);
                        */
                        mWindowManager.removeViewImmediate(pointingStick);
                        for (int i = 0; i < 1; i++) {
                            Log.e("Service", "없어져라얍!");
                        }

                        Log.e("Service", "클");
                        clickLeftMouse();
                        Log.e("Service", "릭");
                        for (int i = 0; i < 1; i++) {
                            Log.e("Service", "살아나라얍!");
                        }


                        mWindowManager.addView(pointingStick,mParams);

                    }
                    break;
                }

                if(!mPointingStickController.getIsMouseMove() && !mPointingStickController.getIsMoveMode()&&!mPointingStickController.getTabMode())//mouse left click

                {
                    if(mPointingStickController.getTabMode())
                    {
                        break;
                    }//tap mode
                    Log.e("Service", "LeftMouse");
                    clickLeftMouse();
                }
                else if( mPointingStickController.getIsMoveMode())
                {
                    pointingStick.setBackgroundResource(R.drawable.pointing_stick);//Move mode에서 원상태로 복귀

                    mPointingStickController.setMoveMode(false);
                }//Move one 1take

                mPointingStickController.setIsMouseMove(true);
                Log.e("Service", "ACTION_UP");
                if(!mPointingStickController.getIsMoveMode())
                {
                    virtualMouseDriverController.onPause();
                    mParams.x = mPointingStickController.getPxWidth();
                    mParams.y = mPointingStickController.getPxHeight();//상대적으로 좌표 설정 ,원위치로 변경
                    mWindowManager.updateViewLayout(pointingStick, mParams);
                }
                break;
        }
        return false;
    }

    static {
        System.loadLibrary("samdwich_jni");
    }
    public native void clickLeftMouse();
}
