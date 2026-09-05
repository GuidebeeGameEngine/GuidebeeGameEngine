#include "Shape.h"
#include <Box2D/Box2D.h>

JNIEXPORT jfloat JNICALL Java_com_guidebee_game_physics_Shape_jniGetRadius(JNIEnv* env,
    jobject object, jlong addr)
{
	b2Shape* shape = (b2Shape*)addr;
	return shape->m_radius;
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_Shape_jniSetRadius(JNIEnv* env, jobject object,
    jlong addr, jfloat radius)
{
	b2Shape* shape = (b2Shape*)addr;
	shape->m_radius = radius;
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_Shape_jniDispose(JNIEnv* env, jobject object, jlong addr)
{
	b2Shape* shape = (b2Shape*)addr;
	delete shape;
}

JNIEXPORT jint JNICALL
Java_com_guidebee_game_physics_Shape_jniGetType(JNIEnv* env, jclass clazz, jlong addr)
{
	b2Shape* shape = (b2Shape*)addr;
	switch(shape->m_type) {
	    case b2Shape::e_circle: return 0;
		case b2Shape::e_edge: return 1;
		case b2Shape::e_polygon: return 2;
		case b2Shape::e_chain: return 3;
		default: return -1;
	}
}

JNIEXPORT jint JNICALL
Java_com_guidebee_game_physics_Shape_jniGetChildCount(JNIEnv* env, jobject object, jlong addr)
{
	b2Shape* shape = (b2Shape*)addr;
	return shape->GetChildCount();
}

