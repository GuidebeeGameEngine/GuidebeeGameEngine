#include "DistanceJoint.h"
#include <Box2D/Box2D.h>

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_DistanceJoint_jniGetLocalAnchorA(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_anchor)
{
	float* anchor = (float*)env->GetPrimitiveArrayCritical(obj_anchor, 0);
	b2DistanceJoint* joint = (b2DistanceJoint*)addr;
	anchor[0] = joint->GetLocalAnchorA().x;
	anchor[1] = joint->GetLocalAnchorA().y;
	env->ReleasePrimitiveArrayCritical(obj_anchor, anchor, 0);

}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_DistanceJoint_jniGetLocalAnchorB(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_anchor)
{
	float* anchor = (float*)env->GetPrimitiveArrayCritical(obj_anchor, 0);
	b2DistanceJoint* joint = (b2DistanceJoint*)addr;
	anchor[0] = joint->GetLocalAnchorB().x;
	anchor[1] = joint->GetLocalAnchorB().y;
	env->ReleasePrimitiveArrayCritical(obj_anchor, anchor, 0);

}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_DistanceJoint_jniSetLength(JNIEnv* env, jobject object,
    jlong addr, jfloat length)
{
	b2DistanceJoint* joint = (b2DistanceJoint*)addr;
	joint->SetLength( length );
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_DistanceJoint_jniGetLength(JNIEnv* env, jobject object,
    jlong addr)
{
	b2DistanceJoint* joint = (b2DistanceJoint*)addr;
	return joint->GetLength();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_DistanceJoint_jniSetFrequency(JNIEnv* env, jobject object,
    jlong addr, jfloat hz)
{
	b2DistanceJoint* joint = (b2DistanceJoint*)addr;
	joint->SetFrequency( hz );
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_DistanceJoint_jniGetFrequency(JNIEnv* env, jobject object,
    jlong addr)
{
	b2DistanceJoint* joint = (b2DistanceJoint*)addr;
	return joint->GetFrequency();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_DistanceJoint_jniSetDampingRatio(JNIEnv* env, jobject object,
    jlong addr, jfloat ratio)
{
	b2DistanceJoint* joint = (b2DistanceJoint*)addr;
	joint->SetDampingRatio( ratio );
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_DistanceJoint_jniGetDampingRatio(JNIEnv* env, jobject object,
    jlong addr)
{
	b2DistanceJoint* joint = (b2DistanceJoint*)addr;
	return joint->GetDampingRatio();
}

