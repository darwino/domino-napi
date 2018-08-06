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


extern "C" JNIEXPORT jdhandle JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbOpen(
	JNIEnv *env, jobject _this, jstring _dbPath)
{
	DHANDLE hDb;
	JStringLMBCS dbPath(env,_dbPath);
	if(!NCheck(env,NSFDbOpen(dbPath.getLMBCS(),&hDb))) {
		return 0;
	}
	return dhandle_to_jdhandle(hDb);
}

extern "C" JNIEXPORT jdhandle JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbOpenExtended(
	JNIEnv *env, jobject _this, jstring _dbPath, jshort Options, jlong hNames, jobject _ModifiedTime, jobject _retDataModified, jobject _retNonDataModified)
{
	DHANDLE hDb;
	JStringLMBCS dbPath(env,_dbPath);
	Struct<TIMEDATE> ModifiedTime(env, _ModifiedTime);
	Struct<TIMEDATE> retDataModified(env, _retDataModified);
	Struct<TIMEDATE> retNonDataModified(env, _retNonDataModified);
	if(!NCheck(env,NSFDbOpenExtended(dbPath.getLMBCS(), (WORD)Options, (DHANDLE)hNames, ModifiedTime, &hDb, retDataModified, retNonDataModified))) {
		return 0;
	}
	return dhandle_to_jdhandle(hDb);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbClose(
	JNIEnv *env, jobject _this, jlong hDb)
{
	if(!NCheck(env,NSFDbClose(jdhandle_to_dhandle(hDb)))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbModifiedTime(
	JNIEnv *env, jobject _this, jlong _hDb, jobject _retDataModified, jobject _retNonDataModified)
{
	DBHANDLE hDb = jdhandle_to_dhandle(_hDb);
	Struct<TIMEDATE> retDataModified(env, _retDataModified);
	Struct<TIMEDATE> retNonDataModified(env, _retNonDataModified);
	
	NCheck(env, NSFDbModifiedTime(hDb, retDataModified, retNonDataModified));
}

extern "C" JNIEXPORT jdhandle JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFGetChangedDBs(
	JNIEnv *env, jobject _this, jstring _serverName, jobject _sinceTime, jpointer _changesSize, jobject _nextSinceTime)
{
	JStringLMBCS ServerName(env, _serverName);
	Struct<TIMEDATE> SinceTime(env, _sinceTime);
	DWORD *ChangesSize = (DWORD*)DEREF_PTR(_changesSize);
	DHANDLE hChanges;
	Struct<TIMEDATE> NextSinceTime(env, _nextSinceTime);

	if (NCheck(env, NSFGetChangedDBs((char*)ServerName.getLMBCS(), SinceTime, ChangesSize, &hChanges, NextSinceTime))) {
		return dhandle_to_jdhandle(hChanges);
	} else {
		return 0;
	}
}

extern "C" JNIEXPORT jdhandle JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbGetModifiedNoteTable(
	JNIEnv *env, jobject _this, jlong hDb, jshort noteClassMask, jobject _since, jobject _until)
{
	Struct<TIMEDATE> since(env,_since);
	Struct<TIMEDATE> until(env,_until);
	DHANDLE retTable=0;


	STATUS status = NSFDbGetModifiedNoteTable(jdhandle_to_dhandle(hDb),(WORD)noteClassMask,since,until,&retTable);
	if(status==ERR_NO_MODIFIED_NOTES) {
		// Special case when there is no entries
		// Is it passing an IDTable?
		if(retTable) {
			IDDestroyTable(retTable);
		}
		return 0;
	}
	if(!NCheck(env,status) ) {
        return 0L;
	}
	
	return dhandle_to_jdhandle(retTable);
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbGetNoteInfo(
	JNIEnv *env, jobject _this, jlong hDb, jint noteID, jobject _retOID, jobject _retModified, jobject _retNoteClass)
{
	Struct<OID> retOID(env,_retOID);
	Struct<TIMEDATE> retModified(env,_retModified);
	IntRef<WORD> retNoteClass(env,_retNoteClass);
	STATUS status = NSFDbGetNoteInfo(jdhandle_to_dhandle(hDb),(NOTEID)noteID,retOID,retModified,retNoteClass);
	if(status==ERR_NOTE_DELETED) {
		return false;
	}
	if(!NCheck(env,status)) {
		return false;
	}
	return true;
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbGetNoteInfoExt(
	JNIEnv *env, jobject _this, jlong hDb, jint noteID, jobject _retOID, jobject _retModified, jobject _retNoteClass, jobject _retAddedToFile, jobject _retResponseCount, jobject _retParentNoteID)
{
	Struct<OID> retOID(env,_retOID);
	Struct<TIMEDATE> retModified(env,_retModified);
	IntRef<WORD> retNoteClass(env,_retNoteClass);
	Struct<TIMEDATE> retAddedToFile(env,_retAddedToFile);
	IntRef<WORD> retResponseCount(env,_retResponseCount);
	IntRef<NOTEID> retParentNoteID(env,_retParentNoteID);
	STATUS status = NSFDbGetNoteInfoExt(jdhandle_to_dhandle(hDb),(NOTEID)noteID,retOID,retModified,retNoteClass,retAddedToFile,retResponseCount,retParentNoteID);
	if(status==ERR_NOTE_DELETED) {
		return false;
	}
	if(!NCheck(env,status)) {
		return false;
	}
	return true;
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbGetNoteInfoByUNID(
	JNIEnv *env, jobject _this, jlong hDb, jobject _unid, jobject _retNoteID, jobject _retOID, jobject _retModified, jobject _retNoteClass)
{
	Struct<UNID> unid(env, _unid);
	IntRef<NOTEID> retNoteID(env, _retNoteID);
	Struct<OID> retOID(env, _retOID);
	Struct<TIMEDATE> retModified(env, _retModified);
	IntRef<WORD> retNoteClass(env, _retNoteClass);
	STATUS status = NSFDbGetNoteInfoByUNID(jdhandle_to_dhandle(hDb), unid, retNoteID, retOID, retModified, retNoteClass);
	if (status == ERR_NOTE_DELETED) {
		return false;
	}
	if (!NCheck(env, status)) {
		return false;
	}
	return true;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbGetMultNoteInfo(
	JNIEnv *env, jobject _this, jlong hDb, jshort count, jshort options, jlong hInBuf, jobject _retSize, jobject _rethOutBuf)
{
	IntRef<DWORD> retSize(env, _retSize);
	LongRef<DHANDLE> rethOutBuf(env, _rethOutBuf);

	if (!NCheck(env, NSFDbGetMultNoteInfo((DBHANDLE)hDb, (WORD)count, (WORD)options, (DHANDLE)hInBuf, retSize, rethOutBuf))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbGetMultNoteInfoByUNID(
	JNIEnv *env, jobject _this, jlong hDb, jshort count, jshort options, jlong hInBuf, jobject _retSize, jobject _rethOutBuf)
{
	IntRef<DWORD> retSize(env, _retSize);
	LongRef<DHANDLE> rethOutBuf(env, _rethOutBuf);

	if (!NCheck(env, NSFDbGetMultNoteInfoByUNID((DBHANDLE)hDb, (WORD)count, (WORD)options, (DHANDLE)hInBuf, retSize, rethOutBuf))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbCreate(
	JNIEnv *env, jobject _this, jstring _pathName, jshort DbClass, jboolean ForceCreation)
{
	JStringLMBCS PathName(env, _pathName);
	if (!NCheck(env, NSFDbCreate(PathName.getLMBCS(), (USHORT)DbClass, (BOOL)ForceCreation))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbDelete(
	JNIEnv *env, jobject _this, jstring _pathName)
{
	JStringLMBCS PathName(env, _pathName);
	if (!NCheck(env, NSFDbDelete(PathName.getLMBCS()))) {
		return;
	}
}

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFBuildNamesList(
	JNIEnv *env, jobject _this, jstring _userName, jint dwFlags)
{
	DHANDLE rethNamesList;
	JStringLMBCS UserName(env, _userName);
	if (!NCheck(env, NSFBuildNamesList((char*)UserName.getLMBCS(), (DWORD)dwFlags, &rethNamesList))) {
		return 0;
	}
	return dhandle_to_jdhandle(rethNamesList);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbPathGet(
	JNIEnv *env, jobject _this, jlong _hDb, jlong _retCanonicalPathName, jlong _retExpandedPathName)
{
	DHANDLE hDB = jdhandle_to_dhandle(_hDb);
	char *retCanonicalPathName = (char*)DEREF_PTR(_retCanonicalPathName);
	char *retExpandedPathName = (char*)DEREF_PTR(_retExpandedPathName);
	NCheck(env, NSFDbPathGet(hDB, retCanonicalPathName, retExpandedPathName));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbReplicaInfoGet(
	JNIEnv *env, jobject _this, jlong _hDb, jobject _retReplicaInfo)
{
	DHANDLE hDB = jdhandle_to_dhandle(_hDb);
	Struct<DBREPLICAINFO> retReplicaInfo(env, _retReplicaInfo);
	NCheck(env, NSFDbReplicaInfoGet(hDB, retReplicaInfo));
}

extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbUserNameGet(
	JNIEnv *env, jobject _this, jlong _hDb)
{
	DHANDLE hDB = jdhandle_to_dhandle(_hDb);
	char retBuffer[MAXUSERNAME + 1];

	if (!NCheck(env, NSFDbUserNameGet(hDB, retBuffer, MAXUSERNAME))) {
		return NULL;
	}
	return LMBCStoJavaString(env, retBuffer);
}

extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbGetOptions(
	JNIEnv *env, jobject _this, jlong _hDb)
{
	DHANDLE hDB = jdhandle_to_dhandle(_hDb);
	DWORD options;
	
	if (!NCheck(env, NSFDbGetOptions(hDB, &options))) {
		return NULL;
	}
	return (jint)options;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbSetOptions(
	JNIEnv *env, jobject _this, jlong _hDb, jint options, jint mask)
{
	DHANDLE hDB = jdhandle_to_dhandle(_hDb);

	NCheck(env, NSFDbSetOptions(hDB, (DWORD)options, (DWORD)mask));
}

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbReadACL(
	JNIEnv *env, jobject _this, jlong _hDb)
{
	DHANDLE hDB = jdhandle_to_dhandle(_hDb);
	DHANDLE hACL;

	if (!NCheck(env, NSFDbReadACL(hDB, &hACL))) {
		return 0;
	}
	return dhandle_to_jdhandle(hACL);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbStoreACL(
	JNIEnv *env, jobject _this, jlong _hDb, jlong _hACL, jint ObjectID, jshort Method)
{
	DHANDLE hDB = jdhandle_to_dhandle(_hDb);
	DHANDLE hACL = jdhandle_to_dhandle(_hACL);

	NCheck(env, NSFDbStoreACL(hDB, hACL, (DWORD)ObjectID, (WORD)Method));
}

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFFolderGetIDTable(
	JNIEnv *env, jobject _this, jlong _hViewDB, jlong _hDataDB, jint _viewNoteID, jint _flags)
{
	DHANDLE hViewDB = jdhandle_to_dhandle(_hViewDB);
	DHANDLE hDataDB = jdhandle_to_dhandle(_hDataDB);
	NOTEID viewNoteID = (NOTEID)_viewNoteID;
	DWORD flags = (DWORD)_flags;
	DHANDLE hTable;

	if (!NCheck(env, NSFFolderGetIDTable(hViewDB, hDataDB, viewNoteID, flags, &hTable))) {
		return 0;
	}
	return dhandle_to_jdhandle(hTable);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFGetFolderChanges(
	JNIEnv *env, jobject _this, jlong _hViewDB, jlong _hDataDB, jint _viewNoteID, jobject _since, jint _flags, jpointer _addedNoteTable, jpointer _removedNoteTable)
{
	DHANDLE hViewDB = jdhandle_to_dhandle(_hViewDB);
	DHANDLE hDataDB = jdhandle_to_dhandle(_hDataDB);
	NOTEID viewNoteID = (NOTEID)_viewNoteID;
	Struct<TIMEDATE> since(env, _since);
	DWORD flags = (DWORD)_flags;
	DHANDLE *addedNoteTable = (DHANDLE*)DEREF_PTR(_addedNoteTable);
	DHANDLE *removedNoteTable = (DHANDLE*)DEREF_PTR(_removedNoteTable);

	NCheck(env, NSFGetFolderChanges(hViewDB, hDataDB, viewNoteID, since, flags, addedNoteTable, removedNoteTable));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFGetAllFolderChanges(
	JNIEnv *env, jobject _this, jlong _hViewDB, jlong _hDataDB, jobject _since, jint _flags, jobject _callback, jobject _until)
{
	DHANDLE hViewDB = jdhandle_to_dhandle(_hViewDB);
	DHANDLE hDataDB = jdhandle_to_dhandle(_hDataDB);
	Struct<TIMEDATE> since(env, _since);
	DWORD flags = (DWORD)_flags;
	J_NSFGETALLFOLDERCHANGESCALLBACK callback(env, _callback);
	Struct<TIMEDATE> until(env, _until);

	STATUS result = NSFGetAllFolderChanges(hViewDB, hDataDB, since, flags, callback, &callback, until);
	// Allow the use of ERR_CANCEL as a signal from the proc to cancel processing
	ERR(result) == ERR_CANCEL || NCheck(env, result);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbAccessGet(
	JNIEnv *env, jobject _this, jdhandle _hDB, jpointer _retAccessLevel, jpointer _retAccessFlag)
{
	DBHANDLE hDB = jdhandle_to_dhandle(_hDB);
	WORD *retAccessLevel = (WORD*)DEREF_PTR(_retAccessLevel);
	WORD *retAccessFlag = (WORD*)DEREF_PTR(_retAccessFlag);

	NSFDbAccessGet(hDB, retAccessLevel, retAccessFlag);
}

extern "C" JNIEXPORT jpointer JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbInfoGet(
	JNIEnv *env, jobject _this, jdhandle _hDB)
{
	DBHANDLE hDB = jdhandle_to_dhandle(_hDB);
	char* buffer = (char*)malloc(NSF_INFO_SIZE);

	if (NCheck(env, NSFDbInfoGet(hDB, buffer))) {
		return REF_PTR(buffer);
	}
	else {
		return NULL;
	}
}
extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbInfoParse(
	JNIEnv *env, jobject _this, jpointer _info, jshort _what)
{
	char *info = (char*)DEREF_PTR(_info);
	WORD what = (WORD)_what;
	char buffer[NSF_INFO_SIZE];

	NSFDbInfoParse(info, what, buffer, NSF_INFO_SIZE - 1);
	return LMBCStoJavaString(env, buffer);
}

extern "C" JNIEXPORT jshort JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFDbClassGet(
	JNIEnv *env, jobject _this, jdhandle _hDB)
{
	DHANDLE hDB = jdhandle_to_dhandle(_hDB);
	WORD retClass;
	if (NCheck(env, NSFDbClassGet(hDB, &retClass))) {
		return (jshort)retClass;
	} else {
		return 0;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFTransactionBegin(
		JNIEnv *env, jobject _this, jdhandle _hDB, jint _flags)
{
	DHANDLE hDB = jdhandle_to_dhandle(_hDB);
	DWORD flags = (DWORD)_flags;
	NCheck(env, NSFTransactionBegin(hDB, flags));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFTransactionCommit(
		JNIEnv *env, jobject _this, jdhandle _hDB, jint _flags)
{
	DHANDLE hDB = jdhandle_to_dhandle(_hDB);
	DWORD flags = (DWORD)_flags;
	NCheck(env, NSFTransactionCommit(hDB, flags));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFTransactionRollback(
		JNIEnv *env, jobject _this, jdhandle _hDB)
{
	DHANDLE hDB = jdhandle_to_dhandle(_hDB);
	NCheck(env, NSFTransactionRollback(hDB));
}
