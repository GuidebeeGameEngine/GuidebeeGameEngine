#include "FrictionJoint.h"
#include <Box2D/Box2D.h>

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_FrictionJoint_jniGetLocalAnchorA(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_anchor)
{
	float* anchor = (float*)env->GetPrimitiveArrayCritical(obj_anchor, 0);
	b2FrictionJoint* joint = (b2FrictionJoint*)addr;
	anchor[0] = joint->GetLocalAnchorA().x;
	anchor[1] = joint->GetLocalAnchorA().y;
	env->ReleasePrimitiveArrayCritical(obj_anchor, anchor, 0);

}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_FrictionJoint_jniGetLocalAnchorB(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_anchor)
{
	float* anchor = (float*)env->GetPrimitiveArrayCritical(obj_anchor, 0);
	b2FrictionJoint* joint = (b2FrictionJoint*)addr;
	anchor[0] = joint->GetLocalAnchorB().x;
	anchor[1] = joint->GetLocalAnchorB().y;
	env->ReleasePrimitiveArrayCritical(obj_anchor, anchor, 0);

}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_FrictionJoint_jniSetMaxForce(JNIEnv* env, jobject object,
    jlong addr, jfloat force)
{
	b2FrictionJoint* joint = (b2FrictionJoint*)addr;
	joint->SetMaxForce( force );
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_FrictionJoint_jniGetMaxForce(JNIEnv* env, jobject object,
    jlong addr)
{
	b2FrictionJoint* joint = (b2FrictionJoint*)addr;
	return joint->GetMaxForce();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_FrictionJoint_jniSetMaxTorque(JNIEnv* env, jobject object,
    jlong addr, jfloat torque)
{
	b2FrictionJoint* joint = (b2FrictionJoint*)addr;
	joint->SetMaxTorque( torque );
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_FrictionJoint_jniGetMaxTorque(JNIEnv* env, jobject object,
    jlong addr)
{
	b2FrictionJoint* joint = (b2FrictionJoint*)addr;
	return joint->GetMaxTorque();
}

