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
import java.lang.reflect.Modifier;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class ClassInfo {

    private ClassName className;
    private File file;
    private boolean isJavaFile;
    private Logger logger = LoggerFactory.getLogger(ClassInfo.class);
    private File root;

    public ClassInfo(File file, File root) throws IOException {
        this.file = file;
        this.root = root;
        isJavaFile = file.getName().contains(".java");

        setClassName();
    }

    public byte[] getBytes() throws IOException {
        return FileUtils.readFileToByteArray(file);
    }

    private void setClassName() {
        this.className = new ClassName(this.file.getName());
    }

    /**
     * Class name without package
     *
     * @return
     */
    public ClassName getClassName() {
        return this.className;
    }

    public File getFile() {
        return this.file;
    }

    /**
     * Examine if class <i>c</i> is an applet class
     */
    private boolean isApplet(Class c) {
        while (c != null) {
            if (c.getName().indexOf("java.applet") == 0) {
                return true;
            }

            c = c.getSuperclass();
            if (c.getName().indexOf("java.lang") == 0) {
                return false;
            }
        }
        return false;
    }

    /**
     * Examine if class <i>c</i> is a GUI class
     */
    private boolean isGUI(Class c) {
        while (c != null) {
            if ((c.getName().indexOf("java.awt") == 0)
                    || (c.getName().indexOf("javax.swing") == 0)) {
                return true;
            }

            c = c.getSuperclass();
            if (c.getName().indexOf("java.lang") == 0) {
                return false;
            }
        }
        return false;
    }

    /**
     * Verify if a class is testable
     *
     * @return Return true if a class is testable; otherwise false
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    public boolean isTestable() throws ClassNotFoundException, IOException {
        Class c = Class.forName(className.asJavaName());

        if (c.isInterface()) {
            logger.error("Can't apply mutation because " + className + " is 'interface'");
            return false;
        }

        if (Modifier.isAbstract(c.getModifiers())) {
            logger.error("Can't apply mutation because " + className + " is 'abstract' class");
            return false;
        }

        if (isGUI(c)) {
            logger.error("Can't apply mutation because " + className + " is 'GUI' class");
            return false;
        }
        if (isApplet(c)) {
            logger.error("Can't apply mutation because " + className + " is 'applet' class");
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return this.className.toString();
    }
}
