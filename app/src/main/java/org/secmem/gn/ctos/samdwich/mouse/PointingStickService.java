package org.secmem.gn.ctos.samdwich.mouse;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.secmem.gn.ctos.samdwich.R;
import org.secmem.gn.ctos.samdwich.global.GlobalVariable;
import org.secmem.gn.ctos.samdwich.global.VirtualMouseDriverController;


/**
 * Created by SECMEM-DY on 2016-01-06.
 */

public class PointingStickService extends Service{
    private PointingStickController mPointingStickController;

    private Button pointingStick;
    private TextView centerPoint;
    private WindowManager.LayoutParams mParams; //Layout params객체, 뷰의 위치 크기 지정
    private WindowManager.LayoutParams mParamsCenter; //Layout params객체, 뷰의 위치 크기 지정
    private WindowManager.LayoutParams mParamsHover;

    private WindowManager mWindowManager;

    private LayoutInflater mInflater;
    private CircleLayout mCircleView;
    private ImageView hideImage;
    private ImageView optionImage;

    private IntentFilter filter;
    private IntentFilter filter2;
    private IntentFilter filter3;
    private IntentFilter filter4;

    private VirtualMouseDriverController virtualMouseDriverController;
    private int mProgress;
    private int mSize;
    /* 포인터가 움직이는 중이면 true, 아니면 false */
    /*onTouch 에서
    return true 를 반환하면 이후 비슷한 이벤트는 더이상 진행되지 않음
    만일 onTouch 에서 특정한 플래그 값만 변경하고,
    이후 click, longclick 이벤트가 계속 수행되길 원하시면 필요한 작업 후
    return false 를 반환
    이벤트 호출 순서 onTouch -> onLongClick -> onClick .*/
    public void setProgress(int progress)
    {
        mParams.alpha = progress / 100.0f;			//알파값 설정
        updateView();
    }
    public void setStickSize(int size)
    {
        int newSize=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());//dp로 변환
        if(mPointingStickController.getIsOptionMenu())
                newSize*=2;
        GlobalVariable.stickWidth =newSize;
        GlobalVariable.stickHeight = newSize;
        if(mPointingStickController.isHideMode())
            return;
        mParams.width=GlobalVariable.stickWidth;
        mParams.height=GlobalVariable.stickHeight;
        mParamsCenter.x=mParams.x;
        mParamsCenter.y=mParams.y;//center point x,y
        updateView();
    }
    public void setStickHide()
    {
        if(mPointingStickController.getIsOptionMenu())
            mWindowManager.removeViewImmediate(mCircleView);
        else if(mPointingStickController.isHideMode())
            mWindowManager.removeViewImmediate(hideImage);
        else{
            mWindowManager.removeViewImmediate(pointingStick);
            mWindowManager.removeViewImmediate(centerPoint);
        }
    }
    public void setStickDisplay()
    {
        if (mPointingStickController.getIsOptionMenu())
            mWindowManager.addView(mCircleView, mParams);
        else if(mPointingStickController.isHideMode())
            mWindowManager.addView(hideImage,mParams);
        else {
            mWindowManager.addView(centerPoint, mParamsCenter);
            mWindowManager.addView(pointingStick, mParams);
        }
        updateView();
    }
    public void updateView()
    {
        if(mPointingStickController.getIsOptionMenu())
            mWindowManager.updateViewLayout(mCircleView, mParams);
        else if(mPointingStickController.isHideMode())
            mWindowManager.updateViewLayout(hideImage,mParams);
        else
            mWindowManager.updateViewLayout(pointingStick, mParams);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("service", "onCreate");
        if(initMouseDriver()==-1) {
            Toast.makeText(getApplicationContext(), "Fail to load Vmouse.", Toast.LENGTH_LONG).show();
            return;
        }
        getPreferencesProgress();getPreferencesSize();//설정값 불러오기

        filter = new IntentFilter();
        filter.addAction(GlobalVariable.HIDE_SERVICE);
        registerReceiver(hideReceiver, filter);

        filter2 = new IntentFilter();
        filter2.addAction(GlobalVariable.DISP_SERVICE);
        registerReceiver(dispReceiver, filter2);

        filter3 = new IntentFilter();
        filter3.addAction(GlobalVariable.CHANGE_SIZE);
        registerReceiver(sizeReceiver, filter3);

        filter4 = new IntentFilter();
        filter4.addAction(GlobalVariable.CHANGE_PROG);
        registerReceiver(progressReceiver, filter4);

        pointingStick = new Button(this);
        pointingStick.setBackgroundResource(R.drawable.pointing_stick);
        pointingStick.setText("●");    //텍스트 설정
        pointingStick.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);                                //텍스트 크기 18sp
        pointingStick.setTextColor(Color.BLUE);
        centerPoint=new TextView(this);
        centerPoint.setText("●");
        centerPoint.setTextColor(Color.RED);
        optionImage=new ImageView(this);
        hideImage = new ImageView(this);

        Bitmap bm = BitmapFactory.decodeResource(this.getResources(), R.drawable.small_button);
        bm = Bitmap.createScaledBitmap(bm, bm.getWidth(), bm.getHeight() ,true);
        int bmWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, bm.getWidth(), getResources().getDisplayMetrics());
        int bmHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, bm.getHeight(), getResources().getDisplayMetrics());
        GlobalVariable.HideImageWidth=bmWidth;
        GlobalVariable.HideImageheight=bmHeight;
        hideImage.setImageBitmap(bm);

        Bitmap bmHover = BitmapFactory.decodeResource(this.getResources(), R.drawable.hover);
        optionImage.setImageBitmap(bmHover);

        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCircleView = (CircleLayout) mInflater.inflate(R.layout.sample_no_rotation2, null);

        //최상위 윈도우에 넣기 위한 설정
        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,					//항상 최 상위에 있게. status bar 밑에 있음. 터치 이벤트 받을 수 있음.
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE		//이 속성을 안주면 터치 & 키 이벤트도 먹게 된다.
                |WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                |WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                //포커스를 안줘서 자기 영역 밖터치는 인식 안하고 키이벤트를 사용하지 않게 설정
                PixelFormat.TRANSLUCENT);										//투명

        mParamsCenter= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                |WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                |WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                PixelFormat.TRANSLUCENT);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mParamsHover= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        |WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        mParamsHover.gravity= Gravity.BOTTOM;


        virtualMouseDriverController = virtualMouseDriverController.getInstance(getApplicationContext());
        if (virtualMouseDriverController.getState()==Thread.State.NEW) {
            virtualMouseDriverController.start();
            virtualMouseDriverController.onPause();
        }
        mPointingStickController=new PointingStickController(this,
                mWindowManager,
                mParams ,
                mParamsCenter,
                mCircleView,
                pointingStick,
                centerPoint,
                hideImage);
        initPosition();//순서 변경시 에러 발생 =>null exception
        mWindowManager.addView(optionImage, mParamsHover);
        mWindowManager.addView(centerPoint, mParamsCenter);
        mWindowManager.addView(pointingStick, mParams);		//최상위 윈도우에 뷰 넣기. *중요 : 여기에 permission을 미리 설정해 두어야 한다. 매니페스트에
        mParams.alpha = mProgress / 100.0f;			//알파값 설정
        mParamsHover.alpha=0.0f;
        mWindowManager.updateViewLayout(pointingStick, mParams);	//팝업 뷰 업데이트
        mParams.windowAnimations=android.R.style.Animation_Dialog;

        setAllListener();
    }
    public void setAllListener()
    {
        mCircleView.setOnItemClickListener(new CircleViewItemClickListener(mPointingStickController));
        pointingStick.setOnLongClickListener(new StickLongClickListener(mPointingStickController));
        pointingStick.setOnTouchListener(new StickTouchListener(mPointingStickController, virtualMouseDriverController));
        hideImage.setOnTouchListener(new HideTouchListener(mPointingStickController));
        optionImage.setOnTouchListener(new MotionListener(mPointingStickController));
    }
    /**
     * 뷰의 위치가 화면 안에 있게 최대값을 설정한다
     */
    private void initPosition() {
        DisplayMetrics matrix = new DisplayMetrics();
        int newSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mSize, getResources().getDisplayMetrics());
        mWindowManager.getDefaultDisplay().getMetrics(matrix);        //화면 정보를 가져와서
        GlobalVariable.displayHeightPx=matrix.heightPixels;
        GlobalVariable.displayWidthPx=matrix.widthPixels;

        mPointingStickController.setPxWidth(matrix.widthPixels);
        mPointingStickController.setPxHeight(matrix.heightPixels);
        Log.e("Service", "width: " + matrix.widthPixels + " height: " + matrix.heightPixels);

        //x 최대값 설정
        mPointingStickController.setMAX_X(mPointingStickController.getPxWidth() - pointingStick.getWidth());
        //y 최대값 설정
        mPointingStickController.setMAX_Y(mPointingStickController.getPxHeight() - pointingStick.getHeight());

        mPointingStickController.setCurrntX(mPointingStickController.getPxWidth() / 5);
        mPointingStickController.setCurrntY(mPointingStickController.getPxHeight() / 5);//최초 위치 설정

        mParams.x = mPointingStickController.getCurrntX();
        mParams.y = mPointingStickController.getCurrntY();//x,y 위치 초기화
        mParamsCenter.x=mParams.x;
        mParamsCenter.y=mParams.y;//center point x,y
        Log.e("Service","x: "+mParams.x+" y: "+mParams.y);
        mParams.width = newSize;
        mParams.height = newSize;
        GlobalVariable.stickWidth = mParams.width;
        GlobalVariable.stickHeight = mParams.height;
    }
    /**
     * 가로 / 세로 모드 변경 시 최대값 다시 설정해 주어야 함.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initPosition();
        int tmp;
        tmp=GlobalVariable.displayMaxLeft;
        GlobalVariable.displayMaxLeft=GlobalVariable.displayMaxRight;
        GlobalVariable.displayMaxRight=tmp;
        tmp=GlobalVariable.displayMaxTop;
        GlobalVariable.displayMaxTop=GlobalVariable.displayMaxBottom;
        GlobalVariable.displayMaxBottom=tmp;
    }// 화면 roatate시에 발생

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        virtualMouseDriverController.interrupt();
        if(mWindowManager != null) {		//서비스 종료시 뷰 제거. *중요 : 뷰를 꼭 제거 해야함.
            if(pointingStick != null && !mPointingStickController.getIsOptionMenu()&& !mPointingStickController.isHideMode()) {
                mWindowManager.removeViewImmediate(pointingStick);
                mWindowManager.removeViewImmediate(centerPoint);
            }
            else if(hideImage!=null && mPointingStickController.isHideMode())
                mWindowManager.removeViewImmediate(hideImage);
            else if(mCircleView!=null)
                mWindowManager.removeViewImmediate(mCircleView);
        }
        mWindowManager.removeViewImmediate(optionImage);
        Log.e("service", "onDestroy");
        unregisterReceiver(hideReceiver);
        unregisterReceiver(dispReceiver);
        unregisterReceiver(sizeReceiver);
        unregisterReceiver(progressReceiver);
        removeMouseDriver();
        virtualMouseDriverController=null;
        super.onDestroy();
    }

    private void getPreferencesProgress(){
        SharedPreferences pref = getSharedPreferences("forProgress", MODE_PRIVATE);
        mProgress = pref.getInt("progress", 100);
    }
    private void getPreferencesSize(){
        SharedPreferences pref = getSharedPreferences("forSize", MODE_PRIVATE);
        mSize = pref.getInt("size", 100);
    }
    BroadcastReceiver hideReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                Log.e("Service","hide");
                setStickHide();
        }
    };
    BroadcastReceiver dispReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                Log.e("Service","hide");
                setStickDisplay();
        }
    };
    BroadcastReceiver sizeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int size;
            size=intent.getIntExtra("size", 100);
            Log.e("Service", "setSize:"+size);
            setStickSize(size);
        }
    };
    BroadcastReceiver progressReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int progress;
            progress=intent.getIntExtra("progress", 100);
            Log.e("Service", "setProgress:"+progress);
            setProgress(progress);
        }
    };
    static {
        System.loadLibrary("samdwich_jni");
    }
    public native int initMouseDriver();
    public native void removeMouseDriver();
}
