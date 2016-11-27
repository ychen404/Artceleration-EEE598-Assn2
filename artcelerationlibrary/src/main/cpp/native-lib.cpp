#include <jni.h>
#include <string>
extern "C"
jstring
Java_edu_asu_msrs_artcelerationlibrary_ArtLib_StringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello";
    return env->NewStringUTF(hello.c_str());
}
