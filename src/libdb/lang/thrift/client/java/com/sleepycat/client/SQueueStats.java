/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002, 2016 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$
 */

package com.sleepycat.client;

import com.sleepycat.thrift.TQueueStat;

/**
 * The SQueueStats object is used to return Queue database statistics.
 */
public class SQueueStats extends ThriftWrapper<TQueueStat, TQueueStat._Fields>
        implements SDatabaseStats {

    SQueueStats(TQueueStat stat) {
        super(stat);
    }

    /**
     * The next available record number.
     */
    public int getCurRecno() {
        return (int) getField(TQueueStat._Fields.CUR_RECNO);
    }

    /**
     * The underlying database extent size, in pages.
     */
    public int getExtentSize() {
        return (int) getField(TQueueStat._Fields.EXTENT_SIZE);
    }

    /**
     * The first undeleted record in the database.
     */
    public int getFirstRecno() {
        return (int) getField(TQueueStat._Fields.FIRST_RECNO);
    }

    /**
     * The magic number that identifies the file as a Queue file.
     */
    public int getMagic() {
        return (int) getField(TQueueStat._Fields.MAGIC);
    }

    /**
     * The number of records in the database.
     * <p>
     * If the {@link SDatabase#getStats} call was configured by the {@link
     * SStatsConfig#setFast} method, the count will be the last saved value
     * unless it has never been calculated, in which case it will be 0.
     */
    public int getNumData() {
        return (int) getField(TQueueStat._Fields.NUM_DATA);
    }

    /**
     * The number of records in the database.
     * <p>
     * If the {@link SDatabase#getStats} call was configured by the {@link
     * SStatsConfig#setFast} method, the count will be the last saved value
     * unless it has never been calculated, in which case it will be 0.
     */
    public int getNumKeys() {
        return (int) getField(TQueueStat._Fields.NUM_KEYS);
    }

    /**
     * The number of pages in the database.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public int getPages() {
        return (int) getField(TQueueStat._Fields.PAGES);
    }

    /**
     * The number of bytes free in database pages.
     * <p>
     * The information is only included if the {@link SDatabase#getStats} call
     * was not configured by the {@link SStatsConfig#setFast} method.
     */
    public int getPagesFree() {
        return (int) getField(TQueueStat._Fields.PAGES_FREE);
    }

    /**
     * The underlying database page size, in bytes.
     */
    public int getPageSize() {
        return (int) getField(TQueueStat._Fields.PAGE_SIZE);
    }

    /**
     * The length of the records.
     */
    public int getReLen() {
        return (int) getField(TQueueStat._Fields.RE_LEN);
    }

    /**
     * The padding byte value for the records.
     */
    public int getRePad() {
        return (int) getField(TQueueStat._Fields.RE_PAD);
    }

    /**
     * The version of the Queue database.
     */
    public int getVersion() {
        return (int) getField(TQueueStat._Fields.VERSION);
    }

    /**
     * For convenience, the SQueueStats class has a toString method
     * that lists all the data fields.
     */
    public String toString() {
        return "QueueStats:"
                + "\n  magic=" + getMagic()
                + "\n  version=" + getVersion()
                + "\n  nkeys=" + getNumKeys()
                + "\n  ndata=" + getNumData()
                + "\n  pagesize=" + getPageSize()
                + "\n  extentsize=" + getExtentSize()
                + "\n  pages=" + getPages()
                + "\n  re_len=" + getReLen()
                + "\n  re_pad=" + getRePad()
                + "\n  pgfree=" + getPagesFree()
                + "\n  first_recno=" + getFirstRecno()
                + "\n  cur_recno=" + getCurRecno()
                ;
    }
}
