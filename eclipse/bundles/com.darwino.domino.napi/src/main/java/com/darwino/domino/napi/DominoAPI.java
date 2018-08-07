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
package com.darwino.domino.napi;

import com.darwino.domino.napi.c.BaseIntRef;
import com.darwino.domino.napi.c.ByteRef;
import com.darwino.domino.napi.c.C;
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
 * abstract Domino API.
 * 
 * @author priand
 */
public abstract class DominoAPI {

	private static DominoAPI instance = new DominoAPIImpl();
	public static DominoAPI get() {
		return instance;
	}

	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// global.h 
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final long NULLHANDLE = 0;
    public static final int NULLMEMHANDLE = 0;
    
    public static final int MAXDWORD64 = (int)0xffffffffffffffffl;
    public static final int MAXDWORD = 0xffffffff;
    public static final short MAXWORD = (short)0xffff;
    public static final byte MAXBYTE = (byte)0xff;
	
	public abstract void NotesInit() throws DominoException;
    public abstract void NotesInitExtended(String... argv) throws DominoException;
    public abstract void NotesTerm() throws DominoException;
    public abstract void NotesInitThread() throws DominoException;
    public abstract void NotesTermThread() throws DominoException;
    

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// globerr.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static final short NOERROR = 0x0000;
	
	public static final short PKG_OS = 0x0100;
	public static final short PKG_NSF = 0x0200;
	public static final short PKG_MISC = 0x0400;
	public static final short PKG_FORMULA = 0x0500;
	public static final short PKG_HTTP = 0x3A00;
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// osfile.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public abstract String OSGetDataDirectory() throws DominoException;
	/**
	 * @since Lotus Notes/Domino 5.0
	 */
	public abstract String OSGetExecutableDirectory() throws DominoException;
	public abstract String OSPathNetConstruct(String portName, String serverName, String fileName) throws DominoException;
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// osenv.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public abstract int OSGetEnvironmentInt(String name) throws DominoException;
	public abstract long OSGetEnvironmentLong(String name) throws DominoException;
	public abstract String OSGetEnvironmentString(String name) throws DominoException;
	public abstract void OSSetEnvironmentInt(String name, int value) throws DominoException;
	public abstract void OSSetEnvironmentVariable(String name, String value) throws DominoException;

	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ostime.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public abstract void OSCurrentTimeZone(BaseIntRef retZone, BaseIntRef retDST) throws DominoException;
	public abstract void OSCurrentTIMEDATE(TIMEDATE retTimeDate) throws DominoException;


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// osmem.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final int MAXONESEGSIZE_W16 = (MAXWORD-1-128);
	public static final int MAXONESEGSIZE_OS21x = (MAXWORD-1-6);
	@SuppressWarnings("unused")
	public static final int MAXONESEGSIZE = (((MAXONESEGSIZE_W16) > (MAXONESEGSIZE_OS21x)) ? (MAXONESEGSIZE_OS21x) : (MAXONESEGSIZE_W16));
	
	public abstract long OSLockObject(long Handle);
	public abstract void OSUnlockObject(long Handle);
	/** @since Notes/Domino 5.0 */
	public abstract void OSMemoryFree(long handle);
	/** @since Notes/Domino 5.0 */
	public abstract long OSMemoryLock(long handle);
	/** @since Notes/Domino 5.0 */
	public abstract boolean OSMemoryUnlock(long handle);
	public abstract long OSMemAlloc(short blkType, int size) throws DominoException;
	public abstract void OSMemFree(long Handle) throws DominoException;
	/**
	 * This Java version of the C macro omits the first parameter because typed pointers don't make
	 * sense in this API.
	 */
	public long OSLock(long hObject) {
		return OSLockObject(hObject);
	}
	/**
	 * This method, like its C macro counterpart, is a direct call to OSUnlockObject and always returns true,
	 * but is present for consistency.
	 */
	public boolean OSUnlock(long handle) {
		OSUnlockObject(handle);
		return true;
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// pool.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This Java version of the C macro omits the first parameter because typed pointers don't make
	 * sense in this API.
	 */
	public long OSLockBlock(BLOCKID blockId) {
		return C.ptrAdd(OSLock(blockId.getPool()), blockId.getBlock());
	}
	public boolean OSUnlockBlock(BLOCKID blockId) {
		OSUnlockObject(blockId.getPool());
		return true;
	}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// osmisc.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static final short OS_TRANSLATE_NATIVE_TO_LMBCS = 0;
	public static final short OS_TRANSLATE_OSNATIVE_TO_LMBCS = DominoNativeUtils.isWinx64() || DominoNativeUtils.isWinx86() ? 7 : OS_TRANSLATE_NATIVE_TO_LMBCS;
	public static final short OS_TRANSLATE_LMBCS_TO_UNICODE = 20;
	public static final short OS_TRANSLATE_LMBCS_TO_UTF8 =    22;
	public static final short OS_TRANSLATE_UNICODE_TO_LMBCS = 23;
	public static final short OS_TRANSLATE_UTF8_TO_LMBCS =    24;
	
	public static final short MAXALPHATIMEDATE = 80;
	
	public abstract short OSTranslate(short translateMode, long inPtr, short inLength, long outPtr, short outLength);
	/** @since Notes/Domino 4.0 */
	public abstract long OSGetLMBCSCLS();
	/** @since Notes/Domino 4.0 */
	public abstract long OSGetNativeCLS();
	/**
	 * @param intlFormat INTLFORMAT*
	 * @param textFormat
	 * @param ppText char**
	 * @param maxLength WORD
	 * @param retTimeDate
	 */
	public abstract void ConvertTextToTIMEDATE(long intlFormat, TFMT textFormat, long ppText, short maxLength, TIMEDATE retTimeDate) throws DominoException;
	public abstract String ConvertTIMEDATEToText(long intlFormat, TFMT textFormat, TIMEDATE inputTime) throws DominoException;
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// misc.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/* Currency flags */

	public static final short NCURFMT_SYMFOLLOWS = (short)0x0001;		/* The currency symbol follows the value */
	public static final short NCURFMT_USESPACES = (short)0x0002;		/* Inset space between symbol and value */
	public static final short NCURFMT_ISOSYMUSED = (short)0x0004;	/* Using 3 letter ISO for currency symbol */

	/* Currency selection values */

	public static final short NCURFMT_COMMON = (short)0;
	public static final short NCURFMT_CUSTOM = (short)1;

	/*	Number Format */

	public static final byte NFMT_GENERAL = (byte)0;		/* Number Formats */
	public static final byte NFMT_FIXED = (byte)1;
	public static final byte NFMT_SCIENTIFIC = (byte)2;
	public static final byte NFMT_CURRENCY = (byte)3;
	public static final byte NFMT_BYTES = (byte)4; 		/* LI 3926.07  Number Format */

	public static final short NATTR_PUNCTUATED = (short)0x0001;	/* Number Attributes */
	public static final short NATTR_PARENS = (short)0x0002;
	public static final short NATTR_PERCENT = (short)0x0004;
	public static final short NATTR_VARYING = (short)0x0008;
	public static final short NATTR_BYTES = (short)0x0010; /* LI 3926.07, new number format type in Hannover, added for backward compatibility */

	/*	Time Format */

	public static final byte TDFMT_FULL = (byte)0;		/* year, month, and day */
	public static final byte TDFMT_CPARTIAL = (byte)1;		/* month and day, year if not this year */
	public static final byte TDFMT_PARTIAL = (byte)2;		/* month and day */
	public static final byte TDFMT_DPARTIAL = (byte)3;		/* year and month */
	public static final byte TDFMT_FULL4 = (byte)4;		/* year(4digit), month, and day */
	public static final byte TDFMT_CPARTIAL4 = (byte)5;		/* month and day, year(4digit) if not this year */
	public static final byte TDFMT_DPARTIAL4 = (byte)6;		/* year(4digit) and month */
	public static final byte TTFMT_FULL = (byte)0;		/* hour, minute, and second */
	public static final byte TTFMT_PARTIAL = (byte)1;		/* hour and minute */
	public static final byte TTFMT_HOUR = (byte)2;		/* hour */
	public static final byte TZFMT_NEVER = (byte)0;		/* all times converted to THIS zone */
	public static final byte TZFMT_SOMETIMES = (byte)1;		/* show only when outside this zone */
	public static final byte TZFMT_ALWAYS = (byte)2;		/* show on all times, regardless */

	public static final byte TSFMT_DATE = (byte)0;		/* DATE */
	public static final byte TSFMT_TIME = (byte)1;		/* TIME */
	public static final byte TSFMT_DATETIME = (byte)2;		/* DATE TIME */
	public static final byte TSFMT_CDATETIME = (byte)3;		/* DATE TIME or TIME Today or TIME Yesterday */
	
	
	public static final short TIMEDATE_MINIMUM		= 0;
	public static final short TIMEDATE_MAXIMUM		= 1;
	public static final short TIMEDATE_WILDCARD		= 2;
	
	public abstract void TimeConstant(short type, TIMEDATE td) throws DominoException;
	public abstract void TimeGMToLocal(TIME time) throws DominoException;
	public abstract void TimeGMToLocalZone(TIME time) throws DominoException;
	public abstract void TimeLocalToGM(TIME time) throws DominoException;
	public abstract int  TimeDateDifference(TIMEDATE td1, TIMEDATE td2);
	public abstract int  TimeDateCompare(TIMEDATE td1, TIMEDATE td2);
	public abstract void TimeDateIncrement(TIMEDATE td, int interval) throws DominoException;
	/** @since Notes/Domino 4.5 */
	public abstract int  TimeExtractDate(TIMEDATE td);
	public abstract void TimeDateClear(TIMEDATE td);
	
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nsfdb.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final short SPECIAL_ID_NOTE = (short)0x8000;
	
	public static final short DBCLASS_BY_EXTENSION	= 0; /* automatically figure it out */

	public static final short DBCLASS_NSFTESTFILE       = (short)0xff00;
	public static final short DBCLASS_NOTEFILE          = (short)0xff01;
	public static final short DBCLASS_DESKTOP           = (short)0xff02;
	public static final short DBCLASS_NOTECLIPBOARD     = (short)0xff03;
	public static final short DBCLASS_TEMPLATEFILE      = (short)0xff04;
	public static final short DBCLASS_GIANTNOTEFILE     = (short)0xff05;
	public static final short DBCLASS_HUGENOTEFILE      = (short)0xff06;
	public static final short DBCLASS_ONEDOCFILE        = (short)0xff07; /* Not a mail message */
	public static final short DBCLASS_V2NOTEFILE        = (short)0xff08;
	public static final short DBCLASS_ENCAPSMAILFILE    = (short)0xff09; /* Specifically used by alt mail */
	public static final short DBCLASS_LRGENCAPSMAILFILE	= (short)0xff0a; /* Specifically used by alt mail */
	public static final short DBCLASS_V3NOTEFILE        = (short)0xff0b;
	public static final short DBCLASS_OBJSTORE          = (short)0xff0c; /* Object store */
	public static final short DBCLASS_V3ONEDOCFILE      = (short)0xff0d;
	public static final short DBCLASS_V4NOTEFILE        = (short)0xff0e;
	public static final short DBCLASS_V5NOTEFILE        = (short)0xff0f;
	public static final short DBCLASS_V6NOTEFILE        = (short)0xff10;
	public static final short DBCLASS_V8NOTEFILE        = (short)0xff11;
	public static final short DBCLASS_V85NOTEFILE       = (short)0xff12;
	public static final short DBCLASS_V9NOTEFILE        = (short)0xff13;

	public static final short DBCLASS_MASK              = (short)0x00ff;
	public static final short DBCLASS_VALID_MASK        = (short)0xff00;
	
	public static final int DB_GETIDTABLE_VALIDATE      = 0x00000001; /* If set, return only "validated" noteIDs */
	
	public static final short INFOPARSE_TITLE = 0;
	public static final short INFOPARSE_CATEGORIES = 1;
	public static final short INFOPARSE_CLASS = 2;
	public static final short INFOPARSE_DESIGN_CLASS = 3;
	
	public abstract long NSFDbOpen(String dbPath) throws DominoException;
    public abstract long NSFDbOpenExtended(String dbPath, short options, long hNames, TIMEDATE modifiedTime, TIMEDATE retDataModified, TIMEDATE retNonDataModified) throws DominoException;
    public abstract void NSFDbClose(long hDb) throws DominoException;
    public abstract long NSFDbGetModifiedNoteTable(long hDb, short noteClassMask, TIMEDATE since, TIMEDATE until) throws DominoException;
    /** @since Notes/Domino 5.0 */
    public abstract long NSFBuildNamesList(String userName, int flags) throws DominoException;
    
    /**
     * @return true if the specified note has not been deleted, false otherwise.
     */
    public abstract boolean NSFDbGetNoteInfo(long hDb, int noteID, OID oid, TIMEDATE retModified, IntRef retNoteClass) throws DominoException;
    /**
     * @return true if the specified note has not been deleted, false otherwise.
     * @since Notes/Domino 8.5.2
     */
    public abstract boolean NSFDbGetNoteInfoExt(long hDb, int noteID, OID oid, TIMEDATE retModified, IntRef retNoteClass, TIMEDATE retAddedToFile, IntRef retResponseCount, IntRef retParentNoteID ) throws DominoException;
    /**
     * @return true if the specified note has not been deleted, false otherwise.
     */
    public abstract boolean NSFDbGetNoteInfoByUNID(long hDb, UNIVERSALNOTEID unid, IntRef retNoteID, OID oid, TIMEDATE retModified, IntRef retNoteClass) throws DominoException;
    
    /** @since Notes/Domino 8.5.3 */
    public abstract void NSFDbGetMultNoteInfo(long hDb, short count, short options, long hInBuf, IntRef retSize, LongRef rethOutBuf) throws DominoException;
    /** @since Notes/Domino 8.5.3 */
    public abstract void NSFDbGetMultNoteInfoByUNID(long hDb, short count, short options, long hInBuf, IntRef retSize, LongRef rethOutBuf) throws DominoException;
    
    /**
     * @param pathName the database name or, alternately, a full network path specification to the database built with OSPathNetConstruct.
     */
    public abstract void NSFDbCreate(String pathName, short dbClass, boolean forceCreation) throws DominoException;
    /**
     * @param pathName the database name or, alternately, a full network path specification to the database built with OSPathNetConstruct.
     */
    public abstract void NSFDbDelete(String pathName) throws DominoException;
    
    /**
     * @param retCanonicalPathName a pointer to an allocated buffer of size {@link #MAXPATH}, or 0. This will be set
     * 		to an LMBCS string
     * @param retExpandedPathName a pointer to an allocated buffer of size {@link #MAXPATH}, or 0. This will be set
     * 		to an LMBCS string
     */
    public abstract void NSFDbPathGet(long hDb, long retCanonicalPathName, long retExpandedPathName) throws DominoException;
    
    /**
     * @param serverName the server name to query
     * @param sinceTime the start time of the search
     * @param changesSize a pointer to a DWORD to hold the size of the path list
     * @param nextSinceTime the new since time
     * @return a handle to the DB path list
     */
    public abstract long NSFGetChangedDBs(String serverName, TIMEDATE sinceTime, long changesSize, TIMEDATE nextSinceTime) throws DominoException;
    /**
     * @param hDb a handle to an open database
     * @param retAccessLevel a pointer to a WORD to hold the access level
     * @param retAccessFlag a pointer to a WORD to hold the access flags
     * @since Notes/Domino 4.0
     */
    public abstract void NSFDbAccessGet(long hDb, long retAccessLevel, long retAccessFlag);
    
    public abstract void NSFDbReplicaInfoGet(long hDb, DBREPLICAINFO retReplicaInfo) throws DominoException;
    /** @since Notes/Domino 4.5 */
    public abstract String NSFDbUserNameGet(long hDb) throws DominoException;
    
    public abstract long NSFDbReadACL(long hDb) throws DominoException;
    public abstract void NSFDbStoreACL(long hDb, long hACL, int objectId, short method) throws DominoException;
    
    // Folders
    /**
     * @return a DHANDLE for the ID table
     * @since Notes/Domino 5.0.3
     */
    public abstract long NSFFolderGetIDTable(long hViewDB, long hDataDB, int viewNoteID, int flags) throws DominoException;
    /** @since Notes/Domino 5.0.4 */
    public abstract void NSFGetFolderChanges(long hViewDB, long hDataDB, int viewNoteID, TIMEDATE since, int flags, long addedNoteTablePtr, long removedNoteTablePtr) throws DominoException;
    /** @since Notes/Domino 6.0.3 */
    public abstract void NSFGetAllFolderChanges(long hViewDB, long hDataDB, TIMEDATE since, int flags, NSFGETALLFOLDERCHANGESCALLBACK callback, TIMEDATE until) throws DominoException;
    
    /**
     * @return a pointer to the info buffer. The call is responsible for freeing this memory
     */
    public abstract long NSFDbInfoGet(long hDb) throws DominoException;
    public abstract String NSFDbInfoParse(long info, short what);
    public abstract short NSFDbClassGet(long hDb) throws DominoException;
    
    public abstract void NSFDbModifiedTime(long hDb, TIMEDATE retDataModified, TIMEDATE retNonDataModified) throws DominoException;
    
    /** Transactions is Sub-Commited if a Sub Transaction */
    public static final int NSF_TRANSACTION_BEGIN_SUB_COMMIT = 0x00000001; 
    /** When starting a txn (not a sub tran) get an intent shared lock on the db */
    public static final int NSF_TRANSACTION_BEGIN_LOCK_DB = 0x00000002;

    /** Don't automatically abort if Commit Processing Fails */
    public static final int TRANCOMMIT_SKIP_AUTO_ABORT = 1; 
    
    public abstract void NSFTransactionBegin(long hDb, int flags) throws DominoException;
    public abstract void NSFTransactionCommit(long hDb, int flags) throws DominoException;
    public abstract void NSFTransactionRollback(long hDb) throws DominoException;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// foldman.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Note the swapped positions of the DB handles relative to the NSF* functions
     * @return the note ID of the newly-created folder
     * @since Notes/Domino 4.0
     */
    public abstract int FolderCreate(long hDataDB, long hFolderDB, int formatNoteID, long hFormatDB, String name, int folderType, int flags) throws DominoException;
    /**
     * Note the swapped positions of the DB handles relative to the NSF* functions
     * @since Notes/Domino 4.0
     */
    public abstract void FolderDelete(long hDataDB, long hFolderDB, int folderNoteID, int flags);
    /**
     * Note the swapped positions of the DB handles relative to the NSF* functions
     * @return the note ID of the newly-created folder
     * @since Notes/Domino 4.0
     */
    public abstract int FolderCopy(long hDataDB, long hFolderDB, int folderNoteID, String name, int flags) throws DominoException;
    /**
     * Note the swapped positions of the DB handles relative to the NSF* functions
     * @since Notes/Domino 4.0
     */
    public abstract void FolderRename(long hDataDB, long hFolderDB, int folderNoteID, String name, int flags) throws DominoException;
    /**
     * Note the swapped positions of the DB handles relative to the NSF* functions
     * @since Notes/Domino 4.0
     */
    public abstract void FolderMove(long hDataDB, long hFolderDB, int folderNoteID, long hParentDB, int parentNoteID, int flags) throws DominoException;
    /**
     * Note the swapped positions of the DB handles relative to the NSF* functions
     * @since Notes/Domino 4.0
     */
    public abstract void FolderDocAdd(long hDataDB, long hFolderDB, int folderNoteID, long hTable, int flags) throws DominoException;
    /**
     * Note the swapped positions of the DB handles relative to the NSF* functions
     * @since Notes/Domino 4.0
     */
    public abstract void FolderDocRemove(long hDataDB, long hFolderDB, int folderNoteID, long hTable, int flags) throws DominoException;
    /**
     * Note the swapped positions of the DB handles relative to the NSF* functions
     * @since Notes/Domino 4.0
     */
    public abstract void FolderDocRemoveAll(long hDataDB, long hFolderDB, int folderNoteID, int flags) throws DominoException;
    /**
     * Note the swapped positions of the DB handles relative to the NSF* functions
     * @return the number of entires in the specified folder's index
     * @since Notes/Domino 4.0
     */
    public abstract int FolderDocCount(long hDataDB, long hFolderDB, int folderNoteID, int flags) throws DominoException;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// design.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final int DESIGN_TYPE_SHARED = 0; /* Note is shared (always located in the database) */
    public static final int DESIGN_TYPE_PRIVATE_DATABASE = 1; /* Note is private and is located in the database */

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// acl.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /*	Defines for Authentication flags */

    public static final short NAMES_LIST_AUTHENTICATED = 0x0001;/* 	Set if names list has been 	*/
    													/*	authenticated via Notes		*/
    public static final short NAMES_LIST_PASSWORD_AUTHENTICATED = 0x0002;	/* 	Set if names list has been 	*/
    													/*	authenticated using external */
    													/*	password -- Triggers "maximum */
    													/*	password access allowed" feature */
    public static final short NAMES_LIST_FULL_ADMIN_ACCESS = 0x0004;	/* 	Set if user requested full admin access and it was granted */
    
    public static final String ACL_DEFAULT_NAME = "-Default-"; //$NON-NLS-1$

    public static final int ACL_UNIFORM_ACCESS = 0x00000001;	/* Require same ACL in ALL replicas of database */
    
    public static final short ACL_FLAG_AUTHOR_NOCREATE = 0x0001;
    public static final short ACL_FLAG_SERVER = 0x0002;	/* Entry represents a Server (V4) */
    public static final short ACL_FLAG_NODELETE =			0x0004;	/* User cannot delete notes */

    public static final short ACL_FLAG_CREATE_PRAGENT = 	0x0008;	/* User can create personal agents (V4) */
    public static final short ACL_FLAG_CREATE_PRFOLDER =	0x0010;	/* User can create personal folders (V4) */
    public static final short ACL_FLAG_PERSON =				0x0020; 	/* Entry represents a Person (V4) */
    public static final short ACL_FLAG_GROUP =				0x0040; 	/* Entry represents a group (V4) */
    public static final short ACL_FLAG_CREATE_FOLDER =		0x0080;	/* User can create and update shared views & folders (V4)
     										This allows an Editor to assume some Designer-level access */
    public static final short ACL_FLAG_CREATE_LOTUSSCRIPT =	0x0100;	/* User can create LotusScript */
    public static final short ACL_FLAG_PUBLICREADER =		0x0200;  /* User can read public notes */
    public static final short ACL_FLAG_PUBLICWRITER =		0x0400;  /* User can write public notes */
    public static final short ACL_FLAG_MONITORS_DISALLOWED =	0x800;	/* User CANNOT register monitors for this database */
    public static final short ACL_FLAG_NOREPLICATE =		0x1000;	/* User cannot replicate or copy this database */
    public static final short ACL_FLAG_ADMIN_READERAUTHOR = 0X4000;	/* Admin server can modify reader and author fields in db */
    public static final short ACL_FLAG_ADMIN_SERVER =		(short)0x8000;	/* Entry is administration server (V4) */
    
    public static final short ACL_LEVEL_NOACCESS = 0;
    public static final short ACL_LEVEL_DEPOSITOR = 1;
    public static final short ACL_LEVEL_READER = 2;
    public static final short ACL_LEVEL_AUTHOR = 3;
    public static final short ACL_LEVEL_EDITOR = 4;
    public static final short ACL_LEVEL_DESIGNER = 5;
    public static final short ACL_LEVEL_MANAGER = 6;
    
    public static final int ACL_PRIVCOUNT = 80;
    
    public abstract long ACLCreate() throws DominoException;
    /**
     * @param hACL DHANDLE
     * @param name
     * @param accessLevel WORD
     * @param privileges ACL_PRIVILEGES* (pointer to BYTE[10])
     * @param accessFlags WORD
     */
    public abstract void ACLAddEntry(long hACL, String name, short accessLevel, long privileges, short accessFlags) throws DominoException;
    /**
     * @param priv ACL_PRIVILEGES* (pointer to BYTE[10])
     * @param num WORD
     */
    public abstract void ACLClearPriv(long priv, short num);
    /**
     * @param hACL DHANDLE
     * @param name
     */
    public abstract void ACLDeleteEntry(long hACL, String name) throws DominoException;
    /**
     * @param hACL DHANDLE
     * @param callback
     */
    public abstract void ACLEnumEntries(long hACL, ACLEnumFunc callback) throws DominoException;
    /**
     * @param hACL DHANDLE
     */
    public abstract String ACLGetAdminServer(long hACL) throws DominoException;
    /** @since Notes/Domino 4.0 */
    public abstract int ACLGetFlags(long hACL) throws DominoException;
    /**
     * @param hAcl DHANDLE
     * @param hHistory DHANDLE ref
     * @param historyCount WORD ref
     */
    public abstract void ACLGetHistory(long hACL, LongRef hHistory, ShortRef historyCount) throws DominoException;
    /**
     * 
     * @param hACL DHANDLE
     * @param privNum WORD
     */
    public abstract String ACLGetPrivName(long hACL, short privNum) throws DominoException;
    /**
     * @param priv ACL_PRIVILEGES* (pointer to BYTE[10])
     * @param num WORD
     */
    public abstract void ACLInvertPriv(long priv, short num);
    /**
     * @param priv ACL_PRIVILEGES* (pointer to BYTE[10])
     * @param num WORD
     */
    public abstract boolean ACLIsPrivSet(long priv, short num);
    /**
     * @param hACL DHANDLE
     * @param pNamesList NAMES_LIST*
     * @param retAccessLevel WORD ref
     * @param retPrivileges ACL_PRIVILEGES* (pointer to BYTE[10])
     * @param retAccessFlags SHORT ref
     * @param rethPrivNames DHANDLE ref
     */
    public abstract void ACLLookupAccess(long hACL, long pNamesList, ShortRef retAccessLevel, long retPrivileges, ShortRef retAccessFlags, LongRef rethPrivNames) throws DominoException;
    /**
     * @param hACL DHANDLE
     * @param serverName
     */
    public abstract void ACLSetAdminServer(long hACL, String serverName) throws DominoException;
    /** @since Notes/Domino 4.0 */
    public abstract void ACLSetFlags(long hACL, int flags) throws DominoException;
    /**
     * @param priv ACL_PRIVILEGES* (pointer to BYTE[10])
     * @param num WORD
     */
    public abstract void ACLSetPriv(long priv, short num);
    /**
     * @param hACL DHANDLE
     * @param privNum WORD
     * @param privName
     */
    public abstract void ACLSetPrivName(long hACL, short privNum, String privName) throws DominoException;
    /**
     * @param hACL DHANDLE
     * @param name
     * @param updateFlags WORD
     * @param newNAme
     * @param newAccessLevel WORD
     * @param newPrivileges ACL_PRIVILEGES* (pointer to BYTE[10])
     * @param newAccessFlags WORD
     */
    public abstract void ACLUpdateEntry(long hACL, String name, short updateFlags, String newName, short newAccessLevel, long newPrivileges, short newAccessFlags) throws DominoException;
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nsfdata.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /*	Reserved NoteIDs */
    public static final int NOTEID_RESERVED		= 0x80000000;		/*	Reserved Note ID, used for
    																categories in NIFReadEntries
    																and for deleted notes in a
    																lot of interfaces. */
    public static final int NOTEID_ADD			= 0x00000000;		/*	Reserved NoteID used as input
    																to NoteUpdate, to add a new
    																note (gets error if UNID assigned
    																to new note already exists). */
    public static final int NOTEID_ADD_OR_REPLACE = 0x80000000;		/*	Reserved NoteID used as input
    																to NoteUpdate, to update if
    																note UNID already exists, or
    																add note if doesn't exist. */
    public static final int NOTEID_ADD_UNID		= 0x80000001;		/*	Reserved NoteID used as input
    																to NoteUpdate.  Try to preserve
    																the specified note UNID, but if
    																it already exists, assign a new
    																one.  (Note that the UNID in the
    																hNote IS updated.) */
    public static final int NOTEID_NULL_FOLDER	= 0x00000000;		/*	Used for null folder ids. */
    
    /*	An RRV "file position" is defined to be a DWORD, 4 bytes long. */
    public static final int RRV_ALIGNMENT 			= 4;				/* most typical RRV alignment (DBTABLE.C) */
    public static final int RRV_DELETED 			= NOTEID_RESERVED;	/* indicates a deleted note (DBTABLE.C) */

    public static final int NOTEID_NO_PARENT		= 0x00000000;		/*	Reserved Note ID, used to indicate
    																	that this note has no parent in the
    																	response hierarchy. */
    
    /*	Data Type Definitions. */


    /*	Class definitions.  Classes are defined to be the
    	"generic" classes of data type that the internal formula computation
    	mechanism recognizes when doing recalcs. */
    
    public static final short CLASS_NOCOMPUTE =   (0 << 8);
    public static final short CLASS_ERROR =       (1 << 8);
    public static final short CLASS_UNAVAILABLE = (2 << 8);
    public static final short CLASS_NUMBER =      (3 << 8);
    public static final short CLASS_TIME =        (4 << 8);
    public static final short CLASS_TEXT =        (5 << 8);
    public static final short CLASS_FORMULA =     (6 << 8);
    public static final short CLASS_USERID =      (7 << 8);

    public static final short  CLASS_MASK =       (short)0xff00;
    
    /*	"Computable" Data Types */

    public static final short TYPE_ERROR =        0 + CLASS_ERROR;       /* Host form */
    public static final short TYPE_UNAVAILABLE =  0 + CLASS_UNAVAILABLE; /* Host form */
    public static final short TYPE_TEXT =         0 + CLASS_TEXT;        /* Host form */
    public static final short TYPE_TEXT_LIST =    1 + CLASS_TEXT;        /* Host form */
    public static final short TYPE_NUMBER =       0 + CLASS_NUMBER;      /* Host form */
    public static final short TYPE_NUMBER_RANGE = 1 + CLASS_NUMBER;      /* Host form */
    public static final short TYPE_TIME =         0 + CLASS_TIME;        /* Host form */
    public static final short TYPE_TIME_RANGE =   1 + CLASS_TIME;        /* Host form */
    public static final short TYPE_FORMULA =      0 + CLASS_FORMULA;     /* Canonical form */
    public static final short TYPE_USERID =       0 + CLASS_USERID;      /* Host form */

    /*	"Non-Computable" Data Types */

    public static final short TYPE_INVALID_OR_UNKNOWN = 0 + CLASS_NOCOMPUTE;  /* Host form */
    public static final short TYPE_COMPOSITE =          1 + CLASS_NOCOMPUTE;  /* Canonical form, >64K handled by more than one item of same name concatenated */
    public static final short TYPE_COLLATION =          2 + CLASS_NOCOMPUTE;  /* Canonical form */
    public static final short TYPE_OBJECT =             3 + CLASS_NOCOMPUTE;  /* Canonical form */
    public static final short TYPE_NOTEREF_LIST =       4 + CLASS_NOCOMPUTE;  /* Host form */
    public static final short TYPE_VIEW_FORMAT =        5 + CLASS_NOCOMPUTE;  /* Canonical form */
    public static final short TYPE_ICON =               6 + CLASS_NOCOMPUTE;  /* Canonical form */
    public static final short TYPE_NOTELINK_LIST =      7 + CLASS_NOCOMPUTE;  /* Host form */
    public static final short TYPE_SIGNATURE =          8 + CLASS_NOCOMPUTE;  /* Canonical form */
    public static final short TYPE_SEAL =               9 + CLASS_NOCOMPUTE;  /* Canonical form */
    public static final short TYPE_SEALDATA =           10 + CLASS_NOCOMPUTE; /* Canonical form */
    public static final short TYPE_SEAL_LIST =          11 + CLASS_NOCOMPUTE; /* Canonical form */
    public static final short TYPE_HIGHLIGHTS =         12 + CLASS_NOCOMPUTE; /* Host form */
    public static final short TYPE_WORKSHEET_DATA =     13 + CLASS_NOCOMPUTE; /* Used ONLY by Chronicle product */
                                                                              /* Canonical form */
    public static final short TYPE_USERDATA =           14 + CLASS_NOCOMPUTE; /* Arbitrary format data - see format below */
                                                                              /* Canonical form */
    public static final short TYPE_QUERY =              15 + CLASS_NOCOMPUTE; /* Saved query CD records; Canonical form */
    public static final short TYPE_ACTION =             16 + CLASS_NOCOMPUTE; /* Saved action CD records; Canonical form */
    public static final short TYPE_ASSISTANT_INFO =     17 + CLASS_NOCOMPUTE; /* Saved assistant info */
    public static final short TYPE_VIEWMAP_DATASET =    18 + CLASS_NOCOMPUTE; /* Saved ViewMap dataset; Canonical form */
    public static final short TYPE_VIEWMAP_LAYOUT =     19 + CLASS_NOCOMPUTE; /* Saved ViewMap layout; Canonical form */
    public static final short TYPE_LSOBJECT =           20 + CLASS_NOCOMPUTE; /* Saved LS Object code for an agent.	*/
    public static final short TYPE_HTML =               21 + CLASS_NOCOMPUTE; /* LMBCS-encoded HTML, >64K handled by more than one item of same name concatenated */
    public static final short TYPE_SCHED_LIST =         22 + CLASS_NOCOMPUTE; /* Busy time schedule entries list; Host form */
    public static final short TYPE_CALENDAR_FORMAT =    24 + CLASS_NOCOMPUTE; /* Canonical form */
    public static final short TYPE_MIME_PART =          25 + CLASS_NOCOMPUTE; /* MIME body part; Canonical form */
    public static final short TYPE_RFC822_TEXT =        2 + CLASS_TEXT;       /* RFC822( RFC2047) message header; Canonical form */
    public static final short TYPE_SEAL2 =              31 + CLASS_NOCOMPUTE; /* Canonical form */
    
    public static final short OBJECT_NO_COPY = (short)0x8000; /* do not copy object when updating to new note or database */
    public static final short OBJECT_PRESERVE = 0x4000; /* keep object around even if hNote doesn't have it when NoteUpdating */
    public static final short OBJECT_PUBLIC = 0x2000; /* Public access object being allocated. */

    /*	Object Types, a sub-category of TYPE_OBJECT */

    public static final short OBJECT_FILE = 0; /* File Attachment */
    public static final short OBJECT_FILTER_LEFTTODO = 3; /* IDTable of "done" docs attached to filter */
    public static final short OBJECT_ASSIST_RUNDATA = 8; /* Assistant run data object */
    public static final short OBJECT_UNKNOWN = (short)0xffff; /* Used as input to NSFDbGetObjectSize */
    
	public static final short HOST_MASK           = 0x0f00;   /* used for NSFNoteAttachFile Encoding arg */
	public static final short HOST_MSDOS          = (0 << 8); /* CRNL at EOL, optional ^Z at EOF */
	public static final short HOST_OLE            = (1 << 8); /* unknown internal representation, up to app */
	public static final short HOST_MAC            = (2 << 8); /* potentially has resource forks, etc. */
	public static final short HOST_UNKNOWN        = (3 << 8); /* came inbound thru a gateway */
	public static final short HOST_HPFS           = (4 << 8); /* HPFS. Contains EAs and long filenames */
	public static final short HOST_OLELIB         = (5 << 8); /* OLE 1 Library encapsulation */
	public static final short HOST_BYTEARRAY_EXT  = (6 << 8); /* OLE 2 ILockBytes byte array extent table */
	public static final short HOST_BYTEARRAY_PAGE = (7 << 8); /* OLE 2 ILockBytes byte array page */
	public static final short HOST_CDSTORAGE      = (8 << 8); /* externally stored CD records */
	public static final short HOST_STREAM         = (9 << 8); /* Binary private stream */
	public static final short HOST_LINK           = (10 << 8);/* contains a RESOURCELINK to a named element */
	
	public static final short HOST_LOCAL          = 0x0f00;   /* ONLY used as argument to NSFNoteAttachFile */
	                                                        /* means "use MY os's HOST_ type */
	
	public static final short EFLAGS_MASK = (short)0xf000;    /* used for NSFNoteAttachFile encoding arg */
	public static final short EFLAGS_INDOC = 0x1000;          /* used to pass FILEFLAG_INDOC flag to NSFNoteAttachFile */
	public static final short EFLAGS_KEEPPATH = 0x2000;       /* don't strip off path in the filename */
	public static final short EFLAGS_AUTOCOMPRESSED = 0x4000; /*used to pass FILEFLAG_AUTOCOMPRESSED flag to NSFNoteAttachFile */
	
	public static final short COMPRESS_MASK = 0x000f; /* used for NSFNoteAttachFile Encoding arg */
	public static final short COMPRESS_NONE = 0;      /* no compression */
	public static final short COMPRESS_HUFF = 1;      /* huffman encoding for compression */
	public static final short COMPRESS_LZ1 = 2;       /* LZ1 compression */
	public static final short RECOMPRESS_HUFF = 3;    /* Huffman compression even if server supports LZ1 */
	
	public static final short NTATT_FTYPE_MASK = 0x0070;   /* File type mask */
	public static final short NTATT_FTYPE_FLAT = 0x0000;   /* Normal one fork file */
	public static final short NTATT_FTYPE_MACBIN = 0x0010; /* MacBinaryII file */
	public static final short NTATT_FTYPE_EBCDIC = 0x0020; /* EBCDIC flat file */
	public static final short NTATT_NODEALLOC = 0x0080;    /* Don't deallocate object when item is deleted */
	
	public static final short ATTRIB_READONLY = 0x0001;	/* file was read-only */
	public static final short ATTRIB_PRIVATE = 0x0002;	/* file was private or public */

	public static final short FILEFLAG_SIGN = 0x0001;	/* file object has object digest appended */
	public static final short FILEFLAG_INDOC = 0x0002;	/* file is represented by an editor run in the document */
	public static final short FILEFLAG_MIME = 0x0004;  /* file object has mime data appended */
	public static final short FILEFLAG_AUTOCOMPRESSED = 0x0080; /* file is a folder automaticly [sic] compressed by Notes */
										/* and NSFDbGetObjectInfo, NSFDbCopyObject. */
	
	/*	Flags used for NSFNoteExtractFileExt */

	public static final short NTEXT_RESONLY = 0x0001;	/*	If a Mac attachment, extract resource fork only. */
	public static final short NTEXT_FTYPE_MASK = 0x0070;	/*	File type mask */
	public static final short NTEXT_FTYPE_FLAT = 0x0000;	/*	Normal one fork file */
	public static final short NTEXT_FTYPE_MACBIN = 0x0010;	/*	MacBinaryII file */
	public static final short NTEXT_RAWMIME = 0x0080;	/*	Do not decode MIME content transfer encoding */
	public static final short NTEXT_IGNORE_HUFF2 = 0x0100;	/*	Ignore checksum mismatch and save data anyway */
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nsfnote.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static final int NULLCIPHERHANDLE = NULLMEMHANDLE;
	
	/* Open Flag Definitions.  These flags are passed to NSFNoteOpen. */

	public static final int OPEN_SUMMARY            = 0x0001;  /* open only summary info */
	public static final int OPEN_NOVERIFYDEFAULT    = 0x0002;  /* don't bother verifying default bit */
	public static final int OPEN_EXPAND             = 0x0004;  /* expand data while opening */
	public static final int OPEN_NOOBJECTS          = 0x0008;  /* don't include any objects */
	public static final int OPEN_NOPLACEHOLDERS     = 0x0010;  /* don't include "placeholder" items */
	                                                            /* (0-length items stored in forms) */
	public static final int OPEN_SHARE              = 0x0020;  /* open in a "shared" memory mode */
	public static final int OPEN_CANONICAL          = 0x0040;  /* Return ALL item values in canonical form */

	public static final int OPEN_HIGHLIGHTS         = 0x0080;  /* Open with FT search highlights */
	public static final int OPEN_MARK_READ          = 0x0100;  /* Mark unread if unread list is currently associated */
	public static final int OPEN_ABSTRACT           = 0x0200;  /* Only open an abstract of large documents */
	public static final int OPEN_NAMELIST           = 0x0400;  /* Return name list as a side-effect of open */
	public static final int OPEN_OVERRIDE_NO_COPY   = 0x0800;  /* Override OBJECT_NO_COPY.  By default, you don't get these object items. */
	public static final int OPEN_RESPONSE_ID_TABLE  = 0x1000;  /* Return response ID table */
	public static final int OPEN_PARENT_NOTEID      = 0x2000;  /* Calculate the Parent NoteID if possible; used by DBCompact. */
	public static final int OPEN_UNLINKED           = 0x4000;  /* Do not resolve links to the object store; used by DBCompact. */
	public static final int OPEN_INCREMENTAL_FULL   = 0x8000;  /* Open an Incremental Type NOTE Fully 
	                                                            (Keeping it marked as an incremental type note)
	                                                            When this is used, NoteUpdate will not try and determine
	                                                            if anything changed.  It will use this note as it is. */
	public static final int OPEN_INCREMENTAL_LOG    = 0x00010000;  /* Open Incrementally and return the data lengths as
	                                                                  part of the placeholder items */
	public static final int OPEN_WITH_FOLDERS       = 0x00020000;  /* Include folder objects - default is not to */
	public static final int OPEN_SELECTED_FIELDS    = 0x00040000;  /* Open a note returning Selected Fields only */
	public static final int OPEN_INCREMENTAL_NO_PLACEHOLDERS =   0x00080000;  
	                                            /* Open an Incremental Type Note and return
	                                                if with no place holders if it is
	                                                an unambiguous note. */
	public static final int OPEN_CACHE              = 0x00100000;  /* If specified, the open will 
	                                                                check to see if this note
	                                                                had already been read and
	                                                                saved in memory.  If not, 
	                                                                and the database is server
	                                                                based, we will also check
	                                                                the on-disk cache.  If the
	                                                                note is not found, it is
	                                                                cached in memory and at some
	                                                                time in the future commited
	                                                                to a local on disk cache. 
	                                                               The notes are guaranteed to be
	                                                                as up to date as the last time
	                                                                NSFValidateNoteCache was called.
	                                                                Minimally, this should be called
	                                                                the 1st time a database is 
	                                                                opened prior to specifying 
	                                                                this flag. */
	public static final int OPEN_CACHEONDISK           = 0x00200000;  /* If specified, the on-disk cache
	                                                                    should be checked regardless
	                                                                    of the location of the originating
	                                                                    Db. */
	public static final int OPEN_SERVER_FOR_CLIENT     = 0x00400000;  /* If specified, the note is being
	                                                                  opened by a server to return to a 
	                                                                  client */
	public static final int OPEN_RETURN_ISPROTECTED    = 0x00800000;  /* If set, Note body flag _NOTE_IS_PROTECTED is always valid.  If this flag
	                                                                    is not passed in, this flag may not be valid */
	public static final int OPEN_RAW_RFC822_TEXT       =   0x01000000;  /* If set, leave TYPE_RFC822_TEXT items in abstract
	                                                                    format.  Otherwise, convert to TYPE_TEXT/TYPE_TIME. */
	public static final int OPEN_RAW_MIME_PART         =  0x02000000;  /* If set, leave TYPE_MIME_PART items in abstract
	                                                                      format.  Otherwise, convert to TYPE_COMPOSITE. */
	public static final int OPEN_RAW_MIME              = (OPEN_RAW_RFC822_TEXT | OPEN_RAW_MIME_PART);
	
	public static final int OPEN_SOFT_DELETED          = 0x04000000;  /* Open note even if it soft-deleted */
	public static final int OPEN_BOOKMARK_CACHE        = 0x08000000;  /* check the bookmark file for this entry*/
	public static final int OPEN_MAX_POOL              = 0x10000000;  /* Initial pool creation should be maximized to avoid realloc growth as items are added */
	public static final int OPEN_REFERENCE             = 0x20000000; /* Keep it a reference note if it is one - don't merge it to make a regular note*/
	public static final int OPEN_SELECTED_DATATYPES    = 0x40000000;  /* Open a note returning Selected Datatypes only */

	public static final int OPEN_NO_ACCESS_CHECKING    = 0x80000000;
	public static final int OPEN2_NODECOMPRESS         = 0x00000001;  /* Don't decompress any cmopressed parts of note */

	public static final int NOTE_ID_SPECIAL            = 0xFFFF0000;
	public static final short NOTE_CLASS_DOCUMENT        = 0x0001;      /* document note */
	public static final short NOTE_CLASS_DATA            = NOTE_CLASS_DOCUMENT; /* old name for document note */
	public static final short NOTE_CLASS_INFO            = 0x0002;      /* notefile info (help-about) note */
	public static final short NOTE_CLASS_FORM            = 0x0004;      /* form note */
	public static final short NOTE_CLASS_VIEW            = 0x0008;      /* view note */
	public static final short NOTE_CLASS_ICON            = 0x0010;      /* icon note */
	public static final short NOTE_CLASS_DESIGN          = 0x0020;      /* design note collection */
	public static final short NOTE_CLASS_ACL             = 0x0040;      /* acl note */
	public static final short NOTE_CLASS_HELP_INDEX      = 0x0080;      /* Notes product help index note */
	public static final short NOTE_CLASS_HELP            = 0x0100;      /* designer's help note */
	public static final short NOTE_CLASS_FILTER          = 0x0200;      /* filter note */
	public static final short NOTE_CLASS_FIELD           = 0x0400;      /* field note */
	public static final short NOTE_CLASS_REPLFORMULA     = 0x0800;      /* replication formula */
	public static final short NOTE_CLASS_PRIVATE         = 0x1000;      /* Private design note, use $PrivateDesign view to locate/classify */
	public static final short NOTE_CLASS_DEFAULT         = (short)0x8000;      /* MODIFIER - default version of each */
	public static final short NOTE_CLASS_NOTIFYDELETION = NOTE_CLASS_DEFAULT; /* see SEARCH_NOTIFYDELETIONS */
	public static final short NOTE_CLASS_ALL              = 0x7fff;      /* all note types */
	public static final short NOTE_CLASS_ALLNONDATA       = 0x7ffe;      /* all non-data notes */
	public static final short NOTE_CLASS_NONE             = 0x0000;      /* no notes */
	public static final short NC_MASK_DB2_VISIBLE         = 0x0FFF;      /* all classes up to NOTE_CLASS_REPLFORMULA*/

	public static final short ITEM_SIGN =			0x0001;	/* This field will be signed if requested */
	public static final short ITEM_SEAL =			0x0002;	/* This field will be encrypted if requested */
	public static final short ITEM_SUMMARY =		0x0004;	/* This field can be referenced in a formula */
	public static final short ITEM_DEFAULT_TYPE =	0x0008;	/* INTERNAL (ON-DISK ONLY!) */
	public static final short ITEM_SEALED	=		0x0010;	/* INTERNAL (used by sealing) */
	public static final short ITEM_READWRITERS =	0x0020;	/* This field identifies subset of users that have read/write access */
	public static final short ITEM_NAMES =		0x0040;	/* This field contains user/group names */
	public static final short ITEM_NOUPDATE =		0x0080;	/* INTERNAL (field should not be saved to disk when note is saved) */
	public static final short ITEM_PLACEHOLDER =	0x0100;	/* Simply add this item to "item name table", but do not store */
	public static final short ITEM_PROTECTED =	0x0200;	/* This field cannot be modified except by "owner" */
	public static final short ITEM_READERS =		0x0400;	/* This field identifies subset of users that have read access */
	public static final short ITEM_SAVED_SUMMARY =0x0800;	/* INTERNAL */
	public static final short ITEM_UNCHANGED =	0x1000;	/* Item is same as on-disk  */
	public static final short ITEM_LINK =			0x2000;	/* This field is a link to/from the object store */
	public static final short ITEM_LINKED	=		0x4000;	/* This field's value is stored in the object store */
	public static final short ITEM_RARELY_USED_NAME =	(short)0x8000;	/* Do not add this item name to the unique named
														   key table, but instead store it with the note.
														   It will not be shared among notes. */
	
	public static final short _NOTE_DB =             0; /* IDs for NSFNoteGet&SetInfo */
	public static final short _NOTE_ID =             1; /* (When adding new values, see the */ 
	public static final short _NOTE_OID =            2; /*  table in NTINFO.C */
	public static final short _NOTE_CLASS =          3;
	public static final short _NOTE_MODIFIED =       4;
	public static final short _NOTE_PRIVILEGES =     5; /* For pre-V3 compatibility. Should use $Readers item */
	public static final short _NOTE_FLAGS	=          7;
	public static final short _NOTE_ACCESSED =       8;
	public static final short _NOTE_PARENT_NOTEID =  10; /* For response hierarchy */
	public static final short _NOTE_RESPONSE_COUNT = 11; /* For response hierarchy */
	public static final short _NOTE_RESPONSES =      12; /* For response hierarchy */
	public static final short _NOTE_ADDED_TO_FILE =  13; /* For AddedToFile time */
	public static final short _NOTE_OBJSTORE_DB =    14; /* DBHANDLE of object store used by linked items */
    
	
	public static final short UPDATE_FORCE =            (short)0x0001; /* update even if ERR_CONFLICT */
	public static final short UPDATE_NAME_KEY_WARNING = (short)0x0002; /* give error if new field name defined */
	public static final short UPDATE_NOCOMMIT =         (short)0x0004; /* do NOT do a database commit after update */
	public static final short UPDATE_NOREVISION =       (short)0x0100; /* do NOT maintain revision history */
	public static final short UPDATE_NOSTUB =           (short)0x0200; /* update body but leave no trace of note in file if deleted */
	public static final short UPDATE_INCREMENTAL =      (short)0x4000; /* Compute incremental note info */
	public static final short UPDATE_DELETED =          (short)0x8000; /* update body DELETED */
    
	public abstract long NSFNoteCreate(long hDb) throws DominoException;
    public abstract long NSFNoteOpen(long hDb, int noteID, short flags) throws DominoException;
    /** @since Notes/Domino 5.0 */
    public abstract long NSFNoteOpenExt(long hDb, int noteID, int flags) throws DominoException;
    public abstract void NSFNoteClose(long hNote) throws DominoException;
    /** @since Notes/Domino 5.0 */
    public abstract void NSFNoteDeleteExtended(long hDb, int noteID, int flags) throws DominoException;
    /**
     * Verifies the structure of an in-memory note.
     * @param hNote handle to an open in-memory note.
     * @throws DominoException if the structure of the note is invalid or if there was an error verifying the note.
     */
    public abstract void NSFNoteCheck(long hNote) throws DominoException;
    /**
     * @param noteMember See <code>_NOTE_xxx</code>
     * @param valuePtr The pointer value of an already-allocated data structure appropriate for the provided <code>noteMember</code> 
     */
    public abstract void NSFNoteGetInfo(long hNote, short noteMember, long valuePtr);
    /**
     * @param noteMember See <code>_NOTE_xxx</code>
     * @param valuePtr The pointer value of an already-allocated data structure appropriate for the provided <code>noteMember</code> 
     */
    public abstract void NSFNoteSetInfo(long hNote, short noteMember, long valuePtr);
    public abstract void NSFNoteUpdate(long hNote, short updateFlags) throws DominoException;
    /** @since Notes/Domino 4.0 */
    public abstract void NSFNoteUpdateExtended(long hNote, int updateFlags) throws DominoException;
    

    public abstract void NSFNoteAttachFile(long hNote, String itemName, String fileName, String origPathName, short encodingType) throws DominoException;
    public abstract void NSFNoteDetachFile(long hNote, BLOCKID itemBlockId) throws DominoException;
    
    /** @since Notes/Domino 8.0.1 */
    public abstract void NSFNoteCipherDecrypt(long hNote, long hKFC, int decryptFlags, long rethCipherForAttachments) throws DominoException;
    /** @since Notes/Domino 8.0.1 */
    public abstract void NSFNoteCipherExtractFile(long hNote, BLOCKID itemBlockId, int extractFlags, int hDecryptionCipher, String fileName) throws DominoException;
    /** @since Notes/Domino 8.0.1 */
    public abstract void NSFNoteCipherExtractWithCallback(long hNote, BLOCKID bhItem, int extractFlags, long hDecryptionCipher, NOTEEXTRACTCALLBACK noteExtractCallback, int reserved, long pReserved) throws DominoException;

    /** @since Notes/Domino 7.0.2 */
    public abstract boolean NSFNoteHasMIME(long hNote);
    public abstract boolean NSFNoteHasObjects(long hNote, BLOCKID firstItemBlockId);
    
    // Item get/set
    
    public abstract void NSFItemInfo(long hNote, String itemName, BLOCKID itemBlockId, ShortRef valueDataType, BLOCKID valueBlockId, IntRef valueLen) throws DominoException;
    public abstract void NSFItemInfoNext(long hNote, BLOCKID nextItem, String itemName, BLOCKID itemBlockId, ShortRef valueDataType, BLOCKID valueBlockId, IntRef valueLen) throws DominoException;
    public abstract boolean NSFItemIsPresent(long hNote, String itemName);
    /** @since Notes/Domino 6.0 */
    public abstract void NSFItemQueryEx(long hNote, BLOCKID itemBlockId, long retItemName, short itemNameBufferLength, ShortRef retItemNameLength, ShortRef retItemFlags, ShortRef retDataType, BLOCKID retbhValue, IntRef retValueLength, ByteRef retSeqByte, ByteRef retDupItemID);
    public abstract void NSFItemDelete(long hNote, String itemName) throws DominoException;
    public abstract void NSFItemDeleteByBLOCKID(long hNote, BLOCKID itemBlockId) throws DominoException;
    
    public abstract void NSFItemSetTime(long hNote, String itemName, TIMEDATE timeDate) throws DominoException;
    public abstract void NSFItemSetText(long hNote, String itemName, String value) throws DominoException;
    public abstract void NSFItemSetTextSummary(long hNote, String itemName, String value, boolean summary) throws DominoException;
    public abstract void NSFItemSetNumber(long hNote, String itemName, double value) throws DominoException;
    public abstract void NSFItemAppend(long hNote, short itemFlags, String itemName, short itemType, long valuePtr, int valueLen) throws DominoException;
    public abstract void NSFItemAppendByBLOCKID(long hNote, short itemFlags, String itemName, BLOCKID valueBlockId, int valueLen, BLOCKID retItemBlockId) throws DominoException;
    
    public abstract void NSFItemScan(long hNote, NSFITEMSCANPROC actionRoutine) throws DominoException;
    
    public abstract String NSFItemConvertToText(long hNote, String itemName, short textBufLen, char separator) throws DominoException;
    
    public abstract String NSFItemGetText(long hNote, String itemName, short textBufLen) throws DominoException;
    
    // Profile documents
    
    public abstract void NSFProfileDelete(long hDB, String profileName, String userName) throws DominoException;
    public abstract void NSFProfileEnum(long hDB, String profileName, NSFPROFILEENUMPROC callback, int flags) throws DominoException;
    public abstract void NSFProfileGetField(long hDB, String profileName, String userName, String fieldName, long retDataType, long retbhValue, long retValueLength) throws DominoException;
    public abstract long NSFProfileOpen(long hDB, String profileName, String userName, boolean copyProfile) throws DominoException;
    public abstract void NSFProfileSetField(long hDB, String profileName, String userName, String fieldName, short dataType, long value, int valueLength) throws DominoException;
    public abstract void NSFProfileUpdate(long hProfile, String profileName, String userName) throws DominoException;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nsfobjec.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public abstract long NSFDbReadObject(long hDb, int objectId, int offset, int length) throws DominoException;
    public abstract void NSFDbGetObjectSize(long hDb, int objectId, short objectType, IntRef retSize, ShortRef retClass, ShortRef retPrivileges) throws DominoException;
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// idtable.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final short IDTABLE_MODIFIED = 0x0001; /* modified - set by Insert/Delete */
	                                                     /* and can be cleared by caller if desired */
    public static final short IDTABLE_INVERTED = 0x0002; /* sense of list inverted */
	                                                     /* (reserved for use by caller only) */
    
    /** @since Notes/Domino 4.5 */
    public abstract boolean IDAreTablesEqual(long hSrc1Table, long hSrc2Table);
	/**
	 * @param alignment alignment value used in a compression algorithm. For note IDs, use {@link com.darwino.domino.napi.c.C#sizeOfNOTEID}.
	 */
	public abstract long IDCreateTable(int alignment) throws DominoException;
	public abstract boolean IDDelete(long hTable, int id) throws DominoException;
	public abstract void IDDeleteAll(long hTable) throws DominoException;
	/** @since Notes/Domino 4.5 */
	public abstract void IDDeleteTable(long hTable, long hIDsToDelete) throws DominoException;
	/** @since Notes/Domino 4.0 */
	public abstract void IDDestroyTable(/*dhandle*/long hTable) throws DominoException;
	public abstract int IDEntries (/*dhandle*/long hTable) throws DominoException;
	public abstract void IDEnumerate(/*dhandle*/long hTable, IDENUMERATEPROC callback) throws DominoException;
	public abstract boolean IDInsert(long hTable, int id) throws DominoException;
	/** @since Notes/Domino 4.5 */
	public abstract void IDInsertTable(long hTable, long hIDsToAdd) throws DominoException;
	public abstract boolean IDIsPresent(long hTable, int id) throws DominoException;
	public abstract boolean IDScan(/*dhandle*/long hTable, boolean first, IntRef retID);
	public abstract long IDTableCopy(long hTable) throws DominoException;
	/**
	 * Note: unlike most ID* functions, this takes a pointer, not a handle
	 */
	public abstract short IDTableFlags(long pIDTable);
	/**
	 * Note: unlike most ID* functions, the final parameter is a pointer to a handle, not a handle itself
	 * @since Notes/Domino 4.5
	 */
	public abstract void IDTableIntersect(long hSrc1Table, long hSrc2Table, long rethDstTable) throws DominoException;
	/**
	 * Note: unlike most ID* functions, this takes a pointer, not a handle
	 */
	public abstract void IDTableSetFlags(long pIDTable, short flags);
	/**
	 * Note: unlike most ID* functions, this takes a pointer, not a handle
	 */
	public abstract void IDTableSetTime(long pIDTable, TIMEDATE time);
	public abstract int IDTableSize(long hTable);
	/**
	 * Note: unlike most ID* functions, this takes a pointer, not a handle
	 */
	public abstract int IDTableSizeP(long pIDTable);
	/**
	 * <p>Note: unlike most ID* functions, this takes a pointer, not a handle</p>
	 * <p>Additionally, unlike the underlying C function, this returns a pointer to the
	 * resultant TIMEDATE structure, not the structure itself.</p>
	 */
	public abstract long IDTableTime(long pIDTable);
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// htmlapi.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static final int HTMLAPI_PROP_TEXTLENGTH = 0;
	public static final int HTMLAPI_PROP_NUMREFS = 1;
	public static final int HTMLAPI_PROP_USERAGENT_LEN = 3;
	public static final int HTMLAPI_PROP_USERAGENT = 4;
	public static final int HTMLAPI_PROP_LINKHANDLING = 5;
	public static final int HTMLAPI_PROP_BINARYDATA = 6;
	public static final int HTMLAPI_PROP_MIMEMAXLINELENSEEN = 102;
	public static final int HTMLAPI_PROP_CONFIG_MAXREFLISTSIZE_MB = 901;
	
	// enum HTML_API_REF_TYPE
	public static final int HTMLAPI_REF_UNKNOWN = 0;
	public static final int HTMLAPI_REF_HREF = 1;
	public static final int HTMLAPI_REF_IMG = 2;
	public static final int HTMLAPI_REF_FRAME = 3;
	public static final int HTMLAPI_REF_APPLET = 4;
	public static final int HTMLAPI_REF_EMBED = 5;
	public static final int HTMLAPI_REF_OBJECT = 6;
	public static final int HTMLAPI_REF_BASE = 7;
	public static final int HTMLAPI_REF_BACKGROUND = 8;
	public static final int HTMLAPI_REF_CID = 9;
	
	// enum CmdId
	public static final int kUnknownCmdId               = 0;
	public static final int kOpenServerCmdId            = 1;
	public static final int kOpenDatabaseCmdId          = 2;
	public static final int kOpenViewCmdId              = 3;
	public static final int kOpenDocumentCmdId          = 4;
	public static final int kOpenElementCmdId           = 5;
	public static final int kOpenFormCmdId              = 6;
	public static final int kOpenAgentCmdId             = 7;
	public static final int kOpenNavigatorCmdId         = 8;
	public static final int kOpenIconCmdId              = 9;
	public static final int kOpenAboutCmdId             = 10;
	public static final int kOpenHelpCmdId              = 11;
	public static final int kCreateDocumentCmdId        = 12;
	public static final int kSaveDocumentCmdId          = 13;
	public static final int kEditDocumentCmdId          = 14;
	public static final int kDeleteDocumentCmdId        = 15;
	public static final int kSearchViewCmdId            = 16;
	public static final int kSearchSiteCmdId            = 17;
	public static final int kNavigateCmdId              = 18;
	public static final int kReadFormCmdId              = 19;
	public static final int kRequestCertCmdId           = 20;
	public static final int kReadDesignCmdId            = 21;
	public static final int kReadViewEntriesCmdId       = 22;
	public static final int kReadEntriesCmdId           = 23;
	public static final int kOpenPageCmdId              = 24;
	public static final int kOpenFrameSetCmdId          = 25;
	public static final int kOpenFieldCmdId             = 26;
	public static final int kSearchDomainCmdId          = 27;
	public static final int kDeleteDocumentsCmdId       = 28;
	public static final int kLoginUserCmdId             = 29;
	public static final int kLogoutUserCmdId            = 30;
	public static final int kOpenImageResourceCmdId     = 31;
	public static final int kOpenImageCmdId             = 32;
	public static final int kCopyToFolderCmdId          = 33;
	public static final int kMoveToFolderCmdId          = 34;
	public static final int kRemoveFromFolderCmdId      = 35;
	public static final int kUndeleteDocumentsCmdId     = 36;
	public static final int kRedirectCmdId              = 37;
	public static final int kGetOrbCookieCmdId          = 38;
	public static final int kOpenCssResourceCmdId       = 39;
	public static final int kOpenFileResourceCmdId      = 40;
	public static final int kOpenJavascriptLibCmdId     = 41;
	public static final int kUnImplemented_01           = 42;
	public static final int kChangePasswordCmdId        = 43;
	public static final int kOpenPreferencesCmdId       = 44;
	public static final int kOpenWebServiceCmdId        = 45;
	public static final int kWsdlCmdId                  = 46;
	public static final int kGetImageCmdId              = 47;
	
	// enum CmdArgValueType
	public static final int CAVT_String     = 0;
	public static final int CAVT_Int        = 1;
	public static final int CAVT_NoteId     = 2;
	public static final int CAVT_UNID       = 3;
	public static final int CAVT_StringList = 4;
	
	// enum UAT
	public static final int UAT_None                    = 0;
	public static final int UAT_Server                  = 1;
	public static final int UAT_Database                = 2;
	public static final int UAT_View                    = 3;
	public static final int UAT_Form                    = 4;
	public static final int UAT_Navigator               = 5;
	public static final int UAT_Agent                   = 6;
	public static final int UAT_Document                = 7;
	public static final int UAT_Filename                = 8; 
	public static final int UAT_ActualFilename          = 9; 
	public static final int UAT_Field                   = 10;
	public static final int UAT_FieldOffset             = 11;
	public static final int UAT_FieldSuboffset          = 12;
	public static final int UAT_Page                    = 13;
	public static final int UAT_FrameSet                = 14;
	public static final int UAT_ImageResource           = 15;
	public static final int UAT_CssResource             = 16;
	public static final int UAT_JavascriptLib           = 17;
	public static final int UAT_FileResource            = 18;
	public static final int UAT_About                   = 19;
	public static final int UAT_Help                    = 20;
	public static final int UAT_Icon                    = 21;
	public static final int UAT_SearchForm              = 22;
	public static final int UAT_SearchSiteForm          = 23;
	public static final int UAT_Outline                 = 24;
	public static final int UAT_NumberOfTypes           = 25;
	
	// enum USV
	public static final int USV_About               = 0;
	public static final int USV_Help                = 1;
	public static final int USV_Icon                = 2;
	public static final int USV_DefaultView         = 3;
	public static final int USV_DefaultForm         = 4;
	public static final int USV_DefaultNav          = 5;
	public static final int USV_SearchForm          = 6;
	public static final int USV_DefaultOutline      = 7;
	public static final int USV_First               = 8;
	public static final int USV_FileField           = 9;
	public static final int USV_NumberOfValues      = 10;
	
	// enum URT
	public static final int URT_None                = 0;
	public static final int URT_Name                = 1;
	public static final int URT_Unid                = 2;
	public static final int URT_NoteId              = 3;
	public static final int URT_Special             = 4;
	public static final int URT_RepId               = 5;
	public static final int URT_NumberOfTypes       = 6;
	
	/** @since Notes/Domino 7.0.2 */
	public abstract void HTMLProcessInitialize() throws DominoException;
	/** @since Notes/Domino 7.0.2 */
	public abstract void HTMLProcessTerminate();
	/** @since Notes/Domino 7.0.2 */
	public abstract long HTMLCreateConverter() throws DominoException;
	/** @since Notes/Domino 7.0.2 */
	public abstract void HTMLDestroyConverter(long hHTML) throws DominoException;
	/** @since Notes/Domino 7.0.2 */
	public abstract void HTMLConvertItem(long hHTML, long hDb, long hNote, String itemName) throws DominoException;
	/** @since Notes/Domino 7.0.2 */
	public abstract void HTMLConvertElement(long hHTML, long hDb, long hNote, String itemName, int itemIndex, int offset) throws DominoException;
	/**
	 * @param pProperty The pointer value of an already-allocated data structure appropriate for the provided <code>propertyType</code>
	 * @since Notes/Domino 7.0.2
	 */
	public abstract void HTMLGetProperty(long hHTML, int propertyType, long pProperty) throws DominoException;
	/** @since Notes/Domino 7.0.2 */
	public abstract void HTMLSetHTMLOptions(long hHTML, String[] optionList) throws DominoException;
	/**
	 * @param textLength The maximum number of characters to be copied, modified when run to be the actual number copied. This must be &le; the size of the buffer
	 * @param pText The pointer value of an already-allocated text buffer
	 * @since Notes/Domino 7.0.2
	 */
	public abstract void HTMLGetText(long hHTML, int startingOffset, IntRef textLength, long pText) throws DominoException;
	/**
	 * @param index The index of the reference to get
	 * @since Notes/Domino 7.0.2
	 */
	public abstract long HTMLGetReference(long hHTML, long index) throws DominoException;
	/**
	 * @return A pointer to the reference data. The caller is responsible for unlocking this.
	 * @since Notes/Domino 7.0.2
	 */
	public abstract long HTMLLockAndFixupReference(long hRef) throws DominoException;
	/** @since Notes/Domino 7.0.2 */
	public abstract void HTMLSetReferenceText(long hHTML, long index, String refText) throws DominoException;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// htmlapi.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final short ERR_HTMLAPI_GENERATING_HTML = (PKG_HTTP+93);
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// mime.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** @since Notes/Domino 7.0.2 */
	public abstract void MIMEConvertCDParts(long hNote, boolean canonical, boolean isMIME, long hCC) throws DominoException;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// mimedir.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** @since Notes/Domino 7.0.2 */
	public abstract void MimeGetTypeInfoFromExt(String ext, long typeBufPtr, short typeBufLen, long subtypeBufPtr, short subtypeBufLen, long descrBufPtr, short descrBufLen);
	/** @since Notes/Domino 7.0.2 */
	public abstract void MimeGetExtFromTypeInfo(String type, String subtype, long extBufPtr, short extBufLen, long descrBufPtr, short descrBufLen);
	/** @since Notes/Domino 7.0.2 */
	public abstract boolean MIMEEntityIsMultiPart(long pMimeEntity);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nsfmime.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** @since Notes/Domino 7.0.2 */
	public abstract long NSFMimePartGetPart(BLOCKID bhItem) throws DominoException;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ods.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static final short LONGRECORDLENGTH = 0x0000;
	public static final short WORDRECORDLENGTH = (short)0xff00;
	public static final short BYTERECORDLENGTH = 0;		/* High byte contains record length */
	
	// Base ODS types
	public static final short _SHORT = 0;
	public static final short _USHORT = _SHORT;
	public static final short _WORD = _SHORT;
	public static final short _STATUS = _SHORT;
	public static final short _UNICODE = _SHORT;
	public static final short _LONG = 1;
	public static final short _FLOAT = 2;
	public static final short _DWORD = _LONG;
	public static final short _ULONG = _LONG;
	
	// ODS types that are the size of a base type
	public static final short _NUMBER = _FLOAT;
	public static final short _NOTEID = _LONG;
	
	public static final short SIG_INVALID = 0;

	/* Signatures for Composite Records in items of data type COMPOSITE */

	public static final short SIG_CD_PDEF_MAIN = (83 | WORDRECORDLENGTH); /* Signatures for items used in Property Broker definitions. LI 3925.04 */
	public static final short SIG_CD_PDEF_TYPE = (84 | WORDRECORDLENGTH);
	public static final short SIG_CD_PDEF_PROPERTY = (85 | WORDRECORDLENGTH);
	public static final short SIG_CD_PDEF_ACTION = (86 | WORDRECORDLENGTH);
	public static final short	SIG_CD_TABLECELL_DATAFLAGS = (87 | BYTERECORDLENGTH);
	public static final short SIG_CD_EMBEDDEDCONTACTLIST = (88 | WORDRECORDLENGTH);
	public static final short SIG_CD_IGNORE		 = (89 | BYTERECORDLENGTH);
	public static final short SIG_CD_TABLECELL_HREF2 = (90 | WORDRECORDLENGTH);
	public static final short SIG_CD_HREFBORDER	 = (91 | WORDRECORDLENGTH);
	public static final short SIG_CD_TABLEDATAEXTENSION = (92 | WORDRECORDLENGTH);
	public static final short SIG_CD_EMBEDDEDCALCTL = (93 | WORDRECORDLENGTH);
	public static final short SIG_CD_ACTIONEXT	 = (94 | WORDRECORDLENGTH);
	public static final short SIG_CD_EVENT_LANGUAGE_ENTRY = (95 | WORDRECORDLENGTH);
	public static final short SIG_CD_FILESEGMENT	 = (96 | LONGRECORDLENGTH);
	public static final short SIG_CD_FILEHEADER	 = (97 | LONGRECORDLENGTH);
	public static final short	SIG_CD_DATAFLAGS	 = (98 | BYTERECORDLENGTH);

	public static final short SIG_CD_BACKGROUNDPROPERTIES = (99 | BYTERECORDLENGTH);

	public static final short SIG_CD_EMBEDEXTRA_INFO = (100 | WORDRECORDLENGTH);
	public static final short SIG_CD_CLIENT_BLOBPART = (101 | WORDRECORDLENGTH);
	public static final short SIG_CD_CLIENT_EVENT	 = (102 | WORDRECORDLENGTH);
	public static final short SIG_CD_BORDERINFO_HS = (103 | WORDRECORDLENGTH);
	public static final short SIG_CD_LARGE_PARAGRAPH = (104 | WORDRECORDLENGTH);
	public static final short SIG_CD_EXT_EMBEDDEDSCHED = (105 | WORDRECORDLENGTH);
	public static final short SIG_CD_BOXSIZE	 = (106 | BYTERECORDLENGTH);
	public static final short SIG_CD_POSITIONING = (107 | BYTERECORDLENGTH);
	public static final short SIG_CD_LAYER	 = (108 | BYTERECORDLENGTH);
	public static final short SIG_CD_DECSFIELD = (109 | WORDRECORDLENGTH);
	public static final short SIG_CD_SPAN_END	 = (110 | BYTERECORDLENGTH);	/* Span End */
	public static final short SIG_CD_SPAN_BEGIN = (111 | BYTERECORDLENGTH);	/* Span Begin */
	public static final short SIG_CD_TEXTPROPERTIESTABLE = (112 | WORDRECORDLENGTH);	/* Text Properties Table */
									  
	public static final short SIG_CD_HREF2	 = (113 | WORDRECORDLENGTH);
	public static final short SIG_CD_BACKGROUNDCOLOR = (114 | BYTERECORDLENGTH);
	public static final short SIG_CD_INLINE	 = (115 | WORDRECORDLENGTH);
	public static final short SIG_CD_V6HOTSPOTBEGIN_CONTINUATION = (116 | WORDRECORDLENGTH);
	public static final short SIG_CD_TARGET_DBLCLK = (117 | WORDRECORDLENGTH);
	public static final short SIG_CD_CAPTION	 = (118 | WORDRECORDLENGTH);
	public static final short SIG_CD_LINKCOLORS = (119 | WORDRECORDLENGTH);
	public static final short SIG_CD_TABLECELL_HREF = (120 | WORDRECORDLENGTH);
	public static final short SIG_CD_ACTIONBAREXT = (121 | WORDRECORDLENGTH);
	public static final short SIG_CD_IDNAME	 = (122 | WORDRECORDLENGTH);
	public static final short SIG_CD_TABLECELL_IDNAME = (123 | WORDRECORDLENGTH);
	public static final short SIG_CD_IMAGESEGMENT = (124 | LONGRECORDLENGTH);
	public static final short SIG_CD_IMAGEHEADER = (125 | LONGRECORDLENGTH);
	public static final short	SIG_CD_V5HOTSPOTBEGIN = (126 | WORDRECORDLENGTH);
	public static final short SIG_CD_V5HOTSPOTEND = (127 | BYTERECORDLENGTH);
	public static final short SIG_CD_TEXTPROPERTY = (128 | WORDRECORDLENGTH);
	public static final short	SIG_CD_PARAGRAPH = (129 | BYTERECORDLENGTH);
	public static final short	SIG_CD_PABDEFINITION = (130 | WORDRECORDLENGTH);
	public static final short	SIG_CD_PABREFERENCE = (131 | BYTERECORDLENGTH);
	public static final short SIG_CD_TEXT		 = (133 | WORDRECORDLENGTH);
	public static final short	SIG_CD_HEADER	 = (142 | WORDRECORDLENGTH);
	public static final short	SIG_CD_LINKEXPORT2 = (146 | WORDRECORDLENGTH);
	public static final short SIG_CD_BITMAPHEADER = (149 | LONGRECORDLENGTH);
	public static final short SIG_CD_BITMAPSEGMENT = (150 | LONGRECORDLENGTH);
	public static final short SIG_CD_COLORTABLE = (151 | LONGRECORDLENGTH);
	public static final short SIG_CD_GRAPHIC = (153 | LONGRECORDLENGTH);
	public static final short SIG_CD_PMMETASEG = (154 | LONGRECORDLENGTH);
	public static final short SIG_CD_WINMETASEG = (155 | LONGRECORDLENGTH);
	public static final short SIG_CD_MACMETASEG = (156 | LONGRECORDLENGTH);
	public static final short SIG_CD_CGMMETA = (157 | LONGRECORDLENGTH);
	public static final short SIG_CD_PMMETAHEADER = (158 | LONGRECORDLENGTH);
	public static final short SIG_CD_WINMETAHEADER = (159 | LONGRECORDLENGTH);
	public static final short SIG_CD_MACMETAHEADER = (160 | LONGRECORDLENGTH);
	public static final short SIG_CD_TABLEBEGIN = (163 | BYTERECORDLENGTH);
	public static final short SIG_CD_TABLECELL = (164 | BYTERECORDLENGTH);
	public static final short SIG_CD_TABLEEND	 = (165 | BYTERECORDLENGTH);
	public static final short SIG_CD_STYLENAME = (166 | BYTERECORDLENGTH);
	public static final short SIG_CD_STORAGELINK = (196 | WORDRECORDLENGTH);
	public static final short SIG_CD_TRANSPARENTTABLE = (197 | LONGRECORDLENGTH);
	public static final short SIG_CD_HORIZONTALRULE = (201 | WORDRECORDLENGTH);
	public static final short SIG_CD_ALTTEXT	 = (202 | WORDRECORDLENGTH);
	public static final short SIG_CD_ANCHOR	 = (203 | WORDRECORDLENGTH);
	public static final short SIG_CD_HTMLBEGIN = (204 | WORDRECORDLENGTH);
	public static final short SIG_CD_HTMLEND	 = (205 | WORDRECORDLENGTH);
	public static final short SIG_CD_HTMLFORMULA = (206 | WORDRECORDLENGTH);
	public static final short SIG_CD_NESTEDTABLEBEGIN = (207 | BYTERECORDLENGTH);
	public static final short SIG_CD_NESTEDTABLECELL = (208 | BYTERECORDLENGTH);
	public static final short SIG_CD_NESTEDTABLEEND = (209 | BYTERECORDLENGTH);
	public static final short SIG_CD_COLOR	 = (210 | BYTERECORDLENGTH);
	public static final short SIG_CD_TABLECELL_COLOR = (211 | BYTERECORDLENGTH);

	/* 212 thru 219 reserved for BSIG'S - don't use until we hit 255 */

	public static final short SIG_CD_BLOBPART	 = (220 | WORDRECORDLENGTH);
	public static final short SIG_CD_BEGIN	 = (221 | BYTERECORDLENGTH);
	public static final short SIG_CD_END		 = (222 | BYTERECORDLENGTH);
	public static final short SIG_CD_VERTICALALIGN = (223 | BYTERECORDLENGTH);
	public static final short SIG_CD_FLOATPOSITION  = (224 | BYTERECORDLENGTH);

	public static final short SIG_CD_TIMERINFO = (225 | BYTERECORDLENGTH);
	public static final short SIG_CD_TABLEROWHEIGHT = (226 | BYTERECORDLENGTH);
	public static final short SIG_CD_TABLELABEL = (227 | WORDRECORDLENGTH);
	public static final short SIG_CD_BIDI_TEXT = (228 | WORDRECORDLENGTH);
	public static final short SIG_CD_BIDI_TEXTEFFECT = (229 | WORDRECORDLENGTH);
	public static final short SIG_CD_REGIONBEGIN = (230 | WORDRECORDLENGTH);
	public static final short SIG_CD_REGIONEND = (231 | WORDRECORDLENGTH);
	public static final short SIG_CD_TRANSITION = (232 | WORDRECORDLENGTH);
	public static final short SIG_CD_FIELDHINT = (233 | WORDRECORDLENGTH);
	public static final short SIG_CD_PLACEHOLDER = (234 | WORDRECORDLENGTH);
	public static final short SIG_CD_EMBEDDEDOUTLINE = (236 | WORDRECORDLENGTH);
	public static final short SIG_CD_EMBEDDEDVIEW = (237 | WORDRECORDLENGTH);
	public static final short SIG_CD_CELLBACKGROUNDDATA = (238 | WORDRECORDLENGTH);

	/* Signatures for Frameset CD records */
	public static final short SIG_CD_FRAMESETHEADER = (239 | WORDRECORDLENGTH);
	public static final short SIG_CD_FRAMESET	 = (240 | WORDRECORDLENGTH);
	public static final short SIG_CD_FRAME	 = (241 | WORDRECORDLENGTH);
	/* Signature for Target Frame info on a link	*/
	public static final short SIG_CD_TARGET	 = (242 | WORDRECORDLENGTH);

	public static final short SIG_CD_MAPELEMENT = (244 | WORDRECORDLENGTH);
	public static final short SIG_CD_AREAELEMENT = (245 | WORDRECORDLENGTH);
	public static final short SIG_CD_HREF		 = (246 | WORDRECORDLENGTH);
	public static final short SIG_CD_EMBEDDEDCTL = (247 | WORDRECORDLENGTH);
	public static final short SIG_CD_HTML_ALTTEXT = (248 | WORDRECORDLENGTH);
	public static final short SIG_CD_EVENT	 = (249 | WORDRECORDLENGTH);
	public static final short SIG_CD_PRETABLEBEGIN = (251 | WORDRECORDLENGTH);
	public static final short SIG_CD_BORDERINFO = (252 | WORDRECORDLENGTH);
	public static final short SIG_CD_EMBEDDEDSCHEDCTL = (253 | WORDRECORDLENGTH);

	public static final short SIG_CD_EXT2_FIELD = (254 | WORDRECORDLENGTH);	/* Currency, numeric, and data/time extensions */
	public static final short SIG_CD_EMBEDDEDEDITCTL = (255 | WORDRECORDLENGTH);

	/* Can not go beyond 255.  However, there may be room at the beginning of 
		the list.  Check there.   */

	/* Signatures for Composite Records that are reserved internal records, */
	/* whose format may change between releases. */

	public static final short	SIG_CD_DOCUMENT_PRE_26 = (128 | BYTERECORDLENGTH);
	public static final short	SIG_CD_FIELD_PRE_36 = (132 | WORDRECORDLENGTH);
	public static final short	SIG_CD_FIELD	 = (138 | WORDRECORDLENGTH);
	public static final short	SIG_CD_DOCUMENT	 = (134 | BYTERECORDLENGTH);
	public static final short	SIG_CD_METAFILE	 = (135 | WORDRECORDLENGTH);
	public static final short	SIG_CD_BITMAP	 = (136 | WORDRECORDLENGTH);
	public static final short	SIG_CD_FONTTABLE = (139 | WORDRECORDLENGTH);
	public static final short	SIG_CD_LINK		 = (140 | BYTERECORDLENGTH);
	public static final short	SIG_CD_LINKEXPORT = (141 | BYTERECORDLENGTH);
	public static final short	SIG_CD_KEYWORD	 = (143 | WORDRECORDLENGTH);
	public static final short	SIG_CD_LINK2	 = (145 | WORDRECORDLENGTH);
	public static final short	SIG_CD_CGM		 = (147 | WORDRECORDLENGTH);
	public static final short	SIG_CD_TIFF		 = (148 | LONGRECORDLENGTH);
	public static final short SIG_CD_PATTERNTABLE = (152 | LONGRECORDLENGTH);
	public static final short SIG_CD_DDEBEGIN	 = (161 | WORDRECORDLENGTH);
	public static final short SIG_CD_DDEEND	 = (162 | WORDRECORDLENGTH);
	public static final short SIG_CD_OLEBEGIN	 = (167 | WORDRECORDLENGTH);
	public static final short SIG_CD_OLEEND	 = (168 | WORDRECORDLENGTH);
	public static final short SIG_CD_HOTSPOTBEGIN = (169 | WORDRECORDLENGTH);
	public static final short SIG_CD_HOTSPOTEND = (170 | BYTERECORDLENGTH);
	public static final short SIG_CD_BUTTON	 = (171 | WORDRECORDLENGTH);
	public static final short SIG_CD_BAR		 = (172 | WORDRECORDLENGTH);
	public static final short SIG_CD_V4HOTSPOTBEGIN = (173 | WORDRECORDLENGTH);
	public static final short SIG_CD_V4HOTSPOTEND = (174 | BYTERECORDLENGTH);
	public static final short SIG_CD_EXT_FIELD = (176 | WORDRECORDLENGTH);
	public static final short SIG_CD_LSOBJECT	 = (177 | WORDRECORDLENGTH);/* Compiled LS code*/
	public static final short SIG_CD_HTMLHEADER = (178 | WORDRECORDLENGTH); /* Raw HTML */
	public static final short SIG_CD_HTMLSEGMENT = (179 | WORDRECORDLENGTH);
	public static final short SIG_CD_LAYOUT	 = (183 | BYTERECORDLENGTH);
	public static final short SIG_CD_LAYOUTTEXT = (184 | BYTERECORDLENGTH);
	public static final short SIG_CD_LAYOUTEND = (185 | BYTERECORDLENGTH);
	public static final short SIG_CD_LAYOUTFIELD = (186 | BYTERECORDLENGTH);
	public static final short SIG_CD_PABHIDE	 = (187 | WORDRECORDLENGTH);
	public static final short SIG_CD_PABFORMREF = (188 | BYTERECORDLENGTH);
	public static final short SIG_CD_ACTIONBAR = (189 | BYTERECORDLENGTH);
	public static final short SIG_CD_ACTION	 = (190 | WORDRECORDLENGTH);

	public static final short SIG_CD_DOCAUTOLAUNCH = (191 | WORDRECORDLENGTH);
	public static final short SIG_CD_LAYOUTGRAPHIC = (192 | BYTERECORDLENGTH);
	public static final short SIG_CD_OLEOBJINFO = (193 | WORDRECORDLENGTH);
	public static final short SIG_CD_LAYOUTBUTTON = (194 | BYTERECORDLENGTH);
	public static final short SIG_CD_TEXTEFFECT = (195 | WORDRECORDLENGTH);

	/*	Saved Query records for items of type TYPE_QUERY */

	public static final short SIG_QUERY_HEADER = (129 | BYTERECORDLENGTH);
	public static final short SIG_QUERY_TEXTTERM = (130 | WORDRECORDLENGTH);
	public static final short SIG_QUERY_BYFIELD = (131 | WORDRECORDLENGTH);
	public static final short SIG_QUERY_BYDATE = (132 | WORDRECORDLENGTH);
	public static final short SIG_QUERY_BYAUTHOR = (133 | WORDRECORDLENGTH);
	public static final short SIG_QUERY_FORMULA = (134 | WORDRECORDLENGTH);
	public static final short SIG_QUERY_BYFORM = (135 | WORDRECORDLENGTH);
	public static final short SIG_QUERY_BYFOLDER = (136 | WORDRECORDLENGTH);
	public static final short SIG_QUERY_USESFORM = (137 | WORDRECORDLENGTH);
	public static final short SIG_QUERY_TOPIC	 = (138 | WORDRECORDLENGTH);
	/*	Save Action records for items of type TYPE_ACTION */

	public static final short SIG_ACTION_HEADER = (129 | BYTERECORDLENGTH);
	public static final short SIG_ACTION_MODIFYFIELD = (130 | WORDRECORDLENGTH);
	public static final short SIG_ACTION_REPLY = (131 | WORDRECORDLENGTH);
	public static final short SIG_ACTION_FORMULA = (132 | WORDRECORDLENGTH);
	public static final short SIG_ACTION_LOTUSSCRIPT = (133 | WORDRECORDLENGTH);
	public static final short SIG_ACTION_SENDMAIL = (134 | WORDRECORDLENGTH);
	public static final short SIG_ACTION_DBCOPY = (135 | WORDRECORDLENGTH);
	public static final short SIG_ACTION_DELETE = (136 | BYTERECORDLENGTH);
	public static final short SIG_ACTION_BYFORM = (137 | WORDRECORDLENGTH);
	public static final short SIG_ACTION_MARKREAD = (138 | BYTERECORDLENGTH);
	public static final short SIG_ACTION_MARKUNREAD = (139 | BYTERECORDLENGTH);
	public static final short SIG_ACTION_MOVETOFOLDER = (140 | WORDRECORDLENGTH);
	public static final short SIG_ACTION_COPYTOFOLDER = (141 | WORDRECORDLENGTH);
	public static final short SIG_ACTION_REMOVEFROMFOLDER = (142 | WORDRECORDLENGTH);
	public static final short SIG_ACTION_NEWSLETTER = (143 | WORDRECORDLENGTH);
	public static final short SIG_ACTION_RUNAGENT = (144 | WORDRECORDLENGTH);
	public static final short SIG_ACTION_SENDDOCUMENT = (145 | BYTERECORDLENGTH);
	public static final short SIG_ACTION_FORMULAONLY = (146 | WORDRECORDLENGTH);
	public static final short SIG_ACTION_JAVAAGENT = (147 | WORDRECORDLENGTH);
	public static final short SIG_ACTION_JAVA	 = (148 | WORDRECORDLENGTH);


	/* Signatures for items of type TYPE_VIEWMAP_DATASET */

	public static final short SIG_VIEWMAP_DATASET = (87 | WORDRECORDLENGTH);

	/* Signatures for items of type TYPE_VIEWMAP */

	public static final short SIG_CD_VMHEADER	 = (175 | BYTERECORDLENGTH);
	public static final short SIG_CD_VMBITMAP	 = (176 | BYTERECORDLENGTH);
	public static final short SIG_CD_VMRECT	 = (177 | BYTERECORDLENGTH);
	public static final short SIG_CD_VMPOLYGON_BYTE = (178 | BYTERECORDLENGTH);
	public static final short SIG_CD_VMPOLYLINE_BYTE = (179 | BYTERECORDLENGTH);
	public static final short SIG_CD_VMREGION	 = (180 | BYTERECORDLENGTH);
	public static final short SIG_CD_VMACTION	 = (181 | BYTERECORDLENGTH);
	public static final short SIG_CD_VMELLIPSE = (182 | BYTERECORDLENGTH);
	public static final short SIG_CD_VMRNDRECT = (184 | BYTERECORDLENGTH);
	public static final short SIG_CD_VMBUTTON	 = (185 | BYTERECORDLENGTH);
	public static final short SIG_CD_VMACTION_2 = (186 | WORDRECORDLENGTH);
	public static final short SIG_CD_VMTEXTBOX = (187 | WORDRECORDLENGTH);
	public static final short SIG_CD_VMPOLYGON  = (188 | WORDRECORDLENGTH);
	public static final short SIG_CD_VMPOLYLINE = (189 | WORDRECORDLENGTH);
	public static final short SIG_CD_VMPOLYRGN = (190 | WORDRECORDLENGTH);
	public static final short SIG_CD_VMCIRCLE	 = (191 | BYTERECORDLENGTH);
	public static final short SIG_CD_VMPOLYRGN_BYTE = (192 | BYTERECORDLENGTH);

	/* Signatures for alternate CD sequences*/
	public static final short SIG_CD_ALTERNATEBEGIN = (198 | WORDRECORDLENGTH);
	public static final short SIG_CD_ALTERNATEEND = (199 | BYTERECORDLENGTH);

	public static final short SIG_CD_OLERTMARKER = (200 | WORDRECORDLENGTH);
	
	public abstract void ODSReadMemory(long ppSrc, short type, long pDest, short iterations);
	public abstract void ODSWriteMemory(long ppDest, short type, long pSrc, short iterations);
	public abstract short ODSLength(short type);
	public abstract void EnumCompositeBuffer(BLOCKID itemBlockId, int itemValueLength, ActionRoutinePtr actionRoutinePtr) throws DominoException;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// viewfmt.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static final byte VIEW_FORMAT_VERSION = 1;
	
	public static final short VIEW_COLUMN_FORMAT_SIGNATURE	= (short)0x4356;
	public static final short VIEW_COLUMN_FORMAT_SIGNATURE2	= (short)0x4357;
	public static final short VIEW_COLUMN_FORMAT_SIGNATURE3	= (short)0x4358;
	public static final short VIEW_COLUMN_FORMAT_SIGNATURE4	= (short)0x4359;
	public static final short VIEW_COLUMN_FORMAT_SIGNATURE5	= (short)0x4360;
	public static final short VIEW_COLUMN_FORMAT_SIGNATURE6	= (short)0x4361; /* LI 3548.38, Vertical Layout */
	
	public static final byte VIEW_CLASS_TABLE = (byte)(0 << 4);
	public static final byte VIEW_CLASS_CALENDAR = (byte)(1 << 4);
	public static final byte VIEW_CLASS_MASK = (byte)0xF0;
	
	public static final byte CALENDAR_TYPE_DAY = (byte)0;
	public static final byte CALENDAR_TYPE_WEEK = (byte)1;
	public static final byte CALENDAR_TYPE_MONTH = (byte)2;
	
	public static final byte VIEW_STYLE_TABLE = VIEW_CLASS_TABLE;
	public static final byte VIEW_STYLE_DAY = (VIEW_CLASS_CALENDAR + 0);
	public static final byte VIEW_STYLE_WEEK = (VIEW_CLASS_CALENDAR + 1);
	public static final byte VIEW_STYLE_MONTH = (VIEW_CLASS_CALENDAR + 2);
	
	public static final short VIEW_TABLE_FLAG_COLLAPSED         = (short)0x0001;	/* Default to fully collapsed */
	public static final short VIEW_TABLE_FLAG_FLATINDEX         = (short)0x0002;	/* Do not index hierarchically */
																/* If FALSE, MUST have */
																/* NSFFormulaSummaryItem($REF) */
																/* as LAST item! */
	public static final short VIEW_TABLE_FLAG_DISP_ALLUNREAD    = (short)0x0004;	/* Display unread flags in margin at ALL levels */
	public static final short VIEW_TABLE_FLAG_CONFLICT          = (short)0x0008;	/* Display replication conflicts */
																/* If TRUE, MUST have */
																/* NSFFormulaSummaryItem($Conflict) */
																/* as SECOND-TO-LAST item! */
	public static final short VIEW_TABLE_FLAG_DISP_UNREADDOCS   = (short)0x0010;	/* Display unread flags in margin for documents only */
	public static final short VIEW_TABLE_GOTO_TOP_ON_OPEN       = (short)0x0020;	/* Position to top when view is opened. */
	public static final short VIEW_TABLE_GOTO_BOTTOM_ON_OPEN    = (short)0x0040;	/* Position to bottom when view is opened. */
	public static final short VIEW_TABLE_ALTERNATE_ROW_COLORING = (short)0x0080;	/* Color alternate rows. */
	public static final short VIEW_TABLE_HIDE_HEADINGS          = (short)0x0100;	/* Hide headings. */
	public static final short VIEW_TABLE_HIDE_LEFT_MARGIN       = (short)0x0200;	/* Hide left margin. */
	public static final short VIEW_TABLE_SIMPLE_HEADINGS        = (short)0x0400;	/* Show simple (background color) headings. */
	public static final short VIEW_TABLE_VARIABLE_LINE_COUNT    = (short)0x0800;	/* TRUE if LineCount is variable (can be reduced as needed). */
	
	public static final short VIEW_TABLE_GOTO_TOP_ON_REFRESH = (short)0x1000;	/* Position to top when view is refreshed. */
	public static final short VIEW_TABLE_GOTO_BOTTOM_ON_REFRESH = (short)0x2000;	/* Position to bottom when view is refreshed. */


	public static final short VIEW_TABLE_EXTEND_LAST_COLUMN = (short)0x4000;	/* TRUE if last column should be extended to fit the window width. */
	public static final short VIEW_TABLE_RTLVIEW = (short)0x8000;	/* TRUE if the View indexing should work from the Right most column */

	public static final short VIEW_TABLE_FLAT_HEADINGS = (short)0x0001;	/* TRUE if we should display no-borders at all on the header */
	public static final short VIEW_TABLE_COLORIZE_ICONS = (short)0x0002;	/* TRUE if the icons displayed inthe view should be colorized */
	public static final short VIEW_TABLE_HIDE_SB = (short)0x0004;	/* TRUE if we should not display a search bar for this view */
	public static final short VIEW_TABLE_HIDE_CAL_HEADER = (short)0x0008;	/* TRUE if we should hide the calendar header */
	public static final short VIEW_TABLE_NOT_CUSTOMIZED = (short)0x0010;	/* TRUE if view has not been customized (i.e. not saved by Designer) */
	public static final short VIEW_TABLE_SHOW_PARITAL_THREADS = (short)0x0020;	/* TRUE if view supports display of partial thread hierarchy (Hannover v8)*/
	public static final short VIEW_TABLE_FLAG_PARTIAL_FLATINDEX = (short)0x0020; /* LI 3548.57 show partial index hierarchically, if TRUE */

	public static final short VCF1_S_Sort = (short)0;		/* Add column to sort */
	public static final short VCF1_M_Sort = (short)0x0001;
	public static final short VCF1_S_SortCategorize = (short)1;		/* Make column a category */
	public static final short VCF1_M_SortCategorize = (short)0x0002;
	public static final short VCF1_S_SortDescending = (short)2;		/* Sort in descending order (ascending if FALSE) */
	public static final short VCF1_M_SortDescending = (short)0x0004;
	public static final short VCF1_S_Hidden = (short)3;		/* Hidden column */
	public static final short VCF1_M_Hidden = (short)0x0008;
	public static final short VCF1_S_Response = (short)4;		/* Response column */
	public static final short VCF1_M_Response = (short)0x0010;
	public static final short VCF1_S_HideDetail = (short)5;		/* Do not show detail on subtotalled columns */
	public static final short VCF1_M_HideDetail = (short)0x0020;
	public static final short VCF1_S_Icon = (short)6;		/* Display icon instead of text */
	public static final short VCF1_M_Icon = (short)0x0040;
	public static final short VCF1_S_NoResize = (short)7;		/* Resizable at run time. */
	public static final short VCF1_M_NoResize = (short)0x0080;
	public static final short VCF1_S_ResortAscending = (short)8;		/* Resortable in ascending order. */
	public static final short VCF1_M_ResortAscending = (short)0x0100;
	public static final short VCF1_S_ResortDescending = (short)9;		/* Resortable in descending order. */
	public static final short VCF1_M_ResortDescending = (short)0x0200;
	public static final short VCF1_S_Twistie = (short)10;		/* Show twistie if expandable. */
	public static final short VCF1_M_Twistie = (short)0x0400;
	public static final short VCF1_S_ResortToView = (short)11;		/* Resort to a view. */
	public static final short VCF1_M_ResortToView = (short)0x0800;
	public static final short VCF1_S_SecondResort = (short)12;		/* Secondary resort column set. */
	public static final short VCF1_M_SecondResort = (short)0x1000;
	public static final short VCF1_S_SecondResortDescending = (short)13;		/* Secondary column resort descending (ascending if clear). */
	public static final short VCF1_M_SecondResortDescending = (short)0x2000;
								/* The following 4 constants are obsolete - see new VCF3_ constants below. */
	public static final short VCF1_S_CaseInsensitiveSort = (short)14;		/* Case insensitive sorting. */
	public static final short VCF1_M_CaseInsensitiveSort = (short)0x4000;
	public static final short VCF1_S_AccentInsensitiveSort = (short)15;		/* Accent insensitive sorting. */
	public static final short VCF1_M_AccentInsensitiveSort = (short)0x8000;
	
	public static final short VCF2_S_DisplayAlignment = (short)0;		/* Display alignment - VIEW_COL_ALIGN_XXX */
	public static final short VCF2_M_DisplayAlignment = (short)0x0003;
	public static final short VCF2_S_SubtotalCode = (short)2;		/* Subtotal code (NIF_STAT_xxx) */
	public static final short VCF2_M_SubtotalCode = (short)0x003c;
	public static final short VCF2_S_HeaderAlignment = (short)6;		/* Header alignment - VIEW_COL_ALIGN_XXX */
	public static final short VCF2_M_HeaderAlignment = (short)0x00c0;
	public static final short VCF2_S_SortPermute = (short)8;		/* Make column permuted if multi-valued */
	public static final short VCF2_M_SortPermute = (short)0x0100;
	public static final short VCF2_S_SecondResortUniqueSort = (short)9;		/* Secondary resort column props different from column def.*/
	public static final short VCF2_M_SecondResortUniqueSort = (short)0x0200;
	public static final short VCF2_S_SecondResortCategorized = (short)10;		/* Secondary resort column categorized. */
	public static final short VCF2_M_SecondResortCategorized = (short)0x0400;
	public static final short VCF2_S_SecondResortPermute = (short)11;		/* Secondary resort column permuted. */
	public static final short VCF2_M_SecondResortPermute = (short)0x0800;
	public static final short VCF2_S_SecondResortPermutePair = (short)12;		/* Secondary resort column pairwise permuted. */
	public static final short VCF2_M_SecondResortPermutePair = (short)0x1000;
	public static final short VCF2_S_ShowValuesAsLinks = (short)13;		/* Show values as links when viewed by web browsers. */
	public static final short VCF2_M_ShowValuesAsLinks = (short)0x2000;
	public static final short VCF2_S_DisplayReadingOrder = (short)14;		/* Display Reading order - VIEW_COL_XXX */
	public static final short VCF2_M_DisplayReadingOrder = (short)0x4000;
	public static final short VCF2_S_HeaderReadingOrder = (short)15;		/* Header Reading order - VIEW_COL_XXX */
	public static final short VCF2_M_HeaderReadingOrder = (short)0x8000;
	
	public static final short VCF3_S_FlatInV5 					= (short)0;		/* View is flat in V5 or greater */
	public static final short VCF3_M_FlatInV5 					= (short)0x0001;
	public static final short VCF3_S_CaseSensitiveSortInV5		= (short)1;		/* Case Sensitive sorting. */
	public static final short VCF3_M_CaseSensitiveSortInV5		= (short)0x0002;
	public static final short VCF3_S_AccentSensitiveSortInV5	= (short)2;		/* Accent Sensitive sorting. */
	public static final short VCF3_M_AccentSensitiveSortInV5	= (short)0x0004;
	public static final short VCF3_S_HideWhenFormula			= (short)3;		/* Column has hide/when formula set */
	public static final short VCF3_M_HideWhenFormula			= (short)0x0008;
	public static final short VCF3_S_TwistieResource			= (short)4;
	public static final short VCF3_M_TwistieResource			= (short)0x0010;
	public static final short VCF3_S_Color						= (short)5;		/* Column value to be used as a color for this entry. */
	public static final short VCF3_M_Color						= (short)0x0020;
												/* 6 */
	public static final short VCF3_ExtDate						= (short)0x0040; /*column has extended date info*/
												/* 7 */
	public static final short VCF3_NumberFormat					= (short)0x0080; /*column has extended number format*/
	public static final short VCF3_S_IsColumnEditable			= (short)8;		/* v6 - can this column be edited */
	public static final short VCF3_M_IsColumnEditable			= (short)0x0100	;/* V6 - color col and user definable color */
	public static final short VCF3_S_UserDefinableColor			= (short)9;
	public static final short VCF3_M_UserDefinableColor			= (short)0x0200;
	public static final short VCF3_S_HideInR5					= (short)10;		/* Column has hide/when formula set and needs to be hidden in R5 or before*/
	public static final short VCF3_M_HideInR5					= (short)0x0400;
	public static final short VCF3_S_NamesFormat				= (short)11;		/* Column has extended names format */
	public static final short VCF3_M_NamesFormat				= (short)0x0800;
	public static final short VCF3_S_HideColumnTitle			= (short)12;		/* Hide column title from display, but not from customization */
	public static final short VCF3_M_HideColumnTitle			= (short)0x1000;
	public static final short VCF3_S_IsSharedColumn				= (short)13;
	public static final short VCF3_M_IsSharedColumn				= (short)0x2000; /* Is this a shared column? */
	public static final short VCF3_S_UseSharedColumnFormulaOnly	= (short)14;
	public static final short VCF3_M_UseSharedColumnFormulaOnly	= (short)0x4000; /* Use only the formula from shared column - let use modify everything else */
	public static final short VCF3_S_ExtendedColFmt6			= (short)15; /* LI 3548.38, Vertical. Column has extended names format */
	public static final short VCF3_M_ExtendedViewColFmt6		= (short)0x8000;
	
	/* View column display alignment.  */
	/*		Note: order and values are assumed in VIEW_ALIGN_XXX_ID's. */

	public static final short VIEW_COL_ALIGN_LEFT = 0;				/* Left justified */
	public static final short VIEW_COL_ALIGN_RIGHT = 1;				/* Right justified */
	public static final short VIEW_COL_ALIGN_CENTER = 2;				/* Centered */

	/* View column display reading order.  */

	public static final short VIEW_COL_LTR = 0;						/* Left To Right reading order */
	public static final short VIEW_COL_RTL = 1;						/* Right To Left reading order */

	/* Simple format data types, used to initialize dialog box to last "mode". */

	public static final short VIEW_COL_NUMBER = 0;
	public static final short VIEW_COL_TIMEDATE = 1;
	public static final short VIEW_COL_TEXT = 2;
	
	public static final short VALID_VIEW_FORMAT_SIG = 0x2BAD;
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// fontid.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static final int FONT_FACE_ROMAN = 0;
	public static final int FONT_FACE_SWISS = 1;
	public static final int FONT_FACE_UNICODE = 2;
	public static final int FONT_FACE_USERINTERFACE = 3;
	public static final int FONT_FACE_TYPEWRITER = 4;
	public static final int STATIC_FONT_FACES = 5;
	
	public static final int MAXFACEIZE = 32;
	
	public static final int ISBOLD = 0x01;
	public static final int ISITALIC = 0x02;
	public static final int ISUNDERLINE = 0x04;
	public static final int ISSTRIKEOUT = 0x08;
	public static final int ISSUPER = 0x10;
	public static final int ISSUB = 0x20;
	public static final int ISEFFECT = 0x80;
	public static final int ISSHADOW = 0x80;
	public static final int ISEMBOSS = 0x90;
	public static final int ISEXTRUDE = 0xa0;
	
	public static final int FONT_SIZE_SHIFT = 24;
	public static final int FONT_COLOR_SHIFT = 16;
	public static final int FONT_STYLE_SHIFT = 8;
	public static final int FONT_FACE_SHIFT = 0;
	
	public static final int FONT_SHADOW_VALUE = 14;
	
	public static final int NULLFONTID = 0;
	public static final int DEFAULT_SMALL_FONT_ID = FontSetFaceID(FontSetSize(NULLFONTID, (byte)9), (byte)FONT_FACE_SWISS);
	public static final int DEFAULT_FONT_ID = FontSetFaceID(FontSetSize(NULLFONTID, (byte)10), (byte)FONT_FACE_SWISS);
	public static final int DEFAULT_BOLD_FONT_ID = FontSetBold(DEFAULT_FONT_ID);
	public static final int FOREIGN_FONT_ID = FontSetFaceID(FontSetSize(NULLFONTID, (byte)10), (byte)FONT_FACE_TYPEWRITER);
	public static final int UNDERLINE_FONT_ID = FontSetUnderline(DEFAULT_SMALL_FONT_ID);
	
	public static int BYTEMASK(int leftshift) { return 0x000000ff << leftshift; }
	public static byte FontGetSize(int fontid) { return (byte)((fontid >> FONT_SIZE_SHIFT) & 0xff); }
	public static int FontSetSize(int fontid, byte size) { return (fontid & ~BYTEMASK(FONT_SIZE_SHIFT)) | (size << FONT_SIZE_SHIFT); }
	public static byte FontGetColor(int fontid) { return (byte)((fontid >> FONT_COLOR_SHIFT) & 0xff); }
	public static int FontSetColor(int fontid, byte color) { return (fontid & ~BYTEMASK(FONT_COLOR_SHIFT)) | (color << FONT_COLOR_SHIFT); }
	public static byte FontGetStyle(int fontid) { return (byte)((fontid >> FONT_STYLE_SHIFT) & 0xff); }
	public static int FontSetStyle(int fontid, byte style) { return (fontid & ~BYTEMASK(FONT_STYLE_SHIFT)) | (style << FONT_STYLE_SHIFT); }
	public static byte FontGetFaceID(int fontid) { return (byte)((fontid >> FONT_FACE_SHIFT) & 0xff); }
	public static int FontSetFaceID(int fontid, byte faceId) { return (fontid & ~BYTEMASK(FONT_FACE_SHIFT)) | (faceId << FONT_FACE_SHIFT); }
	
	public static boolean FontIsUnderline(int fontid) { return (fontid & (ISUNDERLINE << FONT_STYLE_SHIFT)) != 0; }
	public static boolean FontIsItalic(int fontid) { return (fontid & (ISITALIC << FONT_STYLE_SHIFT)) != 0; }
	public static boolean FontIsBold(int fontid) { return (fontid & (ISBOLD << FONT_STYLE_SHIFT)) != 0; }
	
	public static int FontSetItalic(int fontid) { return fontid | (ISITALIC << FONT_STYLE_SHIFT); }
	public static int FontClearItalic(int fontid) { return fontid & ~(ISITALIC << FONT_STYLE_SHIFT); };
	
	public static int FontSetBold(int fontid) { return fontid | (ISBOLD << FONT_STYLE_SHIFT); }
	public static int FontClearBold(int fontid) { return fontid & ~(ISBOLD << FONT_STYLE_SHIFT); };
	
	/** @since Notes/Domino 4.0 */
	public static int FontSetUnderline(int fontid) { return fontid | (ISUNDERLINE << FONT_STYLE_SHIFT); }
	/** @since Notes/Domino 4.0 */
	public static int FontClearUnderline(int fontid) { return fontid & ~(ISUNDERLINE << FONT_STYLE_SHIFT); };
	
	/** @since Notes/Domino 4.0 */
	public static int FontSetStrikeOut(int fontid) { return fontid | (ISSTRIKEOUT << FONT_STYLE_SHIFT); }
	/** @since Notes/Domino 4.0 */
	public static int FontClearStrikeOut(int fontid) { return fontid & ~(ISSTRIKEOUT << FONT_STYLE_SHIFT); };
	
	/** @since Notes/Domino 4.0 */
	public static int FontSetSuperScript(int fontid) { return fontid | (ISSUPER << FONT_STYLE_SHIFT); }
	/** @since Notes/Domino 4.0 */
	public static int FontClearSuperScript(int fontid) { return fontid & ~(ISSUPER << FONT_STYLE_SHIFT); };
	
	/** @since Notes/Domino 4.0 */
	public static int FontSetSubScript(int fontid) { return fontid | (ISSUB << FONT_STYLE_SHIFT); }
	/** @since Notes/Domino 4.0 */
	public static int FontClearSubScript(int fontid) { return fontid & ~(ISSUB << FONT_STYLE_SHIFT); };
	
	public static boolean FontIsStrikeOut(int fontid) { return (fontid & (ISSTRIKEOUT << FONT_STYLE_SHIFT)) != 0; }
	public static boolean FontIsSuperScript(int fontid) { return (fontid & ((ISEFFECT|ISSUPER) << FONT_STYLE_SHIFT)) == (ISSUPER << FONT_STYLE_SHIFT); }
	public static boolean FontIsSubScript(int fontid) { return (fontid & ((ISEFFECT|ISSUB) << FONT_STYLE_SHIFT)) == (ISSUB << FONT_STYLE_SHIFT); }
	public static boolean FontIsPlain(int fontid) { return FontGetStyle(fontid) == 0; }
	/** @since Notes/Domino 4.0 */
	public static int FontSetPlain(int fontid) { return FontSetStyle(fontid, (byte)0); }
	
	/** @since Notes/Domino 4.5 */
	public static boolean FontIsEffect(int fontid) { return (fontid & (ISEFFECT << FONT_STYLE_SHIFT)) != 0; }
	public static boolean FontIsShadow(int fontid) { return (fontid & ((ISSHADOW|ISEMBOSS|ISEXTRUDE) << FONT_STYLE_SHIFT)) == (ISSHADOW << FONT_STYLE_SHIFT); }
	
	/** @since Notes/Domino 4.5 */
	public static boolean FontIsEmboss(int fontid) { return (fontid & (ISEMBOSS << FONT_STYLE_SHIFT)) == (ISEMBOSS << FONT_STYLE_SHIFT); }
	/** @since Notes/Domino 4.5 */
	public static boolean FontIsExtrude(int fontid) { return (fontid & (ISEXTRUDE << FONT_STYLE_SHIFT)) == (ISEXTRUDE << FONT_STYLE_SHIFT); }
	
	/** @since Notes/Domino 4.5 */
	public static int FontSetShadow(int fontid) { return fontid | (ISSHADOW << FONT_STYLE_SHIFT); }
	/** @since Notes/Domino 4.5 */
	public static int FontClearShadow(int fontid) { return fontid & ~(ISSHADOW << FONT_STYLE_SHIFT); }
	/** @since Notes/Domino 4.5 */
	public static int FontSetEmboss(int fontid) { return fontid | (ISEMBOSS << FONT_STYLE_SHIFT); }
	/** @since Notes/Domino 4.5 */
	public static int FontClearEmboss(int fontid) { return fontid & ~(ISEMBOSS << FONT_STYLE_SHIFT); }
	/** @since Notes/Domino 4.5 */
	public static int FontSetExtrude(int fontid) { return fontid | (ISEXTRUDE << FONT_STYLE_SHIFT); }
	/** @since Notes/Domino 4.5 */
	public static int FontClearExtrude(int fontid) { return fontid & ~(ISEXTRUDE << FONT_STYLE_SHIFT); }
	/** @since Notes/Domino 4.5 */
	public static int FontClearEffect(int fontid) { return fontid & ~((ISEMBOSS|ISEXTRUDE) << FONT_STYLE_SHIFT); }
	/** @since Notes/Domino 4.5 */
	public static byte FontGetShadowOffset(int fontid) { return (byte)(FontGetSize(fontid)/(FONT_SHADOW_VALUE) + 1); }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nif.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static final int MAXTUMBLERLEVELS_V2 = 8;
	public static final int MAXTUMBLERLEVELS = 32;
	
	/*	Flag in index entry's NOTEID to indicate (ghost) "category entry" */
	/*	Note: this relies upon the fact that NOTEID_RESERVED is high bit! */

	public static final int NOTEID_GHOST_ENTRY = 0x40000000; /* Bit 30 -> partial thread ghost collection entry */
	public static final int NOTEID_CATEGORY = 0x80000000; /* Bit 31 -> (ghost) "category entry" */
	public static final int NOTEID_CATEGORY_TOTAL = 0xC0000000; /* Bit 31+30 -> (ghost) "grand total entry" */
	public static final int NOTEID_CATEGORY_INDENT = 0x3F000000;	/* Bits 24-29 -> category indent level within this column */
	public static final int NOTEID_CATEGORY_ID = 0x00FFFFFF; /* Low 24 bits are unique category # */


	/*	SignalFlags word returned by NIFReadEntries and V4+ NIFFindByKey */

	public static final short SIGNAL_DEFN_ITEM_MODIFIED = 0x0001;
										/* At least one of the "definition" */
										/* view items ($FORMULA, $COLLATION, */
										/* or $FORMULACLASS) has been modified */
										/* by another user since last ReadEntries. */
										/* Upon receipt, you may wish to */
										/* re-read the view note if up-to-date */
										/* copies of these items are needed. */
										/* Upon receipt, you may also wish to */
										/* re-synchronize your index position */
										/* and re-read the rebuilt index. */
										/* Signal returned only ONCE per detection */
	public static final short SIGNAL_VIEW_ITEM_MODIFIED = 0x0002;
										/* At least one of the non-"definition" */
										/* view items ($TITLE,etc) has been */
										/* modified since last ReadEntries. */
										/* Upon receipt, you may wish to */
										/* re-read the view note if up-to-date */
										/* copies of these items are needed. */
										/* Signal returned only ONCE per detection */
	public static final short SIGNAL_INDEX_MODIFIED = 0x0004;
										/* Collection index has been modified */
										/* by another user since last ReadEntries. */
										/* Upon receipt, you may wish to */
										/* re-synchronize your index position */
										/* and re-read the modified index. */
										/* Signal returned only ONCE per detection */
	public static final short SIGNAL_UNREADLIST_MODIFIED = 0x0008;
										/* Unread list has been modified */
										/* by another window using the same */
										/* hCollection context */
										/* Upon receipt, you may wish to */
										/* repaint the window if the window */
										/* contains the state of unread flags */
										/* (This signal is never generated */
										/*  by NIF - only unread list users) */
	public static final short SIGNAL_DATABASE_MODIFIED = 0x0010;
										/* Collection is not up to date */
	public static final short SIGNAL_MORE_TO_DO = 0x0020;
										/* End of collection has not been reached */
										/* due to buffer being too full. */
										/* The ReadEntries should be repeated */
										/* to continue reading the desired entries. */
	public static final short SIGNAL_VIEW_TIME_RELATIVE = 0x0040;
										/* The view contains a time-relative formula */
										/* (e.g., @Now).  Use this flag to tell if the */
										/* collection will EVER be up-to-date since */
										/* time-relative views, by definition, are NEVER */
										/* up-to-date. */
	public static final short SIGNAL_NOT_SUPPORTED = 0x0080;
										/* Returned if signal flags are not supported */
										/* This is used by NIFFindByKeyExtended when it */
										/* is talking to a pre-V4 server that does not */
										/* support signal flags for FindByKey */

	/*	Mask that defines all "sharing conflicts", which are cases when
		the database or collection has changed out from under the user. */

	public static final short SIGNAL_ANY_CONFLICT =(SIGNAL_DEFN_ITEM_MODIFIED | 
								SIGNAL_VIEW_ITEM_MODIFIED | 
								SIGNAL_INDEX_MODIFIED | 
								SIGNAL_UNREADLIST_MODIFIED | 
								SIGNAL_DATABASE_MODIFIED);

	/*	Mask that defines all "sharing conflicts" except for SIGNAL_DATABASE_MODIFIED.
		This can be used in combination with SIGNAL_VIEW_TIME_RELATIVE to tell if
		the database or collection has truly changed out from under the user or if the
		view is a time-relative view which will NEVER be up-to-date.  SIGNAL_DATABASE_MODIFIED
		is always returned for a time-relative view to indicate that it is never up-to-date. */

	public static final short SIGNAL_ANY_NONDATA_CONFLICT = (SIGNAL_DEFN_ITEM_MODIFIED | 
										SIGNAL_VIEW_ITEM_MODIFIED | 
										SIGNAL_INDEX_MODIFIED | 
										SIGNAL_UNREADLIST_MODIFIED);
	
	/*	NIFFindByKey "find" flags */

	public static final short FIND_PARTIAL = 0x0001;	/* Match only initial characters */
	 								/* ("T" matches "Tim") */
	public static final short FIND_CASE_INSENSITIVE = 0x0002;	/* Case insensitive */
	 								/* ("tim" matches "Tim") */
	public static final short FIND_RETURN_DWORD = 0x0004;	/* Input/Output is DWORD COLLECTIONPOSITION */
	public static final short FIND_ACCENT_INSENSITIVE = 0x0008;	/* Accent insensitive (ignore diacritical marks */
	public static final short FIND_UPDATE_IF_NOT_FOUND = 0x0020; /* If key is not found, update collection */
	 								   /* and search again */
	 
	                      /* At most one of the following four flags should be specified */
	public static final short FIND_LESS_THAN = 0x0040;	/* Find last entry less than the key value */
	public static final short FIND_FIRST_EQUAL = 0x0000;	/* Find first entry equal to the key value (if more than one) */
	public static final short FIND_LAST_EQUAL = 0x0080;	/* Find last entry equal to the key value (if more than one) */
	public static final short FIND_GREATER_THAN = 0x00C0;	/* Find first entry greater than the key value */
	public static final short FIND_EQUAL = 0x0800;	/* Qualifies LESS_THAN and GREATER_THAN to mean */
	 								/* LESS_THAN_OR_EQUAL and GREATER_THAN_OR_EQUAL */
	public static final short FIND_COMPARE_MASK = 0x08C0;	/* Bitmask of the comparison flags defined above */
	 
	public static final short FIND_RANGE_OVERLAP = 0x0100;	/* Overlapping ranges match, and values within a range match */
	public static final short FIND_RETURN_ANY_NON_CATEGORY_MATCH = 0x0200;
	 									/* Return First Match at bottom level of
	 									Categorized view (Doesn't have
										to be first of duplicates */
	public static final short FIND_NONCATEGORY_ONLY = 0x0400; /* Only match non-category entries */
	public static final short FIND_AND_READ_MATCHES = 0x2000;	/* Read and buffer matches NIFFindByKeyExtended2 only */
	
	public static final int FIND_DESIGN_NOTE_PARTIAL = 1;
	
	public static final short NAVIGATE_CURRENT = 0;	/* Remain at current position */
		/* (reset position & return data) */
	public static final short NAVIGATE_PARENT = 3;	/* Up 1 level */
	public static final short NAVIGATE_CHILD = (short)4;	/* Down 1 level to first child */
	public static final short NAVIGATE_NEXT_PEER = (short)5;	/* Next node at our level */
	public static final short NAVIGATE_PREV_PEER = (short)6;	/* Prev node at our level */
	public static final short NAVIGATE_FIRST_PEER = (short)7;	/* First node at our level */
	public static final short NAVIGATE_LAST_PEER = (short)8;	/* Last node at our level */
	public static final short NAVIGATE_CURRENT_MAIN = (short)11;	/* Highest level non-category entry */
	public static final short NAVIGATE_NEXT_MAIN = (short)12;	/* CURRENT_MAIN, then NEXT_PEER */
	public static final short NAVIGATE_PREV_MAIN = (short)13;	/* CURRENT_MAIN, then PREV_PEER only if already there */
	public static final short NAVIGATE_NEXT_PARENT = (short)19;	/* PARENT, then NEXT_PEER */
	public static final short NAVIGATE_PREV_PARENT = (short)20;	/* PARENT, then PREV_PEER */
	
	public static final short NAVIGATE_NEXT = (short)1;	/* Next entry over entire tree */
			/* (parent first, then children,...) */
	public static final short NAVIGATE_PREV = (short)9;	/* Previous entry over entire tree */
			/* (opposite order of PREORDER) */
	public static final short NAVIGATE_ALL_DESCENDANTS = (short)17;	/* NEXT, but only descendants */
			/* below NIFReadEntries StartPos */
	public static final short NAVIGATE_NEXT_UNREAD = (short)10;	/* NEXT, but only "unread" entries */
	public static final short NAVIGATE_NEXT_UNREAD_MAIN = (short)18;	/* NEXT_UNREAD, but stop at main note also */
	public static final short NAVIGATE_PREV_UNREAD_MAIN = (short)34;	/* Previous unread main. */
	public static final short NAVIGATE_PREV_UNREAD = (short)21;	/* PREV, but only "unread" entries */
	public static final short NAVIGATE_NEXT_SELECTED = (short)14;	/* NEXT, but only "selected" entries */
	public static final short NAVIGATE_PREV_SELECTED = (short)22;	/* PREV, but only "selected" entries */
	public static final short NAVIGATE_NEXT_SELECTED_MAIN = (short)32;	/* Next selected main. (Next unread */
			/* main can be found above.) */
	public static final short NAVIGATE_PREV_SELECTED_MAIN = (short)33;	/* Previous selected main. */
	public static final short NAVIGATE_NEXT_EXPANDED = (short)15;	/* NEXT, but only "expanded" entries */
	public static final short NAVIGATE_PREV_EXPANDED = (short)16;	/* PREV, but only "expanded" entries */
	public static final short NAVIGATE_NEXT_EXPANDED_UNREAD = (short)23; /* NEXT, but only "expanded" AND "unread" entries */
	public static final short NAVIGATE_PREV_EXPANDED_UNREAD = (short)24; /* PREV, but only "expanded" AND "unread" entries */
	public static final short NAVIGATE_NEXT_EXPANDED_SELECTED = (short)25; /* NEXT, but only "expanded" AND "selected" entries */
	public static final short NAVIGATE_PREV_EXPANDED_SELECTED = (short)26; /* PREV, but only "expanded" AND "selected" entries */
	public static final short NAVIGATE_NEXT_EXPANDED_CATEGORY = (short)27; /* NEXT, but only "expanded" AND "category" entries */
	public static final short NAVIGATE_PREV_EXPANDED_CATEGORY = (short)28; /* PREV, but only "expanded" AND "category" entries */
	public static final short NAVIGATE_NEXT_EXP_NONCATEGORY = (short)39;	/* NEXT, but only "expanded" "non-category" entries */
	public static final short NAVIGATE_PREV_EXP_NONCATEGORY = (short)40;	/* PREV, but only "expanded" "non-category" entries */
	public static final short NAVIGATE_NEXT_HIT = (short)29;	/* NEXT, but only FTSearch "hit" entries */
			/* (in the SAME ORDER as the hit's relevance ranking) */
	public static final short NAVIGATE_PREV_HIT = (short)30;	/* PREV, but only FTSearch "hit" entries */
			/* (in the SAME ORDER as the hit's relevance ranking) */
	public static final short NAVIGATE_CURRENT_HIT = (short)31;	/* Remain at current position in hit's relevance rank array */
			/* (in the order of the hit's relevance ranking) */
	public static final short NAVIGATE_NEXT_SELECTED_HIT = (short)35;	/* NEXT, but only "selected" and FTSearch "hit" entries */
			/* (in the SAME ORDER as the hit's relevance ranking) */
	public static final short NAVIGATE_PREV_SELECTED_HIT = (short)36;	/* PREV, but only "selected" and FTSearch "hit" entries */
			/* (in the SAME ORDER as the hit's relevance ranking) */
	public static final short NAVIGATE_NEXT_UNREAD_HIT = (short)37;	/* NEXT, but only "unread" and FTSearch "hit" entries */
			/* (in the SAME ORDER as the hit's relevance ranking) */
	public static final short NAVIGATE_PREV_UNREAD_HIT = (short)38;	/* PREV, but only "unread" and FTSearch "hit" entries */
			/* (in the SAME ORDER as the hit's relevance ranking) */
	public static final short NAVIGATE_NEXT_CATEGORY = (short)41;	/* NEXT, but only "category" entries */
	public static final short NAVIGATE_PREV_CATEGORY = (short)42;	/* PREV, but only "category" entries */
	public static final short NAVIGATE_NEXT_NONCATEGORY = (short)43;	/* NEXT, but only "non-category" entries */
	public static final short NAVIGATE_PREV_NONCATEGORY = (short)44;	/* PREV, but only "non-category" entries */
	
	public static final short NAVIGATE_MASK = (short)0x007F;	/* Navigator code (see above) */
	

	public static final short NAVIGATE_MINLEVEL = 0x0100;	/* Honor "Minlevel" field in position */
	public static final short NAVIGATE_MAXLEVEL = 0x0200;	/* Honor "Maxlevel" field in position */
	
	public static final short NAVIGATE_CONTINUE = (short)0x8000;
	
	/*	Fixed length stuff */
	public static final int READ_MASK_NOTEID = (int)0x00000001L;	/* NOTEID of entry */
	public static final int READ_MASK_NOTEUNID = (int)0x00000002L;	/* UNID of entry */
	public static final int READ_MASK_NOTECLASS = (int)0x00000004L;	/* WORD of "note class" */
	public static final int READ_MASK_INDEXSIBLINGS = (int)0x00000008L;	/* DWORD/WORD of # siblings of entry */
	public static final int READ_MASK_INDEXCHILDREN = (int)0x00000010L;	/* DWORD/WORD of # direct children of entry */
	public static final int READ_MASK_INDEXDESCENDANTS = (int)0x00000020L;	/* DWORD/WORD of # descendants below entry */
	public static final int READ_MASK_INDEXANYUNREAD = (int)0x00000040L;	/* WORD of TRUE if "unread" or */
												/* "unread" descendants; else FALSE */
	public static final int READ_MASK_INDENTLEVELS = (int)0x00000080L;	/* WORD of # levels that this */
												/* entry should be indented in */
												/* a formatted view. */
		 										/* For category entries: */
												/*  # sub-levels that this */
												/*  category entry is within its */
												/*  Collation Descriptor.  Used */
												/*  for multiple-level category */
												/*  columns (backslash-delimited). */
												/*  "0" for 1st level in this column, etc. */
												/* For response entries: */
												/*  # levels that this response */
												/*  is below the "main note" level. */
												/* For normal entries: 0 */
	public static final int READ_MASK_SCORE = (int)0x00000200L;	/* Relavence "score" of entry */
												/*  (only used with FTSearch). */
	public static final int READ_MASK_INDEXUNREAD = (int)0x00000400L; /* WORD of TRUE if this entry (only) "unread" */
	
	
		/*	Stuff returned only once at beginning of return buffer */
	public static final int READ_MASK_COLLECTIONSTATS = (int)0x00000100L;	/* Collection statistics (COLLECTIONSTATS/COLLECTIONSTATS16) */
	
	
		/*	Variable length stuff */
	public static final int READ_MASK_INDEXPOSITION = (int)0x00004000L;	/* Truncated COLLECTIONPOSITION/COLLECTIONPOSITION16 */
	public static final int READ_MASK_SUMMARYVALUES = (int)0x00002000L;	/* Summary buffer w/o item names */
	public static final int READ_MASK_SUMMARY = (int)0x00008000L;	/* Summary buffer with item names */
	/*	Structures which are used by NIFGetCollectionData to return data
		about the collection. NOTE: If the COLLECTIONDATA structure changes,
		nifods.c must change as well. */
	
	public static final short OPEN_REBUILD_INDEX = 0x0001;	/* Throw away existing index and */
															/* rebuild it from scratch */
	public static final short OPEN_NOUPDATE = 0x0002;	/* Do not update index or unread */
														/* list as part of open (usually */
														/* set by server when it does it */
														/* incrementally instead). */
	public static final short OPEN_DO_NOT_CREATE = 0x0004;	/* If collection object has not yet */
															/* been created, do NOT create it */
															/* automatically, but instead return */
															/* a special internal error called */
															/* ERR_COLLECTION_NOT_CREATED */
	public static final short OPEN_SHARED_VIEW_NOTE = 0x0010;	/* Tells NIF to "own" the view note */
																/* (which gets read while opening the */
																/* collection) in memory, rather than */
																/* the caller "owning" the view note */
																/* by default.  If this flag is specified */
																/* on subsequent opens, and NIF currently */
																/* owns a copy of the view note, it */
																/* will just pass back the view note */
																/* handle  rather than re-reading it */
																/* from disk/network.  If specified, */
																/* the the caller does NOT have to */
																/* close the handle.  If not specified, */
																/* the caller gets a separate copy, */
																/* and has to NSFNoteClose the */
																/* handle when its done with it. */
	public static final short OPEN_REOPEN_COLLECTION = 0x0020;	/* Force re-open of collection and */
																/* thus, re-read of view note. */
																/* Also implicitly prevents sharing */
																/* of collection handle, and thus */
																/* prevents any sharing of associated */
																/* structures such as unread lists, etc */
	
	public static final int PERCENTILE_COUNT = 11;
	
	public abstract int NIFFindView(long hDb, String name) throws DominoException;
	/** @since Notes/Domino 5.0 */
	public abstract int NIFFindDesignNoteExt(long hDb, String name, short noteClass, String flagsPattern, int options) throws DominoException;
	/**
	 * Returns a handle to the collection (normally the rethCollection parameter)
	 */
	public abstract short NIFOpenCollection(long hViewDB, long hDataDB, int viewNoteID, short openFlags, long hUnreadList, long rethViewNote, UNIVERSALNOTEID viewUNID, long rethCollapsedList, long rethSelectedList) throws DominoException;
	/** @since Notes/Domino 6.0.2 */
	public abstract short NIFOpenCollectionWithUserNameList(long hViewDB, long hDataDB, int viewNoteID, short openFlags, long hUnreadList, long rethViewNote, UNIVERSALNOTEID viewUNID, long rethCollapsedList, long rethSelectedList, long nameList) throws DominoException;
	public abstract void NIFCloseCollection(short hCollection) throws DominoException;
	public abstract void NIFFindByKey(short hCollection, ITEM_TABLE key, short findFlags, COLLECTIONPOSITION retIndexPos, IntRef retNumMatches) throws DominoException;
	public abstract void NIFFindByKeyExtended2(short hCollection, ITEM_TABLE key, int findFlags, int returnFlags, COLLECTIONPOSITION retIndexPos, IntRef retNumMatches, IntRef retSignalFlags, IntRef rethBuffer, IntRef retSequence) throws DominoException;
	
	/**
	 * Returns a handle to the data buffer (normally the rethBuffer parameter)
	 */
	public abstract long NIFReadEntries(short hCollection, COLLECTIONPOSITION indexPos, short skipNavigator, int skipCount, short returnNavigator, int returnCount, int returnMask, ShortRef retBufferLength, IntRef retNumEntriesSkipped, IntRef retNumEntriesReturned, ShortRef retSignalFlags) throws DominoException;
	/** @since Notes/Domino 4.0 */
	public abstract long NIFGetCollectionData(short hCollection) throws DominoException;
	public abstract void NIFUpdateCollection(short hCollection) throws DominoException;
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nifcoll.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static final byte CDF_S_descending = (byte)0;		/* True if descending */
	public static final byte CDF_M_descending = (byte)0x01;	/* False if ascending order (default) */
	public static final byte CDF_M_caseinsensitive = (byte)0x02;	/* Obsolete - see new constant below */
	public static final byte CDF_M_ignoreprefixes = (byte)0x02;	/* If prefix list, then ignore for sorting */
	public static final byte CDF_M_accentinsensitive = (byte)0x04;	/* Obsolete - see new constant below */
	public static final byte CDF_M_permuted = (byte)0x08;	/* If set, lists are permuted */
	public static final byte CDF_M_permuted_pairwise = (byte)0x10;	/* Qualifier if lists are permuted; if set, lists
											   					are pairwise permuted, otherwise lists are
											   					multiply permuted. */
	public static final byte CDF_M_flat_in_v5 = (byte)0x20;	/* If set, treat as permuted */
	public static final byte CDF_M_casesensitive_in_v5 = (byte)0x40;	/* If set, text compares are case-sensitive */
	public static final byte CDF_M_accentsensitive_in_v5 = (byte)0x80;	/* If set, text compares are accent-sensitive */
	
	public static final byte COLLATE_DESCRIPTOR_SIGNATURE = (byte)0x66;
	public static final byte COLLATION_SIGNATURE = 0x44;
	
	public static final byte COLLATE_TYPE_KEY = 0;
	public static final byte COLLATE_TYPE_NOTEID = 3;
	public static final byte COLLATE_TYPE_TUMBLER = 6;
	public static final byte COLLATE_TYPE_CATEGORY = 7;
	public static final byte COLLATE_TYPE_MAX = 7;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nsfsearc.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static final short SEARCH_ALL_VERSIONS		= 0x0001;		/* Include deleted and non-matching notes in search */
																		/* (ALWAYS "ON" in partial searches!) */
	public static final short SEARCH_SUMMARY			= 0x0002;		/* TRUE to return summary buffer with each match */
	public static final short SEARCH_FILETYPE			= 0x0004;		/* For directory mode file type filtering */
								  										/* If set, "NoteClassMask" is treated */
								  										/* as a FILE_xxx mask for directory filtering */
	public static final short SEARCH_NOTIFYDELETIONS	= 0x0010;		/* Set NOTE_CLASS_NOTIFYDELETION bit of NoteClass for deleted notes */
	public static final short SEARCH_ALLPRIVS			= 0x0040;		/* return error if we don't have full privileges */
	public static final short SEARCH_SESSION_USERNAME	= 0x0400;		/* Use current session's user name, not server's */
	public static final short SEARCH_NOABSTRACTS		= 0x1000;	  	/* Filter out "Truncated" documents */
	public static final short SEARCH_DATAONLY_FORMULA   = 0x4000;		/* Search formula applies only to 
																		data notes, i.e., others match */
	
	public static final byte SE_FNOMATCH		= 0x00;	/* does not match formula (deleted or updated) */
	public static final byte SE_FMATCH		= 0x01;	/* matches formula */
	public static final byte SE_FTRUNCATED	= 0x02;	/* document truncated */
	public static final byte SE_FPURGED		= 0x04;	/* note has been purged. Returned only when SEARCH_INCLUDE_PURGED is used */
	public static final byte SE_FNOPURGE		= 0x08;	/* note has no purge status. Returned only when SEARCH_FULL_DATACUTOFF is used */
	public static final byte SE_FSOFTDELETED = 0x10;	/* if SEARCH_NOTIFYDELETIONS: note is soft deleted; NoteClass&NOTE_CLASS_NOTIFYDELETION also on (off for hard delete) */
	public static final byte SE_FNOACCESS    = 0x20;    /* if there is reader's field at doc level this is the return value so that we could mark the replication as incomplete*/
	public static final byte SE_FTRUNCATT	= 0x40;	/* note has truncated attachments. Returned only when SEARCH1_ONLY_ABSTRACTS is used */
	
	public static final int DBOPTION_FT_INDEX			= 0x00000001;	/* Enable full text indexing */
	public static final int DBOPTION_IS_OBJSTORE		= 0x00000002;	/* TRUE if database is being used
	 							 			   as an object store - for garbage collection */
	public static final int DBOPTION_USES_OBJSTORE		= 0x00000004;	/* TRUE if database has notes which refer to an
	 							 			   object store - for garbage collection*/
	public static final int DBOPTION_OBJSTORE_NEVER		= 0x00000008;	/* TRUE if NoteUpdate of notes in this db should
	 							 			   never use an object store. */
	public static final int DBOPTION_IS_LIBRARY			= 0x00000010;  /* TRUE if database is a library */
	public static final int DBOPTION_UNIFORM_ACCESS		= 0x00000020;	/* TRUE if uniform access control
	 							 			   across all replicas */
	public static final int DBOPTION_OBJSTORE_ALWAYS	= 0x00000040;	/* TRUE if NoteUpdate of notes in this db should
	 							 			   always try to use an object store. */
	public static final int DBOPTION_NO_BGAGENT			= 0x00000200;	/* TRUE if db has no background agent */
	public static final int DBOPTION_OUT_OF_SERVICE		= 0x00000400;	/* TRUE is db is out-of-service, no new opens allowed. */
	public static final int DBOPTION_IS_PERSONALJOURNAL	= 0x00000800;	/* TRUE if db is personal journal */
	public static final int DBOPTION_MARKED_FOR_DELETE	= 0x00001000;	/* TRUE if db is marked for delete.  no new opens allowed,
	 							 				cldbdir will delete the database when ref count = 0 */
	public static final int DBOPTION_HAS_CALENDAR		= 0x00002000;	/* TRUE if db stores calendar events */
	public static final int DBOPTION_IS_CATALOG_INDEX	= 0x00004000;	/* TRUE if db is a catalog index */
	public static final int DBOPTION_IS_ADDRESS_BOOK	= 0x00008000;	/* TRUE if db is an address book */
	public static final int DBOPTION_IS_SEARCH_SCOPE	= 0x00010000;	/* TRUE if db is a "multi-db-search" repository */
	public static final int DBOPTION_IS_UA_CONFIDENTIAL	= 0x00020000;	/* TRUE if db's user activity log is confidential, only
	 							 			*  		viewable by designer and manager */
	public static final int DBOPTION_RARELY_USED_NAMES	= 0x00040000;	/* TRUE if item names are to be treated as
	 							 			*  if the ITEM_RARELY_USED_NAME flag is set. */
	public static final int DBOPTION_IS_SITEDB			= 0x00080000;	/* TRUE if db is a "multi-db-site" repository */
	
	public abstract long NSFFormulaCompile(String formulaName, String formulaText, IntRef retFormulaLength, IntRef retCompileError, IntRef retCompileErrorLine, IntRef retCompileErrorColumn, IntRef retCompileErrorOffset, IntRef retCompileErrorLength) throws DominoException;
	public abstract String NSFFormulaDecompile(long pFormulaBuffer, boolean selectionFormula) throws DominoException;
	public abstract void NSFFormulaSummaryItem(long hFormula, String itemName) throws DominoException;
	public abstract void NSFFormulaMerge(long hSrcFormula, long hDestFormula) throws DominoException;
	public abstract short NSFFormulaGetSize(long hFormula) throws DominoException;
	/**
	 * @param flags reserved - should be 0
	 */
	public abstract long NSFComputeStart(short flags, long pCompiledFormula) throws DominoException;
	public abstract void NSFComputeStop(long hCompute) throws DominoException;
	/**
	 * @return a DHANDLE referring to the result structure
	 */
	public abstract long NSFComputeEvaluate(long hCompute, long hNote, IntRef retResultLength, IntRef retNoteMatchesFormula, IntRef retNoteShouldBeDeleted, IntRef retNoteModified) throws DominoException;
	public abstract void NSFSearch(long hDb, long hFormula, String viewTitle, short searchFlags, short noteClassMask, TIMEDATE since, NSFSEARCHPROC enumRoutine, TIMEDATE retUntil) throws DominoException;
	/** @since Notes/Domino 6.0.2 */
	public abstract void NSFSearchWithUserNameList(long hDb, long hFormula, String viewTitle, short searchFlags, short noteClassMask, TIMEDATE since, NSFSEARCHPROC enumRoutine, TIMEDATE retUntil, long nameList) throws DominoException;
	public abstract void NSFSearchExtended3(long hDb, long hFormula, long hFilter, int filterFlags, String viewTitle, int searchFlags, int searchFlags1, int searchFlags2, int searchFlags3, int searchFlags4, short noteClassMask, TIMEDATE since, NSFSEARCHPROC enumRoutine, TIMEDATE retUntil, long hNamesList) throws DominoException;
	/** @since Notes/Domino 4.0 */
	public abstract int NSFDbGetOptions(long hDb) throws DominoException;
	/** @since Notes/Domino 4.0 */
	public abstract void NSFDbSetOptions(long hDb, int options, int mask) throws DominoException;
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// odstypes.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final short _LIST = 13;
	public static final short _OBJECT_DESCRIPTOR = 27;
	public static final short _VIEW_TABLE_FORMAT = 29;
	public static final short _VIEW_COLUMN_FORMAT = 30;
	public static final short _VIEW_TABLE_FORMAT2 = 43;
	public static final short _FILEOBJECT = 58;
	public static final short _COLLATION = 59;
	public static final short _COLLATE_DESCRIPTOR = 60;
	public static final short _CDKEYWORD = 68;
	public static final short _CDTEXT = 113;
	public static final short _CDFIELD = 118;
	public static final short _CDGRAPHIC = 166;
	public static final short _CDHOTSPOTBEGIN = 230;
	public static final short _CDEXTFIELD = 342;
	public static final short _VIEW_COLUMN_FORMAT2 = 428;
	public static final short _CDBEGINRECORD = 577;
	public static final short _CDENDRECORD = 578;
	public static final short _CDEMBEDDEDCTL = 636;
	public static final short _MIME_PART = 639;
	public static final short _CDEXT2FIELD = 672;
	public static final short _VIEW_TABLE_FORMAT3 = 707;
	public static final short _CDFIELDHINT = 742;
	public static final short _CDDATAFLAGS = 834;
	public static final short _CDFILEHEADER = 835;
	public static final short _CDFILESEGMENT = 836;
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// editods.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static final short LDELIM_SPACE = 0x0001;	/* low three nibbles contain delim flags */
	public static final short LDELIM_COMMA = 0x0002;
	public static final short LDELIM_SEMICOLON = 0x0004;
	public static final short LDELIM_NEWLINE = 0x0008;
	public static final short LDELIM_BLANKLINE = 0x0010;
	public static final short LD_MASK = 0x0fff;
	
	public static final short LDDELIM_SPACE = 0x1000;	/* high nibble contains the display type */
	public static final short LDDELIM_COMMA = 0x2000;
	public static final short LDDELIM_SEMICOLON = 0x3000;
	public static final short LDDELIM_NEWLINE = 0x4000;
	public static final short LDDELIM_BLANKLINE = 0x5000;
	public static final short LDD_MASK = (short)0xf000;
	
	public static final short HOTSPOTREC_TYPE_POPUP = 1;
	public static final short HOTSPOTREC_TYPE_HOTREGION = 2;
	public static final short HOTSPOTREC_TYPE_BUTTON = 3;
	public static final short HOTSPOTREC_TYPE_FILE = 4;
	public static final short HOTSPOTREC_TYPE_SECTION = 7;
	public static final short HOTSPOTREC_TYPE_ANY = 8;
	public static final short HOTSPOTREC_TYPE_HOTLINK = 11;
	public static final short HOTSPOTREC_TYPE_BUNDLE = 12;
	public static final short HOTSPOTREC_TYPE_V4_SECTION = 13;
	public static final short HOTSPOTREC_TYPE_SUBFORM = 14;
	public static final short HOTSPOTREC_TYPE_ACTIVEOBJECT = 15;
	public static final short HOTSPOTREC_TYPE_OLERICHTEXT = 18;
	public static final short HOTSPOTREC_TYPE_EMBEDDEDVIEW = 19;	/* embedded view */
	public static final short HOTSPOTREC_TYPE_EMBEDDEDFPANE = 20;	/* embedded folder pane */
	public static final short HOTSPOTREC_TYPE_EMBEDDEDNAV = 21;	/* embedded navigator */
	public static final short HOTSPOTREC_TYPE_MOUSEOVER = 22;
	public static final short HOTSPOTREC_TYPE_FILEUPLOAD = 24;	/* file upload placeholder */
	public static final short HOTSPOTREC_TYPE_EMBEDDEDOUTLINE = 27;	/* embedded outline */
	public static final short HOTSPOTREC_TYPE_EMBEDDEDCTL = 28;	/* embedded control window */
	public static final short HOTSPOTREC_TYPE_EMBEDDEDCALENDARCTL = 30;	/* embedded calendar control (date picker) */
	public static final short HOTSPOTREC_TYPE_EMBEDDEDSCHEDCTL = 31;	/* embedded scheduling control */
	public static final short HOTSPOTREC_TYPE_RCLINK = 32;	/* Not a new type, but renamed for V5 terms*/
	public static final short HOTSPOTREC_TYPE_EMBEDDEDEDITCTL = 34;	/* embedded editor control */
	public static final short HOTSPOTREC_TYPE_CONTACTLISTCTL = 36;	/* Embeddeble buddy list */
	
	public static final int HOTSPOTREC_RUNFLAG_BEGIN		= 0x00000001;
	public static final int HOTSPOTREC_RUNFLAG_END			= 0x00000002;
	public static final int HOTSPOTREC_RUNFLAG_BOX			= 0x00000004;
	public static final int HOTSPOTREC_RUNFLAG_NOBORDER		= 0x00000008;
	public static final int HOTSPOTREC_RUNFLAG_FORMULA		= 0x00000010;	/*	Popup is a formula, not text. */
	public static final int HOTSPOTREC_RUNFLAG_MOVIE		= 0x00000020; /*	File is a QuickTime movie. */
	public static final int HOTSPOTREC_RUNFLAG_IGNORE		= 0x00000040; /*	Run is for backward compatibility
															(i.e. ignore the run)
														*/
	public static final int HOTSPOTREC_RUNFLAG_ACTION		= 0x00000080;	/*	Hot region executes a canned action	*/
	public static final int HOTSPOTREC_RUNFLAG_SCRIPT		= 0x00000100;	/*	Hot region executes a script.	*/
	public static final int HOTSPOTREC_RUNFLAG_INOTES  		= 0x00001000;
	public static final int HOTSPOTREC_RUNFLAG_ISMAP    	= 0x00002000;
	public static final int HOTSPOTREC_RUNFLAG_INOTES_AUTO	= 0x00004000;
	public static final int HOTSPOTREC_RUNFLAG_ISMAP_INPUT	= 0x00008000;

	public static final int HOTSPOTREC_RUNFLAG_SIGNED  		= 0x00010000;
	public static final int HOTSPOTREC_RUNFLAG_ANCHOR  		= 0x00020000;
	public static final int HOTSPOTREC_RUNFLAG_COMPUTED		= 0x00040000;	/*	Used in conjunction
															with computed hotspots.
														*/
	public static final int HOTSPOTREC_RUNFLAG_TEMPLATE		= 0x00080000;	/*	used in conjunction
															with embedded navigator
															panes.
														*/
	public static final int HOTSPOTREC_RUNFLAG_HIGHLIGHT  	= 0x00100000;
	public static final int HOTSPOTREC_RUNFLAG_EXTACTION	= 0x00200000; /*  Hot region executes an extended action */
	public static final int HOTSPOTREC_RUNFLAG_NAMEDELEM	= 0x00400000;	/*	Hot link to a named element */

	/*	Allow R6 dual action type buttons, e.g. client LotusScript, web JS */
	public static final int HOTSPOTREC_RUNFLAG_WEBJAVASCRIPT	= 0x00800000;

	public static final int HOTSPOTREC_RUNFLAG_ODSMASK		= 0x00FFFFFC;	/*	Mask for bits stored on disk*/
	
	public static final byte CDGRAPHIC_VERSION1 = 0;		/* Created by Notes version 2 */
	public static final byte CDGRAPHIC_VERSION2 = 1;		/* Created by Notes version 3 */
	public static final byte CDGRAPHIC_VERSION3 = 2;		/* Created by Notes version 4.5 */
	
	public static final byte CDGRAPHIC_FLAG_DESTSIZE_IS_PIXELS = 0x01;
	public static final byte CDGRAPHIC_FLAG_SPANSLINES = 0x02;
	
	public static final short FREADWRITERS		= 0x0001;	/* Field contains read/writers */
	public static final short FEDITABLE			= 0x0002;	/* Field is editable, not read only */
	public static final short FNAMES			= 0x0004;	/* Field contains distinguished names */
	public static final short FSTOREDV			= 0x0008;	/* Store DV, even if not spec'ed by user */
	public static final short FREADERS			= 0x0010;	/* Field contains document readers */
	public static final short FSECTION			= 0x0020;	/* Field contains a section */
	public static final short FSPARE3			= 0x0040;	/* can be assumed to be clear in memory, V3 & later */
	public static final short FV3FAB			= 0x0080;	/* IF CLEAR, CLEAR AS ABOVE */
	public static final short FCOMPUTED			= 0x0100;	/* Field is a computed field */
	public static final short FKEYWORDS			= 0x0200;	/* Field is a keywords field */
	public static final short FPROTECTED		= 0x0400;	/* Field is protected */
	public static final short FREFERENCE		= 0x0800;	/* Field name is simply a reference to a shared field note */
	public static final short FSIGN				= 0x1000;	/* sign field */
	public static final short FSEAL				= 0x2000;	/* seal field */
	public static final short FKEYWORDS_UI_STANDARD	= 0x0000;	/* standard UI */
	public static final short FKEYWORDS_UI_CHECKBOX	= 0x4000;	/* checkbox UI */
	public static final short FKEYWORDS_UI_RADIOBUTTON = (short)0x8000;	/* radiobutton UI */
	public static final short FKEYWORDS_UI_ALLOW_NEW = (short)0xc000;	/* allow doc editor to add new values */

	public static final int FEXT_LOOKUP_EACHCHAR		= 0x00010000;	/* lookup name as each char typed */
	public static final int FEXT_KWSELRECALC			= 0x00020000;	/* recalc on new keyword selection */
	public static final int FEXT_KWHINKYMINKY			= 0x00040000;	/* suppress showing field hinky minky */
	public static final int FEXT_AFTERVALIDATION		= 0x00080000;	/* recalc after validation */
	public static final int FEXT_ACCEPT_CARET			= 0x00100000;	/* the first field with this bit set will accept the caret */
	/*	These bits are in use by the	0x02000000L
		column value.  The result of	0x04000000L
		the shifted bits is (cols - 1)	0x08000000L */
	public static final int FEXT_KEYWORD_COLS_SHIFT		= 25;
	public static final int FEXT_KEYWORD_COLS_MASK		= 0x0E000000;
	public static final int FEXT_KEYWORD_FRAME_3D		= 0x00000000;
	public static final int FEXT_KEYWORD_FRAME_STANDARD	= 0x10000000;
	public static final int FEXT_KEYWORD_FRAME_NONE		= 0x20000000;
	public static final int FEXT_KEYWORD_FRAME_MASK		= 0x30000000;
	public static final int FEXT_KEYWORD_FRAME_SHIFT	= 28;
	public static final int FEXT_KEYWORDS_UI_COMBO		= 0x40000000;
	public static final int FEXT_KEYWORDS_UI_LIST		= 0x80000000;
	
	public static final int FEXT_KW_CHOICE_RECALC		= 0x00000001; /* TRUE to recalc the value choices. */
	public static final int FEXT_HTML_IN_FIELDDEF		= 0x00000002; /* TRUE means we have a CD_EXTHTML field */

	public static final int FEXT_HIDEDELIMITERS			= 0x00000004; /* TRUE if hiding delimeters */
	public static final int FEXT_KW_RTL_READING_ORDER	= 0x00000008;
	public static final int FEXT_ALLOWTABBINGOUT		= 0x00000010; /* TRUE if tab will exit field (used for richtext only) */
	public static final int FEXT_PASSWORD				= 0x00000020; /* TRUE if field is a password field	*/
	public static final int FEXT_USEAPPLETINBROWSER		= 0x00000040; /* TRUE if an applet should be used for a browser (richtext only)	*/
	public static final int FEXT_CONTROL				= 0x00000080; /* TRUE if field is a control */

	public static final int FEXT_LITERALIZE				= 0x00000100; /* TRUE if this is a formula
														field which should have 
														item substitution based on
														on items on the form.  This
														is the counterpart to a
														computed formula which is
														a formula programmatically
														generated through @formulas. */
	public static final int FEXT_CONTROLDYNAMIC			= 0x00000200; /* TRUE if field is a dynamic control  */
	public static final int FEXT_RUNEXITINGONCHANGE		= 0x00000400; /* TRUE if should run exiting event
													  when value changes.  Currently only
													  implemented for native date/time */
	public static final int FEXT_TIMEZONE				= 0x00000800; /* TRUE if this is a time zone field */
	public static final int FEXT_PROPORTIONALHEIGHT		= 0x00004000; /* TRUE if field has proportional height	*/
	public static final int FEXT_PROPORTIONALWIDTH		= 0x00008000; /* TRUE if field has proportional width	*/
	public static final int FEXT_SHOWIMSTATUS			= 0x02000000; /* TRUE if a names type field displays im online status */
	public static final int FEXT_USEJSCTLINBROWSER		= 0x04000000; /* TRUE if we should use a JS Control in the browser */
	
	public static final short CDEXTFIELD_KEYWORDHELPER = 0x0001;
	public static final short CDEXTFIELD_NAMEHELPER = 0x0002;
	public static final int FIELD_HELPER_NONE = 0;
	public static final int FIELD_HELPER_ADDRDLG = 1;
	public static final int FIELD_HELPER_ACLDLG = 2;
	public static final int FIELD_HELPER_VIEWDLG = 2;
	
	public static final short CD_SECTION_ELEMENT			= 128;
	public static final short CD_FIELDLIMIT_ELEMENT		= 129;
	public static final short CD_BUTTONEX_ELEMENT			= 130;
	public static final short CD_TABLECELL_ELEMENT		= 131;
	
	public static final int FIELD_LIMIT_TYPE_PICTURE		= 0x00000001;
	public static final int FIELD_LIMIT_TYPE_APPLET			= 0x00000002;
	public static final int FIELD_LIMIT_TYPE_SHAREDIMAGE 	= 0x00000004;
	public static final int FIELD_LIMIT_TYPE_OBJECT			= 0x00000008;
	public static final int FIELD_LIMIT_TYPE_TEXTONLY		= 0x00000010;
	public static final int FIELD_LIMIT_TYPE_VIEW			= 0x00000020;
	public static final int FIELD_LIMIT_TYPE_CALENDAR		= 0x00000040;
	public static final int FIELD_LIMIT_TYPE_INBOX			= 0x00000080;
	public static final int FIELD_LIMIT_TYPE_ATTACHMENT		= 0x00000100;
	public static final int FIELD_LIMIT_TYPE_DATEPICKER		= 0x00000200;
	public static final int FIELD_LIMIT_TYPE_GRAPHIC		= 0x00000400;
	public static final int FIELD_LIMIT_TYPE_HELP			= 0x00000800;
	public static final int FIELD_LIMIT_TYPE_CLEAR			= 0x00001000;
	public static final int FIELD_LIMIT_TYPE_LINK			= 0x00020000;
	public static final int FIELD_LIMIT_TYPE_THUMBNAIL		= 0x00040000;
	
	public static final short CDKEYWORD_RADIO				= 0x0001;
	/*	These bits are in use by the	0x0002
		column value.  The result of	0x0004
		the shifted bits is (cols - 1)	0x0008 */
	public static final short CDKEYWORD_COLS_SHIFT		= 1;
	public static final short CDKEYWORD_COLS_MASK			= 0x000E;
	public static final short CDKEYWORD_FRAME_3D			= 0x0000;
	public static final short CDKEYWORD_FRAME_STANDARD	= 0x0010;
	public static final short CDKEYWORD_FRAME_NONE		= 0x0020;
	public static final short CDKEYWORD_FRAME_MASK		= 0x0030;
	public static final short CDKEYWORD_FRAME_SHIFT		= 4;
	public static final short CDKEYWORD_KEYWORD_RTL		= 0x0040;
	public static final short CDKEYWORD_RO_ACTIVE			= 0x0080;
	
	/* edit control styles */
	public static final short EC_STYLE_EDITMULTILINE	= 0x0001;
	public static final short EC_STYLE_EDITVSCROLL	= 0x0002;
	public static final short EC_STYLE_EDITPASSWORD	= 0x0004;
	/* combobox styles */
	public static final short EC_STYLE_EDITCOMBO		= 0x0001;
	/* list box styles */
	public static final short EC_STYLE_LISTMULTISEL = 0x0001;
	/* time control styles */
	public static final short EC_STYLE_CALENDAR		= 0x0001;
	public static final short EC_STYLE_TIME			= 0x0002;
	public static final short EC_STYLE_DURATION		= 0x0004;
	public static final short EC_STYLE_TIMEZONE		= 0x0008;
	/* control style is valid */
	public static final int EC_STYLE_VALID			= 0x80000000;

	/* other control flags */
	public static final short EC_FLAG_UNITS			= 0x000F;
	public static final short EC_FLAG_DIALOGUNITS	= 0x0001;	/* Width/Height are in dialog units, not twips */
	public static final short EC_FLAG_FITTOCONTENTS	= 0x0002;	/* Width/Height should be adjusted to fit contents */
	public static final short EC_FLAG_ALWAYSACTIVE	= 0x0010;	/* this control is active regardless of docs R/W status */
	public static final short EC_FLAG_FITTOWINDOW	= 0x0020;	/* let placeholder automatically fit to window */
	public static final short EC_FLAG_POSITION_TOP	= 0x0040;	/* position control to top of paragraph */
	public static final short EC_FLAG_POSITION_BOTTOM = 0x0080;	/* position control to bottom of paragraph */
	public static final short EC_FLAG_POSITION_ASCENT = 0x0100;	/* position control to ascent of paragraph */
	public static final short EC_FLAG_POSITION_HEIGHT = 0x0200;	/* position control to height of paragraph */
	
	public static final short EMBEDDEDCTL_VERSION1 = 0;
	
	public static final short EMBEDDEDCTL_EDIT = 0;
	public static final short EMBEDDEDCTL_COMBO = 1;
	public static final short EMBEDDEDCTL_LIST = 2;
	public static final short EMBEDDEDCTL_TIME = 3;
	public static final short EMBEDDEDCTL_KEYGEN = 4;
	public static final short EMBEDDEDCTL_FILE = 5;
	public static final short EMBEDDEDCTL_TIMEZONE = 6;
	public static final short EMBEDDEDCTL_COLOR = 7;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// editdflt.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final short FIELD_TYPE_ERROR = 0;
	public static final short FIELD_TYPE_NUMBER = 1;
	public static final short FIELD_TYPE_TIME = 2;
	public static final short FIELD_TYPE_RICH_TEXT = 3;
	public static final short FIELD_TYPE_AUTHORS = 4;
	public static final short FIELD_TYPE_READERS = 5;
	public static final short FIELD_TYPE_NAMES = 6;
	public static final short FIELD_TYPE_KEYWORDS = 7;
	public static final short FIELD_TYPE_TEXT = 8;
	public static final short FIELD_TYPE_SECTION = 9;
	public static final short FIELD_TYPE_PASSWORD = 10;
	public static final short FIELD_TYPE_FORMULA = 11;
	public static final short FIELD_TYPE_TIMEZONE = 12;
	public static final short FIELD_TYPE_COLORCTL = 18;
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// colorid.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static final short NOTES_COLOR_BLACK = 0;
	public static final short NOTES_COLOR_WHITE = 1;
	public static final short NOTES_COLOR_RED = 2;
	public static final short NOTES_COLOR_GREEN = 3;
	public static final short NOTES_COLOR_BLUE = 4;
	public static final short NOTES_COLOR_MAGENTA = 5;
	public static final short NOTES_COLOR_YELLOW = 6;
	public static final short NOTES_COLOR_CYAN = 7;
	public static final short NOTES_COLOR_DKRED = 8;
	public static final short NOTES_COLOR_DKGREEN = 9;
	public static final short NOTES_COLOR_DKBLUE = 10;
	public static final short NOTES_COLOR_DKMAGENTA = 11;
	public static final short NOTES_COLOR_DKYELLOW = 12;
	public static final short NOTES_COLOR_DKCYAN = 13;
	public static final short NOTES_COLOR_GRAY = 14;
	public static final short NOTES_COLOR_LTGRAY = 15;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// names.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final int MAXUSERNAME = 256;
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// stdnames.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static final String ACL_ANONYMOUS_NAME = "Anonymous"; //$NON-NLS-1$
	
	public static final String FIELD_CREATED = "$Created"; //$NON-NLS-1$
	public static final String FIELD_LINK = "$REF"; //$NON-NLS-1$
	public static final String FIELD_UPDATED_BY = "$UpdatedBy"; //$NON-NLS-1$
	public static final String FIELD_TITLE = "$TITLE"; //$NON-NLS-1$
	public static final String FIELD_FORM = "Form"; //$NON-NLS-1$
	public static final String FIELD_NAMED = "$Name"; //$NON-NLS-1$
	public static final String ITEM_MIMETRACK = "$MIMETrack"; //$NON-NLS-1$
	public static final String ITEM_NAME_ATTACHMENT = "$FILE"; //$NON-NLS-1$
	public static final String ITEM_NAME_FONTS = "$Fonts"; //$NON-NLS-1$
	public static final String ITEM_NAME_REVISIONS = "$Revisions"; //$NON-NLS-1$
	public static final String ITEM_NAME_TEMPLATE = "$Body"; //$NON-NLS-1$
	public static final String ITEM_NAME_FILE_DATA = "$FileData"; //$NON-NLS-1$
	public static final String MAIL_MIME_VERSION = "MIME_Version"; //$NON-NLS-1$
	public static final String MAIL_MIME_HEADER_CHARSET = "MIMEMailHeaderCharset"; //$NON-NLS-1$
	public static final String VIEW_TITLE_ITEM = FIELD_TITLE;
	public static final String VIEW_CONFLICT_ITEM = "$Conflict"; //$NON-NLS-1$
	public static final String VIEW_FORMULA_ITEM = "$Formula"; //$NON-NLS-1$
	public static final String VIEW_VIEW_FORMAT_ITEM = "$ViewFormat"; //$NON-NLS-1$
	public static final String VIEW_COLLATION_ITEM = "$Collation"; //$NON-NLS-1$
	public static final String NAMEDNOTE_PROFILE = "$Profile"; //$NON-NLS-1$
	public static final String DOC_SCRIPT_NAME = "$$ScriptName"; //$NON-NLS-1$
	
	public static final int MAXPATH = 256;
	
	public static final int DESIGN_LEVELS = 2;
	public static final int DESIGN_LEVEL_MAX = 64;
	public static final int DESIGN_NAME_MAX = ((DESIGN_LEVEL_MAX+1)*DESIGN_LEVELS);
	public static final int DESIGN_FORM_MAX = DESIGN_NAME_MAX;
	public static final int DESIGN_VIEW_MAX = DESIGN_NAME_MAX;
	public static final int DESIGN_MACRO_MAX = DESIGN_NAME_MAX;
	public static final int DESIGN_FIELD_MAX = DESIGN_LEVEL_MAX+1;
	public static final int DESIGN_COMMENT_MAX = 256;
	public static final int DESIGN_ALL_NAMES_MAX = 256;
	public static final int DESIGN_FOLDER_MAX = DESIGN_VIEW_MAX;
	public static final int DESIGN_FOLDER_MAX_NAME = DESIGN_LEVEL_MAX;
	
	public static final String DESIGN_FLAGS = "$Flags"; //$NON-NLS-1$
	
	public static final char DESIGN_FLAG_FOLDER_VIEW = 'F';
	public static final char DESIGN_FLAG_SUBFORM = 'U';
	
	public static final String DFLAGPAT_V4SEARCHBAR = "(+Qq-Bst5nmz*";/* display things editable at V4 search bar; version filtering */ //$NON-NLS-1$
	public static final String DFLAGPAT_SEARCHBAR = "(+QM-st5nmz*"; 	/* display things editable at search bar; version filtering */ //$NON-NLS-1$

	/* NOTE: DFLAGPAT_VIEWFORM... are deprecated and should not be used. These were previously used to filter three different
		note classes: form, view and field. But we're running out of unique flags, and would like to use appropriate
		flag patterns for each design class. */
	public static final String DFLAGPAT_VIEWFORM = "-FQMUGXWy#i:|@0nK;g~%z^";	/* display things editable with dialog box; version filtering */ //$NON-NLS-1$
	public static final String DFLAGPAT_VIEWFORM_MENUABLE = "-FQMUGXWy#i:|@40nK;g~%z^}";	/* display things showable on the menu */ //$NON-NLS-1$
	public static final String DFLAGPAT_VIEWFORM_ALL_VERSIONS = "-FQMUGXWy#i:|@K;g~%z^}"; /* display things editable with dialog box; no version filtering (for design) */ //$NON-NLS-1$

	public static final String DFLAGPAT_FORM = "-FQMUGXWy#i:|@0nK;g~%z^";	/* display things editable with dialog box; version filtering */ //$NON-NLS-1$
	public static final String DFLAGPAT_FORM_OR_SIMILAR = "-FMGXy#i=:|@0K;g~%z^";	/* Form, page or subform design element, visible to client and web */ //$NON-NLS-1$
	public static final String DFLAGPAT_FORM_OR_PAGE = "-FQMUGXy#i:|@0nK;g~%z^";	/* Form or page design element */ //$NON-NLS-1$
	public static final String DFLAGPAT_FORM_MENUABLE = "-FQMUGXWy#i:|@40nK;g~%z^}";	/* display things showable on the menu */ //$NON-NLS-1$
	public static final String DFLAGPAT_FORM_ALL_VERSIONS = "-FQMUGXWy#i:|@K;g~%z^}"; /* display things editable with dialog box; no version filtering (for design) */ //$NON-NLS-1$
	public static final String DFLAGPAT_PRINTFORM_ALL_VERSIONS = "+?"; /* display things editable with dialog box; no version filtering (for design) */ //$NON-NLS-1$
	public static final String DFLAGPAT_FIELD = "-FQMUGXWy#i@0nK;g~%z^"; 	/* display things editable with dialog box; version filtering (more complex than necessary, */  //$NON-NLS-1$
														/* but trying to avoid any possible incompatibility vs. earlier use of DFLAGPAT_VIEWFORM) */
	public static final String DFLAGPAT_FIELD_ALL_VERSIONS = "-FQMUGXWy#i@K;g~%z^}"; /* display things editable with dialog box; no version filtering (for design) */ //$NON-NLS-1$

	public static final String DFLAGPAT_TOOLSRUNMACRO = "-QXMBESIst5nmz{";/* display things that are runnable; version filtering */  //$NON-NLS-1$
	public static final String DFLAGPAT_AGENTSLIST = "-QXstmz{";		/* display things that show up in agents list. No version filtering (for design) */  //$NON-NLS-1$
	public static final String DFLAGPAT_PASTEAGENTS = "+I";		/* select only paste agents */ //$NON-NLS-1$
	public static final String DFLAGPAT_SCRIPTLIB = "+sh.";		/* display only database global script libraries */ //$NON-NLS-1$
	public static final String DFLAGPAT_SCRIPTLIB_LS = "(+s-jh.*";	/* display only database global LotusScript script libraries */ //$NON-NLS-1$
	public static final String DFLAGPAT_SCRIPTLIB_JAVA = "*sj";		/* display only database global Java script libraries */ //$NON-NLS-1$
	public static final String DFLAGPAT_SCRIPTLIB_JS = "+h";		/* display only database global Javascript script libraries */ //$NON-NLS-1$
	public static final String DFLAGPAT_SCRIPTLIB_SERVER_JS = "+.";	/* display only database global JS server side script libraries */ //$NON-NLS-1$
	public static final String DFLAGPAT_DATABASESCRIPT = "+t";		/* display only database level script */ //$NON-NLS-1$

	public static final String DFLAGPAT_SUBFORM = "(+U-40n*";		 	/* display only subforms; version filtering	*/ //$NON-NLS-1$
	public static final String DFLAGPAT_SUBFORM_DESIGN = "(+U-40*";	/* display only subforms; for design mode, version filtering	*/ //$NON-NLS-1$
	public static final String DFLAGPAT_SUBFORM_ALL_VERSIONS = "+U";	/* only subforms; no version filtering */ //$NON-NLS-1$
	public static final String DFLAGPAT_DBRUNMACRO = "+BS";			/* run all background filters */ //$NON-NLS-1$
	public static final String DFLAGPAT_COMPOSE = "-C40n";			/* display forms that belong in compose menu; version filtering */ //$NON-NLS-1$
	public static final String DFLAGPAT_NOHIDDENNOTES = "-n";			/* select elements not hidden from notes */ //$NON-NLS-1$
	public static final String DFLAGPAT_NOHIDDENWEB = "-w";			/* select elements not hidden from web */ //$NON-NLS-1$
	public static final String DFLAGPAT_QUERYBYFORM = "-DU40gnyz{:|";	/* display forms that appear in query by form; version filtering */ //$NON-NLS-1$
	public static final String DFLAGPAT_PRESERVE = "+P";				/* related to data dictionary; no version filtering */ //$NON-NLS-1$
	public static final String DFLAGPAT_SUBADD = "(+-40*UA";			/* subforms in the add subform list; no version filtering */ //$NON-NLS-1$
	public static final String DFLAGPAT_SUBNEW = "(+-40*UN";			/* subforms that are listed when making a new form.*/ //$NON-NLS-1$
	public static final String DFLAGPAT_VIEW = "-FG40n^";				/* display only views */ //$NON-NLS-1$
	public static final String DFLAGPAT_VIEW_MENUABLE = "-FQMUGXWy#i@40nK;g~%z^}";	/* display things showable on the menu */ //$NON-NLS-1$
	public static final String DFLAGPAT_VIEW_ALL_VERSIONS = "-FG^";		/* display only views (not folders, navigators or shared columns) */ //$NON-NLS-1$
	public static final String DFLAGPAT_VIEW_DESIGN = "-FG40^";		/* display only views, ignore hidden from notes */ //$NON-NLS-1$
	public static final String DFLAGPAT_NOTHIDDEN = "-40n";			/* design element is not hidden*/ //$NON-NLS-1$
	public static final String DFLAGPAT_FOLDER = "(+-04n*F";			/* display only folders; version filtering */ //$NON-NLS-1$
	public static final String DFLAGPAT_FOLDER_DESIGN = "(+-04*F";	/* display only folders; version filtering, ignore hidden notes */ //$NON-NLS-1$
	public static final String DFLAGPAT_FOLDER_ALL_VERSIONS = "*F";	/* display only folders; no version filtering (for design) */ //$NON-NLS-1$
	public static final String DFLAGPAT_CALENDAR = "*c";				/* display only calendar-style views */ //$NON-NLS-1$
	public static final String DFLAGPAT_SHAREDVIEWS = "-FGV^40n";		/* display only shared views */ //$NON-NLS-1$
	public static final String DFLAGPAT_SHAREDVIEWSFOLDERS = "-G^V40p";	/* display only shared views and folder; all notes & web */ //$NON-NLS-1$
	public static final String DFLAGPAT_SHAREDWEBVIEWS = "-FGV40wp^";	/* display only shared views not hidden from web */ //$NON-NLS-1$
	public static final String DFLAGPAT_SHAREDWEBVIEWSFOLDERS = "-GV40wp^"; /* display only shared views and folders not hidden from web */ //$NON-NLS-1$
	public static final String DFLAGPAT_VIEWS_AND_FOLDERS = "-G40n^";	/* display only views and folder; version filtering */ //$NON-NLS-1$
	public static final String DFLAGPAT_VIEWS_AND_FOLDERS_DESIGN = "-G40^";	/* display only views and folder; all notes & web */ //$NON-NLS-1$
	public static final String DFLAGPAT_SHARED_COLS = "(+-*^";		/* display only shared columns */ //$NON-NLS-1$

	public static final String DFLAGPAT_VIEWMAP = "(+-04n*G";			/* display only GraphicViews; version filtering */ //$NON-NLS-1$
	public static final String DFLAGPAT_VIEWMAP_ALL_VERSIONS = "*G";	/* display only GraphicViews; no version filtering (for design) */ //$NON-NLS-1$
	public static final String DFLAGPAT_VIEWMAPWEB = "(+-04w*G";		/* display only GraphicViews available to web; version filtering */ //$NON-NLS-1$
	public static final String DFLAGPAT_VIEWMAP_DESIGN = "(+-04*G"; 	/* display only GraphicViews; all notes & web navs */ //$NON-NLS-1$

	public static final String DFLAGPAT_WEBPAGE = "(+-*W";			/* display WebPages	*/ //$NON-NLS-1$
	public static final String DFLAGPAT_WEBPAGE_NOTES = "(+W-n*";		/* display WebPages	available to notes client */ //$NON-NLS-1$
	public static final String DFLAGPAT_WEBPAGE_WEB = "(+W-w*";		/* display WebPages	available to web client */ //$NON-NLS-1$
	public static final String DFLAGPAT_OTHER_DLG = "(+-04n*H";			/* display forms that belong in compose menu */ //$NON-NLS-1$
	public static final String DFLAGPAT_CATEGORIZED_VIEW = "(+-04n*T";	/* display only categorized views */ //$NON-NLS-1$

	public static final String DFLAGPAT_DEFAULT_DESIGN = "+d";		/* detect default design note for it's class (used for VIEW) */ //$NON-NLS-1$
	public static final String DFLAGPAT_FRAMESET = "(+-*#";			/* display only Frameset notes */ //$NON-NLS-1$
	public static final String DFLAGPAT_FRAMESET_NOTES = "(+#-n*";  	/* Frameset notes available to notes client */ //$NON-NLS-1$
	public static final String DFLAGPAT_FRAMESET_WEB = "(+#-w*";  	/* Frameset notes available to web client */ //$NON-NLS-1$
	public static final String DFLAGPAT_SITEMAP = "+m";			/* SiteMap notes (actually, "mQ345") */ //$NON-NLS-1$
	public static final String DFLAGPAT_SITEMAP_NOTES = "(+m-n*";		/* sitemap notes available to notes client */ //$NON-NLS-1$
	public static final String DFLAGPAT_SITEMAP_WEB = "(+m-w*";		/* sitemap notes available to web client */ //$NON-NLS-1$
	public static final String DFLAGPAT_IMAGE_RESOURCE = "+i";		/* display only shared image resources */ //$NON-NLS-1$
	public static final String DFLAGPAT_IMAGE_RES_NOTES = "(+i-n~*";	/* display only notes visible images */ //$NON-NLS-1$
	public static final String DFLAGPAT_IMAGE_RES_WEB = "(+i-w~*";	/* display only web visible images */ //$NON-NLS-1$
	public static final String DFLAGPAT_IMAGE_WELL_RESOURCE = "(+-*iv"; /* display only shared image resources that have more than one image across */ //$NON-NLS-1$
	public static final String DFLAGPAT_IMAGE_WELL_NOTES = "(+-n*iv"; /* display only shared image resources that have more than one image across - notes only */ //$NON-NLS-1$
	public static final String DFLAGPAT_IMAGE_WELL_WEB = "(+-w*iv"; /* display only shared image resources that have more than one image across - web only */ //$NON-NLS-1$
	public static final String DFLAGPAT_JAVAFILE = "(+-*g[";	/* display only java design elements */ //$NON-NLS-1$

	public static final String DFLAGPAT_JAVA_RESOURCE = "+@";			/* display only shared Java resources */ //$NON-NLS-1$
	public static final String DFLAGPAT_JAVA_RESOURCE_NOTES = "(+@-n*";	/* display only shared Java resources visible to notes */ //$NON-NLS-1$
	public static final String DFLAGPAT_JAVA_RESOURCE_WEB = "(+@-w*";		/* display only shared Java resources visible to web */ //$NON-NLS-1$

	public static final String DFLAGPAT_XSPPAGE = "*gK";		/* xsp pages */ //$NON-NLS-1$
	public static final String DFLAGPAT_XSPPAGE_WEB = "(+-w*gK";	/* xsp pages for the web */ //$NON-NLS-1$
	public static final String DFLAGPAT_XSPPAGE_NOTES = "(+-n*gK";	/* xsp pages for Notes */ //$NON-NLS-1$

	public static final String DFLAGPAT_XSPPAGE_NOPROPS = "(+-2*gK";		/* xsp pages, no prop files */ //$NON-NLS-1$
	public static final String DFLAGPAT_XSPPAGE_NOPROPS_WEB = "(+-2w*gK";	/* xsp pages, no prop files, for the web */ //$NON-NLS-1$
	public static final String DFLAGPAT_XSPPAGE_NOPROPS_NOTES = "(+-2n*gK";	/* xsp pages, no prop files, for Notes */ //$NON-NLS-1$

	public static final String DFLAGPAT_XSPCC = "*g;";		/* xsp pages */ //$NON-NLS-1$
	public static final String DFLAGPAT_XSPCC_WEB = "(+-w*g;";	/* xsp pages for the web */ //$NON-NLS-1$
	public static final String DFLAGPAT_XSPCC_NOTES = "(+-n*g;";	/* xsp pages for Notes */ //$NON-NLS-1$

	public static final String DFLAGPAT_DATA_CONNECTION_RESOURCE = "+k";		/* display only shared data connection resources */ //$NON-NLS-1$
	public static final String DFLAGPAT_DB2ACCESSVIEW = "+z";			/* display only db2 access views */ //$NON-NLS-1$

	public static final String DFLAGPAT_STYLE_SHEET_RESOURCE = "+=";	/* display only shared style sheet resources */ //$NON-NLS-1$
	public static final String DFLAGPAT_STYLE_SHEETS_NOTES = "(+=-n*"; /* display only notes visible style sheets */ //$NON-NLS-1$
	public static final String DFLAGPAT_STYLE_SHEETS_WEB = "(+=-w*";	/* display only web visible style sheets */ //$NON-NLS-1$
	public static final String DFLAGPAT_FILE = "+g-K[];`,";		/* display only files */ //$NON-NLS-1$
	public static final String DFLAGPAT_FILE_DL = "(+g-~K[];`,*";	/* list of files that should show in file DL */ //$NON-NLS-1$
	public static final String DFLAGPAT_FILE_NOTES = "(+g-K[];n`,*";	/* list of notes only files */ //$NON-NLS-1$
	public static final String DFLAGPAT_FILE_WEB = "(+g-K[];w`,*";	/* list of web only files */ //$NON-NLS-1$
	public static final String DFLAGPAT_HTMLFILES = "(+-*g>";	/* display only html files */ //$NON-NLS-1$
	public static final String DFLAGPAT_HTMLFILES_NOTES = "(+-n*g>";	/* htmlfiles that are notes visible */ //$NON-NLS-1$
	public static final String DFLAGPAT_HTMLFILES_WEB = "(+-w*g>";	/* htmlfiles that are web visible */ //$NON-NLS-1$
	public static final String DFLAGPAT_FILE_ELEMS = "(+gi|=-/[],*";	/* files plus images plus comp app plus style sheets with no directory elements, java elements */ //$NON-NLS-1$

	public static final String DFLAGPAT_SERVLET = "+z";		/* servlets */ //$NON-NLS-1$
	public static final String DFLAGPAT_SERVLET_NOTES = "(+z-n*";	/* servlets not hidden from notes */ //$NON-NLS-1$
	public static final String DFLAGPAT_SERVLET_WEB = "(+z-w*";	/* servlets not hidden from the web */ //$NON-NLS-1$

	public static final String DFLAGPAT_WEBSERVICE = "+{";		/* web service */ //$NON-NLS-1$
	public static final String DFLAGPAT_JAVA_WEBSERVICE = "(+Jj-*{";	/* java web services */ //$NON-NLS-1$
	public static final String DFLAGPAT_LS_WEBSERVICE = "*{L";		/* lotusscript web services */ //$NON-NLS-1$

	public static final String DFLAGPAT_JSP = "(+-*g<";		/* display only JSP's */ //$NON-NLS-1$

	public static final String DFLAGPAT_STYLEKIT = "(+-*g`";	/* display only stylekits */ //$NON-NLS-1$
	public static final String DFLAGPAT_STYLEKIT_NOTES = "(+-n*g`";	/* display only notes client stylekits */ //$NON-NLS-1$
	public static final String DFLAGPAT_STYLEKIT_WEB = "(+-w*g`";	/* display only web client stylekits */ //$NON-NLS-1$

	public static final String DFLAGPAT_WIDGET = "(+-*g_";	/* display only widgets */ //$NON-NLS-1$
	public static final String DFLAGPAT_WIDGET_NOTES = "(+-n*g_";	/* display only notes client stylekits */ //$NON-NLS-1$
	public static final String DFLAGPAT_WIDGET_WEB = "(+-w*g_";	/* display only web client stylekits */ //$NON-NLS-1$

	/* Shared actions must be visible to both Notes and the Web since there is
		only one of these puppies - there is no list in the designer to get at
		more than one.  However, for completeness, I'll make the appropriate
		patterns for the day we may want to have separateness. */

	public static final String DFLAGPAT_SACTIONS_DESIGN = "+y"; //$NON-NLS-1$
	public static final String DFLAGPAT_SACTIONS_WEB = "(+-0*y"; //$NON-NLS-1$
	public static final String DFLAGPAT_SACTIONS_NOTES = "(+-0*y"; //$NON-NLS-1$

	public static final String DFLAGPAT_COMPDEF = "+:";		/* Wiring Properties element is a Form note. LI 3925.05 */ //$NON-NLS-1$
	public static final String DFLAGPAT_COMPAPP = "+|";		/* Composite Application element is a Form note. LI 3925.04 */ //$NON-NLS-1$

	/* Web server patterns */
	public static final String DFLAGPAT_NONWEB = "+w80stVXp^";			/* elements that are never used on the web */ //$NON-NLS-1$
	public static final String DFLAGPAT_NONWEB_EXCLUDE = "-w80stVXp^";	/* same flags as DFLAGPAT_NONWEB */ //$NON-NLS-1$
	/* For the rest, no need to include flags from DFLAGPAT_NONWEB, since
	   these flags are excluded in an initial pass. (see insrv\inotes\ndesdict.cpp) */
	public static final String DFLAGPAT_AGENTSWEB = "(+-QXstmz{*";		/* agents that can be run from the web */ //$NON-NLS-1$
	public static final String DFLAGPAT_AGNTORWEBSVCWEB = "(+-QXstmz*";	/* agents or web services that can be run from the web */ //$NON-NLS-1$
	public static final String DFLAGPAT_WEBSERVICEWEB = "+{";			/* web services that can be run from the web */ //$NON-NLS-1$
	public static final String DFLAGPAT_FORMSWEB = "-U#Wi@y:|";			/* forms usable from the web */ //$NON-NLS-1$
	public static final String DFLAGPAT_SUBFORMSWEB = "+U";			/* subforms usable from the web */ //$NON-NLS-1$
	public static final String DFLAGPAT_FRAMESETSWEB = "+#";			/* frameset from the web */ //$NON-NLS-1$
	public static final String DFLAGPAT_PAGESWEB = "+W";				/* web pages from the web */ //$NON-NLS-1$
	public static final String DFLAGPAT_VIEWSWEB = "-G";				/* views usable from the web */ //$NON-NLS-1$
	public static final String DFLAGPAT_NAVIGATORSWEB = "+G";			/* navigators usable from the web */ //$NON-NLS-1$
	public static final String DFLAGPAT_SHAREDFIELDSWEB = "*";		/* shared fields usable from the web */ //$NON-NLS-1$
	public static final String DFLAGPAT_ALLWEB = "*";					/* all design elements */ //$NON-NLS-1$
	public static final String DFLAGPAT_NO_FILERES_DIRS = "-/";		/* all design elements excluding file resource directories*/ //$NON-NLS-1$
	public static final String DFLAGPAT_FIRSTPATTERNCHAR = "(+-*";	/* patterns start with one of these */ //$NON-NLS-1$
	public static final String DFLAGPAT_WEBHYBRIDDB = "+%";			/* all WebHybridDb design elements */ //$NON-NLS-1$
	

	public static final String STOREDFORM_ITEM_SUFFIX = "_StoredForm"; //$NON-NLS-1$
	public static final String STOREDSUBFORM_ITEM_SUFFIX = "_StoredSubform"; /* A number 1 - Number of subforms will also be append... _StoredSubform1 */ //$NON-NLS-1$
	public static final String ITEM_NAME_STOREDFORM_CRC = "$StoredFormCRC";	/* A CRC on the first $Body item of a stored form in doc document. Used to detect down stream client changes. */ //$NON-NLS-1$

	public static final String ITEM_NAME_STOREDFORM_REPID = "$Form_RepId_SF"; /* Replica id of a form's originating database. */ //$NON-NLS-1$
	
	public static final String ITEM_NAME_DOCUMENT = "$Info";			/* document header info */ //$NON-NLS-1$
	public static final String ITEM_NAME_TEMPLATE_NAME = FIELD_TITLE;	/* form title item */
	public static final String ITEM_NAME_FORMLINK = "$FormLinks";		/* form link table */ //$NON-NLS-1$
	public static final String ITEM_NAME_FIELDS = "$Fields";			/* field name table */ //$NON-NLS-1$
	public static final String ITEM_NAME_FORMPRIVS = "$FormPrivs";	/* form privileges */ //$NON-NLS-1$
	public static final String ITEM_NAME_FORMUSERS = "$FormUsers";	/* text list of users allowed to use the form */ //$NON-NLS-1$
	public static final String ITEM_NAME_FRAMESET = "$FrameSet";		/* form item to hold form Frameset definition */ //$NON-NLS-1$
	public static final String ITEM_NAME_FRAMEINFO = "$FrameInfo";	/* frameset used to open form */ //$NON-NLS-1$
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nsferr.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final short ERR_ITEM_NOT_FOUND = PKG_NSF + 34;
	public static final short ERR_SUMMARY_TOO_BIG = PKG_NSF + 49;
	public static final short ERR_NOACCESS = PKG_NSF + 70;
	public static final short ERR_FORMULA_COMPILATION = PKG_FORMULA + 1;
	public static final short ERR_NOTE_DELETED = PKG_NSF+37;
	public static final short ERR_INVALID_NOTE = PKG_NSF+39;
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// miscerr.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** "Entry not found in index" */
	public static final short ERR_NOT_FOUND = PKG_MISC + 4;
	/** "Unable to interpret Time or Date" */
	public static final short ERR_TDI_CONV = PKG_MISC + 12;
	public static final short ERR_MQ_POOLFULL = PKG_MISC + 94;
	public static final short ERR_MQ_TIMEOUT = PKG_MISC+95;
	public static final short ERR_MQSCAN_ABORT = PKG_MISC+96;
	public static final short ERR_DUPLICATE_MQ = PKG_MISC+97;
	public static final short ERR_NO_SUCH_MQ = PKG_MISC+98;
	public static final short ERR_MQ_EXCEEDED_QUOTA = PKG_MISC+99;
	public static final short ERR_MQ_EMPTY = PKG_MISC+100;
	public static final short ERR_MQ_BFR_TOO_SMALL = PKG_MISC+101;
	public static final short ERR_MQ_QUITTING = PKG_MISC+102;
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// nls.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*    NLS_translate */
	public static final short NLS_NONULLTERMINATE = 0x1;
	public static final short NLS_NULLTERMINATE = 0x2;
	public static final short NLS_STRIPUNKNOWN = 0x4;
	public static final short NLS_TARGETISLMBCS = 0x8;
	public static final short NLS_SOURCEISLMBCS = 0x10;
	public static final short NLS_TARGETISUNICODE = 0x20;
	public static final short NLS_SOURCEISUNICODE = 0x40;
	public static final short NLS_TARGETISPLATFORM = 0x80;
	public static final short NLS_SOURCEISPLATFORM = 0x100;
	
	/* NLS_CS_xxx */
	public static final short NLS_CS_DEFAULT = (short)0xFFFF;
	public static final short NLS_CS_UTF8 = 0x00AB;
	
	/** @since Notes/Domino 5.0 */
	public abstract void NLS_translate(long pString, short len, long pStringTarget, IntRef pSize, short controlFlags, long pInfo) throws DominoException;
	/** @since Notes/Domino 5.0 */
	public abstract long NLS_load_charset(short CSID) throws DominoException;
	/** @since Notes/Domino 5.0 */
	public abstract long NLS_unload_charset(long pInfo) throws DominoException;
	

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// oserr.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** File does not exist */
	public static final short ERR_NOEXIST = PKG_OS+3;
	/**
	 * ERR_CANCEL can be used to cancel a PROC operation without generating an exception
	 */
	public static final short ERR_CANCEL = PKG_OS + 157;
	/**
	 * "Maximum number of memory segments that Notes can support has been exceeded - out of handles"
	 */
	public static final short ERR_MEM_HANDLES = PKG_OS + 162;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// mimeods.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final byte MIME_PART_PROLOG = 1;
	public static final byte MIME_PART_BODY = 2;
	public static final byte MIME_PART_EPILOG = 3;
	public static final byte MIME_PART_RETRIEVE_INFO = 4;
	public static final byte MIME_PART_MESSAGE = 5;
	
	public static final short MIME_PART_HAS_BOUNDARY = 0x00000001;
	public static final short MIME_PART_HAS_HEADERS = 0x00000002;
	public static final short MIME_PART_BODY_IN_DBOBJECT = 0x00000004;
	public static final short MIME_PART_SHARED_DBOBJECT = 0x00000008;	/*	Used only with MIME_PART_BODY_IN_DBOBJECT. */
	public static final short MIME_PART_SKIP_FOR_CONVERSION = 0x00000010;	/* only used during MIME->CD conversion */

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// kfm.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public abstract String SECKFMGetUserName() throws DominoException;
	/** @since Notes/Domino 7.0 */
	public abstract long SECKFMOpen(String idFileName, String password, int flags) throws DominoException;
	/**
	 * This signature differs from the C level in that the first parameter is the KFHANDLE value
	 * itself, rather than a pointer to it (though the value itself remains defined as a pointer).
	 * @since Notes/Domino 7.0
	 */
	public abstract void SECKFMClose(long hKFC, int flags) throws DominoException;
	/** @since Notes/Domino 7.0 */
	public abstract void SECExtractIdFileFromDB(long hDB, String profileNoteName, String userName, String password, String putIDFileHere) throws DominoException;
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// urltypes.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static final int CAI_Start					= 0;
	public static final int CAI_StartKey				= 1;
	public static final int CAI_Count					= 2;
	public static final int CAI_Expand					= 3;
	public static final int CAI_FullyExpand			= 4;
	public static final int CAI_ExpandView				= 5;
	public static final int CAI_Collapse				= 6;
	public static final int CAI_CollapseView			= 7;
	public static final int CAI_3PaneUI					= 8;
	public static final int CAI_TargetFrame			= 9;
	public static final int CAI_FieldElemType			= 10;
	public static final int CAI_FieldElemFormat		= 11;
	public static final int CAI_SearchQuery			= 12;
	public static final int CAI_OldSearchQuery		= 13;
	public static final int CAI_SearchMax				= 14;
	public static final int CAI_SearchWV				= 15;
	public static final int CAI_SearchOrder			= 16;
	public static final int CAI_SearchThesarus		= 17;
	public static final int CAI_ResortAscending		= 18;
	public static final int CAI_ResortDescending		= 19;
	public static final int CAI_ParentUNID				= 20;
	public static final int CAI_Click					= 21;
	public static final int CAI_UserName				= 22;
	public static final int CAI_Password				= 23;
	public static final int CAI_To						= 24;
	public static final int CAI_ISMAPx					= 25;
	public static final int CAI_ISMAPy					= 26;
	public static final int CAI_Grid					   = 27;
	public static final int CAI_Date					   = 28;
	public static final int CAI_TemplateType			= 29;
	public static final int CAI_TargetUNID				= 30;
	public static final int CAI_ExpandSection			= 31;
	public static final int CAI_Login					= 32;
	public static final int CAI_PickupCert				= 33;
	public static final int CAI_PickupCACert			= 34;
	public static final int CAI_SubmitCert				= 35;
	public static final int CAI_ServerRequest			= 36;
	public static final int CAI_ServerPickup			= 37;
	public static final int CAI_PickupID				= 38;
	public static final int CAI_TranslateForm			= 39;
	public static final int CAI_SpecialAction			= 40;
	public static final int CAI_AllowGetMethod		= 41;
	public static final int CAI_Seq						= 42;
	public static final int CAI_BaseTarget				= 43;
	public static final int CAI_ExpandOutline			= 44;
	public static final int CAI_StartOutline			= 45;
	public static final int CAI_Days					   = 46;
	public static final int CAI_TableTab				= 47;
	public static final int CAI_MIME					   = 48;
	public static final int CAI_RestrictToCategory	= 49;
	public static final int CAI_Highlight				= 50;
	public static final int CAI_Frame					= 51;
	public static final int CAI_FrameSrc				= 52;
	public static final int CAI_Navigate				= 53;
	public static final int CAI_SkipNavigate			= 54;
	public static final int CAI_SkipCount				= 55;
	public static final int CAI_EndView					= 56;
	public static final int CAI_TableRow				= 57;
	public static final int CAI_RedirectTo				= 58;
	public static final int CAI_SessionId				= 59;
	public static final int CAI_SourceFolder			= 60;
	public static final int CAI_SearchFuzzy			= 61;
	public static final int CAI_HardDelete				= 62;
	public static final int CAI_SimpleView				= 63;
	public static final int CAI_SearchEntry			= 64;
	public static final int CAI_Name					   = 65;
	public static final int CAI_Id						= 66;
	public static final int CAI_RootAlias				= 67;
	public static final int CAI_Scope					= 68;
	public static final int CAI_DblClkTarget			= 69;
	public static final int CAI_Charset					= 70;
	public static final int CAI_EmptyTrash				= 71;
	public static final int CAI_EndKey					= 72;
	public static final int CAI_PreFormat				= 73;
	public static final int CAI_ImgIndex				= 74;
	public static final int CAI_AutoFramed				= 75;
	public static final int CAI_OutputFormat			= 76;
	public static final int CAI_InheritParent			= 77;
	public static final int CAI_Last = 78;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// log.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public abstract void LogEventText(String message, int hModule, short additionalErrorCode) throws DominoException;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// idvault.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @param pServerName a pointer to the server name to query, of at least MAXUSERNAME size. This is provided as
	 * 		a pointer in order to also serve as a return value for the server that contained the ID.
	 * @since Notes/Domino 8.5.2
	 */
	public abstract long SECidfGet(String userName, String password, String putIdFileHere, long pServerName) throws DominoException;
	/** @since Notes/Domino 8.5 */
	public abstract void SECidvResetUserPassword(String server, String userName, String password, short downloadCount) throws DominoException;
	

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ns.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public abstract long NSGetServerList(String portName) throws DominoException;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// extmgr.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final int EM_BEFORE = 0;
	public static final int EM_AFTER = 1;
	
	public static final int EM_REG_BEFORE = 0x0001;
	public static final int EM_REG_AFTER = 0x0002;
	
	public static final int EM_NSFDBCLOSESESSION = 1;
	public static final int EM_NSFDBCLOSE = 2;
	public static final int EM_NSFDBCREATE = 3;
	public static final int EM_NSFDBDELETE = 4;
	public static final int EM_NSFNOTEOPEN = 5;
	public static final int EM_NSFNOTECLOSE = 6;
	public static final int EM_NSFNOTECREATE = 7;
	public static final int EM_NSFNOTEDELETE = 8;
	public static final int EM_NSFNOTEOPENBYUNID = 10;
	public static final int EM_FTGETLASTINDEXTIME = 11;
	public static final int EM_FTINDEX = 12;
	public static final int EM_FTSEARCH = 13;
	public static final int EM_NIFFINDBYKEY = 14;
	public static final int EM_NIFFINDBYNAME = 15;
	public static final int EM_NIFOPENNOTE = 17;
	public static final int EM_NIFREADENTRIES = 18;
	public static final int EM_NIFUPDATECOLLECTION = 20;
	public static final int EM_NSFDBALLOCOBJECT = 22;
	public static final int EM_NSFDBCOMPACT = 23;
	public static final int EM_NSFDBDELETENOTES = 24;
	public static final int EM_NSFDBFREEOBJECT = 25;
	public static final int EM_NSFDBGETMODIFIEDNOTETABLE = 26;
	public static final int EM_NSFDBGETNOTEINFO = 29;
	public static final int EM_NSFDBGETNOTEINFOBYUNID = 30;
	public static final int EM_NSFDBGETOBJECTSIZE = 31;
	public static final int EM_NSFDBGETSPECIALNOTEID = 32;
	public static final int EM_NSFDBINFOGET = 33;
	public static final int EM_NSFDBINFOSET = 34;
	public static final int EM_NSFDBLOCATEBYREPLICAID = 35;
	public static final int EM_NSFDBMODIFIEDTIME = 36;
	public static final int EM_NSFDBREADOBJECT = 37;
	public static final int EM_NSFDBREALLOCOBJECT = 39;
	public static final int EM_NSFDBREPLICAINFOGET = 40;
	public static final int EM_NSFDBREPLICAINFOSET = 41;
	public static final int EM_NSFDBSPACEUSAGE = 42;
	public static final int EM_NSFDBSTAMPNOTES = 43;
	public static final int EM_NSFDBWRITEOBJECT = 45;
	public static final int EM_NSFNOTEUPDATE = 47;
	public static final int EM_NIFOPENCOLLECTION = 50;
	public static final int EM_NIFCLOSECOLLECTION = 51;
	public static final int EM_NSFDBGETBUILDVERSION = 52;
	public static final int EM_NSFDBRENAME = 54;
	public static final int EM_NSFDBITEMDEFTABLE = 56;
	public static final int EM_NSFDBREOPEN = 59;
	public static final int EM_NSFDBOPENEXTENDED = 63;
	public static final int EM_NSFNOTEOPENEXTENDED = 64;
	public static final int EM_TERMINATENSF = 69;
	public static final int EM_NSFNOTEDECRYPT = 70;
	public static final int EM_GETPASSWORD = 73;
	public static final int EM_SETPASSWORD = 74;
	public static final int EM_NSFCONFLICTHANDLER = 75;
	public static final int EM_MAILSENDNOTE = 83;
	public static final int EM_CLEARPASSWORD = 90;
	public static final int EM_NSFNOTEUPDATEXTENDED = 102;
	public static final int EM_SCHFREETIMESEARCH = 105;
	public static final int EM_SCHRETRIEVE = 106;
	public static final int EM_SCHSRVRETRIEVE = 107;
	public static final int EM_NSFDBCOMPACTEXTENDED = 121;
	public static final int EM_ADMINPPROCESSREQUEST = 124;
	public static final int EM_NIFGETCOLLECTIONDATA = 126;
	public static final int EM_NSFDBCOPYNOTE = 127;
	public static final int EM_NSFNOTECOPY = 128;
	public static final int EM_NSFNOTEATTACHFILE = 129;
	public static final int EM_NSFNOTEDETACHFILE = 130;
	public static final int EM_NSFNOTEEXTRACTFILE = 131;
	public static final int EM_NSFNOTEATTACHOLE2OBJECT = 132;
	public static final int EM_NSFNOTEDELETEOLE2OBJECT = 133;
	public static final int EM_NSFNOTEEXTRACTOLE2OBJECT = 134;
	public static final int EM_NSGETSERVERLIST = 135;
	public static final int EM_NSFDBCOPY = 136;
	public static final int EM_NSFDBCREATEANDCOPY = 137;
	public static final int EM_NSFDBCOPYACL = 138;
	public static final int EM_NSFDBCOPYTEMPLATEACL = 139;
	public static final int EM_NSFDBCREATEACLFROMTEMPLATE = 140;
	public static final int EM_NSFDBREADACL = 141;
	public static final int EM_NSFDBSTOREACL = 142;
	public static final int EM_NSFDBFILTER = 143;
	public static final int EM_FTDELETEINDEX = 144;
	public static final int EM_NSFNOTEGETINFO = 145;
	public static final int EM_NSFNOTESETINFO = 146;
	public static final int EM_NSFNOTECOMPUTEWITHFORM = 147;
	public static final int EM_NIFFINDDESIGNNOTE = 148;
	public static final int EM_NIFFINDPRIVATEDESIGNNOTE = 149;
	public static final int EM_NIFGETLASTMODIFIEDTIME = 150;
	public static final int EM_FTSEARCHEXT = 160;
	public static final int EM_NAMELOOKUP = 161;
	public static final int EM_NSFNOTEUPDATEMAILBOX = 164;
	public static final int EM_NIFFINDDESIGNNOTEEXT = 167;
	public static final int EM_AGENTOPEN = 170;
	public static final int EM_AGENTRUN = 171;
	public static final int EM_AGENTCLOSE = 172;
	public static final int EM_AGENTISENABLED = 173;
	public static final int EM_AGENTCREATERUNCONTEXT = 175;
	public static final int EM_AGENTDESTROYRUNCONTEXT = 176;
	public static final int EM_AGENTSETDOCUMENTCONTEXT = 177;
	public static final int EM_AGENTSETTIMEEXECUTIONLIMIT = 178;
	public static final int EM_AGENTQUERYSTDOUTBUFFER = 179;
	public static final int EM_AGENTREDIRECTSTDOUT = 180;
	public static final int EM_SECAUTHENTICATION = 184;
	public static final int EM_NAMELOOKUP2 = 185;
	public static final int EM_NSFDBHASPROFILENOTECHANGED = 198;
	public static final int EM_NSFMARKREAD = 208;
	public static final int EM_NSFADDTOFOLDER = 209;
	public static final int EM_NSFDBSPACEUSAGESCALED = 210; /* V6 */
	public static final int EM_NSFDBGETMAJMINVERSION = 222; /* V5.09 */
	public static final int EM_ROUTERJOURNALMESSAGE = 223;     /* V6 */

	/* V6 SMTP hooks */
	public static final int EM_SMTPCONNECT = 224;
	public static final int EM_SMTPCOMMAND = 225;
	public static final int EM_SMTPMESSAGEACCEPT = 226;
	public static final int EM_SMTPDISCONNECT = 227;
	public static final int EM_NSFARCHIVECOPYNOTES = 228;
	public static final int EM_NSFARCHIVEDELETENOTES = 229;
	public static final int EM_NSFNOTEEXTRACTWITHCALLBACK = 235;
	public static final int EM_NSFDBSTAMPNOTESMULTIITEM  = 239;
	public static final int EM_MEDIARECOVERY_NOTE = 244;
	public static final int EM_NSFGETCHANGEDDBS = 246;
	public static final int EM_NSFDBMODIFIEDTIMEBYNAME = 247;
	public static final int EM_NSFGETDBCHANGES = 250;
	public static final int EM_NSFNOTECIPHERDECRYPT = 251;
	public static final int EM_NSFNOTECIPHEREXTRACTFILE = 252;
	public static final int EM_NSFNOTECIPHEREXTRACTWITHCALLBACK = 253;
	public static final int EM_NSFNOTECIPHEREXTRACTOLE2OBJECT = 254;
	public static final int EM_NSFNOTECIPHERDELETEOLE2OBJECT = 255;
	public static final int EM_NSFDBNOTELOCK = 256;
	public static final int EM_NSFDBNOTEUNLOCK = 257;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// mq.h
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final int MQ_MAX_MSGSIZE = MAXONESEGSIZE - 0x40;
	public static final short MQ_WAIT_FOR_MSG = 0x0001;
	public static final short MQ_OPEN_CREATE = 0x00000001;
}
