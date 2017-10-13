/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002, 2016 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$
 */

package com.sleepycat.client;

import com.sleepycat.thrift.THeapStat;

/**
 * The SHeapStats object is used to return Heap database statistics.
 */
public class SHeapStats extends ThriftWrapper<THeapStat, THeapStat._Fields>
        implements SDatabaseStats {

    SHeapStats(THeapStat stat) {
        super(stat);
    }

    /**
     * Magic number that identifies the file as a Heap file.
     */
    public int getHeapMagic() {
        return (int) getField(THeapStat._Fields.HEAP_MAGIC);
    }

    /**
     * The number of blob records.
     */
    public int getHeapNumBlobs() {
        return (int) getField(THeapStat._Fields.HEAP_NUM_BLOBS);
    }

    /**
     * Reports the number of records in the Heap database.
     */
    public int getHeapNumRecs() {
        return (int) getField(THeapStat._Fields.HEAP_NUM_RECS);
    }

    /**
     * The number of regions in the Heap database.
     */
    public int getHeapNumRegions() {
        return (int) getField(THeapStat._Fields.HEAP_NUM_REGIONS);
    }

    /**
     * The number of pages in the database.
     */
    public int getHeapPageCount() {
        return (int) getField(THeapStat._Fields.HEAP_PAGE_COUNT);
    }

    /**
     * The underlying database page (and bucket) size, in bytes.
     */
    public int getHeapPageSize() {
        return (int) getField(THeapStat._Fields.HEAP_PAGE_SIZE);
    }

    /**
     * The number of pages in a region in the Heap database.
     */
    public int getHeapRegionSize() {
        return (int) getField(THeapStat._Fields.HEAP_REGION_SIZE);
    }

    /**
     * The version of the Heap database.
     */
    public int getHeapVersion() {
        return (int) getField(THeapStat._Fields.HEAP_VERSION);
    }

    /**
     * For convenience, the SHeapStats class has a toString method
     * that lists all the data fields.
     */
    public String toString() {
        return "HeapStats:"
                + "\n  magic=" + getHeapMagic()
                + "\n  version=" + getHeapVersion()
                + "\n  nblobs=" + getHeapNumBlobs()
                + "\n  nrecs=" + getHeapNumRecs()
                + "\n  pagecnt=" + getHeapPageCount()
                + "\n  pagesize=" + getHeapPageSize()
                + "\n  nregions=" + getHeapNumRegions()
                + "\n  regionsize=" + getHeapRegionSize()
                ;
    }
}
