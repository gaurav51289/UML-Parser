package com.umlparser;

import java.util.List;

import com.github.javaparser.ast.AccessSpecifier;

public class ModellingSupport {

	private static final char PUBLIC = '+';
	private static final char PRIVATE = '-';
	
	
	public char getUMLAccessModifier(AccessSpecifier accessSpecifier){
		
		if (accessSpecifier == AccessSpecifier.PUBLIC) {
			return PUBLIC;
		}else if(accessSpecifier == AccessSpecifier.PRIVATE){
			return PRIVATE;
		}else{
			return '\0';
		}
	}
	
	public String processTypes(String incomingString ,int fromWhat, ClassDetails cd) {
		String result = "";
		
	
			if(incomingString.contains(Symbols.SQRBRACKETS)){
				int indexRequired = incomingString.indexOf('[');
				incomingString = incomingString.substring(0, indexRequired);
				incomingString = incomingString + Symbols.BRACKETSFORARR;
				result = incomingString;
				result = ":" + result;
			} else if (incomingString.contains(Symbols.ANGBRACKETOPEN)){
				//Write code for Association
				cd.addAssoCollection(incomingString);
				result = null;
			} else if(!(incomingString.contains(Symbols.INTEGER)) && !(incomingString.contains(Symbols.STRING))
					&& !(incomingString.contains(Symbols.VOID))){
				if(fromWhat == 1){
					//Write code for Association
					cd.addAssoClass(incomingString);
					result = null;
				}
				if(fromWhat == 2 && !incomingString.equals("")){
					String[] temp = incomingString.split(" ");
					
					cd.addUsesRelation(temp[0]);
					
					result = temp[1]+":"+temp[0];
				}
			}else{
				result = incomingString;
				result = ":" + result;
			}		
		
		return result;
	}
	
	
	public <E> String trimSquareBrackets(List<E> inputList) {
		String result = "";
		
		result = inputList.toString();
		result = result.substring(1, result.length() - 1);
		return result;
	}
	
	public String convertParameters(String incomingString){
		String result = "";
		
		String[] temp = incomingString.split(" ");
		result = temp[1] + ":" + temp[0];
		
		return result;
	}
		
}
