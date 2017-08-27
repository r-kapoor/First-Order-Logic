package hw3;

public class UnarySentence extends Sentence{
	
	private UnaryOperator operator;
	private KBSentence operand;
	
	public UnarySentence(String sentenceString){
		String firstChar = getFirstCharString(sentenceString);
		if(NotOperator.isOperator(firstChar)){
			//This is a not operator
			//Remove 1st char so that the string is now the remaining sentence
			sentenceString = sentenceString.substring(1);
			this.operator = new NotOperator();
			operand = new KBSentence(sentenceString);
		}
	}
	
	public UnarySentence(UnaryOperator operator, KBSentence operand) {
		super();
		this.operator = operator;
		this.operand = operand;
	}
	
	private static String getFirstCharString(String sentence){
		char firstChar = sentence.charAt(0);
		return Character.toString(firstChar);
	}

	public static boolean isUnarySentence(String sentence) {
		//Assumption: Unary Sentence starts with an operator
		if(Operator.isOperator(getFirstCharString(sentence))){
			return true;
		}
		return false;
	}
	
	public String toString(){
		return operator.toString() + operand.toString();
	}
	
	@Override
	public Sentence convertToCNF(){
		eliminateImplication();
//		System.out.println(this);
		Sentence reducedScope = reduceScopeOfNegation();
		return reducedScope.convertToConjunctionOfDisjunctions();	
	}
	
	@Override
	public void eliminateImplication(){
		operand.eliminateImplication();
	}
	
	@Override
	public Sentence reduceScopeOfNegation(){
		Sentence operandSentence = operand.getSentence();
		if(operandSentence instanceof Predicate){
			//Is of the form  ~A
			//Scope cannot be reduced further
		}
		else if(operandSentence instanceof UnarySentence){
			//Inner is another unary sentence
			UnarySentence innerSentence = (UnarySentence)operandSentence;
			if(innerSentence.getOperator() instanceof NotOperator){
				//Is of the form ~(~A)
				//Convert it to form A
				KBSentence innerOperand = innerSentence.getOperand();
				innerOperand.reduceScopeOfNegation();
				return innerOperand.getSentence();	
			}
		}
		else if(operandSentence instanceof CompositeSentence){
			CompositeSentence innerSentence = (CompositeSentence)operandSentence;
			BinaryOperator newOperator = null;
			if(innerSentence.getOperator() instanceof OrOperator){
				//Is of the form ~(A|B)
				//Convert to ~A&~B
				newOperator = new AndOperator();
			}
			else if(innerSentence.getOperator() instanceof AndOperator){
				//Is of the form ~(A&B)
				//Convert to ~A|~B
				newOperator = new OrOperator();
			}
			//Assuming that => has been eliminated
			KBSentence leftOperand = innerSentence.getOperandLeft();
			KBSentence rightOperand = innerSentence.getOperandRight();
			KBSentence newLeftOperand = new KBSentence(new UnarySentence(new NotOperator(), leftOperand));
			KBSentence newRightOperand = new KBSentence(new UnarySentence(new NotOperator(), rightOperand));
			Sentence newSentence = new CompositeSentence(newOperator, newLeftOperand, newRightOperand);
			newLeftOperand.reduceScopeOfNegation();
			newRightOperand.reduceScopeOfNegation();
			return newSentence;
		}
		return this;
	}

	public UnaryOperator getOperator() {
		return operator;
	}

	public KBSentence getOperand() {
		return operand;
	}

	@Override
	public ConjunctionSentence convertToConjunctionOfDisjunctions() {
		//At this moment the operand of the sentence should only be a predicate (as scope of negation is already reduced)
		//Thus this is already of the form- ~A
		ConjunctionSentence conjunctiveSentence = new ConjunctionSentence();
		KBSentence kbSentence = new KBSentence(new DisjunctionSentence(new KBSentence(this)));
		conjunctiveSentence.addSentence(kbSentence);
		return conjunctiveSentence;
	}

	public boolean isSameAs(UnarySentence otherUnarySentence) {
		if(this.equals(otherUnarySentence)){
			return true;
		}
		Predicate thisPredicate = (Predicate)this.operand.getSentence();
		Predicate otherPredicate = (Predicate)otherUnarySentence.operand.getSentence();
		if(thisPredicate.isSameAs(otherPredicate)){
			return true;
		}
		return false;
	}
}
