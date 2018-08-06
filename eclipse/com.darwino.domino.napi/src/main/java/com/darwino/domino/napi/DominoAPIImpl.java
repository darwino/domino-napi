/*!COPYRIGHT HEADER! - CONFIDENTIAL 
 *
 * Darwino Inc Confidential.
 *
 * (c) Copyright Darwino Inc. 2014-2018.
 *
 * Notice: The information contained in the source code for these files is the property 
 * of Darwino Inc. which, with its licensors, if any, owns all the intellectual property 
 * rights, including all copyright rights thereto.  Such information may only be used 
 * for debugging, troubleshooting and informational purposes.  All other uses of this information, 
 * including any production or commercial uses, are prohibited. 
 */

package com.darwino.domino.napi;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;

import com.darwino.domino.napi.c.BaseIntRef;
import com.darwino.domino.napi.c.ByteRef;
import com.darwino.domino.napi.c.IntRef;
import com.darwino.domino.napi.c.LongRef;
import com.darwino.domino.napi.c.ShortRef;
import com.darwino.domino.napi.proc.ACLEnumFunc;
import com.darwino.domino.napi.proc.ActionRoutinePtr;
import com.darwino.domino.napi.proc.IDENUMERATEPROC;
import com.darwino.domino.napi.proc.NOTEEXTRACTCALLBACK;
import com.darwino.domino.napi.proc.NSFGETALLFOLDERCHANGESCALLBACK;
import com.darwino.domino.napi.proc.NSFITEMSCANPROC;
import com.darwino.domino.napi.proc.NSFPROFILEENUMPROC;
import com.darwino.domino.napi.proc.NSFSEARCHPROC;
import com.darwino.domino.napi.struct.BLOCKID;
import com.darwino.domino.napi.struct.COLLECTIONPOSITION;
import com.darwino.domino.napi.struct.DBREPLICAINFO;
import com.darwino.domino.napi.struct.ITEM_TABLE;
import com.darwino.domino.napi.struct.OID;
import com.darwino.domino.napi.struct.TFMT;
import com.darwino.domino.napi.struct.TIME;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.struct.UNIVERSALNOTEID;
import com.darwino.domino.napi.util.DominoNativeUtils;


/**
 * Native Domino API.
 * 
 * @author priand
 */
public class DominoAPIImpl extends DominoAPI {

	private static boolean initialized;

	private static synchronized void init() {
		if (!initialized) {
			initialized = true;
			try {
				boolean loaded = false;
				if(StringUtil.isNotEmpty(System.getProperty("org.osgi.framework.version"))) { //$NON-NLS-1$
					// Then use the OSGi native lib route
					try {
						System.loadLibrary("dominonapi"); //$NON-NLS-1$
						loaded = true;
					} catch(UnsatisfiedLinkError e) {
						// May be an embedded jar or other not-quite-working environment
					}
				}
				
				if(!loaded) {
					if (DominoNativeUtils.isWinx86()) {
						DominoNativeUtils.loadLibraryFromJar(DominoAPI.class.getClassLoader(), "lib/win32/dominonapi.dll"); //$NON-NLS-1$
					} else if (DominoNativeUtils.isWinx64()) {
						DominoNativeUtils.loadLibraryFromJar(DominoAPI.class.getClassLoader(), "lib/win64/dominonapi.dll"); //$NON-NLS-1$
					} else if (DominoNativeUtils.isMac_x64()) {
						DominoNativeUtils.loadLibraryFromJar(DominoAPI.class.getClassLoader(), "lib/mac/libdominonapi.jnilib"); //$NON-NLS-1$
					} else if (DominoNativeUtils.isLinux_x86()) {
						DominoNativeUtils.loadLibraryFromJar(DominoAPI.class.getClassLoader(), "lib/linux32/libdominonapi.so"); //$NON-NLS-1$
					} else if (DominoNativeUtils.isLinux_x64()) {
						DominoNativeUtils.loadLibraryFromJar(DominoAPI.class.getClassLoader(), "lib/linux64/libdominonapi.so"); //$NON-NLS-1$
					} else {
						String os = System.getProperty("os.name"); //$NON-NLS-1$
						String arch = System.getProperty("os.arch"); //$NON-NLS-1$
						String message = StringUtil.format("The current platform is not a supported one. (os={0}, arch={1})", os, arch); //$NON-NLS-1$
						throw new UnsupportedOperationException(message);
					}
				}
			} catch (Throwable e) {
				Platform.getInstance().log(e);
				throw new IllegalStateException(e);
			}
		}
	}

	public DominoAPIImpl() {
		init();
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// global.h 
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override public native void NotesInit() throws DominoException;
    @Override public native void NotesInitExtended(String... argv) throws DominoException;
    @Override public native void NotesTerm() throws DominoException;
    @Override public native void NotesInitThread() throws DominoException;
    @Override public native void NotesTermThread() throws DominoException;

    
    
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// osfile.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override public native String OSGetDataDirectory() throws DominoException;
    @Override public native String OSGetExecutableDirectory() throws DominoException;
    @Override public native String OSPathNetConstruct(String portName, String serverName, String fileName) throws DominoException;
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// osenv.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override public native int OSGetEnvironmentInt(String name) throws DominoException;
	@Override public native long OSGetEnvironmentLong(String name) throws DominoException;
	@Override public native String OSGetEnvironmentString(String name) throws DominoException;
	@Override public native void OSSetEnvironmentInt(String name, int value) throws DominoException;
	@Override public native void OSSetEnvironmentVariable(String name, String value) throws DominoException;

	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ostime.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override public native void OSCurrentTimeZone(BaseIntRef retZone, BaseIntRef retDST) throws DominoException;
	@Override public native void OSCurrentTIMEDATE(TIMEDATE retTimeDate) throws DominoException;


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// osmem.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override public native long OSLockObject(long Handle);
	@Override public native void OSUnlockObject(long Handle);
	@Override public native void OSMemoryFree(long handle);
	@Override public native long OSMemoryLock(long handle);
	@Override public native boolean OSMemoryUnlock(long handle);
	@Override public native long OSMemAlloc(short blkType, int size) throws DominoException;
	@Override public native void OSMemFree(long Handle) throws DominoException;
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// osmisc.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override public native short OSTranslate(short translateMode, long inPtr, short inLength, long outPtr, short outLength);
    @Override public native long OSGetLMBCSCLS();
    @Override public native long OSGetNativeCLS();
    @Override public native void ConvertTextToTIMEDATE(long intlFormat, TFMT textFormat, long ppText, short maxLength, TIMEDATE retTimeDate) throws DominoException;
    @Override public native String ConvertTIMEDATEToText(long intlFormat, TFMT textFormat, TIMEDATE inputTime) throws DominoException;
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// misc.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override public native void TimeConstant(short type, TIMEDATE td) throws DominoException;
	@Override public native void TimeGMToLocal(TIME time) throws DominoException;
	@Override public native void TimeGMToLocalZone(TIME time) throws DominoException;
	@Override public native void TimeLocalToGM(TIME time) throws DominoException;
	@Override public native int  TimeDateDifference(TIMEDATE td1, TIMEDATE td2);
	@Override public native int  TimeDateCompare(TIMEDATE td1, TIMEDATE td2);
	@Override public native void TimeDateIncrement(TIMEDATE td, int interval) throws DominoException;
	@Override public native int  TimeExtractDate(TIMEDATE td);
	@Override public native void TimeDateClear(TIMEDATE td);
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nsfdb.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override public native long NSFDbOpen(String dbPath) throws DominoException;
    @Override public native long NSFDbOpenExtended(String dbPath, short options, long hNames, TIMEDATE modifiedTime, TIMEDATE retDataModified, TIMEDATE retNonDataModified) throws DominoException;
    @Override public native void NSFDbClose(long hDb) throws DominoException;
    @Override public native long NSFDbGetModifiedNoteTable(long hDb, short noteClassMask, TIMEDATE since, TIMEDATE until) throws DominoException;
    @Override public native long NSFBuildNamesList(String userName, int flags) throws DominoException;
    
    // The returned boolean is true if the ID was not deleted! 
    @Override public native boolean NSFDbGetNoteInfo(long hDb, int noteID, OID oid, TIMEDATE retModified, IntRef retNoteClass) throws DominoException;
    @Override public native boolean NSFDbGetNoteInfoExt(long hDb, int noteID, OID oid, TIMEDATE retModified, IntRef retNoteClass, TIMEDATE retAddedToFile, IntRef retResponseCount, IntRef retParentNoteID) throws DominoException;
    @Override public native boolean NSFDbGetNoteInfoByUNID(long hDb, UNIVERSALNOTEID unid, IntRef retNoteID, OID oid, TIMEDATE retModified, IntRef retNoteClass) throws DominoException;
    
    @Override public native void NSFDbGetMultNoteInfo(long hDb, short count, short options, long hInBuf, IntRef retSize, LongRef rethOutBuf) throws DominoException;
    @Override public native void NSFDbGetMultNoteInfoByUNID(long hDb, short count, short options, long hInBuf, IntRef retSize, LongRef rethOutBuf) throws DominoException;
    
    @Override public native void NSFDbCreate(String pathName, short dbClass, boolean forceCreation) throws DominoException;
    @Override public native void NSFDbDelete(String pathName) throws DominoException;
    @Override public native void NSFDbPathGet(long hDb, long retCanonicalPathName, long retExpandedPathName) throws DominoException;
    @Override public native long NSFGetChangedDBs(String serverName, TIMEDATE sinceTime, long changesSize, TIMEDATE nextSinceTime) throws DominoException;
    @Override public native void NSFDbAccessGet(long hDb, long retAccessLevel, long retAccessFlag);
    @Override public native void NSFDbReplicaInfoGet(long hDb, DBREPLICAINFO retReplicaInfo) throws DominoException;
    @Override public native String NSFDbUserNameGet(long hDb) throws DominoException;
    @Override public native int NSFDbGetOptions(long hDb) throws DominoException;
	@Override public native void NSFDbSetOptions(long hDb, int options, int mask) throws DominoException;
	@Override public native long NSFDbReadACL(long hDb) throws DominoException;
    @Override public native void NSFDbStoreACL(long hDb, long hACL, int objectId, short method) throws DominoException;
    
    @Override public native long NSFFolderGetIDTable(long hViewDB, long hDataDB, int viewNoteID, int flags) throws DominoException;
    @Override public native void NSFGetFolderChanges(long hViewDB, long hDataDB, int viewNoteID, TIMEDATE since, int flags, long addedNoteTablePtr, long removedNoteTablePtr) throws DominoException;
    @Override public native void NSFGetAllFolderChanges(long hViewDB, long hDataDB, TIMEDATE since, int flags, NSFGETALLFOLDERCHANGESCALLBACK callback, TIMEDATE until) throws DominoException;
    
    @Override public native long NSFDbInfoGet(long hDb) throws DominoException;
    @Override public native String NSFDbInfoParse(long info, short what);
    @Override public native short NSFDbClassGet(long hDb) throws DominoException;
    
    @Override public native void NSFDbModifiedTime(long hDb, TIMEDATE retDataModified, TIMEDATE retNonDataModified) throws DominoException;
    
    @Override public native void NSFTransactionBegin(long hDb, int flags) throws DominoException;
    @Override public native void NSFTransactionCommit(long hDb, int flags) throws DominoException;
    @Override public native void NSFTransactionRollback(long hDb) throws DominoException;
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//foldman.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override public native int FolderCreate(long hDataDB, long hFolderDB, int formatNoteID, long hFormatDB, String name, int folderType, int flags) throws DominoException;
    @Override public native void FolderDelete(long hDataDB, long hFolderDB, int folderNoteID, int flags);
    @Override public native int FolderCopy(long hDataDB, long hFolderDB, int folderNoteID, String name, int flags) throws DominoException;
    @Override public native void FolderRename(long hDataDB, long hFolderDB, int folderNoteID, String name, int flags) throws DominoException;
    @Override public native void FolderMove(long hDataDB, long hFolderDB, int folderNoteID, long hParentDB, int parentNoteID, int flags) throws DominoException;
    @Override public native void FolderDocAdd(long hDataDB, long hFolderDB, int folderNoteID, long hTable, int flags) throws DominoException;
    @Override public native void FolderDocRemove(long hDataDB, long hFolderDB, int folderNoteID, long hTable, int flags) throws DominoException;
    @Override public native void FolderDocRemoveAll(long hDataDB, long hFolderDB, int folderNoteID, int flags) throws DominoException;
    @Override public native int FolderDocCount(long hDataDB, long hFolderDB, int folderNoteID, int flags) throws DominoException;
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// acl.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override public native long ACLCreate() throws DominoException;
    @Override public native int ACLGetFlags(long hACL) throws DominoException;
    @Override public native void ACLSetFlags(long hACL, int flags) throws DominoException;
    @Override public native void ACLAddEntry(long hACL, String name, short accessLevel, long privileges, short accessFlags) throws DominoException;
    @Override public native void ACLClearPriv(long priv, short num);
    @Override public native void ACLDeleteEntry(long hACL, String name) throws DominoException;
    @Override public native void ACLEnumEntries(long hACL, ACLEnumFunc callback) throws DominoException;
    @Override public native String ACLGetAdminServer(long hACL) throws DominoException;
    @Override public native void ACLGetHistory(long hACL, LongRef hHistory, ShortRef historyCount) throws DominoException;
    @Override public native String ACLGetPrivName(long hACL, short privNum) throws DominoException;
    @Override public native void ACLInvertPriv(long priv, short num);
    @Override public native boolean ACLIsPrivSet(long priv, short num);
    @Override public native void ACLLookupAccess(long hACL, long pNamesList, ShortRef retAccessLevel, long retPrivileges, ShortRef retAccessFlags, LongRef rethPrivNames) throws DominoException;
    @Override public native void ACLSetAdminServer(long hACL, String serverName) throws DominoException;
    @Override public native void ACLSetPriv(long priv, short num);
    @Override public native void ACLSetPrivName(long hACL, short privNum, String privName) throws DominoException;
    @Override public native void ACLUpdateEntry(long hACL, String name, short updateFlags, String newName, short newAccessLevel, long newPrivileges, short newAccessFlags) throws DominoException;
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nsfdata.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nsfnote.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override public native long NSFNoteCreate(long hDb) throws DominoException;
    @Override public native long NSFNoteOpen(long hDb, int noteID, short flags) throws DominoException;
    @Override public native long NSFNoteOpenExt(long hDb, int noteID, int flags) throws DominoException;
    @Override public native void NSFNoteClose(long hNote) throws DominoException;
    @Override public native void NSFNoteDeleteExtended(long hDb, int noteID, int flags) throws DominoException;
    @Override public native void NSFNoteCheck(long hNote) throws DominoException;
    @Override public native void NSFNoteGetInfo(long hNote, short noteMember, long valuePtr);
    @Override public native void NSFNoteSetInfo(long hNote, short noteMember, long valuePtr);
    @Override public native void NSFNoteUpdate(long hNote, short updateFlags) throws DominoException;
    @Override public native void NSFNoteUpdateExtended(long hNote, int updateFlags) throws DominoException;

    @Override public native void NSFNoteAttachFile(long hNote, String itemName, String fileName, String origPathName, short encodingType) throws DominoException;
    @Override public native void NSFNoteDetachFile(long hNote, BLOCKID itemBlockId) throws DominoException;
    @Override public native void NSFNoteCipherDecrypt(long hNote, long hKFC, int decryptFlags, long rethCipherForAttachments) throws DominoException;
    @Override public native void NSFNoteCipherExtractFile(long hNote, BLOCKID itemBlockId, int extractFlags, int hDecryptionCipher, String fileName) throws DominoException;
    @Override public native void NSFNoteCipherExtractWithCallback(long hNote, BLOCKID bhItem, int extractFlags, long hDecryptionCipher, NOTEEXTRACTCALLBACK noteExtractCallback, int reserved, long pReserved) throws DominoException;
    
    @Override public native boolean NSFNoteHasMIME(long hNote);
    @Override public native boolean NSFNoteHasObjects(long hNote, BLOCKID firstItemBlockId);
    
    @Override public native void NSFItemInfo(long hNote, String itemName, BLOCKID itemBlockId, ShortRef valueDataType, BLOCKID valueBlockId, IntRef valueLen) throws DominoException;
    @Override public native void NSFItemInfoNext(long hNote, BLOCKID nextItem, String itemName, BLOCKID itemBlockId, ShortRef valueDataType, BLOCKID valueBlockId, IntRef valueLen) throws DominoException;
    @Override public native boolean NSFItemIsPresent(long hNote, String itemName);
    @Override public native void NSFItemQueryEx(long hNote, BLOCKID itemBlockId, long retItemName, short itemNameBufferLength, ShortRef retItemNameLength, ShortRef retItemFlags, ShortRef retDataType, BLOCKID retbhValue, IntRef retValueLength, ByteRef retSeqByte, ByteRef retDupItemID);
    @Override public native void NSFItemDelete(long hNote, String itemName) throws DominoException;
    @Override public native void NSFItemDeleteByBLOCKID(long hNote, BLOCKID itemBlockId) throws DominoException;
    
    @Override public native void NSFItemSetTime(long hNote, String itemName, TIMEDATE timeDate) throws DominoException;
    @Override public native void NSFItemSetText(long hNote, String itemName, String value) throws DominoException;
    @Override public native void NSFItemSetTextSummary(long hNote, String itemName, String value, boolean summary) throws DominoException;
    @Override public native void NSFItemSetNumber(long hNote, String itemName, double value) throws DominoException;
    @Override public native void NSFItemAppend(long hNote, short itemFlags, String itemName, short itemType, long valuePtr, int valueLen) throws DominoException;
    @Override public native void NSFItemAppendByBLOCKID(long hNote, short itemFlags, String itemName, BLOCKID valueBlockId, int valueLen, BLOCKID retItemBlockId) throws DominoException;
    
    @Override public native void NSFItemScan(long hNote, NSFITEMSCANPROC actionRoutine) throws DominoException;
    
    @Override public native String NSFItemConvertToText(long hNote, String itemName, short textBufLen, char separator) throws DominoException;
    @Override public native String NSFItemGetText(long hNote, String itemName, short textBufLen) throws DominoException;
    
    @Override public native void NSFProfileDelete(long hDB, String profileName, String userName) throws DominoException;
    @Override public native void NSFProfileEnum(long hDB, String profileName, NSFPROFILEENUMPROC callback, int flags) throws DominoException;
    @Override public native void NSFProfileGetField(long hDB, String profileName, String userName, String fieldName, long retDataType, long retbhValue, long retValueLength) throws DominoException;
    @Override public native long NSFProfileOpen(long hDB, String profileName, String userName, boolean copyProfile) throws DominoException;
    @Override public native void NSFProfileSetField(long hDB, String profileName, String userName, String fieldName, short dataType, long value, int valueLength) throws DominoException;
    @Override public native void NSFProfileUpdate(long hProfile, String profileName, String userName) throws DominoException;
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nsfobjec.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override public native long NSFDbReadObject(long hDb, int objectId, int offset, int length) throws DominoException;
	@Override public native void NSFDbGetObjectSize(long hDb, int objectId, short objectType, IntRef retSize, ShortRef retClass, ShortRef retPrivileges) throws DominoException;
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// idtable.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override public native boolean IDAreTablesEqual(long hSrc1Table, long hSrc2Table);
    @Override public native long IDCreateTable(int alignment) throws DominoException;
    @Override public native boolean IDDelete(long hTable, int id) throws DominoException;
    @Override public native void IDDeleteAll(long hTable) throws DominoException;
    @Override public native void IDDeleteTable(long hTable, long hIDsToDelete) throws DominoException;
    @Override public native void IDDestroyTable(/*dhandle*/long hTable) throws DominoException;
    @Override public native int IDEntries (/*dhandle*/long hTable) throws DominoException;
    @Override public native void IDEnumerate(/*dhandle*/long hTable, IDENUMERATEPROC callback) throws DominoException;
    @Override public native boolean IDInsert(long hTable, int id) throws DominoException;
    @Override public native void IDInsertTable(long hTable, long hIDsToAdd) throws DominoException;
    @Override public native boolean IDIsPresent(long hTable, int id) throws DominoException;
    @Override public native boolean IDScan(/*dhandle*/long hTable, boolean first, IntRef retID);
    @Override public native long IDTableCopy(long hTable) throws DominoException;
    @Override public native short IDTableFlags(long pIDTable);
    @Override public native void IDTableIntersect(long hSrc1Table, long hSrc2Table, long rethDstTable) throws DominoException;
    @Override public native void IDTableSetFlags(long pIDTable, short flags);
    @Override public native void IDTableSetTime(long pIDTable, TIMEDATE time);
    @Override public native int IDTableSize(long hTable);
    @Override public native int IDTableSizeP(long pIDTable);
    @Override public native long IDTableTime(long pIDTable);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// htmlapi.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override public native void HTMLProcessInitialize() throws DominoException;
	@Override public native void HTMLProcessTerminate();
	@Override public native long HTMLCreateConverter() throws DominoException;
	@Override public native void HTMLDestroyConverter(long hHTML) throws DominoException;
	@Override public native void HTMLConvertItem(long hHTML, long hDb, long hNote, String itemName) throws DominoException;
	@Override public native void HTMLConvertElement(long hHTML, long hDb, long hNote, String itemName, int itemIndex, int offset) throws DominoException;
	@Override public native void HTMLGetProperty(long hHTML, int propertyType, long pProperty) throws DominoException;
	@Override public native void HTMLSetHTMLOptions(long hHTML, String[] optionList) throws DominoException;
	@Override public native void HTMLGetText(long hHTML, int startingOffset, IntRef textLength, long pText) throws DominoException;
	@Override public native long HTMLGetReference(long hHTML, long index) throws DominoException;
	@Override public native long HTMLLockAndFixupReference(long hRef) throws DominoException;
	@Override public native void HTMLSetReferenceText(long hHTML, long index, String refText) throws DominoException;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// mime.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override public native void MIMEConvertCDParts(long hNote, boolean canonical, boolean isMIME, long hCC) throws DominoException;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// mimedir.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override public native void MimeGetTypeInfoFromExt(String ext, long typeBufPtr, short typeBufLen, long subtypeBufPtr, short subtypeBufLen, long descrBufPtr, short descrBufLen);
	@Override public native void MimeGetExtFromTypeInfo(String type, String subtype, long extBufPtr, short extBufLen, long descrBufPtr, short descrBufLen);
	@Override public native boolean MIMEEntityIsMultiPart(long pMimeEntity);
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nsfmime.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override public native long NSFMimePartGetPart(BLOCKID bhItem);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ods.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override public native void ODSReadMemory(long ppSrc, short type, long pDest, short iterations);
	@Override public native void ODSWriteMemory(long ppDest, short type, long pSrc, short iterations);
	@Override public native short ODSLength(short type);
	@Override public native void EnumCompositeBuffer(BLOCKID itemBlockId, int itemValueLength, ActionRoutinePtr actionRoutinePtr) throws DominoException;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nif.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override public native int NIFFindView(long hDb, String name) throws DominoException;
	@Override public native int NIFFindDesignNoteExt(long hDb, String name, short noteClass, String flagsPattern, int options) throws DominoException;
	@Override public native short NIFOpenCollection(long hViewDB, long hDataDB, int viewNoteID, short openFlags, long hUnreadList, long rethViewNote, UNIVERSALNOTEID viewUNID, long rethCollapsedList, long rethSelectedList) throws DominoException;
	@Override public native short NIFOpenCollectionWithUserNameList(long hViewDB, long hDataDB, int viewNoteID, short openFlags, long hUnreadList, long rethViewNote, UNIVERSALNOTEID viewUNID, long rethCollapsedList, long rethSelectedList, long nameList) throws DominoException;
	@Override public native void NIFCloseCollection(short hCollection) throws DominoException;
	@Override public native void NIFFindByKey(short hCollection, ITEM_TABLE key, short findFlags, COLLECTIONPOSITION retIndexPos, IntRef retNumMatches) throws DominoException;
	@Override public native void NIFFindByKeyExtended2(short hCollection, ITEM_TABLE key, int findFlags, int returnFlags, COLLECTIONPOSITION retIndexPos, IntRef retNumMatches, IntRef retSignalFlags, IntRef rethBuffer, IntRef retSequence) throws DominoException;
	@Override public native long NIFReadEntries(short hCollection, COLLECTIONPOSITION indexPos, short skipNavigator, int skipCount, short returnNavigator, int returnCount, int returnMask, ShortRef retBufferLength, IntRef retNumEntriesSkipped, IntRef retNumEntriesReturned, ShortRef retSignalFlags) throws DominoException;
	@Override public native long NIFGetCollectionData(short hCollection) throws DominoException;
	@Override public native void NIFUpdateCollection(short hCollection) throws DominoException;
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nsfsearc.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override public native long NSFFormulaCompile(String formulaName, String formulaText, IntRef retFormulaLength, IntRef retCompileError, IntRef retCompileErrorLine, IntRef retCompileErrorColumn, IntRef retCompileErrorOffset, IntRef retCompileErrorLength) throws DominoException;
	@Override public native String NSFFormulaDecompile(long pFormulaBuffer, boolean selectionFormula) throws DominoException;
	@Override public native void NSFFormulaSummaryItem(long hFormula, String itemName) throws DominoException;
	@Override public native void NSFFormulaMerge(long hSrcFormula, long hDestFormula) throws DominoException;
	@Override public native short NSFFormulaGetSize(long hFormula) throws DominoException;
	@Override public native long NSFComputeStart(short flags, long pCompiledFormula) throws DominoException;
	@Override public native void NSFComputeStop(long hCompute) throws DominoException;
	@Override public native long NSFComputeEvaluate(long hCompute, long hNote, IntRef retResultLength, IntRef retNoteMatchesFormula, IntRef retNoteShouldBeDeleted, IntRef retNoteModified) throws DominoException;
	@Override public native void NSFSearch(long hDb, long hFormula, String viewTitle, short searchFlags, short noteClassMask, TIMEDATE since, NSFSEARCHPROC enumRoutine, TIMEDATE retUntil) throws DominoException;
	@Override public native void NSFSearchWithUserNameList(long hDb, long hFormula, String viewTitle, short searchFlags, short noteClassMask, TIMEDATE since, NSFSEARCHPROC enumRoutine, TIMEDATE retUntil, long nameList) throws DominoException;
	@Override public native void NSFSearchExtended3(long hDb, long hFormula, long hFilter, int filterFlags, String viewTitle, int searchFlags, int searchFlags1, int searchFlags2, int searchFlags3, int searchFlags4, short noteClassMask, TIMEDATE since, NSFSEARCHPROC enumRoutine, TIMEDATE retUntil, long hNamesList) throws DominoException;
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nls.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override public native void NLS_translate(long pString, short len, long pStringTarget, IntRef pSize, short controlFlags, long pInfo) throws DominoException;
	@Override public native long NLS_load_charset(short CSID) throws DominoException;
	@Override public native long NLS_unload_charset(long pInfo) throws DominoException;
	

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// kfm.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override public native String SECKFMGetUserName() throws DominoException;
	@Override public native long SECKFMOpen(String idFileName, String password, int flags) throws DominoException;
	@Override public native void SECKFMClose(long hKFC, int flags) throws DominoException;
	@Override public native void SECExtractIdFileFromDB(long hDB, String profileNoteName, String userName, String password, String putIDFileHere) throws DominoException;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// log.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override public native void LogEventText(String message, int hModule, short additionalErrorCode) throws DominoException;
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//idvault.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override public native long SECidfGet(String userName, String password, String putIdFileHere, long pServerName) throws DominoException;
	@Override public native void SECidvResetUserPassword(String server, String userName, String password, short downloadCount) throws DominoException;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ns.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override public native long NSGetServerList(String portName) throws DominoException;
}
