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

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import com.ibm.commons.log.LogMgr;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.c.IntRef;
import com.darwino.domino.napi.util.DominoNativeUtils;

public class NSFNoteIDCollection extends NSFHandle implements Set<Integer> {
	
	private static final LogMgr log = DominoNativeUtils.NAPI_LOG;
	
	private long modified = 0;

	/**
	 * @param session the parent {@link NSFSession} for the collection
	 * @param handle the DHANDLE value for the internal <code>IDTable</code>
	 * @param destroyOnFree whether or not to call <code>IDDestroyTable</code> on deallocation (for example,
	 * 		for handles created by <code>NSFNoteGetInfo</code>
	 */
	public NSFNoteIDCollection(NSFSession session, long handle, boolean destroyOnFree) {
		super(session, handle);
		if(!destroyOnFree) {
			recycler.setFreed(true);
		}
	}

	@Override
	public boolean add(Integer e) {
		_checkRefValidity();
		
		try {
			modified = System.nanoTime();
			return api.IDInsert(getHandle(), e);
		} catch(DominoException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public boolean addAll(Collection<? extends Integer> c) {
		_checkRefValidity();
		
		boolean changed = false;
		if(c instanceof NSFNoteIDCollection) {
			// If c is another note collection, use the internal method
			try {
				int startingSize = size();
				api.IDInsertTable(getHandle(), ((NSFNoteIDCollection)c).getHandle());
				int newSize = size();
				changed = startingSize != newSize;
			} catch(DominoException e) {
				throw new RuntimeException(e);
			}
		} else {
			// Otherwise, brute-force it
			for(Integer noteId : c) {
				if(add(noteId)) {
					changed = true;
				}
			}
		}
		if(changed) { 
			modified = System.nanoTime();
		}
		return changed;
	}

	@Override
	public void clear() {
		_checkRefValidity();
		
		try {
			modified = System.nanoTime();
			api.IDDeleteAll(getHandle());
		} catch(DominoException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean contains(Object o) {
		if(!(o instanceof Number)) {
			return false;
		}
		_checkRefValidity();
		
		try {
			boolean result = api.IDIsPresent(getHandle(), ((Number)o).intValue());
			return result;
		} catch(DominoException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object o : c) {
			if(!contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public Iterator<Integer> iterator() {
		_checkRefValidity();
		
		return new NSFNoteIDCollectionIterator();
	}

	@Override
	public boolean remove(Object o) {
		if(!(o instanceof Number)) {
			return false;
		}
		_checkRefValidity();
		
		try {
			boolean changed = api.IDDelete(getHandle(), ((Number)o).intValue());
			if(changed) {
				modified = System.nanoTime();
				if(log.isTraceDebugEnabled()) {
					log.traceDebug("Was deleted - updating modified to {0}", modified); //$NON-NLS-1$
				}
			} else {
				if(log.isTraceDebugEnabled()) {
					log.traceDebug("Did not delete {0}", o); //$NON-NLS-1$
				}
			}
			return changed;
		} catch(DominoException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		_checkRefValidity();
		
		boolean changed = false;
		if(c instanceof NSFNoteIDCollection) {
			// If c is another note collection, use the internal method
			try {
				int startingSize = size();
				api.IDDeleteTable(getHandle(), ((NSFNoteIDCollection)c).getHandle());
				int newSize = size();
				changed = startingSize != newSize;
			} catch(DominoException e) {
				throw new RuntimeException(e);
			}
		} else {
			// Otherwise, brute-force it
			for(Object o : c) {
				if(remove(o)) {
					changed = true;
				}
			}
		}
		if(changed) {
			modified = System.nanoTime();
		}
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		_checkRefValidity();
		
		try {
			long toRemove = api.IDCreateTable(C.sizeOfNOTEID);
			for(Integer noteId : this) {
				if(!c.contains(noteId)) {
					api.IDInsert(toRemove, noteId);
				}
			}
			int startingSize = size();
			api.IDDeleteTable(getHandle(), toRemove);
			int newSize = size();
			boolean changed = startingSize != newSize;
			if(changed) {
				modified = System.nanoTime();
			}
			return changed;
		} catch(DominoException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int size() {
		_checkRefValidity();
		
		try {
			return api.IDEntries(getHandle());
		} catch (DominoException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object[] toArray() {
		Integer[] result = new Integer[size()];
		int i = 0;
		for(Integer noteId : this) {
			result[i++] = noteId;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		if(a == null) {
			throw new NullPointerException("The specified array is null");
		}
		if(!Integer.class.isAssignableFrom(a.getClass().getComponentType())) {
			throw new ArrayStoreException("The type of the specified array is not a supertype of Integer");
		}
		int size = size();
		Object[] result;
		if(a.length >= size) {
			result = a;
		} else {
			result = new Object[size];
		}
		int i = 0;
		for(Integer noteId : this) {
			result[i] = noteId;
		}
		return (T[])result;
	}

	/**
	 * Determines whether the provided object is also a note collection and is considered equal by
	 * the underlying IDAreTablesEqual function.
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof NSFNoteIDCollection)) {
			return false;
		}
		if(obj == this) {
			// Quick exit for ref equality
			return true;
		}
		if(getHandle() == 0 || ((NSFNoteIDCollection)obj).getHandle() == 0) {
			// Don't worry about equality for freed objects
			return false;
		} else if(getHandle() == ((NSFNoteIDCollection)obj).getHandle()) {
			return true;
		}
		
		_checkRefValidity();
		
		return api.IDAreTablesEqual(getHandle(), ((NSFNoteIDCollection)obj).getHandle());
	}
	
	@Override
	protected Recycler createRecycler() {
		return new IDCollectionRecycler(api, getHandle());
	}
	
	static class IDCollectionRecycler extends Recycler {
		private final DominoAPI api;
		private long handle;
		
		public IDCollectionRecycler(DominoAPI api, long handle) {
			this.api = api;
			this.handle = handle;
		}

		@Override
		void doFree() {
			if(handle != 0) {
				try {
					api.IDDestroyTable(handle);
				} catch(DominoException e) {
					// This should be very unlikely
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	private class NSFNoteIDCollectionIterator implements Iterator<Integer> {

		private int size;
		private int index;
		private IntRef current;
		private final long created;
		
		public NSFNoteIDCollectionIterator() {
			size = size();
			index = 0;
			current = new IntRef();
			created = System.nanoTime();
		}
		
		@Override
		public boolean hasNext() {
			if(created < modified) {
				throw new ConcurrentModificationException("Underlying ID table has been modified");
			}
			
			return index < size;
		}

		@Override
		public Integer next() {
			if(created < modified) {
				throw new ConcurrentModificationException("Underlying ID table has been modified");
			}
			
			boolean success = api.IDScan(getHandle(), index == 0, current);
			if(!success) {
				throw new NoSuchElementException("Could not retrieve next element");
			}
			if(log.isTraceDebugEnabled()) {
				log.traceDebug("Iterator got ID {0}", current.get());
			}
			index++;
			return current.get();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("NSFNoteIDCollectionIterators do not support removing IDs in process");
		}
		
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#getParent()
	 */
	@Override
	protected NSFSession getParent() {
		return getSession();
	}
}
