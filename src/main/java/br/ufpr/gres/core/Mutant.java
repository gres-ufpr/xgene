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
package br.ufpr.gres.core;

import java.util.ArrayList;
import java.util.List;

/**
 * A fully generated mutant
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public final class Mutant {

    private final List<MutationDetails> details;
    private final byte[] bytes;

    public Mutant(final MutationDetails details, final byte[] bytes) {
        this.details = new ArrayList<>();
        this.details.add(details);
        this.bytes = bytes;
    }

    public Mutant(final List<MutationDetails> details, final byte[] bytes) {
        this.details = details;
        this.bytes = bytes;
    }

    /**
     * Returns a data relating to the mutant
     *
     * @return A MutationDetails object
     */
    public List<MutationDetails> getDetails() {
        return this.details;
    }

    /**
     * Returns a byte array containing the mutant class
     *
     * @return A byte array
     */
    public byte[] getBytes() {
        return this.bytes;
    }
    
    /**
     * Return the mutant order (number of mutations present in the mutant)
     * @return Mutant order
     */
    public int getOrder(){
        return this.details.size();
    }
    
    @Override
    public String toString() {
        String description = "";

        for (int i = 0; i < details.size(); i++) {
            description += i + 1 + ") " + details.get(i).toString() + "\n";
        }

        return description;
    }
}
