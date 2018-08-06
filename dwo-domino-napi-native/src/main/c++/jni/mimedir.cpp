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