package com.example.hellojni;

import android.os.Handler;
import android.os.Message;
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

    private native void moveMouse(int x, int y);

    private VirtualMouseDriverController() {
        mPauseLock = new Object();
        mPaused = false;
        mFinished = false;
    }

    public static synchronized VirtualMouseDriverController getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new VirtualMouseDriverController();
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

    @Override
    public void run() {
        int x=0;
        int y=0;
        while (!mFinished) {
            while (!mPaused) {
                try {
                    Thread.sleep(5);

                    final int MAXMOVE=200;
                    final int INTERVAL=100;

                    for (int i=0;i<INTERVAL;i++) {
                        if(Math.abs(dx)<=MAXMOVE/INTERVAL*i) {
                            x=(dx<0)?(int)(0-Math.sqrt((double)i)):(int)(Math.sqrt((double)i));
                            break;
                        }
                    }
                    for (int i=0;i<INTERVAL;i++) {
                        if(Math.abs(dy)<=MAXMOVE/INTERVAL*i) {
                            y=(dy<0)?(int)(0-Math.sqrt((double)i)):(int)(Math.sqrt((double)i));
                            break;
                        }
                    }

                    Log.e("Service", ""+x+" "+y);

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
