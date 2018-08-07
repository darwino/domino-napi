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

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_MimeGetTypeInfoFromExt(
	JNIEnv *env, jobject _this, jstring _ext, jlong typeBufPtr, jshort typeBufLen, jlong subtypeBufPtr, jshort subtypeBufLen, jlong descrBufPtr, jshort descrBufLen
	) {
	JStringLMBCS ext(env, _ext);
	char *pszExt = (char*)ext.getLMBCS();
	char *pszTypeBuf = (char*)typeBufPtr;
	WORD wTypeBufLen = (WORD)typeBufLen;
	char *pszSubtypeBuf = (char*)subtypeBufPtr;
	WORD wSubtypeBufLen = (WORD)subtypeBufLen;
	char *pszDescrBuf = (char*)descrBufPtr;
	WORD wDescrBufLen = (WORD)descrBufLen;

	// No STATUS on this one
	MimeGetTypeInfoFromExt(pszExt, pszTypeBuf, wTypeBufLen, pszSubtypeBuf, wSubtypeBufLen, pszDescrBuf, wDescrBufLen);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_MimeGetExtFromTypeInfo(
	JNIEnv *env, jobject _this, jstring _type, jstring _subtype, jlong extBufPtr, jshort extBufLen, jlong descrBufPtr, jshort descrBufLen
	) {

	JStringLMBCS type(env, _type);
	char *pszType = (char*)type.getLMBCS();
	JStringLMBCS subtype(env, _subtype);
	char *pszSubtype = (char*)subtype.getLMBCS();

	char *pszExtBuf = (char*)extBufPtr;
	WORD wExtBufLen = (WORD)extBufLen;

	char *pszDescrBuf = (char*)descrBufPtr;
	WORD wDescrBufLen = (WORD)descrBufLen;

	// No STATUS on this one
	MimeGetExtFromTypeInfo(pszType, pszSubtype, pszExtBuf, wExtBufLen, pszDescrBuf, wDescrBufLen);
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_MIMEEntityIsMultiPart(
	JNIEnv *env, jobject _this, jlong _pMimeEntity
	) {

	PMIMEENTITY pMimeEntity = (PMIMEENTITY)LONG_TO_PTR(_pMimeEntity);
	return (jboolean)MIMEEntityIsMultiPart(pMimeEntity);
}