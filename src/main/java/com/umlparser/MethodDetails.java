package com.umlparser;

public class MethodDetails {

	
	
	private String methodName;
	private char methodAccessModifier;
	private String methodParameter;
	private String methodReturnType;

	public MethodDetails(String methodName, char methodAccessModifier, String methodParameter,
			String methodReturnType) {
		
		this.methodName = methodName;
		this.methodAccessModifier = methodAccessModifier;
		this.methodParameter = methodParameter;
		this.methodReturnType = methodReturnType;

	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public char getMethodAccessModifier() {
		return methodAccessModifier;
	}

	
	
	
	public String getFullMethodString(){
		
		String fullString = "";
		
		fullString = methodAccessModifier + methodName + "(" + methodParameter +")" + methodReturnType + ";";
		
		return fullString;
	}

}
