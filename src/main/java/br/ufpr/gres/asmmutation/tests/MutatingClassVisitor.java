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

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class MutatingClassVisitor extends ClassVisitor {

    public MutatingClassVisitor() {        
        super(Opcodes.ASM5);
        
        // Passa como parâmetro os operadores de mutação
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces); 
    }

    
    @Override
    public MethodVisitor visitMethod(int access, String methodName, String methodDescriptor, String signature, String[] exceptions) {
        
        // criar alguma interface pra operador de mutação e realizar o apply caso possa aplicar o operador, algo assim
        // além disso, verificar se há como filtrar onde posso aplicar mutação como o pitest faz
        
        //final MethodVisitor methodVisitor = this.cv.visitMethod(access, methodName, methodDescriptor, signature, exceptions);
        /*for(Operator op : operators){
           op.apply(); 
        }*/
        return super.visitMethod(access, methodName, methodDescriptor, signature, exceptions); 
    }   

    @Override
    public void visitSource(String source, String debug) {
        super.visitSource(source, debug); 
    }        
}
