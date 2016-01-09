package com.example.hellojni;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.example.hellojni.R;

/**
 * Created by 김희중 on 2016-01-08.
 */
public class MyKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

    private KeyboardView kv;
    private Keyboard keyboard;

    private boolean caps = false;

    @Override
    public View onCreateInputView(){
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);

        Log.e("Keyboard","onCreate");

        return kv;
    }

    private void playClick(int keyCode){
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD );
        }
    }

    @Override
    public void onPress(int primaryCode) {
        Log.e("Keyboard","onPress");

    }

    @Override
    public void onRelease(int primaryCode) {
        Log.e("Keyboard","onRelease");
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Log.e("Keyboard","onKey");
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char) primaryCode;
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code), 1);
        }
    }

    @Override
    public void onText(CharSequence text) {
        Log.e("Keyboard","onText");
    }

    @Override
    public void swipeLeft() {
        Log.e("Keyboard","swipeLeft");
    }

    @Override
    public void swipeRight() {
        Log.e("Keyboard","swipeRight");
    }

    @Override
    public void swipeDown() {
        Log.e("Keyboard","swipeDown");
    }

    @Override
    public void swipeUp() {
        Log.e("Keyboard","swipeUp");
    }
}
