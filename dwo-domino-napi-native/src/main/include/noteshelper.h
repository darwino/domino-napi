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

#ifndef _NOTESHELPER_H_
#define _NOTESHELPER_H_


// ===========================================================================================
// Conversion Macros & Types
// ===========================================================================================

#define long_to_jlong(LONG)		((jlong)(LONG))
#define jlong_to_long(JLONG)		((long)(JLONG))
#define longlong_to_jlong(LONGLONG)	((jlong)(LONGLONG))
#ifdef WIN32
	#define jlong_to_longlong(JLONG)	((__int64)(JLONG))
#else
	#define jlong_to_longlong(JLONG)	((long long)(JLONG))
#endif

#define ulong_to_jlong(ULONG)		((jlong)(ULONG))
#define jlong_to_ulong(JLONG)		((unsigned long)(JLONG))

// Note: this can be changed to adapt to the different platforms
// For example, AS/400 will need to go through a table

// Pointers
typedef jlong jpointer;
inline jpointer REF_PTR(void* ptr) { return (jpointer)longlong_to_jlong(ptr);} 
inline void* DEREF_PTR(jpointer ptr, int offset) { return (void*)((char*)(jlong_to_longlong(ptr))+offset);}
inline void* DEREF_PTR(jpointer ptr) { return (void*)((jlong_to_longlong(ptr)));} 

// Handles
typedef jlong jdhandle;
inline DHANDLE jdhandle_to_dhandle(jdhandle hndl) {return (DHANDLE)((sizeof (DHANDLE) > 4) ? jlong_to_longlong(hndl) : jlong_to_ulong(hndl));}
inline jdhandle dhandle_to_jdhandle(DHANDLE hndl) {return (jdhandle)((sizeof (DHANDLE) > 4) ? longlong_to_jlong(hndl) : ulong_to_jlong(hndl));} 
inline MEMHANDLE jlong_to_memhandle(jlong hndl) {return (MEMHANDLE)(jlong_to_ulong(hndl));}
inline jlong memhandle_to_jlong(MEMHANDLE hndl) { return (jlong)(ulong_to_jlong(hndl)); }
typedef jint jhandle;
inline HANDLE jhandle_to_handle(jhandle hndl) { return (HANDLE)hndl; }
inline jhandle handle_to_jhandle(HANDLE hndl) { return (jhandle)hndl; }

// Numbers
inline jdouble& number_to_double(NUMBER& n) { return *(jdouble*)&n; }
inline NUMBER& double_to_number(jdouble& n) { return *(NUMBER*)&n; }


// ===========================================================================================
// Error checking
// ===========================================================================================

bool NCheck( JNIEnv *env, STATUS err );
bool NCheck( JNIEnv *env, STATUS err, STATUS noStackTrace );
bool NCheckFormula(JNIEnv *env, STATUS err, STATUS *_compileError, WORD *errorLine, WORD *errorColumn, WORD *errorOffset, WORD *errorLength);
void NPrintError( JNIEnv *env, STATUS err );
bool NLSCheck(JNIEnv *env, NLS_STATUS err);

void ThrowNullPointerException(JNIEnv *env, const char *msg);

// ===========================================================================================
// String conversion
// ===========================================================================================

jstring LMBCStoJavaString(JNIEnv *env, const char *str, int len);
jstring LMBCStoJavaString(JNIEnv *env, const char *str);

class JStringLMBCS {
	char localBuffer[128*3];
	char* data = NULL;

	void init(JNIEnv* env, const jchar* c, int length);

public:
	JStringLMBCS(JNIEnv* env, jstring s);
	JStringLMBCS(JNIEnv* env, jchar* c);
	JStringLMBCS(JNIEnv* env, jchar* c, int length);
	~JStringLMBCS() {
		if(data!=localBuffer && data != NULL) MemFree(data);
	}
	const char* getLMBCS() { return data; }
	size_t length() { return data == NULL ? 0 : strlen(data); }
};


// ===========================================================================================
// Handle management
// ===========================================================================================

struct DBHANDLE_ {
	DBHANDLE hDB;
	DBHANDLE_() : hDB(0) {}
	~DBHANDLE_() {if(hDB) {NSFDbClose(hDB);}}
	operator DBHANDLE&() { return hDB; }
	DBHANDLE detach() { DBHANDLE temp=hDB; hDB=NULLHANDLE; return temp; }
};

struct NOTEHANDLE_ {
	NOTEHANDLE hNote;
	NOTEHANDLE_() : hNote(0) {}
	~NOTEHANDLE_() {if(hNote) {NSFDbClose(hNote);}}
	operator NOTEHANDLE&() { return hNote; }
	NOTEHANDLE detach() { NOTEHANDLE temp = hNote; hNote = NULLHANDLE; return temp; }
};

struct BLOCKID_ {
	BLOCKID hBlock;
	BLOCKID_(BLOCKID hBlock) : hBlock(hBlock) {}
	~BLOCKID_() { if(!ISNULLBLOCKID(hBlock)){OSUnlockBlock(hBlock);}}
	operator BLOCKID&() { return hBlock; }
};



// ===========================================================================================
// Base object
// ===========================================================================================

class _JniWrapper {
public:
	void* operator new( size_t size ) {
		return MemAlloc(size);
	}
	void operator delete ( void* ptr) {
		MemFree(ptr);
	}
};


// ===========================================================================================
// By reference parameters
// ===========================================================================================

class _Ref : public _JniWrapper {

protected:
	JNIEnv* env;
	jobject object;

public:
	_Ref(JNIEnv* env, jobject object) : env(env), object(object) {
	}
};

class _IntRef : public _Ref {

public:
	static jclass clazz;
	static jmethodID getMethod;
	static jmethodID setMethod;

protected:
	jint loadValue() {
		if(object) {
			return env->CallIntMethod(object,getMethod);
		}
		return 0;
	}

	void saveValue(jint value) {
		if(object) {
			env->CallIntMethod(object,setMethod,value);
		}
	}

public:
	_IntRef(JNIEnv* env, jobject object) : _Ref(env,object) {
	}
};

template <class T> class IntRef : public _IntRef {

protected:
	T value;

public:
	IntRef(JNIEnv* env, jobject object) : _IntRef(env,object) {
		value = (T)0;
	}
	
	IntRef(JNIEnv* env, jobject object, bool init) : _IntRef(env,object) {
		if(init) {
			value = (T)loadValue();
		} else {
			value = (T)0;
		}
	}

	~IntRef() {
		saveValue((jint)value);
	}

	operator T*() {
		return &value;
	}
};

class _LongRef : public _Ref {

public:
	static jclass clazz;
	static jmethodID getMethod;
	static jmethodID setMethod;

protected:
	jlong loadValue() {
		if(object) {
			return env->CallLongMethod(object,getMethod);
		}
		return 0;
	}

	void saveValue(jlong value) {
		if(object) {
			env->CallLongMethod(object,setMethod,value);
		}
	}

public:
	_LongRef(JNIEnv* env, jobject object) : _Ref(env,object) {
	}
};

template <class T> class LongRef : public _LongRef {

protected:
	T value;

public:
	LongRef(JNIEnv* env, jobject object) : _LongRef(env,object) {
		value = (T)0;
	}
	
	LongRef(JNIEnv* env, jobject object, bool init) : _LongRef(env,object) {
		if(init) {
			value = (T)loadValue();
		} else {
			value = (T)0;
		}
	}

	~LongRef() {
		saveValue((jlong)value);
	}

	operator T*() {
		return &value;
	}

	LongRef<T>& operator =(T value) {
		this->value = value;
		return *this;
	}
};

class _ShortRef : public _Ref {

public:
	static jclass clazz;
	static jmethodID getMethod;
	static jmethodID setMethod;

protected:
	jshort loadValue() {
		if (object) {
			return env->CallShortMethod(object, getMethod);
		}
		return 0;
	}

	void saveValue(jshort value) {
		if (object) {
			env->CallShortMethod(object, setMethod, value);
		}
	}

public:
	_ShortRef(JNIEnv* env, jobject object) : _Ref(env, object) {
	}
};

template <class T> class ShortRef : public _ShortRef {

protected:
	T value;

public:
	ShortRef(JNIEnv* env, jobject object) : _ShortRef(env, object) {
		value = (T)0;
	}

	ShortRef(JNIEnv* env, jobject object, bool init) : _ShortRef(env, object) {
		if (init) {
			value = (T)loadValue();
		}
		else {
			value = (T)0;
		}
	}

	~ShortRef() {
		saveValue((jshort)value);
	}

	operator T*() {
		return &value;
	}

	ShortRef<T>& operator =(T value) {
		this->value = value;
		return *this;
	}
};

class _ByteRef : public _Ref {

public:
	static jclass clazz;
	static jmethodID getMethod;
	static jmethodID setMethod;

protected:
	jbyte loadValue() {
		if (object) {
			return env->CallByteMethod(object, getMethod);
		}
		return 0;
	}

	void saveValue(jbyte value) {
		if (object) {
			env->CallByteMethod(object, setMethod, value);
		}
	}

public:
	_ByteRef(JNIEnv* env, jobject object) : _Ref(env, object) {
	}
};

template <class T> class ByteRef : public _ByteRef {

protected:
	T value;

public:
	ByteRef(JNIEnv* env, jobject object) : _ByteRef(env, object) {
		value = (T)0;
	}

	ByteRef(JNIEnv* env, jobject object, bool init) : _ByteRef(env, object) {
		if (init) {
			value = (T)loadValue();
		}
		else {
			value = (T)0;
		}
	}

	~ByteRef() {
		saveValue((jbyte)value);
	}

	operator T*() {
		return &value;
	}

	ByteRef<T>& operator =(T value) {
		this->value = value;
		return *this;
	}
};

// ===========================================================================================
// Wrapped Struct
// ===========================================================================================

class _Struct : public _JniWrapper {

public:
	static jclass clazz;
	static jfieldID field;

protected:
	JNIEnv* env;
	jobject object;
	void* ptr;

public:
	_Struct(JNIEnv* env, jobject object) : env(env), object(object) {
		this->ptr = object ? DEREF_PTR((jpointer)env->GetLongField(object,field)) : NULL;
	}
};

template <class T> class Struct : public _Struct {
public:
	Struct(JNIEnv* env, jobject object) : _Struct(env,object) {
	}

	operator T*() { 
		return (T*)ptr;
	}

	operator T&() { 
		return *(T*)ptr;
	}
};

#endif
