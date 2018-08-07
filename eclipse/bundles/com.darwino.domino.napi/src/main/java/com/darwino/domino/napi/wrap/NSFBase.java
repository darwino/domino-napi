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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ibm.commons.log.LogMgr;
import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.struct.BaseStruct;
import com.darwino.domino.napi.util.DominoNativeUtils;

public abstract class NSFBase {
	
	private static final LogMgr logMemory = DominoNativeUtils.NAPI_MEMORY_LOG;
	
	private int refCount = 0;
	/** An internal flag that indicates whether the instance contains C-side memory or handles that require freeing */
	private boolean needsFree = true;
	private boolean freed = false;
	protected final DominoAPI api;
	private List<NSFBase> children = Collections.synchronizedList(new ArrayList<NSFBase>());
	private Collection<BaseStruct> childStructs = Collections.synchronizedList(new ArrayList<BaseStruct>());

	private static boolean TRACE_CREATION = false;
    private Throwable creationTrace;
    
    /**
     * Configures whether the stack trace at creation should be stored inside each struct, for memory-
     * debugging needs.
     * 
     * @param traceCreation the mode to set
     */
	public static void setTraceCreation(boolean traceCreation) {
		TRACE_CREATION = traceCreation;
	}
	
	/**
	 * @return whether stack traces are stored internally in allocated structs, for memory-debugging needs.
	 */
	public static boolean isTraceCreation() {
		return TRACE_CREATION;
	}

	public NSFBase(DominoAPI api) {
		if(api == null) {
			throw new IllegalArgumentException("api cannot be null");
		}
		this.api = api;
		if(TRACE_CREATION) {
        	creationTrace = new Exception();
        }
	}

	/**
	 * Increments the internal reference count. This method should be called by any consumer
	 * of the object receiving it as a return value from another method.
	 */
	public synchronized final void retain() {
		refCount++;
	}
	public synchronized final void free() {
		free(false, true);
	}
	public synchronized final void free(boolean force) {
		free(force, true);
	}
	/**
	 * @param force whether the object should be freed regardless of how many retain references remain.
	 * 			Defaults to false.
	 * @param removeFromParent whether the object should be removed from its parent's list of children if freed.
	 * 			Defaults to true.
	 */
	public synchronized final void free(boolean force, boolean removeFromParent) {
		if(--refCount < 1 || force) {
			for(int i = children.size()-1; i >= 0; i--) {
				NSFBase child = children.get(i);
				if(child != null) {
					if(!child.isFreed()) {
						child.free(true, false);
					}
				}
			}
			for(BaseStruct child : childStructs) {
				child.free();
			}
			doFree();
			if(removeFromParent) {
				removeFromParent();
			}
			refCount = 0;
			freed = true;
		}
	}
	public synchronized boolean isFreed() {
		return freed;
	}
	
	public DominoAPI getAPI() {
		return api;
	}
	
	/**
	 * Adds an {@link NSFBase} object as a "child" of the current object, to be
	 * freed when this object is freed.
	 * 
	 * <p>Note: this is intended for internal use primarily and should only be used
	 * very rarely externally.</p>
	 * 
	 * @param child the {@link NSFBase} object to add to the child list
	 * @return the passed-in object, for chaining
	 */
	public <T extends NSFBase> T addChild(T child) {
		if(logMemory.isTraceDebugEnabled()) {
			if(children.size() % 100 == 0) {
				logMemory.traceDebug("{0}: addChild #{1} ({2})", getClass().getName(), children.size(), child.getClass().getName()); //$NON-NLS-1$
			}
		}
		children.add(child);
		return child;
	}
	/**
	 * Removed an {@link NSFBase} object as a "child" of the current object, to be
	 * no longer freed when this object is freed.
	 * 
	 * <p>Note: this is intended for internal use primarily and should only be used
	 * very rarely externally.</p>
	 * 
	 * @param child the {@link NSFBase} object to remove from the child list
	 * @return the passed-in object, for chaining
	 */
	public <T extends NSFBase> T removeChild(T child) {
		if(logMemory.isTraceDebugEnabled()) {
			if(children.size() % 100 == 0) {
				logMemory.traceDebug("{0}: removeChild #{1} ({2})", getClass().getName(), children.size(), child.getClass().getName()); //$NON-NLS-1$
			}
		}
		children.remove(child);
		return child;
	}
	protected <T extends BaseStruct> T addChildStruct(T child) {
		childStructs.add(child);
		return child;
	}
	protected <T extends BaseStruct> T removeChildStruct(T child) {
		childStructs.remove(child);
		return child;
	}
	
	/**
	 * @return a count of all child {@link NSFBase} objects of this object, recursively
	 */
	public long getChildObjectCount() {
		long result = 0;
		for(NSFBase child : children) {
			result++;
			result += child.getChildObjectCount();
		}
		return result;
	}
	
	/**
	 * @return a count of all child {@link NSFBase} objects of this object that are instances
	 * of the provided class or its subclasses, recursively
	 */
	public long getChildObjectCount(Class<?> clazz) {
		long result = 0;
		for(NSFBase child : children) {
			if(clazz.isInstance(child)) {
				result++;	
			}
			result += child.getChildObjectCount(clazz);
		}
		return result;
	}
	
	/**
	 * @return a count of all child structs of this object and its child {@link NSFBase} objects, recursively
	 */
	public long getChildStructCount() {
		long result = 0;
		result += childStructs.size();
		for(NSFBase child : children) {
			result += child.getChildStructCount();
		}
		return result;
	}
	
	/**
	 * Prints the child hierarchy of this object to System.out.
	 * 
	 * <p>Example:</p>
	 * 
	 * <pre>NSFDatabase
	 * 	NSFNote
	 * 		NSFItem
	 * 		NSFItem
	 * 	NSFView</pre>
	 */
	public void debugPrintChildHierarchy() {
		System.out.println(getClass().getSimpleName() + "#" + System.identityHashCode(this));
		for(NSFBase child : children) {
			System.out.println("\t" + child.getClass().getSimpleName() + "#" + System.identityHashCode(child));
			_debugPrintGrandchildren(child, "\t\t");
		}
	}
	private void _debugPrintGrandchildren(NSFBase child, String prefix) {
		for(NSFBase grandchild : child.children) {
			System.out.println(prefix + grandchild.getClass().getSimpleName() + "#" + System.identityHashCode(grandchild));
			
			_debugPrintGrandchildren(grandchild, prefix + "\t");
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (needsFree && !isFreed()) {
			if (logMemory.isErrorEnabled()) {
				logMemory.error("Instance of NSFBase child {0} was not freed (ref count {1}) during finalization.", getClass().getName(), refCount); //$NON-NLS-1$
				if (creationTrace != null) {
					logMemory.error(creationTrace, "Stack trace at creation:"); //$NON-NLS-1$
				}
			}
			free(true);
		}
		super.finalize();
	}
	
	/**
	 * This method is called when the final reference to this object is freed.
	 * 
	 * <p>Implemented classes are expected to release any memory or network resources they have open.
	 * They are also expected to write this defensively, allowing the method to be called multiple times
	 * on the same object without problem.</p>
	 */
	protected abstract void doFree();
	
	/**
	 * Checks whether the object's inner reference is valid. The meaning of this varies from object to object,
	 * but generally means that any memory references are still set and any Domino handles are still open.
	 * 
	 * @return whether or not the object's back-end references are valid
	 */
	public abstract boolean isRefValid();
	
	protected void _checkRefValidity() {
		if(!isRefValid()) {
			throw new IllegalStateException(StringUtil.format("Object of type {0} is no longer valid.", getClass().getSimpleName())); //$NON-NLS-1$
		}
	}
	
	protected abstract NSFBase getParent();
	protected void removeFromParent() {
		NSFBase parent = getParent();
		if(parent != null) {
			parent.removeChild(this);
		}
	}
	
	/**
	 * Callable by child instances to indicate whether or not they need C-level memory freeing
	 * (for example, variants of memory structures that do not need handles they normally
	 * would).
	 * 
	 * @param needsFree the needs-free value to set
	 */
	protected void setNeedsFree(boolean needsFree) {
		this.needsFree = needsFree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return StringUtil.format("[{0}]", getClass().getSimpleName()); //$NON-NLS-1$
	}
}
