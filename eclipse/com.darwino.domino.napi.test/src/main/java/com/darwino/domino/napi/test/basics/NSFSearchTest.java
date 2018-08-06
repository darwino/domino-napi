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

package com.darwino.domino.napi.test.basics;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.IntRef;
import com.darwino.domino.napi.proc.NSFSEARCHPROC;
import com.darwino.domino.napi.struct.ITEM_TABLE;
import com.darwino.domino.napi.struct.SEARCH_MATCH;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.test.AllTests;

import static com.darwino.domino.napi.DominoAPI.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class NSFSearchTest {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", NSFSearchTest.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", NSFSearchTest.class.getSimpleName()); //$NON-NLS-1$
	}
	
	@Test
	public void testNSFSearch() throws DominoException {
		long hDb = AllTests.getTwokeyDatabase();
		try {
			TIMEDATE since = new TIMEDATE();
			TIMEDATE until = new TIMEDATE();
			long hFormula = 0;
			try {
				DominoAPI.get().TimeConstant(DominoAPI.TIMEDATE_WILDCARD, since);
				
				// "we don't care"
				IntRef wdc = new IntRef();
				IntRef formulaLen = new IntRef();
				
				hFormula = DominoAPI.get().NSFFormulaCompile(null, "text='Elvis'", formulaLen, wdc, wdc, wdc, wdc, wdc); //$NON-NLS-1$
				
				final List<String> foundUnids = new ArrayList<String>();
				
				NSFSEARCHPROC proc = new NSFSEARCHPROC() {
					@Override public short callback(long searchMatchPtr, long summaryBufferPtr) throws DominoException {
						SEARCH_MATCH searchMatch = new SEARCH_MATCH(searchMatchPtr);
						@SuppressWarnings("unused")
						ITEM_TABLE summary = new ITEM_TABLE(summaryBufferPtr);
						
						foundUnids.add(searchMatch.getOriginatorId().getUNID());
						
						return NOERROR;
					};
				};
				
				DominoAPI.get().NSFSearch(
						hDb,
						hFormula,
						null,
						SEARCH_SUMMARY,
						NOTE_CLASS_DATA,
						since,
						proc,
						until
				);
				
				Platform.getInstance().log("Search finished with until date {0}", until.toJavaDate()); //$NON-NLS-1$
				
				assertEquals(foundUnids.size(), 3);
				
			} finally {
				since.free();
				until.free();
				if(hFormula != 0) { DominoAPI.get().OSMemFree(hFormula); }
			}
		} finally {
			DominoAPI.get().NSFDbClose(hDb);
		}
	}
}
