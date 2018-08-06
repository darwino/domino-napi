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


extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NLS_1translate(
	JNIEnv *env, jobject _this, jpointer _pString, jshort Len, jpointer _pStringTarget, jobject _pSize, jshort ControlFlags, jpointer _pInfo
	) {

	BYTE* pString = (BYTE*)DEREF_PTR(_pString);
	BYTE* pStringTarget = (BYTE*)DEREF_PTR(_pStringTarget);
	IntRef<WORD> pSize(env, _pSize, true);

	char buffer[50];
	memset(buffer, '\0', 50);
	sprintf(buffer, "Using pSize %i", *pSize);
	jprintln(buffer);
	NLS_PINFO pInfo = (NLS_PINFO)DEREF_PTR(_pInfo);

	if (!NLSCheck(env, NLS_translate(pString, (WORD)Len, pStringTarget, pSize, (WORD)ControlFlags, pInfo))) {
		return;
	}
}

extern "C" JNIEXPORT jpointer JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NLS_1load_1charset(
	JNIEnv *env, jobject _this, jshort CSID
	) {

	NLS_PINFO pInfo;

	if (!NLSCheck(env, NLS_load_charset((WORD)CSID, &pInfo))) {
		return 0;
	}
	return REF_PTR(pInfo);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NLS_1unload_1charset(
	JNIEnv *env, jobject _this, jpointer pInfo
	) {

	if (!NLSCheck(env, NLS_unload_charset(DEREF_PTR(pInfo)))) {
		return;
	}
}