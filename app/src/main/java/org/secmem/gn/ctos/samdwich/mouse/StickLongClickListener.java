package org.secmem.gn.ctos.samdwich.mouse;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by SECMEM-DY on 2016-01-09.
 */
public class StickLongClickListener implements View.OnLongClickListener
{
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private PointingStickController mPointingStickController;
    private CircleLayout mCircleView;
    private Button pointingStick;
    private TextView centerPoint;

    public StickLongClickListener(PointingStickController mPointingStickController)
    {
        this.mPointingStickController=mPointingStickController;
        this.mParams=mPointingStickController.getmParams();
        this.mWindowManager=mPointingStickController.getmWindowManager();
        this.mCircleView=mPointingStickController.getmCircleView();
        this.pointingStick=mPointingStickController.getPointingStick();
        this.centerPoint=mPointingStickController.getCenterPoint();
    }
    public boolean onLongClick(View v) {
        if(!mPointingStickController.getIsMouseMove()
                &&!mPointingStickController.getTabMode()){
            Log.e("Service", "LONG CLICK");
            mParams.width=mParams.width*2;
            mParams.height=mParams.height*2;
            mWindowManager.removeView(pointingStick);
            mWindowManager.removeView(centerPoint);
            mWindowManager.addView(mCircleView, mParams);
            mWindowManager.updateViewLayout(mCircleView, mParams);
            mPointingStickController.setIsLongMouseClick(true);
            return true;
        }
        return false;
    }
}
