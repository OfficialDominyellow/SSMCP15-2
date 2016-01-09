package com.example.hellojni;

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
                    //Options[0]="Fix";
                    mPointingStickController.setOptions(0,"Fix");
                }
                else
                {
                    //moveMode=false;
                    mPointingStickController.setMoveMode(false);
                    //Options[0]="Move";
                    mPointingStickController.setOptions(0,"Move");
                }
                mList.dismiss();
                break;
            case 1:
                mList.dismiss();
                break;
            case 2:
                mList.dismiss();
                break;
        }
    }
}
