#include "com_whb_temp_Conversion.h"
#include <stdio.h>
#include "jni_pub_def.h"

jfloat c2f(jfloat temp)
{
	return (jfloat) temp * (jfloat) 9 / (jfloat) 5 + 32; 
}

jfloat f2c(jfloat temp)
{
	return (jfloat)(temp  - 32)* (jfloat) 5 / (jfloat) 9;
}
/*
 * Class:     com_whb_temp_Conversion
 * Method:     nativeCelsius2Fahrenheit
 * Signature: ([F)[F
 */
JNIEXPORT jfloatArray JNICALL Java_com_whb_temp_Conversion_nativeCelsius2Fahrenheit
  (JNIEnv *env, jobject obj, jfloatArray temp)
{
    LOGI("nativeCelsius2Fahrenheit");
    jfloatArray result = NULL;
    jfloat *buff = env->GetFloatArrayElements( temp, NULL);
    jint length = env->GetArrayLength(temp);

    //convert C to F
    for(jint i = 0; i < length; i++) 
    {
    	  LOGI("before buff[%d]:%f",i,buff[i]);
        buff[i] = c2f(buff[i]); 
        LOGI("after buff[%d]:%f",i,buff[i]);
    }

    result = env->NewFloatArray(length);
    env->SetFloatArrayRegion(result, 0, length, buff);
    
    env->ReleaseFloatArrayElements(temp, buff, JNI_ABORT);
    return result;
}

/*
 * Class:     com_whb_temp_Conversion
 * Method:    nativeFahrenheit2Celsius
 * Signature: ([F)[F
 */
JNIEXPORT jfloatArray JNICALL Java_com_whb_temp_Conversion_nativeFahrenheit2Celsius
  (JNIEnv *env, jobject obj, jfloatArray temp)
{
    LOGI("nativeFahrenheit2Celsius");
    jfloatArray result = NULL;
    jfloat *buff = env->GetFloatArrayElements( temp, NULL);
    jint length = env->GetArrayLength(temp);
    
    //convert F to C
    for(jint i = 0; i < length; i++) 
    {
    		LOGI("before buff[%d]:%f",i,buff[i]);
        buff[i] = f2c(buff[i]);
        LOGI("after buff[%d]:%f",i,buff[i]);
    }

    result = env->NewFloatArray(length);
    env->SetFloatArrayRegion(result, 0, length, buff);
    
    env->ReleaseFloatArrayElements(temp, buff, JNI_ABORT);
    return result;
}


