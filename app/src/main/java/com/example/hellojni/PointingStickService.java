package com.example.hellojni;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;


/**
 * Created by SECMEM-DY on 2016-01-06.
 */

public class PointingStickService extends Service{
    private Button pointingStick;
    private Button moveableStick;
    //private TextView pointingStick;//추후 이미지로 변경해야함
    private WindowManager.LayoutParams mParams; //Layout params객체, 뷰의 위치 크기 지정
    private WindowManager.LayoutParams mParams2; //Layout params객체, 뷰의 위치 크기 지정


    private WindowManager mWindowManager;
    private SeekBar mSeekBar;//투명도 조절
    private static VirtualMouseDriverController virtualMouseDriverController;

    private float START_X,START_Y;
    private int PREV_X,PREV_Y;
    private int MAX_X=-1,MAX_Y=-1;
    private int pxWidth=1,pxHeight=-1;
    /* 포인터가 움직이는 중이면 true, 아니면 false */
    private boolean isMoving=false;
    private boolean isMouseMove=false;
    //private Handler mHandler;
    //final VirtualMouseDriverController.MoveMousePointerThread MMPT = new VirtualMouseDriverController.MoveMousePointerThread(true);
    //private final VirtualMouseDriverController.MyHandler mHandler = new VirtualMouseDriverController.MyHandler();

    private GestureDetector mGestureDetector=new GestureDetector(new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

    });

    private OnTouchListener mViewTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int xdiff=0;
            int ydiff=0;
            //virtualMouseDriverController.myThread.pauseThread();
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:				//사용자 터치 다운이면
                    if(MAX_X == -1)
                        setMaxPosition();
                    START_X = event.getRawX();					//터치 시작 점
                    START_Y = event.getRawY();					//터치 시작 점
                    PREV_X = mParams.x;							//뷰의 시작 점
                    PREV_Y = mParams.y;							//뷰의 시작
                    isMouseMove=false;
                    Log.e("Service","ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:

                    xdiff = (int)(event.getRawX() - START_X);	//이동한 거리
                    ydiff = (int)(event.getRawY() - START_Y);	//이동한 거리

                    //터치해서 이동한 만큼 이동 시킨다
                    mParams.x = PREV_X + xdiff;
                    mParams.y = PREV_Y + ydiff;

                    mWindowManager.updateViewLayout(pointingStick, mParams);	//뷰 업데이트
                    optimizePosition();
                    isMouseMove=true;

                    Log.e("Service","ACTION_MOVE");
                    virtualMouseDriverController.myThread.setDifference(xdiff,ydiff);
                    if(virtualMouseDriverController.myThread.getmPause()) {
                        virtualMouseDriverController.myThread.onResume();
                    } else {

                    }
                    break;
                /* reset position */
                case MotionEvent.ACTION_UP:
                    if(!isMouseMove)
                    {
                        clickLeftMouse();
                        Log.e("Service", "left mouse clicked");
                    }
                    virtualMouseDriverController.myThread.interrupt();
                    virtualMouseDriverController.myThread.onPause();
                    isMouseMove=false;
                    //MMPT.stopThread();
                    Log.e("Service","ACTION_UP");
                    mParams.x=pxWidth;
                    mParams.y=pxHeight;//상대적으로 좌표 설정 ,원위치로 변경
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

        moveableStick = new Button(this);                                                                //뷰 생성
        moveableStick.setText("Movealbe");    //텍스트 설정
        moveableStick.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);                                //텍스트 크기 18sp
        moveableStick.setTextColor(Color.BLACK);                                                            //글자 색상
        moveableStick.setBackgroundColor(Color.argb(255, 255, 255, 255));								//텍스트뷰 배경 색

        moveableStick.setOnTouchListener(mViewTouchListener);										//팝업뷰에 터치 리스너 등록
        //moveableStick.setOnClickListener(button1ClickListener);



        pointingStick = new Button(this);                                                                //뷰 생성
        pointingStick.setText("Pointing\nStick");    //텍스트 설정
        pointingStick.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);                                //텍스트 크기 18sp
        pointingStick.setTextColor(Color.BLUE);                                                            //글자 색상
        pointingStick.setBackgroundColor(Color.argb(127, 0, 255, 255));								//텍스트뷰 배경 색

        pointingStick.setOnTouchListener(mViewTouchListener);

        //최상위 윈도우에 넣기 위한 설정
        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,					//항상 최 상위에 있게. status bar 밑에 있음. 터치 이벤트 받을 수 있음.
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,		//이 속성을 안주면 터치 & 키 이벤트도 먹게 된다.
                //포커스를 안줘서 자기 영역 밖터치는 인식 안하고 키이벤트를 사용하지 않게 설정
                PixelFormat.TRANSLUCENT);										//투명
        //mParams.gravity = Gravity.LEFT | Gravity.TOP;						//왼쪽 상단에 위치하게 함.




        mParams2 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,					//항상 최 상위에 있게. status bar 밑에 있음. 터치 이벤트 받을 수 있음.
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,		//이 속성을 안주면 터치 & 키 이벤트도 먹게 된다.
                //포커스를 안줘서 자기 영역 밖터치는 인식 안하고 키이벤트를 사용하지 않게 설정
                PixelFormat.TRANSLUCENT);										//투명
        //mParams.gravity = Gravity.LEFT | Gravity.TOP;						//왼쪽 상단에 위치하게 함.

        mParams2.x=pxWidth;
        mParams2.y=pxHeight;//상대적으로 좌표 설정

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);    //윈도우 매니저 불러옴.
        //mWindowManager.addView(moveableStick, mParams2);		//최상위 윈도우에 뷰 넣기. *중요 : 여기에 permission을 미리 설정해 두어야 한다. 매니페스트에

        setMaxPosition();
        mParams.x=pxWidth;
        mParams.y=pxHeight;//상대적으로 좌표 설정

        mWindowManager.addView(pointingStick, mParams);		//최상위 윈도우에 뷰 넣기. *중요 : 여기에 permission을 미리 설정해 두어야 한다. 매니페스트에

        addOpacityController();		//팝업 뷰의 투명도 조절하는 컨트롤러 추가
        virtualMouseDriverController = new VirtualMouseDriverController();
        virtualMouseDriverController.myThread.start();
        virtualMouseDriverController.myThread.onPause();
    }
    /**
     * 뷰의 위치가 화면 안에 있게 최대값을 설정한다
     */
    private void setMaxPosition() {
        DisplayMetrics matrix = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(matrix);		//화면 정보를 가져와서

        pxWidth=matrix.widthPixels;
        pxHeight=matrix.heightPixels;

        MAX_X = pxWidth - pointingStick.getWidth();            //x 최대값 설정
        MAX_Y = pxHeight - pointingStick.getHeight();			//y 최대값 설정

        Log.e("Service","setMaxPosition X:"+pxWidth+"Y:"+pxHeight);
        pxWidth=pxWidth/5;
        pxHeight=pxHeight/3;
        Log.e("Service","setMaxPosition2 X:"+pxWidth+"Y:"+pxHeight);
    }

    private void optimizePosition() {
        //최대값 넘어가지 않게 설정
        if(mParams.x > MAX_X)
            mParams.x = pxWidth;
        if(mParams.y > MAX_Y)
            mParams.y = pxHeight;
        if(mParams.x < 0)
            mParams.x = 0;
        if(mParams.y < 0)
            mParams.y = 0;
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
        mWindowManager.addView(mSeekBar, params);//pointing Stick;
    }

    /**
     * 가로 / 세로 모드 변경 시 최대값 다시 설정해 주어야 함.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setMaxPosition();		//최대값 다시 설정
        optimizePosition();		//뷰 위치 최적화
    }// 화면 roatate시에 발생

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
    public native void clickLeftMouse();

}
