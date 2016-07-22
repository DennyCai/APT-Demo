package com.compiler;



import com.cursormapper.annotation.Columns;
import com.cursormapper.annotation.Table;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

public class CmpProcessor extends AbstractProcessor {

    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement el : annotations) {
            printNote(el.getQualifiedName().toString());
            Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(el);
            for (Element e : set) {
                if(e.getKind().isClass()){

                }
            }
        }
        return true;
    }

    private void genSource() {
        //                    Element[] els = new Element[1];
        try {
            FileObject fo = mFiler.createSourceFile("com.denny.cursormapper.SampleActivity$Hello",null);
            Writer writer = fo.openWriter();
            BufferedWriter bw = new BufferedWriter(writer);
            bw.append("package com.denny.cursormapper;");
            bw.newLine();
            bw.append("public class SampleActivity$Hello{");
            bw.newLine();
            bw.append("\tpublic static void hello(){");
            bw.newLine();
            bw.append("\tSystem.out.println(\"Hello\");");
            bw.newLine();
            bw.append("}");
            bw.append("}");
            bw.flush();
            bw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void printNote(String str) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Note:" + str);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(Table.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
