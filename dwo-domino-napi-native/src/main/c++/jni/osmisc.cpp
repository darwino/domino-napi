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

// This doesn't use jstring because it's intended for low-level use from the Java side
extern "C" JNIEXPORT jshort JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSTranslate(
	JNIEnv *env, jobject _this, jshort translateMode, jlong inPtr, jshort inLength, jlong outPtr, jshort outLength)
{
	return OSTranslate((WORD)translateMode, (const char *)inPtr, (WORD)inLength, (char *)outPtr, (WORD)outLength);
}

extern "C" JNIEXPORT jpointer JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSGetLMBCSCLS(
	JNIEnv *env, jobject _this)
{
	return REF_PTR(OSGetLMBCSCLS());
}

extern "C" JNIEXPORT jpointer JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSGetNativeCLS(
	JNIEnv *env, jobject _this)
{
	return REF_PTR(OSGetNativeCLS());
}