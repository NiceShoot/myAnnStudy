package com.example.myannprocessor.myLog;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes({"com.example.myannprocessor.myLog.MyLog"})
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
//@AutoService(Processor.class)
public class MyLogProcessorbBak extends AbstractProcessor {

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
        test();
        return true;
    }


    private void test(){

        // 生成 private String name; 这样一个变量
        treeMaker.VarDef(treeMaker.Modifiers(Flags.PRIVATE),names.fromString("name"),treeMaker.Ident(names.fromString("String")),null);

        // 生成 private String name = "BuXueWuShu"
        treeMaker.VarDef(treeMaker.Modifiers(Flags.PRIVATE),names.fromString("name"),treeMaker.Ident(names.fromString("String")),treeMaker.Literal("zhangSan"));

        // 生成 add = "a" + "b";
        treeMaker.Exec(treeMaker.Assign(treeMaker.Ident(names.fromString("add")),treeMaker.Binary(JCTree.Tag.PLUS,treeMaker.Literal("a"),treeMaker.Literal("b"))));

        // 生成 add += "test"
        treeMaker.Exec(treeMaker.Assignop(JCTree.Tag.PLUS_ASG, treeMaker.Ident(names.fromString("add")), treeMaker.Literal("test")));

        // 方法无参无返回值 public void test(){}
        ListBuffer<JCTree.JCStatement> testStatement = new ListBuffer<>();
        JCTree.JCBlock testBody = treeMaker.Block(0, testStatement.toList());
        JCTree.JCMethodDecl test = treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC), // 方法限定值
                names.fromString("test"), // 方法名
                treeMaker.Type(new Type.JCVoidType()), // 返回类型
                com.sun.tools.javac.util.List.nil(),
                com.sun.tools.javac.util.List.nil(),
                com.sun.tools.javac.util.List.nil(),
                testBody,    // 方法体
                null
        );

        // 有参无返回值 public void test2(String name){name = "xxxx"; }
        ListBuffer<JCTree.JCStatement> testStatement2 = new ListBuffer<>();
        testStatement2.append(treeMaker.Exec(treeMaker.Assign(treeMaker.Ident(names.fromString("name")),treeMaker.Literal("xxxx"))));
        JCTree.JCBlock testBody2 = treeMaker.Block(0, testStatement2.toList());
        // 生成入参
        JCTree.JCVariableDecl param = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), names.fromString("name"),treeMaker.Ident(names.fromString("String")), null);
        com.sun.tools.javac.util.List<JCTree.JCVariableDecl> parameters = com.sun.tools.javac.util.List.of(param);
        JCTree.JCMethodDecl test2 = treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC), // 方法限定值
                names.fromString("test2"), // 方法名
                treeMaker.Type(new Type.JCVoidType()), // 返回类型
                com.sun.tools.javac.util.List.nil(),
                parameters, // 入参
                com.sun.tools.javac.util.List.nil(),
                testBody2,
                null
        );

        // 有参有返回值 public String test3(String name){return name;}
        ListBuffer<JCTree.JCStatement> testStatement3 = new ListBuffer<>();
        testStatement3.append(treeMaker.Return(treeMaker.Ident(names.fromString("name"))));
        JCTree.JCBlock testBody3 = treeMaker.Block(0, testStatement3.toList());
        // 生成入参
        JCTree.JCVariableDecl param3 = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), names.fromString("name"),treeMaker.Ident(names.fromString("String")), null);
        com.sun.tools.javac.util.List<JCTree.JCVariableDecl> parameters3 = com.sun.tools.javac.util.List.of(param3);
        JCTree.JCMethodDecl test3 = treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC), // 方法限定值
                names.fromString("test4"), // 方法名
                treeMaker.Ident(names.fromString("String")), // 返回类型
                com.sun.tools.javac.util.List.nil(),
                parameters3, // 入参
                com.sun.tools.javac.util.List.nil(),
                testBody3,
                null
        );
    }

}
