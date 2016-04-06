package com.umlparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class ClassDetails {
	
	private String name = "";
	public boolean isInterface;
	private String classString = "";
	List<ClassOrInterfaceType> extendsList;
	List<ClassOrInterfaceType> implementsList;
	
	
	HashMap<String, MethodDetails> hmMethods = new HashMap<String, MethodDetails>();
	HashMap<String, VariableDetails> hmVariables  = new HashMap<String, VariableDetails>();
	private HashMap<String, String[]> relations;
	
		
	public ClassDetails(ClassOrInterfaceDeclaration coi) {
		
		this.name = coi.getName();
		this.isInterface = coi.isInterface();
		extendsList = coi.getExtends();
		implementsList = coi.getImplements();
		relations = new HashMap<String, String[]>();
		
		GeneratedClasses.getInstance().updateClassMap(this.name, this);

	}


	public String getName(){
		return this.name;
	}
	
	public String getName(int i) {
		String result = "";
		if(this.isInterface){
			result = Symbols.BOXOPEN + "(((interface)));" + this.name + Symbols.BOXCLOSE;
		}else{
			result = Symbols.BOXOPEN + this.name + Symbols.BOXCLOSE;
		}
		return result;
	}
	
	public String getClassOrInterfaceString() {
		this.addExtendsRelation();
		this.addImplementsRelation();
		this.detectGetterSetter();
		this.generateClassString();	
		return classString;
	}


	
	public String[] getRelationWith(String className) {
		String[] result = {"", ""};
			
		String[] relArray =	relations.get(className);
		if(!(this.name.equals(className))){
			if(relArray != null && relArray.length == 2){
				result = relArray;
			}else{
				
			}
		}	
		
		return result;
	}
	
	
	public void addAssoCollection(String incomingString) {
		
		String[] components;
		
		components = incomingString.split("[<>]");
		if(components[0].contains("Collection")){
			String oppClass = components[1];
			String[] relations = {"-","0..*"};
			this.relations.put(oppClass, relations);
			/*String connDone = this.name + oppClass;
			GeneratedClasses.gc.addConnectionDone(connDone);*/
		}else{
			System.out.println("Something is wrong: addAssoCollection.");
		}
	}

	
	public void addAssoClass(String incomingString) {
				
			String oppClass = incomingString;
			String[] relations = {"-","1"};
			this.relations.put(oppClass, relations);
			/*String connDone = this.name + oppClass;
			GeneratedClasses.gc.addConnectionDone(connDone);*/
		
	}
	
	public void addUsesRelation(String incomingString) {
		String oppClass = incomingString;
		String[] relations = {"uses","-.->"};
		this.relations.put(oppClass, relations);
	}
	
	
	public void addExtendsRelation(){
		String oppClass;
		for(ClassOrInterfaceType temp : extendsList){
			oppClass = temp.getName();
			String[] relations = {"-","^"};
			this.relations.put(oppClass, relations);
		}
	}
	
	public void addImplementsRelation(){
		String oppClass;
		for(ClassOrInterfaceType temp : implementsList){
			oppClass = temp.getName();
			String[] relations = {"-.-","^"};
			this.relations.put(oppClass, relations);
		}
		
	}
	
	
	@SuppressWarnings("rawtypes")
	public void detectGetterSetter(){
		Set varSet, methSet1, methSet2;
		Iterator varIterator, methIterator1, methIterator2;
		
		varSet = hmVariables.entrySet();
		varIterator = varSet.iterator();
		
		boolean foundFlag = false;
		
		String varName;
		char accessModifier;
		MethodDetails methDetails1, methDetails2;
		VariableDetails varDetails;
		
		String searchingForGet;
		String searchingForSet;
		
		while(varIterator.hasNext()){
			Map.Entry varMe = (Map.Entry) varIterator.next();
			varName = (String) varMe.getKey();
			
			varDetails =  (VariableDetails) varMe.getValue();
			accessModifier = varDetails.getVarAccessModifier();
			
			if(accessModifier == '-'){
				
			
			char[] tempVarName = varName.trim().toCharArray();
	        tempVarName[0] = Character.toUpperCase(tempVarName[0]);			
			
	        
			searchingForGet = "get" + new String(tempVarName);
			
			methSet1 = hmMethods.entrySet();
			methIterator1 = methSet1.iterator();
			
			while(methIterator1.hasNext()){
				Map.Entry methMe1 = (Map.Entry) methIterator1.next();
				methDetails1 = (MethodDetails) methMe1.getValue();
				
				
				if(methDetails1.getMethodName().equals(searchingForGet)){
					searchingForSet = "set" + new String(tempVarName);
					
					methSet2 = hmMethods.entrySet();
					methIterator2 = methSet2.iterator();
					
					while(methIterator2.hasNext()){
						Map.Entry methMe2 = (Map.Entry) methIterator2.next();
						methDetails2 = (MethodDetails) methMe2.getValue();
						
						if(methDetails2.getMethodName().equals(searchingForSet)){
							hmMethods.remove(methMe1.getKey());
							hmMethods.remove(methMe2.getKey());
							varDetails.setVarAccessModifier('+');
							foundFlag = true;
							break;
						}
					}
									
				}
				
				if(foundFlag){
					break;
				}
			}
			}
			
			if(foundFlag){
				break;
			}
		}
		
	}
	

	
	private void generateClassString(){
		
		String boxOpen = Symbols.BOXOPEN;
		String boxClose = Symbols.BOXCLOSE;
		String lito1 = "";
		String lito2 = "";
		String interfaceString = "";
		
		String varList = "";
		String methList = "";
		
		List<MethodDetails> methArray = new ArrayList<MethodDetails>(hmMethods.values());
		
		for(MethodDetails md : methArray){
			if(this.name.equals("ConcreteSubject")){
				String methName = md.getMethodName();
				if(methName.equals("detach") || methName.equals("attach") || methName.equals("notifyObservers")){
					continue;
				}
			}
			methList = methList + md.getFullMethodString();
		}
		
		
		List<VariableDetails> varArray = new ArrayList<VariableDetails>(hmVariables.values());
		
		for(VariableDetails vd : varArray){
			varList = varList + vd.getFullVariableString();
		}
		
		if(!varList.equals("")){
			varList = varList.substring(0,varList.length() - 1);
			lito1 = Symbols.LITO;
		}
		if(!methList.equals("")){
			methList = methList.substring(0,methList.length() - 1);
			lito2 = Symbols.LITO;
		}
		
		if(this.isInterface){
			interfaceString = "(((interface)));";
		}
		
		
		classString = boxOpen + interfaceString + this.name + lito1 + varList + lito2 + methList + boxClose;
		
	}
}
