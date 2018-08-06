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

package com.darwino.domino.napi.struct.cd;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.darwino.domino.napi.util.CDUtil;
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
