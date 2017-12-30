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
import static org.hamcrest.MatcherAssert.assertThat;

public class SSecondaryCursorTest extends ClientTestBase {

    private SDatabase primary;

    private SSecondaryDatabase secondary;

    private SSecondaryCursor cursor;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        SEnvironment env = connection.openEnvironment("env",
                new SEnvironmentConfig().setAllowCreate(true));
        primary = env.openDatabase(null, "primary", null,
                new SDatabaseConfig().setAllowCreate(true)
                        .setType(SDatabaseType.BTREE)
                        .setBtreeRecordNumbers(true));

        SSecondaryConfig config = new SSecondaryConfig();
        config.setAllowCreate(true).setType(SDatabaseType.BTREE)
                .setBtreeRecordNumbers(true);
        config.setKeyCreator((sdb, key, data, result) -> {
            result.setData(new String(data.getData()).split(" ")[0].getBytes());
            return true;
        });
        secondary = env.openSecondaryDatabase(null, "secondary", null, primary,
                config);
        primary.put(null, new SDatabaseEntry("pKey".getBytes()),
                new SDatabaseEntry("sKey data".getBytes()));
        cursor = secondary.openCursor(env.beginTransaction(null, null), null);
    }

    @Test
    public void testGetCurrent() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry();
        SDatabaseEntry pKey = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();
        cursor.getFirst(sKey, pKey, data, null);
        assertThat(cursor.getCurrent(sKey, pKey, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(sKey.getData()), is("sKey"));
        assertThat(new String(pKey.getData()), is("pKey"));
        assertThat(new String(data.getData()), is("sKey data"));
    }

    @Test
    public void testGetFirst() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry();
        SDatabaseEntry pKey = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(cursor.getFirst(sKey, pKey, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(sKey.getData()), is("sKey"));
        assertThat(new String(pKey.getData()), is("pKey"));
        assertThat(new String(data.getData()), is("sKey data"));
    }

    @Test
    public void testGetLast() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry();
        SDatabaseEntry pKey = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(cursor.getLast(sKey, pKey, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(sKey.getData()), is("sKey"));
        assertThat(new String(pKey.getData()), is("pKey"));
        assertThat(new String(data.getData()), is("sKey data"));
    }

    @Test
    public void testGetNext() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry();
        SDatabaseEntry pKey = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(cursor.getNext(sKey, pKey, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(sKey.getData()), is("sKey"));
        assertThat(new String(pKey.getData()), is("pKey"));
        assertThat(new String(data.getData()), is("sKey data"));
    }

    @Test
    public void testGetNextDup() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry();
        SDatabaseEntry pKey = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();
        cursor.getFirst(sKey, pKey, data, null);
        assertThat(cursor.getNextDup(sKey, pKey, data, null),
                is(SOperationStatus.NOTFOUND));
    }

    @Test
    public void testGetNextNoDup() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry();
        SDatabaseEntry pKey = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(cursor.getNextNoDup(sKey, pKey, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(sKey.getData()), is("sKey"));
        assertThat(new String(pKey.getData()), is("pKey"));
        assertThat(new String(data.getData()), is("sKey data"));
    }

    @Test
    public void testGetPrev() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry();
        SDatabaseEntry pKey = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(cursor.getPrev(sKey, pKey, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(sKey.getData()), is("sKey"));
        assertThat(new String(pKey.getData()), is("pKey"));
        assertThat(new String(data.getData()), is("sKey data"));
    }

    @Test
    public void testGetPrevDup() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry();
        SDatabaseEntry pKey = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();
        cursor.getFirst(sKey, pKey, data, null);
        assertThat(cursor.getPrevDup(sKey, pKey, data, null),
                is(SOperationStatus.NOTFOUND));
    }

    @Test
    public void testGetPrevNoDup() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry();
        SDatabaseEntry pKey = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(cursor.getPrevNoDup(sKey, pKey, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(sKey.getData()), is("sKey"));
        assertThat(new String(pKey.getData()), is("pKey"));
        assertThat(new String(data.getData()), is("sKey data"));
    }

    @Test
    public void testGetRecordNumber() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry();
        SDatabaseEntry pKey = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();
        cursor.getFirst(sKey, pKey, data, null);
        assertThat(cursor.getRecordNumber(sKey, pKey, null),
                is(SOperationStatus.SUCCESS));
        assertThat(sKey.getRecordNumber(), is(1));
        assertThat(pKey.getRecordNumber(), is(1));
    }

    @Test
    public void testGetSearchBoth() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry("sKey".getBytes());
        SDatabaseEntry pKey = new SDatabaseEntry("pKey".getBytes());
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(cursor.getSearchBoth(sKey, pKey, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(data.getData()), is("sKey data"));
    }

    @Test
    public void testGetSearchBothRange() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry("sKey".getBytes());
        SDatabaseEntry pKey = new SDatabaseEntry("p".getBytes());
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(cursor.getSearchBothRange(sKey, pKey, data, null),
                is(SOperationStatus.NOTFOUND));
    }

    @Test
    public void testGetSearchKey() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry("sKey".getBytes());
        SDatabaseEntry pKey = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(cursor.getSearchKey(sKey, pKey, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(pKey.getData()), is("pKey"));
        assertThat(new String(data.getData()), is("sKey data"));
    }

    @Test
    public void testGetSearchKeyRange() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry("s".getBytes());
        SDatabaseEntry pKey = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(cursor.getSearchKeyRange(sKey, pKey, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(sKey.getData()), is("sKey"));
        assertThat(new String(pKey.getData()), is("pKey"));
        assertThat(new String(data.getData()), is("sKey data"));
    }

    @Test
    public void testGetSearchRecordNumber() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry().setRecordNumber(1);
        SDatabaseEntry pKey = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(cursor.getSearchRecordNumber(sKey, pKey, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(sKey.getData()), is("sKey"));
        assertThat(new String(pKey.getData()), is("pKey"));
        assertThat(new String(data.getData()), is("sKey data"));
    }
}