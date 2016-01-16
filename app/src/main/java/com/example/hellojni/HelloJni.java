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
package com.example.hellojni;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

public class HelloJni extends Activity
{
    private Switch serviceSwitch;
    private SeekBar mSeekBar;
    private PointingStickService mPointingStickService;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mSeekBar=(SeekBar)findViewById(R.id.seekBar);

        serviceSwitch=(Switch)findViewById(R.id.switch1);
        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSeekBar.setVisibility(View.VISIBLE);
                    on();
                } else {
                    mSeekBar.setVisibility(View.INVISIBLE);
                    off();
                }
            }
        });

        mSeekBar.setMax(100);					//맥스 값 설정.
        mSeekBar.setProgress(100);			//현재 투명도 설정. 100:불투명, 0은 완전 투명
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    mPointingStickService.setProgress(progress);//seekbar변경 정보를 포인팅스틱에 전달
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        mSeekBar.setVisibility(View.INVISIBLE);
    }
    public void on()
    {
        Log.e("service", "startService");
        Intent intent =new Intent(this,PointingStickService.class);
        bindService(intent,srvConn,BIND_AUTO_CREATE);
        startService(intent);    //서비스 시작
    }

    public void off()
    {
        Log.e("service","endService");
        unbindService(srvConn);
        stopService(new Intent(this, PointingStickService.class));	//서비스 종료
    }
    ServiceConnection srvConn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mPointingStickService=((PointingStickService.SeekBarBinder)binder).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
}
