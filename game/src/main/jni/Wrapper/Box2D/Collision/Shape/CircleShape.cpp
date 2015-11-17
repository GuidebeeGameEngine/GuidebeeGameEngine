#include "CircleShape.h"
#include <Box2D/Box2D.h>

JNIEXPORT jlong JNICALL
Java_com_guidebee_game_physics_CircleShape_newCircleShape(JNIEnv* env,
    jobject object)
{
    return (jlong)(new b2CircleShape( ));
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_CircleShape_jniGetPosition(JNIEnv* env,
    jobject object, jlong addr, jfloatArray obj_position)
{
	float* position = (float*)env->GetPrimitiveArrayCritical(obj_position, 0);
	b2CircleShape* circle = (b2CircleShape*)addr;
	position[0] = circle->m_p.x;
	position[1] = circle->m_p.y;
	env->ReleasePrimitiveArrayCritical(obj_position, position, 0);
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_CircleShape_jniSetPosition(JNIEnv* env,
    jobject object, jlong addr, jfloat positionX, jfloat positionY)
{
	b2CircleShape* circle = (b2CircleShape*)addr;
	circle->m_p.x = positionX;
	circle->m_p.y = positionY;
}

