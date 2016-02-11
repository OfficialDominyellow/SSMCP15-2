package org.secmem.gn.ctos.samdwich.mouse;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.secmem.gn.ctos.samdwich.R;
import org.secmem.gn.ctos.samdwich.global.GlobalVariable;
import org.secmem.gn.ctos.samdwich.global.VirtualMouseDriverController;


/**
 * Created by SECMEM-DY on 2016-01-09.
 */
public class StickTouchListener implements View.OnTouchListener {
    private PointingStickController mPointingStickController;
    private WindowManager.LayoutParams mParams;
    private WindowManager.LayoutParams mParamsCenter;
    private WindowManager mWindowManager;
    private Button pointingStick;
    private TextView centerPoint;
    private ImageView hideImage;
    private Context mContext;
    private GestureDetector mDetector;
    private TabGestureListener mGestureListener;
    private CircleLayout mCircleView;

    long diffTime = 0;

    private static VirtualMouseDriverController virtualMouseDriverController;

    public StickTouchListener(PointingStickController mPointingStickController, VirtualMouseDriverController virtualMouseDriverController)
    {
        this.mPointingStickController=mPointingStickController;
        this.mParams=mPointingStickController.getmParams();
        this.mWindowManager=mPointingStickController.getmWindowManager();
        this.pointingStick=mPointingStickController.getPointingStick();
        this.mContext=mPointingStickController.getmContext();
        this.mParamsCenter=mPointingStickController.getmParamsCenter();
        this.centerPoint=mPointingStickController.getCenterPoint();
        this.hideImage=mPointingStickController.getHideImage();
        this.virtualMouseDriverController=virtualMouseDriverController;
        this.mCircleView=mPointingStickController.getmCircleView();

        mGestureListener=new TabGestureListener(mPointingStickController);
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
                diffTime =  System.currentTimeMillis();

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

                    mPointingStickController.setCurrntX(mParams.x);
                    mPointingStickController.setCurrntY(mParams.y);
                    GlobalVariable.stickWidth = mParams.width;
                    GlobalVariable.stickHeight = mParams.height;
                    mParamsCenter.x=mParams.x;
                    mParamsCenter.y=mParams.y;
                    Log.e("Service","Move x:"+mParams.x+" y:"+mParams.y);
                }

                mWindowManager.updateViewLayout(pointingStick, mParams);	//뷰 업데이트
                mWindowManager.updateViewLayout(centerPoint, mParamsCenter);	//뷰 업데이트
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
                }//롱클릭이 우선순위가 기본 클릭보다 높게 둠*/

                if(event.getToolType(0)==event.TOOL_TYPE_MOUSE) {
                        Log.e("Service", "MOUSE_ACTION_UP");
                        mWindowManager.removeViewImmediate(pointingStick);
                        Log.e("Service", "클");
                        clickLeftMouse();
                        Log.e("Service", "릭");
                        mWindowManager.addView(pointingStick,mParams);
                    break;
                }
                if(!mPointingStickController.getIsMouseMove() && !mPointingStickController.getIsMoveMode()&&!mPointingStickController.getTabMode())//mouse left click
                {
                    if(mPointingStickController.getTabMode())
                        break;//tap mode
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
                if(!mPointingStickController.getIsMoveMode()&&!mPointingStickController.isHideMode())
                {
                    virtualMouseDriverController.onPause();
                    mParams.x = mPointingStickController.getCurrntX();
                    mParams.y = mPointingStickController.getCurrntY();//상대적으로 좌표 설정 ,원위치로 변경
                    mWindowManager.updateViewLayout(pointingStick, mParams);
                    Log.e("Service", "Use Stick x:" + xdiff + " y:" + ydiff);
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
