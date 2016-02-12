package org.secmem.gn.ctos.samdwich.mouse;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by SECMEM-DY on 2016-02-11.
 */
public class MotionListener implements View.OnTouchListener {
    private GestureDetector mDetector;
    private MotionGestureListener mGestureListener;
    public MotionListener(PointingStickController mPointingStickController)
    {
        mGestureListener=new MotionGestureListener(mPointingStickController);
        mDetector=new GestureDetector(mPointingStickController.getmContext(),mGestureListener);
    }
    @Override
       public boolean onTouch(View v, MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }
}
