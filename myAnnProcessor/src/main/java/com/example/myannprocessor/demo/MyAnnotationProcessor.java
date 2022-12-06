package com.example.myannprocessor.demo;

import com.google.auto.service.AutoService;
import org.springframework.util.StringUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes({"com.example.myannprocessor.demo.MyAnnotation"})
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
//@AutoService(Processor.class)
public class MyAnnotationProcessor extends AbstractProcessor {

    private Messager messager;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        this.elementUtils = processingEnv.getElementUtils();
        this.messager = processingEnv.getMessager();
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : elementsAnnotatedWith) {
                // 方法名
                ExecutableElement method = (ExecutableElement)element;
                messager.printMessage(Diagnostic.Kind.NOTE, "方法：ExecutableElement: " + method.getSimpleName() + "(), ElementKind: " + method.getKind());

                // 获取方法对应的类
                TypeElement classElement = (TypeElement) method.getEnclosingElement();
                messager.printMessage(Diagnostic.Kind.NOTE, "方法所属的类：TypeElement: " + classElement.getQualifiedName() + ", ElementKind: " + classElement.getKind());

                // 获取方法的入参
                messager.printMessage(Diagnostic.Kind.NOTE, "方法的入参: ");
                List<? extends VariableElement> parameters = method.getParameters();
                for (VariableElement param : parameters) {
                    // 获取参数名和参数类型
                    TypeMirror typeMirror = param.asType();
                    messager.printMessage(Diagnostic.Kind.NOTE, "VariableElement: " + typeMirror.getKind() + " " + param.getSimpleName() + ", ElementKind: " + param.getKind());
                }

                // 获取方法的泛型类型
                messager.printMessage(Diagnostic.Kind.NOTE, "方法的类型参数: ");
                List<? extends TypeParameterElement> typeParameters = method.getTypeParameters();
                for (TypeParameterElement typeParameter : typeParameters) {
                    messager.printMessage(Diagnostic.Kind.NOTE, "TypeParameterElement: " + typeParameter + ", ElementKind: " + typeParameter.getKind());
                }

                // 获取类的包名
                PackageElement packageElement = elementUtils.getPackageOf(classElement);
                String packageName = packageElement.getQualifiedName().toString();
                messager.printMessage(Diagnostic.Kind.NOTE, "包：PackageElement: " + (!StringUtils.isEmpty(packageName) ? packageName : "no package") + ", ElementKind: " + packageElement.getKind());

                // 获取包中的所有类或接口
                messager.printMessage(Diagnostic.Kind.NOTE, "包中的类或接口如下: ");
                List<? extends Element> enclosedElements = packageElement.getEnclosedElements();
                messager.printMessage(Diagnostic.Kind.NOTE, packageName + " has following class or interface:");
                for (Element enclosedElement : enclosedElements) {
                    ElementKind kind = enclosedElement.getKind();
                    if (kind == ElementKind.CLASS || kind == ElementKind.INTERFACE) {
                        messager.printMessage(Diagnostic.Kind.NOTE, ((TypeElement) enclosedElement).getQualifiedName());
                    }
                }
            }
        }
        return true;
    }
}
