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


extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSGetDataDirectory(
	JNIEnv *env, jobject _this)
{
	char retBuffer[MAXPATH];
	OSGetDataDirectory(retBuffer);
	return LMBCStoJavaString(env,retBuffer);
}

extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSGetExecutableDirectory(
	JNIEnv *env, jobject _this)
{
	char retBuffer[MAXPATH];
	OSGetExecutableDirectory(retBuffer);
	return LMBCStoJavaString(env, retBuffer);
}

extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSPathNetConstruct(
	JNIEnv *env, jobject _this, jstring _portName, jstring _serverName, jstring _fileName)
{
	char *portName = NULL;
	if (_portName != NULL) {
		JStringLMBCS portNameLMBCS(env, _portName);
		portName = (char*)portNameLMBCS.getLMBCS();
	}

	JStringLMBCS serverName(env, _serverName);
	JStringLMBCS fileName(env, _fileName);

	char retBuffer[MAXPATH];
	// This actually won't happen according to the docs, but may as well run the STATUS through the routine just in case
	if (!NCheck(env, OSPathNetConstruct((const char*)portName, serverName.getLMBCS(), fileName.getLMBCS(), retBuffer))) {
		return NULL;
	}
	return LMBCStoJavaString(env, retBuffer);
}