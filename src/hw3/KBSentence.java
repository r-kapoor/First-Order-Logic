package hw3;

public class KBSentence implements Comparable<KBSentence>{
	private Sentence sentence;
	
	public void setSentence(Sentence sentence) {
		this.sentence = sentence;
	}

	private static String preprocessString(String sentenceString){
		return sentenceString.replaceAll("\\s+","");
	}
	
	public KBSentence(String sentenceString){
		sentenceString = preprocessString(sentenceString);
		
		if(sentenceString.startsWith("(")){
			//The sentence starts with ( and hence contains a unary or composite sentence
			String subsentence = sentenceString.substring(1, sentenceString.length() - 1);//Removing 1st and last char which are (
			if(UnarySentence.isUnarySentence(subsentence)){
				this.sentence = new UnarySentence(subsentence);
			}
			else{
				//This is a composite sentence
				this.sentence = new CompositeSentence(subsentence);
			}
		}
		else{
			//The sentence doesn't start with ( and hence has to be a predicate
			this.sentence = new Predicate(sentenceString);
		}
	}
	
	public KBSentence(Sentence sentence) {
		this.sentence = sentence;
	}

	public Sentence getSentence() {
		return sentence;
	}

	public String toString(){
		return "("+sentence.toString()+")";
	}
	
	public void convertToCNF(){
		eliminateImplication();
//		System.out.println("Implication Removal");
//		System.out.println(this);
		reduceScopeOfNegation();
//		System.out.println("Reduce Scope of Negation");
//		System.out.println(this);
		convertToConjunctionOfDisjunctions();
//		System.out.println("Convert to Conj of Disj");
	}

	public void eliminateImplication() {
		sentence.eliminateImplication();
	}
	
	public void reduceScopeOfNegation() {
		sentence = sentence.reduceScopeOfNegation();
	}
	
	public void convertToConjunctionOfDisjunctions(){
		sentence = sentence.convertToConjunctionOfDisjunctions();
	}

	@Override
	public int compareTo(KBSentence o) {
		Sentence otherSentence = o.getSentence();
		if(otherSentence instanceof DisjunctionSentence && sentence instanceof DisjunctionSentence){
			DisjunctionSentence otherDJSentence = (DisjunctionSentence)otherSentence;
			DisjunctionSentence thisDJSentence = (DisjunctionSentence)sentence;
			return (thisDJSentence.getSubsentences().size() - otherDJSentence.getSubsentences().size());
		}
		return 0;
	}

}
