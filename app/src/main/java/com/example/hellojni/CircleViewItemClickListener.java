package com.example.hellojni;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private Context mContext;
    public CircleViewItemClickListener(PointingStickController mPointingStickController,WindowManager.LayoutParams mParams,WindowManager mWindowManager,CircleLayout circleView,Button pointingStick,Context mContext)
    {
        this.mPointingStickController=mPointingStickController;
        this.mParams=mParams;
        this.mWindowManager=mWindowManager;
        this.pointingStick=pointingStick;
        this.circleView=circleView;
        this.mContext=mContext;
    }
    @Override
    public void onItemClick(View view, String name) {
        switch (view.getId()) {
            case R.id.main_calendar_image://Move Mode
                Log.e("Circle","1");
                if(mPointingStickController.getIsMoveMode()==false) {
                    // moveMode = true;
                    mPointingStickController.setMoveMode(true);
                }
                // Handle calendar click
                break;
            case R.id.main_cloud_image://Tab Mode
                Log.e("Circle","2");
                if(!mPointingStickController.getTabMode())
                    mPointingStickController.setTabMode(true);
                else
                    mPointingStickController.setTabMode(false);

                mPointingStickController.setIsLongMouseClick(false);
                // Handle cloud click
                break;
            case R.id.main_facebook_image://Hide Mode
                Log.e("Circle","3");
                // Handle facebook click

                mPointingStickController.setIsLongMouseClick(false);
                break;
            case R.id.main_key_image://Service off
                Log.e("Circle", "4");
                // Handle key click
                mPointingStickController.setIsLongMouseClick(false);
                savePreferencesSwitchFalse();
                mContext.stopService(new Intent(mContext, PointingStickService.class));
                //stopService대신 BR을 이용하는게 더 자연스러울듯
                Intent intent=new Intent();
                intent.setAction(GlobalVariable.STOP_SERVICE);
                mContext.sendBroadcast(intent);

                break;
        }
        mWindowManager.removeView(circleView);
        mParams.width=mParams.width/2;
        mParams.height = mParams.height/2;
        mWindowManager.addView(pointingStick, mParams);
        mWindowManager.updateViewLayout(pointingStick, mParams);
    }
    private void savePreferencesSwitchFalse(){
        SharedPreferences pref = mContext.getSharedPreferences("forSwitch", mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("switchMode", false);
        editor.commit();
    }
}
