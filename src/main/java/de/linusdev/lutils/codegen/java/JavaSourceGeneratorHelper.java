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

package de.linusdev.lutils.codegen.java;

import de.linusdev.lutils.codegen.GeneratorState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public interface JavaSourceGeneratorHelper {


    String SEMICOLON = ";";
    String LINE_BREAK = "\n";
    String INDENT = "    ";
    String CURLY_BRACKET_OPEN = "{";
    String CURLY_BRACKET_CLOSE = "}";
    String ASTERISK = "*";
    String AT = "@";
    String BRACKET_OPEN = "(";
    String BRACKET_CLOSE = ")";
    String EQUALS = "=";

    // Keywords
    String PACKAGE = "package";
    String IMPORT = "import";
    String EXTENDS = "extends";
    String IMPLEMENTS = "implements";
    String STATIC = "static";
    String FINAL = "final";
    String NULL = "null";
    String SUPER = "super";

    // Class Types
    String ENUM = "enum";
    String CLASS = "class";
    String INTERFACE = "interface";
    String RECORD = "record";

    // Visibility
    String PUBLIC = "public";
    String PRIVATE = "private";
    String PROTECTED = "protected";

    static @NotNull JavaSourceGeneratorHelper getDefault() {
        return new JavaSourceGeneratorHelper() {};
    }

    default String javaNull() {
        return NULL;
    }

    default String javaExpressionEnd() {
        return SEMICOLON;
    }

    default @NotNull Path javaSourcePath() {
        return Paths.get("src", "main", "java");
    }

    default @NotNull Path javaResourcesPath() {
        return Paths.get("src", "main", "resources");
    }

    default String javaLineBreak() {
        return LINE_BREAK;
    }

    default String javaIndent() {
        return INDENT;
    }

    default String javaPackageDeclarationStart() {
        return PACKAGE + " ";
    }

    default String javaEnumName() {
        return ENUM;
    }

    default String javaClassName() {
        return CLASS;
    }

    default String javaRecordName() {
        return RECORD;
    }

    default String javaInterfaceName() {
        return INTERFACE;
    }

    default String javaPublic() {
        return PUBLIC;
    }

    default String javaProtected() {
        return PROTECTED;
    }

    default String javaPrivate() {
        return PRIVATE;
    }

    default String javaPackagePrivate() {
        return "";
    }

    default String javaImportStart() {
        return IMPORT + " ";
    }

    default String javaBlockStart() {
        return CURLY_BRACKET_OPEN;
    }

    default String javaBlockEnd() {
        return CURLY_BRACKET_CLOSE;
    }

    default String javaClassEndExpression() {
        return CURLY_BRACKET_CLOSE;
    }

    default String javaExtendsKeyword() {
        return EXTENDS;
    }

    default String javaImplementsKeyword() {
        return IMPLEMENTS;
    }

    default String javaStaticKeyword() {
        return STATIC;
    }

    default String javaNativeKeyword() {
        return "native";
    }

    default String javaFinalKeyword() {
        return FINAL;
    }

    default String javaAnnotationStart() {
        return AT;
    }

    default String javaAssign() {
        return EQUALS;
    }

    default String javaSuper() {
        return SUPER;
    }

    default String javaMethodCall(@NotNull String methodName, @NotNull JavaExpression @NotNull ... parameters) {
        StringBuilder str = new StringBuilder(methodName + BRACKET_OPEN);

        boolean first = true;
        for(JavaExpression parameter : parameters) {
            if(first) first = false;
            else str.append(", ");
            str.append(parameter.getExprString(this));
        }

        return str.toString() + BRACKET_CLOSE;
    }

    default String javaStaticMethodCall(@NotNull JavaClass parent, @NotNull String methodName, @NotNull JavaExpression @NotNull ... parameters) {
        StringBuilder str = new StringBuilder(parent.getName() + "." + methodName + BRACKET_OPEN);

        boolean first = true;
        for(JavaExpression parameter : parameters) {
            if(first) first = false;
            else str.append(", ");
            str.append(parameter.getExprString(this));
        }

        return str.toString() + BRACKET_CLOSE;
    }

    default String javaVariableType(@NotNull JavaClass javaClass) {
        return javaClass.getTypeName();
    }

    default String javaMethodOpenExpression(
            @NotNull JavaMethodGenerator method
    ) throws IOException {
        StringBuilder str = new StringBuilder(method.getVisibility().getName(this));

        if(!str.toString().isBlank())
            str.append(" ");

        if(method.isStatic())
            str.append(javaStaticKeyword()).append(" ");

        if(method.isNative())
            str.append(javaNativeKeyword()).append(" ");

        if(method.isFinal())
            str.append(javaFinalKeyword()).append(" ");

        if(method.isConstructor) {
            str.append(javaVariableType(method.getReturnType())).append(BRACKET_OPEN);
        } else {
            str.append(javaVariableType(method.getReturnType())).append(" ");
            str.append(method.getName()).append(BRACKET_OPEN);
        }

        boolean first = true;
        for(JavaLocalVariable param : method.getParameters()) {
            if(first) first = false;
            else str.append(", ");
            param.write(str, new GeneratorState<>("", this));
        }

        str.append(BRACKET_CLOSE + " ");

        if(!method.isNoBody())
            str.append(javaBlockStart());

        return str + "";
    }

    default String javaMethodCloseExpression(@NotNull JavaMethodGenerator method) {
        if(method.isNoBody())
            return ";";
        return javaBlockEnd();
    }

    default String javaVariableExpression(
            @NotNull JavaVariable variable
    ) {
        String str = variable.getVisibility().getName(this);

        if(!str.isBlank())
            str += " ";

        if(variable.isStatic())
            str += javaStaticKeyword() + " ";

        if(variable.isFinal())
            str += javaFinalKeyword() + " ";

        str += javaVariableType(variable.getType()) + " ";
        str += variable.getName();

        if(variable.getDefaultValue() != null)
            str += " " + javaAssign() + " " + variable.getDefaultValue().getExprString(this);

        return str;
    }

    default String javaDeclareLocalVariableExpression(
            @NotNull JavaLocalVariable variable
    ) {
        String str = "";

        if(variable.isFinal())
            str += javaFinalKeyword() + " ";

        str += javaVariableType(variable.getType()) + " ";
        str += variable.getName();

        if(variable.getDefaultValue() != null)
            str += " " + javaAssign() + " " + variable.getDefaultValue().getExprString(this);

        return str;
    }

    /**
     * package [name.of.package];
     */
    default String javaPackageExpression(@NotNull JavaPackage jPackage) {
        return javaPackageDeclarationStart() + jPackage.getPackageString() + javaExpressionEnd();
    }

    default String javaAnnotation(@NotNull JavaAnnotation annotation) {
        StringBuilder str = new StringBuilder(javaAnnotationStart() + annotation.getType().getName());

        if(annotation.getValues() != null) {
            str.append(BRACKET_OPEN);
            boolean first = true;

            for(Map.Entry<JavaVariable, JavaExpression> entry : annotation.getValues().entrySet()) {
                if(first) first = false;
                else str.append(", ");
                str
                        .append(entry.getKey().getName())
                        .append(" ").append(javaAssign()).append(" ")
                        .append(entry.getValue().getExprString(this));
            }
            str.append(BRACKET_CLOSE);
        }

        return str.toString();
    }

    default String javaClassOpenExpression(
            @NotNull JavaVisibility visibility,
            boolean isStatic,
            @NotNull JavaClassType type,
            @NotNull String name,
            @Nullable JavaClass extendClass,
            JavaClass @NotNull ... implementsClasses
    ) {

        StringBuilder str = new StringBuilder(visibility.getName(this));
        if(!str.toString().isBlank())
            str.append(" ");

        if(isStatic)
            str.append(javaStaticKeyword()).append(" ");

        str.append(type.getName(this)).append(" ").append(name);

        if(extendClass != null) {
            str.append(" ")
                    .append(javaExtendsKeyword()).append(" ")
                    .append(extendClass.getTypeName());
        }

        if(implementsClasses.length != 0) {
            str.append(" ").append(javaImplementsKeyword()).append(" ");
            str.append(implementsClasses[0].getTypeName());
            for (int i = 1; i < implementsClasses.length; i++) {
                str.append(", ").append(implementsClasses[i].getTypeName());
            }
        }

        return str + " " + javaBlockStart();
    }

    default String javaImportExpression(
            @NotNull JavaImport jImport
    ) {
        return javaImportStart() + jImport.getPackage().getPackageString() + "." + jImport.getClassName()
                + (jImport.getVariable() == null ? "" : ("." + jImport.getVariable())) + javaExpressionEnd();
    }

    default String javaNumberExpression(@NotNull Number number) {

        if(number instanceof Long)
            return number + "L";

        return number.toString();
    }

    default String javaBooleanExpression(boolean bool) {
        return Boolean.toString(bool);
    }


}
