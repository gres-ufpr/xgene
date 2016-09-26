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
package br.ufpr.gres.asmmutation.operators.methodlevel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import br.ufpr.gres.asmmutation.carma.Mutant;
import br.ufpr.gres.asmmutation.carma.SourceCodeMapping;
import br.ufpr.gres.asmmutation.operators.AbstractASMTransition;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * <p>
 * Generate AOR (Arithmetic Operator Replacement) mutants
 * </p>
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class AOR extends AbstractASMTransition {

    private static Set<Integer> opcodesDouble = new HashSet<Integer>();
    private static Set<Integer> opcodesFloat = new HashSet<Integer>();
    private static Set<Integer> opcodesInt = new HashSet<Integer>();
    private static Set<Integer> opcodesLong = new HashSet<Integer>();

    private int numVariable = 1;

    static {
        opcodesInt.addAll(Arrays.asList(new Integer[]{
            Opcodes.IADD,
            Opcodes.ISUB,
            Opcodes.IMUL,
            Opcodes.IDIV,
            Opcodes.IREM
        }));

        opcodesLong.addAll(Arrays.asList(new Integer[]{
            Opcodes.LADD,
            Opcodes.LSUB,
            Opcodes.LMUL,
            Opcodes.LDIV,
            Opcodes.LREM
        }));

        opcodesFloat.addAll(Arrays.asList(new Integer[]{
            Opcodes.FADD,
            Opcodes.FSUB,
            Opcodes.FMUL,
            Opcodes.FDIV,
            Opcodes.FREM
        }));

        opcodesDouble.addAll(Arrays.asList(new Integer[]{
            Opcodes.DADD,
            Opcodes.DSUB,
            Opcodes.DMUL,
            Opcodes.DDIV,
            Opcodes.DREM
        }));
    }

    private Set<Integer> getMutations(int opcode) {
        Set<Integer> replacement = new HashSet<Integer>();
        if (opcodesInt.contains(opcode)) {
            replacement.addAll(opcodesInt);
        } else {
            if (opcodesLong.contains(opcode)) {
                replacement.addAll(opcodesLong);
            } else {
                if (opcodesFloat.contains(opcode)) {
                    replacement.addAll(opcodesFloat);
                } else {
                    if (opcodesDouble.contains(opcode)) {
                        replacement.addAll(opcodesDouble);
                    }
                }
            }
        }

        replacement.remove(opcode);
        return replacement;
    }

    @Override
    protected void checkNode(ClassNode classNode, MethodNode methodNode, List<Mutant> result, SourceCodeMapping sourceMapping, AbstractInsnNode node) {
        if (isApplicable(node)) {
            if (node instanceof InsnNode) {
                InsnNode sourceNode = (InsnNode) node;
                for (int opcode : getMutations(sourceNode.getOpcode())) {
                    InsnNode mutation = new InsnNode(opcode);

                    methodNode.instructions.set(sourceNode, mutation);

                    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                    classNode.accept(cw);

                    Mutant mutant = new Mutant();
                    mutant.setName("AOR_" + numVariable);
                    mutant.setByteCode(cw.toByteArray());
                    mutant.setSourceMapping(sourceMapping);
                    mutant.setSurvived(true);
                    mutant.setTransition(this);

                    result.add(mutant);

                    methodNode.instructions.set(mutation, sourceNode);
                    numVariable++;
                }
            }
        }
    }

    @Override
    public String getName() {
        return "AOR";
    }

    private String getOp(int opcode) {
        switch (opcode) {
            case Opcodes.IADD:
            case Opcodes.LADD:
            case Opcodes.FADD:
            case Opcodes.DADD:
                return "+";
            case Opcodes.ISUB:
            case Opcodes.LSUB:
            case Opcodes.FSUB:
            case Opcodes.DSUB:
                return "-";
            case Opcodes.IMUL:
            case Opcodes.LMUL:
            case Opcodes.FMUL:
            case Opcodes.DMUL:
                return "*";
            case Opcodes.IDIV:
            case Opcodes.LDIV:
            case Opcodes.FDIV:
            case Opcodes.DDIV:
                return "/";
            case Opcodes.IREM:
            case Opcodes.LREM:
            case Opcodes.FREM:
            case Opcodes.DREM:
                return "%";
        }
        throw new RuntimeException("Unknown opcode: " + opcode);
    }

    public boolean isApplicable(AbstractInsnNode node) {
        int opcode = node.getOpcode();
        if (opcodesInt.contains(opcode)) {
            return true;
        } else {
            if (opcodesLong.contains(opcode)) {
                return true;
            } else {
                if (opcodesFloat.contains(opcode)) {
                    return true;
                } else {
                    if (opcodesDouble.contains(opcode)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "Arithmetic Operator Replacement";
    }
}
