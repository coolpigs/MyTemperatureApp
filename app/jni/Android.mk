LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := utility
LOCAL_SRC_FILES := jni_register.cpp \
                   com_whb_temp_Conversion.cpp
                   
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)
                  