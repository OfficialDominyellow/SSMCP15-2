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
#include <unistd.h>
#include <fcntl.h>
#include <linux/input.h>
#include <linux/uinput.h>

#define die(str, args...) do { \
        perror(str); \
        exit(EXIT_FAILURE); \
    } while(0)

int fd;
int dx,dy;
void Java_com_example_hellojni_StickTouchListenenr_clickLeftMouse(JNIEnv * a,jobject b)
{
    struct input_event evMouseLeftKey;
    memset(&evMouseLeftKey, 0, sizeof(struct input_event));
    evMouseLeftKey.type = EV_KEY;
    evMouseLeftKey.code = BTN_LEFT;
    evMouseLeftKey.value = 1;
    if(write(fd, &evMouseLeftKey, sizeof(struct input_event)) < 0)
        die("error: write");

    memset(&evMouseLeftKey, 0, sizeof(struct input_event));
    evMouseLeftKey.type = EV_SYN;
    evMouseLeftKey.code = 0;
    evMouseLeftKey.value = 0;
    if(write(fd, &evMouseLeftKey, sizeof(struct input_event)) < 0)
        die("error: write");

    memset(&evMouseLeftKey, 0, sizeof(struct input_event));
    evMouseLeftKey.type = EV_KEY;
    evMouseLeftKey.code = BTN_LEFT;
    evMouseLeftKey.value = 0;
    if(write(fd, &evMouseLeftKey, sizeof(struct input_event)) < 0)
        die("error: write");

    memset(&evMouseLeftKey, 0, sizeof(struct input_event));
    evMouseLeftKey.type = EV_SYN;
    evMouseLeftKey.code = 0;
    evMouseLeftKey.value = 0;
    if(write(fd, &evMouseLeftKey, sizeof(struct input_event)) < 0)
        die("error: write");
}
void Java_com_example_hellojni_PointingStickService_removeMouseDriver(JNIEnv* env, jobject thiz)
{
    if(ioctl(fd, UI_DEV_DESTROY) < 0)
        die("error: ioctl");
    close(fd);
}
void Java_com_example_hellojni_VirtualMouseDriverController_moveMouse(JNIEnv * a,jobject b,jint x,jint y)
{
    struct input_event ev;
    int dx,dy;
    int i,j;
    dx=x,dy=y;

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

}
jint Java_com_example_hellojni_PointingStickService_initMouseDriver(JNIEnv* env, jobject thiz)
{
    struct uinput_user_dev uidev;

    int i,j;
    fd = open("/dev/uinput", O_WRONLY | O_NONBLOCK);
    if(fd < 0) {
        fd=open("/dev/input/uinput",O_WRONLY|O_NONBLOCK);
        if(fd<0){
            die("error: open");
            return -1;
        }
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
    snprintf(uidev.name, UINPUT_MAX_NAME_SIZE, "uinput-virtualMouse");
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

