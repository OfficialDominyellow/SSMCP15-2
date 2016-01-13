package com.example.hellojni;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by 김희중 on 2016-01-11.
 */
public class KeyboardPopupService extends Service{

    private static WindowManager mManager;
    private WindowManager.LayoutParams mParams;
    private static ImageView mImage;
    private int mPrimaryCode;
    private int mKeyboardWidth;
    private int mKeyboardHeight;

    private final String TAG = "KeyboardPopupService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mPrimaryCode = Integer.parseInt(intent.getStringExtra("primaryCode").trim());
        mKeyboardWidth = intent.getIntExtra("keyboardWidth", 150);
        mKeyboardHeight = intent.getIntExtra("keyboardHeight", 150);
        Log.i(TAG, "pc : " + mPrimaryCode);


        if (mImage!=null) {
            onDestroy();
        }

        mImage = new ImageView(this);
        Bitmap extendImage = BitmapFactory.decodeResource(getResources(), R.drawable.popimg);

        //ㅁ
        if(mPrimaryCode == 12609){
            extendImage = BitmapFactory.decodeResource(getResources(), R.drawable.mieum_extend);
        }
        //ㄴ
        else if(mPrimaryCode == 12596){
            extendImage = BitmapFactory.decodeResource(getResources(), R.drawable.nieun_extend);
        }
        //ㅇ
        else if(mPrimaryCode == 12615){
            extendImage = BitmapFactory.decodeResource(getResources(), R.drawable.ieung_extend);
        }
        //ㅅ
        else if(mPrimaryCode == 12613){
            extendImage = BitmapFactory.decodeResource(getResources(), R.drawable.siot_extend);
        }
        //ㄱ
        else if(mPrimaryCode == 12593){
            extendImage = BitmapFactory.decodeResource(getResources(), R.drawable.giyeok_extend);
        }

        extendImage = Bitmap.createScaledBitmap(extendImage, mKeyboardWidth / 3, mKeyboardHeight / 3, true);
        mImage.setImageBitmap(extendImage);

        /*
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        mImage = new ImageView(this);
        mImage = (ImageView)inflater.inflate(R.id.giyeok_extend_layout, null);
        Log.i(TAG, "LAYOUT = " + (LinearLayout.LayoutParams)mImage.getLayoutParams());
        */

        //mPrimaryCode에 따라서 다른 image 나타나도록
        mImage.setMaxHeight(1);


        mImage.setAlpha((float)0.9999999);

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
