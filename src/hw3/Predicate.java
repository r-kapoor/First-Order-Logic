package hw3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Predicate extends Sentence {
	
	private String name;
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	
	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}

	public Predicate(String predicateString){
		int indexOfBracket = predicateString.indexOf('(');
		this.name = predicateString.substring(0, indexOfBracket);
		String parametersString = predicateString.substring(indexOfBracket+1, predicateString.length() - 1);
		//There might be multiple parameters
		String[] parametersStringArray = parametersString.split(",");
		for(String parameterString: parametersStringArray){
			if(Character.isUpperCase(parameterString.charAt(0))){
				//This is a Constant
				Constant constant = new Constant(parameterString);
				this.parameters.add(constant);
			}
			else{
				//This is a variable
				Variable variable = new Variable(parameterString);
				this.parameters.add(variable);
			}
		}
	}
	
	public Predicate(String name, ArrayList<Parameter> parameters){
		this.name = name;
		this.parameters = parameters;
	}

	public String getName() {
		return name;
	}
	
	public String toString(){
		String text = name + "(";
		int count = 0;
		for(Parameter parameter : parameters){
			if(count != 0){
				text += ",";
			}
			text += parameter.toString();
			count++;
		}
		text += ")";
		return text;
	}

	public ArrayList<Parameter> getParameters() {
		return parameters;
	}

	@Override
	public Sentence convertToCNF(){
		//A predicate is already in CNF
		return this;
	}
	
	@Override
	public void eliminateImplication(){
		//A predicate needs no implication elimination
	}
	
	@Override
	public Sentence reduceScopeOfNegation(){
		//Scope cannot be reduced further
		return this;
	}

	@Override
	public ConjunctionSentence convertToConjunctionOfDisjunctions() {
		//A predicate is already in the form- A
		ConjunctionSentence conjunctiveSentence = new ConjunctionSentence();
		KBSentence kbSentence = new KBSentence(new DisjunctionSentence(new KBSentence(this)));
		conjunctiveSentence.addSentence(kbSentence);
		return conjunctiveSentence;
	}

	public boolean isSameAs(Predicate otherPredicate) {
		if(this.equals(otherPredicate)){
			return true;
		}
		if(this.name.equals(otherPredicate.name)){
			//Name same, check variables
			if(this.parameters.size() == otherPredicate.parameters.size()){
				//Same number of params
				//Check if all identical
				Iterator<Parameter> thisIterator = this.parameters.iterator();
				Iterator<Parameter> otherIterator = otherPredicate.parameters.iterator();
				HashMap<String, String> variableOfOneToAnother = new HashMap<String, String>();
				while(thisIterator.hasNext() && otherIterator.hasNext()){
					Parameter thisParameter = thisIterator.next();
					Parameter otherParameter = otherIterator.next();
					if(!thisParameter.isSameAs(otherParameter)){
						if(thisParameter instanceof Variable && otherParameter instanceof Variable){
							Variable thisVariable = (Variable)thisParameter;
							Variable otherVariable = (Variable)otherParameter;
							String thisVariableString = thisVariable.toString();
							String otherVariableString = otherVariable.toString();
							if(variableOfOneToAnother.containsKey(thisVariableString)){
								if(!variableOfOneToAnother.get(thisVariableString).equals(otherVariableString)){
									return false;
								}
							}
							else{
								variableOfOneToAnother.put(thisVariableString, otherVariableString);
							}
						}
						else{
							return false;
						}
					}
				}
				return true;
			}
		}
		return false;
	}
}
