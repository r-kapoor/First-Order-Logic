package hw3;

import java.util.ArrayList;

public class ConjunctionSentence extends Sentence {
	
	private ArrayList<KBSentence> subsentences;

	public ConjunctionSentence() {
		this.subsentences = new ArrayList<KBSentence>();
	}
	
	public ConjunctionSentence(ArrayList<KBSentence> subsentences) {
		this.subsentences = subsentences;
	}
	
	public ConjunctionSentence(KBSentence sentence){
		this.subsentences = new ArrayList<KBSentence>();
		subsentences.add(sentence);
	}

	public void addSentence(KBSentence sentence){
		this.subsentences.add(sentence);
	}
	
	public ArrayList<KBSentence> getSubsentences() {
		return subsentences;
	}

	public void concatSentence(ConjunctionSentence conjunctionSentence){
		this.subsentences.addAll(conjunctionSentence.getSubsentences());
	}
	
	public ConjunctionSentence crossSentences(ConjunctionSentence other){
		ConjunctionSentence newSentence = new ConjunctionSentence();
		ArrayList<KBSentence> otherSubsentences = other.getSubsentences();
		for(KBSentence thisKBSentence : subsentences){
			for(KBSentence otherKBSentence : otherSubsentences){
				Sentence thisSentence = thisKBSentence.getSentence();
				Sentence otherSentence = otherKBSentence.getSentence();
				if(thisSentence instanceof UnarySentence || thisSentence instanceof Predicate){
					thisSentence = new DisjunctionSentence(new KBSentence(thisSentence));
				}
				if(otherSentence instanceof UnarySentence || otherSentence instanceof Predicate){
					otherSentence = new DisjunctionSentence(new KBSentence(otherSentence));
				}
				if(thisSentence instanceof DisjunctionSentence && otherSentence instanceof DisjunctionSentence){
					DisjunctionSentence thisDJSentence = (DisjunctionSentence) thisSentence; 
					DisjunctionSentence otherDJSentence = (DisjunctionSentence) otherSentence;
					DisjunctionSentence newDisjunctionSentence = thisDJSentence.addToCreate(otherDJSentence);
					KBSentence kbSentence = new KBSentence(newDisjunctionSentence);
					newSentence.addSentence(kbSentence);
				}
				else {
					DisjunctionSentence disjunctionSentence = new DisjunctionSentence(thisKBSentence);
					disjunctionSentence.addSentence(otherKBSentence);
					KBSentence kbSentence = new KBSentence(disjunctionSentence);
					newSentence.addSentence(kbSentence);
				}
			}
		}
		return newSentence;
	}
	
	@Override
	public String toString() {
		String text = "";
		int count = 0;
		for(KBSentence subsentence : subsentences){
			if(count > 0){
				text += "&";
			}
			text += subsentence.toString();
			count++;
		}
		return text;
	}

	@Override
	public Sentence convertToCNF() {
		//Should be in CNF
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
		return this;
	}

}
