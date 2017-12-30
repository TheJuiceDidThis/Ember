/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002, 2016 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$
 */

package com.sleepycat.server.util;

import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.MultipleEntry;
import com.sleepycat.thrift.TKeyData;

import java.util.List;

/**
 * DelArgs represents the arguments for a database del call.
 */
public class DelArgs extends InputArgs {
    /** The key argument. */
    public MultipleEntry key;

    /**
     * Construct the arguments for a Database del call.
     *
     * @param keyOrPairs the list of keys or key/data pairs to be deleted
     */
    public DelArgs(List<TKeyData> keyOrPairs) throws DatabaseException {
        this.key = convert(keyOrPairs);
    }
}
