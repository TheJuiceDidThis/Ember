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

public class SJoinCursorTest extends ClientTestBase {

    private SDatabase primary;

    private SJoinCursor joinCursor;

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
                .setSortedDuplicates(true);

        config.setKeyCreator(new KeyCreator(0));
        SSecondaryDatabase secondary1 = env.openSecondaryDatabase(null,
                "secondary1", null, primary, config);

        config.setKeyCreator(new KeyCreator(1));
        SSecondaryDatabase secondary2 = env.openSecondaryDatabase(null,
                "secondary2", null, primary, config);

        primary.put(null, new SDatabaseEntry("pKey1".getBytes()),
                new SDatabaseEntry("key1_1 key2_1".getBytes()));
        primary.put(null, new SDatabaseEntry("pKey2".getBytes()),
                new SDatabaseEntry("key1_1 key2_2".getBytes()));
        primary.put(null, new SDatabaseEntry("pKey3".getBytes()),
                new SDatabaseEntry("key1_2 key2_1".getBytes()));

        SSecondaryCursor cursor1 = secondary1.openCursor(null, null);
        cursor1.getSearchKey(new SDatabaseEntry("key1_1".getBytes()), null,
                null);

        SSecondaryCursor cursor2 = secondary2.openCursor(null, null);
        cursor2.getSearchKey(new SDatabaseEntry("key2_2".getBytes()), null,
                null);

        joinCursor = primary.join(new SCursor[]{cursor1, cursor2},
                new SJoinConfig());
    }

    @Test
    public void testClose() throws Exception {
        joinCursor.close();
        assertClosed(joinCursor);
    }

    @Test
    public void testGetConfig() throws Exception {
        assertThat(joinCursor.getConfig().getNoSort(), is(false));
    }

    @Test
    public void testGetDatabase() throws Exception {
        assertThat(joinCursor.getDatabase(), is(primary));
    }

    @Test
    public void testGetNext() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry();
        assertThat(joinCursor.getNext(key, null), is(SOperationStatus.SUCCESS));
        assertThat(new String(key.getData()), is("pKey2"));
    }

    @Test
    public void testGetNext1() throws Exception {
        SDatabaseEntry key = new SDatabaseEntry();
        SDatabaseEntry data = new SDatabaseEntry();
        assertThat(joinCursor.getNext(key, data, null),
                is(SOperationStatus.SUCCESS));
        assertThat(new String(data.getData()), is("key1_1 key2_2"));
    }

    private static class KeyCreator implements SSecondaryKeyCreator {
        private int index;

        public KeyCreator(int index) {
            this.index = index;
        }

        @Override
        public boolean createSecondaryKey(SSecondaryDatabase secondary,
                SDatabaseEntry key, SDatabaseEntry data, SDatabaseEntry result)
                throws SDatabaseException {
            result.setData(
                    new String(data.getData()).split(" ")[index].getBytes());
            return true;
        }
    }
}