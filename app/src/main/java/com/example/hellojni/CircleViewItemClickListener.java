package com.example.hellojni;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by SECMEM-DY on 2016-01-19.
 */
public class CircleViewItemClickListener implements CircleLayout.OnItemClickListener{
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private CircleLayout circleView;
    private Button pointingStick;
    private PointingStickController mPointingStickController;
    public CircleViewItemClickListener(PointingStickController mPointingStickController,WindowManager.LayoutParams mParams,WindowManager mWindowManager,CircleLayout circleView,Button pointingStick)
    {
        this.mPointingStickController=mPointingStickController;
        this.mParams=mParams;
        this.mWindowManager=mWindowManager;
        this.pointingStick=pointingStick;
        this.circleView=circleView;
    }
    @Override
    public void onItemClick(View view, String name) {
        switch (view.getId()) {
            case R.id.main_calendar_image:
                Log.e("Circle","1");
                if(mPointingStickController.getIsMoveMode()==false) {
                    // moveMode = true;
                    mPointingStickController.setMoveMode(true);
                }
                // Handle calendar click
                break;
            case R.id.main_cloud_image:
                Log.e("Circle","2");
                if(!mPointingStickController.getTabMode())
                    mPointingStickController.setTabMode(true);
                else
                    mPointingStickController.setTabMode(false);

                mPointingStickController.setIsLongMouseClick(false);
                // Handle cloud click
                break;
            case R.id.main_facebook_image:
                Log.e("Circle","3");
                // Handle facebook click

                mPointingStickController.setIsLongMouseClick(false);
                break;
            case R.id.main_key_image:
                Log.e("Circle", "4");

                // Handle key click
                mPointingStickController.setIsLongMouseClick(false);
                break;
        }
        mWindowManager.removeView(circleView);
        mParams.width=mParams.width/2;
        mParams.height = mParams.height/2;
        mWindowManager.addView(pointingStick, mParams);

        mWindowManager.updateViewLayout(pointingStick, mParams);
    }
}
