#include <jni.h>
#include <string>

extern "C"
JNIEXPORT int JNICALL
Java_fr_m1_1tlse3_mcs_corpusmic_Fragments_RecognizeFragment_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    int hello = 2;
    return hello;
}
