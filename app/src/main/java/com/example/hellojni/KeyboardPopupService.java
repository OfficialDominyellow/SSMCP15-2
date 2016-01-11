package com.example.hellojni;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by 김희중 on 2016-01-11.
 */
public class KeyboardPopupService extends Service{

    private static WindowManager mManager;
    private WindowManager.LayoutParams mParams;
    private static ImageView mImage;
    private int mPrimaryCode;

    private final String TAG = "KeyboardPopupService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mPrimaryCode = Integer.parseInt(intent.getStringExtra("primaryCode").trim());
        Log.i(TAG, "pc : " + mPrimaryCode);

        //mPrimaryCode에 따라서 다른 image 나타나도록

        if (mImage!=null) {
            onDestroy();
        }

        mImage = new ImageView(this);
        mImage.setImageResource(R.drawable.popimg);

        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                // �׻� �ֻ��� ȭ�鿡 �ֵ��� �����մϴ�
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                // ��ġ �̺�Ʈ�� ���� �ʽ��ϴ�
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT); // ����

        mManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mManager.addView(mImage, mParams);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service Start");

        onDestroy();

        /*
        Intent intent = new Intent(this, KeyboardPopupService.class);

        Log.i(TAG, "pc : " + intent.getStringExtra("primaryCode"));
        //mPrimaryCode = Integer.parseInt(intent.getStringExtra("primaryCode").trim());
        //Log.i(TAG, "ck : " + mPrimaryCode);

        */
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "Service Destroy");

        if (mImage != null) {
            mManager.removeViewImmediate(mImage);

        }
        mImage = null;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
