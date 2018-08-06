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


extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NIFFindView(
	JNIEnv *env, jobject _this, jlong hDb, jstring _name
	) {
	
	JStringLMBCS name(env, _name);
	NOTEID result;

	if (!NCheck(env, NIFFindView(jdhandle_to_dhandle(hDb), name.getLMBCS(), &result))) {
		return 0;
	}
	return result;
}

extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NIFFindDesignNoteExt(
	JNIEnv *env, jobject _this, jdhandle _hDb, jstring _name, jshort _class, jstring _flagsPattern, jint _options
	) {
	DBHANDLE handle = jdhandle_to_dhandle(_hDb);
	JStringLMBCS name(env, _name);
	WORD Class = (WORD)_class;
	JStringLMBCS flagsPattern(env, _flagsPattern);
	DWORD options = (DWORD)_options;
	NOTEID result;

	if (!NCheck(env, NIFFindDesignNoteExt(handle, name.getLMBCS(), Class, flagsPattern.getLMBCS(), &result, options))) {
		return 0;
	}
	return result;
}

extern "C" JNIEXPORT jshort JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NIFOpenCollection(
	JNIEnv *env, jobject _this, jlong _hViewDB, jlong _hDataDB, jint _ViewNoteID, jshort _OpenFlags, jlong _hUnreadList,
	jlong _rethViewNote, jobject _viewUNID, jlong _rethCollapsedList, jlong _rethSelectedList
	) {

	HCOLLECTION rethCollection;

	DBHANDLE hViewDB = jdhandle_to_dhandle(_hViewDB);
	DBHANDLE hDataDB = jdhandle_to_dhandle(_hDataDB);
	NOTEID ViewNoteID = (NOTEID)_ViewNoteID;
	WORD OpenFlags = (WORD)_OpenFlags;
	DHANDLE hUnreadList = jdhandle_to_dhandle(_hUnreadList);
	NOTEHANDLE *rethViewNote = (NOTEHANDLE*)_rethViewNote;
	UNID* retViewUNID = 0;
	if (_viewUNID != NULL) {
		Struct<UNIVERSALNOTEID> viewUNID(env, _viewUNID);
		retViewUNID = viewUNID;
	}
	DHANDLE *rethCollapsedList = (DHANDLE*)_rethCollapsedList;
	DHANDLE *rethSelectedList = (DHANDLE*)_rethSelectedList;
	

	if (!NCheck(env, NIFOpenCollection(hViewDB, hDataDB, ViewNoteID, OpenFlags, hUnreadList, &rethCollection, rethViewNote, retViewUNID, rethCollapsedList, rethSelectedList))) {
		return 0;
	}
	return rethCollection;
}

extern "C" JNIEXPORT jshort JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NIFOpenCollectionWithUserNameList(
	JNIEnv *env, jobject _this, jlong _hViewDB, jlong _hDataDB, jint _ViewNoteID, jshort _OpenFlags, jlong _hUnreadList,
	jlong _rethViewNote, jobject _viewUNID, jlong _rethCollapsedList, jlong _rethSelectedList, jlong _nameList
	) {

	HCOLLECTION rethCollection;

	DBHANDLE hViewDB = jdhandle_to_dhandle(_hViewDB);
	DBHANDLE hDataDB = jdhandle_to_dhandle(_hDataDB);
	NOTEID ViewNoteID = (NOTEID)_ViewNoteID;
	WORD OpenFlags = (WORD)_OpenFlags;
	DHANDLE hUnreadList = jdhandle_to_dhandle(_hUnreadList);
	NOTEHANDLE *rethViewNote = (NOTEHANDLE*)_rethViewNote;
	UNID* retViewUNID = 0;
	if (_viewUNID != NULL) {
		Struct<UNIVERSALNOTEID> viewUNID(env, _viewUNID);
		retViewUNID = viewUNID;
	}
	DHANDLE *rethCollapsedList = (DHANDLE*)_rethCollapsedList;
	DHANDLE *rethSelectedList = (DHANDLE*)_rethSelectedList;
	DHANDLE nameList = jdhandle_to_dhandle(_nameList);


	if (!NCheck(env, NIFOpenCollectionWithUserNameList(hViewDB, hDataDB, ViewNoteID, OpenFlags, hUnreadList, &rethCollection, rethViewNote, retViewUNID, rethCollapsedList, rethSelectedList, nameList))) {
		return 0;
	}
	return rethCollection;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NIFCloseCollection(
	JNIEnv *env, jobject _this, jshort hCollection
	) {

	if (!NCheck(env, NIFCloseCollection((HCOLLECTION)hCollection))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NIFFindByKey(
	JNIEnv *env, jobject _this, jshort _hCollection, jobject key, jshort _FindFlags, jobject _indexPos, jobject _retNumMatches
	) {

	HCOLLECTION hCollection = (HCOLLECTION)_hCollection;
	Struct<ITEM_TABLE> KeyBuffer(env, key);
	WORD FindFlags = (WORD)_FindFlags;
	COLLECTIONPOSITION *retIndexPos = 0;
	if (_indexPos != NULL) {
		Struct<COLLECTIONPOSITION> indexPos(env, _indexPos);
		retIndexPos = indexPos;
	}
	IntRef<DWORD> retNumMatches(env, _retNumMatches);

	if (!NCheck(env, NIFFindByKey(hCollection, KeyBuffer, FindFlags, retIndexPos, retNumMatches))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NIFFindByKeyExtended2(
	JNIEnv *env, jobject _this, jshort _hCollection, jobject key, jint _FindFlags, jint _ReturnFlags, jobject _indexPos, jobject _retNumMatches,
	jobject _retSignalFlags, jobject _rethBuffer, jobject _retSequence
	) {

	HCOLLECTION hCollection = (HCOLLECTION)_hCollection;
	Struct<ITEM_TABLE> KeyBuffer(env, key);
	DWORD FindFlags = (DWORD)_FindFlags;
	DWORD ReturnFlags = (DWORD)_ReturnFlags;
	COLLECTIONPOSITION *retIndexPos = 0;
	if (_indexPos != NULL) {
		Struct<COLLECTIONPOSITION> indexPos(env, _indexPos);
		retIndexPos = indexPos;
	}
	IntRef<DWORD> retNumMatches(env, _retNumMatches);
	IntRef<WORD> retSignalFlags(env, _retSignalFlags);
	IntRef<DHANDLE> rethBuffer(env, _rethBuffer);
	IntRef<DWORD> retSequence(env, _retSequence);

	if (!NCheck(env, NIFFindByKeyExtended2(hCollection, KeyBuffer, FindFlags, ReturnFlags, retIndexPos, retNumMatches, retSignalFlags, rethBuffer, retSequence))) {
		return;
	}
}

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NIFReadEntries(
	JNIEnv *env, jobject _this, jshort _hCollection, jobject _IndexPos, jshort _SkipNavigator, jint _SkipCount, jshort _ReturnNavigator,
	jint _ReturnCount, jint _ReturnMask, jobject _retBufferLength, jobject _retNumEntriesSkipped, jobject _retNumEntriesReturned,
	jobject _retSignalFlags
	) {

	HCOLLECTION hCollection = (HCOLLECTION)_hCollection;
	COLLECTIONPOSITION *IndexPos = 0;
	if (_IndexPos != NULL) {
		Struct<COLLECTIONPOSITION> indexPosStruct(env, _IndexPos);
		IndexPos = indexPosStruct;
	}
	WORD SkipNavigator = (WORD)_SkipNavigator;
	DWORD SkipCount = (DWORD)_SkipCount;
	WORD ReturnNavigator = (WORD)_ReturnNavigator;
	DWORD ReturnCount = (DWORD)_ReturnCount;
	DWORD ReturnMask = (DWORD)_ReturnMask;
	DHANDLE rethBuffer;
	ShortRef<WORD> retBufferLength(env, _retBufferLength);
	IntRef<DWORD> retNumEntriesSkipped(env, _retNumEntriesSkipped);
	IntRef<DWORD> retNumEntriesReturned(env, _retNumEntriesReturned);
	ShortRef<WORD> retSignalFlags(env, _retSignalFlags);

	if (!NCheck(env, NIFReadEntries(hCollection, IndexPos, SkipNavigator, SkipCount, ReturnNavigator, ReturnCount, ReturnMask, &rethBuffer, retBufferLength, retNumEntriesSkipped, retNumEntriesReturned, retSignalFlags))) {
		return 0;
	}

	return dhandle_to_jdhandle(rethBuffer);
}

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NIFGetCollectionData(
	JNIEnv *env, jobject _this, jshort hCollection
	) {
	DHANDLE rethCollData;
	if (!NCheck(env, NIFGetCollectionData((HCOLLECTION)hCollection, &rethCollData))) {
		return 0;
	}
	return dhandle_to_jdhandle(rethCollData);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NIFUpdateCollection(
	JNIEnv *env, jobject _this, jshort hCollection
	) {

	NCheck(env, NIFUpdateCollection((HCOLLECTION)hCollection));
}