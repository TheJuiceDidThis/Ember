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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SCursorTest extends ClientTestBase {

    private SEnvironment env;

    private SDatabase db;

    private SCursor cursor;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        env = connection.openEnvironment("env",
                new SEnvironmentConfig().setAllowCreate(true));
        db = env.openDatabase(null, "db", "sub-db",
                new SDatabaseConfig().setAllowCreate(true)
                        .setSortedDuplicates(true)
                        .setType(SDatabaseType.BTREE)
                        .setPriority(SCacheFilePriority.DEFAULT));
        cursor = initDb(db);
    }

    private SCursor initDb(SDatabase database) throws Exception {
        SMultipleKeyDataEntry pairs = new SMultipleKeyDataEntry();
        pairs.append("key1".getBytes(), "data1".getBytes());
        pairs.append("key2".getBytes(), "data2".getBytes());
        database.putMultipleKey(null, pairs, true);
        return database.openCursor(env.beginTransaction(null, null), null);
    }

    @Test
    public void testClose() throws Exception {
        cursor.close();
        assertClosed(cursor);
    }

    @Test
    public void testCompare() throws Exception {
        SCursor cursor2 = db.openCursor(null, null);
        cursor.getFirst(new SDatabaseEntry(), new SDatabaseEntry(), null);
        cursor2.getLast(new SDatabaseEntry(), new SDatabaseEntry(), null);
        assertThat(cursor.compare(cursor2), is(1));
    }

    @Test
    public void testCount() throws Exception {
        cursor.getFirst(new SDatabaseEntry(), new SDatabaseEntry(), null);
        assertThat(cursor.count(), is(1));
    }

    @Test
    public void testDup() throws Exception {
        assertThat(cursor.dup(false), notNullValue());
    }

    @Test
    public void testGetConfig() throws Exception {
        assertThat(cursor.getConfig(), notNullValue());
    }

    @Test
    public void testGetPriority() throws Exception {
        assertThat(cursor.getPriority(), is(SCacheFilePriority.DEFAULT));
    }

    @Test
    public void testSetPriority() throws Exception {
        cursor.setPriority(SCacheFilePriority.HIGH);
        assertThat(cursor.getPriority(), is(SCacheFilePriority.HIGH));
    }

    @Test
    public void testGetDatabase() throws Exception {
        assertThat(cursor.getDatabase(), is(db));
    }

    @Test
    public void testGetCurrent() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();

        cursor.getFirst(new SDatabaseEntry(), new SDatabaseEntry(), null);

        assertThat(cursor.getCurrent(key, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(key.getData()), is("key1"));
    }

    @Test
    public void testGetFirst() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();

        assertThat(cursor.getFirst(key, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(key.getData()), is("key1"));
    }

    @Test
    public void testGetLast() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();

        assertThat(cursor.getLast(key, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(key.getData()), is("key2"));
    }

    @Test
    public void testGetNext() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();

        assertThat(cursor.getNext(key, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(key.getData()), is("key1"));
    }

    @Test
    public void testGetNextDup() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();

        cursor.getFirst(new SDatabaseEntry(), new SDatabaseEntry(), null);

        assertThat(cursor.getNextDup(key, data, null),
                is(SOperationStatus.NOTFOUND));
    }

    @Test
    public void testGetNextNoDup() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();

        assertThat(cursor.getNextNoDup(key, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(key.getData()), is("key1"));
    }

    @Test
    public void testGetPrev() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();

        assertThat(cursor.getPrev(key, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(key.getData()), is("key2"));
    }

    @Test
    public void testGetPrevDup() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();

        cursor.getLast(new SDatabaseEntry(), new SDatabaseEntry(), null);

        assertThat(cursor.getPrevDup(key, data, null),
                is(SOperationStatus.NOTFOUND));
    }

    @Test
    public void testGetPrevNoDup() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();

        assertThat(cursor.getPrevNoDup(key, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(key.getData()), is("key2"));
    }

    @Test
    public void testGetRecordNumber() throws Exception {
        SDatabase recNum = env.openDatabase(null, "recNum", null,
                new SDatabaseConfig().setAllowCreate(true)
                        .setType(SDatabaseType.BTREE)
                        .setBtreeRecordNumbers(true));
        SCursor c = initDb(recNum);

        SDatabaseEntry data = new SDatabaseEntry();
        c.getFirst(new SDatabaseEntry(), new SDatabaseEntry(), null);

        assertThat(c.getRecordNumber(data, null), is(SOperationStatus.SUCCESS));
        assertThat(data.getRecordNumber(), is(1));
    }

    @Test
    public void testGetSearchBoth() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry("key1".getBytes());
        SDatabaseEntry data = new SDatabaseEntry("data1".getBytes());
        assertThat(cursor.getSearchBoth(key, data, null),
                is(SOperationStatus.SUCCESS));
    }

    @Test
    public void testGetSearchBothRange() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry("key1".getBytes());
        SDatabaseEntry data = new SDatabaseEntry("da".getBytes());
        assertThat(cursor.getSearchBothRange(key, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(data.getData()), is("data1"));
    }

    @Test
    public void testGetSearchKey() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry("key1".getBytes());
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(cursor.getSearchKey(key, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(data.getData()), is("data1"));
    }

    @Test
    public void testGetSearchKeyRange() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry("key".getBytes());
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(cursor.getSearchKeyRange(key, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(data.getData()), is("data1"));
    }

    @Test
    public void testGetSearchRecordNumber() throws Exception {
        SDatabase recNum = env.openDatabase(null, "recNum", null,
                new SDatabaseConfig().setAllowCreate(true)
                        .setType(SDatabaseType.BTREE)
                        .setBtreeRecordNumbers(true));
        SCursor c = initDb(recNum);

        SDatabaseEntry key = new SDatabaseEntry().setRecordNumber(1);
        SDatabaseEntry data = new SDatabaseEntry();

        assertThat(c.getSearchRecordNumber(key, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(data.getData()), is("data1"));
    }

    @Test
    public void testPut() throws Exception {
        assertThat(cursor.put(new SDatabaseEntry("key".getBytes()),
                        new SDatabaseEntry("data".getBytes())),
                is(SOperationStatus.SUCCESS));
    }

    @Test
    public void testPutKeyFirst() throws Exception {
        assertThat(cursor.putKeyFirst(new SDatabaseEntry("key".getBytes()),
                        new SDatabaseEntry("data".getBytes())),
                is(SOperationStatus.SUCCESS));
    }

    @Test
    public void testPutKeyLast() throws Exception {
        assertThat(cursor.putKeyLast(new SDatabaseEntry("key".getBytes()),
                        new SDatabaseEntry("data".getBytes())),
                is(SOperationStatus.SUCCESS));
    }

    @Test
    public void testPutNoDupData() throws Exception {
        assertThat(cursor.putNoDupData(new SDatabaseEntry("key".getBytes()),
                        new SDatabaseEntry("data".getBytes())),
                is(SOperationStatus.SUCCESS));
    }

    @Test
    public void testPutNoOverwrite() throws Exception {
        assertThat(cursor.putNoOverwrite(new SDatabaseEntry("key".getBytes()),
                        new SDatabaseEntry("data".getBytes())),
                is(SOperationStatus.SUCCESS));
    }

    @Test
    public void testPutAfter() throws Exception {
        SDatabase dup = env.openDatabase(null, "dup", null,
                new SDatabaseConfig().setAllowCreate(true)
                        .setType(SDatabaseType.BTREE)
                        .setUnsortedDuplicates(true));
        SCursor c = initDb(dup);

        c.getFirst(new SDatabaseEntry(), new SDatabaseEntry(), null);
        assertThat(c.putAfter(new SDatabaseEntry("key".getBytes()),
                        new SDatabaseEntry("data".getBytes())),
                is(SOperationStatus.SUCCESS));
    }

    @Test
    public void testPutBefore() throws Exception {
        SDatabase dup = env.openDatabase(null, "dup", null,
                new SDatabaseConfig().setAllowCreate(true)
                        .setType(SDatabaseType.BTREE)
                        .setUnsortedDuplicates(true));
        SCursor c = initDb(dup);

        c.getFirst(new SDatabaseEntry(), new SDatabaseEntry(), null);
        assertThat(c.putBefore(new SDatabaseEntry("key".getBytes()),
                        new SDatabaseEntry("data".getBytes())),
                is(SOperationStatus.SUCCESS));
    }

    @Test
    public void testPutCurrent() throws Exception {
        SDatabase dup = env.openDatabase(null, "dup", null,
                new SDatabaseConfig().setAllowCreate(true)
                        .setType(SDatabaseType.BTREE)
                        .setUnsortedDuplicates(true));
        SCursor c = initDb(dup);

        c.getFirst(new SDatabaseEntry(), new SDatabaseEntry(), null);
        assertThat(c.putCurrent(new SDatabaseEntry("data".getBytes())),
                is(SOperationStatus.SUCCESS));
    }

    @Test
    public void testDelete() throws Exception {
        cursor.getFirst(new SDatabaseEntry(), new SDatabaseEntry(), null);
        assertThat(cursor.delete(), is(SOperationStatus.SUCCESS));
    }
}