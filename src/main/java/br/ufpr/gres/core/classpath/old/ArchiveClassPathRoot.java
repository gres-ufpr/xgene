/*
 * Copyright 2017 Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.ufpr.gres.core.classpath.old;

import br.ufpr.gres.util.StreamUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassPathRoot wrapping a jar or zip file
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class ArchiveClassPathRoot implements ClassPathRoot {

     private static final Logger logger = LoggerFactory.getLogger(ArchiveClassPathRoot.class);
     
    private final File file;

    public ArchiveClassPathRoot(final File file) {
        this.file = file;
    }

    @Override
    public InputStream getData(final String name) throws IOException {
        final ZipFile zip = getRoot();
        try {
            final ZipEntry entry = zip.getEntry(name.replace('.', '/') + ".class");
            if (entry == null) {
                return null;
            }
            return StreamUtils.copyStream(zip.getInputStream(entry));
        } finally {
            zip.close(); // closes input stream
        }
    }

    @Override
    public URL getResource(final String name) throws MalformedURLException {
        final ZipFile zip = getRoot();
        try {
            final ZipEntry entry = zip.getEntry(name);
            if (entry != null) {
                return new URL("jar:file:" + zip.getName() + "!/" + entry.getName());
            } else {
                return null;
            }
        } finally {
            closeQuietly(zip);
        }

    }

    private static void closeQuietly(final ZipFile zip) {
        try {
            zip.close();
        } catch (final IOException e) {
            logger.error("Error to close zip file", e);            
        }
    }

    @Override
    public String toString() {
        return "ArchiveClassPathRoot [file=" + this.file.getName() + "]";
    }

    @Override
    public Collection<String> classNames() {
        final List<String> names = new ArrayList<String>();
        final ZipFile root = getRoot();
        try {
            final Enumeration<? extends ZipEntry> entries = root.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    names.add(stringToClassName(entry.getName()));
                }
            }
            return names;
        } finally {
            closeQuietly(root);
        }

    }

    private String stringToClassName(final String name) {
        return name.substring(0, (name.length() - ".class".length())).replace('/',
                '.');
    }

    @Override
    public Optional<String> cacheLocation() {
        return Optional.ofNullable(this.file.getAbsolutePath());
    }

    private ZipFile getRoot() {
        try {
            return new ZipFile(this.file);
        } catch (final IOException ex) {
            logger.error("Error to get root from zip file (" + this.file + ")", ex);            
            return null;
        }
    }

}
