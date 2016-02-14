package org.secmem.gn.ctos.samdwich.mouse;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.secmem.gn.ctos.samdwich.R;

/**
 * Created by SECMEM-DY on 2016-02-11.
 */
public class MotionGestureListener implements GestureDetector.OnGestureListener {
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private PointingStickController mPointingStickController;
    private CircleLayout mCircleView;
    private Button pointingStick;
    private TextView centerPoint;

    public MotionGestureListener(PointingStickController mPointingStickController)
    {
        this.mPointingStickController=mPointingStickController;
        this.mParams=mPointingStickController.getmParams();
        this.mWindowManager=mPointingStickController.getmWindowManager();
        this.mCircleView=mPointingStickController.getmCircleView();
        this.pointingStick=mPointingStickController.getPointingStick();
        this.centerPoint=mPointingStickController.getCenterPoint();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.e("Hover","mouseMove:"+mPointingStickController.getIsMoveMode()+"   tabMode:"+mPointingStickController.getTabMode()+"   LongMouse:"+mPointingStickController.getIsOptionMenu());
        if(!mPointingStickController.getIsMoveMode()
                &&!mPointingStickController.getTabMode()
                &&!mPointingStickController.getIsOptionMenu()
                &&!mPointingStickController.isHideMode())
        {
            Log.e("Hover", "scroll2");
            mPointingStickController.setIsOptionMenu(true);
            mParams.width=mParams.width*2;
            mParams.height=mParams.height*2;
            mWindowManager.removeView(pointingStick);
            mWindowManager.removeView(centerPoint);
            mWindowManager.addView(mCircleView, mParams);
            mWindowManager.updateViewLayout(mCircleView, mParams);
            return true;
        }
        else if(mPointingStickController.getTabMode())
        {
            pointingStick.setBackgroundResource(R.drawable.pointing_stick);
            mPointingStickController.setTabMode(false);//tab모드 해제
        }
        return true;
    }
}
