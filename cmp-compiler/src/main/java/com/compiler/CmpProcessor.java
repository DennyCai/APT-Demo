package com.compiler;


import com.compiler.internal.BeanField;
import com.compiler.internal.TableClass;
import com.compiler.utils.Utils;
import com.cursormapper.annotation.Table;

import org.stringtemplate.v4.ST;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;

public class CmpProcessor extends AbstractProcessor {

    private Filer mFiler;
    private static Map<String,String> CURSOR_METHOD_MAP;
    private static Map<String,String> SQLITE_TYPE_MAP;
    static {
        CURSOR_METHOD_MAP = new HashMap<>();
        CURSOR_METHOD_MAP.put(String.class.getName(),"getString");
        CURSOR_METHOD_MAP.put(short.class.getName(),"getShort");
        CURSOR_METHOD_MAP.put(Short.class.getName(),"getShort");
        CURSOR_METHOD_MAP.put(int.class.getName(),"getInt");
        CURSOR_METHOD_MAP.put(Integer.class.getName(),"getInt");
        CURSOR_METHOD_MAP.put(long.class.getName(),"getLong");
        CURSOR_METHOD_MAP.put(Long.class.getName(),"getLong");
        CURSOR_METHOD_MAP.put(float.class.getName(),"getFloat");
        CURSOR_METHOD_MAP.put(Float.class.getName(),"getFloat");
        CURSOR_METHOD_MAP.put(double.class.getName(),"getDouble");
        CURSOR_METHOD_MAP.put(Double.class.getName(),"getDouble");
        SQLITE_TYPE_MAP = new HashMap<>();
        SQLITE_TYPE_MAP.put(String.class.getName(),"TEXT");
        SQLITE_TYPE_MAP.put(short.class.getName(),"INTEGER");
        SQLITE_TYPE_MAP.put(Short.class.getName(),"INTEGER");
        SQLITE_TYPE_MAP.put(int.class.getName(),"INTEGER");
        SQLITE_TYPE_MAP.put(Integer.class.getName(),"INTEGER");
        SQLITE_TYPE_MAP.put(long.class.getName(),"LONG");
        SQLITE_TYPE_MAP.put(Long.class.getName(),"LONG");
        SQLITE_TYPE_MAP.put(float.class.getName(),"FLOAT");
        SQLITE_TYPE_MAP.put(Float.class.getName(),"FLOAT");
        SQLITE_TYPE_MAP.put(double.class.getName(),"DOUBLE");
        SQLITE_TYPE_MAP.put(Double.class.getName(),"DOUBLE");
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement el : annotations) {
            printNote(el.getQualifiedName().toString());
            if(isAnnotationType(el,Table.class)) {
                parseTableAnnoation(roundEnv.getElementsAnnotatedWith(el), el);
            }
        }
        return true;
    }

    private void parseTableAnnoation(Set<? extends Element> set, TypeElement el) {
        List<TableClass> classes = new ArrayList<>();
        for (Element e : set) {
            if (e.getKind().isClass()) {
                TableClass tc = parseClass(e);
                printNote(tc.toString());
                classes.add(tc);
            }
        }
        genSource(classes);
    }

    private boolean isAnnotationType(TypeElement el, Class<? extends Annotation> annoClazz) {
        if(el.getQualifiedName().toString().equals(annoClazz.getName()))
            return true;
        return false;
    }

    private TableClass parseClass(Element e) {
        List<? extends Element> elements = e.getEnclosedElements();
        TypeElement typeElement = (TypeElement) e;

        String fullName = typeElement.getQualifiedName().toString();
        String packageName = fullName.substring(0, fullName.lastIndexOf("."));
        String className = typeElement.getSimpleName().toString();
        Map<String, BeanField> fieldNames = new HashMap<>();

        TableClass tableClass = new TableClass();
        tableClass.setPackageName(packageName);
        tableClass.setFields(fieldNames);
        tableClass.setGenPackageName(packageName);
        tableClass.setClassName(className);
        Table table = typeElement.getAnnotation(Table.class);
        if(checkTableName(table.name())){
            tableClass.setTableName(table.name());
        }else{
            tableClass.setTableName(tableClass.getClassName().toLowerCase());
        }
        tableClass.setGenClassName(className + "$$Mapper");

        for (int i = 0; i < elements.size(); i++) {
            Element item = elements.get(i);
            if (item.getKind().isField()) {
                BeanField field = new BeanField();
                field.setName(item.getSimpleName().toString());
                field.setType(item.asType().toString());
                if (!fieldNames.containsKey(field.getName()))
                    fieldNames.put(field.getName(), field);
                continue;
            }
            if (item.getKind() == ElementKind.METHOD) {
                ExecutableElement method = (ExecutableElement) item;
                String methodName = item.getSimpleName().toString();
                String fieldName;
                boolean isSetter;
                if ((isSetter = methodName.startsWith("set")) || methodName.startsWith("get")) {
                    fieldName = methodName.substring(3).toLowerCase();
                    BeanField field = fieldNames.get(fieldName);
                    if (field == null) {
                        field = new BeanField();
                        field.setName(fieldName);
                        fieldNames.put(fieldName, field);
                    }
                    if (isSetter) field.setSetter(methodName);
                    else field.setGetter(methodName);
                    field.setMapper(CURSOR_METHOD_MAP.get(field.getType()));

                }
                continue;
            }
        }
        genCreateSqlTable(tableClass);
        return tableClass;
    }

    private boolean checkTableName(String name) {
        if(name==null||"".equals(name))
            return false;
        Pattern pattern = Pattern.compile("^[A-Z,a-z]+\\d?");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    private void genCreateSqlTable(TableClass tableClass) {
        Map<String,BeanField> fieldMap = tableClass.getFields();
        String[] columns = new String[fieldMap.size()];
        int index = 0;
        for (Map.Entry<String,BeanField> entry:fieldMap.entrySet()){
            String columnName = entry.getKey();
            String columnType = SQLITE_TYPE_MAP.get(entry.getValue().getType());
            columns[index++] = columnName+" "+columnType;
        }
        String columnsDelcaration =  String.join(",",columns);
        tableClass.setCreateTable(columnsDelcaration);
    }


    private void genSource(List<TableClass> classes) {
        String tempPath = "/temp/";
        InputStream is = this.getClass().getResourceAsStream(tempPath+"TableClass.temp");
        String temp = Utils.toString(is);
        ST st = new ST(temp);
        for (TableClass table : classes) {
            try {

                FileObject fo = mFiler.createSourceFile(table.getPackageName()+"."+table.getGenClassName(), new Element[0]);
                if (st.getAttribute("bean") != null)
                    st.remove("bean");
                st.add("bean", table);
                st.add("fields",table.toFields());
                Writer writer = fo.openWriter();
                writer.write(st.render());
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
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
