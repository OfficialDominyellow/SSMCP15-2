package org.secmem.gn.ctos.samdwich;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


/**
 * Created by SECMEM-DY on 2016-01-16.
 */
public class LoadingActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
        Handler hd = new Handler();
        hd.postDelayed(new loadingHandler(), 2000);
    }
    private class loadingHandler implements Runnable {
        public void run() {
                startActivity(new Intent(getApplication(), SettingActivity.class));//설정으로 넘어감
                LoadingActivity.this.finish();
        }
    }
}
