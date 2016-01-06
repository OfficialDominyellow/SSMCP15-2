LOCAL_PATH := $(call my-dir)                   # 현재 파일의 설정된 경로.

include $(CLEAR_VARS)                          # LOCAL 관련 변수를 clear

LOCAL_MODULE        := hello                   # 모듈 이름, 생성되는 파일 이름을 결정.
LOCAL_CFLAGS        += -std=c++11              # 컴파일 플래그. 여기서는 c++14 사용.
LOCAL_CFLAGS        += -w              # 컴파일 플래그. 여기서는 c++14 사용.
LOCAL_SRC_FILES     := HelloNDK.cpp            # 컴파일 되는 파일 리스트.
include $(BUILD_SHARED_LIBRARY)                # 동적 라이브러리로 사용.

# 현재 사용하지 않는 것들은 주석처리.
#LOCAL_CPP_EXTENSION  := .cpp                  # c++ 소스의 확장자 정의, 기본 cpp.
                                               # 임의 변경 가능
LOCAL_C_INCLUDES := /home/chu/dev/framework/prebuilts/ndk/android-ndk-r4/platforms/android-5/arch-x86/usr/include                          # 헤더 파일의 include 경로.
#LOCAL_LDLIBS     :=                           # 연결하고자 하는 링크 옵션
#LOCAL_STATIC_LIBRARIES                        # 링크할 정적 라이브러리
#LOCAL_SHARED_LIBRARIES                        # 링크할 정적 라이브러리
#include $(BUILD_STATIC_LIBRARY)               # 정적 라이브러리로 사용