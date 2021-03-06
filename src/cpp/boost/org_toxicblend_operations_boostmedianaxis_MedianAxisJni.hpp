/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_toxicblend_operations_boostmedianaxis_MedianAxisJni */

#ifndef _Included_org_toxicblend_operations_boostmedianaxis_MedianAxisJni
#define _Included_org_toxicblend_operations_boostmedianaxis_MedianAxisJni
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_toxicblend_operations_boostmedianaxis_MedianAxisJni
 * Method:    allocateJni_
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_org_toxicblend_operations_boostmedianaxis_MedianAxisJni_allocateJni_1
  (JNIEnv *, jobject);

/*
 * Class:     org_toxicblend_operations_boostmedianaxis_MedianAxisJni
 * Method:    deallocateJni_
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_toxicblend_operations_boostmedianaxis_MedianAxisJni_deallocateJni_1
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_toxicblend_operations_boostmedianaxis_MedianAxisJni
 * Method:    getRingJni_
 * Signature: (JI)[F
 */
JNIEXPORT jfloatArray JNICALL Java_org_toxicblend_operations_boostmedianaxis_MedianAxisJni_getRingJni_1
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     org_toxicblend_operations_boostmedianaxis_MedianAxisJni
 * Method:    setRingJni_
 * Signature: (JI[FF)V
 */
JNIEXPORT void JNICALL Java_org_toxicblend_operations_boostmedianaxis_MedianAxisJni_setRingJni_1
  (JNIEnv *, jobject, jlong, jint, jfloatArray, jfloat);

/*
 * Class:     org_toxicblend_operations_boostmedianaxis_MedianAxisJni
 * Method:    addRingJni_
 * Signature: (J[FF)I
 */
JNIEXPORT jint JNICALL Java_org_toxicblend_operations_boostmedianaxis_MedianAxisJni_addRingJni_1
  (JNIEnv *, jobject, jlong, jfloatArray, jfloat);

/*
 * Class:     org_toxicblend_operations_boostmedianaxis_MedianAxisJni
 * Method:    ringContainsAllPointsJni_
 * Signature: (J[FI)Z
 */
JNIEXPORT jboolean JNICALL Java_org_toxicblend_operations_boostmedianaxis_MedianAxisJni_ringContainsAllPointsJni_1
  (JNIEnv *, jobject, jlong, jfloatArray, jint);

/*
 * Class:     org_toxicblend_operations_boostmedianaxis_MedianAxisJni
 * Method:    ringContainsRingJni_
 * Signature: (JII)Z
 */
JNIEXPORT jboolean JNICALL Java_org_toxicblend_operations_boostmedianaxis_MedianAxisJni_ringContainsRingJni_1
  (JNIEnv *, jobject, jlong, jint, jint);

/*
 * Class:     org_toxicblend_operations_boostmedianaxis_MedianAxisJni
 * Method:    voronoiInternalEdgesJni_
 * Signature: (J[I[IFFF)[F
 */
JNIEXPORT jfloatArray JNICALL Java_org_toxicblend_operations_boostmedianaxis_MedianAxisJni_voronoiInternalEdgesJni_1
  (JNIEnv *, jobject, jlong, jintArray, jintArray, jfloat, jfloat, jfloat);

/*
 * Class:     org_toxicblend_operations_boostmedianaxis_MedianAxisJni
 * Method:    simplify3D_
 * Signature: ([FF)[F
 */
JNIEXPORT jfloatArray JNICALL Java_org_toxicblend_operations_boostmedianaxis_MedianAxisJni_simplify3D_1
  (JNIEnv *, jobject, jfloatArray, jfloat);

/*
 * Class:     org_toxicblend_operations_boostmedianaxis_MedianAxisJni
 * Method:    simplify2D_
 * Signature: ([FF)[F
 */
JNIEXPORT jfloatArray JNICALL Java_org_toxicblend_operations_boostmedianaxis_MedianAxisJni_simplify2D_1
  (JNIEnv *, jobject, jfloatArray, jfloat);

#ifdef __cplusplus
}
#endif
#endif
