/*******************************************************************************
 * Copyright (c) 2009, 2015 VMware Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at
 *   http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 * Contributors:
 *   VMware Inc. - initial contribution
 *******************************************************************************/

package org.eclipse.gemini.web.tomcat.internal.loader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

import org.eclipse.virgo.test.stubs.framework.StubBundle;
import org.junit.Test;
import org.osgi.framework.Constants;

/**
 */
public class BundleClassPathURLExtractorTests {

    private final StubBundle bundle = new StubBundle();

    @Test
    public void extraction() throws MalformedURLException, URISyntaxException {
        this.bundle.addHeader(Constants.BUNDLE_CLASSPATH, ".,foo.jar,cp/bar.jar");

        this.bundle.addEntry(".", new URL("file:."));
        this.bundle.addEntry("foo.jar", new URL("file:foo.jar"));
        this.bundle.addEntry("cp/bar.jar", new URL("file:cp/bar.jar"));

        Set<URI> classPathURLs = BundleClassPathURLExtractor.extractBundleClassPathURLs(this.bundle);
        assertEquals(2, classPathURLs.size());
        assertTrue(classPathURLs.contains(new URI("jar:file:foo.jar!/")));
        assertTrue(classPathURLs.contains(new URI("jar:file:cp/bar.jar!/")));
    }

    @Test
    public void extractionWithMissingEntry() throws MalformedURLException, URISyntaxException {
        this.bundle.addHeader(Constants.BUNDLE_CLASSPATH, ".,foo.jar,cp/bar.jar");

        this.bundle.addEntry(".", new URL("file:."));
        this.bundle.addEntry("cp/bar.jar", new URL("file:cp/bar.jar"));

        Set<URI> classPathURLs = BundleClassPathURLExtractor.extractBundleClassPathURLs(this.bundle);
        assertEquals(1, classPathURLs.size());
        assertTrue(classPathURLs.contains(new URI("jar:file:cp/bar.jar!/")));
    }
}
