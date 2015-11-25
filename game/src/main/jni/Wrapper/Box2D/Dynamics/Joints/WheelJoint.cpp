#include <Box2D/Box2D.h>

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_WheelJoint_jniGetLocalAnchorA(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_anchor)
{
	float* anchor = (float*)env->GetPrimitiveArrayCritical(obj_anchor, 0);
	b2WheelJoint* joint = (b2WheelJoint*)addr;
	anchor[0] = joint->GetLocalAnchorA().x;
    anchor[1] = joint->GetLocalAnchorA().y;
	env->ReleasePrimitiveArrayCritical(obj_anchor, anchor, 0);
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_WheelJoint_jniGetLocalAnchorB(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_anchor)
{
	float* anchor = (float*)env->GetPrimitiveArrayCritical(obj_anchor, 0);
	b2WheelJoint* joint = (b2WheelJoint*)addr;
	anchor[0] = joint->GetLocalAnchorB().x;
	anchor[1] = joint->GetLocalAnchorB().y;
	env->ReleasePrimitiveArrayCritical(obj_anchor, anchor, 0);

}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_WheelJoint_jniGetLocalAxisA(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_anchor)
{
	float* anchor = (float*)env->GetPrimitiveArrayCritical(obj_anchor, 0);
	b2WheelJoint* joint = (b2WheelJoint*)addr;
	anchor[0] = joint->GetLocalAxisA().x;
	anchor[1] = joint->GetLocalAxisA().y;
	env->ReleasePrimitiveArrayCritical(obj_anchor, anchor, 0);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_WheelJoint_jniGetJointTranslation(JNIEnv* env, jobject object,
    jlong addr)
{
  	b2WheelJoint* joint = (b2WheelJoint*)addr;
	return joint->GetJointTranslation();
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_WheelJoint_jniGetJointSpeed(JNIEnv* env, jobject object,
    jlong addr)
{
  	b2WheelJoint* joint = (b2WheelJoint*)addr;
	return joint->GetJointSpeed();
}

JNIEXPORT jboolean JNICALL
Java_com_guidebee_game_physics_joints_WheelJoint_jniIsMotorEnabled(JNIEnv* env, jobject object,
    jlong addr)
{
  	b2WheelJoint* joint = (b2WheelJoint*)addr;
	return joint->IsMotorEnabled();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_WheelJoint_jniEnableMotor(JNIEnv* env, jobject object,
    jlong addr, jboolean flag)
{
  	b2WheelJoint* joint = (b2WheelJoint*)addr;
	joint->EnableMotor(flag);
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_WheelJoint_jniSetMotorSpeed(JNIEnv* env, jobject object,
    jlong addr, jfloat speed)
{
  	b2WheelJoint* joint = (b2WheelJoint*)addr;
	joint->SetMotorSpeed(speed);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_WheelJoint_jniGetMotorSpeed(JNIEnv* env, jobject object,
    jlong addr)
{
  	b2WheelJoint* joint = (b2WheelJoint*)addr;
	return joint->GetMotorSpeed();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_WheelJoint_jniSetMaxMotorTorque(JNIEnv* env, jobject object,
    jlong addr, jfloat torque)
{
  	b2WheelJoint* joint = (b2WheelJoint*)addr;
	joint->SetMaxMotorTorque(torque);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_WheelJoint_jniGetMaxMotorTorque(JNIEnv* env, jobject object,
    jlong addr)
{
	b2WheelJoint* joint = (b2WheelJoint*)addr;
	return joint->GetMaxMotorTorque();
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_WheelJoint_jniGetMotorTorque(JNIEnv* env, jobject object,
    jlong addr, jfloat invDt)
{
  	b2WheelJoint* joint = (b2WheelJoint*)addr;
	return joint->GetMotorTorque(invDt);
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_WheelJoint_jniSetSpringFrequencyHz(JNIEnv* env, jobject object,
    jlong addr, jfloat hz)
{
	b2WheelJoint* joint = (b2WheelJoint*)addr;
	joint->SetSpringFrequencyHz(hz);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_WheelJoint_jniGetSpringFrequencyHz(JNIEnv* env, jobject object,
    jlong addr)
{
	b2WheelJoint* joint = (b2WheelJoint*)addr;
	return joint->GetSpringFrequencyHz();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_WheelJoint_jniSetSpringDampingRatio(JNIEnv* env, jobject object,
    jlong addr, jfloat ratio)
{
	b2WheelJoint* joint = (b2WheelJoint*)addr;
	joint->SetSpringDampingRatio(ratio);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_WheelJoint_jniGetSpringDampingRatio(JNIEnv* env, jobject object,
    jlong addr)
{
	b2WheelJoint* joint = (b2WheelJoint*)addr;
	return joint->GetSpringDampingRatio();
}

