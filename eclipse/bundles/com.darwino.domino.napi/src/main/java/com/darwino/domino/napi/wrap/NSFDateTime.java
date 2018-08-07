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

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.enums.TimeConstant;
import com.darwino.domino.napi.struct.TIME;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.util.DominoNativeUtils;

/**
 * This class is a serialization-friendly wrapper for data from a {@link TIMEDATE} and does not
 * require a backing C struct.
 * 
 * <p>This representation is immutable; all methods that alter or translate its values return a
 * new object with the altered value.</p>
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 */
public class NSFDateTime implements Serializable, NSFTimeType, Comparable<NSFDateTime> {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a date-only NSFDateTime value. This is equivalent to using
	 * {@link #NSFDateTime(int, int, int)}, but may be preferred for readability.
	 * @param year
	 * @param month
	 * @param day
	 */
	public static NSFDateTime createDateOnly(int year, int month, int day) {
		return new NSFDateTime(year, month, day);
	}
	/**
	 * Constructs a time-only NSFDateTime value. This is equivalent to using
	 * {@link #NSFDateTime(int, int, int, int)}, but may be preferred for readability.
	 * @param year
	 * @param month
	 * @param day
	 */
	public static NSFDateTime createTimeOnly(int hour, int minute, int second, int hundredth) {
		return new NSFDateTime(hour, minute, second, hundredth);
	}
	
	/**
	 * Constructs an {@link NSFDateTime} value corresponding to the provided {@link TimeConstant}.
	 * @param timeConstant
	 * @return the newly-constructed <code>NSFDateTime</code>
	 * @throws DominoException if there is a lower-level-API problem determining the date/time
	 * @throws IllegalArgumentException if <code>timeConstant</code> is null
	 */
	public static NSFDateTime fromTimeConstant(TimeConstant timeConstant) throws DominoException {
		if(timeConstant == null) {
			throw new IllegalArgumentException("timeConstant cannot be null");
		}
		
		TIMEDATE timeDate = new TIMEDATE();
		try {
			timeDate.setToTimeConstant(timeConstant);
			return new NSFDateTime(timeDate);
		} finally {
			timeDate.free();
		}
	}
	
	/**
	 * Constructs an {@link NSFDateTime} value for the current time, according to the Domino API.
	 * @return the newly-constructed <code>NSFDateTime</code>
	 * @throws DominoException if there is a lower-level-API problem determining the date/time
	 */
	public static NSFDateTime now() throws DominoException {
		TIMEDATE timeDate = new TIMEDATE();
		try {
			DominoAPI.get().OSCurrentTIMEDATE(timeDate);
			return new NSFDateTime(timeDate);
		} finally {
			timeDate.free();
		}
	}
	
	
	private final int year;       // 1-32767
	private final int month;      // 1-12
	private final int day;        // 1-31
	private final int hour;       // 1-7 (Sunday is 1)
	private final int minute;     // 0-59
	private final int second;     // 0-59
	private final int hundredth;  // 0-99
	private final boolean dst;
	private final int zone;       // -11 - +11
	
	private final int innards1;
	private final int innards2;
	
	/**
	 * Constructs a new NSFDateTime based on the current time and time-zone settings, according tp
	 * {@link Calendar#getInstance()}.
	 */
	public NSFDateTime() {
		this(Calendar.getInstance());
	}
	
	public NSFDateTime(Date date) {
		Calendar cal = DominoNativeUtils.getReusableCalendarInstance(DominoNativeUtils.TIMEZONE_UTC, false);
		cal.setTime(date);
		this.year = cal.get(Calendar.YEAR);
		this.month = cal.get(Calendar.MONTH)+1;
		this.day = cal.get(Calendar.DAY_OF_MONTH);
		this.hour = cal.get(Calendar.HOUR_OF_DAY);
		this.minute = cal.get(Calendar.MINUTE);
		this.second = cal.get(Calendar.SECOND);
		this.hundredth = cal.get(Calendar.MILLISECOND) / 10;
		this.dst = false;
		this.zone = 0;
		
		this.innards1 = -1;
		this.innards2 = -1;
	}
	
	public NSFDateTime(Calendar cal) {
		this.year = cal.get(Calendar.YEAR);
		this.month = cal.get(Calendar.MONTH)+1;
		this.day = cal.get(Calendar.DAY_OF_MONTH);
		this.hour = cal.get(Calendar.HOUR_OF_DAY);
		this.minute = cal.get(Calendar.MINUTE);
		this.second = cal.get(Calendar.SECOND);
		this.hundredth = cal.get(Calendar.MILLISECOND) / 10;
		this.dst = cal.get(Calendar.DST_OFFSET) != 0;
		this.zone = millisOffsetToNotes(cal.get(Calendar.ZONE_OFFSET));
		
		this.innards1 = -1;
		this.innards2 = -1;
	}
	
	public NSFDateTime(TIME time) {
		this.year = time.getYear();
		this.month = time.getMonth();
		this.day = time.getDay();
		this.hour = time.getHour();
		this.minute = time.getMinute();
		this.second = time.getSecond();
		this.hundredth = time.getHundredth();
		this.dst = time.getDst() == 1;
		this.zone = time.getZone();
		
		this.innards1 = -1;
		this.innards2 = -1;
	}
	public NSFDateTime(TIMEDATE timedate) throws DominoException {
		this.innards1 = timedate.getInnards(0);
		this.innards2 = timedate.getInnards(1);
		
		TIME time = timedate.toTIME(null);
		if(time != null) {
			try {
				this.year = time.getYear();
				this.month = time.getMonth();
				this.day = time.getDay();
				this.hour = time.getHour();
				this.minute = time.getMinute();
				this.second = time.getSecond();
				this.hundredth = time.getHundredth();
				this.dst = time.getDst() == 1;
				this.zone = time.getZone();
			} finally {
				time.free();
			}
		} else {
			this.year = -1;
			this.month = -1;
			this.day = -1;
			this.hour = -1;
			this.minute = -1;
			this.second = -1;
			this.hundredth = -1;
			this.dst = false;
			this.zone = 0;
		}
	}
	
	/**
	 * Constructs a date-only NSFDateTime value.
	 * @param year
	 * @param month
	 * @param day
	 */
	public NSFDateTime(int year, int month, int day) {
		if(month < 1 || month > 12) {
			throw new IllegalArgumentException("month value of '" + month + "' is not between 1 and 12, inclusive");
		}
		if(day < 1 || day > 32) {
			throw new IllegalArgumentException("day value of '" + day + "' is not between 1 and 31, inclusive");
		}
		
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = -1;
		this.minute = -1;
		this.second = -1;
		this.hundredth = -1;
		this.dst = false;
		this.zone = 0;
		
		this.innards1 = -1;
		this.innards2 = -1;
	}
	
	/**
	 * Constructs a time-only NSFDateTime value.
	 * 
	 * @param hour
	 * @param minute
	 * @param second
	 * @param hundredth
	 */
	public NSFDateTime(int hour, int minute, int second, int hundredth) {
		if(hour < 0 || hour > 23) {
			throw new IllegalArgumentException("hour value of '" + hour+ "' is not between 0 and 23, inclusive");
		}
		if(minute < 0 || minute > 59) {
			throw new IllegalArgumentException("minute value of '" + minute + "' is not between 0 and 59, inclusive");
		}
		if(second < 0 || second > 59) {
			throw new IllegalArgumentException("second value of '" + second + "' is not between 0 and 59, inclusive");
		}
		if(hundredth < 0 || hundredth > 99) {
			throw new IllegalArgumentException("hundredth value of '" + hundredth + "' is not between 0 and 99, inclusive");
		}
		
		this.year = -1;
		this.month = -1;
		this.day = -1;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.hundredth = hundredth;
		this.dst = false;
		this.zone = 0;
		
		this.innards1 = -1;
		this.innards2 = -1;
	}
	
	/* ******************************************************************************
	 * Component getters
	 ********************************************************************************/
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}
	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}
	/**
	 * @return the hour
	 */
	public int getHour() {
		return hour;
	}
	/**
	 * @return the minute
	 */
	public int getMinute() {
		return minute;
	}
	/**
	 * @return the second
	 */
	public int getSecond() {
		return second;
	}
	/**
	 * @return the hundredth
	 */
	public int getHundredth() {
		return hundredth;
	}
	/**
	 * This method returns the time zone offset using Notes's notation. This is interesting in two ways:
	 * 
	 * <ul>
	 * 	<li>The offset is +/- reversed from the actual UTC offset.</li>
	 * 	<li>For non-integer hour offsets, the number is expressed as "XXYY", where "XX" is the minute
	 * 		offset and "YY" is the hour.</li>
	 * </ul>
	 * 
	 * @return the zone, using Notes's notation
	 */
	public int getZone() {
		return zone;
	}
	/**
	 * @return the dst
	 */
	public boolean isDst() {
		return dst;
	}
	
	/* ******************************************************************************
	 * Conversions
	 ********************************************************************************/
	
	/**
	 * Returns this date/time value as a {@link Calendar}. If the date/time expresses only date or time,
	 * the other component is filled in with the epoch.
	 */
	public Calendar toCalendar() {
		int offset = notesZoneToMillis(this.zone);
		TimeZone zone = DominoNativeUtils.getTimeZoneForOffset(offset);
		
		Calendar result = Calendar.getInstance(zone);
		result.setTimeInMillis(0);
		if(!this.isAnyDate()) {
			result.set(Calendar.YEAR, this.year);
			result.set(Calendar.MONTH, this.month-1);
			result.set(Calendar.DAY_OF_MONTH, this.day);
		}
		if(!this.isAnyTime()) {
			result.set(Calendar.HOUR_OF_DAY, this.hour);
			result.set(Calendar.MINUTE, this.minute);
			result.set(Calendar.SECOND, this.second);
			result.set(Calendar.MILLISECOND, this.hundredth * 10);
			result.set(Calendar.DST_OFFSET, this.dst ? (1 * 60 * 60 * 1000) : 0);
			result.set(Calendar.ZONE_OFFSET, offset);
		}
		return result;
	}
	
	public Date toDate() {
		return toCalendar().getTime();
	}
	
	/**
	 * @return whether the NSFDateTime value's time is a wildcard
	 */
	public boolean isAnyTime() {
		return this.hour == -1;
	}
	public NSFDateTime toDateOnly() {
		if(isAnyDate()) {
			throw new IllegalStateException("Cannot create a date-only NSFDateTime from a time-only value");
		}
		return new NSFDateTime(year, month, day);
	}
	
	/**
	 * @return whether the NSFDateTime value's date is a wildcard value
	 */
	public boolean isAnyDate() {
		return this.year == -1;
	}
	public NSFDateTime toTimeOnly() {
		if(isAnyTime()) {
			throw new IllegalStateException("Cannot create a time-only NSFDateTime from a date-only value");
		}
		return new NSFDateTime(hour, minute, second, hundredth);
	}
	
	/**
	 * Creates a <code>TIMEDATE</code> struct representing the current value. The caller is responsible
	 * for freeing this struct.
	 * 
	 * @return a <code>TIMEDATE</code> struct representing the current value
	 * @throws DominoException if there is a lower-level-API problem constructing the struct
	 */
	public TIMEDATE toTIMEDATE() throws DominoException {
		TIMEDATE timeDate = new TIMEDATE();
		timeDate.fromDateTime(year, month, day, hour, minute, second, hundredth, zone, dst ? 1 : 0);
		return timeDate;
	}
	
	/**
	 * @return whether the NSFDateTime value contains neither time nor date information
	 */
	public boolean isNullValue() {
		return isAnyTime() && isAnyDate();
	}
	
	/**
	 * Determines whether this object has valid "innards" values, which are
	 * set when the object is created from a {@link TIMEDATE}.
	 * 
	 * @return whether this object's "innards" values are specified
	 */
	public boolean hasInnards() {
		return innards1 != -1 && innards2 != -1;
	}
	
	/**
	 * @return the value of the first "innards" slot, or <code>-1</code> if this object
	 * 	was not created from a {@link TIMEDATE}
	 */
	public int getInnards1() {
		return innards1;
	}
	/**
	 * @return the value of the second "innards" slot, or <code>-1</code> if this object
	 * 	was not created from a {@link TIMEDATE}
	 */
	public int getInnards2() {
		return innards2;
	}
	
	/* ******************************************************************************
	 * Misc.
	 ********************************************************************************/
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return StringUtil.format("[{0}: year={1}, month={2}, day={3}, hour={4}, minute={5}, second={6}, hundredth={7}, zone={8}, dst={9}]", //$NON-NLS-1$
				getClass().getSimpleName(),
				getYear(), getMonth(), getDay(),
				getHour(), getMinute(), getSecond(), getHundredth(),
				getZone(), isDst()
		);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof NSFDateTime)) {
			return false;
		}
		return StringUtil.equals(toString(), obj.toString());
	}
	
	@Override
	public int hashCode() {
		// Thanks, Effective Java!
		
		int result = getClass().getName().length();
		
		result = 31 * result + (year);
		result = 31 * result + (month);
		result = 31 * result + (day);
		result = 31 * result + (hour);
		result = 31 * result + (minute);
		result = 31 * result + (second);
		result = 31 * result + (hundredth);
		result = 31 * result + (dst ? 1 : 0);
		result = 31 * result + (zone);
		
		result = 31 * result + (innards1);
		result = 31 * result + (innards2);
		
		return result;
	}
	
	@Override
	public int compareTo(NSFDateTime o) {
		if(o == null) {
			return 1;
		}
		
		return toDate().compareTo(o.toDate());
	}
	
	/* ******************************************************************************
	 * Internal utility methods
	 ********************************************************************************/
	
	/**
	 * Converts a "Notes-style" time zone (either an integer from -11 to 11 or a thousands-size
	 * integer with the minutes in the first two digits, and +/- reversed) to Calendar-style
	 * milliseconds offset.
	 */
	private int notesZoneToMillis(int zone) {
		int abs = Math.abs(zone);
		int multiplier = zone < 0 ? 1 : -1;
		if(abs > 100) {
			// For non-integer hour offsets, Notes uses a convention of
			// XXYY, where "XX" are the minutes offset and "YY" are the hours
			int minutes = abs / 100;
			int hours = abs % 100;
			return multiplier * (hours * 60 * 60 * 1000 + minutes * 60 * 1000);
		} else {
			return multiplier * (zone * 60 * 60 * 1000);
		}
	}
	/**
	 * Converts a Calendar-style milliseconds offset to a a "Notes-style" time zone (either an integer
	 * from -11 to 11 or a thousands-size integer with the minutes in the first two digits,
	 * and +/- reversed).
	 */
	private int millisOffsetToNotes(int offset) {
		int abs = Math.abs(offset);
		int multiplier = offset < 0 ? 1 : -1;
		
		int hours = abs / 1000 / 60 / 60;
		int minutePortion = abs % (60 * 60 * 1000) / 1000 / 60;
		if(minutePortion != 0) {
			// Then compose the "XXYY"-style offset
			return multiplier * (minutePortion * 100 + hours);
		} else {
			return multiplier * hours;
		}
	}
}
