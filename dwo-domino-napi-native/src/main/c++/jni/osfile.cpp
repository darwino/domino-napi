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


extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSGetDataDirectory(
	JNIEnv *env, jobject _this)
{
	char retBuffer[MAXPATH];
	OSGetDataDirectory(retBuffer);
	return LMBCStoJavaString(env,retBuffer);
}

extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSGetExecutableDirectory(
	JNIEnv *env, jobject _this)
{
	char retBuffer[MAXPATH];
	OSGetExecutableDirectory(retBuffer);
	return LMBCStoJavaString(env, retBuffer);
}

extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSPathNetConstruct(
	JNIEnv *env, jobject _this, jstring _portName, jstring _serverName, jstring _fileName)
{
	char *portName = NULL;
	if (_portName != NULL) {
		JStringLMBCS portNameLMBCS(env, _portName);
		portName = (char*)portNameLMBCS.getLMBCS();
	}

	JStringLMBCS serverName(env, _serverName);
	JStringLMBCS fileName(env, _fileName);

	char retBuffer[MAXPATH];
	// This actually won't happen according to the docs, but may as well run the STATUS through the routine just in case
	if (!NCheck(env, OSPathNetConstruct((const char*)portName, serverName.getLMBCS(), fileName.getLMBCS(), retBuffer))) {
		return NULL;
	}
	return LMBCStoJavaString(env, retBuffer);
}