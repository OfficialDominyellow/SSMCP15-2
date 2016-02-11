package org.secmem.gn.ctos.samdwich.mouse;

import android.content.Context;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SECMEM-DY on 2016-01-09.
 */
public class PointingStickController
{
    private float START_X,START_Y;
    private int PREV_X,PREV_Y;
    private int MAX_X=-1,MAX_Y=-1;
    private int pxWidth=1,pxHeight=-1;
    private int currntX=-1,currntY;
    /* 포인터가 움직이는 중이면 true, 아니면 false */
    private boolean isMouseMove=false;

    private boolean isOptionMenu=false;
    private boolean moveMode=false;
    private boolean tabMode=false;
    private boolean hideMode=false;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams; //Layout params객체, 뷰의 위치 크기 지정
    private WindowManager.LayoutParams mParamsCenter; //Layout params객체, 뷰의 위치 크기 지정

    private CircleLayout mCircleView;
    private Button pointingStick;
    private TextView centerPoint;

    private ImageView hideImage;

    private Context mContext;

    public PointingStickController(Context mContext, WindowManager mWindowManager, WindowManager.LayoutParams mParams , WindowManager.LayoutParams mParamsCenter,
                                   CircleLayout mCircleView,
                                   Button pointingStick,
                                   TextView centerPoint,ImageView hideImage)
    {
        this.mContext=mContext;
        this.mWindowManager=mWindowManager;
        this.mParams=mParams;
        this.mParamsCenter=mParamsCenter;
        this.mCircleView=mCircleView;
        this.pointingStick=pointingStick;
        this.centerPoint=centerPoint;
        this.hideImage=hideImage;
    }
    public int getCurrntX() {return currntX;}

    public void setCurrntX(int currntX) {this.currntX = currntX;}

    public int getCurrntY() {return currntY;}

    public void setCurrntY(int currntY) {this.currntY = currntY;}

    public ImageView getHideImage() {return hideImage;}

    public boolean isHideMode() {return hideMode;}

    public void setHideMode(boolean hideMode) {this.hideMode = hideMode;}

    public Context getmContext() {return mContext;}

    public Button getPointingStick() {return pointingStick;}

    public TextView getCenterPoint() {return centerPoint;}

    public WindowManager.LayoutParams getmParams() {return mParams;}

    public WindowManager.LayoutParams getmParamsCenter() {return mParamsCenter;}

    public WindowManager getmWindowManager() {return mWindowManager;}

    public CircleLayout getmCircleView() {return mCircleView;}

    public boolean getTabMode() {
        return tabMode;
    }

    public void setTabMode(boolean tabMode) {
        this.tabMode = tabMode;
    }

    public float getSTART_X() {
        return START_X;
    }

    public void setSTART_X(float START_X) {
        this.START_X = START_X;
    }

    public float getSTART_Y() {
        return START_Y;
    }

    public void setSTART_Y(float START_Y) {
        this.START_Y = START_Y;
    }

    public int getPREV_X() {
        return PREV_X;
    }

    public void setPREV_X(int PREV_X) {
        this.PREV_X = PREV_X;
    }

    public int getPREV_Y() {
        return PREV_Y;
    }

    public void setPREV_Y(int PREV_Y) {
        this.PREV_Y = PREV_Y;
    }

    public int getMAX_X() {
        return MAX_X;
    }

    public void setMAX_X(int MAX_X) {
        this.MAX_X = MAX_X;
    }

    public int getMAX_Y() {
        return MAX_Y;
    }

    public void setMAX_Y(int MAX_Y) {
        this.MAX_Y = MAX_Y;
    }

    public int getPxWidth() {return pxWidth;}

    public void setPxWidth(int pxWidth) {this.pxWidth = pxWidth;}

    public int getPxHeight() {return pxHeight;}

    public void setPxHeight(int pxHeight) {this.pxHeight = pxHeight;}

    public boolean getIsMouseMove() {return isMouseMove;}

    public void setIsMouseMove(boolean isMouseMove) {this.isMouseMove = isMouseMove;}

    public boolean getIsMoveMode() {return moveMode;}

    public void setMoveMode(boolean moveMode) {this.moveMode = moveMode;}

    public boolean getIsOptionMenu() {return isOptionMenu;}

    public void setIsOptionMenu(boolean isOptionMenu) {this.isOptionMenu = isOptionMenu;}
}
