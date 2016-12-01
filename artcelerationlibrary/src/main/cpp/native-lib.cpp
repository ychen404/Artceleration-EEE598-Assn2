#include <jni.h>
#include <string>
extern "C"
jstring
//Java_edu_asu_msrs_artcelerationlibrary_ArtLib_StringFromJNI(
Java_edu_asu_msrs_artcelerationlibrary_ArtTransformService_GaussianFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "GoodHello";
    return env->NewStringUTF(hello.c_str());
}