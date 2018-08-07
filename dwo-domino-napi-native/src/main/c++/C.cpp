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
#include <sstream>

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_initSizes(
			JNIEnv *env, jclass clazz, jintArray sizes ) {

	// Find all the type sizes
	jint* array = env->GetIntArrayElements(sizes,NULL);
	array[1] = sizeof(BYTE);
	array[2] = sizeof(WORD);
	array[3] = sizeof(SWORD);
	array[4] = sizeof(DWORD);
	array[5] = sizeof(LONG);
	array[6] = sizeof(USHORT);
	array[7] = sizeof(BOOL);
	array[8] = sizeof(short);
	array[9] = sizeof(int);
	array[10] = sizeof(long);
	array[11] = sizeof(DHANDLE);
	array[12] = sizeof(void*);
	array[13] = sizeof(NUMBER);
	array[14] = sizeof(NOTEID);
	array[15] = sizeof(char);
	array[16] = sizeof(MEMHANDLE);
	array[17] = sizeof(HCOLLECTION);
	array[18] = sizeof(NOTEHANDLE);
	array[19] = sizeof(HANDLE);
	array[20] = sizeof(DBHANDLE);
	array[21] = sizeof(double);
	array[22] = sizeof(FONTID);
	array[23] = sizeof(HTMLAPI_REF_TYPE);
	array[24] = sizeof(CmdArgID);
	array[25] = sizeof(CmdArgValueType);
	array[26] = sizeof(LICENSEID);

	env->ReleaseIntArrayElements(sizes,array,0);
}

extern "C" JNIEXPORT jpointer JNICALL Java_com_darwino_domino_napi_c_C_malloc(
			JNIEnv *env, jclass clazz,
			jint size ) {
	return REF_PTR(MemAlloc(size));
}

extern "C" JNIEXPORT jpointer JNICALL Java_com_darwino_domino_napi_c_C_calloc(
	JNIEnv *env, jclass clazz,
	jint count, jint size) {
	return REF_PTR(MemAllocInit(count, size));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_free(
			JNIEnv *env, jclass clazz,
			jpointer ptr) {
	MemFree(DEREF_PTR(ptr,0));
}

extern "C" JNIEXPORT jpointer JNICALL Java_com_darwino_domino_napi_c_C_ptrAdd(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return REF_PTR(DEREF_PTR(ptr,offset));
}

extern "C" JNIEXPORT jpointer JNICALL Java_com_darwino_domino_napi_c_C_ptrSub(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	char* workPtr = (char*)DEREF_PTR(ptr); 
	workPtr = workPtr - offset;
	return REF_PTR(workPtr);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_memset(
			JNIEnv *env, jclass clazz,
			jpointer buffer, jint offset, jbyte value, jint size) {
	memset(DEREF_PTR(buffer,offset),value,size);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_memcpy(
			JNIEnv *env, jclass clazz,
			jpointer dest, jint offdest, jpointer src, jint offsrc, jint size) {
	memcpy(DEREF_PTR(dest,offdest),DEREF_PTR(src,offsrc),size);
}

extern "C" JNIEXPORT jbyte JNICALL Java_com_darwino_domino_napi_c_C_getBYTE(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return *(BYTE*)(DEREF_PTR(ptr,offset));
}

extern "C" JNIEXPORT jshort JNICALL Java_com_darwino_domino_napi_c_C_getWORD(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return *(WORD*)(DEREF_PTR(ptr,offset));
}

extern "C" JNIEXPORT jshort JNICALL Java_com_darwino_domino_napi_c_C_getSWORD(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return *(SWORD*)(DEREF_PTR(ptr,offset));
}

extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_c_C_getDWORD(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return *(DWORD*)(DEREF_PTR(ptr,offset));
}

extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_c_C_getLONG(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return *(LONG*)(DEREF_PTR(ptr,offset));
}

extern "C" JNIEXPORT jshort JNICALL Java_com_darwino_domino_napi_c_C_getUSHORT(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return *(USHORT*)(DEREF_PTR(ptr,offset));
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_darwino_domino_napi_c_C_getBOOL(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return (jboolean)*(BOOL*)(DEREF_PTR(ptr,offset));
}

extern "C" JNIEXPORT jdouble JNICALL Java_com_darwino_domino_napi_c_C_getNUMBER(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return number_to_double(*(NUMBER*)(DEREF_PTR(ptr,offset)));
}

extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_c_C_getFONTID(
	JNIEnv *env, jclass clazz,
	jpointer ptr, jint offset) {
	return (jint)*(FONTID*)(DEREF_PTR(ptr, offset));
}
extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_c_C_getHTMLAPI_1REF_1TYPE(
	JNIEnv *env, jclass clazz, jpointer ptr, jint offset) {
	return (jint)*(HTMLAPI_REF_TYPE*)(DEREF_PTR(ptr, offset));
}
extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_c_C_getCmdArgValueType(
	JNIEnv *env, jclass clazz, jpointer ptr, jint offset) {
	return (jint)*(CmdArgValueType*)(DEREF_PTR(ptr, offset));
}
extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_c_C_getCmdArgID(
	JNIEnv *env, jclass clazz, jpointer ptr, jint offset) {
	return (jint)*(CmdArgID*)(DEREF_PTR(ptr, offset));
}

extern "C" JNIEXPORT jbyte JNICALL Java_com_darwino_domino_napi_c_C_getByte(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return *(jbyte*)(DEREF_PTR(ptr,offset));
}

extern "C" JNIEXPORT jshort JNICALL Java_com_darwino_domino_napi_c_C_getShort(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return *(short*)(DEREF_PTR(ptr,offset));
}

extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_c_C_getInt(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return *(int*)(DEREF_PTR(ptr,offset));
}

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_c_C_getLong(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return (long_to_jlong(*(long*)(DEREF_PTR(ptr,offset))));
}

extern "C" JNIEXPORT jfloat JNICALL Java_com_darwino_domino_napi_c_C_getFloat(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return *(float*)(DEREF_PTR(ptr,offset));
}

extern "C" JNIEXPORT jdouble JNICALL Java_com_darwino_domino_napi_c_C_getDouble(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return *(double*)(DEREF_PTR(ptr,offset));
}

extern "C" JNIEXPORT jhandle JNICALL Java_com_darwino_domino_napi_c_C_getHandle(
	JNIEnv *env, jclass clazz,
	jpointer ptr, jint offset) {
	return handle_to_jhandle(*(HANDLE*)(DEREF_PTR(ptr, offset)));
}

extern "C" JNIEXPORT jdhandle JNICALL Java_com_darwino_domino_napi_c_C_getDHandle(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return dhandle_to_jdhandle(*(DHANDLE*)(DEREF_PTR(ptr,offset)));
}

extern "C" JNIEXPORT jpointer JNICALL Java_com_darwino_domino_napi_c_C_getPointer(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return *(jpointer*)(DEREF_PTR(ptr,offset));
}

extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_c_C_getLMBCSString__JI(
	JNIEnv *env, jclass clazz,
	jpointer ptr, jint offset) {

	const char *val = (char*)(DEREF_PTR(ptr, offset));
	return LMBCStoJavaString(env, val);
}

extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_c_C_getLMBCSString__JII(
	JNIEnv *env, jclass clazz,
	jpointer ptr, jint offset, jint len) {

	const char *val = (char*)(DEREF_PTR(ptr, offset));
	return LMBCStoJavaString(env, val, len);
}

extern "C" JNIEXPORT jpointer JNICALL Java_com_darwino_domino_napi_c_C_toLMBCSString(
	JNIEnv *env, jclass clazz, jstring _value) {

	JStringLMBCS itemName(env, _value);
	size_t length = itemName.length();
	if (length == 0) {
		char *value = (char*)malloc(sizeof(char));
		value[0] = '\0';
		return REF_PTR(value);
	} else {
		// Make a copy of the data so it's not removed by the destructor
		char *value = (char*)malloc(sizeof(char) * length + 1);
		value[length] = '\0';
		strcpy(value, itemName.getLMBCS());

		return REF_PTR(value);
	}
}


extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setBYTE(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset, jbyte value) {
	*(BYTE*)(DEREF_PTR(ptr,offset)) = value;
}
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setWORD(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset, jshort value) {
	*(WORD*)(DEREF_PTR(ptr,offset)) = value;
}
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setSWORD(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset, jshort value) {
	*(SWORD*)(DEREF_PTR(ptr,offset)) = value;
}
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setDWORD(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset, jint value) {
	*(DWORD*)(DEREF_PTR(ptr,offset)) = value;
}
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setLONG(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset, jint value) {
	*(LONG*)(DEREF_PTR(ptr,offset)) = value;
}
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setUSHORT(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset, jshort value) {
	*(USHORT*)(DEREF_PTR(ptr,offset)) = value;
}
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setBOOL(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset, jboolean value) {
	*(BOOL*)(DEREF_PTR(ptr,offset)) = value;
}
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setNUMBER(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset, jdouble value) {
	*(NUMBER*)(DEREF_PTR(ptr,offset)) = double_to_number(value);
}
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setFONTID(
	JNIEnv *env, jclass clazz,
	jpointer ptr, jint offset, jint value) {
	*(FONTID*)(DEREF_PTR(ptr, offset)) = value;
}
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setHTMLAPI_1REF_1TYPE(
	JNIEnv *env, jclass clazz, jpointer ptr, jint offset, jint value) {
	*(HTMLAPI_REF_TYPE*)(DEREF_PTR(ptr, offset)) = (HTMLAPI_REF_TYPE)value;
}


extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setByte(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset, jbyte value) {
	*(jbyte*)(DEREF_PTR(ptr,offset)) = value;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setShort(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset, jshort value) {
	*(short*)(DEREF_PTR(ptr,offset)) = value;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setInt(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset, jint value) {
	*(int*)(DEREF_PTR(ptr,offset)) = value;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setLong(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset, jlong value) {
	*(long*)(DEREF_PTR(ptr,offset)) = jlong_to_long(value);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setFloat(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset, jfloat value) {
	*(float*)(DEREF_PTR(ptr,offset)) = value;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setDouble(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset, jdouble value) {
	*(double*)(DEREF_PTR(ptr,offset)) = value;
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setHandle(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset, jhandle value) {
	*(HANDLE*)(DEREF_PTR(ptr,offset)) = jhandle_to_handle(value);
}

// This is basically a duplicate of above for explicitness
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setDHandle(
	JNIEnv *env, jclass clazz,
	jpointer ptr, jint offset, jdhandle value) {
	*(DHANDLE*)(DEREF_PTR(ptr, offset)) = jdhandle_to_dhandle(value);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_setPointer(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset, jpointer value) {
	*(void**)(DEREF_PTR(ptr,offset)) = DEREF_PTR(value);
}


extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_writeByteArray(
			JNIEnv *env, jclass clazz,
			jpointer dest, jint offset, jbyteArray src, jint offsrc, jint size) {
	jbyte* array = (jbyte*)env->GetPrimitiveArrayCritical(src, NULL);
	memmove(DEREF_PTR(dest,offset),array+offsrc,size*sizeof(jbyte));
	env->ReleasePrimitiveArrayCritical(src, array, 0);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_readByteArray(
			JNIEnv *env, jclass clazz,
			jbyteArray dest, jint offdest, jpointer src, jint offset, jint size) {
	jbyte* array = (jbyte*)env->GetPrimitiveArrayCritical(dest, NULL);
	memmove(array+offdest,DEREF_PTR(src,offset),size*sizeof(jbyte));
	env->ReleasePrimitiveArrayCritical(dest, array, 0);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_writeShortArray(
			JNIEnv *env, jclass clazz,
			jpointer dest, jint offset, jshortArray src, jint offsrc, jint size) {
	jshort* array = (jshort*)env->GetPrimitiveArrayCritical(src, NULL);
	memmove(DEREF_PTR(dest,offset),array+offsrc,size*sizeof(jshort));
	env->ReleasePrimitiveArrayCritical(src, array, 0);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_readShortArray(
			JNIEnv *env, jclass clazz,
			jshortArray dest, jint offset, jpointer src, jint offsrc, jint size) {
	jshort* array = (jshort*)env->GetPrimitiveArrayCritical(dest, NULL);
	memmove(array+offset,DEREF_PTR(src,offsrc),size*sizeof(jshort));
	env->ReleasePrimitiveArrayCritical(dest, array, 0);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_writeIntArray(
			JNIEnv *env, jclass clazz,
			jpointer dest, jint offset, jintArray src, jint offsrc, jint size) {
	jint* array = (jint*)env->GetPrimitiveArrayCritical(src, NULL);
	memmove(DEREF_PTR(dest,offset),array+offsrc,size*sizeof(jint));
	env->ReleasePrimitiveArrayCritical(src, array, 0);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_readIntArray(
			JNIEnv *env, jclass clazz,
			jintArray dest, jint offset, jpointer src, jint offsrc, jint size) {
	jint* array = (jint*)env->GetPrimitiveArrayCritical(dest, NULL);
	memmove(array+offset,DEREF_PTR(src,offsrc),size*sizeof(jint));
	env->ReleasePrimitiveArrayCritical(dest, array, 0);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_writeLongArray(
			JNIEnv *env, jclass clazz,
			jpointer dest, jint offset, jlongArray src, jint offsrc, jint size) {
	jlong* array = (jlong*)env->GetPrimitiveArrayCritical(src, NULL);
	memmove(DEREF_PTR(dest,offset),array+offsrc,size*sizeof(jlong));
	env->ReleasePrimitiveArrayCritical(src, array, 0);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_readLongArray(
			JNIEnv *env, jclass clazz,
			jlongArray dest, jint offset, jpointer src, jint offsrc, jint size) {
	jlong* array = (jlong*)env->GetPrimitiveArrayCritical(dest, NULL);
	memmove(array+offset,DEREF_PTR(src,offsrc),size*sizeof(jlong));
	env->ReleasePrimitiveArrayCritical(dest, array, 0);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_writeFloatArray(
			JNIEnv *env, jclass clazz,
			jpointer dest, jint offset, jfloatArray src, jint offsrc, jint size) {
	jfloat* array = (jfloat*)env->GetPrimitiveArrayCritical(src, NULL);
	memmove(DEREF_PTR(dest,offset),array+offsrc,size*sizeof(jfloat));
	env->ReleasePrimitiveArrayCritical(src, array, 0);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_readFloatArray(
			JNIEnv *env, jclass clazz,
			jfloatArray dest, jint offset, jpointer src, jint offsrc, jint size) {
	jfloat* array = (jfloat*)env->GetPrimitiveArrayCritical(dest, NULL);
	memmove(array+offset,DEREF_PTR(src,offsrc),size*sizeof(jfloat));
	env->ReleasePrimitiveArrayCritical(dest, array, 0);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_writeDoubleArray(
			JNIEnv *env, jclass clazz,
			jpointer dest, jint offset, jdoubleArray src, jint offsrc, jint size) {
	jdouble* array = (jdouble*)env->GetPrimitiveArrayCritical(src, NULL);
	memmove(DEREF_PTR(dest,offset),array+offsrc,size*sizeof(jdouble));
	env->ReleasePrimitiveArrayCritical(src, array, 0);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_readDoubleArray(
			JNIEnv *env, jclass clazz,
			jdoubleArray dest, jint offset, jpointer src, jint offsrc, jint size) {
	jdouble* array = (jdouble*)env->GetPrimitiveArrayCritical(dest, NULL);
	memmove(array+offset,DEREF_PTR(src,offsrc),size*sizeof(jdouble));
	env->ReleasePrimitiveArrayCritical(dest, array, 0);
}

extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_c_C_strlen(
			JNIEnv *env, jclass clazz,
			jpointer ptr, jint offset) {
	return (jint)strlen((char*)DEREF_PTR(ptr,offset));
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_c_C_strcpy(
			JNIEnv *env, jclass clazz,
			jpointer dest, jint offdest, jpointer src, jint offsrc) {
	strcpy((char*)DEREF_PTR(dest,offdest),(char*)DEREF_PTR(src,offsrc));
}
