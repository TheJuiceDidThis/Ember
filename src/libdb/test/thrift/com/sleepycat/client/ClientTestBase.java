/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002, 2016 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$
 */

package com.sleepycat.client;

import com.sleepycat.server.BdbServiceHandler;
import com.sleepycat.server.config.BdbServiceConfig;
import com.sleepycat.server.util.FileUtils;
import com.sleepycat.thrift.BdbService;
import org.apache.log4j.BasicConfigurator;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class ClientTestBase {

    protected Path testRoot;

    protected BdbServiceHandler handler;

    protected BdbServerConnection connection;

    @Before
    public void setUp() throws Exception {
        testRoot = Files.createTempDirectory("BdbClientTest");
        handler = new BdbServiceHandler();

        PipedInputStream serverIn = new PipedInputStream();
        PipedInputStream clientIn = new PipedInputStream();
        PipedOutputStream serverOut = new PipedOutputStream(clientIn);
        PipedOutputStream clientOut = new PipedOutputStream(serverIn);

        connection = new BdbServerConnection(clientIn, clientOut);
        TServer server = createServer(handler, serverIn, serverOut);

        handler.setServerAndConfig(server, createConfig());

        new Thread(server::serve).start();

        Thread testThread = Thread.currentThread();
        Executors.newScheduledThreadPool(1).schedule(() -> {
            testThread.interrupt();
            connection.close();
        }, 1000, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() throws Exception {
        handler.shutdown();
        FileUtils.deleteFileTree(testRoot.toFile());
    }

    private TServer createServer(BdbServiceHandler handler,
            InputStream serverIn, OutputStream serverOut) {
        TServer.Args args = new TServer.Args(
                new IOStreamServerTransport(serverIn, serverOut));
        args.processor(new BdbService.Processor<>(handler));
        args.protocolFactory(new TCompactProtocol.Factory());

        return new TSimpleServer(args);
    }

    private BdbServiceConfig createConfig() throws IOException {
        Properties properties = new Properties();
        properties.setProperty(BdbServiceConfig.ROOT_HOME,
                testRoot.toAbsolutePath().toString());

        BdbServiceConfig config = new BdbServiceConfig(properties);
        config.initRootDirs();

        return config;
    }

    protected void assertClosed(AutoCloseable handle) throws Exception {
        try {
            handle.close();
            fail();
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("The handle is closed or expired."));
        }
    }
}