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
package br.ufpr.gres.util;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class TestCaseUtils {

    /**
     * Determine if a class contains JUnit tests
     *
     * @param cls
     * @return
     */
    public static boolean isTest(Class<?> cls) {
        if (Modifier.isAbstract(cls.getModifiers())) {
            return false;
        }

        TestClass tc;

        try {
            tc = new TestClass(cls);
        } catch (IllegalArgumentException e) {
            return false;
        } catch (RuntimeException e) {
            //this can happen if class has Annotations that are not available on classpath
            throw new RuntimeException("Failed to analyze class " + cls.getName() + " due to: " + e.toString());
        }

        // JUnit 4
        try {
            List<FrameworkMethod> methods = new ArrayList<>();
            methods.addAll(tc.getAnnotatedMethods(Test.class));
            for (FrameworkMethod method : methods) {
                List<Throwable> errors = new ArrayList<Throwable>();
                method.validatePublicVoidNoArg(false, errors);
                if (errors.isEmpty()) {
                    return true;
                }
            }
        } catch (IllegalArgumentException e) {
            return false;
        }

        // JUnit 3
        Class<?> superClass = cls;
        while ((superClass = superClass.getSuperclass()) != null) {
            if (superClass.getCanonicalName().equals(Object.class.getCanonicalName())) {
                break;
            } else if (superClass.getCanonicalName().equals(TestCase.class.getCanonicalName())) {
                return true;
            }
        }

        // TODO add support for other frameworks, e.g., TestNG ?
        return false;
    }      
}
