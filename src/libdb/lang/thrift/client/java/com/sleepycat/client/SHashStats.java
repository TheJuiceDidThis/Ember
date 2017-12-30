/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002, 2016 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$
 */

package com.sleepycat.client;

import com.sleepycat.thrift.THashStat;

/**
 * The SHashStats object is used to return Hash database statistics.
 */
public class SHashStats extends ThriftWrapper<THashStat, THashStat._Fields>
        implements SDatabaseStats {

    SHashStats(THashStat stat) {
        super(stat);
    }

    /**
     * The number of bytes free on bucket pages.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public long getBFree() {
        return (long) getField(THashStat._Fields.BFREE);
    }

    /**
     * The number of bytes free on hash overflow (big item) pages.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public long getBigBFree() {
        return (long) getField(THashStat._Fields.BIG_BFREE);
    }

    /**
     * The number of hash overflow pages (created when key/data is too big for
     * the page).
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public int getBigPages() {
        return (int) getField(THashStat._Fields.BIG_PAGES);
    }

    /**
     * The number of hash buckets.
     */
    public int getBuckets() {
        return (int) getField(THashStat._Fields.BUCKETS);
    }

    /**
     * The number of duplicate pages.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public int getDup() {
        return (int) getField(THashStat._Fields.DUP);
    }

    /**
     * The number of bytes free on duplicate pages.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public long getDupFree() {
        return (long) getField(THashStat._Fields.DUP_FREE);
    }

    /**
     * The desired fill factor specified at database-creation time.
     */
    public int getFfactor() {
        return (int) getField(THashStat._Fields.FFACTOR);
    }

    /**
     * The number of pages on the free list.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public int getFree() {
        return (int) getField(THashStat._Fields.FREE);
    }

    /**
     * The magic number that identifies the file as a Hash file.
     */
    public int getMagic() {
        return (int) getField(THashStat._Fields.MAGIC);
    }

    /**
     * The number of blob records.
     */
    public int getNumBlobs() {
        return (int) getField(THashStat._Fields.NUM_BLOBS);
    }

    /**
     * The number of key/data pairs in the database.
     * <p>
     * If the {@link SDatabase#getStats} call was configured by the {@link
     * SStatsConfig#setFast} method, the count will be the last saved value
     * unless it has never been calculated, in which case it will be 0.
     */
    public int getNumData() {
        return (int) getField(THashStat._Fields.NUM_DATA);
    }

    /**
     * The number of unique keys in the database.
     * <p>
     * If the {@link SDatabase#getStats} call was configured by the {@link
     * SStatsConfig#setFast} method, the count will be the last saved value
     * unless it has never been calculated, in which case it will be 0.
     */
    public int getNumKeys() {
        return (int) getField(THashStat._Fields.NUM_KEYS);
    }

    /**
     * The number of bucket overflow pages (bucket overflow pages are created
     * when items did not fit on the main bucket page).
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public int getOverflows() {
        return (int) getField(THashStat._Fields.OVERFLOWS);
    }

    /**
     * The number of bytes free on bucket overflow pages.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public long getOvflFree() {
        return (long) getField(THashStat._Fields.OVFL_FREE);
    }

    /**
     * The number of pages in the database.
     */
    public int getPageCount() {
        return (int) getField(THashStat._Fields.PAGE_COUNT);
    }

    /**
     * The underlying Hash database page (and bucket) size, in bytes.
     */
    public int getPageSize() {
        return (int) getField(THashStat._Fields.PAGE_SIZE);
    }

    /**
     * The version of the Hash database.
     */
    public int getVersion() {
        return (int) getField(THashStat._Fields.VERSION);
    }

    /**
     * For convenience, the SHashStats class has a toString method
     * that lists all the data fields.
     */
    public String toString() {
        return "HashStats:"
                + "\n  magic=" + getMagic()
                + "\n  version=" + getVersion()
                + "\n  nkeys=" + getNumKeys()
                + "\n  ndata=" + getNumData()
                + "\n  nblobs=" + getNumBlobs()
                + "\n  pagecnt=" + getPageCount()
                + "\n  pagesize=" + getPageSize()
                + "\n  ffactor=" + getFfactor()
                + "\n  buckets=" + getBuckets()
                + "\n  free=" + getFree()
                + "\n  bfree=" + getBFree()
                + "\n  bigpages=" + getBigPages()
                + "\n  big_bfree=" + getBigBFree()
                + "\n  overflows=" + getOverflows()
                + "\n  ovfl_free=" + getOvflFree()
                + "\n  dup=" + getDup()
                + "\n  dup_free=" + getDupFree()
                ;
    }
}
