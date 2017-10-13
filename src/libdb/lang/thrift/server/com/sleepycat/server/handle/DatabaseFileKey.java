/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002, 2016 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$
 */

package com.sleepycat.server.handle;

import java.io.File;
import java.io.IOException;

/**
 * A DatabaseFileKey uniquely identifies a database file.
 */
public class DatabaseFileKey extends FileKey {
    /** If the database file is an in-memory database. */
    private boolean inMemory;

    public DatabaseFileKey(File dbFile, boolean inMemory) throws IOException {
        super(dbFile);
        this.inMemory = inMemory;
    }

    public boolean isInMemory() {
        return this.inMemory;
    }
}
