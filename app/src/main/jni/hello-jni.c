/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <jni.h>
/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <linux/input.h>
#include <linux/uinput.h>
#include <time.h>

#define die(str, args...) do { \
        perror(str); \
        exit(EXIT_FAILURE); \
    } while(0)

int fd;
struct uinput_user_dev uidev;
struct input_event ev;
int dx,dy;
JNIEXPORT void Java_com_example_hellojni_PointingStickService_removeMouseDriver(JNIEnv* env, jobject thiz)
{
    if(ioctl(fd, UI_DEV_DESTROY) < 0)
        die("error: ioctl");
    close(fd);
}
jint Java_com_example_hellojni_PointingStickService_moveMouse(JNIEnv * a,jobject b,jint x,jint y)
{
    int dx,dy;
    int i,j;
    dx=x,dy=y;

    //for(i = 0; i < 20; i++) {
        memset(&ev, 0, sizeof(struct input_event));
        ev.type = EV_REL;
        ev.code = REL_X;
        ev.value = dx;
        if(write(fd, &ev, sizeof(struct input_event)) < 0)
            die("error: write");

        memset(&ev, 0, sizeof(struct input_event));
        ev.type = EV_REL;
        ev.code = REL_Y;
        ev.value = dy;
        if(write(fd, &ev, sizeof(struct input_event)) < 0)
            die("error: write");

        memset(&ev, 0, sizeof(struct input_event));
        ev.type = EV_SYN;
        ev.code = 0;
        ev.value = 0;
        if(write(fd, &ev, sizeof(struct input_event)) < 0)
            die("error: write");

     //   usleep(15000);
   // }
    return 0;
}
jint Java_com_example_hellojni_PointingStickService_initMouseDriver(JNIEnv* env, jobject thiz)
{
    int i,j;
    fd = open("/dev/uinput", O_WRONLY | O_NONBLOCK);
    if(fd < 0) {
        die("error: open");
        return -1;
    }
    if(ioctl(fd, UI_SET_EVBIT, EV_KEY) < 0) {
        die("error: ioctl");
        return -1;
    }
    if(ioctl(fd, UI_SET_KEYBIT, BTN_LEFT) < 0) {
        die("error: ioctl");
        return -1;
    }

    if(ioctl(fd, UI_SET_EVBIT, EV_REL) < 0) {
        die("error: ioctl");
        return -1;
    }
    if(ioctl(fd, UI_SET_RELBIT, REL_X) < 0) {
        die("error: ioctl");
        return -1;
    }
    if(ioctl(fd, UI_SET_RELBIT, REL_Y) < 0) {
        die("error: ioctl");
        return -1;
    }

    memset(&uidev, 0, sizeof(uidev));
    snprintf(uidev.name, UINPUT_MAX_NAME_SIZE, "uinput-sample");
    uidev.id.bustype = BUS_USB;
    uidev.id.vendor  = 0x1;
    uidev.id.product = 0x1;
    uidev.id.version = 1;

    if(write(fd, &uidev, sizeof(uidev)) < 0) {
        die("error: write");
        return -1;
    }
    if(ioctl(fd, UI_DEV_CREATE) < 0) {
        die("error: ioctl");
        return -1;
    }
    return 1;//success
}

jstring Java_com_example_hellojni_HelloJni_stringFromJNI( JNIEnv* env, jobject thiz )
{
#if defined(__arm__)
  #if defined(__ARM_ARCH_7A__)
    #if defined(__ARM_NEON__)
      #if defined(__ARM_PCS_VFP)
        #define ABI "armeabi-v7a/NEON (hard-float)"
      #else
        #define ABI "armeabi-v7a/NEON"
      #endif
    #else
      #if defined(__ARM_PCS_VFP)
        #define ABI "armeabi-v7a (hard-float)"
      #else
        #define ABI "armeabi-v7a"
      #endif
    #endif
  #else
   #define ABI "armeabi"
  #endif
#elif defined(__i386__)
   #define ABI "x86"
#elif defined(__x86_64__)
   #define ABI "x86_64"
#elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
   #define ABI "mips64"
#elif defined(__mips__)
   #define ABI "mips"
#elif defined(__aarch64__)
   #define ABI "arm64-v8a"
#else
   #define ABI "unknown"
#endif
    int fd;
    struct uinput_user_dev uidev;
    struct input_event ev;
    int dx,dy;
    int i,j;
    fd = open("/dev/uinput", O_WRONLY | O_NONBLOCK);
    if(fd < 0)
        die("error: open");

    if(ioctl(fd, UI_SET_EVBIT, EV_KEY) < 0)
        die("error: ioctl");
    if(ioctl(fd, UI_SET_KEYBIT, BTN_LEFT) < 0)
        die("error: ioctl");

    if(ioctl(fd, UI_SET_EVBIT, EV_REL) < 0)
        die("error: ioctl");
    if(ioctl(fd, UI_SET_RELBIT, REL_X) < 0)
        die("error: ioctl");
    if(ioctl(fd, UI_SET_RELBIT, REL_Y) < 0)
        die("error: ioctl");

    memset(&uidev, 0, sizeof(uidev));
    snprintf(uidev.name, UINPUT_MAX_NAME_SIZE, "uinput-sample");
    uidev.id.bustype = BUS_USB;
    uidev.id.vendor  = 0x1;
    uidev.id.product = 0x1;
    uidev.id.version = 1;

    if(write(fd, &uidev, sizeof(uidev)) < 0)
        die("error: write");

    if(ioctl(fd, UI_DEV_CREATE) < 0)
        die("error: ioctl");

    //sleep(2);

   //srand(time(NULL));

    while(1) {
        for(j=0;j<4;j++)
        switch(j) {
            case 0:
                dx = -10;
                dy = -1;
                break;
            case 1:
                dx = 10;
                dy = 1;
                break;
            case 2:
                dx = -1;
                dy = 10;
                break;
            case 3:
                dx = 1;
                dy = -10;
                break;
        }

        for(i = 0; i < 20; i++) {
            memset(&ev, 0, sizeof(struct input_event));
            ev.type = EV_REL;
            ev.code = REL_X;
            ev.value = dx;
            if(write(fd, &ev, sizeof(struct input_event)) < 0)
                die("error: write");

            memset(&ev, 0, sizeof(struct input_event));
            ev.type = EV_REL;
            ev.code = REL_Y;
            ev.value = dy;
            if(write(fd, &ev, sizeof(struct input_event)) < 0)
                die("error: write");

            memset(&ev, 0, sizeof(struct input_event));
            ev.type = EV_SYN;
            ev.code = 0;
            ev.value = 0;
            if(write(fd, &ev, sizeof(struct input_event)) < 0)
                die("error: write");

            usleep(15000);
        }

        sleep(5);
    }

    sleep(2);

    if(ioctl(fd, UI_DEV_DESTROY) < 0)
        die("error: ioctl");

    close(fd);

    return (*env)->NewStringUTF(env, "Hello from JNI !  Compiled with ABI " ABI ".");
}
