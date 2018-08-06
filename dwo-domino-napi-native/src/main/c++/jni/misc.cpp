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

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_TimeConstant(
	JNIEnv *env, jobject _this,
			jshort type, jobject _td) {
	Struct<TIMEDATE> td(env,_td);
	TimeConstant((WORD)type,td);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_TimeGMToLocal(
	JNIEnv *env, jobject _this,
			jobject _time) {
	Struct<TIME> time(env,_time);
	TimeGMToLocal(time);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_TimeGMToLocalZone(
	JNIEnv *env, jobject _this,
			jobject _time) {
	Struct<TIME> time(env,_time);
	TimeGMToLocalZone(time);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_TimeLocalToGM(
	JNIEnv *env, jobject _this,
			jobject _time) {
	Struct<TIME> time(env,_time);
	TimeLocalToGM(time);
}

extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_TimeDateDifference(
	JNIEnv *env, jobject _this,
			jobject _td1, jobject _td2) {
	Struct<TIMEDATE> td1(env,_td1);
	Struct<TIMEDATE> td2(env,_td2);
	return (jint)TimeDateDifference(td1,td2);
}

extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_TimeDateCompare(
	JNIEnv *env, jobject _this,
	jobject _td1, jobject _td2) {
	Struct<TIMEDATE> td1(env, _td1);
	Struct<TIMEDATE> td2(env, _td2);
	return (jint)TimeDateCompare(td1, td2);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_TimeDateIncrement(
	JNIEnv *env, jobject _this,
			jobject _td, jint interval) {
	Struct<TIMEDATE> td(env,_td);
	TimeDateIncrement(td,interval);
}

extern "C" JNIEXPORT jint JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_TimeExtractDate(
	JNIEnv *env, jobject _this, jobject _td) {
	Struct<TIMEDATE> td(env, _td);
	return (jint)TimeExtractDate(td);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_TimeDateClear(
	JNIEnv *env, jobject _this, jobject _td) {
	Struct<TIMEDATE> td(env, _td);
	TimeDateClear(td);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ConvertTextToTIMEDATE(
	JNIEnv *env, jobject _this, jpointer _intlFormat, jobject _textFormat, jpointer _text, jshort _maxLength, jobject _td) {
	void *intlFormat = DEREF_PTR(_intlFormat);
	Struct<TFMT> textFormat(env, _textFormat);
	char **ppText = (char**)DEREF_PTR(_text);
	WORD maxLength = (WORD)_maxLength;
	Struct<TIMEDATE> td(env, _td);
	NCheck(env, ConvertTextToTIMEDATE(intlFormat, textFormat, ppText, maxLength, td));
}

extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_ConvertTIMEDATEToText(
	JNIEnv *env, jobject _this, jpointer _intlFormat, jobject _textFormat, jobject _inputTime) {
	void *intlFormat = DEREF_PTR(_intlFormat);
	Struct<TFMT> textFormat(env, _textFormat);
	Struct<TIMEDATE> td(env, _inputTime);

	char retTextBuffer[MAXALPHATIMEDATE];
	WORD retTextLength;

	if (NCheck(env, ConvertTIMEDATEToText(intlFormat, textFormat, td, retTextBuffer, MAXALPHATIMEDATE, &retTextLength))) {
		return LMBCStoJavaString(env, retTextBuffer, retTextLength);
	}
	else {
		return NULL;
	}
}
