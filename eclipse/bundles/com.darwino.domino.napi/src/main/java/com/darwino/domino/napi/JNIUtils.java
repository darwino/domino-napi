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
package com.darwino.domino.napi;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.darwino.domino.napi.DominoException.DominoExceptionWithoutStackTrace;
import com.ibm.commons.util.StringUtil;

/**
 * Quick JNI utilities for the domino API.
 * 
 * @author priand
 */
public class JNIUtils {
	
	public static final boolean TRACE_FILE = false;
	public static final String TRACE_FILE_NAME = "c:\\temp\\domapi.log";
	
	private static PrintWriter fileWriter;
	private static PrintWriter getPrintWriter() {
		if(fileWriter==null) {
			try {
				String fileName = TRACE_FILE_NAME;
				fileWriter = new PrintWriter(new FileWriter(fileName));
			} catch(IOException ex) {
			}
		}
		return fileWriter;
	}
	
	private static boolean beginLine = true;
	
	/**
	 * Keeps track of {@link DominoExceptionWithoutStackTrace} instances by status ID, to be used by
	 * {@link #createExceptionWithoutStackTrace(int, String)}.
	 */
	private static final Map<Integer, DominoException> LIGHT_EXCEPTIONS = new HashMap<Integer, DominoException>();
	
    public static void flush() {
    	if(TRACE_FILE) {
    		PrintWriter pw = getPrintWriter();
    		if(pw!=null) {
    			pw.flush();
    			return;
    		}
    	}
        System.err.flush();
    }

    public static void print(String s) {
    	if(TRACE_FILE) {
    		PrintWriter pw = getPrintWriter();
    		if(pw!=null) {
    			pw.print(format(s));
    			pw.flush();
    			beginLine = false;
    			return;
    		}
    	}
        System.err.print(format(s));
		beginLine = false;
    }

    public static void println(String s) {
    	if(TRACE_FILE) {
    		PrintWriter pw = getPrintWriter();
    		if(pw!=null) {
    			pw.println(format(s));
    			pw.flush();
    			beginLine = true;
    			return;
    		}
    	}
        System.err.println(format(s));
		beginLine = true;
    }

    public static void trace(String s, Object...params) {
        println(format(StringUtil.format(s, params)));
    }
    
    public static void _flush() {
        flush();
    }
    
    public static void _print(String s) {
        print("Native: "+s);
    }

    public static void _println(String s) {
        println("Native: "+s);
    }

    
    public static Object createException(int status, String msg) {
		DominoException ex = new DominoException(null, status, msg);
		
		// No need to include this method in the exception stack trace
		StackTraceElement[] stack = ex.getStackTrace();
		StackTraceElement[] trimmed = new StackTraceElement[stack.length-1];
		System.arraycopy(stack, 1, trimmed, 0, stack.length-1);
		ex.setStackTrace(trimmed);
		
		return ex;
    }
    
    public static Object createFormulaException(int status, String msg, int formulaError, String formulaMessage, int errorLine, int errorColumn, int errorOffset, int errorLength) {
    	DominoFormulaException ex = new DominoFormulaException(null, status, formulaMessage, formulaError, errorLine, errorColumn, errorOffset, errorLength, msg);
		
		// No need to include this method in the exception stack trace
		StackTraceElement[] stack = ex.getStackTrace();
		StackTraceElement[] trimmed = new StackTraceElement[stack.length-1];
		System.arraycopy(stack, 1, trimmed, 0, stack.length-1);
		ex.setStackTrace(trimmed);
		
		return ex;
    }
    
	public static synchronized Object createExceptionWithoutStackTrace(int status, String msg) {
		if(!LIGHT_EXCEPTIONS.containsKey(status)) {
			DominoException ex = new DominoException.DominoExceptionWithoutStackTrace(null, status, msg);
			LIGHT_EXCEPTIONS.put(status, ex);
			return ex;
		} else {
			return LIGHT_EXCEPTIONS.get(status);
		}
	}

    public static void _addObjectToList(List<Object> list, Object v) {
        list.add(v);
    }    
    public static void _addIntToList(List<Integer> list, int v) {
        list.add(v);
    }    
    public static void _addLongToList(List<Long> list, long v) {
        list.add(v);
    }
    
    
    private static DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
    private static String format(String s) {
    	if(beginLine) {
    		return df.format(new Date())+": "+s;
    	} else {
    		return s;
    	}
    }
}
