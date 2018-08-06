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

// This doesn't use jstring because it's intended for low-level use from the Java side
extern "C" JNIEXPORT jshort JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSTranslate(
	JNIEnv *env, jobject _this, jshort translateMode, jlong inPtr, jshort inLength, jlong outPtr, jshort outLength)
{
	return OSTranslate((WORD)translateMode, (const char *)inPtr, (WORD)inLength, (char *)outPtr, (WORD)outLength);
}

extern "C" JNIEXPORT jpointer JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSGetLMBCSCLS(
	JNIEnv *env, jobject _this)
{
	return REF_PTR(OSGetLMBCSCLS());
}

extern "C" JNIEXPORT jpointer JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSGetNativeCLS(
	JNIEnv *env, jobject _this)
{
	return REF_PTR(OSGetNativeCLS());
}