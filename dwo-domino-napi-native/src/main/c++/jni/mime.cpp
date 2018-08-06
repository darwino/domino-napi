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

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_MIMEConvertCDParts(
	JNIEnv *env, jobject _this, jlong hNote, jboolean canonical, jboolean isMIME, jlong hCC
	) {
	NCheck(env, MIMEConvertCDParts(LONG_TO_DHANDLE(hNote), (BOOL)canonical, (BOOL)isMIME, (CCHANDLE)hCC));
}