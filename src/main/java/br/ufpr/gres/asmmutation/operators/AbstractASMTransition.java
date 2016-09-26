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
package br.ufpr.gres.asmmutation.operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import br.ufpr.gres.asmmutation.carma.ITransition;
import br.ufpr.gres.asmmutation.carma.Mutant;
import br.ufpr.gres.asmmutation.carma.SourceCodeMapping;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;

/**
 *
 * Abstract mutation transformation processing class. Does the common traversing
 * logic over the byte code.
 *
 * @author arau
 *
 * TODO: arau: should be implemented using delegation instead of inheritance.
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 *
 */
public abstract class AbstractASMTransition implements ITransition {

    public List<Mutant> applyTransitions(byte[] byteCode) {

        ClassNode classNode = new ClassNode();

        ClassReader reader = new ClassReader(byteCode);

        reader.accept(classNode, 0);

        List<Mutant> result = new ArrayList<Mutant>();

        int methodIndex = 0;

        for (MethodNode methodNode : (List<MethodNode>) classNode.methods) {

            Iterator<AbstractInsnNode> instructionIterator = methodNode.instructions.iterator();

            while (instructionIterator.hasNext()) {

                AbstractInsnNode node = instructionIterator.next();

                if (isApplicable(node)) {
                    SourceCodeMapping sourceMapping = new SourceCodeMapping();

                    if (node instanceof LineNumberNode) {
                        sourceMapping.setLineStart(((LineNumberNode) node).line << 10);
                        sourceMapping.setLineEnd(((LineNumberNode) node).line << 10);
                        continue;
                    }

                    this.checkNode(classNode, methodNode, result, sourceMapping, node);
                }
            }
            methodIndex++;

        }

        return result;
    }

    protected abstract void checkNode(ClassNode classNode, MethodNode methodNode, List<Mutant> result, SourceCodeMapping sourceMapping, AbstractInsnNode node);
}
