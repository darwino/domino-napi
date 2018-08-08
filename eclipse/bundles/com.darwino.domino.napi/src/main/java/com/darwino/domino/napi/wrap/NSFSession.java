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
package com.darwino.domino.napi.wrap;

import com.ibm.commons.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.c.IntRef;
import com.darwino.domino.napi.enums.NamesListAuth;
import com.darwino.domino.napi.struct.NAMES_LIST;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.util.DominoNativeUtils;

import lotus.domino.NotesException;

/**
 * @author Jesse Gallagher
 *
 */
public class NSFSession extends NSFBase {
	
	/**
	 * Creates an <code>NSFSession</code> based on the provided <code>lotus.domino.Session</code>, including its
	 * effective user name.
	 * 
	 * @param api the {@link DominoAPI} instance to use for API calls
	 * @param lotusSession the <code>lotus.domino.Session</code> to base the session on
	 * @param internetSession whether the session should comply with "Maximum Internet name and password" rules
	 * @param fullAccess whether the session should be marked as allowing full access
	 * @return the created session
	 */
	public static NSFSession fromLotus(DominoAPI api, lotus.domino.Session lotusSession, boolean internetSession, boolean fullAccess) throws NotesException, DominoException {
		return new NSFSession(api, lotusSession.getEffectiveUserName(), internetSession, fullAccess);
	}
	
	private final Map<Long, NSFDatabase> databasesByHandle = new HashMap<Long, NSFDatabase>();
	private File tempDir;
	
	/**
	 * Creates a new native session object. This session does not use an internal names list to manage access.
	 * 
	 * @param api the {@link DominoAPI} instance to use for API calls
	 */
	public NSFSession(DominoAPI api) {
		super(api);
		((SessionRecycler)recycler).hNamesList = 0;
	}
	/**
	 * Creates a new named session object. This session uses an internal names list to manage access.
	 * 
	 * @param api the {@link DominoAPI} instance to use for API calls
	 * @param effectiveUserName the effective user name to use for the session, if different from the local Notes ID
	 * @param internetSession whether the session should comply with "Maximum Internet name and password" rules
	 * @param fullAccess whether the session should be marked as allowing full access
	 * @throws DominoException if there is a lower-level API problem constructing the named session
	 */
	public NSFSession(DominoAPI api, String effectiveUserName, boolean internetSession, boolean fullAccess) throws DominoException {
		super(api);
		if(StringUtil.equals(effectiveUserName, getUserName())) {
			// Don't bother maintaining the names list when it's already the same name
			((SessionRecycler)recycler).hNamesList = 0;
		} else {
			((SessionRecycler)recycler).hNamesList = api.NSFBuildNamesList(effectiveUserName, 0);
			
			// Consider the name authenticated
			NAMES_LIST namesList = new NAMES_LIST(api.OSLock(((SessionRecycler)recycler).hNamesList));
			try {
				Set<NamesListAuth> flags = EnumSet.of(NamesListAuth.AUTHENTICATED);
				if(internetSession) {
					flags.add(NamesListAuth.PASSWORD_AUTHENTICATED);
				}
				if(fullAccess) {
					flags.add(NamesListAuth.FULL_ADMIN_ACCESS);
				}
				namesList.setAuthenticated(flags);
			} finally {
				api.OSUnlock(((SessionRecycler)recycler).hNamesList);
			}
		}
	}

	public NSFDatabase getDatabase(String apiPath) throws DominoException {
		String path = StringUtil.toString(apiPath);
		long hDB;
		TIMEDATE retDataModified = new TIMEDATE();
		TIMEDATE retNonDataModified = new TIMEDATE();
		try {
			hDB = api.NSFDbOpenExtended(path, (short)0, ((SessionRecycler)recycler).hNamesList, null, retDataModified, retNonDataModified);
		} finally {
			retDataModified.free();
			retNonDataModified.free();
		}
		
		String serverName = null;
		if(path.contains("!!")) { //$NON-NLS-1$
			serverName = path.substring(0, path.indexOf("!!")); //$NON-NLS-1$
		} else {
			serverName = ""; //$NON-NLS-1$
		}
		
		NSFDatabase result = addChild(new NSFDatabase(this, hDB, serverName, true));
		databasesByHandle.put(hDB, result);
		return result;
	}
	public NSFDatabase getDatabase(String serverName, String filePath) throws DominoException {
		String apiPath = api.OSPathNetConstruct(null, StringUtil.toString(serverName), StringUtil.toString(filePath));
		return getDatabase(apiPath);
	}
	/**
	 * Returns an {@link NSFDatabase} object for the given open handle. This method uses an internal pool to keep
	 * track of objects created in this way, and so will return the same object for the same handle.
	 * 
	 * <p>Objects created this way will be freed and have their handles closed when the session is freed.</p>
	 */
	public NSFDatabase getDatabaseByHandle(long hDb, String serverName) throws DominoException {
		if(!databasesByHandle.containsKey(hDb)) {
			databasesByHandle.put(hDb, addChild(new NSFDatabase(this, hDb, serverName, true)));
		}
		return databasesByHandle.get(hDb);
	}
	
	public Object[] evaluate(String formula) throws DominoException, FormulaException {
		return evaluate(formula, null);
	}
	public Object[] evaluate(String formula, NSFNote context) throws DominoException, FormulaException {
		NSFFormula compiledFormula = compileFormula(formula);
		
		long hCompute = 0;
		long hResult = 0, resultPtr = 0;
		try {
			long pCompiledFormula = compiledFormula.getCompiledFormulaPtr();
			hCompute = api.NSFComputeStart((short)0, pCompiledFormula);
			
			IntRef resultLength = new IntRef();
			hResult = api.NSFComputeEvaluate(hCompute, context == null ? 0 : context.getHandle(), resultLength, null, null, null);
			if(hResult == DominoAPI.NULLHANDLE) {
				throw new DominoException(null, "Received NULLHANDLE from NSFComputeEvaluate");
			}
			
			resultPtr = api.OSLockObject(hResult);
			
			return DominoNativeUtils.readItemValueArray(api, resultPtr, resultLength.get(), context == null ? 0 : context.getHandle());
		} finally {
			if(resultPtr != 0) {
				api.OSUnlockObject(hResult);   // This variable difference is intentional
				api.OSMemFree(hResult);
			}
			if(hCompute != 0) { api.NSFComputeStop(hCompute); }
			compiledFormula.free();
		}
	}
	
	public NSFFormula compileFormula(String formula) throws DominoException, FormulaException {
		IntRef retFormulaLength = new IntRef();
		IntRef retCompileError = new IntRef();
		IntRef retCompileErrorLine = new IntRef();
		IntRef retCompileErrorColumn = new IntRef();
		IntRef retCompileErrorOffset = new IntRef();
		IntRef retCompileErrorLength = new IntRef();
		
		long hFormula = 0;
		try {
			hFormula = api.NSFFormulaCompile(null, formula, retFormulaLength, retCompileError, retCompileErrorLine, retCompileErrorColumn, retCompileErrorOffset, retCompileErrorLength);
		} catch(DominoException ex) {
			if(ex.getStatus() == DominoAPI.ERR_FORMULA_COMPILATION) {
				throw new FormulaException(retCompileError.get(), retCompileErrorLine.get(), retCompileErrorColumn.get(), retCompileErrorOffset.get(), retCompileErrorLength.get());
			} else {
				throw ex;
			}
		}
		
		return addChild(new NSFFormula(this, hFormula, retFormulaLength.get() & 0xFFFFFFFF));
	}
	
	public NSFNoteIDCollection createNoteIDCollection() throws DominoException {
		long hTable = api.IDCreateTable(C.sizeOfNOTEID);
		return new NSFNoteIDCollection(this, hTable, true);
	}
	
	public String getUserName() throws DominoException {
		return api.SECKFMGetUserName();
	}
	public String getEffectiveUserName() throws DominoException {
		String[] names = getUserNamesList();
		if(names != null && names.length > 0) {
			return names[0];
		} else {
			return getUserName();
		}
	}
	
	public long getNamesListHandle() {
		return ((SessionRecycler)recycler).hNamesList;
	}
	
	public String[] getUserNamesList() throws DominoException {
		if(((SessionRecycler)recycler).hNamesList == 0) {
			return getUserNamesList(getUserName());
		} else {
			long namesListPtr = api.OSLock(((SessionRecycler)recycler).hNamesList);
			try {
				NAMES_LIST namesList = new NAMES_LIST(namesListPtr);
				String[] names = namesList.getNames();
				return names;
			} finally {
				api.OSUnlock(((SessionRecycler)recycler).hNamesList);
			}
		}
	}
	public String[] getUserNamesList(String userName) throws DominoException {
		long hNamesList = api.NSFBuildNamesList(userName, 0);
		try {
			long namesListPtr = api.OSLock(hNamesList);
			try {
				NAMES_LIST namesList = new NAMES_LIST(namesListPtr);
				String[] names = namesList.getNames();
				return names;
			} finally {
				api.OSUnlock(hNamesList);
			}
		} finally {
			api.OSMemFree(hNamesList);
		}
	}
	
	/**
	 * Attempts to extract the specified user's ID from the ID vault, using the provided password and server.
	 * 
	 * <p>If successful, this returns a memory handle to the ID.</p>
	 * 
	 * @param userName the user name of the ID
	 * @param password the password for the ID
	 * @param server the server containing the vault
	 * @return the extracted ID file
	 * @throws DominoException if there is a lower-level-API problem extracting the ID
	 */
	public NSFUserID getUserId(String userName, String password, String server) throws DominoException {
		_checkRefValidity();
		
		long pServerName = C.calloc(1, DominoAPI.MAXUSERNAME);
		try {
			long pServerNameLMBCS = C.toLMBCSString(server);
			try {
				int len = C.strlen(pServerNameLMBCS, 0);
				C.memcpy(pServerName, 0, pServerNameLMBCS, 0, len);
			} finally {
				C.free(pServerNameLMBCS);
			}
			long hKFC = api.SECidfGet(userName, password, StringUtil.EMPTY_STRING, pServerName);
			
			// Get the server name
			String newServer = C.getLMBCSString(pServerName, 0);
			
			return addChild(new NSFUserID(this, hKFC, newServer, null));
		} finally {
			C.free(pServerName);
		}
	}
	
	/**
	 * @param serverName the server to query
	 * @param since the start of the query
	 * @return an array of database paths modified since the start of the query
	 * @throws DominoException if there is a lower-level-API problem searching for databases
	 */
	public String[] getChangedDBPaths(String serverName, NSFDateTime since) throws DominoException {
		_checkRefValidity();
		
		long changesSizePtr = C.malloc(C.sizeOfDWORD);
		TIMEDATE nextSinceTime = new TIMEDATE();
		try {
			TIMEDATE sinceTime = since.toTIMEDATE();
			try {
				String server = serverName == null ? StringUtil.EMPTY_STRING : serverName;
				long hChanges = api.NSFGetChangedDBs(server, sinceTime, changesSizePtr, nextSinceTime);
				
				long changesSize = C.getDWORD(changesSizePtr, 0) & 0xFFFFFFFFFFFFFFFFL;
				if(changesSize > 0 && hChanges != DominoAPI.NULLHANDLE) {
					long pathList = api.OSLock(hChanges);
					try {
						long remaining = changesSize;
						List<String> result = new ArrayList<String>();
						int breaker = 0;
						while(remaining >= 0) {
							if(breaker++ > 10000) {
								throw new IllegalStateException("Probable infinite loop detected");
							}
							int pathLen = C.strlen(pathList, 0);
							String path = C.getLMBCSString(pathList, 0, pathLen);
							if(StringUtil.isNotEmpty(path)) {
								result.add(path);
							}
							
							remaining -= pathLen + 1;
							pathList = C.ptrAdd(pathList, pathLen+1);
						}
						
						return result.toArray(new String[result.size()]);
					} finally {
						api.OSUnlock(hChanges);
					}
				} else {
					return new String[0];
				}
			} finally {
				sinceTime.free();
			}
		} finally {
			C.free(changesSizePtr);
			nextSinceTime.free();
		}
	}
	
	/**
	 * @return the current active data directory
	 * @throws DominoException if there is a lower-level-API problem retrieving the directory
	 */
	public String getDataDirectory() throws DominoException {
		_checkRefValidity();
		
		return api.OSGetDataDirectory();
	}
	
	/**
	 * @return the current active program directory
	 * @throws DominoException if there is a lower-level-API problem retrieving the directory
	 */
	public String getProgramDirectory() throws DominoException {
		_checkRefValidity();
		
		return api.OSGetExecutableDirectory();
	}
	/**
	 * @param varName the environment variable to look up
	 * @return the variable value as an int, converted by Domino
	 * @throws DominoException if there is a lower-level-API problem retrieving the value
	 */
	public int getEnvironmentVariableInt(String varName) throws DominoException {
		_checkRefValidity();
		
		return api.OSGetEnvironmentInt(varName);
	}
	
	/**
	 * @param varName the environment variable to look up
	 * @return the variable value as a long, converted by Domino
	 * @throws DominoException if there is a lower-level-API problem retrieving the value
	 */
	public long getEnvironmentVariableLong(String varName) throws DominoException {
		return api.OSGetEnvironmentLong(varName);
	}
	
	/**
	 * @param varName the environment variable to look up
	 * @return the variable value as a string
	 * @throws DominoException if there is a lower-level-API problem retrieving the value
	 */
	public String getEnvironmentVariable(String varName) throws DominoException {
		return api.OSGetEnvironmentString(varName);
	}
	
	/**
	 * Retrieves a list of known servers on the given port.
	 * 
	 * @param portName the network port to search, or <code>null</code> for all ports
	 * @return a {@link Collection} of known server names
	 * @throws DominoException if there is a lower-level-API problem retrieving the list
	 */
	public Collection<String> getServerList(String portName) throws DominoException {
		long hServerTextList = api.NSGetServerList(portName);
		if(hServerTextList == DominoAPI.NULLHANDLE) {
			throw new IllegalStateException("NSGetServerList returned NULLHANDLE"); //$NON-NLS-1$
		}
		long pServerList = api.OSLockObject(hServerTextList);
		try {
			int serverCount = C.getWORD(pServerList, 0) & 0xFFFFFFFF;
			
			long pNameLengths = C.ptrAdd(pServerList, C.sizeOfWORD);
			long pNames = C.ptrAdd(pNameLengths, serverCount * C.sizeOfWORD);
			
			int[] nameLengths = new int[serverCount];
			for(int i = 0; i < serverCount; i++) {
				nameLengths[i] = C.getWORD(pNameLengths, i * C.sizeOfWORD) & 0xFFFFFFFF;
			}
			
			Collection<String> result = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
			long pCurrentName = pNames;
			for(int i = 0; i < serverCount; i++) {
				String name = C.getLMBCSString(pCurrentName, 0, nameLengths[i]);
				result.add(name);
				pCurrentName = C.ptrAdd(pCurrentName, nameLengths[i]);
			}
			
			return result;
		} finally {
			api.OSMemFree(hServerTextList);
		}
	}
	
	/**
	 * Sets the temporary directory to use for applicable operations. Specify <code>null</code>
	 * to use the system temporary directory (default).
	 * 
	 * @param tempDir the temporary directory to use
	 */
	public void setTempDir(File tempDir) {
		this.tempDir = tempDir;
	}
	
	/**
	 * Returns any specified temporary directroty that will be used for applicable operations, or
	 * <code>null</code> if those operations will use the system default.
	 * 
	 * @return any specified temporary directory
	 */
	public File getTempDir() {
		return tempDir;
	}
	
	@Override
	protected void doFree() {
		// NOP
	}
	
	static class SessionRecycler extends Recycler {
		long hNamesList;
		private final DominoAPI api;
		
		public SessionRecycler(DominoAPI api) {
			this.api = api;
		}
		
		@Override
		void doFree() {
			if(hNamesList != 0) {
				try {
					api.OSMemFree(hNamesList);
				} catch(DominoException e) { }
			}
		}
	}
	@Override
	protected Recycler createRecycler() {
		return new SessionRecycler(api);
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#isRefValid()
	 */
	@Override
	public boolean isRefValid() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#getParent()
	 */
	@Override
	protected NSFBase getParent() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#removeChild(com.darwino.domino.napi.wrap.NSFBase)
	 */
	@Override
	public <T extends NSFBase> T removeChild(T child) {
		// First, remove it from the handle pool if it's a database
		if(child instanceof NSFDatabase) {
			databasesByHandle.remove(((NSFDatabase)child).getHandle());
		}
		
		return super.removeChild(child);
	}
}
