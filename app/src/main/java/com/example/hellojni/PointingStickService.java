package com.example.hellojni;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

/**
 * Created by SECMEM-DY on 2016-01-06.
 */

public class PointingStickService extends Service{
    private TextView pointingStick;//추후 이미지로 변경해야함
    private WindowManager.LayoutParams mParams; //Layout params객체, 뷰의 위치 크기 지정
    private WindowManager mWindowManager;
    private SeekBar mSeekBar;//투명도 조절
    private static VirtualMouseDriverController virtualMouseDriverController;

    private float START_X,START_Y;
    private int PREV_X,PREV_Y;
    private int MAX_X=-1,MAX_Y=-1;
    /* 포인터가 움직이는 중이면 true, 아니면 false */
    private boolean isMoving=false;
    //private Handler mHandler;
    //final VirtualMouseDriverController.MoveMousePointerThread MMPT = new VirtualMouseDriverController.MoveMousePointerThread(true);
    //private final VirtualMouseDriverController.MyHandler mHandler = new VirtualMouseDriverController.MyHandler();


    private OnTouchListener mViewTouchListener = new OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
            //virtualMouseDriverController.myThread.pauseThread();
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:				//사용자 터치 다운이면
                    if(MAX_X == -1)
                        setMaxPosition();
                    START_X = event.getRawX();					//터치 시작 점
                    START_Y = event.getRawY();					//터치 시작 점
                    PREV_X = mParams.x;							//뷰의 시작 점
                    PREV_Y = mParams.y;							//뷰의 시작 점
                    break;
                case MotionEvent.ACTION_MOVE:

                    int x = (int)(event.getRawX() - START_X);	//이동한 거리
                    int y = (int)(event.getRawY() - START_Y);	//이동한 거리

                    //터치해서 이동한 만큼 이동 시킨다
                    mParams.x = PREV_X + x;
                    mParams.y = PREV_Y + y;

                    mWindowManager.updateViewLayout(pointingStick, mParams);	//뷰 업데이트
                    virtualMouseDriverController.myThread.setDifference(x, y);
                    if(virtualMouseDriverController.myThread.getmPause()) {
                        virtualMouseDriverController.myThread.onResume();
                    } else {

                    }
                    break;
                /* reset position */
                case MotionEvent.ACTION_UP:
                    virtualMouseDriverController.myThread.interrupt();
                    virtualMouseDriverController.myThread.onPause();
                    isMoving=false;
                    //MMPT.stopThread();
                    mParams.x=400;
                    mParams.y=400;
                    mWindowManager.updateViewLayout(pointingStick, mParams);
                    break;
            }
            return true;
        }
    };
    @Override
    public IBinder onBind(Intent arg0) { return null; }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("service", "onCreate");
        if(initMouseDriver()==-1) {
            Toast.makeText(getApplicationContext(), "Fail to load Vmouse.", Toast.LENGTH_LONG).show();
            return;
        }
        pointingStick = new TextView(this);                                                                //뷰 생성
        pointingStick.setText("Pointing\nStick");    //텍스트 설정
        pointingStick.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);                                //텍스트 크기 18sp
        pointingStick.setTextColor(Color.BLUE);                                                            //글자 색상
        pointingStick.setBackgroundColor(Color.argb(127, 0, 255, 255));								//텍스트뷰 배경 색

        pointingStick.setOnTouchListener(mViewTouchListener);										//팝업뷰에 터치 리스너 등록

        //최상위 윈도우에 넣기 위한 설정
        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,					//항상 최 상위에 있게. status bar 밑에 있음. 터치 이벤트 받을 수 있음.
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,		//이 속성을 안주면 터치 & 키 이벤트도 먹게 된다.
                //포커스를 안줘서 자기 영역 밖터치는 인식 안하고 키이벤트를 사용하지 않게 설정
                PixelFormat.TRANSLUCENT);										//투명
        mParams.gravity = Gravity.LEFT | Gravity.TOP;						//왼쪽 상단에 위치하게 함.

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);    //윈도우 매니저 불러옴.
        mWindowManager.addView(pointingStick, mParams);		//최상위 윈도우에 뷰 넣기. *중요 : 여기에 permission을 미리 설정해 두어야 한다. 매니페스트에

        addOpacityController();		//팝업 뷰의 투명도 조절하는 컨트롤러 추가
        virtualMouseDriverController = new VirtualMouseDriverController();
        virtualMouseDriverController.myThread.start();
        virtualMouseDriverController.myThread.onPause();
        //virtualMouseDriverController.myHandler.post(virtualMouseDriverController.myThread);
        //virtualMouseDriverController.myThread.pauseThread();
    }
    /**
     * 뷰의 위치가 화면 안에 있게 최대값을 설정한다
     */
    private void setMaxPosition() {
        DisplayMetrics matrix = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(matrix);		//화면 정보를 가져와서

        MAX_X = matrix.widthPixels - pointingStick.getWidth();			//x 최대값 설정
        MAX_Y = matrix.heightPixels - pointingStick.getHeight();			//y 최대값 설정
    }


    /**
     * 알파값 조절하는 컨트롤러를 추가한다
     */
    private void addOpacityController() {
        mSeekBar = new SeekBar(this);		//투명도 조절 seek bar
        mSeekBar.setMax(100);					//맥스 값 설정.
        mSeekBar.setProgress(100);			//현재 투명도 설정. 100:불투명, 0은 완전 투명
        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override public void onProgressChanged(SeekBar seekBar, int progress,	boolean fromUser) {
                mParams.alpha = progress / 100.0f;			//알파값 설정
                mWindowManager.updateViewLayout(pointingStick, mParams);	//팝업 뷰 업데이트
            }
        });

        //최상위 윈도우에 넣기 위한 설정
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,					//항상 최 상위에 있게. status bar 밑에 있음. 터치 이벤트 받을 수 있음.
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,		//이 속성을 안주면 터치 & 키 이벤트도 먹게 된다.
                //포커스를 안줘서 자기 영역 밖터치는 인식 안하고 키이벤트를 사용하지 않게 설정
                PixelFormat.TRANSLUCENT);										//투명
        params.gravity = Gravity.LEFT | Gravity.TOP;							//왼쪽 상단에 위치하게 함.

        mWindowManager.addView(mSeekBar, params);
    }

    /**
     * 가로 / 세로 모드 변경 시 최대값 다시 설정해 주어야 함.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setMaxPosition();		//최대값 다시 설정
        //optimizePosition();		//뷰 위치 최적화
    }

    @Override
    public void onDestroy() {
        if(mWindowManager != null) {		//서비스 종료시 뷰 제거. *중요 : 뷰를 꼭 제거 해야함.
            if(pointingStick != null) mWindowManager.removeView(pointingStick);
            if(mSeekBar != null) mWindowManager.removeView(mSeekBar);
        }
        Log.e("service","onDestroy");
        removeMouseDriver();
        super.onDestroy();
    }
    static {
        System.loadLibrary("hello-jni");
    }
    public native int initMouseDriver();
    public native void removeMouseDriver();

}
