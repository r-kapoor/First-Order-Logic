package hw3;

import java.util.ArrayList;

public class SentencesForPredicate {
	
	private ArrayList<DisjunctionSentence> sentencesPositive = new ArrayList<DisjunctionSentence>();
	private ArrayList<DisjunctionSentence> sentencesNegative = new ArrayList<DisjunctionSentence>();
	
	public ArrayList<DisjunctionSentence> getSentencesPositive() {
		return sentencesPositive;
	}
	public ArrayList<DisjunctionSentence> getSentencesNegative() {
		return sentencesNegative;
	}
	
	public void addSentenceToPositive(DisjunctionSentence disjunctionSentence){
		//Adding without checking in interest of time
		sentencesPositive.add(disjunctionSentence);
	}
	
	public void addSentenceToNegative(DisjunctionSentence disjunctionSentence){
		sentencesNegative.add(disjunctionSentence);
	}
	
	public String toString(){
		String text = "";
		text += "Positive:\n";
		for(DisjunctionSentence disjunctionSentence : sentencesPositive){
			text += disjunctionSentence.toString() + "\n";
		}
		text += "Negative:\n";
		for(DisjunctionSentence disjunctionSentence : sentencesNegative){
			text += disjunctionSentence.toString() + "\n";
		}
		return text;
	}
}
