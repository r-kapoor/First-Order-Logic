package hw3;

public abstract class Sentence {
	
	public abstract String toString();
	
	public abstract Sentence convertToCNF();

	public abstract void eliminateImplication();
	
	public abstract Sentence reduceScopeOfNegation();
	
	public abstract ConjunctionSentence convertToConjunctionOfDisjunctions();
}