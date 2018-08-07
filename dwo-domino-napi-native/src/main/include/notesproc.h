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

#ifndef _NOTESPROC_H_
#define _NOTESPROC_H_

// ===========================================================================================
// Callback Proc
// ===========================================================================================

template <class T> class _Proc : _JniWrapper {

protected:
	JNIEnv* env;
	jobject object;
	T callback;

public:
	_Proc(JNIEnv* env, jobject object, T callback) : env(env), object(object), callback(callback) {
	}
	operator T() {
		return callback;
	}
};

class J_IDENUMERATEPROC : public _Proc<IDENUMERATEPROC> {
public:
	static jclass clazz;
	static jmethodID m_callback;

	static STATUS LNCALLBACK FCT_IDENUMERATEPROC( void far *Parameter, DWORD id );

public:
	J_IDENUMERATEPROC(JNIEnv* env, jobject object) : _Proc(env,object,&FCT_IDENUMERATEPROC) {
	}
};


class J_NSFITEMSCANPROC : public _Proc < NSFITEMSCANPROC > {
public:
	static jclass clazz;
	static jmethodID m_callback;

	static STATUS LNCALLBACK FCT_NSFITEMSCANPROC(WORD Spare, WORD ItemFlags, char far *Name, WORD NameLength, void far *Value, DWORD ValueLength, void far *RoutineParameter);

public:
	J_NSFITEMSCANPROC(JNIEnv *env, jobject object) : _Proc(env, object, &FCT_NSFITEMSCANPROC) {
	}
};

class J_NSFSEARCHPROC : public _Proc<NSFSEARCHPROC> {
public:
	static jclass clazz;
	static jmethodID m_callback;

	static STATUS LNCALLBACK FCT_NSFSEARCHPROC(void far *EnumRoutineParameter, SEARCH_MATCH far *SearchMatch, ITEM_TABLE far *SummaryBuffer);

public:
	J_NSFSEARCHPROC(JNIEnv *env, jobject object) : _Proc(env, object, &FCT_NSFSEARCHPROC) {
	}
};

class J_NOTEEXTRACTCALLBACK : public _Proc<NOTEEXTRACTCALLBACK> {
public:
	static jclass clazz;
	static jmethodID m_callback;

	static STATUS LNCALLBACK FCT_NOTEEXTRACTCALLBACK(const BYTE *bytes, DWORD length, void far *pParam);

public:
	J_NOTEEXTRACTCALLBACK(JNIEnv *env, jobject object) : _Proc(env, object, &FCT_NOTEEXTRACTCALLBACK) {
	}
};

class J_ActionRoutinePtr : public _Proc<ActionRoutinePtr> {
public:
	static jclass clazz;
	static jmethodID m_callback;

	static STATUS LNCALLBACK FCT_ActionRoutinePtr(char far *RecordPtr, WORD RecordType, DWORD RecordLength, void far *vContext);

public:
	J_ActionRoutinePtr(JNIEnv *env, jobject object) : _Proc(env, object, &FCT_ActionRoutinePtr) {
	}
};

class J_NSFGETALLFOLDERCHANGESCALLBACK : public _Proc<NSFGETALLFOLDERCHANGESCALLBACK> {
public:
	static jclass clazz;
	static jmethodID m_callback;

	static STATUS LNCALLBACK FCT_NSFGETALLFOLDERCHANGESCALLBACK(void *Param, UNIVERSALNOTEID *NoteUNID, DHANDLE AddedNoteTable, DHANDLE RemovedNoteTable);

public:
	J_NSFGETALLFOLDERCHANGESCALLBACK(JNIEnv *env, jobject object) : _Proc(env, object, &FCT_NSFGETALLFOLDERCHANGESCALLBACK) {
	}
};

// Since this has no typedef in the API, define it here
typedef  void (LNCALLBACKPTR ACLEnumFunc) (
	void far *EnumFuncParam,
	char far *Name,
	WORD AccessLevel,
	ACL_PRIVILEGES far *Privileges,
	WORD AccessFlags);

class J_ACLEnumFunc : public _Proc<ACLEnumFunc> {
public:
	static jclass clazz;
	static jmethodID m_callback;

	static void LNCALLBACK FCT_ACLEnumFunc(void* EnumFuncParam, char *Name, WORD AccessLevel, ACL_PRIVILEGES *Privileges, WORD AccessFlags);

public:
	J_ACLEnumFunc(JNIEnv *env, jobject object) : _Proc(env, object, &FCT_ACLEnumFunc) {
	}
};

class J_NSFPROFILEENUMPROC : public _Proc<NSFPROFILEENUMPROC> {
public:
	static jclass clazz;
	static jmethodID m_callback;

	static STATUS LNCALLBACK FCT_NSFPROFILEENUMPROC(DBHANDLE hDB, void *ctx, char *ProfileName, WORD ProfileNameLength, char *UserName, WORD UserNameLength, NOTEID ProfileNoteID);

public:
	J_NSFPROFILEENUMPROC(JNIEnv *env, jobject object) : _Proc(env, object, &FCT_NSFPROFILEENUMPROC) {
	}
};

#endif