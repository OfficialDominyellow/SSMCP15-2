package com.example.chu.jnitest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Context context = getApplicationContext();

        HelloNDK helloNDK = new HelloNDK();
        //Toast.makeText(context, helloNDK.getHelloNDKString(), Toast.LENGTH_LONG).show();

        Toast.makeText(context, ""+helloNDK.setDeviceDriver(), Toast.LENGTH_LONG).show();
    }
}
