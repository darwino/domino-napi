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

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLCreate(
	JNIEnv *env, jobject _this
	) {
	DHANDLE hACL;
	if (!NCheck(env, ACLCreate(&hACL))) {
		return 0;
	}
	return dhandle_to_jdhandle(hACL);
}

extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLGetFlags(
	JNIEnv *env, jobject _this, jlong _hACL
	) {
	DHANDLE hACL = jdhandle_to_dhandle(_hACL);
	DWORD Flags;
	if (!NCheck(env, ACLGetFlags(hACL, &Flags))) {
		return 0;
	}
	return Flags;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLSetFlags(
	JNIEnv *env, jobject _this, jlong _hACL, jint Flags
	) {
	DHANDLE hACL = jdhandle_to_dhandle(_hACL);
	NCheck(env, ACLSetFlags(hACL, (DWORD)Flags));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLAddEntry(
	JNIEnv *env, jobject _this, jlong _hACL, jstring _name, jshort _accessLevel, jpointer _privileges, jshort _accessFlags
	) {
	DHANDLE hACL = jdhandle_to_dhandle(_hACL);
	JStringLMBCS name(env, _name);
	WORD accessLevel = (WORD)_accessLevel;
	ACL_PRIVILEGES* privileges = (ACL_PRIVILEGES*)DEREF_PTR(_privileges);
	WORD accessFlags = (WORD)_accessFlags;

	NCheck(env, ACLAddEntry(hACL, name.getLMBCS(), accessLevel, privileges, accessFlags));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLClearPriv(
	JNIEnv *env, jobject _this, jpointer _priv, jshort _num
	) {
	ACL_PRIVILEGES* priv = (ACL_PRIVILEGES*)DEREF_PTR(_priv);
	WORD num = (WORD)_num;

	ACLClearPriv(*priv, num);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLDeleteEntry(
	JNIEnv *env, jobject _this, jlong _hACL, jstring _name
	) {
	DHANDLE hACL = jdhandle_to_dhandle(_hACL);
	JStringLMBCS name(env, _name);

	NCheck(env, ACLDeleteEntry(hACL, name.getLMBCS()));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLEnumEntries(
	JNIEnv *env, jobject _this, jlong _hACL, jobject _callback
	) {
	DHANDLE hACL = jdhandle_to_dhandle(_hACL);
	J_ACLEnumFunc callback(env, _callback);

	NCheck(env, ACLEnumEntries(hACL, callback, &callback));
}

extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLGetAdminServer(
	JNIEnv *env, jobject _this, jlong _hACL
	) {
	DHANDLE hACL = jdhandle_to_dhandle(_hACL);
	char serverName[MAXUSERNAME];
	if (NCheck(env, ACLGetAdminServer(hACL, serverName))) {
		jstring result = LMBCStoJavaString(env, serverName);
		return result;
	}
	else {
		return NULL;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLGetHistory(
	JNIEnv *env, jobject _this, jlong _hACL, jobject _hHistory, jobject _historyCount
	) {
	DHANDLE hACL = jdhandle_to_dhandle(_hACL);
	LongRef<DHANDLE> hHistory(env, _hHistory);
	ShortRef<WORD> historyCount(env, _historyCount);

	NCheck(env, ACLGetHistory(hACL, hHistory, historyCount));
}

extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLGetPrivName(
	JNIEnv *env, jobject _this, jlong _hACL, jshort _privNum
	) {
	DHANDLE hACL = jdhandle_to_dhandle(_hACL);
	WORD privNum = (WORD)_privNum;

	char privName[ACL_PRIVSTRINGMAX];
	if (NCheck(env, ACLGetPrivName(hACL, privNum, privName))) {
		jstring result = LMBCStoJavaString(env, privName);
		return result;
	}
	else {
		return NULL;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLInvertPriv(
	JNIEnv *env, jobject _this, jpointer _priv, jshort _privNum
	) {
	ACL_PRIVILEGES* priv = (ACL_PRIVILEGES*)DEREF_PTR(_priv);
	WORD num = (WORD)_privNum;

	ACLInvertPriv(*priv, num);
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLIsPrivSet(
	JNIEnv *env, jobject _this, jpointer _priv, jshort _privNum
	) {
	ACL_PRIVILEGES* priv = (ACL_PRIVILEGES*)DEREF_PTR(_priv);
	WORD num = (WORD)_privNum;

	BOOL result = ACLIsPrivSet(*priv, num);
	return (jboolean)result;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLLookupAccess(
	JNIEnv *env, jobject _this, jlong _hACL, jpointer _pNamesList, jobject _retAccessLevel, jpointer _retPrivileges, jobject _retAccessFlags, jobject _rethPrivNames
	) {
	DHANDLE hACL = jdhandle_to_dhandle(_hACL);
	NAMES_LIST *pNamesList = (NAMES_LIST*)DEREF_PTR(_pNamesList);
	ShortRef<WORD> retAccessLevel(env, _retAccessLevel);
	ACL_PRIVILEGES *retPrivileges = (ACL_PRIVILEGES*)DEREF_PTR(_retPrivileges);
	ShortRef<WORD> retAccessFlags(env, _retAccessFlags);
	LongRef<DHANDLE> rethPrivNames(env, _rethPrivNames);

	NCheck(env, ACLLookupAccess(hACL, pNamesList, retAccessLevel, retPrivileges, retAccessFlags, rethPrivNames));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLSetAdminServer(
	JNIEnv *env, jobject _this, jlong _hACL, jstring _serverName
	) {
	DHANDLE hACL = jdhandle_to_dhandle(_hACL);
	JStringLMBCS serverName(env, _serverName);

	NCheck(env, ACLSetAdminServer(hACL, (char*)serverName.getLMBCS()));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLSetPriv(
	JNIEnv *env, jobject _this, jpointer _priv, jshort _privNum
	) {
	ACL_PRIVILEGES* priv = (ACL_PRIVILEGES*)DEREF_PTR(_priv);
	WORD num = (WORD)_privNum;

	ACLSetPriv(*priv, num);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLSetPrivName(
	JNIEnv *env, jobject _this, jlong _hACL, jshort _privNum, jstring _privName
	) {
	DHANDLE hACL = jdhandle_to_dhandle(_hACL);
	WORD privNum = (WORD)_privNum;
	JStringLMBCS privName(env, _privName);

	NCheck(env, ACLSetPrivName(hACL, privNum, (char*)privName.getLMBCS()));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ACLUpdateEntry(
	JNIEnv *env, jobject _this, jlong _hACL, jstring _name, jshort _updateFlags, jstring _newName, jshort _newAccessLevel, jpointer _newPrivileges, jshort _newAccessFlags
	) {
	DHANDLE hACL = jdhandle_to_dhandle(_hACL);
	JStringLMBCS name(env, _name);
	WORD updateFlags = (WORD)_updateFlags;
	JStringLMBCS newName(env, _newName);
	WORD newAccessLevel = (WORD)_newAccessLevel;
	ACL_PRIVILEGES* newPrivileges = (ACL_PRIVILEGES*)DEREF_PTR(_newPrivileges);
	WORD newAccessFlags = (WORD)_newAccessFlags;

	NCheck(env, ACLUpdateEntry(hACL, name.getLMBCS(), updateFlags, newName.getLMBCS(), newAccessLevel, newPrivileges, newAccessFlags));
}