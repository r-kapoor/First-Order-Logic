package hw3;

import java.util.ArrayList;

public class KnowledgeBase implements Cloneable{
	
	private ArrayList<KBSentence> sentences = new ArrayList<KBSentence>();
	
	public KnowledgeBase(ArrayList<KBSentence> sentences) {
		this.sentences = sentences;
	}

	public KnowledgeBase() {
	}

	@Override
	public KnowledgeBase clone(){
		@SuppressWarnings("unchecked")
		KnowledgeBase kb = new KnowledgeBase((ArrayList<KBSentence>) sentences.clone());
		return kb;
	}
	
	public void add(KBSentence sentence){
		sentences.add(sentence);
	}
	
	public void addToFront(KBSentence sentence){
		sentences.add(0, sentence);
	}

	public ArrayList<KBSentence> getSentences() {
		return sentences;
	}

	public void convertToCNF() {
		for (KBSentence sentence : sentences){
//			System.out.println(sentence);
			sentence.convertToCNF();
//			System.out.println(sentence);
		}
	}
	
	public String toString(){
		String text = "";
		for (KBSentence sentence : sentences){
			text += sentence.toString() + "\n";
		}
		return text;
	}

	public void createSeparateClauses() {
		ArrayList<KBSentence> newSentences = new ArrayList<KBSentence>();
		for (KBSentence kbSentence : sentences){
			Sentence sentence = kbSentence.getSentence();
			if(sentence instanceof ConjunctionSentence){
				ConjunctionSentence conjunctionSentence  = (ConjunctionSentence)sentence;
				newSentences.addAll(conjunctionSentence.getSubsentences());
			}
			else {
				newSentences.add(kbSentence);
			}
		}
		this.sentences = newSentences;
	}
	
	private Predicate standardizePredicate(Predicate predicate, int count){
		ArrayList<Parameter> newParameters = new ArrayList<Parameter>();
		Predicate newPredicate = new Predicate(predicate.getName(), newParameters);
		ArrayList<Parameter> parameters = predicate.getParameters();
		for(Parameter parameter : parameters){
			if(parameter instanceof Variable){
				Variable variable = (Variable) parameter;
				Variable newVariable = new Variable(variable, count);
				newParameters.add(newVariable);
			}
			else{
				newParameters.add(parameter);
			}
		}
		return newPredicate;
	}
	
	private UnarySentence standardizeUnarySentence(UnarySentence unarySentence, int count){
		Sentence sentence = unarySentence.getOperand().getSentence();
		if(sentence instanceof Predicate){
			Predicate predicate = (Predicate)sentence;
			sentence = standardizePredicate(predicate, count);
		}
		KBSentence kbSentence = new KBSentence(sentence);
		return new UnarySentence(unarySentence.getOperator(), kbSentence);
	}
	
	private DisjunctionSentence standardizeDisjunctionSentence(DisjunctionSentence disjunctionSentence, int count){
		ArrayList<KBSentence> subsentences = disjunctionSentence.getSubsentences();
		ArrayList<KBSentence> newSubsentences = new ArrayList<KBSentence>();
		for(KBSentence subsentence : subsentences){
			Sentence sentence = subsentence.getSentence();
			if(sentence instanceof UnarySentence){
				UnarySentence unarySentence = (UnarySentence)sentence;
				sentence = standardizeUnarySentence(unarySentence, count);
			}
			else if(sentence instanceof Predicate){
				Predicate predicate = (Predicate)sentence;
				sentence = standardizePredicate(predicate, count);
			}
			newSubsentences.add(new KBSentence(sentence));
		}
		DisjunctionSentence newDisjunctionSentence = new DisjunctionSentence(newSubsentences);
		return newDisjunctionSentence;
	}

	public void standardizeVariables() {
		int count = 0;
		for (KBSentence kbSentence : sentences){
			Sentence sentence = kbSentence.getSentence();
			if(sentence instanceof Predicate){
				Predicate predicate = (Predicate)sentence;
				kbSentence.setSentence(standardizePredicate(predicate, count));
			}
			else if(sentence instanceof UnarySentence){
				UnarySentence unarySentence = (UnarySentence)sentence;
				kbSentence.setSentence(standardizeUnarySentence(unarySentence, count));
			}
			else if(sentence instanceof DisjunctionSentence){
				DisjunctionSentence disjunctionSentence = (DisjunctionSentence)sentence;
				kbSentence.setSentence(standardizeDisjunctionSentence(disjunctionSentence, count));
			}
			//In all other cases not standardizing. Assuming it is in one of the above forms
			count++;
		}
	}

}
