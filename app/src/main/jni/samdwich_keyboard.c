////
//// Created by 김희중 on 2016-01-25.
////
//
//#include "samdwich_driver.h"
//
//void Java_org_secmem_gn_ctos_samdwich_mouse_TabGestureListener_inputTabKey(JNIEnv * a,jobject b)
//{
//    struct input_event tabEV;
//
//    memset(&tabEV,0,sizeof(struct input_event));
//    tabEV.type = EV_KEY;
//    tabEV.code=KEY_TAB;
//    tabEV.value=1;
//    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
//        die("error: write");
//
//    memset(&tabEV, 0, sizeof(struct input_event));
//    tabEV.type = EV_SYN;
//    tabEV.code = 0;
//    tabEV.value = 0;
//    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
//        die("error: write");
//
//    memset(&tabEV,0,sizeof(struct input_event));
//    tabEV.type = EV_KEY;
//    tabEV.code=KEY_TAB;
//    tabEV.value=0;
//    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
//        die("error: write");
//
//    memset(&tabEV, 0, sizeof(struct input_event));
//    tabEV.type = EV_SYN;
//    tabEV.code = 0;
//    tabEV.value = 0;
//    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
//        die("error: write");
//}
//
//void Java_org_secmem_gn_ctos_samdwich_mouse_TabGestureListener_inputBackTabKey(JNIEnv * a,jobject b)
//{
//    struct input_event tabEV;
//
//    memset(&tabEV,0,sizeof(struct input_event));
//    tabEV.type = EV_KEY;
//    tabEV.code=KEY_LEFTSHIFT;
//    tabEV.value=1;
//    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
//        die("error: write");
//    memset(&tabEV, 0, sizeof(struct input_event));
//    tabEV.type = EV_SYN;
//    tabEV.code = 0;
//    tabEV.value = 0;
//    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
//        die("error: write");
//
//
//    memset(&tabEV,0,sizeof(struct input_event));
//    tabEV.type = EV_KEY;
//    tabEV.code=KEY_TAB;
//    tabEV.value=1;
//    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
//        die("error: write");
//
//    memset(&tabEV, 0, sizeof(struct input_event));
//    tabEV.type = EV_SYN;
//    tabEV.code = 0;
//    tabEV.value = 0;
//    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
//        die("error: write");
//
//    memset(&tabEV,0,sizeof(struct input_event));
//    tabEV.type = EV_KEY;
//    tabEV.code=KEY_TAB;
//    tabEV.value=0;
//    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
//        die("error: write");
//
//    memset(&tabEV, 0, sizeof(struct input_event));
//    tabEV.type = EV_SYN;
//    tabEV.code = 0;
//    tabEV.value = 0;
//    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
//        die("error: write");
//
//
//    memset(&tabEV,0,sizeof(struct input_event));
//    tabEV.type = EV_KEY;
//    tabEV.code=KEY_LEFTSHIFT;
//    tabEV.value=0;
//    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
//        die("error: write");
//    memset(&tabEV, 0, sizeof(struct input_event));
//    tabEV.type = EV_SYN;
//    tabEV.code = 0;
//    tabEV.value = 0;
//    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
//        die("error: write");
//}
//
//void Java_org_secmem_gn_ctos_samdwich_mouse_TabGestureListener_inputEnterKey(JNIEnv * a,jobject b)
//{
//    struct input_event tabEV;
//    memset(&tabEV,0,sizeof(struct input_event));
//    tabEV.type = EV_KEY;
//    tabEV.code=KEY_ENTER;
//    tabEV.value=1;
//    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
//        die("error: write");
//
//    memset(&tabEV, 0, sizeof(struct input_event));
//    tabEV.type = EV_SYN;
//    tabEV.code = 0;
//    tabEV.value = 0;
//    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
//        die("error: write");
//
//    memset(&tabEV,0,sizeof(struct input_event));
//    tabEV.type = EV_KEY;
//    tabEV.code=KEY_ENTER;
//    tabEV.value=0;
//    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
//        die("error: write");
//
//    memset(&tabEV, 0, sizeof(struct input_event));
//    tabEV.type = EV_SYN;
//    tabEV.code = 0;
//    tabEV.value = 0;
//    if(write(fd, &tabEV, sizeof(struct input_event)) < 0)
//        die("error: write");
//}