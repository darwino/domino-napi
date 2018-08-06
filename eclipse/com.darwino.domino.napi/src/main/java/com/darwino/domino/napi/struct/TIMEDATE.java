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

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.ibm.commons.log.LogMgr;
import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.TimeConstant;
import com.darwino.domino.napi.util.DominoNativeUtils;




/**
 * Notes TIMEDATE struct.
 */
public class TIMEDATE extends BaseStruct implements TimeStruct, Comparable<TIMEDATE> {
	
	private static final LogMgr log = DominoNativeUtils.NAPI_LOG;

	static {
		int[] sizes = new int[2];
		initNative(sizes);
		sizeOf = sizes[0];
		_Innards = sizes[1];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Innards;
			
	public TIMEDATE() {
		super(C.calloc(1, sizeOf),true);
	}

	public TIMEDATE(/*pointer*/long pointer) {
		super(pointer,false);
	}

	public TIMEDATE(/*pointer*/long pointer, boolean owned) {
		super(pointer,owned);
	}
	
	/**
	 * Sets the value of this TIMEDATE to a time constant defined by Domino.
	 * 
	 * @param timeConstant a time constant value, one of TIMEDATE_xxx
	 */
	public void setToTimeConstant(short timeConstant) throws DominoException {
		_checkRefValidity();
		DominoAPI.get().TimeConstant(timeConstant, this);
	}
	public void setToTimeConstant(TimeConstant timeConstant) throws DominoException {
		_checkRefValidity();
		setToTimeConstant(timeConstant.getValue());
	}
	
	/**
	 * @return the byte values of this <code>TIMEDATE</code> as a hex string
	 */
	public String toHexString() {
		StringBuilder b = new StringBuilder();
		appendHexInt8(b, getInnards(1));
		appendHexInt8(b, getInnards(0));
		return b.toString();
	}
	private void appendHexInt8(StringBuilder b, int value) {
		for (int i = 7; i >= 0; i--) {
			int v = (value >>> (i * 4)) & 0x0F;
	        char hexChar = (char)((v>=10) ? (v-10+'A') : (v+'0'));
			b.append(hexChar);
		}
	}
	
	public final int getInnards(int index) { return _getDWORD(_Innards + C.sizeOfDWORD*index); }
	public final void setInnards(int index, int value) { _setDWORD(_Innards + C.sizeOfDWORD*index, value); }
	
	/**
	 * It is the responsibility of the caller to free the returned {@link TIME} struct.
	 * @return a {@link TIME} struct populated with this TIMEDATE and processed with <code>TimeGMToLocalZone</code>
	 */
	public TIME toTIME() throws DominoException {
		return toTIME(TimeZone.getDefault());
	}
	
	/**
	 * It is the responsibility of the caller to free the returned {@link TIME} struct.
	 * 
	 * @param timeZone the time zone to use for conversion. If specified as <code>null</code>, then the zone
	 * 		is not considered during conversion 
	 * @return a {@link TIME} struct populated with this TIMEDATE and processed with <code>TimeGMToLocal</code>
	 * 	or <code>TimeGMToLocalZone</code>
	 * @throws DominoException
	 */
	public TIME toTIME(TimeZone timeZone) throws DominoException {
		_checkRefValidity();
		// NULL date
		if(getInnards(0)==0 && getInnards(1)==0) {
			return null;
		}
		TIME time = new TIME();
		if(timeZone == null) {
			C.memcpy(time.getDataPtr(), TIME._GM, getDataPtr(), 0, TIMEDATE.sizeOf);
			DominoAPI.get().TimeGMToLocal(time);
		} else {
			Calendar current = DominoNativeUtils.getReusableCalendarInstance(timeZone, true);
			time.setZone(-1 * (current.get(Calendar.ZONE_OFFSET) / 1000 / 60 / 60));
			// Say it's DST if there's an offset at all
			time.setDst((current.get(Calendar.DST_OFFSET) > 0) ? 1 : 0);
			C.memcpy(time.getDataPtr(), TIME._GM, getDataPtr(), 0, TIMEDATE.sizeOf);
			DominoAPI.get().TimeGMToLocalZone(time);
		}
		return time;
	}

	/* ******************************************************************************
	 * Java conversion
	 ********************************************************************************/
	
	public Date toJavaDate() throws DominoException {
		return new Date(toJavaMillis());
	}
	public Date toJavaDateCompat() throws DominoException {
		return new Date(toJavaMillisCompat());
	}
	public long toJavaMillis() throws DominoException {
		_checkRefValidity();
		// NULL date
		if(getInnards(0)==0 && getInnards(1)==0) {
			return 0;
		}
		TIME time = toTIME(DominoNativeUtils.TIMEZONE_UTC);
		try {
			Calendar c = DominoNativeUtils.getReusableCalendarInstance(DominoNativeUtils.TIMEZONE_UTC, true);
			if(!time.isTimeOnly()) {
				c.set(Calendar.YEAR, time.getYear());
				c.set(Calendar.MONTH, time.getMonth()-1);
				c.set(Calendar.DAY_OF_MONTH, time.getDay());
			} else {
				c.set(Calendar.YEAR, 0);
				c.set(Calendar.MONTH, 0);
				c.set(Calendar.DAY_OF_MONTH, 0);
			}
			if(!time.isDateOnly()) {
				c.set(Calendar.HOUR_OF_DAY, time.getHour());
				c.set(Calendar.MINUTE, time.getMinute());
				c.set(Calendar.SECOND, time.getSecond());
				c.set(Calendar.MILLISECOND, time.getHundredth()*10);
			} else {
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);
			}
			return c.getTimeInMillis();
		} finally {
			time.free();
		}
	}
	/**
	 * This method converts the TIMEDATE to an epoch timestamp using behavior based on the legacy
	 * Domino Java API: empty dates or times are based on the current day, rather than the epoch.
	 */
	public long toJavaMillisCompat() throws DominoException {
		_checkRefValidity();
		// NULL date
		if(getInnards(0)==0 && getInnards(1)==0) {
			return 0;
		}
		
		TIME time = toTIME();
		try {
			Calendar c = DominoNativeUtils.getReusableCalendarInstance(TimeZone.getDefault(), true);
			if(!time.isTimeOnly()) {
				c.set(Calendar.YEAR, time.getYear());
				c.set(Calendar.MONTH, time.getMonth()-1);
				c.set(Calendar.DAY_OF_MONTH, time.getDay());
			}
			if(!time.isDateOnly()) {
				c.set(Calendar.HOUR_OF_DAY, time.getHour());
				c.set(Calendar.MINUTE, time.getMinute());
				c.set(Calendar.SECOND, time.getSecond());
				c.set(Calendar.MILLISECOND, time.getHundredth()*10);
			} else {
				c.set(Calendar.MILLISECOND, 0);
			}
			return c.getTimeInMillis();
		} finally {
			time.free();
		}
	}
	
	/**
	 * Returns this date/time value as a {@link Calendar}. If the date/time expresses only date or time,
	 * the other component is filled in with the epoch.
	 * @throws DominoException 
	 */
	public Calendar toJavaCalendar() throws DominoException {
		TIME time = toTIME();
		try {
			int offset = time.getZone() * 60 * 60 * 1000;
			TimeZone zone = DominoNativeUtils.getTimeZoneForOffset(offset);
			
			Calendar result = DominoNativeUtils.getReusableCalendarInstance(zone, false);
			result.setTimeInMillis(0);
			if(!time.isTimeOnly()) {
				result.set(Calendar.YEAR, time.getYear());
				result.set(Calendar.MONTH, time.getMonth()-1);
				result.set(Calendar.DAY_OF_MONTH, time.getDay());
			}
			if(!time.isDateOnly()) {
				result.set(Calendar.HOUR_OF_DAY, time.getHour());
				result.set(Calendar.MINUTE, time.getMinute());
				result.set(Calendar.SECOND, time.getSecond());
				result.set(Calendar.MILLISECOND, time.getHundredth() * 10);
				
				result.set(Calendar.DST_OFFSET, time.getDst() > 0 ? (1 * 60 * 60 * 1000) : 0);
			}
			return result;
		} finally {
			time.free();
		}
	}
	
	public void fromJavaCalendar(Calendar c) throws DominoException {
		_checkRefValidity();
		
		TIME time = new TIME();
		try {
			time.setYear(c.get(Calendar.YEAR));
			time.setMonth(c.get(Calendar.MONTH) + 1);
			time.setDay(c.get(Calendar.DAY_OF_MONTH));
			time.setHour(c.get(Calendar.HOUR_OF_DAY));
			time.setMinute(c.get(Calendar.MINUTE));
			time.setSecond(c.get(Calendar.SECOND));
			
			time.setHundredth(c.get(Calendar.MILLISECOND) / 10);
			
			time.setZone(-1 * (c.get(Calendar.ZONE_OFFSET) / 1000 / 60 / 60));
			// Say it's DST if there's an offset at all
			time.setDst((c.get(Calendar.DST_OFFSET) > 0) ? 1 : 0);
			DominoAPI.get().TimeLocalToGM(time);
			C.memcpy(getDataPtr(), 0, time.getDataPtr(), TIME._GM, TIMEDATE.sizeOf);
		} finally {
			time.free();
		}
	}
	
	public void fromJavaDate(Date date) throws DominoException {
		fromJavaMillis(date.getTime());
	}
	public void fromJavaMillis(long date) throws DominoException {
		_checkRefValidity();
		// NULL date
		if(date==0) {
			setInnards(0, 0);
			setInnards(1, 0);
			return;
		}
		
		// This should be specifically rounded to the nearest hundredth of a second, up or down, to match Domino
		long roundedTime = (date + 5) / 10 * 10;
		
		Calendar c = DominoNativeUtils.getReusableCalendarInstance(DominoNativeUtils.TIMEZONE_UTC, false);
		c.setTimeInMillis(roundedTime);
		fromJavaCalendar(c);
	}
	
	/**
	 * @return whether the TIMEDATE value contains only date information (and its time is a wildcard)
	 */
	public boolean isDateOnly() throws DominoException {
		TIME time = toTIME();
		try {
			return time.isDateOnly();
		} finally {
			time.free();
		}
	}
	
	public void fromDateOnly(int year, int month, int day) throws DominoException {
		fromDateTime(year, month, day, -1, -1, -1, -1, 0, 0);
	}
	
	/**
	 * @return whether the TIMEDATE value contains only time information (and its date is a wildcard)
	 */
	public boolean isTimeOnly() throws DominoException {
		TIME time = toTIME();
		try {
			return time.isTimeOnly();
		} finally {
			time.free();
		}
	}
	
	public void fromTimeOnly(int hour, int minute, int second, int hundredth) throws DominoException {
		fromDateTime(-1, -1, -1, hour, minute, second, hundredth, 0, 0);
	}
	
	public void fromDateTime(int year, int month, int day, int hour, int minute, int second, int hundredth, int zone, int dst) throws DominoException {
		_checkRefValidity();
		
		TIME time = new TIME();
		try {
			time.setYear(year);
			time.setMonth(month);
			time.setDay(day);
			time.setHour(hour);
			time.setMinute(minute);
			time.setSecond(second);
			
			time.setHundredth(hundredth);
			
			time.setZone(zone);
			time.setDst(dst);
			DominoAPI.get().TimeLocalToGM(time);
			C.memcpy(getDataPtr(), 0, time.getDataPtr(), TIME._GM, TIMEDATE.sizeOf);
		} finally {
			time.free();
		}
	}
	
	/* ******************************************************************************
	 * Misc.
	 ********************************************************************************/
	
	@Override
	public int compareTo(TIMEDATE t2) {
		_checkRefValidity();
		if(t2 == null) {
			throw new NullPointerException();
		}
		t2._checkRefValidity();
		return DominoAPI.get().TimeDateCompare(this, t2);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof TIMEDATE)) {
			return false;
		}
		if(!this.isRefValid() || !((TIMEDATE)obj).isRefValid()) {
			return super.equals(obj);
		}
		
		return this.compareTo((TIMEDATE)obj) == 0;
	}
	
	@Override
	public String toString() {
		if(isRefValid()) {
			try {
				return StringUtil.format("[{0}: Date={1}]", //$NON-NLS-1$
						getClass().getSimpleName(),
						toJavaDate()
				);
			} catch(DominoException e) {
				return super.toString();
			}
		} else {
			return super.toString();
		}
	}
}