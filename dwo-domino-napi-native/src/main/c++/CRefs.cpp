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


jclass _IntRef::clazz;
jmethodID _IntRef::getMethod;
jmethodID _IntRef::setMethod;
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_BaseIntRef_initNative(
			JNIEnv *env, jclass clazz
			) {
	_IntRef::clazz = clazz;
	_IntRef::getMethod = env->GetMethodID(clazz, "get", "()I");
	_IntRef::setMethod = env->GetMethodID(clazz, "set", "(I)V");
}


jclass _LongRef::clazz;
jmethodID _LongRef::getMethod;
jmethodID _LongRef::setMethod;
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_BaseLongRef_initNative(
			JNIEnv *env, jclass clazz
			) {
	_LongRef::clazz = clazz;
	_LongRef::getMethod = env->GetMethodID(clazz, "get", "()J");
	_LongRef::setMethod = env->GetMethodID(clazz, "set", "(J)V");
}



jclass _ShortRef::clazz;
jmethodID _ShortRef::getMethod;
jmethodID _ShortRef::setMethod;
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_BaseShortRef_initNative(
	JNIEnv *env, jclass clazz
	) {
	_ShortRef::clazz = clazz;
	_ShortRef::getMethod = env->GetMethodID(clazz, "get", "()S");
	_ShortRef::setMethod = env->GetMethodID(clazz, "set", "(S)V");
}

jclass _ByteRef::clazz;
jmethodID _ByteRef::getMethod;
jmethodID _ByteRef::setMethod;
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_BaseByteRef_initNative(
	JNIEnv *env, jclass clazz
	) {
	_ByteRef::clazz = clazz;
	_ByteRef::getMethod = env->GetMethodID(clazz, "get", "()B");
	_ByteRef::setMethod = env->GetMethodID(clazz, "set", "(B)V");
}