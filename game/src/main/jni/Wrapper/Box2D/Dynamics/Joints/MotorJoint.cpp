#include <Box2D/Box2D.h>

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_MotorJoint_jniGetLinearOffset(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_linearOffset)
{
	float* linearOffset = (float*)env->GetPrimitiveArrayCritical(obj_linearOffset, 0);
	b2MotorJoint* joint = (b2MotorJoint*)addr;
	linearOffset[0] = joint->GetLinearOffset().x;
	linearOffset[1] = joint->GetLinearOffset().y;
	env->ReleasePrimitiveArrayCritical(obj_linearOffset, linearOffset, 0);
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_MotorJoint_jniSetLinearOffset(JNIEnv* env, jobject object,
    jlong addr, jfloat linearOffsetX, jfloat linearOffsetY)
{
	b2MotorJoint* joint = (b2MotorJoint*)addr;
	joint->SetLinearOffset(b2Vec2(linearOffsetX, linearOffsetY));
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_MotorJoint_jniGetAngularOffset(JNIEnv* env, jobject object,
    jlong addr)
{
	b2MotorJoint* joint = (b2MotorJoint*)addr;
	return joint->GetAngularOffset();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_MotorJoint_jniSetAngularOffset(JNIEnv* env, jobject object,
    jlong addr, jfloat angularOffset)
{
	b2MotorJoint* joint = (b2MotorJoint*)addr;
	joint->SetAngularOffset(angularOffset);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_MotorJoint_jniGetMaxForce(JNIEnv* env, jobject object,
    jlong addr)
{
	b2MotorJoint* joint = (b2MotorJoint*)addr;
	return joint->GetMaxForce();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_MotorJoint_jniSetMaxForce(JNIEnv* env, jobject object,
    jlong addr, jfloat maxForce)
{
	b2MotorJoint* joint = (b2MotorJoint*)addr;
	joint->SetMaxForce(maxForce);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_MotorJoint_jniGetMaxTorque(JNIEnv* env, jobject object,
    jlong addr)
{
	b2MotorJoint* joint = (b2MotorJoint*)addr;
	return joint->GetMaxTorque();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_MotorJoint_jniSetMaxTorque(JNIEnv* env, jobject object,
    jlong addr, jfloat maxTorque)
{
	b2MotorJoint* joint = (b2MotorJoint*)addr;
	joint->SetMaxTorque(maxTorque);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_MotorJoint_jniGetCorrectionFactor(JNIEnv* env, jobject object,
    jlong addr)
{
	b2MotorJoint* joint = (b2MotorJoint*)addr;
	return joint->GetCorrectionFactor();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_MotorJoint_jniSetCorrectionFactor(JNIEnv* env, jobject object,
    jlong addr, jfloat correctionFactor)
{
	b2MotorJoint* joint = (b2MotorJoint*)addr;
	joint->SetCorrectionFactor(correctionFactor);
}

