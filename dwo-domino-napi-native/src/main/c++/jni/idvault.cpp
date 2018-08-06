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

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_SECidfGet(
	JNIEnv *env, jobject _this, jstring _userName, jstring _password, jstring _putIdFileHere, jpointer _pServerName
	) {
	JStringLMBCS userName(env, _userName);
	JStringLMBCS password(env, _password);
	JStringLMBCS putIdFileHere(env, _putIdFileHere);
	char* pServerName = (char*)DEREF_PTR(_pServerName);
	KFHANDLE hKFC = NULLKFHANDLE;

	char* passwd = (char*)(password.length() == 0 ? "" : password.getLMBCS());
	char* idDest = (char*)(putIdFileHere.length() == 0 ? NULL : putIdFileHere.getLMBCS());

	if (NCheck(env, SECidfGet((char*)userName.getLMBCS(), passwd, idDest, &hKFC, pServerName, 0, 0, NULL))) {
		return (jlong)hKFC;
	} else {
		return NULL;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_SECidvResetUserPassword(
	JNIEnv *env, jobject _this, jstring _server, jstring _userName, jstring _password, jshort _downloadCount
	) {
	JStringLMBCS server(env, _server);
	JStringLMBCS userName(env, _userName);
	JStringLMBCS password(env, _password);
	WORD wDownloadCount = (WORD)_downloadCount;

	char* serv = (char*)(server.length() == 0 ? "" : server.getLMBCS());
	char* user = (char*)(userName.length() == 0 ? "" : userName.getLMBCS());
	char* passwd = (char*)(password.length() == 0 ? "" : password.getLMBCS());
	

	NCheck(env, SECidvResetUserPassword(serv, user, passwd, wDownloadCount, 0, NULL));
}