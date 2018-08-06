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

extern "C" JNIEXPORT jdhandle JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSGetServerList(
	JNIEnv *env, jobject _this, jstring _pPortName)
{
	DHANDLE retServerTextList;
	JStringLMBCS pPortName(env,_pPortName);
	if(!NCheck(env,NSGetServerList((char*)pPortName.getLMBCS(),&retServerTextList))) {
		return 0;
	}
	return dhandle_to_jdhandle(retServerTextList);
}
