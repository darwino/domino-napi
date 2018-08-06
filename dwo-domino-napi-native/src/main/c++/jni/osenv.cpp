/*!COPYRIGHT HEADER! - CONFIDENTIAL 
 *
 * Darwino Inc Confidential.
 *
 * (c) Copyright Darwino Inc 2014-2015.
 *
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U.S. Copyright Office.     
 */

#include "allinc.h"


extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSGetEnvironmentInt(
	JNIEnv *env, jobject _this,
			jstring _name) {
	JStringLMBCS name(env,_name);
	return OSGetEnvironmentInt(name.getLMBCS());
}

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSGetEnvironmentLong(
	JNIEnv *env, jobject _this,
			jstring _name) {
	JStringLMBCS name(env,_name);
	return long_to_jlong(OSGetEnvironmentLong(name.getLMBCS()));
}

extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSGetEnvironmentString(
	JNIEnv *env, jobject _this, jstring _name) {
	JStringLMBCS name(env,_name);
	char retBuffer[4 * 1024];
	if(OSGetEnvironmentString(name.getLMBCS(),retBuffer,sizeof(retBuffer))) {
		return LMBCStoJavaString(env,retBuffer);
	}
	return NULL;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSSetEnvironmentInt(
	JNIEnv *env, jobject _this,
			jstring _name, jint value) {
	JStringLMBCS name(env,_name);
	OSSetEnvironmentInt(name.getLMBCS(),value);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSSetEnvironmentVariable(
	JNIEnv *env, jobject _this,
			jstring _name, jstring _value) {
	JStringLMBCS name(env,_name);
	JStringLMBCS value(env,_value);
	OSSetEnvironmentVariable(name.getLMBCS(),value.getLMBCS());
}
