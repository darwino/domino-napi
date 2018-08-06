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


extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_SECKFMGetUserName(
	JNIEnv *env, jobject _this
	) {
	
	char retBuffer[MAXUSERNAME + 1];

	if (!NCheck(env, SECKFMGetUserName(retBuffer))) {
		return NULL;
	}
	return LMBCStoJavaString(env, retBuffer);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_SECExtractIdFileFromDB(
	JNIEnv *env, jobject _this, jdhandle _hDB, jstring _profileNoteName, jstring _userName, jstring _password, jstring _putIDFileHere)
{
	// This uses the 8.5.1-era extra ability of pReserved to look up in the ID Vault
	DHANDLE hDB = jdhandle_to_dhandle(_hDB);
	JStringLMBCS profileNoteName(env, _profileNoteName);
	JStringLMBCS userName(env, _userName);
	JStringLMBCS password(env, _password);
	JStringLMBCS putIdFileHere(env, _putIDFileHere);

	char* user = (char*)userName.getLMBCS();
	char* passwd = (char*)(password.length() == 0 ? "" : password.getLMBCS());
	char* idDest = (char*)(putIdFileHere.length() == 0 ? NULL : putIdFileHere.getLMBCS());

	NCheck(env, SECExtractIdFileFromDB(hDB, (char*)profileNoteName.getLMBCS(), profileNoteName.length(), user, userName.length(), passwd, idDest, 0, user));
}

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_SECKFMOpen(
	JNIEnv *env, jobject _this, jstring _IDFileName, jstring _password, jint _flags)
{

	KFHANDLE hKFC = NULLKFHANDLE;
	JStringLMBCS idFileName(env, _IDFileName);
	JStringLMBCS password(env, _password);
	DWORD flags = (DWORD)_flags;

	char* idFile = (char*)idFileName.getLMBCS();
	char* passwd = (char*)(password.length() == 0 ? "" : password.getLMBCS());

	if (NCheck(env, SECKFMOpen(&hKFC, idFile, passwd, flags, 0, NULL))) {
		return (jlong)hKFC;
	} else {
		return 0;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_SECKFMClose(
	JNIEnv *env, jobject _this, jlong _hKFC, jint _flags)
{

	KFHANDLE hKFC = (KFHANDLE)_hKFC;
	DWORD Flags = (DWORD)_flags;

	NCheck(env, SECKFMClose(&hKFC, Flags, 0, NULL));
}