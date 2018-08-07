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
package com.darwino.domino.napi.struct.cd;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.ibm.commons.util.NotImplementedException;

/**
 * This class provides a {@link List} interface for a sequence of Composite Data records in
 * memory.
 * 
 * @author Jesse Gallagher
 * @since 2.0.0
 */
public class CompositeDataList extends AbstractSequentialList<BaseCDStruct<? extends CDHeader<?, ?>>> {

	private final long data;
	private final long len;
	
	private long currentPos;
	private List<BaseCDStruct<? extends CDHeader<?, ?>>> fetched = new ArrayList<BaseCDStruct<? extends CDHeader<?, ?>>>();
	
	public CompositeDataList(long dataPtr, long dataLen) {
		this.data = dataPtr;
		this.currentPos = data;
		this.len = dataLen;
	}

	@Override
	public ListIterator<BaseCDStruct<? extends CDHeader<?, ?>>> listIterator(int start) {
		return new CDIterator(start);
	}

	@Override
	public int size() {
		// We'll have to read the whole thing to know this
		int breaker = 0;
		while(this.hasNextRecord()) {
			this.fetchNextRecord();
			if(breaker++ > 100000) {
				throw new RuntimeException("Probable infinite loop detected");
			}
		}
		
		return this.fetched.size();
	}

	// *******************************************************************************
	// * Internal implementation methods
	// *******************************************************************************
	private boolean hasNextRecord() {
		return currentPos < (data + len);
	}
	
	private BaseCDStruct<? extends CDHeader<?, ?>> fetchNextRecord() {
		// Peek at the next couple bytes to get a SIG and find the appropriate record type
		//CDHeader<? extends Number, ? extends Number> header = CDUtil.getHeader(currentPos);
		// TODO finish implementing
		throw new NotImplementedException();
	}
	
	private class CDIterator implements ListIterator<BaseCDStruct<? extends CDHeader<?, ?>>> {
		
		private final int start;
		private int index;
		
		public CDIterator(int start) {
			this.start = start;
			index = start-1;
		}

		@Override
		public void add(BaseCDStruct<? extends CDHeader<?, ?>> record) {
			throw new NotImplementedException();
		}

		@Override
		public boolean hasNext() {
			return hasNextRecord();
		}

		@Override
		public boolean hasPrevious() {
			return index > start;
		}

		@Override
		public BaseCDStruct<? extends CDHeader<?, ?>> next() {
			if(!hasNext()) {
				throw new IndexOutOfBoundsException("No records remaining");
			}
			
			index++;
			if(index == fetched.size()) {
				return fetchNextRecord();
			} else {
				return fetched.get(index);
			}
		}

		@Override
		public int nextIndex() {
			return index+1;
		}

		@Override
		public BaseCDStruct<? extends CDHeader<?, ?>> previous() {
			if(index <= start) {
				throw new IndexOutOfBoundsException("No previous record");
			}
			index--;
			return fetched.get(index);
		}

		@Override
		public int previousIndex() {
			return index-1;
		}

		@Override
		public void remove() {
			throw new NotImplementedException();
		}

		@Override
		public void set(BaseCDStruct<? extends CDHeader<?, ?>> record) {
			throw new NotImplementedException();
		}
		
	}
}
