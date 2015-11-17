#include "Manifold.h"
#include <Box2D/Box2D.h>

JNIEXPORT jint JNICALL
Java_com_guidebee_game_physics_Manifold_jniGetType(JNIEnv* env, jobject object, jlong addr)
{
	b2Manifold* manifold = (b2Manifold*)addr;
	return manifold->type;
}

JNIEXPORT jint JNICALL
Java_com_guidebee_game_physics_Manifold_jniGetPointCount(JNIEnv* env, jobject object, jlong addr)
{
  	b2Manifold* manifold = (b2Manifold*)addr;
	return manifold->pointCount;
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_Manifold_jniGetLocalNormal(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_values)
{
	float* values = (float*)env->GetPrimitiveArrayCritical(obj_values, 0);
	b2Manifold* manifold = (b2Manifold*)addr;
	values[0] = manifold->localNormal.x;
	values[1] = manifold->localNormal.y;
	env->ReleasePrimitiveArrayCritical(obj_values, values, 0);
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_Manifold_jniGetLocalPoint(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_values)
{
	float* values = (float*)env->GetPrimitiveArrayCritical(obj_values, 0);
    b2Manifold* manifold = (b2Manifold*)addr;
    values[0] = manifold->localPoint.x;
    values[1] = manifold->localPoint.y;
	env->ReleasePrimitiveArrayCritical(obj_values, values, 0);

}

static inline jint wrapped_Java_com_guidebee_game_physics_Manifold_jniGetPoint
(JNIEnv* env, jobject object, jlong addr, jfloatArray obj_values, jint idx, float* values)
{
    b2Manifold* manifold = (b2Manifold*)addr;
    values[0] = manifold->points[idx].localPoint.x;
    values[1] = manifold->points[idx].localPoint.y;
    values[2] = manifold->points[idx].normalImpulse;
    values[3] = manifold->points[idx].tangentImpulse;
    return (jint)manifold->points[idx].id.key;
	
}

JNIEXPORT jint JNICALL
Java_com_guidebee_game_physics_Manifold_jniGetPoint(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_values, jint idx)
{
	float* values = (float*)env->GetPrimitiveArrayCritical(obj_values, 0);
	jint JNI_returnValue
	    = wrapped_Java_com_guidebee_game_physics_Manifold_jniGetPoint(env, object,
	        addr, obj_values, idx, values);
	env->ReleasePrimitiveArrayCritical(obj_values, values, 0);
	return JNI_returnValue;
}

