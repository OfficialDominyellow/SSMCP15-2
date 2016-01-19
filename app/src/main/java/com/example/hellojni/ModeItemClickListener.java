package com.example.hellojni;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;

/**
 * Created by SECMEM-DY on 2016-01-09.
 */
public class ModeItemClickListener implements AdapterView.OnItemClickListener {
    private PointingStickController mPointingStickController;
    private ListPopupWindow mList;
    public ModeItemClickListener(PointingStickController mPointingStickController,ListPopupWindow mList)
    {
        this.mPointingStickController=mPointingStickController;
        this.mList=mList;
    }
    public void onItemClick(AdapterView<?>parent,View view,int position,long id) {
        switch(position)
        {
            case 0://move pointing Stick
                if(mPointingStickController.getIsMoveMode()==false) {
                   // moveMode = true;
                    mPointingStickController.setMoveMode(true);
                }
                mList.dismiss();
                break;
            case 1://tab
                if(!mPointingStickController.getTabMode())
                    mPointingStickController.setTabMode(true);
                else
                    mPointingStickController.setTabMode(false);
                //clickTabKey();
                mList.dismiss();
                Log.e("Item", "Tab");
            case 2://off
                mList.dismiss();
                Log.e("Item", "Off");
                break;
        }
    }
}
