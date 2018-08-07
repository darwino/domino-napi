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


extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSCurrentTimeZone(
	JNIEnv *env, jobject _this,
	jobject _retZone, jobject _retDST) {
	IntRef<int> retZone(env,_retZone);
	IntRef<int> retDST(env,_retDST);
	OSCurrentTimeZone(retZone,retDST);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSCurrentTIMEDATE(
	JNIEnv *env, jobject _this,
	jobject _retTimeDate) {
	Struct<TIMEDATE> retTimeDate(env,_retTimeDate);
	OSCurrentTIMEDATE(retTimeDate);
}
