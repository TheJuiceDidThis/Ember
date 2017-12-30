/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002, 2016 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$
 */

package com.sleepycat.client;

/**
 * A container that holds multiple record number / data item pairs.
 */
public class SMultipleRecnoDataEntry extends SMultiplePairs {

    /**
     * Append an entry to the container.
     *
     * @param recno the record number of the record to be added
     * @param data an array containing the data to be added
     */
    public void append(int recno, byte[] data) {
        append(new SDatabaseEntry().setRecordNumber(recno),
                new SDatabaseEntry(data));
    }
}
