#include "Fixture.h"
#include <Box2D/Box2D.h>

JNIEXPORT jint JNICALL
Java_com_guidebee_game_physics_Fixture_jniGetType(JNIEnv* env, jobject object, jlong addr)
{
    b2Fixture* fixture = (b2Fixture*)addr;
    b2Shape::Type type = fixture->GetType();
    switch( type )
    {
        case b2Shape::e_circle: return 0;
        case b2Shape::e_edge: return 1;
        case b2Shape::e_polygon: return 2;
        case b2Shape::e_chain: return 3;
        default:
            return -1;
    }
	
}

JNIEXPORT jlong JNICALL
Java_com_guidebee_game_physics_Fixture_jniGetShape(JNIEnv* env, jobject object, jlong addr)
{
	b2Fixture* fixture = (b2Fixture*)addr;
	return (jlong)fixture->GetShape();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_Fixture_jniSetSensor(JNIEnv* env, jobject object,
    jlong addr, jboolean sensor)
{
	b2Fixture* fixture = (b2Fixture*)addr;
	fixture->SetSensor(sensor);
}

JNIEXPORT jboolean JNICALL
Java_com_guidebee_game_physics_Fixture_jniIsSensor(JNIEnv* env, jobject object, jlong addr)
{
	b2Fixture* fixture = (b2Fixture*)addr;
	return fixture->IsSensor();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_Fixture_jniSetFilterData(JNIEnv* env, jobject object,
    jlong addr, jshort categoryBits, jshort maskBits, jshort groupIndex)
{
    b2Fixture* fixture = (b2Fixture*)addr;
    b2Filter filter;
    filter.categoryBits = categoryBits;
    filter.maskBits = maskBits;
    filter.groupIndex = groupIndex;
    fixture->SetFilterData(filter);
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_Fixture_jniGetFilterData(JNIEnv* env, jobject object,
    jlong addr, jshortArray obj_filter)
{
	short* filter = (short*)env->GetPrimitiveArrayCritical(obj_filter, 0);
    b2Fixture* fixture = (b2Fixture*)addr;
    unsigned short* filterOut = (unsigned short*)filter;
    b2Filter f = fixture->GetFilterData();
    filterOut[0] = f.maskBits;
    filterOut[1] = f.categoryBits;
    filterOut[2] = f.groupIndex;
	env->ReleasePrimitiveArrayCritical(obj_filter, filter, 0);
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_Fixture_jniRefilter(JNIEnv* env, jobject object, jlong addr)
{
	b2Fixture* fixture = (b2Fixture*)addr;
	fixture->Refilter();
}

JNIEXPORT jboolean JNICALL
Java_com_guidebee_game_physics_Fixture_jniTestPoint(JNIEnv* env, jobject object, jlong addr,
    jfloat x, jfloat y)
{
	b2Fixture* fixture = (b2Fixture*)addr;
	return fixture->TestPoint( b2Vec2( x, y ) );
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_Fixture_jniSetDensity(JNIEnv* env, jobject object,
    jlong addr, jfloat density)
{
	b2Fixture* fixture = (b2Fixture*)addr;
	fixture->SetDensity(density);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_Fixture_jniGetDensity(JNIEnv* env, jobject object, jlong addr)
{
	b2Fixture* fixture = (b2Fixture*)addr;
	return fixture->GetDensity();
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_Fixture_jniGetFriction(JNIEnv* env, jobject object, jlong addr)
{
	b2Fixture* fixture = (b2Fixture*)addr;
	return fixture->GetFriction();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_Fixture_jniSetFriction(JNIEnv* env, jobject object, jlong addr,
    jfloat friction)
{
	b2Fixture* fixture = (b2Fixture*)addr;
	fixture->SetFriction(friction);
}

JNIEXPORT jfloat JNICALL
Java_com_guidebee_game_physics_Fixture_jniGetRestitution(JNIEnv* env, jobject object, jlong addr)
{
	b2Fixture* fixture = (b2Fixture*)addr;
	return fixture->GetRestitution();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_Fixture_jniSetRestitution(JNIEnv* env, jobject object,
    jlong addr, jfloat restitution)
{
	b2Fixture* fixture = (b2Fixture*)addr;
	fixture->SetRestitution(restitution);
}

