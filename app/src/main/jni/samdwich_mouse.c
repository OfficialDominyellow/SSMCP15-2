//
// Created by 김희중 on 2016-01-25.
//

#include "samdwich_driver.h"

void Java_org_secmem_gn_ctos_samdwich_mouse_StickTouchListener_clickLeftMouse(JNIEnv * a,jobject b)
{
    struct input_event evMouseLeftKey;
    memset(&evMouseLeftKey, 0, sizeof(struct input_event));
    evMouseLeftKey.type = EV_KEY;
    evMouseLeftKey.code = BTN_LEFT;
    evMouseLeftKey.value = 1;//push
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
    evMouseLeftKey.value = 0;//realeased
    if(write(fd, &evMouseLeftKey, sizeof(struct input_event)) < 0)
        die("error: write");

    memset(&evMouseLeftKey, 0, sizeof(struct input_event));
    evMouseLeftKey.type = EV_SYN;
    evMouseLeftKey.code = 0;
    evMouseLeftKey.value = 0;
    if(write(fd, &evMouseLeftKey, sizeof(struct input_event)) < 0)
        die("error: write");
}
void Java_org_secmem_gn_ctos_samdwich_mouse_PointingStickService_removeMouseDriver(JNIEnv* env, jobject thiz)
{
    if(ioctl(fd, UI_DEV_DESTROY) < 0)
        die("error: ioctl");
    close(fd);
}
void Java_org_secmem_gn_ctos_samdwich_global_VirtualMouseDriverController_moveMouse(JNIEnv * a,jobject b,jint x,jint y)
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
jint Java_org_secmem_gn_ctos_samdwich_mouse_PointingStickService_initMouseDriver(JNIEnv* env, jobject thiz)
{
    struct uinput_user_dev uidev;
    struct input_event ev;
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
    if(ioctl(fd, UI_SET_KEYBIT, KEY_TAB) < 0) {
        die("error: ioctl");
        return -1;
    }
    if(ioctl(fd, UI_SET_KEYBIT, KEY_ENTER) < 0) {
        die("error: ioctl");
        return -1;
    }
    if(ioctl(fd, UI_SET_KEYBIT, KEY_LEFTSHIFT) < 0) {
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

    int dx=1,dy=1;

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

    return 1;//success
}

//KEYBOARD
void Java_org_secmem_gn_ctos_samdwich_mouse_TabGestureListener_inputTabKey(JNIEnv * a,jobject b)
{
    struct input_event tabEV;

    memset(&tabEV,0,sizeof(struct input_event));
    tabEV.type = EV_KEY;
    tabEV.code=KEY_TAB;
    tabEV.value=1;
    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
        die("error: write");

    memset(&tabEV, 0, sizeof(struct input_event));
    tabEV.type = EV_SYN;
    tabEV.code = 0;
    tabEV.value = 0;
    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
        die("error: write");

    memset(&tabEV,0,sizeof(struct input_event));
    tabEV.type = EV_KEY;
    tabEV.code=KEY_TAB;
    tabEV.value=0;
    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
        die("error: write");

    memset(&tabEV, 0, sizeof(struct input_event));
    tabEV.type = EV_SYN;
    tabEV.code = 0;
    tabEV.value = 0;
    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
        die("error: write");
}

void Java_org_secmem_gn_ctos_samdwich_mouse_TabGestureListener_inputBackTabKey(JNIEnv * a,jobject b)
{
    struct input_event tabEV;

    memset(&tabEV,0,sizeof(struct input_event));
    tabEV.type = EV_KEY;
    tabEV.code=KEY_LEFTSHIFT;
    tabEV.value=1;
    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
        die("error: write");
    memset(&tabEV, 0, sizeof(struct input_event));
    tabEV.type = EV_SYN;
    tabEV.code = 0;
    tabEV.value = 0;
    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
        die("error: write");


    memset(&tabEV,0,sizeof(struct input_event));
    tabEV.type = EV_KEY;
    tabEV.code=KEY_TAB;
    tabEV.value=1;
    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
        die("error: write");

    memset(&tabEV, 0, sizeof(struct input_event));
    tabEV.type = EV_SYN;
    tabEV.code = 0;
    tabEV.value = 0;
    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
        die("error: write");

    memset(&tabEV,0,sizeof(struct input_event));
    tabEV.type = EV_KEY;
    tabEV.code=KEY_TAB;
    tabEV.value=0;
    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
        die("error: write");

    memset(&tabEV, 0, sizeof(struct input_event));
    tabEV.type = EV_SYN;
    tabEV.code = 0;
    tabEV.value = 0;
    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
        die("error: write");


    memset(&tabEV,0,sizeof(struct input_event));
    tabEV.type = EV_KEY;
    tabEV.code=KEY_LEFTSHIFT;
    tabEV.value=0;
    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
        die("error: write");
    memset(&tabEV, 0, sizeof(struct input_event));
    tabEV.type = EV_SYN;
    tabEV.code = 0;
    tabEV.value = 0;
    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
        die("error: write");
}

void Java_org_secmem_gn_ctos_samdwich_mouse_TabGestureListener_inputEnterKey(JNIEnv * a,jobject b)
{
    struct input_event tabEV;
    memset(&tabEV,0,sizeof(struct input_event));
    tabEV.type = EV_KEY;
    tabEV.code=KEY_ENTER;
    tabEV.value=1;
    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
        die("error: write");

    memset(&tabEV, 0, sizeof(struct input_event));
    tabEV.type = EV_SYN;
    tabEV.code = 0;
    tabEV.value = 0;
    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
        die("error: write");

    memset(&tabEV,0,sizeof(struct input_event));
    tabEV.type = EV_KEY;
    tabEV.code=KEY_ENTER;
    tabEV.value=0;
    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
        die("error: write");

    memset(&tabEV, 0, sizeof(struct input_event));
    tabEV.type = EV_SYN;
    tabEV.code = 0;
    tabEV.value = 0;
    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
        die("error: write");
}