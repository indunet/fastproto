/*
 * Copyright 2019-2024 indunet.org
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

package org.indunet.fastproto.processor;

import org.indunet.fastproto.annotation.DecodingFormula;
import org.indunet.fastproto.annotation.EncodingFormula;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Annotation processor that generates Function implementation classes for
 * {@link DecodingFormula} and {@link EncodingFormula} annotations with lambda expressions.
 *
 * <p>This processor scans fields annotated with these annotations, extracts the lambda expressions,
 * and generates corresponding Function implementation classes at compile time.
 * This eliminates the need for runtime dynamic compilation, making FastProto compatible
 * with Android and Java 11+ JRE environments.
 *
 * <p>Generated classes are placed in the {@code org.indunet.fastproto.formula.generated} package
 * and registered in a {@code FormulaRegistry} class for runtime lookup.
 *
 * @author Deng Ran
 * @since 3.13.0
 */
@SupportedAnnotationTypes({
        "org.indunet.fastproto.annotation.DecodingFormula",
        "org.indunet.fastproto.annotation.EncodingFormula"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class FormulaProcessor extends AbstractProcessor {

    private static final String GENERATED_PACKAGE = "org.indunet.fastproto.formula.generated";
    private static final Pattern LAMBDA_PATTERN = Pattern.compile("^\\s*(\\w+)\\s*->\\s*(.+)$");

    // Map from data type annotation simple name to Java type
    private static final Map<String, String> DATA_TYPE_MAP = new HashMap<>();

    static {
        DATA_TYPE_MAP.put("BoolType", "Boolean");
        DATA_TYPE_MAP.put("BoolArrayType", "boolean[]");
        DATA_TYPE_MAP.put("AsciiType", "Character");
        DATA_TYPE_MAP.put("AsciiArrayType", "char[]");
        DATA_TYPE_MAP.put("CharType", "Character");
        DATA_TYPE_MAP.put("CharArrayType", "char[]");
        DATA_TYPE_MAP.put("Int8Type", "Integer");
        DATA_TYPE_MAP.put("Int8ArrayType", "int[]");
        DATA_TYPE_MAP.put("BinaryType", "byte[]");
        DATA_TYPE_MAP.put("Int16Type", "Integer");
        DATA_TYPE_MAP.put("Int16ArrayType", "int[]");
        DATA_TYPE_MAP.put("Int32Type", "Integer");
        DATA_TYPE_MAP.put("Int32ArrayType", "int[]");
        DATA_TYPE_MAP.put("Int64Type", "Long");
        DATA_TYPE_MAP.put("Int64ArrayType", "long[]");
        DATA_TYPE_MAP.put("UInt8Type", "Integer");
        DATA_TYPE_MAP.put("UInt8ArrayType", "int[]");
        DATA_TYPE_MAP.put("UInt16Type", "Integer");
        DATA_TYPE_MAP.put("UInt16ArrayType", "int[]");
        DATA_TYPE_MAP.put("UInt32Type", "Long");
        DATA_TYPE_MAP.put("UInt32ArrayType", "long[]");
        DATA_TYPE_MAP.put("UInt64Type", "java.math.BigInteger");
        DATA_TYPE_MAP.put("UInt64ArrayType", "java.math.BigInteger[]");
        DATA_TYPE_MAP.put("FloatType", "Float");
        DATA_TYPE_MAP.put("FloatArrayType", "float[]");
        DATA_TYPE_MAP.put("DoubleType", "Double");
        DATA_TYPE_MAP.put("DoubleArrayType", "double[]");
        DATA_TYPE_MAP.put("TimeType", "java.util.Date");
        DATA_TYPE_MAP.put("StringType", "String");
        DATA_TYPE_MAP.put("EnumType", "Enum");
        DATA_TYPE_MAP.put("BcdType", "Integer");
        DATA_TYPE_MAP.put("BitFieldType", "Integer");
    }

    private Filer filer;
    private Messager messager;

    // Collect all formula entries for registry generation
    private final List<FormulaEntry> formulaEntries = new ArrayList<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // Process DecodingFormula annotations
        for (Element element : roundEnv.getElementsAnnotatedWith(DecodingFormula.class)) {
            if (element.getKind() != ElementKind.FIELD) {
                continue;
            }
            processDecodingFormula((VariableElement) element);
        }

        // Process EncodingFormula annotations
        for (Element element : roundEnv.getElementsAnnotatedWith(EncodingFormula.class)) {
            if (element.getKind() != ElementKind.FIELD) {
                continue;
            }
            processEncodingFormula((VariableElement) element);
        }

        // Generate FormulaRegistry if we have any entries and this is the final round
        if (roundEnv.processingOver() && !formulaEntries.isEmpty()) {
            generateFormulaRegistry();
        }

        return false; // Allow other processors to process these annotations
    }

    private void processDecodingFormula(VariableElement field) {
        DecodingFormula annotation = field.getAnnotation(DecodingFormula.class);
        String lambda = annotation.lambda();

        if (lambda == null || lambda.isEmpty()) {
            return; // Skip if no lambda expression
        }

        String inputType = findInputTypeForDecoding(field);
        if (inputType == null) {
            messager.printMessage(Diagnostic.Kind.WARNING,
                    "Cannot determine input type for @DecodingFormula lambda. " +
                    "Please ensure the field has a data type annotation (e.g., @Int16Type).",
                    field);
            return;
        }

        String className = generateFormulaClass(field, lambda, inputType, "Decoding");
        if (className != null) {
            String key = buildRegistryKey(field, "decoding");
            formulaEntries.add(new FormulaEntry(key, className));
        }
    }

    private void processEncodingFormula(VariableElement field) {
        EncodingFormula annotation = field.getAnnotation(EncodingFormula.class);
        String lambda = annotation.lambda();

        if (lambda == null || lambda.isEmpty()) {
            return; // Skip if no lambda expression
        }

        // For encoding, the input type is the field's declared type
        TypeMirror fieldType = field.asType();
        String inputType = getBoxedTypeName(fieldType);

        String className = generateFormulaClass(field, lambda, inputType, "Encoding");
        if (className != null) {
            String key = buildRegistryKey(field, "encoding");
            formulaEntries.add(new FormulaEntry(key, className));
        }
    }

    private String findInputTypeForDecoding(VariableElement field) {
        // Look for data type annotations on the field
        for (AnnotationMirror annotationMirror : field.getAnnotationMirrors()) {
            String annotationName = annotationMirror.getAnnotationType().asElement().getSimpleName().toString();
            if (DATA_TYPE_MAP.containsKey(annotationName)) {
                return DATA_TYPE_MAP.get(annotationName);
            }
        }
        return null;
    }

    private String getBoxedTypeName(TypeMirror type) {
        String typeName = type.toString();
        // Convert primitive types to boxed types
        switch (typeName) {
            case "boolean": return "Boolean";
            case "byte": return "Byte";
            case "char": return "Character";
            case "short": return "Short";
            case "int": return "Integer";
            case "long": return "Long";
            case "float": return "Float";
            case "double": return "Double";
            default: return typeName;
        }
    }

    private String generateFormulaClass(VariableElement field, String lambda, String inputType, String formulaType) {
        TypeElement enclosingClass = (TypeElement) field.getEnclosingElement();
        String enclosingClassName = enclosingClass.getSimpleName().toString();
        String fieldName = field.getSimpleName().toString();
        String className = enclosingClassName + "_" + fieldName + "_" + formulaType + "Formula";
        String qualifiedName = GENERATED_PACKAGE + "." + className;

        // Parse the lambda expression
        Matcher matcher = LAMBDA_PATTERN.matcher(lambda.trim());
        if (!matcher.matches()) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    "Invalid lambda expression format: " + lambda + ". Expected format: 'x -> expression'",
                    field);
            return null;
        }

        String paramName = matcher.group(1);
        String expression = matcher.group(2).trim();
        // Remove trailing semicolon if present
        if (expression.endsWith(";")) {
            expression = expression.substring(0, expression.length() - 1).trim();
        }

        try {
            JavaFileObject file = filer.createSourceFile(qualifiedName, field);
            try (Writer writer = file.openWriter()) {
                writer.write("/*\n");
                writer.write(" * Generated by FastProto FormulaProcessor.\n");
                writer.write(" * Do not modify this file manually.\n");
                writer.write(" */\n\n");
                writer.write("package " + GENERATED_PACKAGE + ";\n\n");
                writer.write("import java.util.function.Function;\n\n");
                writer.write("/**\n");
                writer.write(" * Generated " + formulaType.toLowerCase() + " formula for field " + fieldName + "\n");
                writer.write(" * in class " + enclosingClass.getQualifiedName() + ".\n");
                writer.write(" * <p>Lambda: " + escapeJavadoc(lambda) + "\n");
                writer.write(" */\n");
                writer.write("public class " + className + " implements Function<" + inputType + ", Object> {\n\n");
                writer.write("    @Override\n");
                writer.write("    public Object apply(" + inputType + " " + paramName + ") {\n");
                writer.write("        return " + expression + ";\n");
                writer.write("    }\n");
                writer.write("}\n");
            }
            
            messager.printMessage(Diagnostic.Kind.NOTE,
                    "Generated formula class: " + qualifiedName, field);
            return qualifiedName;
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    "Failed to generate formula class: " + e.getMessage(), field);
            return null;
        }
    }

    private String buildRegistryKey(VariableElement field, String formulaType) {
        TypeElement enclosingClass = (TypeElement) field.getEnclosingElement();
        String className = enclosingClass.getQualifiedName().toString();
        String fieldName = field.getSimpleName().toString();
        return className + "#" + fieldName + "#" + formulaType;
    }

    private void generateFormulaRegistry() {
        String qualifiedName = GENERATED_PACKAGE + ".FormulaRegistry";

        try {
            JavaFileObject file = filer.createSourceFile(qualifiedName);
            try (Writer writer = file.openWriter()) {
                writer.write("/*\n");
                writer.write(" * Generated by FastProto FormulaProcessor.\n");
                writer.write(" * Do not modify this file manually.\n");
                writer.write(" */\n\n");
                writer.write("package " + GENERATED_PACKAGE + ";\n\n");
                writer.write("import java.util.HashMap;\n");
                writer.write("import java.util.Map;\n");
                writer.write("import java.util.function.Function;\n\n");
                writer.write("/**\n");
                writer.write(" * Registry of all generated formula classes.\n");
                writer.write(" * Used by FastProto at runtime to look up pre-compiled formulas.\n");
                writer.write(" */\n");
                writer.write("public final class FormulaRegistry {\n\n");
                writer.write("    private static final Map<String, Function<?, ?>> FORMULAS = new HashMap<>();\n\n");
                writer.write("    static {\n");
                for (FormulaEntry entry : formulaEntries) {
                    writer.write("        FORMULAS.put(\"" + entry.key + "\", new " + entry.className + "());\n");
                }
                writer.write("    }\n\n");
                writer.write("    private FormulaRegistry() {\n");
                writer.write("        // Utility class\n");
                writer.write("    }\n\n");
                writer.write("    /**\n");
                writer.write("     * Get a pre-compiled formula by its registry key.\n");
                writer.write("     *\n");
                writer.write("     * @param key the registry key in format \"className#fieldName#formulaType\"\n");
                writer.write("     * @return the formula function, or null if not found\n");
                writer.write("     */\n");
                writer.write("    @SuppressWarnings(\"unchecked\")\n");
                writer.write("    public static <T, R> Function<T, R> get(String key) {\n");
                writer.write("        return (Function<T, R>) FORMULAS.get(key);\n");
                writer.write("    }\n\n");
                writer.write("    /**\n");
                writer.write("     * Check if a formula exists in the registry.\n");
                writer.write("     *\n");
                writer.write("     * @param key the registry key\n");
                writer.write("     * @return true if the formula exists\n");
                writer.write("     */\n");
                writer.write("    public static boolean contains(String key) {\n");
                writer.write("        return FORMULAS.containsKey(key);\n");
                writer.write("    }\n");
                writer.write("}\n");
            }

            messager.printMessage(Diagnostic.Kind.NOTE,
                    "Generated FormulaRegistry with " + formulaEntries.size() + " entries.");
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    "Failed to generate FormulaRegistry: " + e.getMessage());
        }
    }

    private String escapeJavadoc(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("@", "&#64;");
    }

    /**
     * Represents a formula entry for the registry.
     */
    private static class FormulaEntry {
        final String key;
        final String className;

        FormulaEntry(String key, String className) {
            this.key = key;
            this.className = className;
        }
    }
}
