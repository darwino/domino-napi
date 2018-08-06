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

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ODSReadMemory(
	JNIEnv *env, jobject _this, jlong _ppSrc, jshort _type, jlong _pDest, jshort _iterations)
{
	void *ppSrc = DEREF_PTR(_ppSrc);
	WORD type = (WORD)_type;
	void *pDest = DEREF_PTR(_pDest);
	WORD iterations = (WORD)_iterations;

	ODSReadMemory(ppSrc, (WORD)type, pDest, iterations);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ODSWriteMemory(
	JNIEnv *env, jobject _this, jpointer _ppDest, jshort _type, jpointer _pSrc, jshort _iterations)
{
	void *ppDest = DEREF_PTR(_ppDest);
	WORD type = (WORD)_type;
	const void *pSrc = (const void*)DEREF_PTR(_pSrc);
	WORD iterations = (WORD)_iterations;

	ODSWriteMemory(ppDest, type, pSrc, iterations);
}

extern "C" JNIEXPORT jshort JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ODSLength(
	JNIEnv *env, jobject _this, jshort _type)
{
	WORD type = (WORD)_type;
	return (jshort)ODSLength((WORD)type);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_EnumCompositeBuffer(
	JNIEnv *env, jobject _this, jobject _itemValue, jint _itemValueLength, jobject _actionRoutinePtr)
{
	Struct<BLOCKID> itemValue(env, _itemValue);
	DWORD itemValueLength = (DWORD)_itemValueLength;
	J_ActionRoutinePtr actionRoutinePtr(env, _actionRoutinePtr);

	STATUS result = EnumCompositeBuffer(itemValue, itemValueLength, actionRoutinePtr, &actionRoutinePtr);
	ERR(result) == ERR_CANCEL || ERR(result) == ERR_ODS_ENUM_COMPLETE || NCheck(env, result);
}