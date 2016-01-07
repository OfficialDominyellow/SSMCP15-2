package com.example.hellojni;

import android.os.Handler;
import android.os.Message;

/**
 * Created by chu on 1/7/16.
 */
public class VirtualMouseDriverController {
        public native void moveMouse(int x, int y);
        public static MyHandler myHandler = new MyHandler();
        public static MyThread myThread = new MyThread();

        public VirtualMouseDriverController() {

        }

        public void optimizePosition(int dx, int dy) {
            System.out.println("" + dx + " " + dy);
            moveMouse((int) (dx / 10), (int) (dy / 10));
        }

    public static class MyHandler extends Handler {
        VirtualMouseDriverController virtualMouseDriverController = new VirtualMouseDriverController();

        public MyHandler() {

        }

        @Override
        public void handleMessage(Message msg) {
                System.out.println(""+msg);

                switch (msg.what) {
                    case 0:
                        virtualMouseDriverController.moveMouse((int) (msg.arg1 / 10), 0);
                        break;
                    case 1:
                        virtualMouseDriverController.moveMouse(0, (int) (msg.arg1 / 10));
                        break;
                    default:
                        break;
                }
        }
    }


    public static class MyThread extends Thread {
        private Object mPauseLock;
        private boolean mPaused;
        private boolean mFinished;

        VirtualMouseDriverController virtualMouseDriverController = new VirtualMouseDriverController();
        int dx=0;
        int dy=0;
        boolean isRun=false;

        public MyThread() {
            mPauseLock = new Object();
            mPaused = false;
            mFinished = false;
        }

        public void setDifference(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public void startThread() {
            this.isRun = true;
        }

        public void stopThread() {
            this.isRun = false;
        }

        public boolean getmPause() {
            return this.mPaused;
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
            while (!mFinished) {
                    while (!mPaused) {
                        try {
                            Thread.sleep(5);
                            virtualMouseDriverController.moveMouse((int) (dx / 100), (int) (dy / 100));
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

/*
            this.isRun = true;
            Message msg = new Message();
            Message msg2 = new Message();


            try {
                while (true) {
                    Thread.sleep(5);
                    virtualMouseDriverController.moveMouse((int) (dx / 100), (int) (dy / 100));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
            isRun = false;
            System.out.println("stopped");


            msg.what=0;
            msg.arg1=dx;
            msg2.what=1;
            msg2.arg1=dy;

            myHandler.sendMessage(msg);
            myHandler.sendMessage(msg2);
*/

        }

    }

}
