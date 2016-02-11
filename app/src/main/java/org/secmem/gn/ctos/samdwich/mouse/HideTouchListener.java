package org.secmem.gn.ctos.samdwich.mouse;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.secmem.gn.ctos.samdwich.global.GlobalVariable;

/**
 * Created by SECMEM-DY on 2016-02-01.
 */
public class HideTouchListener implements View.OnTouchListener {
    private PointingStickController mPointingStickController;private WindowManager.LayoutParams mParams;
    private WindowManager.LayoutParams mParamsCenter;
    private WindowManager mWindowManager;
    private Button pointingStick;
    private TextView centerPoint;
    private ImageView hideImage;
    private Context mContext;
    public HideTouchListener(PointingStickController mPointingStickController)
    {
        this.mPointingStickController=mPointingStickController;
        this.mParams=mPointingStickController.getmParams();
        this.mWindowManager=mPointingStickController.getmWindowManager();
        this.pointingStick=mPointingStickController.getPointingStick();
        this.mContext=mPointingStickController.getmContext();
        this.mParamsCenter=mPointingStickController.getmParamsCenter();
        this.centerPoint=mPointingStickController.getCenterPoint();
        this.hideImage=mPointingStickController.getHideImage();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mWindowManager.removeViewImmediate(hideImage);
        mWindowManager.addView(centerPoint, mParamsCenter);
        mWindowManager.addView(pointingStick, mParams);
        mParams.width= GlobalVariable.stickWidth;
        mParams.height=GlobalVariable.stickHeight;
        mParams.x = mPointingStickController.getCurrntX();
        mParams.y = mPointingStickController.getCurrntY();//상대적으로 좌표 설정 ,원위치로 변경
        mWindowManager.updateViewLayout(pointingStick, mParams);
        mWindowManager.updateViewLayout(centerPoint, mParamsCenter);
        mPointingStickController.setHideMode(false);
        mPointingStickController.setIsOptionMenu(false);
        return true;
    }
}
