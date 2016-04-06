package com.umlparser;

import java.io.FileInputStream;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

@SuppressWarnings("rawtypes")
public class MyUMLParser extends VoidVisitorAdapter {
	

	private CompilationUnit cu;
	private ModellingSupport ms = new ModellingSupport();
	
	ClassDetails cd;
	

	@SuppressWarnings("unchecked")
	public MyUMLParser(String filePath) {
		
		cu = null;
		
		try {
			FileInputStream fin = new FileInputStream(filePath);
			cu = JavaParser.parse(fin);
			this.visit(cu, null);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void visit(ClassOrInterfaceDeclaration coi, Object o){
		cd = new ClassDetails(coi);
		super.visit(coi, o);
	}
	
	@Override
	public void visit(ConstructorDeclaration cod, Object o){
		
		String constName = "";
		char constAccessModifier;
		String constParameter;
		
		
		
		constName = cod.getName();
		constAccessModifier = ms.getUMLAccessModifier(ModifierSet.getAccessSpecifier(cod.getModifiers()));
		constParameter = ms.trimSquareBrackets(cod.getParameters());
		constParameter = ms.processTypes(constParameter, 2, cd);
		
		if(constAccessModifier == '+'){
			MethodDetails mdet = new MethodDetails(constName, constAccessModifier, constParameter, "");
			cd.hmMethods.put(constName, mdet);
		}	
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void visit(MethodDeclaration md, Object o){
		char methodAccessModifier;
		String methodName = "";
		String methodParameter = "";
		String methodReturnType = "";
		
		methodName = md.getName();		
		methodAccessModifier = ms.getUMLAccessModifier(ModifierSet.getAccessSpecifier(md.getModifiers()));
		methodParameter = ms.trimSquareBrackets(md.getParameters());
		methodParameter = ms.processTypes(methodParameter, 2, cd);
		methodReturnType = ms.processTypes(md.getType().toString(),1, cd);
		
		if(methodAccessModifier == '+'){
			MethodDetails mdet = new MethodDetails(methodName, methodAccessModifier, methodParameter, methodReturnType);
			cd.hmMethods.put(methodName, mdet);
		}	
		super.visit(md, o);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public void visit(FieldDeclaration fd, Object o) {
		
		char varAccessModifier;
		String varName = "";
		String varReturnType = "";
		
		varAccessModifier = ms.getUMLAccessModifier(ModifierSet.getAccessSpecifier(fd.getModifiers()));
		
		varName = ms.trimSquareBrackets(fd.getVariables());
		varReturnType = ms.processTypes(fd.getType().toString(),1, cd);
		
		if(varAccessModifier == '+' || varAccessModifier == '-'){
		VariableDetails vdet = new VariableDetails(varAccessModifier, varName, varReturnType);
		cd.hmVariables.put(varName, vdet);
		}
		//outgoingString = ms.buildOutputStringForVariable(varAccessModifier, varName, varReturnType);
		//ms.appendYUMLStringVars(outgoingString);		
		
	    super.visit(fd, o);
	}
	
	
	public void testPrint(){
		
		
	}
}
