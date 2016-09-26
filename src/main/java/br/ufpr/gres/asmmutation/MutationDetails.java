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
package br.ufpr.gres.asmmutation;

import br.ufpr.gres.asmmutation.IMutationOperators;
import org.objectweb.asm.tree.InsnList;

/**
 * Captures all data relating to a mutant.
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public final class MutationDetails {

    /**
     * Class name
     */
    private final String className;

    /**
     * Id
     */
    private final int id;

    /**
     * Mutation's line number
     */
    private final int lineNumber;

    /**
     * Mutation code
     */
    private final InsnList mutation;
    /**
     * Object that applied the mutation
     */
    private final String operatorName;

    /**
     * Original code
     */
    private final InsnList original;

    /**
     * Package name (class, f.e: org.uma.test)
     */
    private final String packageName;

    public MutationDetails(String className, int id, int lineNumber, InsnList mutation, String operatorName, InsnList original, String packageName) {
        this.className = className;
        this.id = id;
        this.lineNumber = lineNumber;
        this.mutation = mutation;
        this.operatorName = operatorName;
        this.original = original;
        this.packageName = packageName;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MutationDetails other = (MutationDetails) obj;
        if (this.id == 0) {
            if (other.id != 0) {
                return false;
            }
        } else {
            if (!(this.id == other.id)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the human readable description of the mutation. This may be a
     * constant string or may provide more contextual information depending on the
     * mutation operator.
     *
     * @return Human readable description of the mutation
     */
    public String getDescription() {
        return this.operatorName;
    }

    /**
     * Returns the file in which this mutation is located
     *
     * @return file in which mutation is located
     */
    public String getFilename() {
        return this.className;
    }

    /**
     * Returns the line number on which the mutation occurs as reported within the
     * jvm bytecode
     *
     * @return The line number on which the mutation occurs.
     */
    public int getLineNumber() {
        return this.lineNumber;
    }

    public String getMutantId() {
        return this.operatorName + "_" + id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        //result = (prime * result) + ((this.id == 0) ? 0 : this.id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return String.format("MutationDetails [id=%s, filename=%s, packageName=%s, lineNumber=%s] ", 
                             this.id, 
                             this.className, 
                             this.packageName,
                             this.lineNumber);
    }
}
