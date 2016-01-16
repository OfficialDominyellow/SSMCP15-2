package com.example.hellojni;

/**
 * Created by 김희중 on 2016-01-16.
 */
public class MyMath {
    //좌표간 유클리드 거리 리턴
    public static int getDist(int fx, int fy, int tx, int ty){
        return (tx-fx)*(tx-fx) + (ty-fy)*(ty-fy);
    }

    //좌표간 기울기 리턴
    public static double getTangent(int fx, int fy, int tx, int ty){
        if(tx-fx == 0){
            tx = fx+1;
        }
        return ((double)(ty-fy))/((double)(tx-fx));
    }
}
