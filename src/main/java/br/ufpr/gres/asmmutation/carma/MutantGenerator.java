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

import java.util.List;
import br.ufpr.gres.asmmutation.operators.methodlevel.AOR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class MutantGenerator implements IMutationGenerator {

    private Logger logger = LoggerFactory.getLogger(MutantGenerator.class);

    @Override
    public List<Mutant> generateMutants(String classUnderTest, String directory, byte[] originalClassByteCode) {
        
        List<Mutant> mutants = new AOR().applyTransitions(originalClassByteCode);

        this.logger.debug("Number of hits during last mutation process step: " + mutants.size());
                
        return mutants;
    }
}
