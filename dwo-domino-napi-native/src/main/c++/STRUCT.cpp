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

#define OFFSET(STRUCT,MEMBER) ((jint)(size_t)((char *)&((STRUCT*)0)->MEMBER - (char *)(STRUCT*)0 ))

jclass _Struct::clazz;
jfieldID _Struct::field;

extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_BaseStruct_initNative(
			JNIEnv *env, jclass clazz
			) {
	_Struct::clazz = clazz;
	if (!JNIGetFieldID(env, _Struct::field, clazz, "data", "J")) {
		jprintln("Could not get field ID for BaseStruct.field!");
	}
}


//
// OID
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_OID_initNative(
			JNIEnv *env, jclass clazz, jintArray values
			) {
	jint* array = env->GetIntArrayElements(values,NULL);
	array[0] = sizeof(OID);
	array[1] = OFFSET(OID,File);
	array[2] = OFFSET(OID,Note);
	array[3] = OFFSET(OID,Sequence);
	array[4] = OFFSET(OID,SequenceTime);

	env->ReleaseIntArrayElements(values,array,0);
}

//
// UNIVERSALNOTEID
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_UNIVERSALNOTEID_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);
	array[0] = sizeof(UNIVERSALNOTEID);
	array[1] = OFFSET(UNIVERSALNOTEID, File);
	array[2] = OFFSET(UNIVERSALNOTEID, Note);

	env->ReleaseIntArrayElements(values, array, 0);
}


//
// TIME
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_TIME_initNative(
			JNIEnv *env, jclass clazz, jintArray values
			) {
	jint* array = env->GetIntArrayElements(values,NULL);
	array[0] = sizeof(TIME);
	array[1] = OFFSET(TIME,year);
	array[2] = OFFSET(TIME,month);
	array[3] = OFFSET(TIME,day);
	array[4] = OFFSET(TIME,weekday);
	array[5] = OFFSET(TIME,hour);
	array[6] = OFFSET(TIME,minute);
	array[7] = OFFSET(TIME,second);
	array[8] = OFFSET(TIME,hundredth);
	array[9] = OFFSET(TIME,dst);
	array[10] = OFFSET(TIME,zone);
	array[11] = OFFSET(TIME,GM);

	env->ReleaseIntArrayElements(values,array,0);
}

//
// TIMEDATE
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_TIMEDATE_initNative(
			JNIEnv *env, jclass clazz, jintArray values
			) {
	jint* array = env->GetIntArrayElements(values,NULL);
	array[0] = sizeof(TIMEDATE);
	array[1] = OFFSET(TIMEDATE,Innards);

	env->ReleaseIntArrayElements(values,array,0);
}


//
// BLOCKID
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_BLOCKID_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);
	array[0] = sizeof(BLOCKID);
	array[1] = OFFSET(BLOCKID, pool);
	array[2] = OFFSET(BLOCKID, block);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// RANGE
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_RANGE_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);
	array[0] = sizeof(RANGE);
	array[1] = OFFSET(RANGE, ListEntries);
	array[2] = OFFSET(RANGE, RangeEntries);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// NUMBER_PAIR
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_NUMBER_1PAIR_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);
	array[0] = sizeof(NUMBER_PAIR);
	array[1] = OFFSET(NUMBER_PAIR, Lower);
	array[2] = OFFSET(NUMBER_PAIR, Upper);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// TIMEDATE_PAIR
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_TIMEDATE_1PAIR_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);
	array[0] = sizeof(TIMEDATE_PAIR);
	array[1] = OFFSET(TIMEDATE_PAIR, Lower);
	array[2] = OFFSET(TIMEDATE_PAIR, Upper);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// LIST
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_LIST_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);
	array[0] = sizeof(LIST);
	array[1] = OFFSET(LIST, ListEntries);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// HTMLAPI_URLArg
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_HTMLAPI_1URLArg_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);
	array[0] = sizeof(HTMLAPI_URLArg);
	array[1] = OFFSET(HTMLAPI_URLArg, Id);
	array[2] = OFFSET(HTMLAPI_URLArg, Type);
	array[3] = OFFSET(HTMLAPI_URLArg, Value);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// HTMLAPI_URLTargetComponent
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_HTMLAPI_1URLTargetComponent_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);
	array[0] = sizeof(HTMLAPI_URLTargetComponent);
	array[1] = OFFSET(HTMLAPI_URLTargetComponent, AddressableType);
	array[2] = OFFSET(HTMLAPI_URLTargetComponent, ReferenceType);
	array[3] = OFFSET(HTMLAPI_URLTargetComponent, Value);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// HTMLAPIReference
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_HTMLAPIReference_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);
	array[0] = sizeof(HTMLAPIReference);
	array[1] = OFFSET(HTMLAPIReference, RefType);
	array[2] = OFFSET(HTMLAPIReference, pRefText);
	array[3] = OFFSET(HTMLAPIReference, CommandId);
	array[4] = OFFSET(HTMLAPIReference, pTargets);
	array[5] = OFFSET(HTMLAPIReference, pArgs);
	array[6] = OFFSET(HTMLAPIReference, NumTargets);
	array[7] = OFFSET(HTMLAPIReference, NumArgs);
	array[8] = OFFSET(HTMLAPIReference, pFragment);
	array[9] = OFFSET(HTMLAPIReference, URLParts);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// MIME_PART
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_MIME_1PART_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(MIME_PART);
	array[1] = OFFSET(MIME_PART, wVersion);
	array[2] = OFFSET(MIME_PART, dwFlags);
	array[3] = OFFSET(MIME_PART, cPartType);
	array[4] = OFFSET(MIME_PART, cSpare);
	array[5] = OFFSET(MIME_PART, wByteCount);
	array[6] = OFFSET(MIME_PART, wBoundaryLen);
	array[7] = OFFSET(MIME_PART, wHeadersLen);
	array[8] = OFFSET(MIME_PART, wSpare);
	array[9] = OFFSET(MIME_PART, dwSpare);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// VIEW_TABLE_FORMAT
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_VIEW_1TABLE_1FORMAT_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(VIEW_TABLE_FORMAT);
	array[1] = OFFSET(VIEW_TABLE_FORMAT, Header);
	array[2] = OFFSET(VIEW_TABLE_FORMAT, Columns);
	array[3] = OFFSET(VIEW_TABLE_FORMAT, ItemSequenceNumber);
	array[4] = OFFSET(VIEW_TABLE_FORMAT, Flags);
	array[5] = OFFSET(VIEW_TABLE_FORMAT, Flags2);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// VIEW_FORMAT_HEADER
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_VIEW_1FORMAT_1HEADER_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(VIEW_FORMAT_HEADER);
	array[1] = OFFSET(VIEW_FORMAT_HEADER, Version);
	array[2] = OFFSET(VIEW_FORMAT_HEADER, ViewStyle);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// VIEW_TABLE_FORMAT2
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_VIEW_1TABLE_1FORMAT2_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(VIEW_TABLE_FORMAT2);
	array[1] = OFFSET(VIEW_TABLE_FORMAT2, Length);
	array[2] = OFFSET(VIEW_TABLE_FORMAT2, BackgroundColor);
	array[3] = OFFSET(VIEW_TABLE_FORMAT2, V2BorderColor);
	array[4] = OFFSET(VIEW_TABLE_FORMAT2, TitleFont);
	array[5] = OFFSET(VIEW_TABLE_FORMAT2, UnreadFont);
	array[6] = OFFSET(VIEW_TABLE_FORMAT2, TotalsFont);
	array[7] = OFFSET(VIEW_TABLE_FORMAT2, AutoUpdateSeconds);
	array[8] = OFFSET(VIEW_TABLE_FORMAT2, AlternateBackgroundColor);
	array[9] = OFFSET(VIEW_TABLE_FORMAT2, wSig);
	array[10] = OFFSET(VIEW_TABLE_FORMAT2, LineCount);
	array[11] = OFFSET(VIEW_TABLE_FORMAT2, Spacing);
	array[12] = OFFSET(VIEW_TABLE_FORMAT2, BackgroundColorExt);
	array[13] = OFFSET(VIEW_TABLE_FORMAT2, HeaderLineCount);
	array[14] = OFFSET(VIEW_TABLE_FORMAT2, Flags1);
	array[15] = OFFSET(VIEW_TABLE_FORMAT2, Spare);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// VIEW_TABLE_FORMAT3
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_VIEW_1TABLE_1FORMAT3_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(VIEW_TABLE_FORMAT3);
	array[1] = OFFSET(VIEW_TABLE_FORMAT3, Length);
	array[2] = OFFSET(VIEW_TABLE_FORMAT3, Flags);
	array[3] = OFFSET(VIEW_TABLE_FORMAT3, BackgroundColor);
	array[4] = OFFSET(VIEW_TABLE_FORMAT3, AlternateBackgroundColor);
	array[5] = OFFSET(VIEW_TABLE_FORMAT3, GridColorValue);
	array[6] = OFFSET(VIEW_TABLE_FORMAT3, wViewMarginTop);
	array[7] = OFFSET(VIEW_TABLE_FORMAT3, wViewMarginLeft);
	array[8] = OFFSET(VIEW_TABLE_FORMAT3, wViewMarginRight);
	array[9] = OFFSET(VIEW_TABLE_FORMAT3, wViewMarginBottom);
	array[10] = OFFSET(VIEW_TABLE_FORMAT3, MarginBackgroundColor);
	array[11] = OFFSET(VIEW_TABLE_FORMAT3, HeaderBackgroundColor);
	array[12] = OFFSET(VIEW_TABLE_FORMAT3, wViewMarginTopUnder);
	array[13] = OFFSET(VIEW_TABLE_FORMAT3, UnreadColor);
	array[14] = OFFSET(VIEW_TABLE_FORMAT3, TotalsColor);
	array[15] = OFFSET(VIEW_TABLE_FORMAT3, wMaxRows);
	array[16] = OFFSET(VIEW_TABLE_FORMAT3, wThemeSetting);
	array[17] = OFFSET(VIEW_TABLE_FORMAT3, dwReserved);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// VIEW_COLUMN_FORMAT
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_VIEW_1COLUMN_1FORMAT_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(VIEW_COLUMN_FORMAT);
	array[1] = OFFSET(VIEW_COLUMN_FORMAT, Signature);
	array[2] = OFFSET(VIEW_COLUMN_FORMAT, Flags1);
	array[3] = OFFSET(VIEW_COLUMN_FORMAT, ItemNameSize);
	array[4] = OFFSET(VIEW_COLUMN_FORMAT, TitleSize);
	array[5] = OFFSET(VIEW_COLUMN_FORMAT, FormulaSize);
	array[6] = OFFSET(VIEW_COLUMN_FORMAT, ConstantValueSize);
	array[7] = OFFSET(VIEW_COLUMN_FORMAT, DisplayWidth);
	array[8] = OFFSET(VIEW_COLUMN_FORMAT, FontID);
	array[9] = OFFSET(VIEW_COLUMN_FORMAT, Flags2);
	array[10] = OFFSET(VIEW_COLUMN_FORMAT, NumberFormat);
	array[11] = OFFSET(VIEW_COLUMN_FORMAT, TimeFormat);
	array[12] = OFFSET(VIEW_COLUMN_FORMAT, FormatDataType);
	array[13] = OFFSET(VIEW_COLUMN_FORMAT, ListSep);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// VIEW_COLUMN_FORMAT2
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_VIEW_1COLUMN_1FORMAT2_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(VIEW_COLUMN_FORMAT2);
	array[1] = OFFSET(VIEW_COLUMN_FORMAT2, Signature);
	array[2] = OFFSET(VIEW_COLUMN_FORMAT2, HeaderFontID);
	array[3] = OFFSET(VIEW_COLUMN_FORMAT2, ResortToViewUNID);
	array[4] = OFFSET(VIEW_COLUMN_FORMAT2, wSecondResortColumnIndex);
	array[5] = OFFSET(VIEW_COLUMN_FORMAT2, Flags3);
	array[6] = OFFSET(VIEW_COLUMN_FORMAT2, wHideWhenFormulaSize);
	array[7] = OFFSET(VIEW_COLUMN_FORMAT2, wTwistieResourceSize);
	array[8] = OFFSET(VIEW_COLUMN_FORMAT2, wCustomOrder);
	array[9] = OFFSET(VIEW_COLUMN_FORMAT2, wCustomHiddenFlags);
	array[10] = OFFSET(VIEW_COLUMN_FORMAT2, ColumnColor);
	array[11] = OFFSET(VIEW_COLUMN_FORMAT2, HeaderFontColor);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// NFMT
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_NFMT_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(NFMT);
	array[1] = OFFSET(NFMT, Digits);
	array[2] = OFFSET(NFMT, Format);
	array[3] = OFFSET(NFMT, Attributes);
	array[4] = OFFSET(NFMT, Unused);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// TFMT
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_TFMT_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(TFMT);
	array[1] = OFFSET(TFMT, Date);
	array[2] = OFFSET(TFMT, Time);
	array[3] = OFFSET(TFMT, Zone);
	array[4] = OFFSET(TFMT, Structure);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// COLLATE_DESCRIPTOR
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_COLLATE_1DESCRIPTOR_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(COLLATE_DESCRIPTOR);
	array[1] = OFFSET(COLLATE_DESCRIPTOR, Flags);
	array[2] = OFFSET(COLLATE_DESCRIPTOR, signature);
	array[3] = OFFSET(COLLATE_DESCRIPTOR, keytype);
	array[4] = OFFSET(COLLATE_DESCRIPTOR, NameOffset);
	array[5] = OFFSET(COLLATE_DESCRIPTOR, NameLength);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// COLLATION
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_COLLATION_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(COLLATION);
	array[1] = OFFSET(COLLATION, BufferSize);
	array[2] = OFFSET(COLLATION, Items);
	array[3] = OFFSET(COLLATION, Flags);
	array[4] = OFFSET(COLLATION, signature);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// COLLECTIONPOSITION
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_COLLECTIONPOSITION_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(COLLECTIONPOSITION);
	array[1] = OFFSET(COLLECTIONPOSITION, Level);
	array[2] = OFFSET(COLLECTIONPOSITION, MinLevel);
	array[3] = OFFSET(COLLECTIONPOSITION, MaxLevel);
	array[4] = OFFSET(COLLECTIONPOSITION, Tumbler);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// ITEM_TABLE
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_ITEM_1TABLE_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(ITEM_TABLE);
	array[1] = OFFSET(ITEM_TABLE, Length);
	array[2] = OFFSET(ITEM_TABLE, Items);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// ITEM_VALUE_TABLE
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_ITEM_1VALUE_1TABLE_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(ITEM_VALUE_TABLE);
	array[1] = OFFSET(ITEM_VALUE_TABLE, Length);
	array[2] = OFFSET(ITEM_VALUE_TABLE, Items);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// ITEM
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_ITEM_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(ITEM);
	array[1] = OFFSET(ITEM, NameLength);
	array[2] = OFFSET(ITEM, ValueLength);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// COLLECTIONDATA
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_COLLECTIONDATA_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(COLLECTIONDATA);
	array[1] = OFFSET(COLLECTIONDATA, DocCount);
	array[2] = OFFSET(COLLECTIONDATA, DocTotalSize);
	array[3] = OFFSET(COLLECTIONDATA, BTreeLeafNodes);
	array[4] = OFFSET(COLLECTIONDATA, BTreeDepth);
	array[5] = OFFSET(COLLECTIONDATA, Spare);
	array[6] = OFFSET(COLLECTIONDATA, KeyOffset);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// COLLECTIONSTATS
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_COLLECTIONSTATS_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(COLLECTIONSTATS);
	array[1] = OFFSET(COLLECTIONSTATS, TopLevelEntries);
	array[2] = OFFSET(COLLECTIONSTATS, spare); // This is actually LastModifiedTime according to the doc NSF, but not the .h

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// FILEOBJECT
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_FILEOBJECT_initNative(
	JNIEnv *env, jclass clazz, jintArray values
	) {
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(FILEOBJECT);
	array[1] = OFFSET(FILEOBJECT, Header);
	array[2] = OFFSET(FILEOBJECT, FileNameLength);
	array[3] = OFFSET(FILEOBJECT, HostType);
	array[4] = OFFSET(FILEOBJECT, CompressionType);
	array[5] = OFFSET(FILEOBJECT, FileAttributes);
	array[6] = OFFSET(FILEOBJECT, Flags);
	array[7] = OFFSET(FILEOBJECT, FileSize);
	array[8] = OFFSET(FILEOBJECT, FileCreated);
	array[9] = OFFSET(FILEOBJECT, FileModified);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// OBJECT_DESCRIPTOR
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_OBJECT_1DESCRIPTOR_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint* array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(OBJECT_DESCRIPTOR);
	array[1] = OFFSET(OBJECT_DESCRIPTOR, ObjectType);
	array[2] = OFFSET(OBJECT_DESCRIPTOR, RRV);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// GLOBALINSTANCEID
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_GLOBALINSTANCEID_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(GLOBALINSTANCEID);
	array[1] = OFFSET(GLOBALINSTANCEID, File);
	array[2] = OFFSET(GLOBALINSTANCEID, Note);
	array[3] = OFFSET(GLOBALINSTANCEID, NoteID);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// SEARCH_MATCH
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_SEARCH_1MATCH_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(SEARCH_MATCH);
	array[1] = OFFSET(SEARCH_MATCH, ID);
	array[2] = OFFSET(SEARCH_MATCH, OriginatorID);
	array[3] = OFFSET(SEARCH_MATCH, NoteClass);
	array[4] = OFFSET(SEARCH_MATCH, SERetFlags);
	array[5] = OFFSET(SEARCH_MATCH, Privileges);
	array[6] = OFFSET(SEARCH_MATCH, SummaryLength);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// DBREPLICAINFO
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_DBREPLICAINFO_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(DBREPLICAINFO);
	array[1] = OFFSET(DBREPLICAINFO, ID);
	array[2] = OFFSET(DBREPLICAINFO, Flags);
	array[3] = OFFSET(DBREPLICAINFO, CutoffInterval);
	array[4] = OFFSET(DBREPLICAINFO, Cutoff);

	env->ReleaseIntArrayElements(values, array, 0);
}


//
// NAMES_LIST
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_NAMES_1LIST_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(NAMES_LIST);
	array[1] = OFFSET(NAMES_LIST, NumNames);
	array[2] = OFFSET(NAMES_LIST, License);
	array[3] = OFFSET(NAMES_LIST, Authenticated);

	env->ReleaseIntArrayElements(values, array, 0);
}


///////////////////////////////////////////////////////////////////////////////////////////////////////
// Composite Data Begin
///////////////////////////////////////////////////////////////////////////////////////////////////////

//
// BSIG
// 
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_BSIG_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(BSIG);
	array[1] = OFFSET(BSIG, Signature);
	array[2] = OFFSET(BSIG, Length);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// WSIG
// 
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_WSIG_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(WSIG);
	array[1] = OFFSET(WSIG, Signature);
	array[2] = OFFSET(WSIG, Length);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// LSIG
// 
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_LSIG_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(LSIG);
	array[1] = OFFSET(LSIG, Signature);
	array[2] = OFFSET(LSIG, Length);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// CDHOTSPOTBEGIN
// 
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_CDHOTSPOTBEGIN_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(CDHOTSPOTBEGIN);
	array[1] = OFFSET(CDHOTSPOTBEGIN, Header);
	array[2] = OFFSET(CDHOTSPOTBEGIN, Type);
	array[3] = OFFSET(CDHOTSPOTBEGIN, Flags);
	array[4] = OFFSET(CDHOTSPOTBEGIN, DataLength);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// CDGRAPHIC
// 
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_CDGRAPHIC_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(CDGRAPHIC);
	array[1] = OFFSET(CDGRAPHIC, Header);
	array[2] = OFFSET(CDGRAPHIC, DestSize);
	array[3] = OFFSET(CDGRAPHIC, CropSize);
	array[4] = OFFSET(CDGRAPHIC, CropOffset);
	array[5] = OFFSET(CDGRAPHIC, fResize);
	array[6] = OFFSET(CDGRAPHIC, Version);
	array[7] = OFFSET(CDGRAPHIC, bFlags);
	array[8] = OFFSET(CDGRAPHIC, wReserved);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// CDTEXT
// 
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_CDTEXT_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(CDTEXT);
	array[1] = OFFSET(CDTEXT, Header);
	array[2] = OFFSET(CDTEXT, FontID);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// CDBEGINRECORD
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_CDBEGINRECORD_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(CDBEGINRECORD);
	array[1] = OFFSET(CDBEGINRECORD, Header);
	array[2] = OFFSET(CDBEGINRECORD, Version);
	array[3] = OFFSET(CDBEGINRECORD, Signature);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// CDENDRECORD
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_CDENDRECORD_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(CDENDRECORD);
	array[1] = OFFSET(CDENDRECORD, Header);
	array[2] = OFFSET(CDENDRECORD, Version);
	array[3] = OFFSET(CDENDRECORD, Signature);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// CDFIELD
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_CDFIELD_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(CDFIELD);
	array[1] = OFFSET(CDFIELD, Header);
	array[2] = OFFSET(CDFIELD, Flags);
	array[3] = OFFSET(CDFIELD, DataType);
	array[4] = OFFSET(CDFIELD, ListDelim);
	array[5] = OFFSET(CDFIELD, NumberFormat);
	array[6] = OFFSET(CDFIELD, TimeFormat);
	array[7] = OFFSET(CDFIELD, FontID);
	array[8] = OFFSET(CDFIELD, DVLength);
	array[9] = OFFSET(CDFIELD, ITLength);
	array[10] = OFFSET(CDFIELD, TabOrder);
	array[11] = OFFSET(CDFIELD, IVLength);
	array[12] = OFFSET(CDFIELD, NameLength);
	array[13] = OFFSET(CDFIELD, DescLength);
	array[14] = OFFSET(CDFIELD, TextValueLength);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// CDFIELDHINT
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_CDFIELDHINT_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(CDFIELDHINT);
	array[1] = OFFSET(CDFIELDHINT, Header);
	array[2] = OFFSET(CDFIELDHINT, HintTextLength);
	array[3] = OFFSET(CDFIELDHINT, Flags);
	array[4] = OFFSET(CDFIELDHINT, Spare);
	array[5] = OFFSET(CDFIELDHINT, Spare2);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// CDEXTFIELD
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_CDEXTFIELD_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(CDEXTFIELD);
	array[1] = OFFSET(CDEXTFIELD, Header);
	array[2] = OFFSET(CDEXTFIELD, Flags1);
	array[3] = OFFSET(CDEXTFIELD, Flags2);
	array[4] = OFFSET(CDEXTFIELD, EntryHelper);
	array[5] = OFFSET(CDEXTFIELD, EntryDBNameLen);
	array[6] = OFFSET(CDEXTFIELD, EntryViewNameLen);
	array[7] = OFFSET(CDEXTFIELD, EntryColumnNumber);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// CDEXT2FIELD
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_CDEXT2FIELD_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);


	array[0] = sizeof(CDEXT2FIELD);
	array[1] = OFFSET(CDEXT2FIELD, Header);
	array[2] = OFFSET(CDEXT2FIELD, NumSymPref);
	array[3] = OFFSET(CDEXT2FIELD, NumSymFlags);
	array[4] = OFFSET(CDEXT2FIELD, DecimalSymLength);
	array[5] = OFFSET(CDEXT2FIELD, MilliSepSymLength);
	array[6] = OFFSET(CDEXT2FIELD, NegativeSymLength);
	array[7] = OFFSET(CDEXT2FIELD, MilliGroupSize);
	array[8] = OFFSET(CDEXT2FIELD, VerticalSpacing);
	array[9] = OFFSET(CDEXT2FIELD, HorizontalSpacing);
	array[10] = OFFSET(CDEXT2FIELD, Unused2);
	array[11] = OFFSET(CDEXT2FIELD, FirstFieldLimitType);
	array[12] = OFFSET(CDEXT2FIELD, CurrencyPref);
	array[13] = OFFSET(CDEXT2FIELD, CurrencyType);
	array[14] = OFFSET(CDEXT2FIELD, CurrencyFlags);
	array[15] = OFFSET(CDEXT2FIELD, CurrencySymLength);
	array[16] = OFFSET(CDEXT2FIELD, ISOCountry);
	array[17] = OFFSET(CDEXT2FIELD, ThumbnailImageWidth);
	array[18] = OFFSET(CDEXT2FIELD, ThumbnailImageHeight);
	array[19] = OFFSET(CDEXT2FIELD, wThumbnailImageFileNameLength);
	array[20] = OFFSET(CDEXT2FIELD, wIMOnlineNameFormulaLen);
	array[21] = OFFSET(CDEXT2FIELD, DTPref);
	array[22] = OFFSET(CDEXT2FIELD, DTFlags);
	array[23] = OFFSET(CDEXT2FIELD, DTFlags2);
	array[24] = OFFSET(CDEXT2FIELD, DTDOWFmt);
	array[25] = OFFSET(CDEXT2FIELD, DTYearFmt);
	array[26] = OFFSET(CDEXT2FIELD, DTMonthFmt);
	array[27] = OFFSET(CDEXT2FIELD, DTDayFmt);
	array[28] = OFFSET(CDEXT2FIELD, DTDsep1Len);
	array[29] = OFFSET(CDEXT2FIELD, DTDsep2Len);
	array[30] = OFFSET(CDEXT2FIELD, DTDsep3Len);
	array[31] = OFFSET(CDEXT2FIELD, DTTsepLen);
	array[32] = OFFSET(CDEXT2FIELD, DTDShow);
	array[33] = OFFSET(CDEXT2FIELD, DTDSpecial);
	array[34] = OFFSET(CDEXT2FIELD, DTTShow);
	array[35] = OFFSET(CDEXT2FIELD, DTTZone);
	array[36] = OFFSET(CDEXT2FIELD, Unused5);
	array[37] = OFFSET(CDEXT2FIELD, ECFlags);
	array[38] = OFFSET(CDEXT2FIELD, Unused612);
	array[39] = OFFSET(CDEXT2FIELD, wCharacters);
	array[40] = OFFSET(CDEXT2FIELD, wInputEnabledLen);
	array[41] = OFFSET(CDEXT2FIELD, wIMGroupFormulaLen);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// CDDATAFLAGS
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_CDDATAFLAGS_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(CDDATAFLAGS);
	array[1] = OFFSET(CDDATAFLAGS, Header);
	array[2] = OFFSET(CDDATAFLAGS, nFlags);
	array[3] = OFFSET(CDDATAFLAGS, elemType);
	array[4] = OFFSET(CDDATAFLAGS, dwReserved);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// CDKEYWORD
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_CDKEYWORD_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(CDKEYWORD);
	array[1] = OFFSET(CDKEYWORD, Header);
	array[2] = OFFSET(CDKEYWORD, FontID);
	array[3] = OFFSET(CDKEYWORD, Keywords);
	array[4] = OFFSET(CDKEYWORD, Flags);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// CDEMBEDDEDCTL
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_CDEMBEDDEDCTL_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(CDEMBEDDEDCTL);
	array[1] = OFFSET(CDEMBEDDEDCTL, Header);
	array[2] = OFFSET(CDEMBEDDEDCTL, CtlStyle);
	array[3] = OFFSET(CDEMBEDDEDCTL, Flags);
	array[4] = OFFSET(CDEMBEDDEDCTL, Width);
	array[5] = OFFSET(CDEMBEDDEDCTL, Height);
	array[6] = OFFSET(CDEMBEDDEDCTL, Version);
	array[7] = OFFSET(CDEMBEDDEDCTL, CtlType);
	array[8] = OFFSET(CDEMBEDDEDCTL, MaxChars);
	array[9] = OFFSET(CDEMBEDDEDCTL, MaxLines);
	array[10] = OFFSET(CDEMBEDDEDCTL, Percentage);
	array[11] = OFFSET(CDEMBEDDEDCTL, Spare);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// CDFILEHEADER
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_CDFILEHEADER_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(CDFILEHEADER);
	array[1] = OFFSET(CDFILEHEADER, Header);
	array[2] = OFFSET(CDFILEHEADER, FileExtLen);
	array[3] = OFFSET(CDFILEHEADER, FileDataSize);
	array[4] = OFFSET(CDFILEHEADER, SegCount);
	array[5] = OFFSET(CDFILEHEADER, Flags);
	array[6] = OFFSET(CDFILEHEADER, Reserved);

	env->ReleaseIntArrayElements(values, array, 0);
}

//
// CDFILESEGMENT
//
extern "C" JNIEXPORT void JNICALL Java_com_darwino_domino_napi_struct_cd_CDFILESEGMENT_initNative(
	JNIEnv *env, jclass clazz, jintArray values)
{
	jint *array = env->GetIntArrayElements(values, NULL);

	array[0] = sizeof(CDFILESEGMENT);
	array[1] = OFFSET(CDFILESEGMENT, Header);
	array[2] = OFFSET(CDFILESEGMENT, DataSize);
	array[3] = OFFSET(CDFILESEGMENT, SegSize);
	array[4] = OFFSET(CDFILESEGMENT, Flags);
	array[5] = OFFSET(CDFILESEGMENT, Reserved);

	env->ReleaseIntArrayElements(values, array, 0);
}
