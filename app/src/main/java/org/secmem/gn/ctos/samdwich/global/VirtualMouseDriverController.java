package org.secmem.gn.ctos.samdwich.global;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by chu on 1/7/16.
 */

public class VirtualMouseDriverController extends Thread {
    private volatile static VirtualMouseDriverController uniqueInstance;
    private int dx = 0;
    private int dy = 0;
    private double result=0;
    private float metrixDensityDpi;
    private Object mPauseLock;
    private boolean mPaused;
    private boolean mFinished;
    private int mMouseSpeed=5;
    private static int MAXMOVE;
    private static int INTERVAL;
    private static int DIVIDEDMAXMOVE;

    private Resources resources;
    private DisplayMetrics metrics;

    final private String TAG = "VMDriverCont";

    private VirtualMouseDriverController(Context context) {
        mPauseLock = new Object();
        mPaused = false;
        mFinished = false;
        resources = context.getResources();
        metrics = resources.getDisplayMetrics();
        metrixDensityDpi=metrics.densityDpi / 160f;
    }

    public static synchronized VirtualMouseDriverController getInstance(Context context) {
        if (uniqueInstance == null) {
            uniqueInstance = new VirtualMouseDriverController(context);
            MAXMOVE=(int) GlobalVariable.convertDpToPixel(63, context.getApplicationContext());
            INTERVAL=(int)GlobalVariable.convertDpToPixel(5,context.getApplicationContext());
            DIVIDEDMAXMOVE=MAXMOVE/INTERVAL;
        }
        return uniqueInstance;
    }

    public void setDifference(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
        result=Math.sqrt(dx*dx+dy*dy);
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

    public void onRestart(){
        synchronized (mPauseLock) {
            mFinished = false;
            mPauseLock.notifyAll();
        }
    }
    public void onDestroy()
    {
        synchronized (mPauseLock) {
            mFinished = true;
        }
    }

    @Override
    public void run() {
        int x=0;
        int y=0;
        int sleepTime=10;
        Log.i(TAG, "is run ??");
        while (!mFinished) {
            while (!mPaused) {
                try {
                    if(result<(10 * metrixDensityDpi)) {
                        sleepTime=25;
                    } else if(result<(12 * metrixDensityDpi)) {
                        sleepTime=20;
                    } else if(result<(25 * metrixDensityDpi)) {
                        sleepTime=13;
                    } else if((result<50 * metrixDensityDpi)) {
                        sleepTime=11;
                    } else {
                        sleepTime=8;
                    }
                    Thread.sleep(sleepTime);
                    int abs=Math.abs(dx);
                    for (int i=0;i<INTERVAL;i++) {
                        if(abs<=DIVIDEDMAXMOVE*i) {
                            //x=(dx<0)?(int)(0-Math.sqrt((double)i)):(int)(Math.sqrt((double)i));
                            x=(dx<0)?(int)(0-i):(int)(i);
                            break;
                        }
                    }
                    abs=Math.abs(dy);
                    for (int i=0;i<INTERVAL;i++) {
                        if(abs<=DIVIDEDMAXMOVE*i) {
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
    static {
        System.loadLibrary("samdwich_jni");
    }
    private native void moveMouse(int x, int y);
}
