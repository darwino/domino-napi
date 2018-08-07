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
package com.darwino.domino.napi.util;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.darwino.domino.napi.DominoAPI;
import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;

/**
 * Elements of this are taken from the OpenNTF Domino API - https://github.com/OpenNTF/org.openntf.domino/blob/master/domino/core/src/main/java/org/openntf/domino/utils/DominoUtils.java
 * 
 * @since 0.8.0
 *
 */
public enum DominoUtils {
	;
	
	/**
	 * This pattern matches the internal format used for the note name in the {@link DominoAPI#FIELD_NAMED} item.
	 */
	public static final Pattern PATTERN_PROFILE_NAME = Pattern.compile("^\\$[Pp]rofile_(\\d\\d\\d).+_.*$"); //$NON-NLS-1$
	
	/**
	 * To unid.
	 * 
	 * @param value
	 *            the value
	 * @return a 32-character hexadecimal string that can be used as a UNID, uniquely and deterministically based on the value argument
	 */
	public static String toUnid(final Serializable value) {
		if (value instanceof CharSequence && DominoUtils.isUnid((CharSequence) value)) {
			return value.toString().toUpperCase();
		} else if(value instanceof CharSequence) {
			String stripped = value.toString().replace("-", ""); //$NON-NLS-1$ //$NON-NLS-2$
			if(isUnid(stripped)) {
				return stripped.toUpperCase();
			}
		}
		String hash = DominoUtils.md5(value);
		while (hash.length() < 32) {
			hash = "0" + hash; //$NON-NLS-1$
		}
		return hash.toUpperCase();
	}
	
	/**
	 * Checks if is unid.
	 * 
	 * @param value
	 *            the value
	 * @return true, if is 32-character hexadecimal sequence
	 */
	public static boolean isUnid(final CharSequence value) {
		if (value.length() != 32)
			return false;
		return DominoUtils.isHex(value);
	}
	
	/**
	 * Checks if is hex.
	 * 
	 * @param value
	 *            the value
	 * @return true, if is hex
	 */
	public static boolean isHex(final CharSequence value) {
		if (value == null)
			return false;
		String chk = value.toString().trim().toLowerCase();
		for (int i = 0; i < chk.length(); i++) {
			char c = chk.charAt(i);
			boolean isHexDigit = Character.isDigit(c) || Character.isWhitespace(c) || c == 'a' || c == 'b' || c == 'c' || c == 'd'
					|| c == 'e' || c == 'f';

			if (!isHexDigit) {
				return false;
			}

		}
		return true;
	}
	
	/**
	 * Md5.
	 * 
	 * @param object
	 *            the Serializable object
	 * @return the string representing the MD5 hash value of the serialized version of the object
	 */
	public static String md5(final Serializable object) {
		return DominoUtils.checksum(object, "MD5"); //$NON-NLS-1$
	}
	
	/**
	 * Checksum.
	 * 
	 * @param object
	 *            the object
	 * @param algorithm
	 *            the algorithm
	 * @return the string
	 */
	public static String checksum(final Serializable object, final String algorithm) {
		String result = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(object);
			result = DominoUtils.checksum(baos.toByteArray(), algorithm);
			out.close();
		} catch (Throwable t) {
			Platform.getInstance().log(t);
		}
		return result;
	}
	
	/**
	 * Checksum.
	 * 
	 * @param bytes
	 *            the bytes
	 * @param alg
	 *            the alg
	 * @return the string
	 */
	public static String checksum(final byte[] bytes, final String alg) {
		String hashed = ""; //$NON-NLS-1$
		byte[] defaultBytes = bytes;
		try {
			MessageDigest algorithm = MessageDigest.getInstance(alg);
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte[] messageDigest = algorithm.digest();
			BigInteger bi = new BigInteger(1, messageDigest);

			hashed = bi.toString(16);
		} catch (Throwable t) {
			Platform.getInstance().log(t);
		}
		return hashed;
	}
	
	/**
	 * Extracts the form name from a given profile $Name value
	 * 
	 * @param profileName the profile key, from the $Name field
	 * @return the profile form name, or <code>null</code> if the provided name is not a profile key
	 */
	public static String getProfileForm(String profileName) {
		if(StringUtil.isEmpty(profileName)) {
			return null;
		}
		Matcher matcher = PATTERN_PROFILE_NAME.matcher(profileName);
		if(!matcher.matches()) {
			return null;
		} else {
			int chars = Integer.parseInt(matcher.group(1), 10);
			int offset = DominoAPI.NAMEDNOTE_PROFILE.length() + 4; // $profile_000
			return profileName.substring(offset, offset+chars);
		}
	}
	
	/**
	 * Extracts the user name from a given profile $Name value
	 * 
	 * @param profileName the profile key, from the $Name field
	 * @return the profile user name, or <code>null</code> if the provided name is not a profile key
	 */
	public static String getProfileUser(String profileName) {
		if(StringUtil.isEmpty(profileName)) {
			return null;
		}
		Matcher matcher = PATTERN_PROFILE_NAME.matcher(profileName);
		if(!matcher.matches()) {
			return null;
		} else {
			int chars = Integer.parseInt(matcher.group(1), 10);
			int offset = DominoAPI.NAMEDNOTE_PROFILE.length() + 4; // $profile_000
			return profileName.substring(offset+chars+1);
		}
	}
}
