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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.IntRef;
import com.darwino.domino.napi.c.ShortRef;
import com.darwino.domino.napi.struct.BLOCKID;
import com.darwino.domino.napi.struct.TIME;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.util.DominoNativeUtils;

@SuppressWarnings("nls")
public class NAPIBasics {
	
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", NAPIBasics.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", NAPIBasics.class.getSimpleName()); //$NON-NLS-1$
	}
	
	@Test
	public void testSessionCreation() throws DominoException {
		String dataDir = DominoAPI.get().OSGetDataDirectory();
		assertNotNull(dataDir);
		assertTrue(dataDir.length() > 0);
		
		Platform.getInstance().log("Data directory={0}", dataDir); //$NON-NLS-1$
	}
	
	@Test
	public void testExecDirectory() throws DominoException {
		String execDir = DominoAPI.get().OSGetExecutableDirectory();
		assertNotNull(execDir);
		assertTrue(execDir.length() > 0);
		
		Platform.getInstance().log("Exec directory={0}", execDir); //$NON-NLS-1$
	}
	
	@Test
	public void testPathConstruct() throws DominoException {
		String path = DominoAPI.get().OSPathNetConstruct(null, "Test/Server", "names.nsf"); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(path);
		assertEquals(path, "Test/Server!!names.nsf"); //$NON-NLS-1$
		Platform.getInstance().log("Net path: {0}", path); //$NON-NLS-1$
	}
	
	@Test
	public void testDateTimeToJavaDate() throws DominoException {
		TIME t = new TIME();
		try {
			t.setYear(2015);
			t.setMonth(5);
			t.setDay(22);
			t.setHour(14);
			t.setMinute(44);
			t.setSecond(18);
			t.setHundredth(17);
			t.setDst(0);
			t.setZone(0);
			Platform.getInstance().log("GMT TIME {0}-{1}-{2} {3}:{4}:{5} {6}", t.getYear(), t.getMonth(), t.getDay(), t.getHour(), t.getMinute(), t.getSecond(), t.getHundredth()); //$NON-NLS-1$
			DominoAPI.get().TimeLocalToGM(t);
			
			TIMEDATE td = new TIMEDATE(t.getField(TIME._GM));
			Date date = td.toJavaDate();
			Calendar cal = Calendar.getInstance(DominoNativeUtils.TIMEZONE_UTC);
			cal.setTime(date);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S"); //$NON-NLS-1$
			df.setTimeZone(DominoNativeUtils.TIMEZONE_UTC);
			Platform.getInstance().log("Java Date={0}",df.format(cal.getTime())); //$NON-NLS-1$
			
			assertEquals(2015,cal.get(Calendar.YEAR));
			assertEquals(5,cal.get(Calendar.MONTH)+1);
			assertEquals(22,cal.get(Calendar.DAY_OF_MONTH));
			assertEquals(14,cal.get(Calendar.HOUR_OF_DAY));
			assertEquals(44,cal.get(Calendar.MINUTE));
			assertEquals(18,cal.get(Calendar.SECOND));
			assertEquals(17,cal.get(Calendar.MILLISECOND)/10);
		} finally {
			t.free();
		}
	}
	
	@Test
	public void testDateTimeFromJavaDate() throws DominoException {
		TIME t = new TIME();
		try {
			Calendar cal = Calendar.getInstance(DominoNativeUtils.TIMEZONE_UTC);
			cal.set(Calendar.YEAR, 2015);
			cal.set(Calendar.MONTH, 5-1);
			cal.set(Calendar.DAY_OF_MONTH, 22);
			cal.set(Calendar.HOUR_OF_DAY, 14);
			cal.set(Calendar.MINUTE, 44);
			cal.set(Calendar.SECOND, 18);
			cal.set(Calendar.MILLISECOND, 170);

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S"); //$NON-NLS-1$
			df.setTimeZone(DominoNativeUtils.TIMEZONE_UTC);
			Platform.getInstance().log("Java Date={0}",df.format(cal.getTime())); //$NON-NLS-1$
			
			TIMEDATE td = new TIMEDATE(t.getField(TIME._GM));
			td.fromJavaMillis(cal.getTimeInMillis());

			t.setDst(0);
			t.setZone(0);
			DominoAPI.get().TimeGMToLocalZone(t);
			Platform.getInstance().log("GMT TIME {0}-{1}-{2} {3}:{4}:{5} {6}", t.getYear(), t.getMonth(), t.getDay(), t.getHour(), t.getMinute(), t.getSecond(), t.getHundredth()); //$NON-NLS-1$
			
			assertEquals(2015,t.getYear());
			assertEquals(5,t.getMonth());
			assertEquals(22,t.getDay());
			assertEquals(14,t.getHour());
			assertEquals(44,t.getMinute());
			assertEquals(18,t.getSecond());
			assertEquals(17,t.getHundredth());
		} finally {
			t.free();
		}
	}
	
	@Test
	public void testCurrentUserName() throws DominoException {
		String userName = DominoAPI.get().SECKFMGetUserName();
		
		Platform.getInstance().log("Username is {0}", userName); //$NON-NLS-1$
		
		assertNotNull("Username should not be null", userName); //$NON-NLS-1$
		assertFalse("Username should not be empty", userName.isEmpty()); //$NON-NLS-1$
	}
	
	@Test
	public void testLogEventText() throws DominoException {
		DominoAPI.get().LogEventText("Test message from LogEventText", 0, (short)0);
		
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < 100; i++) {
			result.append("Noticeably longer test message from LogEventText. ");
		}
		DominoAPI.get().LogEventText(result.toString(), 0, (short)0);
	}
	
	@Test
	public void testIDTableBasics() throws DominoException {
		System.out.println("testIDTableBasics start");
		
		long handle = DominoAPI.get().IDCreateTable(0);
		try {
			System.out.println("Created ID table with handle " + handle);
			assertEquals("ID table should have 0 entries", 0, DominoAPI.get().IDEntries(handle));
			DominoAPI.get().IDInsert(handle, 1234);
			assertEquals("ID table should have 1 entry", 1, DominoAPI.get().IDEntries(handle));
			assertTrue("ID table should contain 1234", DominoAPI.get().IDIsPresent(handle, 1234));
			assertFalse("ID table should not contain 1235", DominoAPI.get().IDIsPresent(handle, 1235));
		} finally {
			System.out.println("Going to destroy ID table with handle " + handle);
			DominoAPI.get().IDDestroyTable(handle);
		}
		System.out.println("testIDTableBasics end");
	}
	
	@Test
	public void testLiteExceptions() throws DominoException {
		long hDb = AllTests.getFakeNamesDatabase();
		try {
			long hNote = DominoAPI.get().NSFNoteCreate(hDb);
			BLOCKID itemBlockId = new BLOCKID();
			ShortRef valueDataType = new ShortRef();
			BLOCKID valueBlockId = new BLOCKID();
			IntRef valueLenRef = new IntRef();
			try {
				// Get a known not-found item
				DominoAPI.get().NSFItemInfo(hNote, "FakeItem", itemBlockId, valueDataType, valueBlockId, valueLenRef);
			} catch(DominoException.DominoExceptionWithoutStackTrace de) {
				// Good!
			} finally {
				itemBlockId.free();
				valueBlockId.free();
				DominoAPI.get().NSFNoteClose(hNote);
			}
		} finally {
			DominoAPI.get().NSFDbClose(hDb);
		}
	}
}
