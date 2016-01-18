package com.example.hellojni;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;

import java.util.List;

/**
 * Created by SECMEM-DY on 2016-01-11.
 */
public class MyKeyboardView  extends KeyboardView {

    private Context context;
    public MyKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }
    public MyKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
    }
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        /*
        List<Keyboard.Key> keys=getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
                Log.e("KEY", "Drawing key with code ");
                Drawable dr = (Drawable) context.getResources().getDrawable(R.drawable.test);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);
        }
        */
    }
}