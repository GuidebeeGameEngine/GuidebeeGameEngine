/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_guidebee_game_physics_box2d_ChainShape */

#ifndef _Included_com_guidebee_game_physics_box2d_ChainShape
#define _Included_com_guidebee_game_physics_box2d_ChainShape
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_guidebee_game_physics_box2d_ChainShape
 * Method:    newChainShape
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_guidebee_game_physics_ChainShape_newChainShape
  (JNIEnv *, jobject);

/*
 * Class:     com_guidebee_game_physics_box2d_ChainShape
 * Method:    jniCreateLoop
 * Signature: (J[FI)V
 */
JNIEXPORT void JNICALL Java_com_guidebee_game_physics_ChainShape_jniCreateLoop
  (JNIEnv *, jobject, jlong, jfloatArray, jint);

/*
 * Class:     com_guidebee_game_physics_box2d_ChainShape
 * Method:    jniCreateChain
 * Signature: (J[FI)V
 */
JNIEXPORT void JNICALL Java_com_guidebee_game_physics_ChainShape_jniCreateChain
  (JNIEnv *, jobject, jlong, jfloatArray, jint);

/*
 * Class:     com_guidebee_game_physics_box2d_ChainShape
 * Method:    jniSetPrevVertex
 * Signature: (JFF)V
 */
JNIEXPORT void JNICALL Java_com_guidebee_game_physics_ChainShape_jniSetPrevVertex
  (JNIEnv *, jobject, jlong, jfloat, jfloat);

/*
 * Class:     com_guidebee_game_physics_box2d_ChainShape
 * Method:    jniSetNextVertex
 * Signature: (JFF)V
 */
JNIEXPORT void JNICALL Java_com_guidebee_game_physics_ChainShape_jniSetNextVertex
  (JNIEnv *, jobject, jlong, jfloat, jfloat);

/*
 * Class:     com_guidebee_game_physics_box2d_ChainShape
 * Method:    jniGetVertexCount
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_guidebee_game_physics_ChainShape_jniGetVertexCount
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_guidebee_game_physics_box2d_ChainShape
 * Method:    jniGetVertex
 * Signature: (JI[F)V
 */
JNIEXPORT void JNICALL Java_com_guidebee_game_physics_ChainShape_jniGetVertex
  (JNIEnv *, jobject, jlong, jint, jfloatArray);

#ifdef __cplusplus
}
#endif
#endif
