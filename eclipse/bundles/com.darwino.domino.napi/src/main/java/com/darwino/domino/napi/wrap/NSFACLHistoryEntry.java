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
import java.util.Date;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.util.DominoNativeUtils;
import com.ibm.commons.log.LogMgr;
import com.ibm.commons.util.StringUtil;

/**
 * Represents a single entry in an ACL history, which is a date + textual event pair.
 * 
 * <p>This object is serializable and maintains no association with other API objects.</p> 
 * 
 * @author Jesse Gallagher
 * @since 2.0.0
 */
public class NSFACLHistoryEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final LogMgr log = DominoNativeUtils.NAPI_LOG;
	
	private final long time;
	private final String text;

	public NSFACLHistoryEntry(DominoAPI api, String entryLine) throws DominoException {
		// The entry will contain the date/time in local text form (!) followed by the action text.
		// Let the API sort out the time.
		
		long pLMBCS = C.toLMBCSString(entryLine);
		try {
			long ppText = C.malloc(C.sizeOfPOINTER);
			try {
				C.setPointer(ppText, 0, pLMBCS);
				
				TIMEDATE td = new TIMEDATE();
				try {
					api.ConvertTextToTIMEDATE(0, null, ppText, DominoAPI.MAXALPHATIMEDATE, td);
					time = td.toJavaMillis();
					
					// Now read the rest, which will follow a space
					long pRemainder = C.getPointer(ppText, 0);
					
					String entryText = C.getLMBCSString(pRemainder, 0);
					this.text = entryText;
				} catch(DominoException e) {
					if(log.isErrorEnabled() && e.getStatus() != DominoAPI.ERR_TDI_CONV) {
						// Don't log the error if it's a time/date problem, which is the most common
						log.error(e, "Encountered error when processing ACL entry line '{0}'", entryLine);
					}
					throw e;
				} finally {
					td.free();
				}
			} finally {
				C.free(ppText);
			}
		} finally {
			C.free(pLMBCS);
		}
	}

	public long getTime() {
		return time;
	}
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		return StringUtil.format("[{0}: time={1}, text={2}]", //$NON-NLS-1$
			getClass().getSimpleName(),
			new Date(time),
			text
		);
		
	}
}
