#include <Box2D/Box2D.h>

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_WeldJoint_jniGetLocalAnchorA(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_anchor)
{
	float* anchor = (float*)env->GetPrimitiveArrayCritical(obj_anchor, 0);
	b2WeldJoint* joint = (b2WeldJoint*)addr;
	anchor[0] = joint->GetLocalAnchorA().x;
	anchor[1] = joint->GetLocalAnchorA().y;
	env->ReleasePrimitiveArrayCritical(obj_anchor, anchor, 0);
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_WeldJoint_jniGetLocalAnchorB(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_anchor)
{
	float* anchor = (float*)env->GetPrimitiveArrayCritical(obj_anchor, 0);
	b2WeldJoint* joint = (b2WeldJoint*)addr;
	anchor[0] = joint->GetLocalAnchorB().x;
	anchor[1] = joint->GetLocalAnchorB().y;
	env->ReleasePrimitiveArrayCritical(obj_anchor, anchor, 0);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_WeldJoint_jniGetFrequency(JNIEnv* env, jobject object,
    jlong addr)
{
	b2WeldJoint* joint = (b2WeldJoint*)addr;
	return joint->GetFrequency();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_WeldJoint_jniSetFrequency(JNIEnv* env, jobject object,
    jlong addr, jfloat hz)
{
	b2WeldJoint* joint = (b2WeldJoint*)addr;
	joint->SetFrequency(hz);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_WeldJoint_jniGetDampingRatio(JNIEnv* env, jobject object,
    jlong addr)
{
	b2WeldJoint* joint = (b2WeldJoint*)addr;
	return joint->GetDampingRatio();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_WeldJoint_jniSetDampingRatio(JNIEnv* env, jobject object,
    jlong addr, jfloat ratio)
{
	b2WeldJoint* joint = (b2WeldJoint*)addr;
	joint->SetDampingRatio(ratio);
}

