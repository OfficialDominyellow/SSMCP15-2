package org.secmem.gn.ctos.samdwich.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

/**
 * Created by SECMEM-DY on 2016-01-11.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;

import org.secmem.gn.ctos.samdwich.R;

import java.util.List;

/**
 * Created by SECMEM-DY on 2016-01-11.
 */
public class MyKeyboardView  extends KeyboardView {

    private final String TAG = "MyKeyboardView";

    private Context context;

    public MyKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MyKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            if (key.codes[0] == 32) {
                Log.e("KEY", "Drawing key with code " + key.codes[0]);
                Drawable dr = (Drawable) context.getResources().getDrawable(R.drawable.key_trans_space);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);
            } else if (key.codes[0] == 12609) {
                Drawable dr = (Drawable) context.getResources().getDrawable(R.drawable.key_mieum);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);
            } else if (key.codes[0] == 12596) {
                Drawable dr = (Drawable) context.getResources().getDrawable(R.drawable.key_nieun);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);
            } else if (key.codes[0] == 12615) {
                Drawable dr = (Drawable) context.getResources().getDrawable(R.drawable.key_ieung);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);
            } else if (key.codes[0] == 12613) {
                Drawable dr = (Drawable) context.getResources().getDrawable(R.drawable.key_siot);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);
            } else if (key.codes[0] == 12593) {
                Drawable dr = (Drawable) context.getResources().getDrawable(R.drawable.key_giyeok);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);
            } else if (key.codes[0] == 12641) {
                Drawable dr = (Drawable) context.getResources().getDrawable(R.drawable.key_eu);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);
            } else if (key.codes[0] == 12643) {
                Drawable dr = (Drawable) context.getResources().getDrawable(R.drawable.key_e);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);
            } else if (key.codes[0] == 12685) {
                Drawable dr = (Drawable) context.getResources().getDrawable(R.drawable.key_araea);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);
            }
            //del
            else if (key.codes[0] == -5) {
                Drawable dr = (Drawable) context.getResources().getDrawable(R.drawable.key_backspc);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);
            }
            //done
            else if (key.codes[0] == -4) {
                Drawable dr = (Drawable) context.getResources().getDrawable(R.drawable.key_done);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);
            }
            //ret
            else if (key.codes[0] == 10) {
                Drawable dr = (Drawable) context.getResources().getDrawable(R.drawable.key_ret);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);
            }
        }
    }
}
