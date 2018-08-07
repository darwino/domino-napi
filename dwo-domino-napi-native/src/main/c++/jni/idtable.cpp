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

extern "C" JNIEXPORT jboolean JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDAreTablesEqual(
	JNIEnv *env, jobject _this, jdhandle hSrc1Table, jdhandle hSrc2Table)
{
	return (jboolean)IDAreTablesEqual(jdhandle_to_dhandle(hSrc1Table), jdhandle_to_dhandle(hSrc2Table));
}

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDCreateTable(
	JNIEnv *env, jobject _this, jint Alignment)
{
	DHANDLE hTable;
	if (!NCheck(env, IDCreateTable((DWORD)Alignment, &hTable))) {
		return 0;
	}
	return dhandle_to_jdhandle(hTable);
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDDelete(
	JNIEnv *env, jobject _this, jdhandle hTable, jint id)
{
	BOOL fDeleted;
	if (!NCheck(env, IDDelete(jdhandle_to_dhandle(hTable), (DWORD)id, &fDeleted))) {
		return 0;
	}
	return (jboolean)fDeleted;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDDeleteAll(
	JNIEnv *env, jobject _this, jdhandle hTable)
{
	if (!NCheck(env, IDDeleteAll(jdhandle_to_dhandle(hTable)))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDDeleteTable(
	JNIEnv *env, jobject _this, jdhandle hTable, jdhandle hIDsToDelete)
{
	if (!NCheck(env, IDDeleteTable(jdhandle_to_dhandle(hTable), jdhandle_to_dhandle(hIDsToDelete)))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDDestroyTable(
	JNIEnv *env, jobject _this, jdhandle hTable)
{
	DHANDLE h = jdhandle_to_dhandle(hTable);
	if(!NCheck(env,IDDestroyTable(h))) {
		return;
	}
}

extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDEntries(
	JNIEnv *env, jobject _this, jdhandle hTable)
{
	DHANDLE h = jdhandle_to_dhandle(hTable);
	DWORD sz = IDEntries(h);
	return (jint)sz;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDEnumerate(
	JNIEnv *env, jobject _this, jdhandle hTable, jobject proc)
{
	DHANDLE h = jdhandle_to_dhandle(hTable);
	J_IDENUMERATEPROC cb(env, proc);
	STATUS result = IDEnumerate(h, cb, &cb);
	// Allow the use of ERR_CANCEL as a signal from the proc to cancel processing
	ERR(result) == ERR_CANCEL || NCheck(env, result);
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDInsert(
	JNIEnv *env, jobject _this, jdhandle hTable, jint id)
{
	BOOL fInserted;
	if (!NCheck(env, IDInsert(jdhandle_to_dhandle(hTable), (DWORD)id, &fInserted))) {
		return 0;
	}
	return (jboolean)fInserted;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDInsertTable(
	JNIEnv *env, jobject _this, jdhandle hTable, jdhandle hIDsToAdd)
{
	if (!NCheck(env, IDInsertTable(jdhandle_to_dhandle(hTable), jdhandle_to_dhandle(hIDsToAdd)))) {
		return;
	}
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDIsPresent(
	JNIEnv *env, jobject _this, jdhandle hTable, jint id)
{
	return (BOOL)IDIsPresent(jdhandle_to_dhandle(hTable), (DWORD)id);
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDScan(
	JNIEnv *env, jobject _this, jdhandle hTable, jboolean fFirst, jobject _retID)
{
	IntRef<DWORD> retID(env, _retID, true);
	return (jboolean)IDScan(jdhandle_to_dhandle(hTable), (BOOL)fFirst, retID);
}

extern "C" JNIEXPORT jdhandle JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDTableCopy(
	JNIEnv *env, jobject _this, jdhandle hTable)
{
	DHANDLE rethTable;
	if (!NCheck(env, IDTableCopy(jdhandle_to_dhandle(hTable), &rethTable))) {
		return 0;
	}
	return dhandle_to_jdhandle(rethTable);
}

extern "C" JNIEXPORT jshort JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDTableFlags(
	JNIEnv *env, jobject _this, jpointer _pIDTable)
{
	void *pIDTable = DEREF_PTR(_pIDTable);
	return (jshort)IDTableFlags(pIDTable);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDTableIntersect(
	JNIEnv *env, jobject _this, jdhandle hSrc1Table, jdhandle hSrc2Table, jpointer _rethDstTable)
{
	DHANDLE *rethDstTable = (DHANDLE*)DEREF_PTR(_rethDstTable);
	if (!NCheck(env, IDTableIntersect(jdhandle_to_dhandle(hSrc1Table), jdhandle_to_dhandle(hSrc2Table), rethDstTable))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDTableSetFlags(
	JNIEnv *env, jobject _this, jpointer _pIDTable, jshort Flags)
{
	void *pIDTable = DEREF_PTR(_pIDTable);
	IDTableSetFlags(pIDTable, (WORD)Flags);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDTableSetTime(
	JNIEnv *env, jobject _this, jpointer _pIDTable, jobject _Time)
{
	void *pIDTable = DEREF_PTR(_pIDTable);
	Struct<TIMEDATE> Time(env, _Time);
	IDTableSetTime(pIDTable, Time);
}

extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDTableSize(
	JNIEnv *env, jobject _this, jdhandle hTable)
{
	return (jint)IDTableSize(jdhandle_to_dhandle(hTable));
}

extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDTableSizeP(
	JNIEnv *env, jobject _this, jpointer _pIDTable)
{
	void *pIDTable = DEREF_PTR(_pIDTable);
	return (jint)IDTableSizeP(pIDTable);
}

extern "C" JNIEXPORT jpointer JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_IDTableTime(
	JNIEnv *env, jobject _this, jpointer _pIDTable)
{
	void *pIDTable = DEREF_PTR(_pIDTable);
	TIMEDATE td = IDTableTime(pIDTable);
	TIMEDATE *result = (TIMEDATE*)malloc(sizeof(TIMEDATE));
	memcpy(result, &td, sizeof(TIMEDATE));
	return REF_PTR(result);
}