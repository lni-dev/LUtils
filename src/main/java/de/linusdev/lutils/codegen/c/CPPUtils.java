/*
 * Copyright (c) 2024 Linus Andera
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

package de.linusdev.lutils.codegen.c;

import de.linusdev.lutils.codegen.java.JavaMethod;
import org.jetbrains.annotations.NotNull;

public interface CPPUtils {

    static @NotNull String reinterpretCast(@NotNull String typeToCastTo, @NotNull String expression) {
        return "reinterpret_cast<" + typeToCastTo + ">(" + expression + ")";
    }

    static @NotNull String staticCast(@NotNull String typeToCastTo, @NotNull String expression) {
        return "static_cast<" + typeToCastTo + ">(" + expression + ")";
    }

    static @NotNull String callLocalFun(
            @NotNull String funName,
            @NotNull String... params
    ) {

        StringBuilder ret = new StringBuilder(funName + "(");

        if(params.length > 0) {
            ret.append(params[0]);
            for (int i = 1; i < params.length; i++) {
                ret.append(", ").append(params[i]);
            }
        }

        return ret + ")";
    }

    static @NotNull String declareAndAssign(
            @NotNull String type,
            @NotNull String variable,
            @NotNull String expression
    ) {
        return type + " " + variable + " = " + expression;
    }

    static @NotNull String indent(int count) {
        return "    ".repeat(count);
    }

    static @NotNull String block(
            int currentIndentCount,
            @NotNull String... expressions
    ) {
        StringBuilder ret = new StringBuilder("\n" + indent(currentIndentCount) + "{");

        for (String expression : expressions) {
            ret.append("\n").append(indent(currentIndentCount + 1)).append(expression).append(";");
        }

        ret.append("\n").append(indent(currentIndentCount)).append("}");
        return ret.toString();
    }

    static @NotNull String funDeclaration(
            @NotNull String beforeType,
            @NotNull String type,
            @NotNull String afterType,
            @NotNull String funName,
            @NotNull String[] paramTypes,
            @NotNull String... paramNames
    ) {
        StringBuilder ret = new StringBuilder(beforeType + " " + type + " " + afterType + " " + funName + "(");

        if(paramTypes.length > 0) {
            ret.append(paramTypes[0]).append(" ").append(paramNames[0]);
            for (int i = 1; i < paramTypes.length; i++) {
                ret.append(", ").append(paramTypes[i]).append(" ").append(paramNames[i]);
            }
        }

        return ret + ")";
    }

    static @NotNull String returnExpression(
            @NotNull String expressionToReturn
    ) {
        return "return " + expressionToReturn;
    }

    static @NotNull String jniJavaFunName(
            @NotNull JavaMethod method
    ) {
        StringBuilder ret = new StringBuilder("Java");

        for (String s : method.getParentClass().getPackage().getArray()) {
            ret.append("_").append(s);
        }

        ret.append("_").append(method.getParentClass().getName());
        ret.append("_").append(method.getName());

        return ret.toString();
    }

    static @NotNull String typedefFunPointer(
            @NotNull String retType,
            @NotNull String call,
            @NotNull String name,
            @NotNull String[] paramTypes

    ) {
        StringBuilder ret = new StringBuilder("typedef " + retType + " (" + call + " *" + name + ")(");

        if(paramTypes.length > 0) {
            ret.append(paramTypes[0]);
            for (int i = 1; i < paramTypes.length; i++) {
                ret.append(", ").append(paramTypes[i]);
            }
        }

        return ret + ");";
    }

    static @NotNull String JNI_EXPORT() {
        return "JNIEXPORT";
    }

    static @NotNull String JNI_CALL() {
        return "JNICALL";
    }

}
