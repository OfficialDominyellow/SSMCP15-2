/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.secmem.gn.ctos.samdwich;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;

import org.secmem.gn.ctos.samdwich.global.GlobalVariable;
import org.secmem.gn.ctos.samdwich.mouse.PointingStickService;


public class SettingActivity extends Activity
{
    private Switch serviceSwitch;
    private SeekBar mSeekBar;
    private RadioGroup mGroupSize;
    private RadioButton size1,size2,size3;
    private RadioGroup mGrouphand;
    private RadioButton leftHand,rightHand;
    private Button questionBTN,infoBTN;

    private boolean switchValue;
    private int mProgress;
    private int mSize;
    private int handValue;
    private IntentFilter filter;
    private Intent intent;
    private Intent receiverIntent;
    private boolean flag=false;
    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.e("service", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getPreferencesSwitch();getPreferencesProgress();getPreferencesSize();getPreferenceshandedness();

        filter = new IntentFilter();
        filter.addAction(GlobalVariable.STOP_SERVICE);
        intent =new Intent(this,PointingStickService.class);

        receiverIntent=new Intent();

        mSeekBar=(SeekBar)findViewById(R.id.seekBar);
        serviceSwitch=(Switch)findViewById(R.id.switch1);
        mGroupSize=(RadioGroup)findViewById(R.id.sizeGroup);
        size1=(RadioButton)findViewById(R.id.option1);
        size2=(RadioButton)findViewById(R.id.option2);
        size3=(RadioButton)findViewById(R.id.option3);
        mGrouphand=(RadioGroup)findViewById(R.id.handednessGroup);
        leftHand=(RadioButton)findViewById(R.id.left);
        rightHand=(RadioButton)findViewById(R.id.right);
        questionBTN=(Button)findViewById(R.id.questionBTN);
        infoBTN=(Button)findViewById(R.id.infoBTN);

        if(mSize==GlobalVariable.size1)
            size1.setChecked(true);
        else if(mSize==GlobalVariable.size2)
            size2.setChecked(true);
        else
            size3.setChecked(true);

        initActivityOption();

        if(switchValue)
            serviceSwitch.setChecked(true);
        else
            serviceSwitch.setChecked(false);

        if(handValue==0)
            rightHand.setChecked(true);
        else
            leftHand.setChecked(true);
    }

    public void on() {
        Log.e("service", "startService");
        filter.addAction(GlobalVariable.STOP_SERVICE);
        if(!flag) {
            Log.e("service", "registerReceiver");
            registerReceiver(switchReceiver, filter);
            flag = true;
        }
        startService(intent);    //서비스 시작
    }
    public void off() {
        Log.e("service", "endService");
        if(flag){
            Log.e("service", "unregisterReceiver");
            unregisterReceiver(switchReceiver);
            flag=false;
        }
        stopService(new Intent(this, PointingStickService.class));	//서비스 종료
    }
    public void onPause()
    {
        super.onPause();
        Log.e("service", "onPause");
    }
    public void onResume()
    {
        super.onResume();
        if(!flag) {
            registerReceiver(switchReceiver, filter);
            flag = true;
        }
        Log.e("service", "onResume");
    }
    public void onStop()
    {
        super.onStop();
        Log.e("service", "onStop");
    }
    public void onDestroy()
    {
        super.onDestroy();
        Log.e("service", "onDestroy");
        unregisterReceiver(switchReceiver);
    }

    private void initActivityOption()
    {
        questionBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,QuestionActivity.class));
            }
        });
        infoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,InfoActivity.class));
            }
        });

        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchValue = true;
                    savePreferencesSwitch();
                    setVisible();
                    on();
                    Log.e("Service", "On");
                } else {
                    switchValue = false;
                    savePreferencesSwitch();
                    setInVisible();
                    off();
                }
            }
        });
        mSeekBar.setMax(100);					//맥스 값 설정.
        mSeekBar.setProgress(mProgress);			//현재 투명도 설정. 100:불투명, 0은 완전 투명
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                receiverIntent.putExtra("progress", progress);
                receiverIntent.setAction(GlobalVariable.CHANGE_PROG);
                sendBroadcast(receiverIntent);
                mProgress = progress;
                savePreferencesProgress();
            }
        });

        mGroupSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.option1:
                        mSize = GlobalVariable.size1;
                        break;
                    case R.id.option2:
                        mSize = GlobalVariable.size2;
                        break;
                    case R.id.option3:
                        mSize = GlobalVariable.size3;
                        break;
                }
                receiverIntent.putExtra("size", mSize);
                receiverIntent.setAction(GlobalVariable.CHANGE_SIZE);
                sendBroadcast(receiverIntent);
                savePreferencesSize();
            }
        });
        mGrouphand.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.left:
                        //to do
                        handValue=1;
                        break;
                    case R.id.right:
                        //to do
                        handValue=0;
                        break;
                }
                savePreferenceshandedness();
            }
        });

        setInVisible();
        //디바이스 화면 값
        Display display=((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int dipWidth=display.getWidth();
        int dipHeight=display.getHeight();
        GlobalVariable.displayMaxLeft=-(dipWidth/2)+(dipWidth*3)/10;
        GlobalVariable.displayMaxRight=(dipWidth/2)-(dipWidth*3)/10;
        GlobalVariable.displayMaxTop=-(dipHeight/2)+(dipHeight*2)/10;
        GlobalVariable.displayMaxBottom=(dipHeight/2)-(dipHeight*2)/10;//20%마진을 줌

        Log.e("Move","Size left:"+ GlobalVariable.displayMaxLeft+"  right:"+ GlobalVariable.displayMaxRight);
        Log.e("Move","Size top:"+ GlobalVariable.displayMaxTop+"  bottmom:"+ GlobalVariable.displayMaxBottom);
    }
    public void setVisible()
    {
        mSeekBar.setVisibility(View.VISIBLE);
        mGroupSize.setVisibility(View.VISIBLE);
    }
    public void setInVisible()
    {
        mSeekBar.setVisibility(View.INVISIBLE);
        mGroupSize.setVisibility(View.INVISIBLE);
    }
    // 값 불러오기
    private void getPreferencesSwitch(){
        SharedPreferences pref = getSharedPreferences("forSwitch", MODE_PRIVATE);
        switchValue=pref.getBoolean("switchMode", false);
    }
    // 값 저장하기
    private void savePreferencesSwitch(){
        SharedPreferences pref = getSharedPreferences("forSwitch", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("switchMode", switchValue);
        editor.commit();
    }
    private void getPreferencesProgress(){
        SharedPreferences pref = getSharedPreferences("forProgress", MODE_PRIVATE);
        mProgress = pref.getInt("progress", 100);
    }
    private void savePreferencesProgress(){
        SharedPreferences pref = getSharedPreferences("forProgress", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("progress", mProgress);
        editor.commit();
    }
    private void getPreferencesSize(){
        SharedPreferences pref = getSharedPreferences("forSize", MODE_PRIVATE);
        mSize = pref.getInt("size", 100);
    }
    private void savePreferencesSize(){
        SharedPreferences pref = getSharedPreferences("forSize", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("size", mSize);
        editor.commit();
    }
    private void getPreferenceshandedness(){
        SharedPreferences pref = getSharedPreferences("forHandedness", MODE_PRIVATE);
        handValue = pref.getInt("hand", 0);//default right
    }
    private void savePreferenceshandedness(){
        SharedPreferences pref = getSharedPreferences("forHandedness", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("hand", handValue);
        editor.commit();
    }
    BroadcastReceiver switchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            serviceSwitch.performClick();
            switchValue = false;
            savePreferencesSwitch();
            setInVisible();
            off();
        }
    };
}
