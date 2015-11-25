#include <Box2D/Box2D.h>

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_MouseJoint_jniSetTarget(JNIEnv* env, jobject object,
    jlong addr, jfloat x, jfloat y)
{
	b2MouseJoint* joint = (b2MouseJoint*)addr;
	joint->SetTarget( b2Vec2(x, y ) );
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_MouseJoint_jniGetTarget(JNIEnv* env, jobject object,
    jlong addr, jfloatArray obj_target)
{
	float* target = (float*)env->GetPrimitiveArrayCritical(obj_target, 0);
	b2MouseJoint* joint = (b2MouseJoint*)addr;
	target[0] = joint->GetTarget().x;
	target[1] = joint->GetTarget().y;
	env->ReleasePrimitiveArrayCritical(obj_target, target, 0);
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_MouseJoint_jniSetMaxForce(JNIEnv* env, jobject object,
    jlong addr, jfloat force)
{
	b2MouseJoint* joint = (b2MouseJoint*)addr;
	joint->SetMaxForce( force );
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_MouseJoint_jniGetMaxForce(JNIEnv* env, jobject object,
    jlong addr)
{
	b2MouseJoint* joint = (b2MouseJoint*)addr;
	return joint->GetMaxForce();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_MouseJoint_jniSetFrequency(JNIEnv* env, jobject object,
    jlong addr, jfloat hz)
{
	b2MouseJoint* joint = (b2MouseJoint*)addr;
	joint->SetFrequency(hz);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_MouseJoint_jniGetFrequency(JNIEnv* env, jobject object,
    jlong addr)
{
	b2MouseJoint* joint = (b2MouseJoint*)addr;
	return joint->GetFrequency();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_joints_MouseJoint_jniSetDampingRatio(JNIEnv* env, jobject object,
    jlong addr, jfloat ratio)
{
	b2MouseJoint* joint = (b2MouseJoint*)addr;
	joint->SetDampingRatio( ratio );
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_joints_MouseJoint_jniGetDampingRatio(JNIEnv* env, jobject object,
    jlong addr)
{
	b2MouseJoint* joint = (b2MouseJoint*)addr;
	return joint->GetDampingRatio();
}

