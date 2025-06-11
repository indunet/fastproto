package org.indunet.fastproto.processor;

import org.indunet.fastproto.annotation.GenerateCodec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 * Simple annotation processor that generates codec wrapper classes.
 */
@SupportedAnnotationTypes("org.indunet.fastproto.annotation.GenerateCodec")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CodecProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Elements elements = processingEnv.getElementUtils();
        for (Element element : roundEnv.getElementsAnnotatedWith(GenerateCodec.class)) {
            if (!(element instanceof TypeElement)) {
                continue;
            }
            TypeElement type = (TypeElement) element;
            String pkg = elements.getPackageOf(type).getQualifiedName().toString();
            String simple = type.getSimpleName().toString();
            String className = simple + "Codec";
            String qualified = pkg.isEmpty() ? className : pkg + "." + className;
            try {
                JavaFileObject file = processingEnv.getFiler().createSourceFile(qualified, type);
                try (Writer writer = file.openWriter()) {
                    writer.write("package " + pkg + ";\n\n");
                    writer.write("public class " + className + " {\n");
                    writer.write("    public static " + simple + " decode(byte[] bytes) {\n");
                    writer.write("        return org.indunet.fastproto.FastProto.decode(bytes, " + simple + ".class);\n");
                    writer.write("    }\n");
                    writer.write("    public static byte[] encode(" + simple + " obj) {\n");
                    writer.write("        return org.indunet.fastproto.FastProto.encode(obj);\n");
                    writer.write("    }\n");
                    writer.write("}\n");
                }
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
        }
        return true;
    }
}
