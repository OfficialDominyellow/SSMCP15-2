package org.secmem.gn.ctos.samdwich.mouse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.secmem.gn.ctos.samdwich.R;
import org.secmem.gn.ctos.samdwich.global.GlobalVariable;


/**
 * Created by SECMEM-DY on 2016-01-19.
 */
public class CircleViewItemClickListener implements CircleLayout.OnItemClickListener{
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private CircleLayout mCircleView;
    private Button pointingStick;
    private PointingStickController mPointingStickController;
    private Context mContext;
    private TextView centerPoint;
    private ImageView hideImage;
    private WindowManager.LayoutParams mParamsCenter;
    public CircleViewItemClickListener(PointingStickController mPointingStickController)
    {
        this.mPointingStickController=mPointingStickController;
        this.mParams=mPointingStickController.getmParams();
        this.mWindowManager=mPointingStickController.getmWindowManager();
        this.pointingStick=mPointingStickController.getPointingStick();
        this.mCircleView=mPointingStickController.getmCircleView();
        this.mContext=mPointingStickController.getmContext();
        this.mParamsCenter=mPointingStickController.getmParamsCenter();
        this.centerPoint=mPointingStickController.getCenterPoint();
        this.hideImage=mPointingStickController.getHideImage();
    }
    @Override
    public void onItemClick(View view, String name) {
        switch (view.getId()) {
            case R.id.main_move_image://Move Mode
                Log.e("Circle","1");
                if(mPointingStickController.getIsMoveMode()==false) {
                    mPointingStickController.setMoveMode(true);
                    pointingStick.setBackgroundResource(R.drawable.move_mode);
                }
                break;
            case R.id.main_tab_image://Tab Mode
                Log.e("Circle", "2");
                if(!mPointingStickController.getTabMode()) {
                    mPointingStickController.setTabMode(true);
                    pointingStick.setBackgroundResource(R.drawable.tab_mode);
                }
                else
                    mPointingStickController.setTabMode(false);
                break;
            case R.id.main_hide_image://Hide Mode
                Log.e("Circle", "3");
                mWindowManager.removeViewImmediate(mCircleView);
                mParams.width=GlobalVariable.HideImageWidth;
                mParams.height=GlobalVariable.HideImageheight;
                mParams.x=GlobalVariable.displayWidthPx;

                mWindowManager.addView(hideImage, mParams);
                mWindowManager.updateViewLayout(hideImage, mParams);
                mPointingStickController.setHideMode(true);
                break;
            case R.id.main_off_image://Service off
                Log.e("Circle", "4");
                savePreferencesSwitchFalse();
                mContext.stopService(new Intent(mContext, PointingStickService.class));
                //stopService대신 BR을 이용하는게 더 자연스러울듯
                Intent intent=new Intent();
                intent.setAction(GlobalVariable.STOP_SERVICE);
                mContext.sendBroadcast(intent);
                break;
            case R.id.main_back_image://back
                Log.e("Circle", "5");
                break;
        }
        finishCircleView();
        return;
    }
    public void finishCircleView() {
        mPointingStickController.setIsOptionMenu(false);
        if(mPointingStickController.isHideMode())
            return;
        mWindowManager.removeViewImmediate(mCircleView);
        mParams.width=mParams.width/2;
        mParams.height = mParams.height/2;
        mWindowManager.addView(centerPoint, mParamsCenter);
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