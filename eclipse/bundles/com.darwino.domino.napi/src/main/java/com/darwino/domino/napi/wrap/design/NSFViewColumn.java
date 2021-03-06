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
package com.darwino.domino.napi.wrap.design;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.enums.VCF1;
import com.darwino.domino.napi.enums.VCF3;
import com.darwino.domino.napi.wrap.NSFFormula;

/**
 * This class represents the a column from a view's $ViewFormat item.
 * 
 * <p>This class does not retain any handles to C-side memory and is serializable.</p>
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
public class NSFViewColumn implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private byte[] formulaData;
	private String title;
	private Set<VCF1> flags;
	private short flags1raw;
	private Set<VCF3> flags3;
	private short flags3raw;
	
	public NSFViewColumn(String name, byte[] formulaData, String title, Collection<VCF1> flags, short flags1raw, Collection<VCF3> flags3, short flags3raw) {
		this.name = name;
		this.formulaData = new byte[formulaData.length];
		System.arraycopy(formulaData, 0, this.formulaData, 0, formulaData.length);
		this.title = title;
		this.flags = EnumSet.copyOf(flags);
		this.flags1raw = flags1raw;
		this.flags3 = EnumSet.copyOf(flags3);
		this.flags3raw = flags3raw;
	}
	
	// TODO implement remaining components from VIEW_COLUMN_FORMAT*
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param api the {@link DominoAPI} instance to use for formula operations
	 * @return the compiled column formula
	 */
	public NSFFormula getFormula(DominoAPI api) {
		return new NSFFormula(api, formulaData);
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return the flags
	 */
	public Set<VCF1> getFlags() {
		return Collections.unmodifiableSet(flags);
	}
	
	/**
	 * @return the flags1raw
	 */
	public short getFlags1raw() {
		return flags1raw;
	}
	
	public Set<VCF3> getFlags3() {
		return Collections.unmodifiableSet(flags3);
	}
	
	public short getFlags3raw() {
		return flags3raw;
	}
}
