/*
 * Copyright (c) 2024-2026 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.linusdev.lutils.nat.struct.utils;

import de.linusdev.lutils.codegen.java.JavaClass;
import de.linusdev.lutils.codegen.java.JavaMethod;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * {@link Structure} static methods utils
 */
public interface SSMUtils {

    /**
     * Get the {@link StructureInfo} of given {@code structClass}.
     * @param structClass the class of the structure, whose info shall be acquired.
     * @param generator The {@link StaticGenerator} of the structure or {@code null} if it was not yet acquired. (will be automatically acquired by this method)
     * @see StaticGenerator#calculateInfo(Class, ABI, int[], Class[])  StaticGenerator.calculateInfo(...)
     */
    static @NotNull StructureInfo getInfo(
            @Nullable StaticGenerator generator,
            @NotNull Class<?> structClass,
            @Nullable ABI abi,
            int @Nullable [] length,
            @NotNull Class<?> @Nullable [] elementTypes
    ) {
        if(generator == null)
            generator = getGenerator(structClass);
        return generator.calculateInfo(structClass, abi, length, elementTypes);
    }

    /**
     * Get the {@link StaticGenerator} ({@link StructureStaticVariables#GENERATOR this}) of given {@code structClass}.
     * @param structClass the class of the structure, whose generator shall be acquired.
     * @see StructureStaticVariables#GENERATOR
     */
    static @NotNull StaticGenerator getGenerator(
            @NotNull Class<?> structClass
    ) {
        if(!Structure.class.isAssignableFrom(structClass))
            throw new IllegalStateException(structClass.getCanonicalName() + " does not extend Structure.");

        Field infoField;
        try {
            infoField = structClass.getField("GENERATOR");
            return (StaticGenerator) infoField.get(null);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(structClass.getCanonicalName() + " is missing 'public static StructureInfo GENERATOR' variable");
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot access 'public static StructureInfo INFO' variable of " + structClass.getCanonicalName());
        }

    }

    /**
     * @param structClass Class containing the static method
     * @return {@link StructureStaticVariables#newUnallocated() newUnallocated()} method of given {@code structClass}
     */
    static @NotNull StructCreationMethod getNewUnallocatedMethod(@NotNull Class<?> structClass) {
        return findMethod(structClass, "newUnallocated");
    }

    /**
     * @param structClass Class containing the static method
     * @return {@link StructureStaticVariables#newAllocatable(ABI, int[], Class[])  newAllocatable(StructValue)} method of given {@code structClass}
     */
    static @NotNull StructCreationMethod getNewAllocatableMethod(@NotNull Class<?> structClass) {
        return findMethod(structClass, "newAllocatable");
    }

    private static @NotNull StructCreationMethod findMethod(@NotNull Class<?> structClass, @NotNull String methodName) {
        for(Method method : structClass.getDeclaredMethods()) {
            if(Modifier.isStatic(method.getModifiers()) && method.getName().startsWith(methodName)) {
                return new StructCreationMethod(method);
            }
        }

        throw new UnsupportedOperationException("Given struct-class does not have a " + methodName + "() method.");
    }

    class StructCreationMethod implements JavaMethod {

        private final @NotNull Method method;
        private final @NotNull JavaClass parentClass;
        private final @NotNull JavaClass returnType;
        private final @NotNull String name;

        public StructCreationMethod(@NotNull Method method) {
            this.method = method;
            this.parentClass = JavaClass.ofClass(method.getDeclaringClass());
            this.returnType = JavaClass.ofClass(method.getReturnType());
            this.name = method.getName();
        }

        public @NotNull Method getMethod() {
            return method;
        }

        @Override
        public @NotNull JavaClass getParentClass() {
            return parentClass;
        }

        @Override
        public @NotNull JavaClass getReturnType() {
            return returnType;
        }

        @Override
        public @NotNull String getName() {
            return name;
        }

        @Override
        public boolean isStatic() {
            return true;
        }
    }

}
