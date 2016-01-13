package com.example.hellojni;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;


/**
 * Created by 김희중 on 2016-01-08.
 */
public class MyKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

    private final String TAG = "MyKeyboard";

    private MyKeyboardView kv;
    //private KeyboardView kv;
    private Keyboard keyboard;
    private boolean caps = false;

    private double xPositionStart;
    private double yPositionStart;

    private double xPositionEnd;
    private double yPositionEnd;

    private int downKeycode;

    private static Intent mService;

    @Override
    public View onCreateInputView(){
        kv=(MyKeyboardView)getLayoutInflater().inflate(R.layout.customkeyboard, null);

        keyboard = new Keyboard(this, R.xml.qwerty);

        kv.setKeyboard(keyboard);
        kv.setPopupOffset(250, 150);

        kv.setPreviewEnabled(false);

        kv.setOnKeyboardActionListener(this);
        mService = new Intent(this, KeyboardPopupService.class);

        kv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    xPositionStart = event.getX();
                    yPositionStart = event.getY();
                    //Toast.makeText(getApplicationContext(), "X start : " + xPositionStart + " Y start : " + yPositionStart, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "DOWN");
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    xPositionEnd = event.getX();
                    yPositionEnd = event.getY();
                    //Toast.makeText(getApplicationContext(), "X END : " + xPositionEnd + " Y END : " + yPositionEnd, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "UP");
                }
                else if(event.getAction()==MotionEvent.ACTION_MOVE)
                {
                    Log.i(TAG, "MOVE");
                }
                return false;
            }


        });
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
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyLongPress start : " + keyCode);

        switch(keyCode){
            case 32:
                Toast.makeText(getApplicationContext(), "space Long click", Toast.LENGTH_SHORT).show();
                break;
            default:

        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onPress(int primaryCode) {
        Log.i(TAG, "onPress start : " + primaryCode);
        downKeycode = primaryCode;
        kv.setPopupOffset(-(int) xPositionStart, -(int) yPositionStart);

        Log.i(TAG, "POPUP");
        popupStart(primaryCode);

    }

    /*
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyUp start : " + keyCode);
        return super.onKeyUp(keyCode, event);
    }
    */

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        popupEnd();

        primaryCode = downKeycode;
        Log.i(TAG, "onKey start : " + primaryCode);

        //primaryCode + 시작좌표 마지막좌표 판단해서 left right up down cen 판단해서 알맞는 키 입력되도록


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

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    public void popupStart(int primaryCode){

        //Intent service = new Intent(this, KeyboardPopupService.class);
        mService.putExtra("primaryCode", primaryCode+"");
        startService(mService);
    }

    public void popupEnd(){
        //Intent service = new Intent(this, KeyboardPopupService.class);
        stopService(mService);
    }

}
