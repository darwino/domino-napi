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
package com.darwino.domino.napi.test.basics;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.struct.TIMEDATE;

public class NAPITimeTest {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", NAPITimeTest.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", NAPITimeTest.class.getSimpleName()); //$NON-NLS-1$
	}
	
	@Test
	public void testTimeCompare() throws DominoException {
		TIMEDATE t1 = new TIMEDATE();
		TIMEDATE t2 = new TIMEDATE();
		try {
			Date now = new Date();
			
			Calendar yesterday = Calendar.getInstance();
			yesterday.setTime(now);
			yesterday.add(Calendar.DAY_OF_YEAR, -1);
			
			Calendar tomorrow = Calendar.getInstance();
			tomorrow.setTime(now);
			tomorrow.add(Calendar.DAY_OF_YEAR, 1);
			
			// Test equality
			t1.fromJavaDate(now);
			t2.fromJavaDate(now);

			assertTrue("t1 and t2 should be equal via equals", t1.equals(t2)); //$NON-NLS-1$
			assertEquals("t1 and t2 should be equal via compareTo", 0, t1.compareTo(t2)); //$NON-NLS-1$
			
			// Test greater than
			t2.fromJavaCalendar(yesterday);
			
			assertFalse("t1 and t2 should be not equal via equals", t1.equals(t2)); //$NON-NLS-1$
			assertEquals("t1 should be greater than t2", 1, t1.compareTo(t2)); //$NON-NLS-1$
			
			// Test less than
			t2.fromJavaCalendar(tomorrow);
			
			assertFalse("t1 and t2 should be not equal via equals", t1.equals(t2)); //$NON-NLS-1$
			assertEquals("t1 should be less than t2", -1, t1.compareTo(t2)); //$NON-NLS-1$
			
		} finally {
			t1.free();
			t2.free();
		}
	}
	
	@SuppressWarnings("nls")
	@Test
	public void testTimeRoundTrip() throws DominoException {
		TIMEDATE time = new TIMEDATE();
		try {
			Date now = new Date();
			
			time.fromJavaDate(now);
			
			Date retrieved = time.toJavaDate();
			Platform.getInstance().log("now is {0}", now);
			Platform.getInstance().log("Retrieved is {0}", retrieved);
			long diff = Math.abs(retrieved.getTime() - now.getTime());
			assertTrue("Diff should be within 100ms, is " + diff, diff < 100);
		} finally {
			time.free();
		}
	}
	
	
	@Test
	public void testFromDateOnly() throws DominoException {
		TIMEDATE t = new TIMEDATE();
		try {
			int year = 2015;
			int month = 1;
			int day = 1;
			t.fromDateOnly(year, month, day);
			
			assertTrue("t should be date only", t.isDateOnly()); //$NON-NLS-1$
			assertFalse("t should not be time only", t.isTimeOnly()); //$NON-NLS-1$
			
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT")); //$NON-NLS-1$
			cal.setTime(t.toJavaDate());
			
			assertEquals("Year should be the same", year, cal.get(Calendar.YEAR)); //$NON-NLS-1$
			assertEquals("Month should be the same", month, cal.get(Calendar.MONTH)+1); //$NON-NLS-1$
			assertEquals("Day should be the same", day, cal.get(Calendar.DAY_OF_MONTH)); //$NON-NLS-1$
		} finally {
			t.free();
		}
	}
	
	@Test
	public void testFromTimeOnly() throws DominoException {
		TIMEDATE t = new TIMEDATE();
		try {
			int hour = 0;
			int minute = 1;
			int second = 15;
			int hundredth = 12;
			t.fromTimeOnly(hour, minute, second, hundredth);
			
			assertTrue("t should be time only", t.isTimeOnly()); //$NON-NLS-1$
			assertFalse("t should not be date only", t.isDateOnly()); //$NON-NLS-1$
			
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")); //$NON-NLS-1$
			cal.setTime(t.toJavaDate());
			
			assertEquals("Hour should be the same", hour, cal.get(Calendar.HOUR_OF_DAY)); //$NON-NLS-1$
			assertEquals("Minute should be the same", minute, cal.get(Calendar.MINUTE)); //$NON-NLS-1$
			assertEquals("second should be the same", second, cal.get(Calendar.SECOND)); //$NON-NLS-1$
			assertEquals("millis should be the same-ish", hundredth, cal.get(Calendar.MILLISECOND) / 10); //$NON-NLS-1$
		} finally {
			t.free();
		}
	}
	
	@Test
	public void testFreedTimeComparison() throws DominoException {
		Date d = new Date();
		TIMEDATE t1 = new TIMEDATE();
		try {
			t1.fromJavaDate(d);
			TIMEDATE t2 = new TIMEDATE();
			t2.fromJavaDate(d);
			
			assertTrue("t1 and t2 should be equal", t1.equals(t2));
			t2.free();
			
			assertFalse("t1 and t2 should no longer be equal", t1.equals(t2));
			assertTrue("t2 should still equal itself", t2.equals(t2));
		} finally {
			t1.free();
		}
		
	}
}
