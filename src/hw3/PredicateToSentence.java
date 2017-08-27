package hw3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class PredicateToSentence {
	
	HashMap<String, SentencesForPredicate> hashMap = new HashMap<String, SentencesForPredicate>();
	KnowledgeBase kb;
	
	public PredicateToSentence(KnowledgeBase kb) {
		this.kb = kb;
		ArrayList<KBSentence> kbSentences = kb.getSentences();
		for(KBSentence kbSentence : kbSentences){
			Sentence sentence = kbSentence.getSentence();
			if(sentence instanceof DisjunctionSentence){
				DisjunctionSentence disjunctionSentence = (DisjunctionSentence)sentence;
				addDisjunctionSentenceToHashMap(disjunctionSentence);
			}
			else{
//				System.out.println("Not disjunction"+sentence.getClass().getName()+" "+sentence.toString());
			}
		}
	}

	private void addDisjunctionSentenceToHashMap(DisjunctionSentence disjunctionSentence) {
		ArrayList<KBSentence> subsentences = disjunctionSentence.getSubsentences();
		for(KBSentence subsentence : subsentences){
			Predicate predicate = getPredicateFromKBSentence(subsentence);
			Boolean isNegative = isNegativeSentence(subsentence);
			addToHashMap(predicate.getName(), isNegative, disjunctionSentence);
		}
	}
	
	private void addToHashMap(String predicateName, Boolean isNegative, DisjunctionSentence disjunctionSentence) {
		if(hashMap.containsKey(predicateName)){
			//The predicate is already present in some form
			SentencesForPredicate sentenceForPredicate = hashMap.get(predicateName);
			if(isNegative){
				sentenceForPredicate.addSentenceToNegative(disjunctionSentence);
			}
			else{
				sentenceForPredicate.addSentenceToPositive(disjunctionSentence);
			}
		}
		else{
			//Need to add this predicate
			SentencesForPredicate sentenceForPredicate = new SentencesForPredicate();
			if(isNegative){
				sentenceForPredicate.addSentenceToNegative(disjunctionSentence);
			}
			else{
				sentenceForPredicate.addSentenceToPositive(disjunctionSentence);
			}
			hashMap.put(predicateName, sentenceForPredicate);
		}
	}

	public boolean isNegativeSentence(KBSentence kbSentence){
		Sentence unitSentence = kbSentence.getSentence();
		if(unitSentence instanceof Predicate){
			return false;
		}
		else if(unitSentence instanceof UnarySentence){
			return true;
		}
		return false;
	}
	
	public static Predicate getPredicateFromKBSentence(KBSentence kbSentence){
		Sentence unitSentence = kbSentence.getSentence();
		if(unitSentence instanceof Predicate){
			return (Predicate)unitSentence;
		}
		else if(unitSentence instanceof UnarySentence){
			UnarySentence unarySentence = (UnarySentence)unitSentence;
			Sentence predicateSentence = unarySentence.getOperand().getSentence();
			if(predicateSentence instanceof Predicate){
				return (Predicate)predicateSentence;
			}
		}
		return null;
	}
	
	public String toString(){
		String text = "";
		Set<Entry<String, SentencesForPredicate>> entrySet = hashMap.entrySet();
		for(Entry<String, SentencesForPredicate> entry : entrySet){
			text += entry.getKey() + ":\n";
			text += entry.getValue().toString();
		}
		return text;
	}

	public void addQueryToKBIndex(Sentence querySentence) {
		ArrayList<KBSentence> sentences = kb.getSentences();
		Collections.sort(sentences);
		Predicate predicate;
		Boolean isNegative;
		Sentence queryNegation;
		if(querySentence instanceof UnarySentence){
			isNegative = false;
			UnarySentence queryUnarySentence = (UnarySentence)querySentence;
			predicate = (Predicate)queryUnarySentence.getOperand().getSentence();
			queryNegation = predicate;
		}
		else{
			predicate = (Predicate)querySentence;
			isNegative = true;
			queryNegation = new UnarySentence(new NotOperator(), new KBSentence(querySentence));
		}
		String predicateName = predicate.getName();
		DisjunctionSentence disjunctionSentence = new DisjunctionSentence(new KBSentence(queryNegation));
		KBSentence kbSentence = new KBSentence(disjunctionSentence);
		kb.addToFront(kbSentence);
		addToHashMap(predicateName, isNegative, disjunctionSentence);
	}

	public boolean runResolution() {
//		System.out.println(kb);
		//Now the query is in the 1st place
		//Start from the 1st sentence in the kb, unify with the others got from kbIndex, look for contradictions, add new sentences just after the current one
		ArrayList<KBSentence> sentences = kb.getSentences();
		CoveredSentences coveredSentences = new CoveredSentences();
		int currentIndex = 0;
		while(currentIndex < sentences.size()){
			DisjunctionSentence currentSentence = (DisjunctionSentence)sentences.get(currentIndex).getSentence();
//			System.out.println("Current Sentence:"+currentSentence);
			if(currentSentence.getSubsentences().size() > 1){
				currentIndex++;
				continue;
			}
			if(coveredSentences.isCovered(currentSentence)){
				currentIndex++;
				continue;
			}
			else{
				coveredSentences.addToCoveredSentences(currentSentence);
			}
			//Try to unify with the rest
			ArrayList<KBSentence> subsentences = currentSentence.getSubsentences();
			for(KBSentence kbSubsentence : subsentences){
				Sentence subsentence = kbSubsentence.getSentence();
				Predicate predicate;
				Boolean isNegative;
				if(subsentence instanceof UnarySentence){
					isNegative = true;
					UnarySentence queryUnarySentence = (UnarySentence)subsentence;
					predicate = (Predicate)queryUnarySentence.getOperand().getSentence();
				}
				else{
					predicate = (Predicate)subsentence;
					isNegative = false;
				}
				String predicateName = predicate.getName();
				if(hashMap.containsKey(predicateName)){
					//There is some other sentence having same predicate
					SentencesForPredicate sentencesForPredicate = hashMap.get(predicateName);
					ArrayList<DisjunctionSentence> sentencesWithOpposite;
					if(isNegative) {
						sentencesWithOpposite = sentencesForPredicate.getSentencesPositive();
					}
					else{
						sentencesWithOpposite = sentencesForPredicate.getSentencesNegative();
					}
					int oppositeIndex = 0;
					while(oppositeIndex < sentencesWithOpposite.size()){
						DisjunctionSentence sentenceWithOpposite = sentencesWithOpposite.get(oppositeIndex);
						oppositeIndex++;
//						System.out.println("Opposite:"+sentenceWithOpposite);
						if(currentSentence.isContradiction(sentenceWithOpposite)){
							//There is a contradiction
							//This proof was successful
//							System.out.println("Contradiction:");
//							System.out.println(currentSentence);
//							System.out.println(sentenceWithOpposite);
							return true;
						}
						UnificationSubstitution substitution = currentSentence.unify(sentenceWithOpposite, predicate, isNegative);
						if(substitution == null){
							//Cannot be unified
							continue;
						}
						DisjunctionSentence newDisjunctionSentence = createNewSentenceWithSubstitution(substitution, currentSentence, sentenceWithOpposite, predicate);
						if(newDisjunctionSentence == null){
							//Nothing was there except the predicates
							//Case like A(Dog), ~A(Cat)
							continue;
						}
						//A new disjunction sentence has been created. Need to add it to kb.
//						System.out.println("New Disjunction:"+newDisjunctionSentence);
						sentences.add(currentIndex+1, new KBSentence(newDisjunctionSentence));
						addDisjunctionSentenceToHashMap(newDisjunctionSentence);
					}
				}
			}
			currentIndex++;
		}
		return false;
	}

	private DisjunctionSentence createNewSentenceWithSubstitution(UnificationSubstitution substitution,
			DisjunctionSentence currentSentence, DisjunctionSentence sentenceWithOpposite, Predicate predicate) {
		String predicateName = predicate.getName();
		DisjunctionSentence newDisjunctionSentence = new DisjunctionSentence();
		ArrayList<KBSentence> thisSubsentences = currentSentence.getSubsentences();
		ArrayList<KBSentence> otherSubsentences = sentenceWithOpposite.getSubsentences();
		Sentence thisUnifiedSubSentence = (substitution.thisUnarySentence == null)?substitution.thisPredicate:substitution.thisUnarySentence;
		Sentence otherUnifiedSubSentence = (substitution.otherUnarySentence == null)?substitution.otherPredicate:substitution.otherUnarySentence;
		addToDisjunctionSentence(substitution, predicateName, thisSubsentences, newDisjunctionSentence, thisUnifiedSubSentence);
		addToDisjunctionSentence(substitution, predicateName, otherSubsentences, newDisjunctionSentence, otherUnifiedSubSentence);
		
		if(newDisjunctionSentence.getSubsentences().size() == 0){
			return null;
		}
		return newDisjunctionSentence;
	}

	private void addToDisjunctionSentence(UnificationSubstitution substitution, String predicateName,
			ArrayList<KBSentence> thisSubsentences, DisjunctionSentence newDisjunctionSentence, Sentence unifiedSubSentence) {
		HashMap<String, Integer> variableToStandardizedValue = new HashMap<String, Integer>();
		for(KBSentence kbSentence : thisSubsentences){
			Sentence thisSentence = kbSentence.getSentence();
			if(!thisSentence.equals(unifiedSubSentence)){
				//This is not the same as original unified subsentence
				Predicate thisPredicate = getPredicateFromKBSentence(kbSentence);
				Sentence newSentence;
				ArrayList<Parameter> newParameters = new ArrayList<Parameter>();
				if(thisSentence instanceof Predicate){
					newSentence = new Predicate(thisPredicate.getName(), newParameters);
				}
				else{
					//This is a unary sentence
					newSentence = new UnarySentence(new NotOperator(), new KBSentence(new Predicate(thisPredicate.getName(), newParameters)));
				}
				//Need to substitute the variables if applicable
				ArrayList<Parameter> thisParameters = thisPredicate.getParameters();
				for(Parameter parameter:thisParameters){
					if(parameter instanceof Variable){
						Variable variable = (Variable)parameter;
						String variableName = variable.getName();
						if(substitution.variableToConstant.containsKey(variable.toString())){
							//Needs to be substituted
							String constantName = substitution.variableToConstant.get(variable.toString());
							newParameters.add(new Constant(constantName));
						}
						else{
							//No need to substitute
							//Need to add a new Variable according to the sentence
							Variable newVariable = new Variable(variableName);
							if(variableToStandardizedValue.containsKey(variable.toString())){
								//This variable has already occurred in the disjunction sentence
								Integer standardizedValue = variableToStandardizedValue.get(variable.toString());
								newVariable.setStandardizedValue(standardizedValue);
							}
							else{
								//This variable has not yet occurred in this disjunction sentence
								int newStandardizedValue = Variable.getMaxStandardizedValue() + 1;
								newVariable.setStandardizedValue(newStandardizedValue);
								variableToStandardizedValue.put(variable.toString(), newStandardizedValue);
							}
							newParameters.add(newVariable);
						}
					}
					else{
						//Parameter is a constant
						newParameters.add(parameter);
					}
				}
				newDisjunctionSentence.addSentence(new KBSentence(newSentence));
			}
			else{
				//This is same Predicate. Will be skipped
			}
		}
	}

}
