package org.secmem.gn.ctos.samdwich;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by SECMEM-DY on 2016-02-17.
 */
public class InfoActivity extends Activity {
    TextView headTextView;
    TextView contentTextView;
    public void onCreate(Bundle savedInstanceState) {
        Log.e("InfoActivity", "InfoActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        headTextView=(TextView)findViewById(R.id.headTextView);
        contentTextView=(TextView)findViewById(R.id.contentTextView);
    }
}
