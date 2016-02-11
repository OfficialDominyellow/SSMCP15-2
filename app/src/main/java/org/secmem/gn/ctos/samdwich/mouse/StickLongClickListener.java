package org.secmem.gn.ctos.samdwich.mouse;

import android.util.Log;
import android.view.View;

/**
 * Created by SECMEM-DY on 2016-01-09.
 */
public class StickLongClickListener implements View.OnLongClickListener
{
    private PointingStickController mPointingStickController;
    public StickLongClickListener(PointingStickController mPointingStickController)
    {
        this.mPointingStickController=mPointingStickController;
    }
    public boolean onLongClick(View v) {
        if(!mPointingStickController.getIsMouseMove()
                &&!mPointingStickController.getTabMode()){
            Log.e("Service", "LONG CLICK");
            downLeftMouse();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            upLeftMouse();
            return true;
        }
        return false;
    }
    static {
        System.loadLibrary("samdwich_jni");
    }
    public native void downLeftMouse();
    public native void upLeftMouse();
}
