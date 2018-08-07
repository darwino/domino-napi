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


////////////////////////////////////////////////////////////////////////////////////////////////////////
// Error handling
////////////////////////////////////////////////////////////////////////////////////////////////////////

void ThrowErrorRelease( JNIEnv *env, const char* msg, STATUS err ) {
    // Clear the pending exception status
    env->ExceptionDescribe();
    env->ExceptionClear();
	
	// Use a custom constructor
	jstring jmsg = env->NewStringUTF(msg);
	jthrowable throwable = (jthrowable)JNIUtils::createException(env, err, jmsg);
	env->Throw(throwable);
}

void ThrowFormulaErrorRelease(JNIEnv *env, const char* msg, STATUS err, const char* formulaMsg, STATUS formulaError, WORD errorLine, WORD errorColumn, WORD errorOffset, WORD errorLength) {
	// Clear the pending exception status
	env->ExceptionDescribe();
	env->ExceptionClear();

	// Use a custom constructor
	jstring jmsg = env->NewStringUTF(msg);
	jstring jFormulaMsg = env->NewStringUTF(formulaMsg);
	jthrowable throwable = (jthrowable)JNIUtils::createFormulaException(env, err, jmsg, formulaError, jFormulaMsg, errorLine, errorColumn, errorOffset, errorLength);
	env->Throw(throwable);
}

void ThrowErrorWithoutStackTraceRelease(JNIEnv *env, const char* msg, STATUS err) {
	// Clear the pending exception status
	env->ExceptionDescribe();
	env->ExceptionClear();

	// Use a custom constructor
	jstring jmsg = env->NewStringUTF(msg);
	jthrowable throwable = (jthrowable)JNIUtils::createExceptionWithoutStackTrace(env, err, jmsg);
	env->Throw(throwable);
}

/**
 * Checks to see if the provided STATUS is an error and, if so, raises it to Java in a
 * DominoException instance.
 */
bool NCheck( JNIEnv *env, STATUS err ) {
    if( err!=NOERROR ) {
    	// Get the notes error msg
        char buffer[512];
        OSLoadString( NULLHANDLE, ERR(err), buffer, WORD(sizeof(buffer))-1 );

		ThrowErrorRelease(env,buffer,err);

		//jprintln(buffer);
		return false;
     }

     return true;
}
/**
 * Checks to see if the provided STATUS is an error and, if so, raises it to Java in a
 * DominoException instance.
 *
 * Additionally, if the error code matches noStackTrace, this uses a "lightweight"
 * DominoException subclass that does not fill in its stack trace. This is suitable for
 * very-common "signal" errors that are intended to not be thrown further.
 */
bool NCheck(JNIEnv *env, STATUS err, STATUS noStackTrace) {
	if (err != NOERROR) {
		// Get the notes error msg
		char buffer[512];
		STATUS maskErr = ERR(err);
		OSLoadString( NULLHANDLE, maskErr, buffer, WORD(sizeof(buffer)) - 1);

		if(maskErr == noStackTrace) {
			ThrowErrorWithoutStackTraceRelease(env, buffer, err);
		} else {
			ThrowErrorRelease(env,buffer,err);
		}

		//jprintln(buffer);
		return false;
	}

	return true;
}

bool NLSCheck(JNIEnv *env, NLS_STATUS err) {
	if (err != NLS_SUCCESS) {

		const char* message;
		switch (err) {
		case NLS_BADPARM:
			message = "NLS_BADPARM";
			break;
		case NLS_BUFFERTOOSMALL:
			message = "NLS_BUFFERTOOSMALL";
			break;
		case NLS_CHARSSTRIPPED:
			message = "NLS_CHARSSTRIPPED";
			break;
		case NLS_ENDOFSTRING:
			message = "NLS_ENDOFSTRING";
			break;
		case NLS_FALLBACKUSED:
			message = "NLS_FALLBACKUSED";
			break;
		case NLS_FILEINVALID:
			message = "NLS_FILEINVALID";
			break;
		case NLS_FILENOTFOUND:
			message = "NLS_FILENOTFOUND";
			break;
		case NLS_FINDFAILED:
			message = "NLS_FINDFAILED";
			break;
		case NLS_INVALIDCHARACTER:
			message = "NLS_INVALIDCHARACTER";
			break;
		case NLS_INVALIDDATA:
			message = "NLS_INVALIDDATA";
			break;
		case NLS_INVALIDENTRY:
			message = "NLS_INVALIDENTRY";
			break;
		case NLS_INVALIDTABLE:
			message = "NLS_INVALIDTABLE";
			break;
		case NLS_PROPNOTFOUND:
			message = "NLS_PROPNOTFOUND";
			break;
		case NLS_STARTOFSTRING:
			message = "NLS_STARTOFSTRING";
			break;
		case NLS_STRINGSIZECHANGED:
			message = "NLS_STRINGSIZECHANGED";
			break;
		case NLS_TABLEHEADERINVALID:
			message = "NLS_TABLEHEADERINVALID";
			break;
		case NLS_TABLENOTFOUND:
			message = "NLS_TABLENOTFOUND";
			break;
		default:
			message = "Unknown";
		}

		ThrowErrorRelease(env, "NLS Error", err);

		jprintln("NLS Error: {0}", message);
		return false;
	}
	return true;
}

bool NCheckFormula(JNIEnv *env, STATUS err, STATUS *formulaError, WORD *errorLine, WORD *errorColumn, WORD *errorOffset, WORD *errorLength) {
	if (err != NOERROR) {
		// Get the notes error msg
		char buffer[512];
		OSLoadString(NULLHANDLE, ERR(err), buffer, WORD(sizeof(buffer)) - 1);
		char formulaBuffer[512];
		OSLoadString(NULLHANDLE, ERR(*formulaError), formulaBuffer, WORD(sizeof(formulaBuffer)) - 1);

		ThrowFormulaErrorRelease(env, buffer, err, formulaBuffer, *formulaError, *errorLine, *errorColumn, *errorOffset, *errorLength);

		//jprintln(buffer);
		return false;
	}

	return true;
}

void NPrintError( JNIEnv *env, STATUS err ) {
    if( err!=NOERROR ) {
        // Get the notes error msg
        char buffer[512];
        OSLoadString( NULLHANDLE, ERR(err), buffer, WORD(sizeof(buffer))-1 );

		jprintln(buffer);
     }
}

void ThrowNullPointerException(JNIEnv *env, const char *msg) {
	// Clear the pending exception status
	env->ExceptionDescribe();
	env->ExceptionClear();

	jclass exClass;
	const char *className = "java/lang/NullPointerException";

	exClass = env->FindClass(className);
	
	env->ThrowNew(exClass, msg);
}


////////////////////////////////////////////////////////////////////////////////////////////////////////
// String helpers
////////////////////////////////////////////////////////////////////////////////////////////////////////


jstring LMBCStoJavaString(JNIEnv *env, const char *str)
{
	if(str) {
		return LMBCStoJavaString(env,str, 0);
	}
	return NULL;
}

jstring LMBCStoJavaString(JNIEnv *env, const char *str, int len)
{
	if(str) {
		if (!*str) {
			len = 0;
		}

		WORD pSize = len == 0 ? SIZET_TO_WORD(strlen(str)) : len;
		jchar localBuffer[2048];
		jchar* unicodeBuffer;
		jstring jStr = NULL;

		if (sizeof(jchar)*(pSize+1) < sizeof (localBuffer)) {
			unicodeBuffer = localBuffer;
			memset (localBuffer, 0, sizeof (localBuffer));
		} else {
			unicodeBuffer = (jchar*)MemAlloc(sizeof(jchar)*(pSize+1));
		}

		if (unicodeBuffer != NULL) {
			pSize = OSTranslate(OS_TRANSLATE_LMBCS_TO_UNICODE,str,pSize,(char*)unicodeBuffer,((pSize + 1) * sizeof (jchar)));
			jStr = env->NewString(unicodeBuffer, pSize/sizeof(jchar));
			if (unicodeBuffer != localBuffer) {
				MemFree(unicodeBuffer);
			}
		}
		return jStr;
	}

	return NULL;
}

JStringLMBCS::JStringLMBCS(JNIEnv* env, jstring s)
{
	int length = s !=NULL ? env->GetStringLength(s) : 0;
	if(length>0) {
		const jchar* c = (jchar*)env->GetStringCritical( s, NULL );
		init(env,c,length);
		// Free the temporary java buffer
		env->ReleaseStringCritical(s, c);
	} else {
	}
}

JStringLMBCS::JStringLMBCS(JNIEnv* env, jchar* c) {
	int length = c !=NULL ? jstrlen(c) : 0;
	init(env,c,length);
}

JStringLMBCS::JStringLMBCS(JNIEnv* env, jchar* c, int length) {
	init(env,c,length);
}


void JStringLMBCS::init(JNIEnv* env, const jchar* jc, int length) {
	this->data = NULL;
	if(length > 0) {
		// Allocate the buffer that will receive the LMBCS characters
		// *3 is safe enough, but the actual string will certainly be shorter
		WORD pSize = length*3;
		if (sizeof(char)*(pSize+1) < sizeof (localBuffer)) {
			data = localBuffer;
			memset (localBuffer, 0, sizeof (localBuffer));
		} else {
			data = (char*)MemAlloc(sizeof(char)*(pSize+1));
		}

		if (data != NULL) {
			pSize = OSTranslate(OS_TRANSLATE_UNICODE_TO_LMBCS,(const char*)jc,(WORD)length*sizeof(jchar),(char*)data,(WORD)((pSize + 1) * sizeof (char)));
			data[pSize] = 0;
		}
	}
}
