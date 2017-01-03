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
package br.ufpr.gres.selection.strategy;

import br.ufpr.gres.core.MutationDetails;
import br.ufpr.gres.core.Mutator;
import br.ufpr.gres.core.classpath.ClassDetails;
import br.ufpr.gres.core.classpath.Resources;
import br.ufpr.gres.core.operators.IMutationOperator;
import br.ufpr.gres.core.operators.method_level.AOR;
import br.ufpr.gres.core.operators.method_level.ROR;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class StrategyTest {

    public static List<MutationDetails> getMutations() throws IOException, ClassNotFoundException {
        ArrayList<IMutationOperator> MUTATORS = new ArrayList<>();
        MUTATORS.add(AOR.AOR);
        MUTATORS.add(ROR.ROR);

        String directory = Paths.get(System.getProperty("user.dir")) + File.separator + "examples" + File.separator + "bub";
        ClassDetails classes = new Resources(directory).getClasses().get(0);

        final byte[] classToMutate = classes.getBytes();

        return Mutator.doMutation(MUTATORS, classToMutate);
    }

}
