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