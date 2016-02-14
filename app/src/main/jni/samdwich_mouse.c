//
// Created by 김희중 on 2016-01-25.
//

#include "samdwich_driver.h"
void setEventAndWrite(__u16 type, __u16 code, __s32 value)
{
    ev.type=type;
    ev.code=code;
    ev.value=value;
    if(write(fd, &ev, sizeof(struct input_event)) < 0)
        die("error: write");
}
void Java_org_secmem_gn_ctos_samdwich_mouse_PointingStickService_removeMouseDriver(JNIEnv* env, jobject thiz)
{
    if(ioctl(fd, UI_DEV_DESTROY) < 0)
        die("error: ioctl");
    close(fd);
}
void Java_org_secmem_gn_ctos_samdwich_mouse_StickTouchListener_clickLeftMouse(JNIEnv * a,jobject b)
{
    setEventAndWrite(EV_KEY,BTN_LEFT,1);
    setEventAndWrite(EV_SYN,0,0);
    setEventAndWrite(EV_KEY,BTN_LEFT,0);
    setEventAndWrite(EV_SYN,0,0);
}
void Java_org_secmem_gn_ctos_samdwich_mouse_StickLongClickListener_downLeftMouse(JNIEnv * a,jobject b)
{
    setEventAndWrite(EV_KEY,BTN_LEFT,1);
    setEventAndWrite(EV_SYN,0,0);
}
void Java_org_secmem_gn_ctos_samdwich_mouse_StickLongClickListener_upLeftMouse(JNIEnv * a,jobject b)
{
    setEventAndWrite(EV_KEY,BTN_LEFT,0);
    setEventAndWrite(EV_SYN,0,0);
}
void Java_org_secmem_gn_ctos_samdwich_global_VirtualMouseDriverController_moveMouse(JNIEnv * a,jobject b,jint x,jint y)
{
    int dx=x,dy=y;
    setEventAndWrite(EV_REL,REL_X,dx);
    setEventAndWrite(EV_REL,REL_Y,dy);
    setEventAndWrite(EV_SYN,0,0);
}
jint Java_org_secmem_gn_ctos_samdwich_mouse_PointingStickService_initMouseDriver(JNIEnv* env, jobject thiz)
{
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

    memset(&ev, 0, sizeof(struct input_event));
    setEventAndWrite(EV_REL,REL_X,1);
    setEventAndWrite(EV_REL,REL_Y,1);
    setEventAndWrite(EV_SYN,0,0);

    return 1;//success
}
//KEYBOARD
void Java_org_secmem_gn_ctos_samdwich_mouse_TabGestureListener_inputTabKey(JNIEnv * a,jobject b)
{
    setEventAndWrite(EV_KEY,KEY_TAB,1);
    setEventAndWrite(EV_SYN,0,0);

    setEventAndWrite(EV_KEY,KEY_TAB,0);
    setEventAndWrite(EV_SYN,0,0);
}

void Java_org_secmem_gn_ctos_samdwich_mouse_TabGestureListener_inputBackTabKey(JNIEnv * a,jobject b)
{
    setEventAndWrite(EV_KEY,KEY_LEFTSHIFT,1);
    setEventAndWrite(EV_SYN,0,0);

    setEventAndWrite(EV_KEY,KEY_TAB,1);
    setEventAndWrite(EV_SYN,0,1);

    setEventAndWrite(EV_KEY,KEY_TAB,0);
    setEventAndWrite(EV_SYN,0,0);

    setEventAndWrite(EV_KEY,KEY_LEFTSHIFT,0);
    setEventAndWrite(EV_SYN,0,0);
}
void Java_org_secmem_gn_ctos_samdwich_mouse_TabGestureListener_inputEnterKey(JNIEnv * a,jobject b)
{
    setEventAndWrite(EV_KEY,KEY_ENTER,1);
    setEventAndWrite(EV_SYN,0,1);

    setEventAndWrite(EV_KEY,KEY_ENTER,0);
    setEventAndWrite(EV_SYN,0,0);
}
void Java_org_secmem_gn_ctos_samdwich_mouse_TabGestureListener_downEnterKey(JNIEnv * a,jobject b)
{
    setEventAndWrite(EV_KEY,KEY_ENTER,1);
    setEventAndWrite(EV_SYN,0,0);
}
void Java_org_secmem_gn_ctos_samdwich_mouse_TabGestureListener_upEnterKey(JNIEnv * a,jobject b)
{
    setEventAndWrite(EV_KEY,KEY_ENTER,0);
    setEventAndWrite(EV_SYN,0,0);
}