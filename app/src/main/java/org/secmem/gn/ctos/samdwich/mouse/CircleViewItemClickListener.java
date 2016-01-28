package org.secmem.gn.ctos.samdwich.mouse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

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
    public CircleViewItemClickListener(PointingStickController mPointingStickController,WindowManager.LayoutParams mParams,WindowManager mWindowManager,CircleLayout mCircleView,Button pointingStick,Context mContext)
    {
        this.mPointingStickController=mPointingStickController;
        this.mParams=mParams;
        this.mWindowManager=mWindowManager;
        this.pointingStick=pointingStick;
        this.mCircleView=mCircleView;
        this.mContext=mContext;
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
                Log.e("Circle","3");
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
            default:
                Log.e("Circle", "Back");
                break;
        }
        finishCircleView();
    }
    public void finishCircleView()
    {
        mPointingStickController.setIsLongMouseClick(false);
        mWindowManager.removeView(mCircleView);
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
