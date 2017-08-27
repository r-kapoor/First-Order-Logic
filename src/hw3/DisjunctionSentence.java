package hw3;

import java.util.ArrayList;
import java.util.Iterator;

public class DisjunctionSentence extends Sentence {
	
	private ArrayList<KBSentence> subsentences;
	
	public DisjunctionSentence() {
		this.subsentences = new ArrayList<KBSentence>();
	}
	
	public DisjunctionSentence(ArrayList<KBSentence> subsentences) {
		this.subsentences = subsentences;
	}
	
	public DisjunctionSentence(KBSentence sentence){
		this.subsentences = new ArrayList<KBSentence>();
		subsentences.add(sentence);
	}

	public void addSentence(KBSentence sentence){
		this.subsentences.add(sentence);
	}
	
	public ArrayList<KBSentence> getSubsentences() {
		return subsentences;
	}

	public void concatSentence(DisjunctionSentence disjunctionSentence){
		this.subsentences.addAll(disjunctionSentence.getSubsentences());
	}
	
	public DisjunctionSentence addToCreate(DisjunctionSentence disjunctionSentence){
		@SuppressWarnings("unchecked")
		ArrayList<KBSentence>thisSubsentences = (ArrayList<KBSentence>)this.subsentences.clone();
		thisSubsentences.addAll(disjunctionSentence.getSubsentences());
		return new DisjunctionSentence(thisSubsentences);
	}

	@Override
	public String toString() {
		String text = "";
		int count = 0;
		for(KBSentence subsentence : subsentences){
			if(count > 0){
				text += "|";
			}
			text += subsentence.toString();
			count++;
		}
		return text;
	}

	@Override
	public Sentence convertToCNF() {
		//Should already be in CNF
		return this;
	}

	@Override
	public void eliminateImplication() {
		//No implications
	}

	@Override
	public Sentence reduceScopeOfNegation() {
		//No reduction necessary
		return this;
	}

	@Override
	public ConjunctionSentence convertToConjunctionOfDisjunctions() {
		//Already in the form
		return new ConjunctionSentence(new KBSentence(this));
	}

	public UnificationSubstitution unify(DisjunctionSentence otherSentence, Predicate predicate, Boolean isNegative) {
		Predicate thisPredicate = null, otherPredicate = null;
		UnarySentence thisUnarySentence = null, otherUnarySentence = null;
		UnificationSubstitution substitution = new UnificationSubstitution();
		for(KBSentence kbSentence : this.subsentences){
			Sentence subsentence = kbSentence.getSentence();
			if(isNegative){
				//The predicate is negative (ie. in unary)
				if(subsentence instanceof UnarySentence){
					thisUnarySentence = (UnarySentence)subsentence;
					thisPredicate= (Predicate)thisUnarySentence.getOperand().getSentence();
				}
			}
			else{
				//The predicate is positive (i.e. a predicate)
				if(subsentence instanceof Predicate){
					thisPredicate = (Predicate)subsentence;
				}
			}
			if(thisPredicate != null && thisPredicate.getClass().equals(predicate.getClass())){
				break;
			}
		}
		for(KBSentence KBsubsentence : otherSentence.subsentences){
			Sentence subsentence = KBsubsentence.getSentence();
			if(isNegative){
				//Original predicate is negative. This should be positive
				if(subsentence instanceof Predicate){
					otherPredicate = (Predicate)subsentence;
				}
			}
			else{
				//Original predicate is positive. This should be negative
				if(subsentence instanceof UnarySentence){
					otherUnarySentence = (UnarySentence)subsentence;
					otherPredicate = (Predicate)otherUnarySentence.getOperand().getSentence();
				}
			}
			if(otherPredicate != null && otherPredicate.getName().equals(predicate.getName())){
				break;
			}
		}
		substitution.setThisPredicate(thisPredicate);
		substitution.setOtherPredicate(otherPredicate);
		substitution.setThisUnarySentence(thisUnarySentence);
		substitution.setOtherUnarySentence(otherUnarySentence);
		ArrayList<Parameter> thisParameters = thisPredicate.getParameters();
		ArrayList<Parameter> otherParameters = otherPredicate.getParameters();
		Iterator<Parameter> thisIterator = thisParameters.iterator();
		Iterator<Parameter> otherIterator = otherParameters.iterator();
		while(thisIterator.hasNext() && otherIterator.hasNext()){
			Parameter thisParameter = thisIterator.next();
			Parameter otherParameter = otherIterator.next();
			if(thisParameter instanceof Constant && otherParameter instanceof Constant){
				//Both are constants
				Constant thisConstant = (Constant)thisParameter;
				Constant otherConstant = (Constant)otherParameter;
				if(thisConstant.getName().equals(otherConstant.getName())){
					//Constants are same
					continue;
				}
				else{
					//Cannot be unified
					return null;
				}
			}
			else if(thisParameter instanceof Variable && otherParameter instanceof Constant){
				//Need to substitute Variable with Constant
				Variable thisVariable = (Variable)thisParameter;
				Constant otherConstant = (Constant)otherParameter;
				if(!substitution.add(thisVariable.toString(), otherConstant.getName())){
					return null;
				}
			}
			else if(thisParameter instanceof Constant && otherParameter instanceof Variable){
				//Need to substitute Variable with Constant
				Constant thisConstant = (Constant)thisParameter;
				Variable otherVariable = (Variable)otherParameter;
				if(!substitution.add(otherVariable.toString(), thisConstant.getName())){
					return null;
				}
			}
			else{
				//Both are variables
				//Nothing to do
			}
		}
		return substitution;
	}

	public boolean isSameAs(DisjunctionSentence otherDisjunctionSentence) {
		if(this.equals(otherDisjunctionSentence)){
			return true;
		}
		ArrayList<KBSentence> otherSubsentences = otherDisjunctionSentence.subsentences;
		for(KBSentence subsentence : subsentences){
			Sentence sentence = subsentence.getSentence();
			boolean matchFound = false;
			for(KBSentence otherSubsentence : otherSubsentences){
				Sentence otherSentence = otherSubsentence.getSentence();
				if(sentence instanceof Predicate && otherSentence instanceof Predicate){
					Predicate predicate = (Predicate)sentence;
					Predicate otherPredicate = (Predicate)otherSentence;
					if(predicate.isSameAs(otherPredicate)){
						matchFound = true;
					}
				}
				if(sentence instanceof UnarySentence && otherSentence instanceof UnarySentence){
					UnarySentence unarySentence = (UnarySentence)sentence;
					UnarySentence otherUnarySentence = (UnarySentence)otherSentence;
					if(unarySentence.isSameAs(otherUnarySentence)){
						matchFound = true;
					}
				}
			}
			if(!matchFound){
				//No match found for this subsentence in the other
				return false;
			}
		}
		return true;
	}

	public boolean isContradiction(DisjunctionSentence sentenceWithOpposite) {
		//Assuming that only unit sentences can be contradictions
		if(this.subsentences.size() > 1 || sentenceWithOpposite.subsentences.size() > 1){
			return false;
		}
		//They are unit sentences
		Sentence thisSentence = this.subsentences.get(0).getSentence();
		Sentence oppositeSentence = sentenceWithOpposite.subsentences.get(0).getSentence();
		Predicate predicate = null;
		UnarySentence unarySentence = null;
		if(thisSentence instanceof Predicate){
			predicate = (Predicate)thisSentence;
		}
		else if(thisSentence instanceof UnarySentence){
			unarySentence = (UnarySentence)thisSentence;
		}
		if(oppositeSentence instanceof Predicate){
			predicate = (Predicate)oppositeSentence;
		}
		else if(oppositeSentence instanceof UnarySentence){
			unarySentence = (UnarySentence)oppositeSentence;
		}
		if(predicate != null && unarySentence != null){
			//1 is a predicate 1 is a unary
			Predicate otherPredicate = (Predicate)unarySentence.getOperand().getSentence();
			if(predicate.getName().equals(otherPredicate.getName())){
				ArrayList<Parameter> thisParameters = predicate.getParameters();
				ArrayList<Parameter> otherParameters = otherPredicate.getParameters();
				Iterator<Parameter> thisIterator = thisParameters.iterator();
				Iterator<Parameter> otherIterator = otherParameters.iterator();
				while(thisIterator.hasNext() && otherIterator.hasNext()){
					Parameter thisParameter = thisIterator.next();
					Parameter otherParameter = otherIterator.next();
					if(thisParameter instanceof Constant && otherParameter instanceof Constant){
						//Both are constants
						if(!thisParameter.isSameAs(otherParameter)){
							return false;
						}
					}
					//For all other cases
					//A(Dog,...) ~A(Dog,...)
					//A(x,..) ~A(y,..)
					//A(x,..) ~A(Dog,...)
					//- continue
				}
				return true;//It is a contradiction
			}
		}
		
		return false;
	}

}
