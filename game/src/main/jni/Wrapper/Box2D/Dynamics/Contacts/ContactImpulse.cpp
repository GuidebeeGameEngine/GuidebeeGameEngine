#include <Box2D/Box2D.h>

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_ContactImpulse_jniGetNormalImpulses(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_values)
{
	float* values = (float*)env->GetPrimitiveArrayCritical(obj_values, 0);
	b2ContactImpulse* contactImpulse = (b2ContactImpulse*)addr;
	values[0] = contactImpulse->normalImpulses[0];
	values[1] = contactImpulse->normalImpulses[1];
	env->ReleasePrimitiveArrayCritical(obj_values, values, 0);
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_ContactImpulse_jniGetTangentImpulses(JNIEnv* env,
    jobject object, jlong addr, jfloatArray obj_values)
{
	float* values = (float*)env->GetPrimitiveArrayCritical(obj_values, 0);
  	b2ContactImpulse* contactImpulse = (b2ContactImpulse*)addr;
	values[0] = contactImpulse->tangentImpulses[0];
	values[1] = contactImpulse->tangentImpulses[1];
	env->ReleasePrimitiveArrayCritical(obj_values, values, 0);
}

JNIEXPORT jint JNICALL
Java_com_guidebee_game_physics_ContactImpulse_jniGetCount(JNIEnv* env, jobject object, jlong addr)
{
	b2ContactImpulse* contactImpulse = (b2ContactImpulse*)addr;
	return contactImpulse->count;
}

