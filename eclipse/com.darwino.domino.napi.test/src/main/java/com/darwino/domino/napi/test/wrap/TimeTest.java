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

package com.darwino.domino.napi.test.wrap;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.util.DominoNativeUtils;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFDateRange;
import com.darwino.domino.napi.wrap.NSFDateTime;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.NSFTimeType;

/**
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
@SuppressWarnings("nls")
public class TimeTest extends AbstractNoteTest {
	@Test
	public void testUtilDate() throws Exception {
		NSFNote note = database.createNote();
		try {
			Date now = new Date();
			note.set("SomeDate", now); //$NON-NLS-1$
			
			Date result = note.get("SomeDate", Date.class); //$NON-NLS-1$
			assertTrue("Dates should be within 100ms", Math.abs(now.getTime() - result.getTime()) < 100); //$NON-NLS-1$
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testCalendarGMT() throws Exception {
		NSFNote note = database.createNote();
		try {
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC")); //$NON-NLS-1$
			note.set("SomeDate", now); //$NON-NLS-1$
			
			Calendar result = note.get("SomeDate", Calendar.class); //$NON-NLS-1$
			assertTrue("Dates should be within 100ms", Math.abs(now.getTimeInMillis() - result.getTimeInMillis()) < 100); //$NON-NLS-1$
			assertEquals("Time zone offsets should be equal", result.getTimeZone().getRawOffset(), now.getTimeZone().getRawOffset()); //$NON-NLS-1$
			assertEquals("Time zone offset should be 0", 0, result.getTimeZone().getRawOffset()); //$NON-NLS-1$
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testCalendarET() throws Exception {
		NSFNote note = database.createNote();
		try {
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York")); //$NON-NLS-1$
			note.set("SomeDate", now); //$NON-NLS-1$
			
			Calendar result = note.get("SomeDate", Calendar.class); //$NON-NLS-1$
			assertTrue("Dates should be within 100ms", Math.abs(now.getTimeInMillis() - result.getTimeInMillis()) < 100); //$NON-NLS-1$
			assertEquals("Time zone offsets should be equal", result.getTimeZone().getRawOffset(), now.getTimeZone().getRawOffset()); //$NON-NLS-1$
			assertEquals("Time zone offset should be -5h", -5 * 60 * 60 * 1000, result.getTimeZone().getRawOffset()); //$NON-NLS-1$
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testDateOnly() throws Exception {
		NSFNote note = database.createNote();
		try {
			int year = 2015;
			int month = 1;
			int day = 1;
			
			NSFDateTime date = NSFDateTime.createDateOnly(year, month, day);
			assertEquals(year, date.getYear());
			assertEquals(month, date.getMonth());
			assertEquals(day, date.getDay());
			assertTrue(date.isAnyTime());
			note.set("SomeDate", date); //$NON-NLS-1$
			
			NSFDateTime noteDate = note.get("SomeDate", NSFDateTime.class); //$NON-NLS-1$
			Platform.getInstance().log("testDateOnly: got date: {0}", noteDate); //$NON-NLS-1$
			assertTrue(noteDate.isAnyTime());
			assertEquals(date.getYear(), noteDate.getYear());
			assertEquals(date.getMonth(), noteDate.getMonth());
			assertEquals(date.getDay(), noteDate.getDay());
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testTimeOnly() throws Exception {
		NSFNote note = database.createNote();
		try {
			int hour = 0;
			int minute = 1;
			int second = 2;
			int hundredth = 3;
			NSFDateTime date = NSFDateTime.createTimeOnly(hour, minute, second, hundredth);
			assertEquals(hour, date.getHour());
			assertEquals(minute, date.getMinute());
			assertEquals(second, date.getSecond());
			assertEquals(hundredth, date.getHundredth());
			assertTrue(date.isAnyDate());
			note.set("SomeDate", date); //$NON-NLS-1$
			
			NSFDateTime noteDate = note.get("SomeDate", NSFDateTime.class); //$NON-NLS-1$
			Platform.getInstance().log("testTimeOnly: got date: {0}", noteDate); //$NON-NLS-1$
			assertTrue(noteDate.isAnyDate());
			assertEquals(date.getHour(), noteDate.getHour());
			assertEquals(date.getMinute(), noteDate.getMinute());
			assertEquals(date.getSecond(), noteDate.getSecond());
			assertEquals(date.getHundredth(), noteDate.getHundredth());
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testReadDateOnly() throws Exception {
		NSFDatabase database = session.getDatabase(AllTests.FORUM_DOC_DB_NAME);
		try {
			NSFNote note = database.getNoteByUNID(AllTests.FORUM_DOC_DOUBLE_ATTACHMENT_UNID);
			try {
				// 2015-12-12
				NSFDateTime noteDate = note.get("CreationDateOnly", NSFDateTime.class); //$NON-NLS-1$
				Platform.getInstance().log("CreationDateOnly is {0}", noteDate); //$NON-NLS-1$
				assertTrue("noteDate should be date only", noteDate.isAnyTime()); //$NON-NLS-1$
				assertFalse("noteDate should not be time only", noteDate.isAnyDate()); //$NON-NLS-1$
				assertEquals(2015, noteDate.getYear());
				assertEquals(12, noteDate.getMonth());
				assertEquals(12, noteDate.getDay());
				
				NSFDateTime dateOnly = noteDate.toDateOnly();
				assertTrue(dateOnly.isAnyTime());
				assertEquals(2015, dateOnly.getYear());
				assertEquals(12, dateOnly.getMonth());
				assertEquals(12, dateOnly.getDay());
			} finally {
				note.free();
			}
		} finally {
			database.free();
		}
	}
	
	@Test
	public void testReadTimeOnly() throws Exception {
		NSFDatabase database = session.getDatabase(AllTests.FORUM_DOC_DB_NAME);
		try {
			NSFNote note = database.getNoteByUNID(AllTests.FORUM_DOC_DOUBLE_ATTACHMENT_UNID);
			try {
				// 11:23:21 AM
				NSFDateTime noteDate = note.get("CreationTimeOnly", NSFDateTime.class); //$NON-NLS-1$
				Platform.getInstance().log("CreationTimeOnly is {0}", noteDate); //$NON-NLS-1$
				assertTrue("noteDate should be time only", noteDate.isAnyDate()); //$NON-NLS-1$
				assertFalse("noteDate should not be date only", noteDate.isAnyTime()); //$NON-NLS-1$
				assertEquals(11, noteDate.getHour());
				assertEquals(23, noteDate.getMinute());
				assertEquals(21, noteDate.getSecond());
				
				NSFDateTime timeOnly = noteDate.toTimeOnly();
				assertTrue(timeOnly.isAnyDate());
				assertEquals(11, timeOnly.getHour());
				assertEquals(23, timeOnly.getMinute());
				assertEquals(21, timeOnly.getSecond());
			} finally {
				note.free();
			}
		} finally {
			database.free();
		}
	}
	
	@Test
	public void testNsfDateTimeDateOnly() throws Exception {
		NSFDateTime dt = new NSFDateTime(2015, 1, 2);
		
		assertTrue(dt.isAnyTime());
		assertFalse(dt.isAnyDate());
		
		assertEquals(2015, dt.getYear());
		assertEquals(1, dt.getMonth());
		assertEquals(2, dt.getDay());
	}
	
	@Test
	public void testNsfDateTimeTimeOnly() throws Exception {
		NSFDateTime dt = new NSFDateTime(12, 0, 1, 51);
		
		assertTrue(dt.isAnyDate());
		assertFalse(dt.isAnyTime());
		
		assertEquals(12, dt.getHour());
		assertEquals(0, dt.getMinute());
		assertEquals(1, dt.getSecond());
		assertEquals(51, dt.getHundredth());
	}
	
	@Test
	public void testSetDateOnly() throws Exception {
		NSFDateTime dateOnly = NSFDateTime.createDateOnly(2015, 1, 1);
		NSFNote note = database.createNote();
		try {
			note.set("DateOnly", dateOnly); //$NON-NLS-1$
			NSFDateTime dt = note.get("DateOnly", NSFDateTime.class); //$NON-NLS-1$
			
			assertTrue(dt.isAnyTime());
			assertFalse(dt.isAnyDate());
			assertEquals(dateOnly, dt.toDateOnly());
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testSetTimeOnly() throws Exception {
		NSFDateTime timeOnly = NSFDateTime.createTimeOnly(12, 0, 0, 51);
		NSFNote note = database.createNote();
		try {
			note.set("TimeOnly", timeOnly); //$NON-NLS-1$
			NSFDateTime dt = note.get("TimeOnly", NSFDateTime.class); //$NON-NLS-1$
			
			assertTrue(dt.isAnyDate());
			assertFalse(dt.isAnyTime());
			assertEquals(timeOnly, dt.toTimeOnly());
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testSetDateTimeRange() throws Exception {
		Calendar now = Calendar.getInstance();
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_YEAR, -1);
		
		NSFDateRange range = new NSFDateRange(yesterday, now);
		
		NSFNote note = database.createNote();
		try {
			note.set("DateRange", range);
			NSFDateRange nsfRange = note.get("DateRange", NSFDateRange.class);
			Platform.getInstance().log("testSetDateTimeRange: Got DateRange: {0}", nsfRange);
			
			assertNotEquals("nsfRange should not be null", null, nsfRange);
			assertEquals("nsfRange should equal range", range, nsfRange);
			long diff = Math.abs(yesterday.getTimeInMillis() - nsfRange.getLower().toCalendar().getTimeInMillis());
			assertTrue("Lower dates should be within 100ms, is " + diff, diff < 100); //$NON-NLS-1$
			diff = Math.abs(now.getTimeInMillis() - nsfRange.getUpper().toCalendar().getTimeInMillis());
			assertTrue("Upper dates should be within 100ms, is " + diff, diff < 100); //$NON-NLS-1$
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testSetDateTimeRangeZones() throws Exception {
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
		Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		yesterday.add(Calendar.DAY_OF_YEAR, -1);
		
		NSFDateRange range = new NSFDateRange(yesterday, now);
		
		NSFNote note = database.createNote();
		try {
			note.set("DateRange", range);
			NSFDateRange nsfRange = note.get("DateRange", NSFDateRange.class);
			Platform.getInstance().log("testSetDateTimeRangeZones: Got DateRange: {0}", nsfRange);
			
			assertNotEquals("nsfRange should not be null", null, nsfRange);
			assertEquals("nsfRange should equal range", range, nsfRange);
			long diff = Math.abs(yesterday.getTimeInMillis() - nsfRange.getLower().toCalendar().getTimeInMillis());
			assertTrue("Lower dates should be within 100ms, is " + diff, diff < 100); //$NON-NLS-1$
			diff = Math.abs(now.getTimeInMillis() - nsfRange.getUpper().toCalendar().getTimeInMillis());
			assertTrue("Upper dates should be within 100ms, is " + diff, diff < 100); //$NON-NLS-1$
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testSetDateTimeRangeZonesDST() throws Exception {
		Calendar nextYear = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
		
		// Should definitely be DST
		nextYear.add(Calendar.YEAR, 1);
		nextYear.set(Calendar.MONTH, 5);
		nextYear.set(Calendar.DAY_OF_MONTH, 20);
		
		Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		yesterday.add(Calendar.DAY_OF_YEAR, -1);
		
		NSFDateRange range = new NSFDateRange(yesterday, nextYear);
		
		NSFNote note = database.createNote();
		try {
			note.set("DateRange", range);
			NSFDateRange nsfRange = note.get("DateRange", NSFDateRange.class);
			Platform.getInstance().log("testSetDateTimeRangeZonesDST: Got DateRange: {0}", nsfRange);
			
			assertNotEquals("nsfRange should not be null", null, nsfRange);
			assertEquals("nsfRange should equal range", range, nsfRange);
			assertTrue("Upper date should be DST", nsfRange.getUpper().isDst());
			long diff = Math.abs(yesterday.getTimeInMillis() - nsfRange.getLower().toCalendar().getTimeInMillis());
			assertTrue("Lower dates should be within 100ms, is " + diff, diff < 100); //$NON-NLS-1$
			diff = Math.abs(nextYear.getTimeInMillis() - nsfRange.getUpper().toCalendar().getTimeInMillis());
			assertTrue("Upper dates should be within 100ms, is " + diff, diff < 100); //$NON-NLS-1$
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testSetDateTimeRangeZonesNonDST() throws Exception {
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
		
		// Should definitely be non-DST
		now.set(Calendar.YEAR, 2016);
		now.set(Calendar.MONTH, 0);
		now.set(Calendar.DAY_OF_MONTH, 20);
		
		Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		yesterday.add(Calendar.DAY_OF_YEAR, -1);
		
		NSFDateRange range = new NSFDateRange(yesterday, now);
		
		NSFNote note = database.createNote();
		try {
			note.set("DateRange", range);
			NSFDateRange nsfRange = note.get("DateRange", NSFDateRange.class);
			Platform.getInstance().log("testSetDateTimeRangeZonesDST: Got DateRange: {0}", nsfRange);
			
			assertNotEquals("nsfRange should not be null", null, nsfRange);
			assertEquals("nsfRange should equal range", range, nsfRange);
			assertFalse("Upper date should not be DST", nsfRange.getUpper().isDst());
			long diff = Math.abs(yesterday.getTimeInMillis() - nsfRange.getLower().toCalendar().getTimeInMillis());
			assertTrue("Lower dates should be within 100ms, is " + diff, diff < 100); //$NON-NLS-1$
			diff = Math.abs(now.getTimeInMillis() - nsfRange.getUpper().toCalendar().getTimeInMillis());
			assertTrue("Upper dates should be within 100ms, is " + diff, diff < 100); //$NON-NLS-1$
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testSetNSFDateOnlyRange() throws Exception {
		System.out.println("testSetNSFDateOnlyRange start");
		
		NSFDateTime start = NSFDateTime.createDateOnly(2015, 1, 1);
		System.out.println("Created start " + start);
		NSFDateTime end = NSFDateTime.createDateOnly(2016, 2, 2);
		System.out.println("Created end " + end);

		NSFDateRange range = new NSFDateRange(start, end);
		System.out.println("Created range " + range);
		
		NSFNote note = database.createNote();
		try {
			note.set("DateRange", range);
			NSFDateRange nsfRange = note.get("DateRange", NSFDateRange.class);
			
			assertNotEquals("nsfRange should not be null", null, nsfRange);
			assertEquals("nsfRange should equal range", range, nsfRange);
			assertEquals("lower should be start", start, nsfRange.getLower().toDateOnly());
			assertEquals("upper should be end", end, nsfRange.getUpper().toDateOnly());
		} finally {
			note.free();
		}
		
		System.out.println("testSetNSFDateOnlyRange end");
	}
	
	@Test
	public void testSetDateOnlyRange() throws Exception {
		NSFDateTime start = NSFDateTime.createDateOnly(2015, 1, 1);
		NSFDateTime end = NSFDateTime.createDateOnly(2016, 2, 2);
		
		NSFDateRange range = new NSFDateRange(start, end);
		
		NSFNote note = database.createNote();
		try {
			note.set("DateRange", range);
			NSFDateRange nsfRange = note.get("DateRange", NSFDateRange.class);
			
			assertNotEquals("nsfRange should not be null", null, nsfRange);
			assertEquals("nsfRange should equal range", range, nsfRange);
			assertEquals("lower should be start", start, nsfRange.getLower());
			assertEquals("upper should be end", end, nsfRange.getUpper());
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testSetNSFTimeOnlyRange() throws Exception {
		NSFDateTime start = NSFDateTime.createTimeOnly(12, 0, 0, 0);
		NSFDateTime end = NSFDateTime.createTimeOnly(15, 1, 1, 0);
		
		NSFDateRange range = new NSFDateRange(start, end);
		
		NSFNote note = database.createNote();
		try {
			note.set("DateRange", range);
			NSFDateRange nsfRange = note.get("DateRange", NSFDateRange.class);
			
			assertNotEquals("nsfRange should not be null", null, nsfRange);
			assertEquals("nsfRange should equal range", range, nsfRange);
			assertEquals("lower should be start", start, nsfRange.getLower().toTimeOnly());
			assertEquals("upper should be end", end, nsfRange.getUpper().toTimeOnly());
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testSetTimeOnlyRange() throws Exception {
		NSFDateTime start = NSFDateTime.createTimeOnly(12, 0, 0, 0);
		NSFDateTime end = NSFDateTime.createTimeOnly(15, 1, 1, 0);
		
		NSFDateRange range = new NSFDateRange(start, end);
		
		NSFNote note = database.createNote();
		try {
			note.set("DateRange", range);
			NSFDateRange nsfRange = note.get("DateRange", NSFDateRange.class);
			
			assertNotEquals("nsfRange should not be null", null, nsfRange);
			assertEquals("nsfRange should equal range", range, nsfRange);
			assertEquals("lower should be start", start, range.getLower());
			assertEquals("upper should be end", end, range.getUpper());
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testSetDateTimeArray() throws Exception {
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
		Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		yesterday.add(Calendar.DAY_OF_YEAR, -1);
		
		NSFDateTime val1 = new NSFDateTime(now);
		NSFDateTime val2 = new NSFDateTime(yesterday);
		
		NSFNote note = database.createNote();
		try {
			note.set("Dates", new Object[] { val1, val2 });
			NSFDateTime[] nsfVals = note.get("Dates", NSFDateTime[].class);
			
			assertNotEquals("nsfVals should not be null", null, nsfVals);
			assertEquals("nsfVals should have two entries", 2, nsfVals.length);
			assertEquals("nsfVals[0] should equal val1", val1, nsfVals[0]);
			assertEquals("nsfVals[1] should equal val2", val2, nsfVals[1]);
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testSetDateTimeAndRangeArray() throws Exception {
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
		Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		yesterday.add(Calendar.DAY_OF_YEAR, -1);
		
		NSFDateRange val0 = new NSFDateRange(yesterday, now);
		NSFDateTime val1 = new NSFDateTime(now);
		NSFDateTime val2 = new NSFDateTime(yesterday);
		
		NSFNote note = database.createNote();
		try {
			note.set("Dates", new Object[] { val0, val1, val2 });
			NSFTimeType[] nsfVals = note.get("Dates", NSFTimeType[].class);
			
			assertNotEquals("nsfVals should not be null", null, nsfVals);
			assertEquals("nsfVals should have three entries", 3, nsfVals.length);
			// NSF always stores ranges after date/times, regardless of initial order
			assertEquals("nsfVals[0] should equal val1", val1, nsfVals[0]);
			assertEquals("nsfVals[1] should equal val2", val2, nsfVals[1]);
			assertEquals("nsfVals[2] should be val0", val0, nsfVals[2]);
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testSetMixAndRangeArray() throws Exception {
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
		Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		yesterday.add(Calendar.DAY_OF_YEAR, -1);
		NSFDateTime noon = new NSFDateTime(12, 0, 0, 0);
		
		NSFDateRange val0 = new NSFDateRange(yesterday, now);
		
		NSFNote note = database.createNote();
		try {
			note.set("Dates", new Object[] { val0, now, noon, yesterday });
			NSFTimeType[] nsfVals = note.get("Dates", NSFTimeType[].class);
			
			assertNotEquals("nsfVals should not be null", null, nsfVals);
			assertEquals("nsfVals should have three entries", 4, nsfVals.length);
			// NSF always stores ranges after date/times, regardless of initial order
			assertEquals("nsfVals[0] should be an NSFDateTime", NSFDateTime.class, nsfVals[0].getClass());
			long diff = Math.abs(now.getTimeInMillis() - ((NSFDateTime)nsfVals[0]).toCalendar().getTimeInMillis());
			assertTrue("nsfVals[0] should equal be within 100ms of now, is " + diff, diff < 100);
			assertEquals("nsfVals[1] should be an NSFDateTime", NSFDateTime.class, nsfVals[1].getClass());
			assertEquals("nsfVals[1] should equal noon", noon, ((NSFDateTime)nsfVals[1]).toTimeOnly());
			assertEquals("nsfVals[2] should be an NSFDateTime", NSFDateTime.class, nsfVals[2].getClass());
			diff = Math.abs(yesterday.getTimeInMillis() - ((NSFDateTime)nsfVals[2]).toCalendar().getTimeInMillis());
			assertTrue("nsfVals[2] should be within 100ms of yesterday, is " + diff, diff < 100);
			assertEquals("nsfVals[3] should equal val0", val0, nsfVals[3]);
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testHalfHourOffset() throws Exception {
		System.out.println("testHalfHourOffset start");
		
		NSFNote note = mimeDocs.getNoteByUNID(AllTests.MIMEDOCS_HALF_HOUR_TZ_UNID);
		try {
			NSFDateTime dt = note.get("TimeZoneTest", NSFDateTime.class);
			assertNotEquals("dt should not be null", null, dt);
			
			// Check the basics
			// 09/28/2004 08:56:01 AM ZE5B
			assertEquals(2004, dt.getYear());
			assertEquals(9, dt.getMonth());
			assertEquals(28, dt.getDay());
			assertEquals(8, dt.getHour());
			assertEquals(56, dt.getMinute());
			assertEquals(1, dt.getSecond());
			
			Calendar cal = dt.toCalendar();
			assertEquals("TZ offset should be 5.5 hours", (int)(5.5 * 60 * 60 * 1000), cal.get(Calendar.ZONE_OFFSET));
		} finally {
			note.free();
		}
		System.out.println("testHalfHourOffset end");
	}
	
	@Test
	public void testCreateHalfHourOffset() throws Exception {
		System.out.println("testCreateHalfHourOffset start");
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.ZONE_OFFSET, (int)(5.5 * 60 * 60 * 1000));
		System.out.println("Cal offset was set to " + cal.get(Calendar.ZONE_OFFSET));
		
		NSFDateTime dt = new NSFDateTime(cal);
		assertEquals("zone should be -3005", -3005, dt.getZone());

		System.out.println("testCreateHalfHourOffset end");
	}
	
	@Test
	public void testDoubleWildcard() throws Exception {
		System.out.println("testDoubleWildcard start");
		
		NSFNote note = forumDocs.getNoteByUNID(AllTests.FORUM_DOC_WILDCARD_DTS_UNID);
		try {
			NSFDateTime dt = note.get("AnyDate", NSFDateTime.class);
			System.out.println("dt is " + dt);
			assertNotEquals("dt should not be null", null, dt);
			assertTrue("dt should be null date", dt.isNullValue());
		} finally {
			note.free();
		}
		
		System.out.println("testDoubleWildcard end");
	}
	
	@Test
	public void testEmptyCreatedTime() throws Exception {
		System.out.println("testEmptyCreatedTime start");
		
		NSFNote note = forumDocs.createNote();
		try {
			TIMEDATE td = new TIMEDATE();
			try {
				td.setInnards(0, 0);
				td.setInnards(1, 0);
				note.set("EmptyTime", td);
				note.save();
			} finally {
				td.free();
			}
			
			NSFDateTime dt = note.get("EmptyTime", NSFDateTime.class);
			assertNotEquals("dt should not be null", null, dt);
			assertTrue("dt should be null date", dt.isNullValue());
			
		} finally {
			note.free();
		}
		
		System.out.println("testEmptyCreatedTime end");
	}
	
	@Test
	public void testDateConstructor() throws Exception {
		System.out.println("testDateConstructor start");
		
		Date now = new Date();
		NSFDateTime dt = new NSFDateTime(now);
		Date fromDT = dt.toDate();
		
		assertEquals("now and fromDT should be within 10ms", now.getTime() / 10, fromDT.getTime() / 10);
		
		System.out.println("testDateConstructor end");
	}
	
	@Test
	public void testInnardsConversion() throws Exception {
		TIMEDATE td1 = new TIMEDATE();
		TIMEDATE td2 = new TIMEDATE();
		try {
			td1.fromDateTime(2016, 1, 1, 1, 1, 1, 1, 0, 0);
			td2.fromDateTime(2016, 2, 2, 2, 2, 2, 2, 2, 1);

			String expected = DominoNativeUtils.toUnid(td1, td2);
			
			NSFDateTime dt1 = new NSFDateTime(td1);
			NSFDateTime dt2 = new NSFDateTime(td2);
			
			String result = DominoNativeUtils.toUnid(dt1, dt2);
			
			assertEquals("Generated UNIDs should match", expected, result);
			
		} finally {
			td1.free();
			td2.free();
		}
	}
}
