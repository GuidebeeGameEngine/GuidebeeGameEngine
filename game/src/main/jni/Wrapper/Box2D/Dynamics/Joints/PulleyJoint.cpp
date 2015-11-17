#include "PulleyJoint.h"
#include <Box2D/Box2D.h>

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_PulleyJoint_jniGetGroundAnchorA(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_anchor)
{
	float* anchor = (float*)env->GetPrimitiveArrayCritical(obj_anchor, 0);
	b2PulleyJoint* joint = (b2PulleyJoint*)addr;
	anchor[0] = joint->GetGroundAnchorA().x;
	anchor[1] = joint->GetGroundAnchorA().y;
	env->ReleasePrimitiveArrayCritical(obj_anchor, anchor, 0);

}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_PulleyJoint_jniGetGroundAnchorB(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_anchor)
{
	float* anchor = (float*)env->GetPrimitiveArrayCritical(obj_anchor, 0);
	b2PulleyJoint* joint = (b2PulleyJoint*)addr;
	anchor[0] = joint->GetGroundAnchorB().x;
	anchor[1] = joint->GetGroundAnchorB().y;
	env->ReleasePrimitiveArrayCritical(obj_anchor, anchor, 0);

}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_PulleyJoint_jniGetLength1(JNIEnv* env, jobject object,
    jlong addr)
{
	b2PulleyJoint* joint = (b2PulleyJoint*)addr;
	return joint->GetLengthA();
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_PulleyJoint_jniGetLength2(JNIEnv* env, jobject object,
    jlong addr)
{
	b2PulleyJoint* joint = (b2PulleyJoint*)addr;
	return joint->GetLengthB();
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_PulleyJoint_jniGetRatio(JNIEnv* env, jobject object,
    jlong addr)
{
	b2PulleyJoint* joint = (b2PulleyJoint*)addr;
	return joint->GetRatio();
}

