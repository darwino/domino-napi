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


extern "C" JNIEXPORT jpointer JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSLockObject(
	JNIEnv *env, jobject _this, jlong Handle) {
	return REF_PTR(OSLockObject((DHANDLE)Handle));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSUnlockObject(
	JNIEnv *env, jobject _this, jlong Handle) {
	OSUnlockObject(jdhandle_to_dhandle(Handle));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSMemoryFree(
	JNIEnv *env, jobject _this, jlong handle) {
	OSMemoryFree(jlong_to_memhandle(handle));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSMemoryLock(
	JNIEnv *env, jobject _this, jlong handle) {
	OSMemoryLock(jlong_to_memhandle(handle));
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSMemoryUnlock(
	JNIEnv *env, jobject _this, jlong handle) {
	BOOL result = OSMemoryUnlock(jlong_to_memhandle(handle));
	return (jboolean)result;
}

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSMemAlloc(
	JNIEnv *env, jobject _this, jshort blkType, jint dwSize) {
	DHANDLE retHandle;
	if (!NCheck(env, OSMemAlloc((WORD)blkType, (DWORD)dwSize, &retHandle))) {
		return 0;
	}
	return dhandle_to_jdhandle(retHandle);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSMemFree(
	JNIEnv *env, jobject _this, jlong Handle) {
	if (!NCheck(env, OSMemFree(jdhandle_to_dhandle(Handle)))) {
		return;
	}
}
