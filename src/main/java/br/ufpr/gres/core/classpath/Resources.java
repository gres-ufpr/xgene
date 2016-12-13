/*
 * Copyright 2016 Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>.
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

package br.ufpr.gres.core.classpath;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class Resources {

    /*
    * Root folder (for example: src path, test path)
     */
    private final File root;

    public Resources(final File root) {
        this.root = root;
    }

    public Resources(final String root) {
        this(new File(root));
    }

    public List<ClassInfo> getJavaFiles() throws IOException {
        return getClasses(this.root, ".java");
    }

    public List<ClassInfo> getClasses() throws IOException {
        return getClasses(this.root, ".class");
    }

    private List<ClassInfo> getClasses(final File file, String filter) throws IOException {
        final List<ClassInfo> classNames = new LinkedList<>();

        if (file.exists()) {
            for (final File f : file.listFiles()) {
                if (f.isDirectory()) {
                    classNames.addAll(getClasses(f, filter));
                } else if (f.getName().endsWith(filter)) {
                    classNames.add(new ClassInfo(f, this.root));
                }
            }           
        }
        
        return classNames;
    }
}