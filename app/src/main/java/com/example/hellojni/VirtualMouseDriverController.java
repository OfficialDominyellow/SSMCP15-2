package com.example.hellojni;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by chu on 1/7/16.
 */

public class VirtualMouseDriverController extends Thread {
    private volatile static VirtualMouseDriverController uniqueInstance;
    private int dx = 0;
    private int dy = 0;
    private Object mPauseLock;
    private boolean mPaused;
    private boolean mFinished;
    private int mMouseSpeed=5;
    private static int MAXMOVE;
    private static int INTERVAL;
    private int x=0;
    private int y=0;
    private static Context context;

    private int originX=120;
    private int originY=120;

    private native void moveMouse(int x, int y);

    private VirtualMouseDriverController(Context context) {
        mPauseLock = new Object();
        mPaused = false;
        mFinished = false;
        this.context = context;
    }

    public static synchronized VirtualMouseDriverController getInstance(Context context) {
        if (uniqueInstance == null) {
            uniqueInstance = new VirtualMouseDriverController(context);
            MAXMOVE=(int)convertDpToPixel(63,context.getApplicationContext());
            INTERVAL=(int)convertDpToPixel(5,context.getApplicationContext());
        }
        return uniqueInstance;
    }

    public void setDifference(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public boolean getmPause() {
        return this.mPaused;
    }

    public void setmMouseSpeed(int speed) {
        this.mMouseSpeed = speed;
    }

    /**
     * Call this on pause.
     */
    public void onPause() {
        synchronized (mPauseLock) {
            mPaused = true;
        }
    }

    /**
     * Call this on resume.
     */
    public void onResume() {
        synchronized (mPauseLock) {
            mPaused = false;
            mPauseLock.notifyAll();
        }
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
    public int getMouseX()
    {
        return x;
    }
    public int getMouseY()
    {
        return y;
    }

    public int getOriginX(){
        return originX;
    }
    public int getOriginY(){
        return originY;
    }

    @Override
    public void run() {
        int x=0;
        int y=0;
        int sleepTime=10;
        while (!mFinished) {
            while (!mPaused) {
                try {
                    if(Math.sqrt(dx*dx+dy*dy)<(int)convertDpToPixel(10,this.context.getApplicationContext())) {
                        sleepTime=25;
                    } else if(Math.sqrt(dx*dx+dy*dy)<(int)convertDpToPixel(12,this.context.getApplicationContext())) {
                        sleepTime=20;
                    } else if(Math.sqrt(dx*dx+dy*dy)<(int)convertDpToPixel(25,this.context.getApplicationContext())) {
                        sleepTime=13;
                    } else if(Math.sqrt(dx*dx+dy*dy)<(int)convertDpToPixel(50,this.context.getApplicationContext())) {
                        sleepTime=11;
                    } else {
                        sleepTime=8;
                    }
                    Thread.sleep(sleepTime);
                    for (int i=0;i<INTERVAL;i++) {
                        if(Math.abs(dx)<=MAXMOVE/INTERVAL*i) {
                            //x=(dx<0)?(int)(0-Math.sqrt((double)i)):(int)(Math.sqrt((double)i));
                            x=(dx<0)?(int)(0-i):(int)(i);
                            break;
                        }
                    }
                    for (int i=0;i<INTERVAL;i++) {
                        if(Math.abs(dy)<=MAXMOVE/INTERVAL*i) {
                            //y=(dy<0)?(int)(0-Math.sqrt((double)i)):(int)(Math.sqrt((double)i));
                            y=(dy<0)?(int)(0-i):(int)(i);
                            break;
                        }
                    }

                    Log.e("Service", ""+x+" "+y);
                    if(mPaused)
                        continue;
                    else
                        moveMouse(x, y);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            synchronized (mPauseLock) {
                try {
                    mPauseLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }


    }
}
