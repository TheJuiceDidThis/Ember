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
import static org.hamcrest.MatcherAssert.assertThat;

public class SSecondaryDatabaseTest extends ClientTestBase {

    private SDatabase primary;

    private SSecondaryDatabase secondary;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        SEnvironment env = connection.openEnvironment("env",
                new SEnvironmentConfig().setAllowCreate(true));
        primary = env.openDatabase(null, "primary", null,
                new SDatabaseConfig().setAllowCreate(true)
                        .setType(SDatabaseType.BTREE));

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
    }

    @Test
    public void testClose() throws Exception {
        secondary.close();
        assertClosed(secondary);
    }

    @Test
    public void testGetPrimaryDatabase() throws Exception {
        assertThat(secondary.getPrimaryDatabase(), is(primary));
    }

    @Test
    public void testGet() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry("sKey".getBytes());
        SDatabaseEntry pKey = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(secondary.get(null, sKey, pKey, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(pKey.getData()), is("pKey"));
        assertThat(new String(data.getData()), is("sKey data"));
    }

    @Test
    public void testGetSearchBoth() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry("sKey".getBytes());
        SDatabaseEntry pKey = new SDatabaseEntry("pKey".getBytes());
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(secondary.getSearchBoth(null, sKey, pKey, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(data.getData()), is("sKey data"));
    }

    @Test
    public void testGetSearchRecordNumber() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry().setRecordNumber(1);
        SDatabaseEntry pKey = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(
                secondary.getSearchRecordNumber(null, sKey, pKey, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(pKey.getData()), is("pKey"));
        assertThat(new String(data.getData()), is("sKey data"));
    }

    @Test
    public void testOpenCursor() throws Exception {
        assertThat(secondary.openCursor(null, null), notNullValue());
    }

    @Test
    public void testGet1() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry("sKey".getBytes());
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(secondary.get(null, sKey, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(data.getData()), is("sKey data"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSearchBoth1() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry("sKey".getBytes());
        SDatabaseEntry data = new SDatabaseEntry("sKey data".getBytes());
        secondary.getSearchBoth(null, sKey, data, null);
    }

    @Test
    public void testGetSearchRecordNumber1() throws Exception {
        SDatabaseEntry sKey = new SDatabaseEntry().setRecordNumber(1);
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(secondary.getSearchRecordNumber(null, sKey, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(data.getData()), is("sKey data"));
    }

}