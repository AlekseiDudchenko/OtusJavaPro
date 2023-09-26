package de.dudchenko.customannotations.processors;

import com.google.auto.service.AutoService;
import de.dudchenko.customannotations.CustomToString;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes("de.dudchenko.customannotations.CustomToString")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class CustomAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(CustomToString.class)) {
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;
                generateToString(typeElement);
            }
        }
        return true;
    }

    private void generateToString(TypeElement typeElement) {
        String className = typeElement.getSimpleName().toString();
        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();

        String classCode = "package " + packageName + ";\n" +
                "public class " + className + "Generated {\n" +
                "    @Override\n" +
                "    public String toString() {\n" +
                "        return \"" + className + " toString()\";\n" +
                "    }\n" +
                "}\n";

        try {
            JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(packageName + "." + className + "Generated");
            try (Writer writer = fileObject.openWriter()) {
                writer.write(classCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
