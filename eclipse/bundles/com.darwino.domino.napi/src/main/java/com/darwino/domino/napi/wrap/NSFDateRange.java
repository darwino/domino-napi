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
import java.util.Calendar;

import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.struct.TIMEDATE_PAIR;

/**
 * This class is a serialization-friendly wrapper for data from a {@link TIMEDATE_PAIR} and does not
 * require a backing C struct.
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public class NSFDateRange implements Serializable, NSFTimeType {
	private static final long serialVersionUID = 1L;

	private final NSFDateTime lower;
	private final NSFDateTime upper;
	
	public NSFDateRange(TIMEDATE_PAIR pair) throws DominoException {
		if(pair == null) {
			throw new IllegalArgumentException("TIMEDATE_PAIR cannot be null");
		}
		
		lower = new NSFDateTime(pair.getLower());
		upper = new NSFDateTime(pair.getUpper());
	}
	
	public NSFDateRange(TIMEDATE lower, TIMEDATE upper) throws DominoException {
		if(lower == null) {
			throw new IllegalArgumentException("Lower TIMEDATE cannot be null");
		}
		if(upper == null) {
			throw new IllegalArgumentException("Upper TIMEDATE cannot be null");
		}
		
		this.lower = new NSFDateTime(lower);
		this.upper = new NSFDateTime(upper);
	}
	
	public NSFDateRange(NSFDateTime lower, NSFDateTime upper) {
		if(lower == null) {
			throw new IllegalArgumentException("Lower NSFDateTime cannot be null");
		}
		if(upper == null) {
			throw new IllegalArgumentException("Upper NSFDateTime cannot be null");
		}
		
		if(lower.isAnyTime() && !upper.isAnyTime()) {
			throw new IllegalArgumentException("Cannot combine distinct date/time-only types");
		} else if(lower.isAnyDate() && !upper.isAnyDate()) {
			throw new IllegalArgumentException("Cannot combine distinct date/time-only types");
		}
		this.lower = lower;
		this.upper = upper;
	}
	
	public NSFDateRange(Calendar lower, Calendar upper) {
		if(lower == null) {
			throw new IllegalArgumentException("Lower Calendar cannot be null");
		}
		if(upper == null) {
			throw new IllegalArgumentException("Upper Calendar cannot be null");
		}
		
		this.lower = new NSFDateTime(lower);
		this.upper = new NSFDateTime(upper);
	}
	
	/* ******************************************************************************
	 * Getters/setters
	 ********************************************************************************/
	
	/**
	 * @return the lower
	 */
	public NSFDateTime getLower() {
		return lower;
	}
	/**
	 * @return the upper
	 */
	public NSFDateTime getUpper() {
		return upper;
	}
	
	public boolean isAnyTime() {
		return this.upper.isAnyTime();
	}
	
	public boolean isAnyDate() {
		return this.upper.isAnyDate();
	}
	
	public boolean isNullDate() {
		return this.lower.isNullValue() || this.upper.isNullValue();
	}
	
	/* ******************************************************************************
	 * Misc.
	 ********************************************************************************/
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return StringUtil.format("[{0}: lower={1}, upper={2}]", //$NON-NLS-1$
				getClass().getSimpleName(),
				lower,
				upper
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
		if(!(obj instanceof NSFDateRange)) {
			return false;
		}
		return lower.equals(((NSFDateRange)obj).getLower()) && upper.equals(((NSFDateRange)obj).getUpper());
	}
}
