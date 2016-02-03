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
    private RadioGroup mGroup;
    private RadioButton size1,size2,size3;
    private boolean switchValue;
    private int mProgress;
    private int mSize;
    private IntentFilter filter;
    private Intent intent;
    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getPreferencesSwitch();getPreferencesProgress();getPreferencesSize();

        filter = new IntentFilter();
        filter.addAction(GlobalVariable.STOP_SERVICE);
        intent =new Intent(this,PointingStickService.class);

        mSeekBar=(SeekBar)findViewById(R.id.seekBar);
        serviceSwitch=(Switch)findViewById(R.id.switch1);
        mGroup=(RadioGroup)findViewById(R.id.sizeGroup);
        size1=(RadioButton)findViewById(R.id.option1);
        size2=(RadioButton)findViewById(R.id.option2);
        size3=(RadioButton)findViewById(R.id.option3);

        if(mSize==70)
            size1.setChecked(true);
        else if(mSize==100)
            size2.setChecked(true);
        else
            size3.setChecked(true);

        initActivityOption();

        if(switchValue)
            serviceSwitch.setChecked(true);
        else
            serviceSwitch.setChecked(false);
    }

    public void on() {
        Log.e("service", "startService");
        filter.addAction(GlobalVariable.STOP_SERVICE);
        registerReceiver(switchReceiver, filter);
        startService(intent);    //서비스 시작
    }
    public void off() {
        Log.e("service", "endService");
        unregisterReceiver(switchReceiver);
        stopService(new Intent(this, PointingStickService.class));	//서비스 종료
    }
    private void initActivityOption()
    {
        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchValue = true;
                    savePreferencesSwitch();
                    setVisible();
                    on();
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
                Intent intent = new Intent();
                intent.putExtra("progress", progress);
                intent.setAction(GlobalVariable.CHANGE_PROG);
                sendBroadcast(intent);
                mProgress = progress;
                savePreferencesProgress();
            }
        });

        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.option1:
                        mSize = 70;
                        break;
                    case R.id.option2:
                        mSize = 100;
                        break;
                    case R.id.option3:
                        mSize = 120;
                        break;
                }
                Intent intent = new Intent();
                intent.putExtra("size", mSize);
                intent.setAction(GlobalVariable.CHANGE_SIZE);
                sendBroadcast(intent);
                savePreferencesSize();
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
    public void onPause()
    {
        super.onPause();
    }
    public void setVisible()
    {
        mSeekBar.setVisibility(View.VISIBLE);
        mGroup.setVisibility(View.VISIBLE);
    }
    public void setInVisible()
    {
        mSeekBar.setVisibility(View.INVISIBLE);
        mGroup.setVisibility(View.INVISIBLE);
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
