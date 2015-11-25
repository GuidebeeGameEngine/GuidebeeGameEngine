#include <Box2D/Box2D.h>

JNIEXPORT jlong JNICALL
Java_com_guidebee_game_physics_ChainShape_newChainShape(JNIEnv* env, jobject object)
{
    return (jlong)(new b2ChainShape());
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_ChainShape_jniCreateLoop(JNIEnv* env,
    jobject object, jlong addr, jfloatArray obj_verts, jint numVertices)
{
	float* verts = (float*)env->GetPrimitiveArrayCritical(obj_verts, 0);
	b2ChainShape* chain = (b2ChainShape*)addr;
	b2Vec2* verticesOut = new b2Vec2[numVertices];
	for( int i = 0; i < numVertices; i++ )
		verticesOut[i] = b2Vec2(verts[i<<1], verts[(i<<1)+1]);
	chain->CreateLoop( verticesOut, numVertices );
	delete[] verticesOut;
	env->ReleasePrimitiveArrayCritical(obj_verts, verts, 0);
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_ChainShape_jniCreateChain(JNIEnv* env,
    jobject object, jlong addr, jfloatArray obj_verts, jint numVertices)
{
	float* verts = (float*)env->GetPrimitiveArrayCritical(obj_verts, 0);
	b2ChainShape* chain = (b2ChainShape*)addr;
	b2Vec2* verticesOut = new b2Vec2[numVertices];
	for( int i = 0; i < numVertices; i++ )
		verticesOut[i] = b2Vec2(verts[i<<1], verts[(i<<1)+1]);
	chain->CreateChain( verticesOut, numVertices );
	delete[] verticesOut;
	env->ReleasePrimitiveArrayCritical(obj_verts, verts, 0);
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_ChainShape_jniSetPrevVertex(JNIEnv* env,
    jobject object, jlong addr, jfloat x, jfloat y)
{
	b2ChainShape* chain = (b2ChainShape*)addr;
	chain->SetPrevVertex(b2Vec2(x, y));
}


JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_ChainShape_jniSetNextVertex(JNIEnv* env,
    jobject object, jlong addr, jfloat x, jfloat y)
{
	b2ChainShape* chain = (b2ChainShape*)addr;
	chain->SetNextVertex(b2Vec2(x, y));
}


JNIEXPORT jint JNICALL
Java_com_guidebee_game_physics_ChainShape_jniGetVertexCount(JNIEnv* env,
jobject object, jlong addr)
{
	b2ChainShape* chain = (b2ChainShape*)addr;
	return chain->GetVertexCount();
}

JNIEXPORT void JNICALL
Java_com_guidebee_game_physics_ChainShape_jniGetVertex(JNIEnv* env,
    jobject object, jlong addr, jint index, jfloatArray obj_verts)
{
	float* verts = (float*)env->GetPrimitiveArrayCritical(obj_verts, 0);
	b2ChainShape* chain = (b2ChainShape*)addr;
	const b2Vec2 v = chain->GetVertex( index );
	verts[0] = v.x;
	verts[1] = v.y;
	env->ReleasePrimitiveArrayCritical(obj_verts, verts, 0);
}

