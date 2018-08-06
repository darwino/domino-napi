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


extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFFormulaCompile(
	JNIEnv *env, jobject _this, jstring _formulaName, jstring _formulaText, jobject _retFormulaLength, jobject _retCompileError, jobject _retCompileErrorLine,
	jobject _retCompileErrorColumn, jobject _retCompileErrorOffset, jobject _retCompileErrorLength
	)
{
	char *formulaNameCast;
	WORD formulaNameLength;
	if (_formulaName != NULL) {
		JStringLMBCS formulaName(env, _formulaName);
		formulaNameCast = (char*)formulaName.getLMBCS();
		formulaNameLength = (WORD)formulaName.length();
	} else {
		formulaNameCast = NULL;
		formulaNameLength = 0;
	}

	JStringLMBCS formulaText(env, _formulaText);
	char *formulaTextCast = (char*)formulaText.getLMBCS();
	WORD formulaTextLength = (WORD)formulaText.length();
	
	FORMULAHANDLE hFormula;
	IntRef<WORD> retFormulaLength(env, _retFormulaLength);
	IntRef<STATUS> retCompileError(env, _retCompileError);
	IntRef<WORD> retCompileErrorLine(env, _retCompileErrorLine);
	IntRef<WORD> retCompileErrorColumn(env, _retCompileErrorColumn);
	IntRef<WORD> retCompileErrorOffset(env, _retCompileErrorOffset);
	IntRef<WORD> retCompileErrorLength(env, _retCompileErrorLength);

	if (!NCheckFormula(env, NSFFormulaCompile(formulaNameCast, formulaNameLength, formulaTextCast, formulaTextLength, &hFormula,
		retFormulaLength, retCompileError, retCompileErrorLine, retCompileErrorColumn, retCompileErrorOffset,
		retCompileErrorLength),
		retCompileError, retCompileErrorLine, retCompileErrorColumn, retCompileErrorOffset, retCompileErrorLength)) {
		return 0;
	}
	return dhandle_to_jdhandle(hFormula);
}

extern "C" JNIEXPORT jstring JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFFormulaDecompile(
	JNIEnv *env, jobject _this, jpointer _pFormulaBuffer, jboolean _fSelectionFormula)
{
	char *pFormulaBuffer = (char*)DEREF_PTR(_pFormulaBuffer);
	BOOL fSelectionFormula = (BOOL)_fSelectionFormula;
	DHANDLE hFormulaText;
	WORD formulaTextLength;

	if (NCheck(env, NSFFormulaDecompile(pFormulaBuffer, fSelectionFormula, &hFormulaText, &formulaTextLength))) {
		// Retrieve the string from the handle and return encapsulated
		char* formulaText = (char*)OSLockObject(hFormulaText);
		jstring result = LMBCStoJavaString(env, formulaText);
		OSUnlockObject(hFormulaText);
		return result;
	} else {
		return NULL;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFFormulaSummaryItem(
	JNIEnv *env, jobject _this, jlong hFormula, jstring _itemName)
{
	JStringLMBCS itemName(env, _itemName);
	const char *itemNameCast = itemName.getLMBCS();
	WORD itemNameLength = (WORD)itemName.length();

	if (!NCheck(env, NSFFormulaSummaryItem((FORMULAHANDLE)hFormula, itemNameCast, itemNameLength))) {
		return;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFFormulaMerge(
	JNIEnv *env, jobject _this, jlong hSrcFormula, jlong hDestFormula)
{
	if (!NCheck(env, NSFFormulaMerge((FORMULAHANDLE)hSrcFormula, (FORMULAHANDLE)hDestFormula))) {
		return;
	}
}

extern "C" JNIEXPORT jshort JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFFormulaGetSize(
	JNIEnv *env, jobject _this, jlong hFormula)
{
	WORD formulaLength;
	if (!NCheck(env, NSFFormulaGetSize((FORMULAHANDLE)hFormula, &formulaLength))) {
		return 0;
	}
	return formulaLength;
}

extern "C" JNIEXPORT jpointer JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFComputeStart(
	JNIEnv *env, jobject _this, jshort Flags, jpointer _lpCompiledFormula)
{
	void *lpCompiledFormula = DEREF_PTR(_lpCompiledFormula);
	HCOMPUTE hCompute;

	if (!NCheck(env, NSFComputeStart((WORD)Flags, lpCompiledFormula, &hCompute))) {
		return 0;
	}
	return REF_PTR(hCompute);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFComputeStop(
	JNIEnv *env, jobject _this, jpointer _hCompute)
{
	HCOMPUTE hCompute = DEREF_PTR(_hCompute);

	NCheck(env, NSFComputeStop(hCompute));
}

extern "C" JNIEXPORT jlong JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFComputeEvaluate(
	JNIEnv *env, jobject _this, jpointer _hCompute, jlong hNote, jobject _retResultLength, jobject _retNoteMatchesFormula, jobject _retNoteShouldBeDeleted, jobject _retNoteModified)
{
	HCOMPUTE hCompute = DEREF_PTR(_hCompute);
	DHANDLE hResult;
	IntRef<WORD> retResultLength(env, _retResultLength);
	IntRef<BOOL> retNoteMatchesFormula(env, _retNoteMatchesFormula);
	IntRef<BOOL> retNoteShouldBeDeleted(env, _retNoteShouldBeDeleted);
	IntRef<BOOL> retNoteModified(env, _retNoteModified);

	if (!NCheck(env, NSFComputeEvaluate(hCompute, (NOTEHANDLE)hNote, &hResult, retResultLength, retNoteMatchesFormula, retNoteShouldBeDeleted, retNoteModified))) {
		return 0;
	}
	return dhandle_to_jdhandle(hResult);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFSearch(
	JNIEnv *env, jobject _this, jlong _hDB, jlong _hFormula, jstring _viewTitle, jshort searchFlags, jshort noteClassMask, jobject _since, jobject _enumRoutine, jobject _retUntil)
{
	DBHANDLE hDB = jdhandle_to_dhandle(_hDB);
	FORMULAHANDLE hFormula = (FORMULAHANDLE)_hFormula;
	JStringLMBCS viewTitle(env, _viewTitle);
	Struct<TIMEDATE> since(env, _since);
	Struct<TIMEDATE> retUntil(env, _retUntil);

	J_NSFSEARCHPROC enumRoutine(env, _enumRoutine);

	STATUS result = NSFSearch(hDB, hFormula, (char*)viewTitle.getLMBCS(), (WORD)searchFlags, (WORD)noteClassMask, since, enumRoutine, &enumRoutine, retUntil);
	// Allow the use of ERR_CANCEL as a signal from the proc to cancel processing
	ERR(result) == ERR_CANCEL || NCheck(env, result);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFSearchWithUserNameList(
	JNIEnv *env, jobject _this, jlong _hDB, jlong _hFormula, jstring _viewTitle, jshort searchFlags, jshort noteClassMask, jobject _since, jobject _enumRoutine, jobject _retUntil, jlong _nameList)
{
	DBHANDLE hDB = jdhandle_to_dhandle(_hDB);
	FORMULAHANDLE hFormula = (FORMULAHANDLE)_hFormula;
	JStringLMBCS viewTitle(env, _viewTitle);
	Struct<TIMEDATE> since(env, _since);
	Struct<TIMEDATE> retUntil(env, _retUntil);
	DHANDLE nameList = jdhandle_to_dhandle(_nameList);

	J_NSFSEARCHPROC enumRoutine(env, _enumRoutine);

	STATUS result = NSFSearchWithUserNameList(hDB, hFormula, (char*)viewTitle.getLMBCS(), (WORD)searchFlags, (WORD)noteClassMask, since, enumRoutine, &enumRoutine, retUntil, nameList);
	// Allow the use of ERR_CANCEL as a signal from the proc to cancel processing
	ERR(result) == ERR_CANCEL || NCheck(env, result);
}

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_DominoAPIImpl_NSFSearchExtended3(
	JNIEnv *env, jobject _this, jdhandle _hDB, jdhandle _hFormula, jdhandle _hFilter, jint _filterFlags, jstring _viewTitle,
	jint _searchFlags, jint _searchFlags1, jint _searchFlags2, jint _searchFlags3, jint _searchFlags4, jshort _noteClassMask,
	jobject _since, jobject _enumRoutine, jobject _retUntil, jdhandle _nameList)
{
	DBHANDLE hDb = jdhandle_to_dhandle(_hDB);
	FORMULAHANDLE hFormula = (FORMULAHANDLE)_hFormula;
	DHANDLE hFilter = jdhandle_to_dhandle(_hFilter);
	DWORD filterFlags = (DWORD)_filterFlags;
	JStringLMBCS viewTitle(env, _viewTitle);
	DWORD searchFlags = (DWORD)_searchFlags;
	DWORD searchFlags1 = (DWORD)_searchFlags1;
	DWORD searchFlags2 = (DWORD)_searchFlags2;
	DWORD searchFlags3 = (DWORD)_searchFlags3;
	DWORD searchFlags4 = (DWORD)_searchFlags4;
	WORD noteClassMask = (WORD)_noteClassMask;
	Struct<TIMEDATE> since(env, _since);
	J_NSFSEARCHPROC enumRoutine(env, _enumRoutine);
	Struct<TIMEDATE> retUntil(env, _retUntil);
	DHANDLE nameList = jdhandle_to_dhandle(_nameList);
	
	STATUS result = NSFSearchExtended3(
		hDb,
		hFormula,
		hFilter,
		filterFlags,
		(char*)viewTitle.getLMBCS(),
		searchFlags,
		searchFlags1,
		searchFlags2,
		searchFlags3,
		searchFlags4,
		noteClassMask,
		since,
		enumRoutine,
		&enumRoutine,
		retUntil,
		nameList);
	
	ERR(result) == ERR_CANCEL || NCheck(env, result);
}
