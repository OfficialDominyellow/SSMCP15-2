package com.example.chu.jnitest;

/**
 * Created by chu on 12/21/15.
 */
public class HelloNDK {
    static {
        System.loadLibrary("hello");
    }
    public native String getHelloNDKString();
    public native int setDeviceDriver();
}