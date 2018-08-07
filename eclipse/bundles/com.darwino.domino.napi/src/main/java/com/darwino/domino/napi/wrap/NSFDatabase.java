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

package com.darwino.domino.napi.wrap;

import lotus.domino.Database;
import lotus.domino.NotesException;

import com.ibm.commons.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.c.IntRef;
import com.darwino.domino.napi.enums.ACLLevel;
import com.darwino.domino.napi.enums.DBClass;
import com.darwino.domino.napi.enums.DBOption;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.TransactionCommitFlag;
import com.darwino.domino.napi.enums.TransactionFlag;
import com.darwino.domino.napi.proc.NSFSEARCHPROC;
import com.darwino.domino.napi.struct.DBREPLICAINFO;
import com.darwino.domino.napi.struct.OID;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.struct.UNIVERSALNOTEID;
import com.darwino.domino.napi.util.DominoNativeUtils;
import com.darwino.domino.napi.wrap.design.NSFDatabaseDesign;

/**
 * @author Jesse Gallagher
 *
 */
public class NSFDatabase extends NSFHandle {
	public static NSFDatabase fromLotus(NSFSession session, Database lotusDatabase) throws NotesException, DominoException {
		String serverName = lotusDatabase.getServer();
		if(StringUtil.equalsIgnoreCase(serverName, lotusDatabase.getParent().getServerName())) {
			// Ignore the server name when it's on the same server
			serverName = ""; //$NON-NLS-1$
		}
		String fileName = lotusDatabase.getFilePath();
		return session.getDatabase(serverName, fileName);
	}
	
	/**
	 * This class represents the pair of dates returned by {@link NSFDatabase#getLastModified}.
	 * 
	 * @author Jesse Gallagher
	 * @since 1.5.1
	 */
	public static class ModificationTimePair implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private final NSFDateTime dataModified;
		private final NSFDateTime nonDataModified;
		
		private ModificationTimePair(NSFDateTime dataModified, NSFDateTime nonDataModified) {
			this.dataModified = dataModified;
			this.nonDataModified = nonDataModified;
		}
		
		/**
		 * @return the most recent modification time of a data note in the database
		 */
		public NSFDateTime getDataModified() {
			return dataModified;
		}
		/**
		 * @return the most recent modification time of a design note in the database
		 */
		public NSFDateTime getNonDataModified() {
			return nonDataModified;
		}
	}
	
	private final String serverName;
	private final boolean destroyOnFree;
	
	private List<NSFView> viewCache;
	private List<NSFView> folderCache;
	private Map<Integer, NSFNoteIDCollection> folderTableCache;
	
	/**
	 * Constructs a new <code>NSFDatabase</code> from the provided parent {@link NSFSession},
	 * opened API handle, and effective server name. Note: as this does not guarantee proper
	 * parent/child relationships, it should be used rarely, and the <code>getDatabase*</code>
	 * methods from <code>NSFSession</code> should be preferred.
	 * 
	 * @param destroyOnFree whether or not to call <code>NSFDbClose</code> on deallocation
	 */
	public NSFDatabase(NSFSession session, long handle, String serverName, boolean destroyOnFree) {
		super(session, handle);
		this.serverName = serverName;
		this.destroyOnFree = destroyOnFree;
	}
	
	public NSFNote createNote() throws DominoException {
		_checkRefValidity();
		
		long hNote = api.NSFNoteCreate(this.getHandle());
		return addChild(new NSFNote(this, hNote));
	}
	
	public NSFNote getNoteByID(String noteId) throws DominoException {
		_checkRefValidity();
		
		// Parse as Long to allow for the high-bit special Note IDs without complaint from Java
		return getNoteByID((int)Long.parseLong(noteId, 16));
	}
	public NSFNote getNoteByID(int noteId) throws DominoException {
		_checkRefValidity();
		
		long hNote = api.NSFNoteOpenExt(getHandle(), noteId, DominoAPI.OPEN_RAW_MIME | DominoAPI.OPEN_RESPONSE_ID_TABLE);
		return addChild(new NSFNote(this, hNote));
	}
	
	public NSFNote getNoteByUNID(String unid) throws DominoException {
		_checkRefValidity();
		
		return getNoteByID(getNoteIDForUNID(unid));
	}
	
	/**
	 * This method retrieves information from the note's header, without opening the note itself,
	 * and so is useful for efficiency and for querying deleted notes.
	 * 
	 * <p>This information is retrieved via <code>NSFDbGetNoteInfoExt</code>.</p>
	 * 
	 * @param noteId the note ID to query (may contain deletion flags)
	 * @return a {@link NSFNoteInfo} object with the note's information
	 * @throws DominoException if there is a lower-level-API problem retrieving the note infromation
	 */
	public NSFNoteInfo getNoteInfo(int noteId) throws DominoException {
		_checkRefValidity();
		
		OID oid = new OID();
		TIMEDATE modified = new TIMEDATE();
		IntRef noteClass = new IntRef();
		TIMEDATE addedToFile = new TIMEDATE();
		IntRef responseCount = new IntRef();
		IntRef parentNoteID = new IntRef();
		try {
			DominoAPI.get().NSFDbGetNoteInfoExt(getHandle(), DominoNativeUtils.toNonDeletedId(noteId), oid, modified, noteClass, addedToFile, responseCount, parentNoteID);
			return new NSFNoteInfo(noteId, oid, modified, (short)noteClass.get(), addedToFile, responseCount.get(), parentNoteID.get());
		} finally {
			oid.free();
			modified.free();
			addedToFile.free();
		}
	}
	
	/**
	 * Delete the specified note from the database.
	 * 
	 * @param force whether to force deletion
	 * @param purge whether to delete without leaving a replication stub
	 * @throws DominoException if there is a problem deleting the note
	 */
	public void deleteNoteByID(int noteId, boolean force, boolean purge) throws DominoException {
		_checkRefValidity();
		
		int flags = 0;
		if(force) {
			flags |= DominoAPI.UPDATE_FORCE;
		}
		if(purge) {
			flags |= DominoAPI.UPDATE_NOSTUB;
		}
		api.NSFNoteDeleteExtended(getHandle(), noteId, flags);
	}
	
	/**
	 * Retrieve the first view or folder with the specified name or alias.
	 * 
	 * @param viewName the name or alias of the view to find
	 * @return the first view in the database with the specified name or alias
	 * @throws DominoException if a view of that name cannot be found in the database
	 */
	public NSFView getView(String viewName) throws DominoException {
		_checkRefValidity();
		
		int viewNoteID = api.NIFFindView(this.getHandle(), viewName);
		if(viewNoteID == 0) {
			throw new DominoException(null, "Cannot find view with name \"{0}\"", viewName); //$NON-NLS-1$
		}
		return addChild(new NSFView(this, viewNoteID));
	}
	
	/**
	 * Retrieve the view or folder by its design note's ID.
	 * 
	 * @param noteId the ID of the view or folder's design note
	 * @return the view in the database for the specified note ID
	 * @throws DominoException if the specified note cannot be found in the database or if it is not a view
	 */
	public NSFView getView(int noteId) throws DominoException {
		_checkRefValidity();
		
		if(noteId == 0) {
			throw new IllegalArgumentException("Cannot create a view for note ID 0");
		}
		
		// Special support for the design collection
		if(noteId != (DominoAPI.NOTE_CLASS_DESIGN | DominoAPI.NOTE_ID_SPECIAL)) {
			// Check to see if it exists
			IntRef retNoteClass = new IntRef();
			api.NSFDbGetNoteInfo(this.getHandle(), noteId, null, null, retNoteClass);
			if(retNoteClass.get() != DominoAPI.NOTE_CLASS_VIEW) {
				throw new DominoException(null, "The specified note {0} is not a view", Integer.toString(noteId, 16));
			}
		}
		
		return addChild(new NSFView(this, noteId));
	}
	
	/**
	 * Retrieve a {@link List} of the views and folders in the database.
	 * 
	 * @return a {@link List} of the views and folders in the database
	 * @throws DominoException if there is a lower-level-API problem collecting the views
	 */
	public List<NSFView> getViews() throws DominoException {
		if(this.viewCache == null) {
			_checkRefValidity();
			
			final List<NSFView> result = new ArrayList<NSFView>();
			List<Integer> formIds = getDesign().findDesignNotes(DominoAPI.NOTE_CLASS_VIEW, null);
			for(Integer noteId : formIds) {
				result.add(addChild(new NSFView(NSFDatabase.this, noteId)));
			}
			
			this.viewCache = result;
		}
		return Collections.unmodifiableList(this.viewCache);
	}
	
	/**
	 * Retrieve a {@link List} of the folders in the database. This is equivalent to calling
	 * {@link #getViews()} and filtering out non-view objects.
	 * 
	 * @return a {@link List} of the folders in the database
	 * @throws DominoException if there is a lower-level-API problem collecting the views
	 */
	public List<NSFView> getFolders() throws DominoException {
		_checkRefValidity();
		
		if(this.folderCache == null) {
			if(this.viewCache != null) {
				// If the view cache has already been generated, use that
				List<NSFView> views = getViews();
				List<NSFView> result = new ArrayList<NSFView>();
				for(NSFView view : views) {
					if(view.isFolder()) {
						result.add(view);
					} else {
						view.free();
					}
				}
				this.folderCache = result;
			} else {
				// Otherwise, do an optimized search for folders
				final List<NSFView> result = new ArrayList<NSFView>();
				NSFView designCollection = getView(DominoAPI.NOTE_CLASS_DESIGN | DominoAPI.NOTE_ID_SPECIAL);
				NSFViewEntryCollection entries = designCollection.getAllEntries();
				entries.setReadMask(DominoAPI.READ_MASK_NOTECLASS | DominoAPI.READ_MASK_NOTEID | DominoAPI.READ_MASK_SUMMARYVALUES);
				try {
					entries.eachEntry(new NSFViewEntryCollection.ViewEntryVisitor() {
						@Override public void visit(NSFViewEntry entry) throws DominoException {
							if(entry.getNoteClass() == DominoAPI.NOTE_CLASS_VIEW) {
								// Then check the flags
								String flags = StringUtil.toString(entry.getColumnValues()[4]);
								if(flags.indexOf(DominoAPI.DESIGN_FLAG_FOLDER_VIEW) > -1) {
									result.add(addChild(new NSFView(NSFDatabase.this, entry.getNoteId())));
								}
							}
						}
					});
				} catch(DominoException e) {
					throw e;
				} catch(RuntimeException e) {
					throw e;
				} catch(Exception e) {
					throw new DominoException(e, "Exception while finding views");
				} finally {
					designCollection.free();
				}
				
				this.folderCache = result;
			}
		} return Collections.unmodifiableList(this.folderCache);
	}
	
	/**
	 * Create a folder in the database with the given name.
	 * 
	 * @param name the name of the folder to create
	 * @param isPrivate whether the folder should be marked as private
	 * @return the newly-created folder
	 * @throws DominoException if there is a lower-level-API problem creating the folder
	 */
	public NSFView createFolder(String name, boolean isPrivate) throws DominoException {
		_checkRefValidity();
		
		long hDb = this.getHandle();
		int type = isPrivate ? DominoAPI.DESIGN_TYPE_PRIVATE_DATABASE : DominoAPI.DESIGN_TYPE_SHARED;
		int noteId = api.FolderCreate(hDb, hDb, 0, hDb, name, type, 0);
		NSFView result = addChild(new NSFView(this, noteId));
		
		this.folderCache = null;
		this.viewCache = null;
		
		return result;
	}
	
	public int getNoteIDForUNID(String unid) throws DominoException {
		_checkRefValidity();
		
		UNIVERSALNOTEID unidStruct = new UNIVERSALNOTEID();
		IntRef retNoteID = new IntRef();
		try {
			unidStruct.setUNID(unid);
			// Fetch the ID first, so that the note can be opened with NSFNoteOpenExt
			api.NSFDbGetNoteInfoByUNID(getHandle(), unidStruct, retNoteID, null, null, null);
			int noteId = retNoteID.get();
			return noteId;
		} finally {
			unidStruct.free();
		}
	}
	public String getUNIDForNoteID(int noteId) throws DominoException {
		_checkRefValidity();
		
		OID oid = new OID();
		try {
			api.NSFDbGetNoteInfo(getHandle(), noteId, oid, null, null);
			String unid = oid.getUNID();
			return unid;
		} finally {
			oid.free();
		}
	}
	
	/**
	 * Returns an {@link NSFNoteIDCollection} containing the IDs of notes in the database matching the
	 * provided note class mask and modified since the provided {@link TIMEDATE}.
	 */
	public NSFNoteIDCollection getModifiedNotes(short noteClassMask, TIMEDATE since) throws DominoException {
		_checkRefValidity();
		
		TIMEDATE until = new TIMEDATE();
		try {
			NSFNoteIDCollection result = getModifiedNotes(noteClassMask, since, until);
			return result;
		} finally {
			until.free();
		}
	}
	/**
	 * <p>Returns an {@link NSFNoteIDCollection} containing the IDs of notes in the database matching the
	 * provided note class mask and modified since the provided {@link TIMEDATE}.</p>
	 * 
	 * <p>The ending time of the search is stored in the <code>until</code> parameter.</p>
	 */
	public NSFNoteIDCollection getModifiedNotes(short noteClassMask, TIMEDATE since, TIMEDATE until) throws DominoException {
		_checkRefValidity();
		
		long hTable = api.NSFDbGetModifiedNoteTable(getHandle(), noteClassMask, since, until);
		return getParent().addChild(new NSFNoteIDCollection(getSession(), hTable, true));
	}
	
	public String getApiPath() throws DominoException {
		_checkRefValidity();
		
		long retCanonicalPathName = C.malloc(DominoAPI.MAXPATH);
		try {
			api.NSFDbPathGet(getHandle(), retCanonicalPathName, 0);
			return C.getLMBCSString(retCanonicalPathName, 0);
		} finally {
			C.free(retCanonicalPathName);
		}
	}
	
	public String getServer() throws DominoException {
		return serverName;
	}
	
	public String getFilePath() throws DominoException {
		return getApiPath();
	}
	
	public String getReplicaId() throws DominoException {
		_checkRefValidity();
		
		DBREPLICAINFO info = new DBREPLICAINFO();
		try {
			api.NSFDbReplicaInfoGet(getHandle(), info);
			return info.getID().toHexString();
		} finally {
			info.free();
		}
	}
	
	public void search(String query, NSFSEARCHPROC proc) throws DominoException, FormulaException {
		search(query, proc, (short)0, DominoAPI.NOTE_CLASS_DATA, null, null);
	}
	public void search(String query, NSFSEARCHPROC proc, short searchFlags, short noteClassMask, TIMEDATE since, TIMEDATE until) throws DominoException, FormulaException {
		search(query, proc, (int)searchFlags, 0, 0, 0, 0, noteClassMask, since, until);
	}
	public void search(String query, NSFSEARCHPROC proc, int searchFlags, int searchFlags1, int searchFlags2, int searchFlags3, int searchFlags4, short noteClassMask, TIMEDATE since, TIMEDATE until) throws DominoException, FormulaException {
		_checkRefValidity();
		NSFFormula compiledQuery = getSession().compileFormula(query);
		try {
			long hNamesList = getParent().getNamesListHandle();
			api.NSFSearchExtended3(getHandle(), compiledQuery.getHandle(), 0, 0, null, searchFlags, searchFlags1, searchFlags2, searchFlags3, searchFlags4, noteClassMask, since, proc, until, hNamesList);
		} finally {
			compiledQuery.free();
		}
	}
	
	/**
	 * Returns the effective user name used to open the database.
	 * 
	 * @return the effective user name
	 * @throws DominoEception if there is a problem determining the user name
	 */
	public String getEffectiveUserName() throws DominoException {
		return api.NSFDbUserNameGet(getHandle());
	}
	
	public List<NSFView> getFoldersContainingNoteID(int noteId) throws DominoException {
		if(this.folderTableCache == null) {
			this.folderTableCache = new IdentityHashMap<Integer, NSFNoteIDCollection>();
			for(NSFView folder : this.getFolders()) {
				NSFNoteIDCollection contents = folder.asFolder().getContents();
				contents.removeFromParent();
				addChild(contents);
				this.folderTableCache.put(folder.getNoteID(), contents);
			}
		}
		List<NSFView> result = new ArrayList<NSFView>();
		for(Map.Entry<Integer, NSFNoteIDCollection> entry : this.folderTableCache.entrySet()) {
			if(entry.getValue().contains(noteId)) {
				result.add(getView(entry.getKey()));
			}
		}
		return result;
	}
	
	public ACLLevel getCurrentAccessLevel() {
		_checkRefValidity();
		
		long retAccessLevel = C.malloc(C.sizeOfWORD);
		long retAccessFlag = C.malloc(C.sizeOfWORD);
		try {
			api.NSFDbAccessGet(getHandle(), retAccessLevel, retAccessFlag);
			return DominoEnumUtil.valueOf(ACLLevel.class, C.getWORD(retAccessLevel, 0));
		} finally {
			C.free(retAccessLevel);
			C.free(retAccessFlag);
		}
	}
	
	public String getTitle() throws DominoException {
		_checkRefValidity();
		
		long infoBuffer = api.NSFDbInfoGet(getHandle());
		try {
			return api.NSFDbInfoParse(infoBuffer, DominoAPI.INFOPARSE_TITLE);
		} finally {
			C.free(infoBuffer);
		}
	}
	
	public DBClass getDatabaseClass() throws DominoException {
		_checkRefValidity();
		
		short dbClass = api.NSFDbClassGet(getHandle());
		return DominoEnumUtil.valueOf(DBClass.class, dbClass);
	}
	
	/**
	 * Retrieves the last modification time of data and non-data notes in the database.
	 * 
	 * @return a {@link ModificationTimePair} containing the two {@link NSFDateTime}s
	 * @throws DominoException if there is a lower-level-API problem determining the times
	 */
	public ModificationTimePair getLastModified() throws DominoException {
		_checkRefValidity();
		
		TIMEDATE retDataModified = new TIMEDATE();
		TIMEDATE retNonDataModified = new TIMEDATE();
		try {
			api.NSFDbModifiedTime(getHandle(), retDataModified, retNonDataModified);
			NSFDateTime dataModified = new NSFDateTime(retDataModified);
			NSFDateTime nonDataModified = new NSFDateTime(retNonDataModified);
			return new ModificationTimePair(dataModified, nonDataModified);
		} finally {
			retDataModified.free();
			retNonDataModified.free();
		}
	}
	
	public NSFACL getACL() throws DominoException {
		_checkRefValidity();
		
		long hACL = api.NSFDbReadACL(getHandle());
		return addChild(new NSFACL(this, hACL));
	}
	
	/**
	 * Returns an {@link NSFDatabaseDesign} view of the database, which allows encapsulated
	 * access to the design notes of the database.
	 * 
	 * @return an {@link NSFDatabaseDesign} object representing the database
	 */
	public NSFDatabaseDesign getDesign() {
		return new NSFDatabaseDesign(this);
	}
	
	public Set<DBOption> getOptions() throws DominoException {
		_checkRefValidity();
		
		int opts = getAPI().NSFDbGetOptions(getHandle());
		return DominoEnumUtil.valuesOf(DBOption.class, opts);
	}
	
	/**
	 * Begins a transaction for the current database handle.
	 * 
	 * @param flags a set of {@link TransactionFlag}s to pass to the transaction call
	 * @throws DominoException if there is a lower-level-API problem starting the transaction
	 */
	public void transactionBegin(Collection<TransactionFlag> flags) throws DominoException {
		_checkRefValidity();
		
		api.NSFTransactionBegin(getHandle(), DominoEnumUtil.toBitField(TransactionFlag.class, flags));
	}
	
	/**
	 * Commits an active transaction for the current database handle.
	 * 
	 * @param flags a set of {@link TransactionCommitFlag}s to pass to the transaction call
	 * @throws DominoException if there is a lower-level-API problem committing the transaction
	 */
	public void transactionCommit(Collection<TransactionCommitFlag> flags) throws DominoException {
		_checkRefValidity();
		
		api.NSFTransactionCommit(getHandle(), DominoEnumUtil.toBitField(TransactionCommitFlag.class, flags));
	}
	
	/**
	 * Rolls back an active transaction for the current database handle.
	 * 
	 * @throws DominoException if there is a lower-level-API problem rolling back the transaction
	 */
	public void transactionRollback() throws DominoException {
		_checkRefValidity();
		
		api.NSFTransactionRollback(getHandle());
	}

	@Override
	protected void doFree() {
		if(this.folderTableCache != null) {
			for(NSFNoteIDCollection idTable : this.folderTableCache.values()) {
				idTable.free();
			}
		}
		
		if(this.getHandle() > 0 && destroyOnFree) {
			try {
				api.NSFDbClose(this.getHandle());
			} catch(DominoException e) {
				// This should be very unlikely
				throw new RuntimeException(e);
			}
		}
		super.doFree();
	}
	
	@Override
	public NSFSession getParent() {
		return getSession();
	}
}
