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

package br.ufpr.gres.asmmutation.tests;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.objectweb.asm.ClassReader;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) throws Exception {
        String directory = Paths.get(System.getProperty("user.dir")).getParent() + File.separator + "examples" + File.separator + "cal";
        ClassReader cr = new ClassReader(Files.readAllBytes(Paths.get(directory, "Cal.class")));        
        MutatingClassVisitor mcv = new MutatingClassVisitor();
        cr.accept(mcv, ClassReader.EXPAND_FRAMES);
    }
}
