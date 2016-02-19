#ifndef _JNI_REGISTER_H_
#define _JNI_REGISTER_H_

/**************************/
/***** compile macro ******/
/**************************/
#undef DEBUG_VERSION


#ifdef DEBUG_VERSION
#include "android/log.h"
#define  LOG_TAG    "WHB_JNI"
#define  LOGI(...)     __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#else
#define  LOGI(...)
#endif

#endif
