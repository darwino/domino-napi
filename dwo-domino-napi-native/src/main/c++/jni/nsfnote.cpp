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


extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteOpen(
	JNIEnv *env, jobject _this, jlong hDb, jint noteID, jshort flags)
{
	DHANDLE hNote;	
	if (!NCheck(env, NSFNoteOpen(jdhandle_to_dhandle(hDb), noteID, (WORD)flags, &hNote))) {
		return 0;
	}
	return dhandle_to_jdhandle(hNote);
}

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteOpenExt(
	JNIEnv *env, jobject _this, jlong hDb, jint noteID, jint flags)
{
	DHANDLE hNote;
	if (!NCheck(env, NSFNoteOpenExt(jdhandle_to_dhandle(hDb), noteID, (DWORD)flags, &hNote))) {
		return 0;
	}
	return dhandle_to_jdhandle(hNote);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteClose(
	JNIEnv *env, jobject _this, jlong hNote)
{
	if (!NCheck(env, NSFNoteClose(jdhandle_to_dhandle(hNote)))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteDeleteExtended(
	JNIEnv *env, jobject _this, jlong hDb, jint noteID, jint flags)
{
	NCheck(env, NSFNoteDeleteExtended(jdhandle_to_dhandle(hDb), noteID, (DWORD)flags));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteCheck(
	JNIEnv *env, jobject _this, jlong hNote)
{
	if (!NCheck(env, NSFNoteCheck(jdhandle_to_dhandle(hNote)))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteGetInfo(
	JNIEnv *env, jobject _this, jlong hNote, jshort noteMember, jlong valuePtr)
{
	// This does not return a status value
	NSFNoteGetInfo(jdhandle_to_dhandle(hNote), (WORD)noteMember, (void*)valuePtr);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteSetInfo(
	JNIEnv *env, jobject _this, jlong hNote, jshort noteMember, jlong valuePtr)
{
	// This does not return a status value
	NSFNoteSetInfo(jdhandle_to_dhandle(hNote), (WORD)noteMember, (void*)valuePtr);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteUpdate(
	JNIEnv *env, jobject _this, jlong hNote, jshort updateFlags)
{
	if (!NCheck(env, NSFNoteUpdate(jdhandle_to_dhandle(hNote), (WORD)updateFlags))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteUpdateExtended(
	JNIEnv *env, jobject _this, jlong hNote, jint updateFlags)
{
	if (!NCheck(env, NSFNoteUpdateExtended(jdhandle_to_dhandle(hNote), (DWORD)updateFlags))) {
		return;
	}
}

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteCreate(
	JNIEnv *env, jobject _this, jlong hDb)
{
	NOTEHANDLE hNote;
	if (!NCheck(env, NSFNoteCreate(jdhandle_to_dhandle(hDb), &hNote))) {
		return 0;
	}
	return (jlong)hNote;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFItemDelete(
	JNIEnv *env, jobject _this, jlong hNote, jstring _itemName)
{
	JStringLMBCS itemName(env, _itemName);
	const char *itemNameC = itemName.getLMBCS();
	WORD nameLen = SIZET_TO_WORD(strlen(itemNameC));


	if (!NCheck(env, NSFItemDelete(jdhandle_to_dhandle(hNote), itemNameC, nameLen))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFItemDeleteByBLOCKID(
	JNIEnv *env, jobject _this, jlong hNote, jobject _itemBlockId)
{
	Struct<BLOCKID> itemBlockId(env, _itemBlockId);

	NCheck(env, NSFItemDeleteByBLOCKID(jdhandle_to_dhandle(hNote), itemBlockId));
}

// This method hides the details of the nameLen field, as the Java string's len may differ from its LMBCS version
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFItemInfo(
	JNIEnv *env, jobject _this, jlong _hNote, jstring _itemName, jobject _itemBlockId,
	jobject _valueDataType, jobject _valueBlockId, jobject _valueLen)
{
	NOTEHANDLE hNote = jdhandle_to_dhandle(_hNote);

	Struct<BLOCKID> itemBlockId(env, _itemBlockId);
	ShortRef<WORD> valueDataType(env, _valueDataType);
	Struct<BLOCKID> valueBlockId(env, _valueBlockId);
	IntRef<DWORD> valueLen(env, _valueLen);

	if (_itemName != NULL) {
		JStringLMBCS itemName(env, _itemName);
		const char *itemNameC = itemName.getLMBCS();
		WORD nameLen = SIZET_TO_WORD(itemName.length());

		NCheck(env, NSFItemInfo(hNote, itemNameC, nameLen, itemBlockId, valueDataType, valueBlockId, valueLen), ERR_ITEM_NOT_FOUND);
	} else {
		NCheck(env, NSFItemInfo(hNote, NULL, 0, itemBlockId, valueDataType, valueBlockId, valueLen), ERR_ITEM_NOT_FOUND);
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFItemInfoNext(
	JNIEnv *env, jobject _this, jlong _hNote, jobject _nextItem, jstring _itemName, jobject _itemBlockId,
	jobject _valueDataType, jobject _valueBlockId, jobject _valueLen)
{
	NOTEHANDLE hNote = jdhandle_to_dhandle(_hNote);
	Struct<BLOCKID> NextItem(env, _nextItem);

	Struct<BLOCKID> item_blockid(env, _itemBlockId);
	ShortRef<WORD> value_type(env, _valueDataType);
	Struct<BLOCKID> value_blockid(env, _valueBlockId);
	IntRef<DWORD> value_len(env, _valueLen);

	if (_itemName != NULL) {
		JStringLMBCS itemName(env, _itemName);
		const char *itemNameC = itemName.getLMBCS();
		WORD nameLen = SIZET_TO_WORD(itemName.length());

		NCheck(env, NSFItemInfoNext(hNote, NextItem, itemNameC, nameLen, item_blockid, value_type, value_blockid, value_len), ERR_ITEM_NOT_FOUND);
	} else {
		NCheck(env, NSFItemInfoNext(hNote, NextItem, NULL, 0, item_blockid, value_type, value_blockid, value_len), ERR_ITEM_NOT_FOUND);
	}

}

extern "C" JNIEXPORT jboolean JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFItemIsPresent(
	JNIEnv *env, jobject _this, jlong hNote, jstring _itemName)
{
	JStringLMBCS itemName(env, _itemName);
	return (jboolean)NSFItemIsPresent(jdhandle_to_dhandle(hNote), itemName.getLMBCS(), itemName.length());
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFItemSetTime(
	JNIEnv *env, jobject _this, jlong hNote, jstring _itemName, jobject _timeDate)
{
	JStringLMBCS itemName(env, _itemName);
	Struct<TIMEDATE> timeDate(env, _timeDate);
	
	if (!NCheck(env, NSFItemSetTime(jdhandle_to_dhandle(hNote), itemName.getLMBCS(), timeDate))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFItemSetText(
	JNIEnv *env, jobject _this, jlong hNote, jstring _itemName, jstring _value)
{
	JStringLMBCS itemName(env, _itemName);
	const char *itemNameC = itemName.getLMBCS();
	JStringLMBCS value(env, _value);
	const char *valueC = value.getLMBCS();
	WORD valueLen = SIZET_TO_WORD(value.length());

	if (!NCheck(env, NSFItemSetText(jdhandle_to_dhandle(hNote), itemNameC, valueC, valueLen))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFItemSetTextSummary(
	JNIEnv *env, jobject _this, jlong hNote, jstring _itemName, jstring _value, jboolean _summary)
{
	JStringLMBCS itemName(env, _itemName);
	JStringLMBCS value(env, _value);
	WORD valueLen = SIZET_TO_WORD(value.length());
	BOOL summary = (BOOL)_summary;

	if (!NCheck(env, NSFItemSetTextSummary(jdhandle_to_dhandle(hNote), itemName.getLMBCS(), value.getLMBCS(), valueLen, summary))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFItemSetNumber(
	JNIEnv *env, jobject _this, jdhandle hNote, jstring _itemName, jdouble _value)
{
	JStringLMBCS itemName(env, _itemName);
	NUMBER value = double_to_number(_value);

	if (!NCheck(env, NSFItemSetNumber(jdhandle_to_dhandle(hNote), itemName.getLMBCS(), &value))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFItemAppend(
	JNIEnv *env, jobject _this, jdhandle hNote, jshort itemFlags, jstring _itemName, jshort itemType, jpointer _valuePtr, jint valueLen)
{
	JStringLMBCS itemName(env, _itemName);
	const char *itemNameC = itemName.getLMBCS();
	WORD nameLen = (WORD)itemName.length();
	const void *valuePtr = (const void*)DEREF_PTR(_valuePtr);

	if (!NCheck(env, NSFItemAppend(jdhandle_to_dhandle(hNote), (WORD)itemFlags, itemNameC, nameLen, (WORD)itemType, valuePtr, (DWORD)valueLen))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFItemAppendByBLOCKID(
	JNIEnv *env, jobject _this, jdhandle hNote, jshort itemFlags, jstring _itemName, jobject _value_bid, jint valueLen, jobject _item_bid_ptr)
{
	JStringLMBCS itemName(env, _itemName);
	const char *itemNameC = itemName.getLMBCS();
	WORD nameLen = (WORD)itemName.length();
	Struct<BLOCKID> value_bid(env, _value_bid);
	Struct<BLOCKID> item_bid_ptr(env, _item_bid_ptr);

	NCheck(env, NSFItemAppendByBLOCKID(jdhandle_to_dhandle(hNote), (WORD)itemFlags, itemNameC, nameLen, value_bid, (DWORD)valueLen, item_bid_ptr));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteAttachFile(
	JNIEnv *env, jobject _this, jdhandle hNote, jstring _itemName, jstring _fileName, jstring _origPathName, jshort encodingType)
{
	JStringLMBCS itemName(env, _itemName);
	WORD itemNameLength = (WORD)itemName.length();
	JStringLMBCS fileName(env, _fileName);
	JStringLMBCS origPathName(env, _origPathName);

	if (!NCheck(env, NSFNoteAttachFile(jdhandle_to_dhandle(hNote), itemName.getLMBCS(), itemNameLength, fileName.getLMBCS(), origPathName.getLMBCS(), (WORD)encodingType))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteDetachFile(
	JNIEnv *env, jobject _this, jdhandle hNote, jobject _itemBlockId)
{
	Struct<BLOCKID> itemBlockId(env, _itemBlockId);
	if (!NCheck(env, NSFNoteDetachFile(jdhandle_to_dhandle(hNote), itemBlockId))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteCipherDecrypt(
	JNIEnv *env, jobject _this, jdhandle _hNote, jlong _hKFC, jint _decryptFlags, jpointer _rethCipherForAttachments)
{
	DHANDLE hNote = jdhandle_to_dhandle(_hNote);
	KFHANDLE hKFC = (KFHANDLE)_hKFC;
	DWORD DecryptFlags = (DWORD)_decryptFlags;
	CIPHERHANDLE* rethCipherForAttachments = (CIPHERHANDLE*)_rethCipherForAttachments;

	NCheck(env, NSFNoteCipherDecrypt(hNote, hKFC, DecryptFlags, rethCipherForAttachments, 0, NULL));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteCipherExtractFile(
	JNIEnv *env, jobject _this, jdhandle hNote, jobject _bhItem, jint extractFlags, jint hDecryptionCipher, jstring _fileName)
{
	Struct<BLOCKID> bhItem(env, _bhItem);
	JStringLMBCS fileName(env, _fileName);

	NCheck(env, NSFNoteCipherExtractFile(jdhandle_to_dhandle(hNote), bhItem, (DWORD)extractFlags, (CIPHERHANDLE)hDecryptionCipher, fileName.getLMBCS(), 0, NULL));
}

// This method skips RoutineParameter because the callback uses it internally
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFItemScan(
	JNIEnv *env, jobject _this, jdhandle hNote, jobject _actionRoutine)
{
	J_NSFITEMSCANPROC actionRoutine(env, _actionRoutine);

	STATUS result = NSFItemScan(jdhandle_to_dhandle(hNote), actionRoutine, &actionRoutine);
	// Allow the use of ERR_CANCEL as a signal from the proc to cancel processing
	ERR(result) == ERR_CANCEL || NCheck(env, result);
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteHasMIME(
	JNIEnv *env, jobject _this, jlong _hNote)
{
	NOTEHANDLE hNote = jdhandle_to_dhandle(_hNote);

	return (jboolean)NSFNoteHasMIME(hNote);
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteHasObjects(
	JNIEnv *env, jobject _this, jlong _hNote, jobject _firstItemBlockId)
{
	NOTEHANDLE hNote = jdhandle_to_dhandle(_hNote);
	Struct<BLOCKID> firstItemBlockId(env, _firstItemBlockId);

	return (jboolean)NSFNoteHasObjects(hNote, firstItemBlockId);
}

// This method skips pParam because the callback uses it internally
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFNoteCipherExtractWithCallback(
	JNIEnv *env, jobject _this, jlong _hNote, jobject _bhItem, jint _ExtractFlags, jlong _hDecryptionCipher, jobject _pNoteExtractCallback, jint _Reserved, jlong _pReserved)
{
	NOTEHANDLE hNote = jdhandle_to_dhandle(_hNote);
	Struct<BLOCKID> bhItem(env, _bhItem);
	DWORD ExtractFlags = (DWORD)_ExtractFlags;
	CIPHERHANDLE hDecryptionCipher = (CIPHERHANDLE)_hDecryptionCipher;
	J_NOTEEXTRACTCALLBACK noteExtractCallback(env, _pNoteExtractCallback);
	DWORD Reserved = (DWORD)_Reserved;
	void *pReserved = DEREF_PTR(_pReserved);

	STATUS result = NSFNoteCipherExtractWithCallback(hNote, bhItem, ExtractFlags, hDecryptionCipher, noteExtractCallback, &noteExtractCallback, Reserved, pReserved);
	// Allow the use of ERR_CANCEL as a signal from the proc to cancel processing
	ERR(result) == ERR_CANCEL || NCheck(env, result);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFItemQueryEx(
	JNIEnv *env, jobject _this, jlong _hNote, jobject _item_bid, jlong _item_name, jshort _return_buf_len, jobject _name_len_ptr,
	jobject _item_flags_ptr, jobject _value_datatype_ptr, jobject _value_bid_ptr, jobject _value_len_ptr, jobject _retSeqByte,
	jobject _retDupItemID)
{
	NOTEHANDLE hNote = jdhandle_to_dhandle(_hNote);
	Struct<BLOCKID> item_bid(env, _item_bid);
	char *item_name = (char*)DEREF_PTR(_item_name);
	WORD return_buf_len = (WORD)_return_buf_len;
	ShortRef<WORD> name_len_ptr(env, _name_len_ptr);
	ShortRef<WORD> item_flags_ptr(env, _item_flags_ptr);
	ShortRef<WORD> value_datatype_ptr(env, _value_datatype_ptr);
	Struct<BLOCKID> value_bid_ptr(env, _value_bid_ptr);
	IntRef<DWORD> value_len_ptr(env, _value_len_ptr);
	ByteRef<BYTE> retReqByte(env, _retSeqByte);
	ByteRef<BYTE> retDupItemID(env, _retDupItemID);

	NSFItemQueryEx(hNote, item_bid, item_name, return_buf_len, name_len_ptr, item_flags_ptr, value_datatype_ptr, value_bid_ptr, value_len_ptr, retReqByte, retDupItemID);
}

extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFItemConvertToText(
	JNIEnv *env, jobject _this, jlong _note_handle, jstring _item_name, jshort _text_buf_len, jchar _separator)
{
	NOTEHANDLE note_handle = jdhandle_to_dhandle(_note_handle);
	JStringLMBCS itemName(env, _item_name);
	WORD text_buf_len = (WORD)_text_buf_len;
	char* text_buf_ptr = (char*)malloc(text_buf_len * sizeof(char));
	text_buf_ptr[0] = '\0';
	char separator = (char)_separator;

	// Ignoring result size
	NSFItemConvertToText(note_handle, itemName.getLMBCS(), text_buf_ptr, text_buf_len, separator);
	jstring result = LMBCStoJavaString(env, text_buf_ptr);
	free(text_buf_ptr);
	return result;
}

extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFItemGetText(
	JNIEnv *env, jobject _this, jlong _note_handle, jstring _item_name, jshort _textBufLen)
{
	NOTEHANDLE note_handle = jdhandle_to_dhandle(_note_handle);
	JStringLMBCS itemName(env, _item_name);
	WORD bufferSize = (WORD)_textBufLen;
	char* buffer = (char*)malloc(sizeof(char) * bufferSize);
	
	if(NCheck(env, NSFItemGetText(note_handle, itemName.getLMBCS(), buffer, bufferSize))) {
		jstring result = LMBCStoJavaString(env, buffer);
		free(buffer);
		return result;
	} else {
		return NULL;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFProfileDelete(
	JNIEnv *env, jobject _this, jdhandle _hDB, jstring _profileName, jstring _userName)
{
	DBHANDLE hDB = jdhandle_to_dhandle(_hDB);
	JStringLMBCS profileName(env, _profileName);
	JStringLMBCS userName(env, _userName);

	NCheck(env, NSFProfileDelete(hDB, profileName.getLMBCS(), profileName.length(), userName.getLMBCS(), userName.length()));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFProfileEnum(
	JNIEnv *env, jobject _this, jdhandle _hDB, jstring _profileName, jobject _callback, jint _flags)
{
	DBHANDLE hDB = jdhandle_to_dhandle(_hDB);
	JStringLMBCS profileName(env, _profileName);
	J_NSFPROFILEENUMPROC Callback(env, _callback);
	DWORD Flags = (DWORD)_flags;

	STATUS result = NSFProfileEnum(hDB, profileName.getLMBCS(), profileName.length(), Callback, &Callback, Flags);
	ERR(result) == ERR_CANCEL || NCheck(env, result);

}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFProfileGetField(
		JNIEnv *env, jobject _this, jdhandle _hDB, jstring _profileName, jstring _userName, jstring _fieldName, jpointer _retDatatype, jpointer _retbhValue, jpointer _retValueLength)
{
	DBHANDLE hDB = jdhandle_to_dhandle(_hDB);
	JStringLMBCS profileName(env, _profileName);
	JStringLMBCS userName(env, _userName);
	JStringLMBCS fieldName(env, _fieldName);
	WORD *retDatatype = (WORD*)DEREF_PTR(_retDatatype);
	BLOCKID *retbhValue = (BLOCKID*)DEREF_PTR(_retbhValue);
	DWORD *retValueLength = (DWORD*)DEREF_PTR(_retValueLength);

	NCheck(env, NSFProfileGetField(hDB, profileName.getLMBCS(), profileName.length(), userName.getLMBCS(), userName.length(), fieldName.getLMBCS(), fieldName.length(), retDatatype, retbhValue, retValueLength));
}

extern "C" JNIEXPORT jdhandle JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFProfileOpen(
		JNIEnv *env, jobject _this, jdhandle _hDB, jstring _profileName, jstring _userName, jboolean _copyProfile)
{
	DBHANDLE hDB = jdhandle_to_dhandle(_hDB);
	JStringLMBCS profileName(env, _profileName);
	JStringLMBCS userName(env, _userName);
	BOOL CopyProfile = (BOOL)_copyProfile;

	NOTEHANDLE result;

	if(NCheck(env, NSFProfileOpen(hDB, profileName.getLMBCS(), profileName.length(), userName.getLMBCS(), userName.length(), CopyProfile, &result))) {
		return dhandle_to_jdhandle(result);
	} else {
		return 0;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFProfileSetField(
		JNIEnv *env, jobject _this, jdhandle _hDB, jstring _profileName, jstring _userName, jstring _fieldName, jshort _datatype, jpointer _value, jint _valueLength)
{
	DBHANDLE hDB = jdhandle_to_dhandle(_hDB);
	JStringLMBCS profileName(env, _profileName);
	JStringLMBCS userName(env, _userName);
	JStringLMBCS fieldName(env, _fieldName);
	WORD Datatype = (WORD)_datatype;
	void *value = DEREF_PTR(_value);
	DWORD ValueLength = (DWORD)_valueLength;

	NCheck(env, NSFProfileSetField(hDB, profileName.getLMBCS(), profileName.length(), userName.getLMBCS(), userName.length(), fieldName.getLMBCS(), fieldName.length(), Datatype, value, ValueLength));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFProfileUpdate(
		JNIEnv *env, jobject _this, jdhandle _hProfile, jstring _profileName, jstring _userName)
{
	NOTEHANDLE hProfile = jdhandle_to_dhandle(_hProfile);
	JStringLMBCS profileName(env, _profileName);
	JStringLMBCS userName(env, _userName);

	NCheck(env, NSFProfileUpdate(hProfile, profileName.getLMBCS(), profileName.length(), userName.getLMBCS(), userName.length()));
}
