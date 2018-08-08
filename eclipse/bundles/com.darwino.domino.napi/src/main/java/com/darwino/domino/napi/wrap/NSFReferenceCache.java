package com.darwino.domino.napi.wrap;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Reference lifecycle management for {@link NSFBase} objects.
 * 
 * @author Jesse Gallagher
 * @since 2.2.0
 */
class NSFReferenceCache {

	/*
	 * This will have two jobs:
	 * - Maintain a reference queue with phandom references indicating objects that can be freed,
	 * 		and periodically free them
	 * - Maintain an identity map with weak references to free when explicitly done
	 */
	
	private static ThreadLocal<NSFReferenceCache> instances = new ThreadLocal<NSFReferenceCache>() {
		@Override
		protected NSFReferenceCache initialValue() {
			return new NSFReferenceCache();
		}
	};
	
	/**
	 * @return the IdentifiableFreeableReferenceCache for the current thread
	 */
	public static NSFReferenceCache get() {
		return instances.get();
	}
	
	private final ReferenceQueue<NSFBase> phantomRefs = new ReferenceQueue<NSFBase>();
	private final Map<Reference<NSFBase>, NSFBase.Recycler> recyclers = new IdentityHashMap<Reference<NSFBase>, NSFBase.Recycler>();
	
	private int counter = 0;
	private static final int FREE_THRESHOLD = 100;

	private NSFReferenceCache() {
		
	}
	
	public void register(NSFBase referent) {
		recyclers.put(new PhantomReference<NSFBase>(referent, phantomRefs), referent.getRecycler());
		
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
		Reference<? extends NSFBase> ref;
		while((ref = phantomRefs.poll()) != null) {
			NSFBase.Recycler recycler = recyclers.remove(ref);
			if(recycler != null) {
				recycler.free();
			}
		}
	}
	
	/**
	 * Frees all enqueued objects that have not yet been freed by the GC. This method should be
	 * called as the final act of a thread or program.
	 */
	public void free() {
		this.flushPhantomQueue();
		
		for(Reference<NSFBase> ref : recyclers.keySet()) {
			NSFBase.Recycler recycler = recyclers.remove(ref);
			if(recycler != null) {
				recycler.free();
			}
		}
		recyclers.clear();
	}
	
	@Override
	protected void finalize() throws Throwable {
		free();
		super.finalize();
	}
}
