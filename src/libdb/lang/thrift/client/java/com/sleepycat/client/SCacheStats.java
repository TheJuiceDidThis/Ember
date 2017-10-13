/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002, 2016 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$
 */

package com.sleepycat.client;

import com.sleepycat.thrift.TCacheStat;

/**
 * Cache statistics for a database environment.
 */
public class SCacheStats {
    /** The Thrift object. */
    private final TCacheStat stat;

    SCacheStats(TCacheStat stat) {
        this.stat = stat;
    }

    /**
     * Gigabytes of cache (total cache size is gbytes + bytes).
     */
    public int getGbytes() {
        return this.stat.gbytes;
    }

    /**
     * Bytes of cache (total cache size is gbytes + bytes).
     */
    public int getBytes() {
        return this.stat.bytes;
    }

    /**
     * Number of caches.
     */
    public int getNumCache() {
        return this.stat.numCache;
    }

    /**
     * Maximum number of caches.
     */
    public int getMaxNumCache() {
        return this.stat.maxNumCache;
    }

    /**
     * Maximum file size for mmap.
     */
    public long getMmapSize() {
        return this.stat.mmapSize;
    }

    /**
     * Maximum number of open file descriptors.
     */
    public int getMaxOpenfd() {
        return this.stat.maxOpenfd;
    }

    /**
     * The maximum number of sequential write operations scheduled by the
     * library when flushing dirty pages from the cache.
     */
    public int getMaxWrite() {
        return this.stat.maxWrite;
    }

    /**
     * The number of microseconds the thread of control should pause before
     * scheduling further write operations.
     */
    public int getMaxWriteSleep() {
        return this.stat.maxWriteSleep;
    }

    /**
     * Pages in the cache.
     */
    public int getPages() {
        return this.stat.pages;
    }

    /**
     * Requested pages mapped into the process' address space (there is no
     * available information about whether or not this request caused disk I/O,
     * although examining the application page fault rate may be helpful).
     */
    public int getMap() {
        return this.stat.pageMapped;
    }

    /**
     * Requested pages found in the cache.
     */
    public long getCacheHit() {
        return this.stat.cacheHit;
    }

    /**
     * Requested pages not found in the cache.
     */
    public long getCacheMiss() {
        return this.stat.cacheMiss;
    }

    /**
     * Pages created in the cache.
     */
    public long getPageCreate() {
        return this.stat.pageCreate;
    }

    /**
     * Pages read into the cache.
     */
    public long getPageIn() {
        return this.stat.pageIn;
    }

    /**
     * Pages written from the cache to the backing file.
     */
    public long getPageOut() {
        return this.stat.pageOut;
    }

    /**
     * Clean pages forced from the cache.
     */
    public long getRoEvict() {
        return this.stat.roEvict;
    }

    /**
     * Dirty pages forced from the cache.
     */
    public long getRwEvict() {
        return this.stat.rwEvict;
    }

    /**
     * Dirty pages written as explicitly requested.
     */
    public long getPageTrickle() {
        return this.stat.pageTrickle;
    }

    /**
     * Clean pages currently in the cache.
     */
    public int getPageClean() {
        return this.stat.pageClean;
    }

    /**
     * Dirty pages currently in the cache.
     */
    public int getPageDirty() {
        return this.stat.pageDirty;
    }

    /**
     * Number of hash buckets in the buffer hash table.
     */
    public int getHashBuckets() {
        return this.stat.hashBuckets;
    }

    /** The number of hash bucket mutexes in the buffer hash table. */
    public int getHashMutexes() {
        return this.stat.hashMutexes;
    }

    /**
     * Page size in bytes.
     */
    public int getPageSize() {
        return this.stat.pageSize;
    }

    /**
     * Total number of buffer hash table lookups.
     */
    public int getHashSearches() {
        return this.stat.hashSearches;
    }

    /**
     * The longest chain ever encountered in buffer hash table lookups.
     */
    public int getHashLongest() {
        return this.stat.hashLongest;
    }

    /**
     * Total number of hash elements traversed during hash table lookups.
     */
    public long getHashExamined() {
        return this.stat.hashExamined;
    }

    /**
     * The number of times that a thread of control was able to obtain a
     * hash bucket lock without waiting.
     */
    public long getHashNowait() {
        return this.stat.hashNowait;
    }

    /**
     * The number of times that a thread of control was forced to wait
     * before obtaining a hash bucket lock.
     */
    public long getHashWait() {
        return this.stat.hashWait;
    }

    /**
     * The number of times a thread of control was able to obtain the
     * hash bucket lock without waiting on the bucket which had the
     * maximum number of times that a thread of control needed to wait.
     */
    public long getHashMaxNowait() {
        return this.stat.hashMaxNowait;
    }

    /**
     * The maximum number of times any hash bucket lock was waited for by
     * a thread of control.
     */
    public long getHashMaxWait() {
        return this.stat.hashMaxWait;
    }

    /**
     * The number of times that a thread of control was able to obtain a
     * cache region mutex without waiting.
     */
    public long getRegionNowait() {
        return this.stat.regionNowait;
    }

    /**
     * The number of times that a thread of control was forced to wait
     * before obtaining a cache region mutex.
     */
    public long getRegionWait() {
        return this.stat.regionWait;
    }

    /**
     * Number of buffers frozen.
     */
    public long getMultiversionFrozen() {
        return this.stat.multiversionFrozen;
    }

    /**
     * Number of buffers thawed.
     */
    public long getMultiversionThawed() {
        return this.stat.multiversionThawed;
    }

    /**
     * Number of frozen buffers freed.
     */
    public long getMultiversionFreed() {
        return this.stat.multiversionFreed;
    }

    /**
     * Number of outdated intermediate versions reused.
     */
    public long getMultiversionReused() {
        return this.stat.multiversionReused;
    }

    /**
     * Number of page allocations.
     */
    public long getAlloc() {
        return this.stat.alloc;
    }

    /**
     * Number of hash buckets checked during allocation.
     */
    public long getAllocBuckets() {
        return this.stat.allocBuckets;
    }

    /**
     * Maximum number of hash buckets checked during an allocation.
     */
    public long getAllocMaxBuckets() {
        return this.stat.allocMaxBuckets;
    }

    /**
     * Number of pages checked during allocation.
     */
    public long getAllocPages() {
        return this.stat.allocPages;
    }

    /**
     * Maximum number of pages checked during an allocation.
     */
    public long getAllocMaxPages() {
        return this.stat.allocMaxPages;
    }

    /**
     * Number of operations blocked waiting for I/O to complete.
     */
    public long getIoWait() {
        return this.stat.ioWait;
    }

    /**
     * Number of mpool sync operations interrupted.
     */
    public long getSyncInterrupted() {
        return this.stat.syncInterrupted;
    }

    /**
     * Individual cache size.
     */
    public long getRegSize() {
        return this.stat.regSize;
    }

    /** The maximum size, in bytes, of the mutex region. */
    public long getRegmax() {
        return this.stat.regmax;
    }

    /**
     * For convenience, the SCacheStats class has a toString method that
     * lists all the data fields.
     */
    public String toString() {
        return "CacheStats:"
                + "\n  gbytes=" + getGbytes()
                + "\n  bytes=" + getBytes()
                + "\n  ncache=" + getNumCache()
                + "\n  max_ncache=" + getMaxNumCache()
                + "\n  mmapsize=" + getMmapSize()
                + "\n  maxopenfd=" + getMaxOpenfd()
                + "\n  maxwrite=" + getMaxWrite()
                + "\n  maxwrite_sleep=" + getMaxWriteSleep()
                + "\n  pages=" + getPages()
                + "\n  map=" + getMap()
                + "\n  cache_hit=" + getCacheHit()
                + "\n  cache_miss=" + getCacheMiss()
                + "\n  page_create=" + getPageCreate()
                + "\n  page_in=" + getPageIn()
                + "\n  page_out=" + getPageOut()
                + "\n  ro_evict=" + getRoEvict()
                + "\n  rw_evict=" + getRwEvict()
                + "\n  page_trickle=" + getPageTrickle()
                + "\n  page_clean=" + getPageClean()
                + "\n  page_dirty=" + getPageDirty()
                + "\n  hash_buckets=" + getHashBuckets()
                + "\n  hash_mutexes=" + getHashMutexes()
                + "\n  pagesize=" + getPageSize()
                + "\n  hash_searches=" + getHashSearches()
                + "\n  hash_longest=" + getHashLongest()
                + "\n  hash_examined=" + getHashExamined()
                + "\n  hash_nowait=" + getHashNowait()
                + "\n  hash_wait=" + getHashWait()
                + "\n  hash_max_nowait=" + getHashMaxNowait()
                + "\n  hash_max_wait=" + getHashMaxWait()
                + "\n  region_nowait=" + getRegionNowait()
                + "\n  region_wait=" + getRegionWait()
                + "\n  mvcc_frozen=" + getMultiversionFrozen()
                + "\n  mvcc_thawed=" + getMultiversionThawed()
                + "\n  mvcc_freed=" + getMultiversionFreed()
                + "\n  mvcc_reused=" + getMultiversionReused()
                + "\n  alloc=" + getAlloc()
                + "\n  alloc_buckets=" + getAllocBuckets()
                + "\n  alloc_max_buckets=" + getAllocMaxBuckets()
                + "\n  alloc_pages=" + getAllocPages()
                + "\n  alloc_max_pages=" + getAllocMaxPages()
                + "\n  io_wait=" + getIoWait()
                + "\n  sync_interrupted=" + getSyncInterrupted()
                + "\n  regsize=" + getRegSize()
                + "\n  regmax=" + getRegmax()
                ;
    }
}
