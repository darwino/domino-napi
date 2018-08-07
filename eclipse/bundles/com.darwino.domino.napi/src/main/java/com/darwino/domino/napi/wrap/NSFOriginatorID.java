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

import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.struct.OID;
import com.darwino.domino.napi.util.DominoNativeUtils;

/**
 * This class is a non-C-based representation of the Notes {@link OID} structure.
 * 
 * @author Jesse Gallagher
 * @since 1.5.1
 */
public class NSFOriginatorID implements Serializable {
	private static final long serialVersionUID = 1L;

	private final NSFDateTime file;
	private final NSFDateTime note;
	private final long sequence;
	private final NSFDateTime sequenceTime;
	
	public NSFOriginatorID(OID oid) throws DominoException {
		this.file = new NSFDateTime(oid.getFile());
		this.note = new NSFDateTime(oid.getNote());
		this.sequence = oid.getSequence() & 0xFFFFFFFFFFFFFFFFL;
		this.sequenceTime = new NSFDateTime(oid.getSequenceTime());
	}
	
	/**
	 * @return the "File" and "Note" components as a UNID string
	 * @throws DominoException 
	 */
	public String getUnid() {
		return DominoNativeUtils.toUnid(file, note);
	}
	
	/**
	 * The "File" component corresponds to the NSF that contained the note at creation,
	 * which may or may not be a useful date/time value.
	 * 
	 * @return the "File" component, as a {@link NSFDateTime}
	 */
	public NSFDateTime getFile() {
		return file;
	}
	
	/**
	 * The "Note" component usually contains the creation date/time of the note, but this
	 * may have been overridden.
	 * 
	 * @return the "Note" component, as a {@link NSFDateTime}
	 */
	public NSFDateTime getNote() {
		return note;
	}
	
	/**
	 * The "Sequence" value is the incremental version of the note.
	 * 
	 * @return the "Sequence" component, as a <code>long</code>
	 */
	public long getSequence() {
		return sequence;
	}
	
	/**
	 * The "SequenceTime" value is the last time the note was modified globally.
	 * 
	 * @return the "SequenceTime" component, as a {@link NSFDateTime}
	 */
	public NSFDateTime getSequenceTime() {
		return sequenceTime;
	}
}
