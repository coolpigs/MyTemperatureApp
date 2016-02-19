#include <jni.h>

/* Header for class com_whb_temp_Conversion */

#ifndef _Included_com_whb_temp_Conversion
#define _Included_com_whb_temp_Conversion
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_whb_temp_Conversion
 * Method:    nativeCelsius2Fahrenheit
 * Signature: ([F)[F
 */
JNIEXPORT jfloatArray JNICALL Java_com_whb_temp_Conversion_nativeCelsius2Fahrenheit
  (JNIEnv *, jobject, jfloatArray);

/*
 * Class:     com_whb_temp_Conversion
 * Method:    nativeFahrenheit2Celsius
 * Signature: ([F)[F
 */
JNIEXPORT jfloatArray JNICALL Java_com_whb_temp_Conversion_nativeFahrenheit2Celsius
  (JNIEnv *, jobject, jfloatArray);




#ifdef __cplusplus
}
#endif
#endif