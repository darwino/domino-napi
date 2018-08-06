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