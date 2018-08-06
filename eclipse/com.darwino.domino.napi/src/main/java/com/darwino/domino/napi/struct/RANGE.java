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

package com.darwino.domino.napi.struct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ibm.commons.util.NotImplementedException;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.util.DominoNativeUtils;

/**
 * <p>The RANGE struct consists of two fixed fields - ListEntries and RangeEntries - followed by an arbitrary number single and pair variants
 * of TIMEDATE and NUMBER.</p>
 *
 * <p>Not that NUMBER_PAIRs are particularly useful.</p>
 * 
 * @author Jesse Gallagher
 */
public class RANGE extends BaseStruct {

	static {
		int[] sizes = new int[3];
		initNative(sizes);
		sizeOf = sizes[0];
		_ListEntries = sizes[1];
		_RangeEntries = sizes[2];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _ListEntries;
	public static final int _RangeEntries;
	
	public static enum Type {
		NUMBER, TIME
	}

	/**
	 * <p>Creates a new RANGE containing the values from the provided {@link List} converted to Domino NUMBERs (double).</p>
	 * <p>This includes allocating the dynamic space past the RANGE to house the NUMBER values.</p>
	 */
	public static RANGE fromNumberList(Collection<? extends Number> numList) {
		int rangeSize = RANGE.sizeOf + (C.sizeOfNUMBER * numList.size());
		long rangePtr = C.malloc(rangeSize);
		
		RANGE range = new RANGE(rangePtr, true);
		range.setType(Type.NUMBER);
		range.setListEntries((short)numList.size());
		range.setRangeEntries((short)0);
		// write the values as doubles
		int i = 0;
		for(Number val : numList) {
			int offset = RANGE.sizeOf + (C.sizeOfNUMBER * i);
			if(!(val instanceof Number)) {
				C.free(rangePtr);
				throw new IllegalArgumentException("Illegal non-Number included in List");
			}
			C.setNUMBER(rangePtr, offset, val.doubleValue());
			
			i++;
		}
		return range;
	}
	/**
	 * <p>Creates a new RANGE containing the values from the provided {@link List} converted to Domino TIMEDATEs and TIMEDATE_PAIRs.
	 * The list's values must be date/time objects according to {@link DominoNativeUtils#isDateTimeType(Object)}.</p>
	 * 
	 * <p>This includes allocating the dynamic space past the RANGE to house the TIMEDATE values.</p>
	 */
	public static RANGE fromDateList(Collection<?> dateList) throws DominoException {
		// First, split the collection into date/times and ranges
		Collection<Object> datetimes = new ArrayList<Object>();
		Collection<Object> datetimepairs = new ArrayList<Object>();
		
		for(Object val : dateList) {
			if(DominoNativeUtils.isDateTimeType(val)) {
				datetimes.add(val);
			} else if(DominoNativeUtils.isDateRangeType(val)) {
				datetimepairs.add(val);
			} else {
				throw new IllegalArgumentException("Illegal non-Date/non-Calendar value included in List");
			}
		}
		
		int rangeSize = RANGE.sizeOf + (TIMEDATE.sizeOf * datetimes.size()) + (TIMEDATE_PAIR.sizeOf * datetimepairs.size());
		long rangePtr = C.malloc(rangeSize);
		
		RANGE range = new RANGE(rangePtr, true);
		range.setType(Type.TIME);
		range.setListEntries((short)datetimes.size());
		range.setRangeEntries((short)datetimepairs.size());
		
		rangePtr = C.ptrAdd(rangePtr, RANGE.sizeOf);
		
		// write the single values as TIMEDATEs
		for(Object val : datetimes) {
			TIMEDATE timeDate = new TIMEDATE(rangePtr);
			DominoNativeUtils.fillTimeDate(timeDate, val);
			
			rangePtr = C.ptrAdd(rangePtr, TIMEDATE.sizeOf);
		}
		
		// write the pair values as TIMEDATE_PAIRs
		for(Object val : datetimepairs) {
			TIMEDATE_PAIR timeDatePair = new TIMEDATE_PAIR(rangePtr);
			DominoNativeUtils.fillTimeDatePair(timeDatePair, val);
			
			rangePtr = C.ptrAdd(rangePtr, TIMEDATE_PAIR.sizeOf);
		}
		
		return range;
	}
	
	private Type type;
	
	public RANGE() {
		super(C.malloc(sizeOf), true);
	}
	public RANGE(Type type) {
		this();
		setType(type);
	}
	public RANGE(long data) {
		super(data, false);
	}
	public RANGE(long data, Type type) {
		this(data);
		setType(type);
	}
	public RANGE(long data, boolean owned) {
		super(data, owned);
	}
	public RANGE(long data, boolean owned, Type type) {
		this(data, owned);
		setType(type);
	}

	public short getListEntries() { return _getUSHORT(_ListEntries); }
	public void setListEntries(short listEntries) { _setUSHORT(_ListEntries, listEntries); }

	public short getRangeEntries() { return _getUSHORT(_RangeEntries); }
	public void setRangeEntries(short rangeEntries) { _setUSHORT(_RangeEntries, rangeEntries); }
	
	public void setType(Type type) { this.type = type; }
	public Type getType() { return type; }
	
	public int getTotalSize() {
		if(type == null) {
			throw new IllegalStateException("Cannot reliably determine size without a set type");
		}
		switch(type) {
		case NUMBER:
			// TODO add size of NUMBER_RANGE if we implement that for some reason
			return sizeOf + (getListEntries() * C.sizeOfNUMBER);
		case TIME:
			return sizeOf + (getListEntries() * TIMEDATE.sizeOf) + (getRangeEntries() * TIMEDATE_PAIR.sizeOf);
		default:
			// We can't get here
			return 0;
		}
	}
	
	/**
	 * <p>Returns the number values of this list as doubles.</p>
	 * 
	 * <p>The resultant data is detached from the original struct's memory.</p>
	 */
	public double[] getNumberValues() {
		_checkRefValidity();
		
		int numbers = getListEntries() & 0xFFFFFFFF;
		// Assume number ranges don't really exist, at least for now
		int ranges = getRangeEntries() & 0xFFFFFFFF;
		if(ranges > 0) {
			throw new NotImplementedException("getNumberValues() does not yet support number ranges"); //$NON-NLS-1$
		}
		
		long valuePtr = C.ptrAdd(data, sizeOf);
		double[] result = new double[numbers];
		for(int i = 0; i < numbers; i++) {
			result[i] = C.getNUMBER(valuePtr, 0);
			valuePtr = C.ptrAdd(valuePtr, C.sizeOfNUMBER);
		}
		
		return result;
	}
	
	/**
	 * <p>Returns the TIMEDATE elements following this RANGE, but not the TIMEDATE_PAIRs.</p>
	 * 
	 * <p>The returned structures are views on the original memory.</p>
	 */
	public TIMEDATE[] getTimeDateValues() {
		_checkRefValidity();
		
		int dates = getListEntries() & 0xFFFFFFFF;

		TIMEDATE[] result = new TIMEDATE[dates];
		
		long valuePtr = C.ptrAdd(data, sizeOf);
		for(int i = 0; i < dates; i++) {
			result[i] = new TIMEDATE(valuePtr);
			valuePtr = C.ptrAdd(valuePtr, TIMEDATE.sizeOf);
		}
		
		return result;
	}
	
	/**
	 * <p>Returns the TIMEDATE_PAIR elements following this RANGE, but not the TIMEDATEs</p>
	 * 
	 * <p>The returned structures are views on the original memory.</p>
	 */
	public TIMEDATE_PAIR[] getTimeDatePairValues()  {
		_checkRefValidity();
		
		int dates = getListEntries() & 0xFFFFFFFF;
		int ranges = getRangeEntries() & 0xFFFFFFFF;

		TIMEDATE_PAIR[] result = new TIMEDATE_PAIR[ranges];
		
		long valuePtr = C.ptrAdd(data, sizeOf);
		valuePtr = C.ptrAdd(valuePtr, dates * TIMEDATE.sizeOf);
		for(int i = 0; i < ranges; i++) {
			result[i] = new TIMEDATE_PAIR(valuePtr);
			valuePtr = C.ptrAdd(valuePtr, TIMEDATE_PAIR.sizeOf);
		}
		
		return result;
	}
}
