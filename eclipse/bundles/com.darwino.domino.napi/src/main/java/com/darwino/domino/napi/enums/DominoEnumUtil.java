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

package com.darwino.domino.napi.enums;

import java.util.Collection;
import java.util.EnumSet;

/**
 * This class contains utility methods for dealing with {@link INumberEnum} Domino enums.
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 */
public final class DominoEnumUtil {
	private DominoEnumUtil() { }
	
	/**
	 * Given a Domino number-style enum, returns the enum constant for the provided <code>short</code>
	 */
	public static <N extends Number, T extends Enum<T> & INumberEnum<N>> T valueOf(Class<T> clazz, short value) {
		for(T enumVal : clazz.getEnumConstants()) {
			long enumValue = enumVal.getLongValue();
			if(enumValue == value) {
				return enumVal;
			}
		}
		return null;
	}
	
	/**
	 * Given a Domino number-style enum, returns the enum constant for the provided <code>int</code>
	 */
	public static <N extends Number, T extends Enum<T> & INumberEnum<N>> T valueOf(Class<T> clazz, int value) {
		for(T enumVal : clazz.getEnumConstants()) {
			long enumValue = enumVal.getLongValue();
			if(enumValue == value) {
				return enumVal;
			}
		}
		return null;
	}
	
	/**
	 * Given a Domino number-style enum, returns the enum constant for the provided <code>long</code>
	 */
	public static <N extends Number, T extends Enum<T> & INumberEnum<N>> T valueOf(Class<T> clazz, long value) {
		for(T enumVal : clazz.getEnumConstants()) {
			long enumValue = enumVal.getLongValue();
			if(enumValue == value) {
				return enumVal;
			}
		}
		return null;
	}
	
	/**
	 * Given a Domino number-style bitfield enum, returns the matching enum constants for the provided
	 * <code>short</code>
	 */
	public static <N extends Number, T extends Enum<T> & INumberEnum<N>> EnumSet<T> valuesOf(Class<T> clazz, short value) {
		EnumSet<T> result = EnumSet.noneOf(clazz);
		long val = (long)value;
		for(T enumVal : clazz.getEnumConstants()) {
			long enumValue = enumVal.getLongValue();
			if((val & enumValue) != 0) {
				result.add(enumVal);
			}
		}
		return result;
	}
	
	/**
	 * Given a Domino number-style bitfield enum, returns the matching enum constants for the provided
	 * <code>int</code>
	 */
	public static <N extends Number, T extends Enum<T> & INumberEnum<N>> EnumSet<T> valuesOf(Class<T> clazz, int value) {
		EnumSet<T> result = EnumSet.noneOf(clazz);
		long val = (long)value;
		for(T enumVal : clazz.getEnumConstants()) {
			long enumValue = enumVal.getLongValue();
			if((val & enumValue) != 0) {
				result.add(enumVal);
			}
		}
		return result;
	}
	
	/**
	 * Given a Domino number-style bitfield enum, returns the matching enum constants for the provided
	 * <code>long</code>
	 */
	public static <N extends Number, T extends Enum<T> & INumberEnum<N>> EnumSet<T> valuesOf(Class<T> clazz, long value) {
		EnumSet<T> result = EnumSet.noneOf(clazz);
		for(T enumVal : clazz.getEnumConstants()) {
			long enumValue = enumVal.getLongValue();
			if((value & enumValue) != 0) {
				result.add(enumVal);
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <N extends Number, T extends Enum<T> & INumberEnum<N>> N toBitField(Class<T> clazz, Collection<T> values) {
		Long result = new Long(0);
		if(values != null) {
			for(T enumVal : values) {
				result |= enumVal.getLongValue();
			}
		}
		
		// Boil the value down to the right size
		Class<N> numberClass = (Class<N>) clazz.getEnumConstants()[0].getValue().getClass();
		if(numberClass.equals(Byte.class)) {
			return (N)(new Byte(result.byteValue()));
		} else if(numberClass.equals(Short.class)) {
			return (N)(new Short(result.shortValue()));
		} else if(numberClass.equals(Integer.class)) {
			return (N)(new Integer(result.intValue()));
		} else {
			return (N)result;
		}
	}
}
