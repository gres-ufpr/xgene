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
package br.ufpr.gres.testcase;

import br.ufpr.gres.core.classpath.ClassDetails;
import br.ufpr.gres.testcase.junit.JUnitFailure;
import br.ufpr.gres.testcase.junit.JUnitResult;
import br.ufpr.gres.testcase.junit.JUnitResultBuilder;
import br.ufpr.gres.util.TestCaseUtils;
import br.ufpr.gres.util.comparator.AlphanumComparator;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class TestCase {

    private static final Logger logger = LoggerFactory.getLogger(TestCase.class);
    private ClassDetails testClass;
    private Class clazzInstance;

    private ArrayList<Method> testCasesMethods;
    private ArrayList<String> testCasesNames;

    /**
     * Test results
     */
    private Map<String, TestResultType> testResults = new TreeMap<>(new AlphanumComparator());

    public TestCase(ClassDetails testClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.testClass = testClass;
        this.testCasesMethods = new ArrayList<>();
        this.testCasesNames = new ArrayList<>();
        this.clazzInstance = testClass.getClassInstance();

        load();
    }

    public ArrayList<String> getTestCasesNames() {
        return this.testCasesNames;
    }

    public ClassDetails getTestClassDetails() {
        return this.testClass;
    }

    /**
     * Create a instance of the test case
     */
    private void load() {
        try {
            // Determine if a class contains JUnit tests
            if (!TestCaseUtils.isTest(this.clazzInstance)) {
                throw new Exception("The test class " + testClass.getClassName().toString() + " does not contains JUnit tests.");
            }

            // read testcases from the test set class
            testCasesMethods = new ArrayList(Arrays.asList(this.clazzInstance.getDeclaredMethods()));

            if (testCasesMethods.isEmpty()) {
                throw new Exception("The test class " + testClass.getClassName().toString() + " does not contains tests.");
            }

            // Read the test case names
            testCasesMethods.stream().forEach((currentTestCase) -> {
                // Define all how PASS, after is updated in runTest method                            
                testResults.put(currentTestCase.getName(), TestResultType.PASS);
                testCasesNames.add(currentTestCase.getName());
            });

            Collections.sort(this.testCasesNames, new AlphanumComparator());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            logger.error("Error for read the test class " + testClass.getClassName().toString(), ex);
        } catch (Exception ex) {
            logger.error("Error for read the test class " + testClass.getClassName().toString(), ex);
        }
    }

    /**
     * Compute the result of a test under the original program
     */
    public void runTest() {
        if (this.clazzInstance != null) {
            JUnitCore core = new JUnitCore();
            Result result = core.run(this.clazzInstance);
            JUnitResultBuilder builder = new JUnitResultBuilder();
            JUnitResult junitResult = builder.build(result);

            if (!junitResult.wasSuccessful()) {
                for (JUnitFailure failure : junitResult.getFailures()) {
                    this.testResults.put(failure.getDescriptionMethodName(), TestResultType.FAIL);
                }
            }
        }
    }
}
