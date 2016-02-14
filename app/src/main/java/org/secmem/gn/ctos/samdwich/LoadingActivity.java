package org.secmem.gn.ctos.samdwich;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by SECMEM-DY on 2016-01-16.
 */
public class LoadingActivity extends Activity {
    private ImageView logo;
    private TextView logotxt;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
        logo=(ImageView)findViewById(R.id.logo);
        logotxt=(TextView)findViewById(R.id.logotxt);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        animation.setDuration(1500);
        logo.setAnimation(animation);
        logotxt.setAnimation(animation);
        logo.startAnimation(animation);
        logotxt.startAnimation(animation);
        Handler hd = new Handler();
        hd.postDelayed(new loadingHandler(), 1500);
    }
    private class loadingHandler implements Runnable {
        public void run() {
                startActivity(new Intent(getApplication(), SettingActivity.class));//설정으로 넘어감
                LoadingActivity.this.finish();
        }
    }
}
