package org.secmem.gn.ctos.samdwich;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by SECMEM-DY on 2016-02-17.
 */
public class QuestionActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        Log.e("QuestionActivity", "QuestionActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
    }
}
