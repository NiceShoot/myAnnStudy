package com.example.myannprocessor.myLog;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes({"com.example.myannprocessor.myLog.MyLog"})
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
//@AutoService(Processor.class)
public class MyLogProcessor extends AbstractProcessor {

    private TreeMaker treeMaker;
    private JavacTrees javacTrees;
    private Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.javacTrees = JavacTrees.instance(processingEnv);
        this.names = Names.instance(context);
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : elementsAnnotatedWith) {
                JCTree jcTree = javacTrees.getTree(element);
                jcTree.accept(new TreeTranslator(){
                    // 访问者访问JCTree上类定义的JCClassDecl
                    @Override
                    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                        if (jcTree.getPreferredPosition() == jcClassDecl.pos){
                            JCTree.JCVariableDecl jcVariableDecl = makeLoggerInstanceDecl(jcClassDecl, element);
                            jcClassDecl.defs = jcClassDecl.defs.prepend(jcVariableDecl);
                        }
                        super.visitClassDef(jcClassDecl);
                    }
                });
            }
        }
        return true;
    }
    private JCTree.JCVariableDecl makeLoggerInstanceDecl (JCTree.JCClassDecl jcClassDecl, Element element) {
        // 创建可执行语句
        JCTree.JCExpressionStatement var = this.treeMaker.Exec(this.treeMaker.Apply(
                        List.nil(),
                        this.memberAccess("org.slf4j.LoggerFactory.getLogger"),
                        List.of(this.memberAccess(jcClassDecl.getSimpleName().toString() + ".class"))
                )
        );
        // 定义静态变量 log , Logger log = LogFactory.getLogger("xxxxx");
        return this.treeMaker.VarDef(this.treeMaker.Modifiers(Flags.PRIVATE+Flags.STATIC+Flags.FINAL),
                this.names.fromString("log"), // 指定静态变量名称
                this.memberAccess("org.slf4j.Logger"), // 指定Logger 类型，这里是slf4j
                var.getExpression() // 表达式赋值给静态变量
        );
    }

    private JCTree.JCExpression memberAccess (String components) {
        String[] componentsArr = components.split("\\.");
        Name name = this.getNameFromString(componentsArr[0]);
        JCTree.JCExpression expression = this.treeMaker.Ident(name);
        for (int i = 1; i < componentsArr.length; i ++) {
            expression = this.treeMaker.Select(expression,this.getNameFromString(componentsArr[i]));
        }
        return expression;
    }

    private Name getNameFromString (String str) {
        return this.names.fromString(str);
    }
}
