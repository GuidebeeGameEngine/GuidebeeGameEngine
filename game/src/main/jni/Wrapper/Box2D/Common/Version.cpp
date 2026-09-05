#include "Version.h"
#include <Box2D/Common/b2Settings.h>
/*
 * Class:     com_guidebee_game_engine_box2d_api_common_Version
 * Method:    getVersion
 * Signature: ()V
 */
JNIEXPORT void JNICALL 
Java_com_guidebee_game_physics_Version_getVersion(JNIEnv * env, jclass  cls)
{
	jfieldID fid;
	fid = env->GetStaticFieldID( cls, "Major", "I");
	env->SetStaticIntField(cls,fid,b2_version.major);
	fid = env->GetStaticFieldID( cls, "Minor", "I");
	env->SetStaticIntField(cls,fid,b2_version.minor);
	fid = env->GetStaticFieldID( cls, "Revision", "I");
	env->SetStaticIntField(cls,fid,b2_version.revision);
	
}