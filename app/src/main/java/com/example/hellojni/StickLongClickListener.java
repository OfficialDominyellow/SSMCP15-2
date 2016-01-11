package com.example.hellojni;

import android.util.Log;
import android.view.View;
import android.widget.ListPopupWindow;

/**
 * Created by SECMEM-DY on 2016-01-09.
 */
public class StickLongClickListener implements View.OnLongClickListener
{
    private PointingStickController mPointingStickController;
    private ListPopupWindow mList;

    public StickLongClickListener(PointingStickController mPointingStickController,ListPopupWindow mList)
    {
        this.mPointingStickController=mPointingStickController;
        this.mList=mList;
    }
    public boolean onLongClick(View v) {
        if(!mPointingStickController.getIsMouseMove()){
            Log.e("Service", "LONG CLICK");
            mList.show();
            //isLongMouseClick=true;
            mPointingStickController.setIsLongMouseClick(true);
            return true;
        }
        return false;
    }
}
