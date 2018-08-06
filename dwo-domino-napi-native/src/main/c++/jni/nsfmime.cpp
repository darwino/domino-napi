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

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFMimePartGetPart(
	JNIEnv *env, jobject _this, jobject _bhItem
	) {
	Struct<BLOCKID> bhItem(env, _bhItem);
	
	DHANDLE hPart;
	if (!NCheck(env, NSFMimePartGetPart(bhItem, &hPart))) {
		return 0;
	}
	return dhandle_to_jdhandle(hPart);
}