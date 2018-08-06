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

// DominoSQLLite.cpp : Defines the entry point for the DLL application.
//

#ifdef NT
BOOL APIENTRY DllMain( HANDLE hModule, 
                       DWORD  ul_reason_for_call, 
                       LPVOID lpReserved
					 )
{
    return TRUE;
}
#endif


/////////////////////////////////////////////////////////////////////
// JVM INITIALISATION
/////////////////////////////////////////////////////////////////////

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved)
{
    JNIEnv *env;
    if (JNI_OK != vm->GetEnv((void **)&env, JNI_VERSION))
        return JNI_ERR;

	// Initialize the JNIUtils

	if(!JNIUtils::init(vm,env)) {
		return JNI_ERR;
	}

    return JNI_VERSION_1_2;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved) {
}
