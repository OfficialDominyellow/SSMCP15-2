//
// Created by 김희중 on 2016-01-25.
//

#ifndef SSMCP15_2_SAMDWICH_DRIVER_H
#define SSMCP15_2_SAMDWICH_DRIVER_H

#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <linux/input.h>
#include <linux/uinput.h>

#define die(str, args...) do { \
        perror(str); \
        exit(EXIT_FAILURE); \
    } while(0)

static int fd;//uinput fd


void Java_org_secmem_gn_ctos_samdwich_mouse_StickTouchListener_clickLeftMouse(JNIEnv * a,jobject b);
void Java_org_secmem_gn_ctos_samdwich_mouse_PointingStickService_removeMouseDriver(JNIEnv* env, jobject thiz);
void Java_org_secmem_gn_ctos_samdwich_mouse_TabGestureListener_inputBackTabKey(JNIEnv * a,jobject b);
void Java_org_secmem_gn_ctos_samdwich_global_VirtualMouseDriverController_moveMouse(JNIEnv * a,jobject b,jint x,jint y);
jint Java_org_secmem_gn_ctos_samdwich_mouse_PointingStickService_initMouseDriver(JNIEnv* env, jobject thiz);

//KEYBOARD
void Java_org_secmem_gn_ctos_samdwich_mouse_TabGestureListener_inputTabKey(JNIEnv * a,jobject b);
void Java_org_secmem_gn_ctos_samdwich_mouse_TabGestureListener_inputEnterKey(JNIEnv * a,jobject b);


#endif //SSMCP15_2_SAMDWICH_DRIVER_H
