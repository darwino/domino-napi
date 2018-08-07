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
