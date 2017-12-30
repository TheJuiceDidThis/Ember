/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002, 2016 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$
 */

package com.sleepycat.client;

import com.sleepycat.thrift.TBtreeStat;

/**
 * The SBtreeStats object is used to return Btree or Recno database statistics.
 */
public class SBtreeStats extends ThriftWrapper<TBtreeStat, TBtreeStat._Fields>
        implements SDatabaseStats {

    SBtreeStats(TBtreeStat stat) {
        super(stat);
    }

    /**
     * The number of database duplicate pages.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public int getDupPages() {
        return (int) getField(TBtreeStat._Fields.DUP_PAGES);
    }

    /**
     * The number of bytes free in database duplicate pages.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public long getDupPagesFree() {
        return (long) getField(TBtreeStat._Fields.DUP_PAGES_FREE);
    }

    /**
     * The number of empty database pages.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public int getEmptyPages() {
        return (int) getField(TBtreeStat._Fields.EMPTY_PAGES);
    }

    /**
     * The number of pages on the free list.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public int getFree() {
        return (int) getField(TBtreeStat._Fields.FREE);
    }

    /**
     * The number of database internal pages.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public int getIntPages() {
        return (int) getField(TBtreeStat._Fields.INT_PAGES);
    }

    /**
     * The number of bytes free in database internal pages.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public long getIntPagesFree() {
        return (long) getField(TBtreeStat._Fields.INT_PAGES_FREE);
    }

    /**
     * The number of database leaf pages.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public int getLeafPages() {
        return (int) getField(TBtreeStat._Fields.LEAF_PAGES);
    }

    /**
     * The number of bytes free in database leaf pages.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public long getLeafPagesFree() {
        return (long) getField(TBtreeStat._Fields.LEAF_PAGES_FREE);
    }

    /**
     * The number of levels in the database.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public int getLevels() {
        return (int) getField(TBtreeStat._Fields.LEVELS);
    }

    /**
     * The magic number that identifies the file as a Btree database.
     */
    public int getMagic() {
        return (int) getField(TBtreeStat._Fields.MAGIC);
    }

    /**
     * The minimum keys per page.
     */
    public int getMinKey() {
        return (int) getField(TBtreeStat._Fields.MIN_KEY);
    }

    /**
     * The number of blob records.
     */
    public int getNumBlobs() {
        return (int) getField(TBtreeStat._Fields.NUM_BLOBS);
    }

    /**
     * The number of key/data pairs or records in the database.
     * <p>
     * For the Btree Access Method, the number of key/data pairs in the
     * database. If the {@link SDatabase#getStats} call was not configured by
     * the {@link SStatsConfig#setFast} method, the count will be exact.
     * Otherwise, the count will be the last saved value unless it has never
     * been calculated, in which case it will be 0.
     * <p>
     * For the Recno Access Method, the number of records in the database. If
     * the database was configured with mutable record numbers, the count will
     * be exact. Otherwise, if the {@link SDatabase#getStats} call was
     * configured by the {@link SStatsConfig#setFast} method, the count will be
     * exact but will include deleted records; if the {@link
     * SDatabase#getStats} call was not configured by the {@link
     * SStatsConfig#setFast} method, the count will be exact and will not
     * include deleted records.
     */
    public int getNumData() {
        return (int) getField(TBtreeStat._Fields.NUM_DATA);
    }

    /**
     * The number of keys or records in the database.
     * <p>
     * For the Btree Access Method, the number of keys in the database. If the
     * {@link SDatabase#getStats} call was not configured by the {@link
     * SStatsConfig#setFast} method or the database was configured to support
     * retrieval by record number, the count will be exact. Otherwise, the
     * count will be the last saved value unless it has never been calculated,
     * in which case it will be 0.
     * <p>
     * For the Recno Access Method, the number of records in the database. If
     * the database was configured with mutable record numbers, the count will
     * be exact. Otherwise, if the {@link SDatabase#getStats} call was
     * configured by the {@link SStatsConfig#setFast} method, the count will be
     * exact but will include deleted records; if the {@link
     * SDatabase#getStats} call was not configured by the {@link
     * SStatsConfig#setFast} method, the count will be exact and will not
     * include deleted records.
     */
    public int getNumKeys() {
        return (int) getField(TBtreeStat._Fields.NUM_KEYS);
    }

    /**
     * The number of database overflow pages.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public int getOverPages() {
        return (int) getField(TBtreeStat._Fields.OVER_PAGES);
    }

    /**
     * The number of bytes free in database overflow pages.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public long getOverPagesFree() {
        return (long) getField(TBtreeStat._Fields.OVER_PAGES_FREE);
    }

    /**
     * The number of pages in the database.
     */
    public int getPageCount() {
        return (int) getField(TBtreeStat._Fields.PAGE_COUNT);
    }

    /**
     * The underlying database page size, in bytes.
     */
    public int getPageSize() {
        return (int) getField(TBtreeStat._Fields.PAGE_SIZE);
    }

    /**
     * The length of fixed-length records.
     */
    public int getReLen() {
        return (int) getField(TBtreeStat._Fields.RE_LEN);
    }

    /**
     * The padding byte value for fixed-length records.
     */
    public int getRePad() {
        return (int) getField(TBtreeStat._Fields.RE_PAD);
    }

    /**
     * The version of the Btree database.
     */
    public int getVersion() {
        return (int) getField(TBtreeStat._Fields.VERSION);
    }

    /**
     * For convenience, the SBtreeStats class has a toString method
     * that lists all the data fields.
     */
    public String toString() {
        return "BtreeStats:"
                + "\n  magic=" + getMagic()
                + "\n  version=" + getVersion()
                + "\n  nkeys=" + getNumKeys()
                + "\n  ndata=" + getNumData()
                + "\n  pagecnt=" + getPageCount()
                + "\n  pagesize=" + getPageSize()
                + "\n  minkey=" + getMinKey()
                + "\n  nblobs=" + getNumBlobs()
                + "\n  re_len=" + getReLen()
                + "\n  re_pad=" + getRePad()
                + "\n  levels=" + getLevels()
                + "\n  int_pg=" + getIntPages()
                + "\n  leaf_pg=" + getLeafPages()
                + "\n  dup_pg=" + getDupPages()
                + "\n  over_pg=" + getOverPages()
                + "\n  empty_pg=" + getEmptyPages()
                + "\n  free=" + getFree()
                + "\n  int_pgfree=" + getIntPagesFree()
                + "\n  leaf_pgfree=" + getLeafPagesFree()
                + "\n  dup_pgfree=" + getDupPagesFree()
                + "\n  over_pgfree=" + getOverPagesFree()
                ;
    }
}
