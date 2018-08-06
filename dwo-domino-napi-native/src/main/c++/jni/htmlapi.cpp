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


extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_HTMLProcessInitialize(
	JNIEnv *env, jobject _this
	) {
	NCheck(env, HTMLProcessInitialize());
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_HTMLProcessTerminate(
	JNIEnv *env, jobject _this
	) {

	// No status return on this one
	HTMLProcessTerminate();
}

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_HTMLCreateConverter(
	JNIEnv *env, jobject _this
	) {

	HTMLHANDLE hHTML;
	if (!NCheck(env, HTMLCreateConverter(&hHTML))) {
		return 0;
	}

	return memhandle_to_jlong(hHTML);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_HTMLDestroyConverter(
	JNIEnv *env, jobject _this, jlong hHTML
	) {

	if (!NCheck(env, HTMLDestroyConverter(jlong_to_memhandle(hHTML)))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_HTMLConvertItem(
	JNIEnv *env, jobject _this, jlong hHTML, jlong hDb, jlong hNote, jstring _itemName
	) {

	JStringLMBCS itemName(env, _itemName);
	char* itemNameCast = const_cast<char *>(itemName.getLMBCS());

	if (!NCheck(env, HTMLConvertItem(jlong_to_memhandle(hHTML), jdhandle_to_dhandle(hDb), jdhandle_to_dhandle(hNote), itemNameCast))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_HTMLConvertElement(
	JNIEnv *env, jobject _this, jlong hHTML, jlong hDb, jlong hNote, jstring _itemName, jint itemIndex, jint offset
	) {

	JStringLMBCS itemName(env, _itemName);
	char* itemNameCast = const_cast<char *>(itemName.getLMBCS());

	if (!NCheck(env, HTMLConvertElement(jlong_to_memhandle(hHTML), jdhandle_to_dhandle(hDb), jdhandle_to_dhandle(hNote), itemNameCast, (DWORD)itemIndex, (DWORD)offset))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_HTMLGetProperty(
	JNIEnv *env, jobject _this, jlong hHTML, jint propertyType, jpointer pProperty
	) {
	if (!NCheck(env, HTMLGetProperty(jlong_to_memhandle(hHTML), (HTMLAPI_PROP_TYPE)propertyType, (void*)pProperty))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_HTMLSetHTMLOptions(
	JNIEnv *env, jobject _this, jlong hHTML, jobjectArray _optionList
	) {

	// Convert the incoming String[] to a native array
	int stringCount = env->GetArrayLength(_optionList);
	char **optionList = (char**)malloc(sizeof(char*) * (stringCount+1));
	for (int i = 0; i < stringCount; i++) {
		jstring jString = (jstring)env->GetObjectArrayElement(_optionList, i);
		const char *cString = env->GetStringUTFChars(jString, 0);
		char *localString = (char*)malloc(sizeof(char) * (strlen(cString) + 1));
		memset(localString, '\0', sizeof(char) * (strlen(cString) + 1));
		strcpy(localString, cString);
		optionList[i] = localString;

		env->ReleaseStringUTFChars(jString, cString);
		env->DeleteLocalRef(jString);
	}
	// Add a final null pointer
	optionList[stringCount] = NULL;

	STATUS result = HTMLSetHTMLOptions(jlong_to_memhandle(hHTML), (const char**)optionList);

	// Free the strings
	for (int i = 0; i < stringCount; i++) {
		free(optionList[i]);
	}

	if (!NCheck(env, result)) {
		return;
	}
}

// TODO switch to returning a already-prepared string?
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_HTMLGetText(
	JNIEnv *env, jobject _this, jlong hHTML, jint startingOffset, jobject _textLength, jlong pText
	) {
	IntRef<DWORD> textLength(env, _textLength, true);
	NCheck(env, HTMLGetText(jlong_to_memhandle(hHTML), (DWORD)startingOffset, textLength, (char*)pText));
}

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_HTMLGetReference(
	JNIEnv *env, jobject _this, jlong hHTML, jlong Index
	) {

	MEMHANDLE hRef;

	if (!NCheck(env, HTMLGetReference(jlong_to_memhandle(hHTML), (DWORD)Index, &hRef))) {
		return 0;
	}

	return memhandle_to_jlong(hRef);
}

extern "C" JNIEXPORT jpointer JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_HTMLLockAndFixupReference(
	JNIEnv *env, jobject _this, jlong hRef
	) {

	HTMLAPIReference* ppRef;

	if (!NCheck(env, HTMLLockAndFixupReference(jlong_to_memhandle(hRef), &ppRef))) {
		return 0;
	}
	return REF_PTR(ppRef);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_HTMLSetReferenceText(
	JNIEnv *env, jobject _this, jlong hHTML, jlong Index, jstring _refText
	) {

	JStringLMBCS refText(env, _refText);
	char* refTextCast = const_cast<char *>(refText.getLMBCS());

	if (!NCheck(env, HTMLSetReferenceText(jlong_to_memhandle(hHTML), (DWORD)Index, refTextCast))) {
		return;
	}
}