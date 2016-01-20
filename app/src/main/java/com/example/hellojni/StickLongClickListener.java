package com.example.hellojni;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListPopupWindow;

/**
 * Created by SECMEM-DY on 2016-01-09.
 */
public class StickLongClickListener implements View.OnLongClickListener
{
    private WindowManager.LayoutParams mParams; //Layout params객체, 뷰의 위치 크기 지정
    private WindowManager mWindowManager;
    private PointingStickController mPointingStickController;
    private CircleLayout circleView;
    private Button pointingStick;

    public StickLongClickListener(PointingStickController mPointingStickController,WindowManager.LayoutParams mParams,WindowManager mWindowManager,CircleLayout circleView,Button pointingStick)
    {
        this.mPointingStickController=mPointingStickController;
        this.mParams=mParams;
        this.mWindowManager=mWindowManager;
        this.circleView=circleView;
        this.pointingStick=pointingStick;
    }
    public boolean onLongClick(View v) {
        if(!mPointingStickController.getIsMouseMove()
                &&!mPointingStickController.getTabMode()){
            Log.e("Service", "LONG CLICK");
            //mList.show();
            mParams.width=mParams.width*2;
            mParams.height=mParams.height*2;
            mWindowManager.removeView(pointingStick);
            mWindowManager.addView(circleView, mParams);
            mWindowManager.updateViewLayout(circleView, mParams);
            mPointingStickController.setIsLongMouseClick(true);
            return true;
        }
        return false;
    }
}
