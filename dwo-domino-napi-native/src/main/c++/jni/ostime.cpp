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


extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSCurrentTimeZone(
	JNIEnv *env, jobject _this,
	jobject _retZone, jobject _retDST) {
	IntRef<int> retZone(env,_retZone);
	IntRef<int> retDST(env,_retDST);
	OSCurrentTimeZone(retZone,retDST);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_OSCurrentTIMEDATE(
	JNIEnv *env, jobject _this,
	jobject _retTimeDate) {
	Struct<TIMEDATE> retTimeDate(env,_retTimeDate);
	OSCurrentTIMEDATE(retTimeDate);
}
