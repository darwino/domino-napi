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

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbReadObject(
	JNIEnv *env, jobject _this, jlong _hDB, jint ObjectID, jint Offset, jint Length)
{
	DHANDLE hDB = jdhandle_to_dhandle(_hDB);
	DHANDLE rethBuffer;

	if (!NCheck(env, NSFDbReadObject(hDB, (DWORD)ObjectID, (DWORD)Offset, (DWORD)Length, &rethBuffer))) {
		return 0;
	}
	return dhandle_to_jdhandle(rethBuffer);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbGetObjectSize(
	JNIEnv *env, jobject _this, jlong _hDB, jint ObjectID, jshort ObjectType, jobject _retSize, jobject _retClass, jobject _retPrivileges)
{
	DHANDLE hDB = jdhandle_to_dhandle(_hDB);
	IntRef<DWORD> retSize(env, _retSize);
	ShortRef<WORD> retClass(env, _retClass);
	ShortRef<WORD> retPrivileges(env, _retPrivileges);

	NCheck(env, NSFDbGetObjectSize(hDB, (DWORD)ObjectID, (WORD)ObjectType, retSize, retClass, retPrivileges));
}