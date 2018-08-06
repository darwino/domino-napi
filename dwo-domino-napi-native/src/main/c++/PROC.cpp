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


jclass J_IDENUMERATEPROC::clazz;
jmethodID J_IDENUMERATEPROC::m_callback;
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_proc_IDENUMERATEPROC_initNative(
			JNIEnv *env, jclass clazz
			) {
	if(!JNIFindClass(env,J_IDENUMERATEPROC::clazz,"com/darwino/domino/napi/proc/IDENUMERATEPROC")) return;
	if(!JNIGetMethodID( env, J_IDENUMERATEPROC::m_callback, clazz, "callback", "(I)S")) return;
}

STATUS LNCALLBACK J_IDENUMERATEPROC::FCT_IDENUMERATEPROC( void far *Parameter, DWORD id ) {
	J_IDENUMERATEPROC& proc = *(J_IDENUMERATEPROC*)Parameter;
	if(proc.m_callback) { // In case initialization failed!
		return proc.env->CallShortMethod(proc.object,J_IDENUMERATEPROC::m_callback,(jint)id);
	}
	return 0;
}

jclass J_NSFITEMSCANPROC::clazz;
jmethodID J_NSFITEMSCANPROC::m_callback;
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_proc_NSFITEMSCANPROC_initNative(
	JNIEnv *env, jclass clazz)
{
	if (!JNIFindClass(env, J_NSFITEMSCANPROC::clazz, "com/darwino/domino/napi/proc/NSFITEMSCANPROC")) return;
	if (!JNIGetMethodID(env, J_NSFITEMSCANPROC::m_callback, clazz, "callback", "(SLjava/lang/String;JI)S")) return;
}

STATUS LNCALLBACK J_NSFITEMSCANPROC::FCT_NSFITEMSCANPROC(WORD Spare, WORD ItemFlags, char far *Name, WORD NameLength, void far *Value, DWORD ValueLength, void far *RoutineParameter) {
	J_NSFITEMSCANPROC &proc = *(J_NSFITEMSCANPROC*)RoutineParameter;
	if (proc.m_callback) { // In case initialization failed!
		jstring name = LMBCStoJavaString(proc.env, Name, NameLength);
		STATUS result = (STATUS)proc.env->CallShortMethod(proc.object, J_NSFITEMSCANPROC::m_callback, (jshort)ItemFlags, name, REF_PTR(Value), (jint)ValueLength);
		proc.env->DeleteLocalRef(name);
		return result;
	} else {
		return ERR_CANCEL;
	}
}

jclass J_NSFSEARCHPROC::clazz;
jmethodID J_NSFSEARCHPROC::m_callback;
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_proc_NSFSEARCHPROC_initNative(
	JNIEnv *env, jclass clazz)
{
	if (!JNIFindClass(env, J_NSFSEARCHPROC::clazz, "com/darwino/domino/napi/proc/NSFSEARCHPROC")) return;
	if (!JNIGetMethodID(env, J_NSFSEARCHPROC::m_callback, clazz, "callback", "(JJ)S")) return;
}

STATUS LNCALLBACK J_NSFSEARCHPROC::FCT_NSFSEARCHPROC(void far *EnumRoutineParameter, SEARCH_MATCH far *SearchMatch, ITEM_TABLE far *SummaryBuffer) {
	J_NSFSEARCHPROC &proc = *(J_NSFSEARCHPROC*)EnumRoutineParameter;
	if (proc.m_callback) {
		// Make a local copy of the SEARCH_MATCH structure to avoid cross-platform issues
		SEARCH_MATCH localSearchMatch;
		memcpy((char*)(&localSearchMatch), (char*)SearchMatch, sizeof(SEARCH_MATCH));

		STATUS result = (STATUS)proc.env->CallShortMethod(proc.object, J_NSFSEARCHPROC::m_callback, REF_PTR(&localSearchMatch), REF_PTR(SummaryBuffer));
		return result;
	} else {
		return ERR_CANCEL;
	}
}

jclass J_NOTEEXTRACTCALLBACK::clazz;
jmethodID J_NOTEEXTRACTCALLBACK::m_callback;
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_proc_NOTEEXTRACTCALLBACK_initNative(
	JNIEnv *env, jclass clazz)
{
	if (!JNIFindClass(env, J_NOTEEXTRACTCALLBACK::clazz, "com/darwino/domino/napi/proc/NOTEEXTRACTCALLBACK")) return;
	if (!JNIGetMethodID(env, J_NOTEEXTRACTCALLBACK::m_callback, clazz, "callback", "(JI)S")) return;
}

STATUS LNCALLBACK J_NOTEEXTRACTCALLBACK::FCT_NOTEEXTRACTCALLBACK(const BYTE *bytes, DWORD length, void far *pParam) {
	J_NOTEEXTRACTCALLBACK &proc = *(J_NOTEEXTRACTCALLBACK*)pParam;
	if (proc.m_callback) {
		STATUS result = (STATUS)proc.env->CallShortMethod(proc.object, J_NOTEEXTRACTCALLBACK::m_callback, REF_PTR((void*)bytes), (jint)length);
		return result;
	} else {
		return ERR_CANCEL;
	}
}

jclass J_ActionRoutinePtr::clazz;
jmethodID J_ActionRoutinePtr::m_callback;
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_proc_ActionRoutinePtr_initNative(
	JNIEnv *env, jclass clazz)
{
	if (!JNIFindClass(env, J_ActionRoutinePtr::clazz, "com/darwino/domino/napi/proc/ActionRoutinePtr")) return;
	if (!JNIGetMethodID(env, J_ActionRoutinePtr::m_callback, clazz, "callback", "(JSI)S")) return;
}
STATUS LNCALLBACK J_ActionRoutinePtr::FCT_ActionRoutinePtr(char far *RecordPtr, WORD RecordType, DWORD RecordLength, void far *vContext) {
	J_ActionRoutinePtr &proc = *(J_ActionRoutinePtr*)vContext;
	if (proc.m_callback) {
		STATUS result = (STATUS)proc.env->CallShortMethod(proc.object, J_ActionRoutinePtr::m_callback, REF_PTR((void*)RecordPtr), (jshort)RecordType, (jint)RecordLength);
		return result;
	} else {
		return ERR_CANCEL;
	}
}

jclass J_NSFGETALLFOLDERCHANGESCALLBACK::clazz;
jmethodID J_NSFGETALLFOLDERCHANGESCALLBACK::m_callback;
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_proc_NSFGETALLFOLDERCHANGESCALLBACK_initNative(
	JNIEnv *env, jclass clazz)
{
	if (!JNIFindClass(env, J_NSFGETALLFOLDERCHANGESCALLBACK::clazz, "com/darwino/domino/napi/proc/NSFGETALLFOLDERCHANGESCALLBACK")) return;
	if (!JNIGetMethodID(env, J_NSFGETALLFOLDERCHANGESCALLBACK::m_callback, clazz, "callback", "(JJJ)S")) return;
}
STATUS LNCALLBACK J_NSFGETALLFOLDERCHANGESCALLBACK::FCT_NSFGETALLFOLDERCHANGESCALLBACK(void *Param, UNIVERSALNOTEID *NoteUNID, DHANDLE AddedNoteTable, DHANDLE RemovedNoteTable) {
	J_NSFGETALLFOLDERCHANGESCALLBACK &proc = *(J_NSFGETALLFOLDERCHANGESCALLBACK*)Param;
	if (proc.m_callback) {
		STATUS result = (STATUS)proc.env->CallShortMethod(proc.object, J_NSFGETALLFOLDERCHANGESCALLBACK::m_callback, REF_PTR((void*)NoteUNID), (jlong)AddedNoteTable, (jlong)RemovedNoteTable);
		return result;
	} else {
		return ERR_CANCEL;
	}
}

jclass J_ACLEnumFunc::clazz;
jmethodID J_ACLEnumFunc::m_callback;
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_proc_ACLEnumFunc_initNative(
	JNIEnv *env, jclass clazz)
{
	if (!JNIFindClass(env, J_ACLEnumFunc::clazz, "com/darwino/domino/napi/proc/ACLEnumFunc")) return;
	if (!JNIGetMethodID(env, J_ACLEnumFunc::m_callback, clazz, "callback", "(Ljava/lang/String;SJS)V")) return;
}
void LNCALLBACK J_ACLEnumFunc::FCT_ACLEnumFunc(void* EnumFuncParam, char *Name, WORD AccessLevel, ACL_PRIVILEGES *Privileges, WORD AccessFlags) {
	J_ACLEnumFunc &proc = *(J_ACLEnumFunc*)EnumFuncParam;
	// With no return value, there's no way to issue a CANCEL
	if (proc.m_callback) {
		jstring name = LMBCStoJavaString(proc.env, Name);
		proc.env->CallVoidMethod(proc.object, J_ACLEnumFunc::m_callback, name, (jshort)AccessLevel, REF_PTR(Privileges), (jshort)AccessFlags);
	}
}

jclass J_NSFPROFILEENUMPROC::clazz;
jmethodID J_NSFPROFILEENUMPROC::m_callback;
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_proc_NSFPROFILEENUMPROC_initNative(
		JNIEnv *env, jclass clazz)
{
	if(!JNIFindClass(env, J_NSFPROFILEENUMPROC::clazz, "com/darwino/domino/napi/proc/NSFPROFILEENUMPROC")) return;
	if(!JNIGetMethodID(env, J_NSFPROFILEENUMPROC::m_callback, clazz, "callback", "(JLjava/lang/String;Ljava/lang/String;I)S")) return;
}
STATUS LNCALLBACK J_NSFPROFILEENUMPROC::FCT_NSFPROFILEENUMPROC(DBHANDLE hDB, void *ctx, char *ProfileName, WORD ProfileNameLength, char *UserName, WORD UserNameLength, NOTEID ProfileNoteID) {
	J_NSFPROFILEENUMPROC &proc = *(J_NSFPROFILEENUMPROC*)ctx;
	if(proc.m_callback) {
		jstring jProfileName = LMBCStoJavaString(proc.env, ProfileName, ProfileNameLength);
		jstring jUserName = LMBCStoJavaString(proc.env, UserName, UserNameLength);
		STATUS result = (STATUS)proc.env->CallShortMethod(proc.object, J_NSFPROFILEENUMPROC::m_callback, dhandle_to_jdhandle(hDB), jProfileName, jUserName, (jint)ProfileNoteID);
		proc.env->DeleteLocalRef(jProfileName);
		proc.env->DeleteLocalRef(jUserName);
		return result;
	} else {
		return ERR_CANCEL;
	}
}
