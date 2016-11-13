#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_fr_m1_1tlse3_mcs_corpusmic_Fragments_RecognizeFragment_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
