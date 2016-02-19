#include "com_whb_temp_Conversion.h"
#include <jni.h>
#include <stdio.h>
#include "jni_pub_def.h"

static JNINativeMethod method_table[] = {
    //name,signature,fnPtr
    { "nativeCelsius2Fahrenheit", "([F)[F", (void*)Java_com_whb_temp_Conversion_nativeCelsius2Fahrenheit },
    { "nativeFahrenheit2Celsius", "([F)[F", (void*)Java_com_whb_temp_Conversion_nativeFahrenheit2Celsius },
};

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved)
{
    JNIEnv *env;
	jclass clazz;
	jint result = -1;
	if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK)
	{
		LOGI("ERROR: GetEnv failed\n");
		goto bail;
	}

	if ((clazz = env->FindClass("com/whb/temp/Conversion")) == NULL)
	{
		LOGI("ERROR: FindClass com/whb/temp/Conversion failed\n");
		goto bail;
	}

	if (env->RegisterNatives(clazz, method_table, sizeof(method_table) / sizeof(JNINativeMethod)) < 0)
	{
        LOGI("RegisterNatives failed");
        goto bail;
    }

	result = JNI_VERSION_1_4;
bail:
	return result;
}

JNIEXPORT void JNI_OnUnload(JavaVM *vm, void *reserved)
{
	JNIEnv *env;
	jclass k;
	jint r;

	r = vm->GetEnv((void **) &env, JNI_VERSION_1_2);
	k = env->FindClass ("com/whb/temp/Conversion");
	env->UnregisterNatives(k);
}
