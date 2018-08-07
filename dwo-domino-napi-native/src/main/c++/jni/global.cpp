/**
 * Copyright © 2014-2018 Darwino, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
#include "allinc.h"


extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NotesInit(
			JNIEnv *env, jobject _this
			) {
	NCheck(env,NotesInit());
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NotesInitExtended(
	JNIEnv *env, jobject _this, jobjectArray _argv)
{
	// Convert the incoming String[] to a native array
	int argc = env->GetArrayLength(_argv);
	char **argv = (char**)malloc(sizeof(char*) * (argc + 1));
	for (int i = 0; i < argc; i++) {
		jstring jString = (jstring)env->GetObjectArrayElement(_argv, i);
		const char *cString = env->GetStringUTFChars(jString, 0);
		char *localString = (char*)malloc(sizeof(char) * (strlen(cString) + 1));
		memset(localString, '\0', sizeof(char) * (strlen(cString) + 1));
		strcpy(localString, cString);
		argv[i] = localString;

		env->ReleaseStringUTFChars(jString, cString);
		env->DeleteLocalRef(jString);
	}

	STATUS result = NotesInitExtended(argc, argv);

	// Free the strings
	for (int i = 0; i < argc; i++) {
		free(argv[i]);
	}
	free(argv);

	NCheck(env, result);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NotesTerm(
	JNIEnv *env, jobject _this
			) {
	NotesTerm();
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NotesInitThread(
	JNIEnv *env, jobject _this
			) {
	NCheck(env,NotesInitThread());
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NotesTermThread(
	JNIEnv *env, jobject _this
			) {
	NotesTermThread();
}
