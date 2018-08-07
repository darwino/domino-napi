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
package com.darwino.domino.napi.struct;

import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.c.C;



/**
 * Notes TIME struct.
 */
public class TIME extends BaseStruct {

	static {
		int[] sizes = new int[12];
		initNative(sizes);
		sizeOf = sizes[0];
		_year = sizes[1];
		_month = sizes[2];
		_day = sizes[3];
		_weekday = sizes[4];
		_hour = sizes[5];
		_minute = sizes[6];
		_second = sizes[7];
		_hundredth = sizes[8];
		_dst = sizes[9];
		_zone = sizes[10];
		_GM = sizes[11];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _year;
	public static final int _month;
	public static final int _day;
	public static final int _weekday;
	public static final int _hour;
	public static final int _minute;
	public static final int _second;
	public static final int _hundredth;
	public static final int _dst;
	public static final int _zone;
	public static final int _GM;
			
	public TIME() {
		super(C.calloc(1, sizeOf),true);
	}

	public TIME(/*pointer*/long pointer) {
		super(pointer,false);
	}

	public TIME(/*pointer*/long pointer, boolean owned) {
		super(pointer,owned);
	}
	
	public final int getYear() { return _getInt(_year); }
	public final void setYear(int value) { _setInt(_year, value); }
	
	public final int getMonth() { return _getInt(_month); }
	public final void setMonth(int value) { _setInt(_month, value); }
	
	public final int getDay() { return _getInt(_day); }
	public final void setDay(int value) { _setInt(_day, value); }
	
	public final int getWeekday() { return _getInt(_weekday); }
	public final void setWeekday(int value) { _setInt(_weekday, value); }
	
	public final int getHour() { return _getInt(_hour); }
	public final void setHour(int value) { _setInt(_hour, value); }
	
	public final int getMinute() { return _getInt(_minute); }
	public final void setMinute(int value) { _setInt(_minute, value); }
	
	public final int getSecond() { return _getInt(_second); }
	public final void setSecond(int value) { _setInt(_second, value); }
	
	public final int getHundredth() { return _getInt(_hundredth); }
	public final void setHundredth(int value) { _setInt(_hundredth, value); }

	public final int getDst() { return _getInt(_dst); }
	public final void setDst(int value) { _setInt(_dst, value); }

	public final int getZone() { return _getInt(_zone); }
	public final void setZone(int value) { _setInt(_zone, value); }
		
	public final TIMEDATE getGM() {
		return new TIMEDATE(getField(_GM)); 
	}
	public final void setGM(TIMEDATE td) {
		_checkRefValidity();
		C.memcpy(data,_GM,td.data,0,TIMEDATE.sizeOf);
	}
	
	public boolean isTimeOnly() {
		return getYear() == -1;
	}
	public boolean isDateOnly() {
		return getHour() == -1;
	}
	
	/* ******************************************************************************
	 * Misc.
	 ********************************************************************************/

	@Override
	public String toString() {
		if(isRefValid()) {
			return StringUtil.format("{0}-{1}-{2} {3}:{4}:{5} {6}", getYear(), getMonth(), getDay(), getHour(), getMinute(), getSecond(), getHundredth()*10); //$NON-NLS-1$
		} else {
			return super.toString();
		}
	}
}
