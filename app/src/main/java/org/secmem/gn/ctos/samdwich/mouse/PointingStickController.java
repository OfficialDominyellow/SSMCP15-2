package org.secmem.gn.ctos.samdwich.mouse;

/**
 * Created by SECMEM-DY on 2016-01-09.
 */
public class PointingStickController
{
    private float START_X,START_Y;
    private int PREV_X,PREV_Y;
    private int MAX_X=-1,MAX_Y=-1;
    private int pxWidth=1,pxHeight=-1;
    /* 포인터가 움직이는 중이면 true, 아니면 false */
    private boolean isMouseMove=false;
    private boolean isLongMouseClick=false;
    private boolean moveMode=false;

    private boolean tabMode=false;

    public boolean getTabMode() {
        return tabMode;
    }

    public void setTabMode(boolean tabMode) {
        this.tabMode = tabMode;
    }

    public PointingStickController()
    {

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

    public int getPxWidth() {
        return pxWidth;
    }

    public void setPxWidth(int pxWidth) {
        this.pxWidth = pxWidth;
    }

    public int getPxHeight() {
        return pxHeight;
    }

    public void setPxHeight(int pxHeight) {
        this.pxHeight = pxHeight;
    }

    public boolean getIsMouseMove() {
        return isMouseMove;
    }

    public void setIsMouseMove(boolean isMouseMove) {
        this.isMouseMove = isMouseMove;
    }

    public boolean getIsLongMouseClick() {
        return isLongMouseClick;
    }

    public void setIsLongMouseClick(boolean isLongMouseClick) {
        this.isLongMouseClick = isLongMouseClick;
    }

    public boolean getIsMoveMode() {
        return moveMode;
    }

    public void setMoveMode(boolean moveMode) {
        this.moveMode = moveMode;
    }
}
