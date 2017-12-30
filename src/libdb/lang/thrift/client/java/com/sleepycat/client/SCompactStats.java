/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002, 2016 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$
 */

package com.sleepycat.client;

import com.sleepycat.thrift.TCompactResult;

/**
 * Statistics returned by a {@link SDatabase#compact} operation.
 */
public class SCompactStats
        extends ThriftWrapper<TCompactResult, TCompactResult._Fields> {

    SCompactStats(TCompactResult result) {
        super(result);
    }

    /**
     * The number of empty hash buckets that were found the compaction phase.
     */
    public int getEmptyBuckets() {
        return (int) getField(TCompactResult._Fields.EMPTY_BUCKETS);
    }

    /**
     * The number of database pages freed during the compaction phase.
     */
    public int getPagesFree() {
        return (int) getField(TCompactResult._Fields.PAGES_FREE);
    }

    /**
     * The number of database pages reviewed during the compaction phase.
     */
    public int getPagesExamine() {
        return (int) getField(TCompactResult._Fields.PAGES_EXAMINE);
    }

    /**
     * The number of levels removed from the Btree or Recno database during the
     * compaction phase.
     */
    public int getLevels() {
        return (int) getField(TCompactResult._Fields.LEVELS);
    }

    /**
     * If no transaction parameter was specified to {@link SDatabase#compact},
     * the number of deadlocks which occurred.
     */
    public int getDeadlock() {
        return (int) getField(TCompactResult._Fields.DEADLOCK);
    }

    /**
     * The number of database pages returned to the filesystem.
     */
    public int getPagesTruncated() {
        return (int) getField(TCompactResult._Fields.PAGES_TRUNCATED);
    }

    /**
     * For convenience, the SCompactStats class has a toString method that
     * lists all the data fields.
     */
    public String toString() {
        return "CompactStats:"
                + "\n  empty_buckets=" + getEmptyBuckets()
                + "\n  pages_free=" + getPagesFree()
                + "\n  pages_examine=" + getPagesExamine()
                + "\n  levels=" + getLevels()
                + "\n  deadlock=" + getDeadlock()
                + "\n  pages_truncated=" + getPagesTruncated()
                ;
    }
}
