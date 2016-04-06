package com.umlparser;

public class VariableDetails {

	private char varAccessModifier;
	private String varName;
	private String varReturnType;
	
	public VariableDetails(char varAccessModifier, String varName, String varReturnType) {
		
		this.varAccessModifier = varAccessModifier;
		this.varName = varName;
		this.varReturnType = varReturnType;
	}
	
	public char getVarAccessModifier() {
		return varAccessModifier;
	}
	
	public void setVarAccessModifier(char varAccessModifier){
		this.varAccessModifier = varAccessModifier;
	}
	
	public String getFullVariableString(){
		String result = "";
		
		if(varReturnType == null){
			result = "";
		}else {
			result = varAccessModifier + varName + varReturnType + ";";
		}
		
		return result;
	}



}