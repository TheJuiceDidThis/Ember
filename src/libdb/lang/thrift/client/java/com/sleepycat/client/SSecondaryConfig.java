/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002, 2016 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$
 */

package com.sleepycat.client;

import com.sleepycat.thrift.TDatabaseConfig;
import com.sleepycat.thrift.TFKDeleteAction;
import com.sleepycat.thrift.TSecondaryDatabaseConfig;

import java.util.Objects;

/**
 * The configuration properties of a SSecondaryDatabase extend those of a
 * primary SDatabase. The secondary database configuration is specified when
 * calling {@link SEnvironment#openSecondaryDatabase}.
 * <p>
 * To create a configuration object with default attributes:
 * <pre>
 *  SecondaryConfig config = new SecondaryConfig();
 * </pre>
 * To set custom attributes:
 * <pre>
 *  SSecondaryConfig config = new SSecondaryConfig();
 *  config.setAllowCreate(true);
 *  config.setSortedDuplicates(true);
 *  config.setKeyCreator(new MyKeyCreator());
 * </pre>
 */
public class SSecondaryConfig extends SDatabaseConfig {
    /** The internal wrapper object. */
    private SecondaryConfigWrapper wrapper;

    /** The foreign database. */
    private SDatabase foreign;

    /** The secondary key creator. */
    private SSecondaryKeyCreator keyCreator;

    /** The secondary multiple-key creator. */
    private SSecondaryMultiKeyCreator multiKeyCreator;

    /**
     * Creates an instance with the system's default settings.
     */
    public SSecondaryConfig() {
        super(new TDatabaseConfig());
        this.wrapper = new SecondaryConfigWrapper(super.getThriftObj());
    }

    SSecondaryConfig(SSecondaryConfig config) {
        super(config.getThriftObj());
        this.wrapper = new SecondaryConfigWrapper(super.getThriftObj());
        this.foreign = config.foreign;
        this.keyCreator = config.keyCreator;
        this.multiKeyCreator = config.multiKeyCreator;
    }

    TSecondaryDatabaseConfig getThriftObject() {
        return this.wrapper.getThriftObj();
    }

    /**
     * Return whether the secondary key is immutable.
     *
     * @return whether the secondary key is immutable
     * @see #setImmutableSecondaryKey
     */
    public boolean getImmutableSecondaryKey() {
        return (boolean) this.wrapper.getField(
                TSecondaryDatabaseConfig._Fields.IMMUTABLE_SECONDARY_KEY);
    }

    /**
     * Specify whether the secondary key is immutable.
     * <p>
     * Specifying that a secondary key is immutable can be used to optimize
     * updates when the secondary key in a primary record will never be changed
     * after that primary record is inserted.
     * <p>
     * Be sure to set this property to true only if the secondary key in the
     * primary record is never changed.  If this rule is violated, the
     * secondary index will become corrupted, that is, it will become out of
     * sync with the primary.
     *
     * @param immutableSecondaryKey whether the secondary key is immutable
     * @return this
     */
    public SSecondaryConfig setImmutableSecondaryKey(
            final boolean immutableSecondaryKey) {
        this.wrapper.getThriftObj()
                .setImmutableSecondaryKey(immutableSecondaryKey);
        return this;
    }

    /**
     * Return the database used to check the foreign key integrity constraint,
     * or null if no foreign key constraint will be checked.
     *
     * @return the foreign key database, or null
     * @see #setForeignKeyDatabase
     */
    public SDatabase getForeignKeyDatabase() {
        return this.foreign;
    }

    /**
     * Define a foreign key integrity constraint for a given foreign key
     * database.
     * <p>
     * If this property is non-null, a record must be present in the
     * specified foreign database for every record in the secondary database,
     * where the secondary key value is equal to the foreign database key
     * value. Whenever a record is to be added to the secondary database, the
     * secondary key is used as a lookup key in the foreign database.
     * <p>
     * <p>The foreign database must not have duplicates allowed.</p>
     *
     * @param foreignDb the database used to check the foreign key
     * integrity constraint, or null if no foreign key constraint should be
     * checked.
     * @return this
     */
    public SSecondaryConfig setForeignKeyDatabase(SDatabase foreignDb) {
        this.foreign = foreignDb;
        if (foreignDb == null) {
            this.wrapper.getThriftObj().unsetForeignDb();
        } else {
            this.wrapper.getThriftObj().setForeignDb(foreignDb.getThriftObj());
        }
        return this;
    }

    /**
     * Return the action taken when a referenced record in the foreign key
     * database is deleted.
     *
     * @return the action taken when a referenced record in the foreign key
     * database is deleted.
     * @see #setForeignKeyDeleteAction
     */
    public SForeignKeyDeleteAction getForeignKeyDeleteAction() {
        TFKDeleteAction action = (TFKDeleteAction) this.wrapper.getField(
                TSecondaryDatabaseConfig._Fields.FOREIGN_KEY_DELETE_ACTION);
        return SForeignKeyDeleteAction.toBdb(action);
    }

    /**
     * Specify the action taken when a referenced record in the foreign key
     * database is deleted.
     * <p>
     * This property is ignored if the foreign key database property is null.
     *
     * @param action the action taken when a referenced record in the foreign
     * key database is deleted.
     * @return this
     * @see SForeignKeyDeleteAction
     */
    public SSecondaryConfig setForeignKeyDeleteAction(
            SForeignKeyDeleteAction action) {
        this.wrapper.getThriftObj().setForeignKeyDeleteAction(
                SForeignKeyDeleteAction.toThrift(action));
        return this;
    }

    /**
     * Return the user-supplied object used for creating single-valued
     * secondary keys.
     *
     * @return the user-supplied object used for creating single-valued
     * secondary keys
     * @see #setKeyCreator
     */
    public SSecondaryKeyCreator getKeyCreator() {
        return this.keyCreator;
    }

    /**
     * Specify the user-supplied object used for creating single-valued
     * secondary keys.
     * <p>
     * Unless the primary database is read-only, a key creator is required
     * when opening a secondary database.  Either a SKeyCreator or
     * SMultiKeyCreator must be specified, but both may not be specified.
     * <p>
     *
     * @param keyCreator the user-supplied object used for creating
     * single-valued secondary keys.
     */
    public SSecondaryConfig setKeyCreator(SSecondaryKeyCreator keyCreator) {
        this.keyCreator = keyCreator;
        return this;
    }

    /**
     * Return the user-supplied object used for creating multi-valued
     * secondary keys.
     *
     * @return the user-supplied object used for creating multi-valued secondary
     * keys
     * @see #setMultiKeyCreator
     */
    public SSecondaryMultiKeyCreator getMultiKeyCreator() {
        return this.multiKeyCreator;
    }

    /**
     * Specify the user-supplied object used for creating multi-valued
     * secondary keys.
     * <p>
     * Unless the primary database is read-only, a key creator is required
     * when opening a secondary database.  Either a SKeyCreator or
     * SMultiKeyCreator must be specified, but both may not be specified.
     *
     * @param multiKeyCreator the user-supplied object used for creating
     * multi-valued secondary keys
     */
    public void setMultiKeyCreator(
            final SSecondaryMultiKeyCreator multiKeyCreator) {
        this.multiKeyCreator = multiKeyCreator;
    }

    private static class SecondaryConfigWrapper extends
            ThriftWrapper<TSecondaryDatabaseConfig, TSecondaryDatabaseConfig._Fields> {
        public SecondaryConfigWrapper(TDatabaseConfig dbConfig) {
            super(new TSecondaryDatabaseConfig().setDbConfig(
                    Objects.requireNonNull(dbConfig)));
        }
    }
}
