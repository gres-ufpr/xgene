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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) throws Exception {
        //Example path
        String directory = Paths.get(System.getProperty("user.dir")).getParent() + File.separator + "examples" + File.separator + "cal";
        print(new MutantGenerator().generateMutants("Cal.class", directory, Files.readAllBytes(Paths.get(directory, "Cal.class"))), directory);        
    }

    public static void print(List<Mutant> mutants, String directory) {
        for (Mutant mutant : mutants) {
            try (DataOutputStream dout = new DataOutputStream(new FileOutputStream(new File(directory + File.separator + "mutants", mutant.getName() + ".class")))) {
                dout.write(mutant.getByteCode());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }        
    }
}
