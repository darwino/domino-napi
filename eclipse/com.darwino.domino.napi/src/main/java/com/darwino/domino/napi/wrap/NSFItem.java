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

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.ByteRef;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.c.IntRef;
import com.darwino.domino.napi.c.ShortRef;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.ItemFlag;
import com.darwino.domino.napi.enums.ValueType;
import com.darwino.domino.napi.struct.BLOCKID;
import com.darwino.domino.napi.struct.MIME_PART;
import com.darwino.domino.napi.struct.OBJECT_DESCRIPTOR;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.struct.TIMEDATE_PAIR;
import com.darwino.domino.napi.struct.TimeStruct;
import com.darwino.domino.napi.struct.UNIVERSALNOTEID;
import com.darwino.domino.napi.util.DominoNativeUtils;

/**
 * @author Jesse Gallagher
 * @since 0.8.0
 */
public class NSFItem extends NSFBase {

	private final NSFNote parent;
	private String name;
	
	private BLOCKID itemBlockId;
	private ValueType type;
	private BLOCKID valueBlockId;
	private int valueLen;
	
	private boolean queried = false;
	private EnumSet<ItemFlag> flags = null;
	private byte seqByte;
	private byte dupItemId;
	
	/**
	 * Constructs a new <code>NSFItem</code> object for the provided note and item name. The item will be the
	 * first item in the note with the provided name.
	 * 
	 * <p>During construction, the item takes ownership of the provided <code>BLOCKID</code>s.</p>
	 * 
	 * @param parent the <code>NSFNote</code> containing the item
	 * @param itemName the name of the item
	 * @param itemBlockId the <code>itemBlockId</code> returned by <code>NSFItemInfo</code>
	 * @param dataType the {@link ValueType} enum corresponding to the type returned by <code>NSFItemInfo</code>
	 * @param valueBlockId the <code>valueBlockId</code> returned by <code>NSFItemInfo</code>
	 * @param valueLen the <code>valueLen</code> returned by <code>NSFItemInfo</code>
	 */
	public NSFItem(NSFNote parent, String name, BLOCKID itemBlockId, ValueType dataType, BLOCKID valueBlockId, int valueLen) {
		super(parent.getAPI());
		
		this.parent = parent;
		this.itemBlockId = addChildStruct(itemBlockId == null ? new BLOCKID() : itemBlockId);
		this.type = dataType;
		this.valueBlockId = addChildStruct(valueBlockId == null ? new BLOCKID() : valueBlockId);
		this.valueLen = valueLen;
		
		if(StringUtil.isEmpty(name)) {
			// If this item came via unusual means (e.g. getFirstItem(null)), do the query now to get the item name
			query();
		} else {
			this.name = name;
		}
	}
	
	/**
	 * Creates an NSFItem based on an existing in-memory NSFItem, including its child structures. Structure ownership
	 * is transferred from the original to this object.
	 * 
	 * <p>The original NSFItem will still function, but it is expected that it will be discarded immediately after transfer.</p>
	 * 
	 * @param existing the existing NSFItem to clone
	 */
	public NSFItem(NSFItem existing) {
		super(existing.parent.getAPI());
		
		this.parent = existing.parent;
		this.itemBlockId = addChildStruct(existing.removeChildStruct(existing.itemBlockId));
		this.type = existing.type;
		this.valueBlockId = addChildStruct(existing.removeChildStruct(existing.valueBlockId));
		this.valueLen = existing.valueLen;
		this.name = existing.name;
		this.queried = existing.queried;
		this.flags = existing.flags;
		this.seqByte = existing.seqByte;
		this.dupItemId = existing.dupItemId;
	}
	
	/**
	 * @return the parent
	 */
	@Override
	public NSFNote getParent() {
		return parent;
	}
	
	/**
	 * 
	 * @return the type of the item, as a {@link ValueType}
	 * @throws DominoException
	 */
	public ValueType getType() throws DominoException {
		return type;
	}
	
	/**
	 * @return the flags
	 */
	public Set<ItemFlag> getFlags() {
		if(!queried) {
			query();
		}
		return EnumSet.copyOf(flags);
	}
	
	public void addFlag(ItemFlag flag) throws DominoException {
		_checkRefValidity();
		
		Set<ItemFlag> existing = getFlags();
		if(existing.add(flag)) {
			setFlags(existing);
		}
	}
	public void removeFlag(ItemFlag flag) throws DominoException {
		Set<ItemFlag> existing = getFlags();
		if(existing.remove(flag)) {
			setFlags(existing);
		}
	}
	
	public void setFlags(Collection<ItemFlag> flags) throws DominoException {
		short flagsRaw = DominoEnumUtil.toBitField(ItemFlag.class, flags);
		
		// TODO see if there's a non-crazy way to do this
		// Read in the existing value
		_checkValueRefValidity();
		long existingValuePtr = api.OSLockBlock(valueBlockId);
		long valuePtr = C.malloc(valueLen);
		try {
			C.memcpy(valuePtr, 0, existingValuePtr, 0, valueLen);
			api.OSUnlockBlock(valueBlockId);
			
			// Then replace the item in-memory
			api.NSFItemDeleteByBLOCKID(getParent().getHandle(), itemBlockId);
			itemBlockId.free(true);
			valueBlockId.free();
			itemBlockId = addChildStruct(new BLOCKID());
			api.NSFItemAppend(getParent().getHandle(), flagsRaw, getName(), C.getWORD(valuePtr, 0), C.ptrAdd(valuePtr, C.sizeOfWORD), valueLen-2);
			
			// Now find the new item block ID
			BLOCKID tempItemBlockId = new BLOCKID(), prevItemBlockId = new BLOCKID();
			try {
				api.NSFItemInfo(getParent().getHandle(), name, tempItemBlockId, null, null, null);
			} catch(DominoException de) {
				if(de.getStatus() == DominoAPI.ERR_ITEM_NOT_FOUND) {
				} else {
					throw de;
				}
			}
			int breaker = 0;
			while(true) {
				C.memcpy(prevItemBlockId.getDataPtr(), 0, tempItemBlockId.getDataPtr(), 0, BLOCKID.sizeOf);
				try {
					api.NSFItemInfoNext(getParent().getHandle(), prevItemBlockId, name, tempItemBlockId, null, null, null);
				} catch(DominoException de) {
					if(de.getStatus() == DominoAPI.ERR_ITEM_NOT_FOUND) {
						break;
					} else {
						throw de;
					}
				}
				
				if(breaker++ > 1000) {
					throw new RuntimeException("Probable infinite loop detected");
				}
			}
			// prevItemBlockId should be what we want
			C.memcpy(itemBlockId.getDataPtr(), 0, prevItemBlockId.getDataPtr(), 0, BLOCKID.sizeOf);
			prevItemBlockId.free();
			tempItemBlockId.free();
			
			query();
		} finally {
			C.free(valuePtr);
		}
	}
	
	/**
	 * @return the itemBlockId
	 */
	public BLOCKID getItemBlockId() {
		return itemBlockId;
	}
	
	/**
	 * @return the valueBlockId
	 */
	public BLOCKID getValueBlockId() {
		return valueBlockId;
	}
	
	/**
	 * @return the valueLen
	 */
	public int getValueLen() {
		return valueLen;
	}
	
	/**
	 * @return the name of the item, as provided during creation
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the seqByte
	 */
	public byte getSeqByte() {
		if(!queried) {
			query();
		}
		return seqByte;
	}
	
	/**
	 * @return the dupItemId
	 */
	public byte getDupItemId() {
		if(!queried) {
			query();
		}
		
		return dupItemId;
	}
	
	/**
	 * @see DominoNativeUtils#readItemValueArray(long, int, long)
	 */
	public Object[] getValue() throws DominoException {
		_checkRefValidity();
		
		if(valueLen == 0 || valueLen == C.sizeOfUSHORT) {
			// Length of 2 indicates that it's just a type indicator with no data
			return new Object[0];
		}
		
		// Blocks consist of an overall pool and an individual block
		// Locking a block involves locking the pool and incrementing to the block
		
		// Find the block pointer address (pool.h)
		_checkValueRefValidity();
		long valuePtr = api.OSLockBlock(valueBlockId);
		try {
			Object[] nativeValues = DominoNativeUtils.readItemValueArray(api, valuePtr, valueLen, parent.getHandle());
			Object[] result = null;
			try {
				result = wrapValues(nativeValues);
			} finally {
				for(Object obj : nativeValues) {
					if(obj instanceof UNIVERSALNOTEID) {
						((UNIVERSALNOTEID)obj).free();
					} else if(obj instanceof TimeStruct) {
						((TimeStruct)obj).free();
					}
				}
			}
			
			return result;
		} finally {
			api.OSUnlockBlock(valueBlockId);
		}
	}
	
	/**
	 * This method attempts to coerce the item's value into the provided class.
	 * 
	 * <p>In the case of supported single-value classes and multi-value items, the item's first value
	 * is returned.</p>
	 * 
	 * @param itemName the item name whose value to retrieve
	 * @param clazz the class to attempt to coerce the item to
	 * @return the item's value as an instance of the provided class, or <code>null</code> if the item doesn't exist or can't be coerced
	 * @throws DominoException if there is a problem retrieving the value
	 */
	// TODO add type coercion from ODA
	public <T> T getValue(Class<T> clazz) throws DominoException {
		Object[] val = getValue();
		return DominoNativeUtils.coerceValue(val, clazz);
	}
	
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#doFree()
	 */
	@Override
	protected void doFree() {
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if(itemBlockId != null && itemBlockId.getDataPtr() != 0) {
			try {
				return StringUtil.format("[{0}: name={1}, flags={2}, type={3}, itemBlockId={4}, valueBlockId={5}]", //$NON-NLS-1$
						getClass().getName(),
						getName(),
						getFlags(),
						getType(),
						itemBlockId,
						valueBlockId
				);
			} catch(DominoException e) {
				return StringUtil.format("[{0}: name={1}, flags={2}, itemBlockId={3}, valueBlockId={4}]", //$NON-NLS-1$
						getClass().getName(),
						getName(),
						getFlags(),
						itemBlockId,
						valueBlockId
				);
			}
		} else {
			return super.toString();
		}
	}
	
	
	/* ******************************************************************************
	 * Internal utility methods
	 ********************************************************************************/
	
	/**
	 * Wraps the data types returned by {@link DominoNativeUtils#readItemValueArray(long, int, long)} in externally-friendly
	 * forms. This does <em>not</em> free the original structures.
	 */
	private Object[] wrapValues(Object[] values) throws DominoException {
		if(values.length == 0) {
			return new Object[0];
		} else if(values[0] instanceof TIMEDATE || values[0] instanceof TIMEDATE_PAIR) {
			NSFTimeType[] dateTimes = new NSFTimeType[values.length];
			for(int i = 0; i < values.length; i++) {
				if(values[i] instanceof TIMEDATE) {
					TIMEDATE val = (TIMEDATE)values[i];
					NSFDateTime dtVal = new NSFDateTime(val);
					dateTimes[i] = dtVal;
				} else {
					TIMEDATE_PAIR val = (TIMEDATE_PAIR)values[i];
					NSFDateRange rangeVal = new NSFDateRange(val);
					dateTimes[i] = rangeVal;
				}
			}
			return dateTimes;
		} else if(values[0] instanceof UNIVERSALNOTEID) {
			String[] unids = new String[values.length];
			for(int i = 0; i < values.length; i++) {
				UNIVERSALNOTEID val = (UNIVERSALNOTEID)values[i];
				unids[i] = val.getUNID();
			}
			return unids;
		} else if(values[0] instanceof MIME_PART) {
			// There'll only be one of these
			MIME_PART val = (MIME_PART)values[0];
			return new Object[] { addChildStruct(val) };
		} else if(values[0] instanceof OBJECT_DESCRIPTOR) {
			// There'll only be one of these
			OBJECT_DESCRIPTOR descriptor = (OBJECT_DESCRIPTOR)values[0];
			return new Object[] { addChildStruct(descriptor) };
		} else {
			return values;
		}
	}
	
	private void query() {
		_checkRefValidity();
		getParent()._checkRefValidity();
		
		long itemNamePtr = C.malloc(256);
		try {
			ShortRef retItemNameLength = new ShortRef();
			ShortRef retItemFlags = new ShortRef();
			ShortRef retDataType = new ShortRef();
			IntRef retValueLength = new IntRef();
			ByteRef retSeqByte = new ByteRef();
			ByteRef retDupItemID = new ByteRef();
			
			valueBlockId.free();
			valueBlockId = addChildStruct(new BLOCKID());
			
			api.NSFItemQueryEx(getParent().getHandle(), itemBlockId, itemNamePtr, (short)256, retItemNameLength, retItemFlags, retDataType, valueBlockId, retValueLength, retSeqByte, retDupItemID);
			
			this.name = C.getLMBCSString(itemNamePtr, 0, retItemNameLength.get());
			this.flags = EnumSet.copyOf(DominoEnumUtil.valuesOf(ItemFlag.class, retItemFlags.get()));
			this.type = DominoEnumUtil.valueOf(ValueType.class, retDataType.get());
			this.valueLen = retValueLength.get();
			this.seqByte = retSeqByte.get();
			this.dupItemId = retDupItemID.get();
			
			queried = true;
		} finally {
			C.free(itemNamePtr);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#isRefValid()
	 */
	@Override
	public boolean isRefValid() {
		return itemBlockId != null && itemBlockId.getDataPtr() != 0 && itemBlockId.getPool() != 0;
	}
	
	private void _checkValueRefValidity() {
		if(valueBlockId == null || valueBlockId.getDataPtr() == 0) {
			throw new IllegalStateException("Illegal valueBlockId " + valueBlockId);
		}
	}
}
