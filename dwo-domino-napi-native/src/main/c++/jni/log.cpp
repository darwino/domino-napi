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

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_LogEventText(
	JNIEnv *env, jobject _this, jstring _string, jint _hModule, jshort _additionalErrorCode
	) {
	JStringLMBCS message(env, _string);
	char *String = (char*)message.getLMBCS();
	HANDLE hModule = jhandle_to_handle(_hModule);
	STATUS additionalErrorCode = (STATUS)_additionalErrorCode;

	NCheck(env, LogEventText(String, hModule, additionalErrorCode));
}