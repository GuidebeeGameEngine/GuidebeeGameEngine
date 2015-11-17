#include "PrismaticJoint.h"
#include <Box2D/Box2D.h>

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniGetLocalAnchorA(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_anchor)
{
	float* anchor = (float*)env->GetPrimitiveArrayCritical(obj_anchor, 0);
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	anchor[0] = joint->GetLocalAnchorA().x;
	anchor[1] = joint->GetLocalAnchorA().y;
	env->ReleasePrimitiveArrayCritical(obj_anchor, anchor, 0);
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniGetLocalAnchorB(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_anchor)
{
	float* anchor = (float*)env->GetPrimitiveArrayCritical(obj_anchor, 0);
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	anchor[0] = joint->GetLocalAnchorB().x;
	anchor[1] = joint->GetLocalAnchorB().y;
	env->ReleasePrimitiveArrayCritical(obj_anchor, anchor, 0);
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniGetLocalAxisA(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_anchor)
{
	float* anchor = (float*)env->GetPrimitiveArrayCritical(obj_anchor, 0);
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	anchor[0] = joint->GetLocalAxisA().x;
	anchor[1] = joint->GetLocalAxisA().y;
	env->ReleasePrimitiveArrayCritical(obj_anchor, anchor, 0);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniGetJointTranslation(JNIEnv* env,
    jobject object, jlong addr)
{
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	return joint->GetJointTranslation();
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniGetJointSpeed(JNIEnv* env, jobject object,
    jlong addr)
{
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	return joint->GetJointSpeed();
}

JNIEXPORT jboolean JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniIsLimitEnabled(JNIEnv* env, jobject object,
    jlong addr)
{
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	return joint->IsLimitEnabled();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniEnableLimit(JNIEnv* env, jobject object,
    jlong addr, jboolean flag)
{
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	joint->EnableLimit(flag);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniGetLowerLimit(JNIEnv* env, jobject object,
    jlong addr)
{
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	return joint->GetLowerLimit();
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniGetUpperLimit(JNIEnv* env, jobject object,
    jlong addr)
{
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	return joint->GetUpperLimit();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniSetLimits(JNIEnv* env, jobject object,
    jlong addr, jfloat lower, jfloat upper)
{
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	joint->SetLimits(lower, upper );
}

JNIEXPORT jboolean JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniIsMotorEnabled(JNIEnv* env, jobject object,
    jlong addr)
{

    b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	return joint->IsMotorEnabled();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniEnableMotor(JNIEnv* env, jobject object,
    jlong addr, jboolean flag)
{
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	joint->EnableMotor(flag);
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniSetMotorSpeed(JNIEnv* env, jobject object,
    jlong addr, jfloat speed)
{
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	joint->SetMotorSpeed(speed);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniGetMotorSpeed(JNIEnv* env, jobject object,
    jlong addr)
{
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	return joint->GetMotorSpeed();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniSetMaxMotorForce(JNIEnv* env, jobject object,
    jlong addr, jfloat force)
{
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	joint->SetMaxMotorForce(force);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniGetMotorForce(JNIEnv* env, jobject object,
    jlong addr, jfloat invDt)
{
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	return joint->GetMotorForce(invDt);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniGetMaxMotorForce(JNIEnv* env, jobject object,
    jlong addr)
{
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	return joint->GetMaxMotorForce();
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_PrismaticJoint_jniGetReferenceAngle(JNIEnv* env, jobject object,
    jlong addr)
{
	b2PrismaticJoint* joint = (b2PrismaticJoint*)addr;
	return joint->GetReferenceAngle();
}

