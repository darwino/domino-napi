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

/*
@Override public native int FolderCreate(long hDataDB, long hFolderDB, int formatNoteID, long hFormatDB, String name, int folderType, int flags) throws DominoException;
@Override public native void FolderDelete(long hDataDB, long hFolderDB, int folderNoteID, int flags);
@Override public native int FolderCopy(long hDataDB, long hFolderDB, int folderNoteID, String name, int flags) throws DominoException;
@Override public native void FolderRename(long hDataDB, long hFolderDB, int folderNoteID, String name, int flags) throws DominoException;
@Override public native void FolderMove(long hDataDB, long hFolderDB, int folderNoteID, long hParentDB, int parentNoteID, int flags) throws DominoException;
@Override public native void FolderDocAdd(long hDataDB, long hFolderDB, int folderNoteID, long hTable, int flags) throws DominoException;
@Override public native void FolderDocRemove(long hDataDB, long hFolderDB, int folderNoteID, long hTable, int flags) throws DominoException;
@Override public native void FolderDocRemoveAll(long hDataDB, long hFolderDB, int folderNoteID, int flags) throws DominoException;
@Override public native int FolderDocCount(long hDataDB, long hFolderDB, int folderNoteID, int flags) throws DominoException;
*/

extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_FolderCreate(
	JNIEnv *env, jobject _this, jlong _hDataDB, jlong _hViewDB, jint _formatNoteID, jlong _hFormatDB, jstring _name, jint _folderType, jint _flags)
{
	DHANDLE hDataDB = jdhandle_to_dhandle(_hDataDB);
	DHANDLE hViewDB = jdhandle_to_dhandle(_hViewDB);
	NOTEID formatNoteID = (NOTEID)_formatNoteID;
	DBHANDLE hFormatDB = jdhandle_to_dhandle(_hFormatDB);
	JStringLMBCS name(env, _name);
	WORD nameLen = name.length();
	DWORD folderType = (DWORD)_folderType;
	DWORD flags = (DWORD)_flags;
	NOTEID noteId;

	if (!NCheck(env, FolderCreate(hDataDB, hViewDB, formatNoteID, hFormatDB, (char*)name.getLMBCS(), nameLen, folderType, flags, &noteId))) {
		return 0;
	}
	return noteId;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_FolderDelete(
	JNIEnv *env, jobject _this, jlong _hDataDB, jlong _hViewDB, jint _folderNoteID, jint _flags)
{
	DHANDLE hDataDB = jdhandle_to_dhandle(_hDataDB);
	DHANDLE hViewDB = jdhandle_to_dhandle(_hViewDB);
	NOTEID folderNoteID = (NOTEID)_folderNoteID;
	DWORD flags = (DWORD)_flags;

	NCheck(env, FolderDelete(hDataDB, hViewDB, folderNoteID, flags));
}


extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_FolderCopy(
	JNIEnv *env, jobject _this, jlong _hDataDB, jlong _hViewDB, jint _folderNoteID, jstring _name, jint _flags)
{
	DHANDLE hDataDB = jdhandle_to_dhandle(_hDataDB);
	DHANDLE hViewDB = jdhandle_to_dhandle(_hViewDB);
	NOTEID folderNoteID = (NOTEID)_folderNoteID;
	JStringLMBCS name(env, _name);
	WORD nameLen = name.length();
	DWORD flags = (DWORD)_flags;
	NOTEID noteId;

	if (!NCheck(env, FolderCopy(hDataDB, hViewDB, folderNoteID, (char*)name.getLMBCS(), nameLen, flags, &noteId))) {
		return 0;
	}
	return noteId;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_FolderRename(
	JNIEnv *env, jobject _this, jlong _hDataDB, jlong _hViewDB, jint _folderNoteID, jstring _name, jint _flags)
{
	DHANDLE hDataDB = jdhandle_to_dhandle(_hDataDB);
	DHANDLE hViewDB = jdhandle_to_dhandle(_hViewDB);
	NOTEID folderNoteID = (NOTEID)_folderNoteID;
	JStringLMBCS name(env, _name);
	WORD nameLen = name.length();
	DWORD flags = (DWORD)_flags;

	NCheck(env, FolderRename(hDataDB, hViewDB, folderNoteID, (char*)name.getLMBCS(), nameLen, flags));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_FolderMove(
	JNIEnv *env, jobject _this, jlong _hDataDB, jlong _hViewDB, jint _folderNoteID, jlong _hParentDB, jint _parentNoteID, jint _flags)
{
	DHANDLE hDataDB = jdhandle_to_dhandle(_hDataDB);
	DHANDLE hViewDB = jdhandle_to_dhandle(_hViewDB);
	NOTEID folderNoteID = (NOTEID)_folderNoteID;
	DHANDLE hParentDB = jdhandle_to_dhandle(_hParentDB);
	NOTEID parentNoteID = (NOTEID)_parentNoteID;
	DWORD flags = (DWORD)_flags;

	NCheck(env, FolderMove(hDataDB, hViewDB, folderNoteID, hParentDB, parentNoteID, flags));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_FolderDocAdd(
	JNIEnv *env, jobject _this, jlong _hDataDB, jlong _hViewDB, jint _folderNoteID, jlong _hTable, jint _flags)
{
	DHANDLE hDataDB = jdhandle_to_dhandle(_hDataDB);
	DHANDLE hViewDB = jdhandle_to_dhandle(_hViewDB);
	NOTEID folderNoteID = (NOTEID)_folderNoteID;
	DHANDLE hTable = jdhandle_to_dhandle(_hTable);
	DWORD flags = (DWORD)_flags;

	NCheck(env, FolderDocAdd(hDataDB, hViewDB, folderNoteID, hTable, flags));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_FolderDocRemove(
	JNIEnv *env, jobject _this, jlong _hDataDB, jlong _hViewDB, jint _folderNoteID, jlong _hTable, jint _flags)
{
	DHANDLE hDataDB = jdhandle_to_dhandle(_hDataDB);
	DHANDLE hViewDB = jdhandle_to_dhandle(_hViewDB);
	NOTEID folderNoteID = (NOTEID)_folderNoteID;
	DHANDLE hTable = jdhandle_to_dhandle(_hTable);
	DWORD flags = (DWORD)_flags;

	NCheck(env, FolderDocRemove(hDataDB, hViewDB, folderNoteID, hTable, flags));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_FolderDocRemoveAll(
	JNIEnv *env, jobject _this, jlong _hDataDB, jlong _hViewDB, jint _folderNoteID, jint _flags)
{
	DHANDLE hDataDB = jdhandle_to_dhandle(_hDataDB);
	DHANDLE hViewDB = jdhandle_to_dhandle(_hViewDB);
	NOTEID folderNoteID = (NOTEID)_folderNoteID;
	DWORD flags = (DWORD)_flags;

	NCheck(env, FolderDocRemoveAll(hDataDB, hViewDB, folderNoteID, flags));
}

extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_FolderDocCount(
	JNIEnv *env, jobject _this, jlong _hDataDB, jlong _hViewDB, jint _folderNoteID, jint _flags)
{
	DHANDLE hDataDB = jdhandle_to_dhandle(_hDataDB);
	DHANDLE hViewDB = jdhandle_to_dhandle(_hViewDB);
	NOTEID folderNoteID = (NOTEID)_folderNoteID;
	DWORD flags = (DWORD)_flags;
	DWORD result;

	if (!NCheck(env, FolderDocCount(hDataDB, hViewDB, folderNoteID, flags, &result))) {
		return 0;
	}
	return result;
}