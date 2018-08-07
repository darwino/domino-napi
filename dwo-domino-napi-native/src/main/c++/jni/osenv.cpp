/**
 * Copyright © 2014-2018 Darwino, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
