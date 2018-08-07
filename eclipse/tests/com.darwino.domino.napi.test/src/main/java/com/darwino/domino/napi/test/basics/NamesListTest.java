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

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.struct.NAMES_LIST;
import com.ibm.commons.Platform;

@SuppressWarnings("nls")
public class NamesListTest {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", NamesListTest.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", NamesListTest.class.getSimpleName()); //$NON-NLS-1$
	}
	
	@Test
	public void testNamesList() throws Exception {
		DominoAPI api = DominoAPI.get();
		
		String name = "CN=Jesse Gallagher/O=Frost";
		long hNamesList = api.NSFBuildNamesList(name, 0);
		long namesListPtr = api.OSLock(hNamesList);
		try {
			NAMES_LIST namesList = new NAMES_LIST(namesListPtr);
			
			Platform.getInstance().log("names list is {0}", namesList);
			
			List<String> names = Arrays.asList(namesList.getNames());
			Platform.getInstance().log("names is {0}", names);
			assertTrue("names should contain " + name, names.contains(name));
			assertTrue("names should contain *", names.contains("*"));
		} finally {
			api.OSUnlock(hNamesList);
			api.OSMemFree(hNamesList);
		}
	}
}
