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

import static com.darwino.domino.napi.DominoAPI.*;

import com.ibm.commons.log.LogMgr;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.c.IntRef;
import com.darwino.domino.napi.c.ShortRef;
import com.darwino.domino.napi.struct.COLLECTIONPOSITION;
import com.darwino.domino.napi.struct.COLLECTIONSTATS;
import com.darwino.domino.napi.util.DominoNativeUtils;

/**
 * <p>This object frees the internal {@link COLLECTIONPOSITION} when freed.</p>
 * 
 * @author Jesse Gallagher
 *
 */
public class NSFViewEntryCollection extends NSFBase {
	// TODO switch to an Iterator... probably
	public static interface ViewEntryVisitor {
		public void visit(NSFViewEntry entry) throws Exception;
	}
	
	private static final LogMgr log = DominoNativeUtils.NAPI_LOG;
	
	private final NSFView parent;
	private final COLLECTIONPOSITION position;
	private final int returnCount;
	private final int keyMatches;
	private int readMask;
	private boolean readColumnNames;
	
	private int bufferMaxEntries = 40;
	
	public NSFViewEntryCollection(NSFView parent, COLLECTIONPOSITION position, int returnCount, int keyMatches, int readMask) throws DominoException {
		super(parent.getAPI());
		
		this.parent = parent;
		this.position = addChildStruct(position);
		this.returnCount = returnCount;
		this.keyMatches = keyMatches;
		this.readMask = readMask;
		
		this.readColumnNames = false;
	}
	
	public int getCount() throws DominoException {
		if(keyMatches > -1) {
			return keyMatches;
		} else {
			int entryCount = parent.getEntryCount();
			if(returnCount > -1 && returnCount < entryCount) {
				return returnCount;
			} else {
				return entryCount;
			}
		}
		
	}
	
	/**
	 * Indicated whether the collection will read in item names when retrieving entries
	 * @return whether the collection will read in item names when retrieving entries
	 */
	public boolean isReadColumnNames() {
		return readColumnNames;
	}
	/**
	 * Sets whether the collection should read in item names when retrieving entries
	 * @param readColumnNames whether the collection should read in item names when retrieving entries
	 */
	// TODO make this update the default read mask
	public void setReadColumnNames(boolean readColumnNames) {
		this.readColumnNames = readColumnNames;
	}
	
	public void setReadMask(int readMask) {
		this.readMask = readMask;
	}
	public int getReadMask() {
		return readMask;
	}
	
	public void setBufferMaxEntries(int bufferMaxEntries) {
		this.bufferMaxEntries = bufferMaxEntries;
	}
	public int getBufferMaxEntries() {
		return bufferMaxEntries;
	}
	
	// TODO adapt this for when there's not a keyMatches
	public void eachEntry(ViewEntryVisitor visitor) throws Exception {
		_checkRefValidity();
		
		IntRef pNumEntriesReturned = new IntRef();
		ShortRef pSignalFlags = new ShortRef();
		int notesFound = 0;

		short signalFlags;
		
		int readCount;
		int loopCount = 0;
		do {
			readCount = Integer.MAX_VALUE;
			
			int readMask = getEffectiveReadMask();
			
			long hBuffer = api.NIFReadEntries(
				parent.getCollectionHandle(),
				position,
				// TODO change when FT search is implemented - add NAVIGATE_NEXT_HIT
				NAVIGATE_NEXT,
				1,
				// TODO change when FT search is implemented - add NAVIGATE_NEXT_HIT
				NAVIGATE_NEXT,
				readCount,
				readMask,
				null,
				null,
				pNumEntriesReturned,
				pSignalFlags
				);
			
			if(hBuffer == NULLHANDLE) {
				// Then there are no entries
				if(log.isTraceDebugEnabled()) {
					log.traceDebug("NSFViewEntryCollection: Received null handle when reading entries; the view must be empty"); //$NON-NLS-1$
				}
				return;
			}
			
			long entriesReturned = pNumEntriesReturned.get() & 0xFFFFFFFF;
			
			signalFlags = pSignalFlags.get();
			
			long infoPtr = api.OSLockObject(hBuffer);
			try {
				if((readMask & READ_MASK_COLLECTIONSTATS) != 0) {
					@SuppressWarnings("unused")
					COLLECTIONSTATS stats = new COLLECTIONSTATS(infoPtr);
					infoPtr = C.ptrAdd(infoPtr, COLLECTIONSTATS.sizeOf);	
				}
				
				notesFound++;
				for(int i = 0; i < entriesReturned; i++) {
					NSFViewEntry entry = new NSFViewEntry(this, notesFound, infoPtr, readMask);
					try {
						visitor.visit(entry);
					} finally {
						entry.free();
					}
					
					infoPtr = entry._getFinalPointer();
				}
			} finally {
				api.OSUnlockObject(hBuffer);
				api.OSMemFree(hBuffer);
			}
		
			if(loopCount++ > 1000000) {
				throw new RuntimeException("Probable infinite loop detected"); //$NON-NLS-1$
			}
		} while((signalFlags & SIGNAL_MORE_TO_DO) != 0);
	}
	
	public NSFViewEntry getFirst() throws DominoException {
		_checkRefValidity();
		
		IntRef pNumEntriesReturned = new IntRef();
		
		int readMask = getEffectiveReadMask();
		
		long hBuffer = api.NIFReadEntries(
			parent.getCollectionHandle(),
			position,
			// TODO change when FT search is implemented - add NAVIGATE_NEXT_HIT
			NAVIGATE_NEXT,
			1,
			// TODO change when FT search is implemented - add NAVIGATE_NEXT_HIT
			NAVIGATE_NEXT,
			1,
			readMask,
			null,
			null,
			pNumEntriesReturned,
			null
			);
		
		if(hBuffer == NULLHANDLE) {
			// Then there are no entries
			if(log.isTraceDebugEnabled()) {
				log.traceDebug("NSFViewEntryCollection: Received null handle when reading entries; the view must be empty"); //$NON-NLS-1$
			}
			return null;
		}
		
		long infoPtr = api.OSLockObject(hBuffer);
		NSFViewEntry entry;
		try {
			entry = addChild(new NSFViewEntry(this, 0, infoPtr, readMask));
		} finally {
			api.OSUnlockObject(hBuffer);
			api.OSMemFree(hBuffer);
		}
		
		return entry;
	}
	
	public int getDefaultReadMask() {
		return READ_MASK_NOTEID +
				READ_MASK_NOTEUNID +
				READ_MASK_INDEXSIBLINGS +
				READ_MASK_INDEXCHILDREN +
				READ_MASK_INDEXDESCENDANTS +
				READ_MASK_INDEXANYUNREAD +
				READ_MASK_INDENTLEVELS +
				// TODO (check parent FT searched)
				READ_MASK_INDEXUNREAD +
				READ_MASK_INDEXPOSITION +
				(isReadColumnNames() ? READ_MASK_SUMMARY : READ_MASK_SUMMARYVALUES);
	}
	
	@Override
	public NSFView getParent() {
		return parent;
	}
	
	
	@Override
	protected void doFree() {
		if(position != null) {
			position.free();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#isRefValid()
	 */
	@Override
	public boolean isRefValid() {
		return position.getDataPtr() != 0;
	}
	
	/* ******************************************************************************
	 * Private utility methods
	 ********************************************************************************/
	/**
	 * @return the requested read mask with additional sanity checks
	 */
	public int getEffectiveReadMask() {
		int readMask = getReadMask();
		if(readMask == -1) {
			readMask = getDefaultReadMask();
		}
		
		// If summary values are requested, also include the ID so we can
		// tell if it's a doc entry and thus has the two final columns
		if((readMask & READ_MASK_SUMMARY) != 0 || (readMask & READ_MASK_SUMMARYVALUES) != 0) {
			readMask |= READ_MASK_NOTEID;
		}
		
		return readMask;
	}
}
