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

import java.io.Serializable;
import java.util.Set;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.SourceValue;

/**
 * Internal representation of a BytecodeInstruction
 * <p>
 * Extends ASMWrapper which serves as an interface to the ASM library.
 * <p>
 * Known super classes are DefUse and Branch which yield specific functionality
 * needed to achieve theirs respective coverage criteria
 * <p>
 * Old: Node of the control flow graph
 *
 * @author Gordon Fraser, Andre Mis
 */
public class BytecodeInstruction extends ASMWrapper implements Serializable,
                                                               Comparable<BytecodeInstruction> {

    private static final long serialVersionUID = 3630449183355518857L;
    protected int bytecodeOffset;

    // identification of a byteCode instruction inside EvoSuite
    protected ClassLoader classLoader;
    protected String className;
    protected int instructionId;

    // auxiliary information
    private int lineNumber = -1;
    protected String methodName;

    /**
     * Generates a ByteCodeInstruction instance that represents a byteCode
     * instruction as indicated by the given ASMNode in the given method and
     * class
     *
     * @param className
     *                       a {@link java.lang.String} object.
     * @param methodName
     *                       a {@link java.lang.String} object.
     * @param instructionId
     *                       a int.
     * @param bytecodeOffset
     *                       a int.
     * @param asmNode
     *                       a {@link org.objectweb.asm.tree.AbstractInsnNode} object.
     */
    public BytecodeInstruction(ClassLoader classLoader, String className,
                               String methodName, int instructionId, int bytecodeOffset, AbstractInsnNode asmNode) {

        if (className == null || methodName == null || asmNode == null) {
            throw new IllegalArgumentException("null given");
        }
        if (instructionId < 0) {
            throw new IllegalArgumentException(
                    "expect instructionId to be positive, not " + instructionId);
        }

        this.instructionId = instructionId;
        this.bytecodeOffset = bytecodeOffset;
        this.asmNode = asmNode;

        this.classLoader = classLoader;

        setClassName(className);
        setMethodName(methodName);
    }

    /**
     * <p>
     * Constructor for BytecodeInstruction.
     * </p>
     *
     * @param className
     *                       a {@link java.lang.String} object.
     * @param methodName
     *                       a {@link java.lang.String} object.
     * @param instructionId
     *                       a int.
     * @param bytecodeOffset
     *                       a int.
     * @param asmNode
     *                       a {@link org.objectweb.asm.tree.AbstractInsnNode} object.
     * @param lineNumber
     *                       a int.
     */
    public BytecodeInstruction(ClassLoader classLoader, String className,
                               String methodName, int instructionId, int bytecodeOffset, AbstractInsnNode asmNode,
                               int lineNumber) {

        this(classLoader, className, methodName, instructionId, bytecodeOffset, asmNode);

        if (lineNumber != -1) {
            setLineNumber(lineNumber);
        }
    }
    // inherited from Object
    
    /*
    * (non-Javadoc)
    *
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    */
    @Override
    public int compareTo(BytecodeInstruction o) {
        return getLineNumber() - o.getLineNumber();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BytecodeInstruction other = (BytecodeInstruction) obj;
        if (className == null) {
            if (other.className != null) {
                return false;
            }
        } else {
            if (!className.equals(other.className)) {
                return false;
            }
        }
        if (instructionId != other.instructionId) {
            return false;
        }
        if (methodName == null) {
            if (other.methodName != null) {
                return false;
            }
        } else {
            if (!methodName.equals(other.methodName)) {
                return false;
            }
        }
        return true;
    }
    /**
     * <p>
     * getASMNodeString
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getASMNodeString() {
        String type = getType();
        String opcode = getInstructionType();
        
        String stack = "";
        stack = "null";
        
        if (asmNode instanceof LabelNode) {
            return "LABEL " + ((LabelNode) asmNode).getLabel().toString();
        } else {
            if (asmNode instanceof FieldInsnNode) {
                return "Field" + " " + ((FieldInsnNode) asmNode).owner + "."
                       + ((FieldInsnNode) asmNode).name + " Type=" + type
                       + ", Opcode=" + opcode;
            } else {
                if (asmNode instanceof FrameNode) {
                    return "Frame" + " " + asmNode.getOpcode() + " Type=" + type
                           + ", Opcode=" + opcode;
                } else {
                    if (asmNode instanceof IincInsnNode) {
                        return "IINC " + ((IincInsnNode) asmNode).var + " Type=" + type
                               + ", Opcode=" + opcode;
                    } else {
                        if (asmNode instanceof InsnNode) {
                            return "" + opcode;
                        } else {
                            if (asmNode instanceof IntInsnNode) {
                                return "INT " + ((IntInsnNode) asmNode).operand + " Type=" + type
                                       + ", Opcode=" + opcode;
                            } else {
                                if (asmNode instanceof MethodInsnNode) {
                                    return opcode + " " + ((MethodInsnNode) asmNode).owner + "." + ((MethodInsnNode) asmNode).name + ((MethodInsnNode) asmNode).desc;
                                } else {
                                    if (asmNode instanceof JumpInsnNode) {
                                        return "JUMP " + ((JumpInsnNode) asmNode).label.getLabel()
                                               + " Type=" + type + ", Opcode=" + opcode + ", Stack: "
                                               + stack + " - Line: " + lineNumber;
                                    } else {
                                        if (asmNode instanceof LdcInsnNode) {
                                            return "LDC " + ((LdcInsnNode) asmNode).cst + " Type=" + type; // +
                                        } // ", Opcode=";
                                        // + opcode; // cst starts with mutationid if
                                        // this is location of mutation
                                        else {
                                            if (asmNode instanceof LineNumberNode) {
                                                return "LINE " + " " + ((LineNumberNode) asmNode).line;
                                            } else {
                                                if (asmNode instanceof LookupSwitchInsnNode) {
                                                    return "LookupSwitchInsnNode" + " " + asmNode.getOpcode()
                                                           + " Type=" + type + ", Opcode=" + opcode;
                                                } else {
                                                    if (asmNode instanceof MultiANewArrayInsnNode) {
                                                        return "MULTIANEWARRAY " + " " + asmNode.getOpcode() + " Type="
                                                               + type + ", Opcode=" + opcode;
                                                    } else {
                                                        if (asmNode instanceof TableSwitchInsnNode) {
                                                            return "TableSwitchInsnNode" + " " + asmNode.getOpcode() + " Type="
                                                                   + type + ", Opcode=" + opcode;
                                                        } else {
                                                            if (asmNode instanceof TypeInsnNode) {
                                                                switch (asmNode.getOpcode()) {
                                                                    case Opcodes.NEW:
                                                                        return "NEW " + ((TypeInsnNode) asmNode).desc;
                                                                    case Opcodes.ANEWARRAY:
                                                                        return "ANEWARRAY " + ((TypeInsnNode) asmNode).desc;
                                                                    case Opcodes.CHECKCAST:
                                                                        return "CHECKCAST " + ((TypeInsnNode) asmNode).desc;
                                                                    case Opcodes.INSTANCEOF:
                                                                        return "INSTANCEOF " + ((TypeInsnNode) asmNode).desc;
                                                                    default:
                                                                        return "Unknown node" + " Type=" + type + ", Opcode=" + opcode;
                                                                }
                                                            } // return "TYPE " + " " + node.getOpcode() + " Type=" + type
                                                            // + ", Opcode=" + opcode;
                                                            else {
                                                                if (asmNode instanceof VarInsnNode) {
                                                                    return opcode + " " + ((VarInsnNode) asmNode).var;
                                                                } else {
                                                                    return "Unknown node" + " Type=" + type + ", Opcode=" + opcode;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * <p>
     * getBytecodeOffset
     * </p>
     *
     * @return a int.
     */
    public int getBytecodeOffset() {
        return bytecodeOffset;
    }


    /**
     * <p>
     * Getter for the field <code>className</code>.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String getClassName() {
        return className;
    }
    private void setClassName(String className) {
        if (className == null) {
            throw new IllegalArgumentException("null given");
        }
        
        this.className = className;
    }
    @Override
    public String getFieldMethodCallName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    // --- Field Management ---
    /**
     * {@inheritDoc}
     */
    @Override
    public int getInstructionId() {
        return instructionId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLineNumber() {

        if (lineNumber == -1 && isLineNumber()) {
            retrieveLineNumber();
        }

        return lineNumber;
    }

    /**
     * <p>
     * Setter for the field <code>lineNumber</code>.
     * </p>
     *
     * @param lineNumber
     *                   a int.
     */
    public void setLineNumber(int lineNumber) {
        if (lineNumber <= 0) {
            throw new IllegalArgumentException(
                    "expect lineNumber value to be positive");
        }

        if (isLabel()) {
            return;
        }

        if (isLineNumber()) {
            int asmLine = super.getLineNumber();
            // sanity check
            if (lineNumber != -1 && asmLine != lineNumber) {
                throw new IllegalStateException(
                        "linenumber instruction has lineNumber field set to a value different from instruction linenumber");
            }
            this.lineNumber = asmLine;
        } else {
            this.lineNumber = lineNumber;
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getMethodName() {
        return methodName;
    }
    // getter + setter
    private void setMethodName(String methodName) {
        if (methodName == null) {
            throw new IllegalArgumentException("null given");
        }
        
        this.methodName = methodName;
    }
    /**
     * <p>
     * getName
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return "BytecodeInstruction " + instructionId + " in " + methodName;
    }

    @Override
    public BytecodeInstruction getSourceOfArrayReference() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * At first, if this instruction constitutes a line number instruction this
     * method tries to retrieve the lineNumber from the underlying asmNode and
     * set the lineNumber field to the value given by the asmNode.
     * <p>
     * This can lead to an IllegalStateException, should the lineNumber field
     * have been set to another value previously
     * <p>
     * After that, if the lineNumber field is still not initialized, this method
     * returns false Otherwise it returns true
     *
     * @return a boolean.
     */
    public boolean hasLineNumberSet() {
        retrieveLineNumber();
        return lineNumber != -1;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                 + ((className == null) ? 0 : className.hashCode());
        result = prime * result + instructionId;
        result = prime * result
                 + ((methodName == null) ? 0 : methodName.hashCode());
        return result;
    }

    @Override
    public boolean isFieldMethodCallDefinition() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isFieldMethodCallUse() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isMethodCallOfField() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    /**
     * <p>
     * printFrameInformation
     * </p>
     */
//    public void printFrameInformation() {
//        System.out.println("Frame STACK:");
//        for (int i = 0; i < frame.getStackSize(); i++) {
//            SourceValue v = (SourceValue) frame.getStack(i);
//            System.out.print(" " + i + "(" + v.insns.size() + "): ");
//            for (Object n : v.insns) {
//                AbstractInsnNode node = (AbstractInsnNode) n;
//                BytecodeInstruction ins = BytecodeInstructionPool.getInstance(classLoader).getInstruction(className,
//                                                                                                          methodName,
//                                                                                                          node);
//                System.out.print(ins.toString() + ", ");
//            }
//            System.out.println();
//        }
//
//        System.out.println("Frame LOCALS:");
//        for (int i = 1; i < frame.getLocals(); i++) {
//            SourceValue v = (SourceValue) frame.getLocal(i);
//            System.out.print(" " + i + "(" + v.insns.size() + "): ");
//            for (Object n : v.insns) {
//                AbstractInsnNode node = (AbstractInsnNode) n;
//                BytecodeInstruction ins = BytecodeInstructionPool.getInstance(classLoader).getInstruction(className,
//                                                                                                          methodName,
//                                                                                                          node);
//                System.out.print(ins.toString() + ", ");
//            }
//            System.out.println();
//        }
//    }
    /**
     * <p>
     * isWithinConstructor
     * </p>
     *
     * @return a boolean.
     */
    public boolean isWithinConstructor() {
        return getMethodName().startsWith("<init>");
    }
    /**
     * If the underlying ASMNode is a LineNumberNode the lineNumber field of
     * this instance will be set to the lineNumber contained in that
     * LineNumberNode
     * <p>
     * Should the lineNumber field have been set to a value different from that
     * contained in the asmNode, this method throws an IllegalStateExeption
     */
    private void retrieveLineNumber() {
        if (isLineNumber()) {
            int asmLine = super.getLineNumber();
            // sanity check
            if (this.lineNumber != -1 && asmLine != this.lineNumber) {
                throw new IllegalStateException(
                        "lineNumber field was manually set to a value different from the actual lineNumber contained in LineNumberNode");
            }
            this.lineNumber = asmLine;
        }
    }


}
