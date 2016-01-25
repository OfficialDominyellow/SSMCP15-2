package org.secmem.gn.ctos.samdwich.mouse;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Button;
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
    private WindowManager.LayoutParams mParams; //Layout params객체, 뷰의 위치 크기 지정
    private WindowManager mWindowManager;

    private LayoutInflater mInflater;
    private CircleLayout mCircleView;

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
    public class DataBinder extends Binder {
        public PointingStickService getService(){return PointingStickService.this;}
    }
    DataBinder mBinder=new DataBinder();
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("service", "onBind");return mBinder;
    }
    public void setProgress(int progress) throws RemoteException
    {
        mParams.alpha = progress / 100.0f;			//알파값 설정
        updateView();
    }
    public void setStickSize(int size) throws RemoteException
    {
        int newSize=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());//dp로 변환
        if(mPointingStickController.getIsLongMouseClick())
                newSize*=2;
        mParams.width=newSize;
        mParams.height=newSize;
        updateView();
    }
    public void updateView()
    {
        if(mPointingStickController.getIsLongMouseClick())
            mWindowManager.updateViewLayout(mCircleView,mParams);
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
        getPreferencesProgress();getPreferencesSize();
        mPointingStickController=new PointingStickController();
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        pointingStick = new Button(this);
        pointingStick.setBackgroundResource(R.drawable.pointing_stick);
        pointingStick.setText("●");    //텍스트 설정
        pointingStick.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);                                //텍스트 크기 18sp
        pointingStick.setTextColor(Color.RED);
        //pointingStick.setBackgroundColor(Color.argb(127, 0, 255, 255));								//텍스트뷰 배경 색

        mCircleView = (CircleLayout) mInflater.inflate(R.layout.sample_no_rotation2, null);

        //최상위 윈도우에 넣기 위한 설정
        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,					//항상 최 상위에 있게. status bar 밑에 있음. 터치 이벤트 받을 수 있음.
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE		//이 속성을 안주면 터치 & 키 이벤트도 먹게 된다.
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                |WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                //포커스를 안줘서 자기 영역 밖터치는 인식 안하고 키이벤트를 사용하지 않게 설정
                PixelFormat.TRANSLUCENT);										//투명
        //mParams.gravity = Gravity.LEFT | Gravity.TOP;						//왼쪽 상단에 위치하게 함.
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        initPosition();
        mWindowManager.addView(pointingStick, mParams);		//최상위 윈도우에 뷰 넣기. *중요 : 여기에 permission을 미리 설정해 두어야 한다. 매니페스트에

        virtualMouseDriverController = virtualMouseDriverController.getInstance(getApplicationContext());
        if (virtualMouseDriverController.getState()==Thread.State.NEW) {
            virtualMouseDriverController.start();
            virtualMouseDriverController.onPause();
        }
        setAllListener();
        mParams.alpha = mProgress / 100.0f;			//알파값 설정
        mWindowManager.updateViewLayout(pointingStick, mParams);	//팝업 뷰 업데이트
    }
    public void setAllListener()
    {
        mCircleView.setOnItemClickListener(new CircleViewItemClickListener(mPointingStickController, mParams, mWindowManager, mCircleView, pointingStick,getBaseContext()));
        pointingStick.setOnLongClickListener(new StickLongClickListener(mPointingStickController,mParams,mWindowManager,mCircleView,pointingStick));
        pointingStick.setOnTouchListener(new StickTouchListenenr(mPointingStickController, mParams, mWindowManager, pointingStick,
                this, virtualMouseDriverController));
    }
    /**
     * 뷰의 위치가 화면 안에 있게 최대값을 설정한다
     */
    private void initPosition() {
        DisplayMetrics matrix = new DisplayMetrics();
        int newSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mSize, getResources().getDisplayMetrics());
        mWindowManager.getDefaultDisplay().getMetrics(matrix);        //화면 정보를 가져와서

        mPointingStickController.setPxWidth(matrix.widthPixels);
        mPointingStickController.setPxHeight(matrix.heightPixels);

        //x 최대값 설정
        mPointingStickController.setMAX_X(mPointingStickController.getPxWidth() - pointingStick.getWidth());
        //y 최대값 설정
        mPointingStickController.setMAX_Y(mPointingStickController.getPxHeight() - pointingStick.getHeight());

        mPointingStickController.setPxWidth(mPointingStickController.getPxWidth() / 5);
        mPointingStickController.setPxHeight(mPointingStickController.getPxHeight() / 5);//최초 위치 설정

        mParams.x = mPointingStickController.getPxWidth();
        mParams.y = mPointingStickController.getPxHeight();//x,y 위치 초기화

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
    public void onDestroy() {
        virtualMouseDriverController.interrupt();
        if(mWindowManager != null) {		//서비스 종료시 뷰 제거. *중요 : 뷰를 꼭 제거 해야함.
            if(pointingStick != null && !mPointingStickController.getIsLongMouseClick())
                mWindowManager.removeView(pointingStick);
            else if(mCircleView!=null)
                mWindowManager.removeView(mCircleView);
        }
        Log.e("service", "onDestroy");
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
    static {
        System.loadLibrary("samdwich_jni");
    }
    public native int initMouseDriver();
    public native void removeMouseDriver();
}
