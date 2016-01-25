package org.secmem.gn.ctos.samdwich.mouse;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by SECMEM-DY on 2016-01-09.
 */
public class StickLongClickListener implements View.OnLongClickListener
{
    private WindowManager.LayoutParams mParams; //Layout params객체, 뷰의 위치 크기 지정
    private WindowManager mWindowManager;
    private PointingStickController mPointingStickController;
    private CircleLayout mCircleView;
    private Button pointingStick;

    public StickLongClickListener(PointingStickController mPointingStickController,WindowManager.LayoutParams mParams,WindowManager mWindowManager,CircleLayout mCircleView,Button pointingStick)
    {
        this.mPointingStickController=mPointingStickController;
        this.mParams=mParams;
        this.mWindowManager=mWindowManager;
        this.mCircleView=mCircleView;
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
            mWindowManager.addView(mCircleView, mParams);
            mWindowManager.updateViewLayout(mCircleView, mParams);
            mPointingStickController.setIsLongMouseClick(true);
            return true;
        }
        return false;
    }
}
