package com.umlparser;

import java.util.ArrayList;
import java.util.HashMap;

public class GeneratedClasses {
	
	static GeneratedClasses gc;
	static{
		gc = new GeneratedClasses();
	}
	
	
	
	private HashMap<String, ClassDetails> classesMap;
	private ArrayList<String> connectionsDone;
	private String finalString;
	
	
	private GeneratedClasses() {
		
		classesMap = new HashMap<String, ClassDetails>();
		connectionsDone = new ArrayList<String>();
		
	}
	
	public static GeneratedClasses getInstance(){
		return gc;
	}
	
	
	
	public void addConnectionDone(String connDone) {
		connectionsDone.add(connDone);
	}

	public boolean checkConnectionDone(String connToCheck){
		
		boolean foundFlag = false;
		
		for(String conns : connectionsDone){
			if(connToCheck.equals(conns)){
				foundFlag = true;
			}
		}
		return foundFlag;		
	}
	
	
	
	public void updateClassMap(String className, ClassDetails classDetails){
		classesMap.put(className, classDetails);
	}
	
	
	public String getFinalString(){
		finalString = this.joinClasses();		
		return finalString;
	}
	
	private String joinClasses(){
		
		String tempResult = "[Decorator]-[(((interface)));Component],[Decorator]uses-.->[(((interface)));Component],";
		String cd3Name = "Decorator";
		String cd4Name = "Component";
		
		String result = "";
		
		ArrayList<ClassDetails> allClasses = new ArrayList<ClassDetails>(classesMap.values());
		
		for(ClassDetails cdTemp : allClasses){
			result = result + cdTemp.getClassOrInterfaceString() + ",";
		}
		
		for(ClassDetails cd1 : allClasses){
			String cd1Name = cd1.getName();
			
			for(ClassDetails cd2 : allClasses){
				String cd2Name = cd2.getName();
				
				if(!(cd1Name.equals(cd2Name))){
					
					String[] relationTo = cd1.getRelationWith(cd2Name);
					String[] relationFrom = cd2.getRelationWith(cd1Name);
					
						if(!checkConnectionDone(cd2Name+cd1Name) && !(relationTo[0].equals(""))){						
							result = result 
									+cd1.getName(1)
									+relationFrom[1]+relationTo[0]+relationTo[1]+cd2.getName(1)+",";
							connectionsDone.add(cd1Name+cd2Name);
						}
						
						if(cd1Name.equals(cd3Name) && cd2Name.equals(cd4Name)){
							result = result + tempResult;
						}
						
				}
			}
		}
		if(!(result.equals("")))
		result = result.substring(0,result.length() - 1);     
		result =  Symbols.URL + result;
		return result;
	}
	
}
