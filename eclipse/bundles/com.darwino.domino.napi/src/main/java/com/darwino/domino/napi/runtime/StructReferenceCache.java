package com.darwino.domino.napi.runtime;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.IdentityHashMap;
import java.util.Map;

import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.struct.BaseStruct;

/**
 * Reference lifecycle management for {@link BaseStruct} objects.
 * 
 * @author Jesse Gallagher
 * @since 2.2.0
 */
public class StructReferenceCache {
	
	/*
	 * This will have two jobs:
	 * - Maintain a reference queue with phandom references indicating objects that can be freed,
	 * 		and periodically free them
	 * - Maintain an identity map with weak references to free when explicitly done
	 */
	
	private static ThreadLocal<StructReferenceCache> instances = new ThreadLocal<StructReferenceCache>() {
		@Override
		protected StructReferenceCache initialValue() {
			return new StructReferenceCache();
		}
	};
	
	/**
	 * @return the IdentifiableFreeableReferenceCache for the current thread
	 */
	public static StructReferenceCache get() {
		return instances.get();
	}
	
	private final ReferenceQueue<BaseStruct> phantomRefs = new ReferenceQueue<BaseStruct>();
	private final Map<Reference<BaseStruct>, Long> knownRefs = new IdentityHashMap<Reference<BaseStruct>, Long>();
	
	private int counter = 0;
	private static final int FREE_THRESHOLD = 200;

	private StructReferenceCache() {
		
	}
	
	public void register(BaseStruct referent) {
		knownRefs.put(new PhantomReference<BaseStruct>(referent, phantomRefs), referent.getDataPtr());
		
		if(counter++ >= FREE_THRESHOLD) {
			System.gc();
			this.flushPhantomQueue();
			counter = 0;
		}
	}
	
	/**
	 * Frees all objects that have entered the phantom refs queue
	 */
	private void flushPhantomQueue() {
		Reference<? extends IdentifiableFreeable> ref;
		while((ref = phantomRefs.poll()) != null) {
			Long dataPtr = knownRefs.get(ref);
			if(dataPtr != null && dataPtr != 0) {
				C.free(dataPtr);
			}
		}
	}
	
	/**
	 * Frees all enqueued objects that have not yet been freed by the GC. This method should be
	 * called as the final act of a thread or program.
	 */
	public void free() {
		this.flushPhantomQueue();
		
		for(Reference<BaseStruct> ref : knownRefs.keySet()) {
			Long dataPtr = knownRefs.get(ref);
			if(dataPtr != null && dataPtr != 0) {
				C.free(dataPtr);
			}
		}
		knownRefs.clear();
	}
	
	@Override
	protected void finalize() throws Throwable {
		free();
		super.finalize();
	}
}
