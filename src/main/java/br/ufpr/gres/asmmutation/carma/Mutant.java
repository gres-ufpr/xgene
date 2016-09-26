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
package br.ufpr.gres.asmmutation.carma;

import java.util.Set;

/**
 * Specification of a mutant
 *
 * @author mike
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 *
 */
public class Mutant {

    private byte[] byteCode;

    private String className;
    private Set<String> executedTestsNames;
    private Set<String> killerTestNames;
    private String name;

    private boolean survived;
    private SourceCodeMapping sourceMapping;

    private ITransition transition;

    @Override
    public boolean equals(Object other) {
        if (other instanceof Mutant) {
            return this.byteCode.equals(((Mutant) other).byteCode);
        }
        return false;
    }

    public byte[] getByteCode() {
        return this.byteCode;
    }

    public void setByteCode(byte[] byteCode) {
        this.byteCode = byteCode;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Set<String> getExecutedTestsNames() {
        return this.executedTestsNames;
    }

    public void setExecutedTestsNames(Set<String> executedTestsNames) {
        this.executedTestsNames = executedTestsNames;
    }

    public Set<String> getKillerTestNames() {
        return this.killerTestNames;
    }

    public void setKillerTestNames(Set<String> killerTestNames) {
        this.killerTestNames = killerTestNames;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SourceCodeMapping getSourceMapping() {
        return this.sourceMapping;
    }

    public void setSourceMapping(SourceCodeMapping sourceMapping) {
        this.sourceMapping = sourceMapping;
    }

    public ITransition getTransition() {
        return this.transition;
    }

    public void setTransition(ITransition transition) {
        this.transition = transition;
    }

    /*public ITransitionGroup getTransitionGroup() {
        return this.transitionGroup;
    }

    public void setTransitionGroup(ITransitionGroup transitionGroup) {
        this.transitionGroup = transitionGroup;
    }*/
    @Override
    public int hashCode() {
        return this.byteCode.hashCode();
    }

    public boolean isSurvived() {
        return this.survived;
    }

    public void setSurvived(boolean survived) {
        this.survived = survived;
    }

    @Override
    public String toString() {
        return this.name + " " + this.className; //+ " " + this.transitionGroup;
    }

}
