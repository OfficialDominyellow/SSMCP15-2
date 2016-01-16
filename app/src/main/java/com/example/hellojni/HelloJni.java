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
import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class HelloJni extends Activity
{
    private Switch serviceSwitch;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        serviceSwitch=(Switch)findViewById(R.id.switch1);
        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    on();
                }
                else{
                    off();
                }
            }
        });
    }
    public void on()
    {

        Log.e("service", "startService");
        startService(new Intent(this, PointingStickService.class));    //서비스 시작
    }
    public void off()
    {

        Log.e("service","endService");
        stopService(new Intent(this, PointingStickService.class));	//서비스 종료
    }
}
