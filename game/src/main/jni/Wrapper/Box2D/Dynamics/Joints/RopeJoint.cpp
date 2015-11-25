#include <Box2D/Box2D.h>

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_RopeJoint_jniGetLocalAnchorA(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_anchor)
{
	float* anchor = (float*)env->GetPrimitiveArrayCritical(obj_anchor, 0);
	b2RopeJoint* joint = (b2RopeJoint*)addr;
	anchor[0] = joint->GetLocalAnchorA().x;
	anchor[1] = joint->GetLocalAnchorA().y;
	env->ReleasePrimitiveArrayCritical(obj_anchor, anchor, 0);

}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_RopeJoint_jniGetLocalAnchorB(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_anchor)
{
	float* anchor = (float*)env->GetPrimitiveArrayCritical(obj_anchor, 0);
	b2RopeJoint* joint = (b2RopeJoint*)addr;
	anchor[0] = joint->GetLocalAnchorB().x;
	anchor[1] = joint->GetLocalAnchorB().y;
	env->ReleasePrimitiveArrayCritical(obj_anchor, anchor, 0);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_RopeJoint_jniGetMaxLength(JNIEnv* env, jobject object,
    jlong addr)
{
	b2RopeJoint* rope = (b2RopeJoint*)addr;
	return rope->GetMaxLength();
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_RopeJoint_jniSetMaxLength(JNIEnv* env, jobject object,
    jlong addr, jfloat length)
{
	b2RopeJoint* rope = (b2RopeJoint*)addr;
	rope->SetMaxLength(length);
}

