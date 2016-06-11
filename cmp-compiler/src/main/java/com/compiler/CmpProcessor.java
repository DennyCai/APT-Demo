package com.compiler;



import com.cursormapper.annotation.Columns;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public class CmpProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
//        processingEnv.getFiler().
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement el : annotations) {
            printNote(el.getQualifiedName().toString());
            Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(el);
            for (Element e : set) {
                printNote(e.getEnclosingElement().getSimpleName().toString());
                printNote(e.getEnclosingElement().getClass().getName());
            }
        }
        return true;
    }

    private void printNote(String str) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Note:" + str);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(Columns.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
