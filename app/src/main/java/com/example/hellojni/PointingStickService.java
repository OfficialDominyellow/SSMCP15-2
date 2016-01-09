package com.example.hellojni;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;


/**
 * Created by SECMEM-DY on 2016-01-06.
 */

public class PointingStickService extends Service{
    private PointingStickController mPointingStickController;
    private StickLongClickListener mStickLongClickListener;
    private ModeItemClickListener mModeItemClickListener;
    private StickTouchListenenr mStickTouchListenenr;

    private Button pointingStick;
    private WindowManager.LayoutParams mParams; //Layout params객체, 뷰의 위치 크기 지정
    private WindowManager mWindowManager;
    private SeekBar mSeekBar;//투명도 조절
    private ListPopupWindow mList;//옵션 제공 (롱클릭시);
    private String[] Options={"Move","Hide","Tap"};//test

    public static VirtualMouseDriverController virtualMouseDriverController;
    /* 포인터가 움직이는 중이면 true, 아니면 false */

    /*onTouch 에서
    return true 를 반환하면 이후 비슷한 이벤트는 더이상 진행되지 않음
    만일 onTouch 에서 특정한 플래그 값만 변경하고,
    이후 click, longclick 이벤트가 계속 수행되길 원하시면 필요한 작업 후
    return false 를 반환
    이벤트 호출 순서 onTouch -> onLongClick -> onClick .*/

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
        mPointingStickController=new PointingStickController(Options);
        pointingStick = new Button(this);
        //pointingStick=mPointingStickController.getPointingStick();                                                         //뷰 생성

        pointingStick.setText("Pointing\nStick");    //텍스트 설정
        pointingStick.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);                                //텍스트 크기 18sp
        pointingStick.setTextColor(Color.BLUE);                                                            //글자 색상
        pointingStick.setBackgroundColor(Color.argb(127, 0, 255, 255));								//텍스트뷰 배경 색

        mList=new ListPopupWindow(this);
        mList.setWidth(300);
        mList.setHeight(300);
        mList.setAnchorView(pointingStick);//리스트 팝업 윈도우 등록
        mList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                Options));
        mList.setModal(true);

        //최상위 윈도우에 넣기 위한 설정
        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,					//항상 최 상위에 있게. status bar 밑에 있음. 터치 이벤트 받을 수 있음.
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,		//이 속성을 안주면 터치 & 키 이벤트도 먹게 된다.
                //포커스를 안줘서 자기 영역 밖터치는 인식 안하고 키이벤트를 사용하지 않게 설정
                PixelFormat.TRANSLUCENT);										//투명
        //mParams.gravity = Gravity.LEFT | Gravity.TOP;						//왼쪽 상단에 위치하게 함.
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


        initPosition();
        mWindowManager.addView(pointingStick, mParams);		//최상위 윈도우에 뷰 넣기. *중요 : 여기에 permission을 미리 설정해 두어야 한다. 매니페스트에

        addOpacityController();		//팝업 뷰의 투명도 조절하는 컨트롤러 추가

        virtualMouseDriverController = virtualMouseDriverController.getInstance(getApplicationContext());
        if (virtualMouseDriverController.getState()==Thread.State.NEW) {
            virtualMouseDriverController.start();
            virtualMouseDriverController.onPause();
        }
        setAllListener();
    }
    public void setAllListener()
    {
        mStickLongClickListener=new StickLongClickListener(mPointingStickController,mList);
        pointingStick.setOnLongClickListener(mStickLongClickListener);

        mStickTouchListenenr=new StickTouchListenenr(mPointingStickController,mParams, mList, mWindowManager,  pointingStick,
                 this);
        pointingStick.setOnTouchListener(mStickTouchListenenr);

        mModeItemClickListener=new ModeItemClickListener(mPointingStickController,mList);
        mList.setOnItemClickListener(mModeItemClickListener);

    }

    /**
     * 뷰의 위치가 화면 안에 있게 최대값을 설정한다
     */
    private void initPosition() {
        DisplayMetrics matrix = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(matrix);		//화면 정보를 가져와서

        mPointingStickController.setPxWidth(matrix.widthPixels);
        mPointingStickController.setPxHeight(matrix.heightPixels);

        //x 최대값 설정
        mPointingStickController.setMAX_X(mPointingStickController.getPxWidth() - pointingStick.getWidth());
        //y 최대값 설정
        mPointingStickController.setMAX_Y(mPointingStickController.getPxHeight() - pointingStick.getHeight());

        mPointingStickController.setPxWidth(mPointingStickController.getPxWidth()/5);
        mPointingStickController.setPxHeight(mPointingStickController.getPxHeight() / 5);

        mParams.x= mPointingStickController.getPxWidth();
        mParams.y=mPointingStickController.getPxHeight();//x,y 위치 초기화
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
        initPosition();
    }// 화면 roatate시에 발생

    @Override
    public void onDestroy() {
        virtualMouseDriverController.interrupt();

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
