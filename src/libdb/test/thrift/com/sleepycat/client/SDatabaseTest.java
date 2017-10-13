/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002, 2016 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$
 */

package com.sleepycat.client;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class SDatabaseTest extends ClientTestBase {

    private SEnvironment env;

    private SDatabase db;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        env = connection.openEnvironment("env",
                new SEnvironmentConfig().setAllowCreate(true));
        db = env.openDatabase(null, "db", "sub-db",
                new SDatabaseConfig().setAllowCreate(true)
                        .setType(SDatabaseType.BTREE)
                        .setPriority(SCacheFilePriority.DEFAULT)
                        .setBtreeRecordNumbers(true));
    }

    @Test
    public void testClose() throws Exception {
        db.close();
        assertClosed(db);
    }

    @Test
    public void testGetConfig() throws Exception {
        SDatabaseConfig config = db.getConfig();
        assertThat(config.getSortedDuplicates(), is(false));
    }

    @Test
    public void testSetConfig() throws Exception {
        db.setConfig(new SDatabaseConfig().setPriority(SCacheFilePriority.LOW));
        assertThat(db.getConfig().getPriority(), is(SCacheFilePriority.LOW));
    }

    @Test
    public void testGetDatabaseFile() throws Exception {
        assertThat(db.getDatabaseFile(), is("db"));
    }

    @Test
    public void testGetDatabaseName() throws Exception {
        assertThat(db.getDatabaseName(), is("sub-db"));
    }

    @Test
    public void testGetEnvironment() throws Exception {
        assertThat(db.getEnvironment(), is(env));
    }

    @Test
    public void testGetSecondaryDatabases() throws Exception {
        assertThat(db.getSecondaryDatabases().size(), is(0));
    }

    @Test
    public void testGetSecondaryDatabasesWithSecDb() throws Exception {
        SSecondaryConfig config = new SSecondaryConfig().setKeyCreator(
                (sdb, key, data, result) -> {
                    result.setData(data.getData());
                    return true;
                });
        config.setAllowCreate(true).setType(SDatabaseType.BTREE);

        SSecondaryDatabase sdb =
                env.openSecondaryDatabase(null, "secondary", null, db, config);

        assertThat(db.getSecondaryDatabases().size(), is(1));
        assertThat(db.getSecondaryDatabases(), hasItem(sdb));

        sdb.close();

        assertThat(db.getSecondaryDatabases().size(), is(0));
    }

    @Test
    public void testGet() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry("key".getBytes());

        db.put(null, key, new SDatabaseEntry("data".getBytes()));

        assertThat(
                db.get(null, new SDatabaseEntry("bad".getBytes()), null, null),
                is(SOperationStatus.NOTFOUND));

        SDatabaseEntry data = new SDatabaseEntry();
        db.get(null, key, data, null);

        assertThat(new String(data.getData()), is("data"));
    }

    @Test
    public void testGetSearchBoth() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry("key".getBytes());
        SDatabaseEntry data = new SDatabaseEntry("data".getBytes());

        db.put(null, key, new SDatabaseEntry("data".getBytes()));

        assertThat(db.getSearchBoth(null, key, data, null),
                is(SOperationStatus.SUCCESS));
    }

    @Test
    public void testGetSearchRecordNumber() throws Exception {
        db.put(null, new SDatabaseEntry("key".getBytes()),
                new SDatabaseEntry("data".getBytes()));

        SDatabaseEntry key = new SDatabaseEntry().setRecordNumber(1);
        SDatabaseEntry data = new SDatabaseEntry();

        db.getSearchRecordNumber(null, key, data, null);

        assertThat(new String(key.getData()), is("key"));
        assertThat(new String(data.getData()), is("data"));
    }

    @Test
    public void testExists() throws Exception {
        assertThat(db.exists(null, new SDatabaseEntry("bad".getBytes())),
                is(SOperationStatus.NOTFOUND));
    }

    @Test
    public void testGetKeyRange() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry("key".getBytes());
        db.put(null, key, new SDatabaseEntry("data".getBytes()));
        assertThat(db.getKeyRange(null, key), notNullValue());
    }

    @Test
    public void testPut() throws Exception {
        assertThat(db.put(null, new SDatabaseEntry("key".getBytes()),
                        new SDatabaseEntry("data".getBytes())),
                is(SOperationStatus.SUCCESS));
    }

    @Test
    public void testPutNoDupData() throws Exception {
        SDatabase sorted = env.openDatabase(null, "sorted", null,
                new SDatabaseConfig().setAllowCreate(true)
                        .setType(SDatabaseType.BTREE)
                        .setSortedDuplicates(true));

        assertThat(
                sorted.putNoDupData(null, new SDatabaseEntry("key".getBytes()),
                        new SDatabaseEntry("data".getBytes())),
                is(SOperationStatus.SUCCESS));

        sorted.close();
    }

    @Test
    public void testPutNoOverwrite() throws Exception {
        assertThat(db.putNoOverwrite(null, new SDatabaseEntry("key".getBytes()),
                        new SDatabaseEntry("data".getBytes())),
                is(SOperationStatus.SUCCESS));
    }

    @Test
    public void testPutMultipleKey() throws Exception {
        SMultipleKeyDataEntry pairs = new SMultipleKeyDataEntry();
        pairs.append("key1".getBytes(), "data1".getBytes());
        pairs.append("key2".getBytes(), "data2".getBytes());

        assertThat(db.putMultipleKey(null, pairs, true),
                is(SOperationStatus.SUCCESS));

        assertThat(db.exists(null, new SDatabaseEntry("key1".getBytes())),
                is(SOperationStatus.SUCCESS));
        assertThat(db.exists(null, new SDatabaseEntry("key2".getBytes())),
                is(SOperationStatus.SUCCESS));
    }

    @Test
    public void testDelete() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry("key".getBytes());

        db.put(null, key, new SDatabaseEntry("data".getBytes()));

        assertThat(db.exists(null, key), is(SOperationStatus.SUCCESS));

        db.delete(null, key);

        assertThat(db.exists(null, key), is(SOperationStatus.NOTFOUND));
    }

    @Test
    public void testDeleteMultiple() throws Exception {
        SMultipleKeyDataEntry pairs = new SMultipleKeyDataEntry();
        pairs.append("key1".getBytes(), "data1".getBytes());
        pairs.append("key2".getBytes(), "data2".getBytes());

        db.putMultipleKey(null, pairs, true);

        SMultipleDataEntry keys = new SMultipleDataEntry();
        keys.append("key1".getBytes());
        keys.append("key2".getBytes());

        db.deleteMultiple(null, keys);

        assertThat(db.exists(null, new SDatabaseEntry("key1".getBytes())),
                is(SOperationStatus.NOTFOUND));
        assertThat(db.exists(null, new SDatabaseEntry("key2".getBytes())),
                is(SOperationStatus.NOTFOUND));
    }

    @Test
    public void testDeleteMultipleKey() throws Exception {
        SMultipleKeyDataEntry pairs = new SMultipleKeyDataEntry();
        pairs.append("key1".getBytes(), "data1".getBytes());
        pairs.append("key2".getBytes(), "data2".getBytes());

        db.putMultipleKey(null, pairs, true);

        db.deleteMultipleKey(null, pairs);

        assertThat(db.exists(null, new SDatabaseEntry("key1".getBytes())),
                is(SOperationStatus.NOTFOUND));
        assertThat(db.exists(null, new SDatabaseEntry("key2".getBytes())),
                is(SOperationStatus.NOTFOUND));
    }

    @Test
    public void testOpenCursor() throws Exception {
        SCursor cursor = db.openCursor(null, null);
        cursor.close();
    }

    @Test
    public void testOpenSequence() throws Exception {
        SSequence seq =
                db.openSequence(null, new SDatabaseEntry("seq".getBytes()),
                        new SSequenceConfig().setAllowCreate(true));
        seq.close();
    }

    @Test
    public void testRemoveSequence() throws Exception {
        SSequence seq =
                db.openSequence(null, new SDatabaseEntry("seq".getBytes()),
                        new SSequenceConfig().setAllowCreate(true));
        seq.close();
        db.removeSequence(null, new SDatabaseEntry("seq".getBytes()), false,
                true);
    }

    @Test
    public void testCompact() throws Exception {
        assertThat(db.compact(null, new SDatabaseEntry("start".getBytes()),
                new SDatabaseEntry("stop".getBytes()), new SDatabaseEntry(),
                null), notNullValue());
    }

    @Test
    public void testTruncate() throws Exception {
        assertThat(db.truncate(null, false), is(-1));
    }

    @Test
    public void testGetStats() throws Exception {
        assertThat(db.getStats(null, null), notNullValue());
    }
}