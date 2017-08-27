package hw3;

import java.util.HashMap;

public class UnificationSubstitution {
	
	HashMap<String, String> variableToConstant = new HashMap<String, String>();
	Predicate thisPredicate, otherPredicate;
	UnarySentence thisUnarySentence, otherUnarySentence;
	
	public void setThisPredicate(Predicate thisPredicate) {
		this.thisPredicate = thisPredicate;
	}

	public void setOtherPredicate(Predicate otherPredicate) {
		this.otherPredicate = otherPredicate;
	}

	public void setThisUnarySentence(UnarySentence thisUnarySentence) {
		this.thisUnarySentence = thisUnarySentence;
	}

	public void setOtherUnarySentence(UnarySentence otherUnarySentence) {
		this.otherUnarySentence = otherUnarySentence;
	}

	public boolean add(String variableName, String constantName){
		if(variableToConstant.containsKey(variableName)){
			//This is already mapped to something
			if(!variableToConstant.get(variableName).equals(constantName)){
				//This is not the same. Cannot add this one
				return false;
			}
		}
		else{
			variableToConstant.put(variableName, constantName);
		}
		return true;
	}

}
