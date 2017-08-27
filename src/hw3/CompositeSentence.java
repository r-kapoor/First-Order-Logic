package hw3;

public class CompositeSentence extends Sentence{
	
	private BinaryOperator operator;
	private KBSentence operandLeft;
	private KBSentence operandRight;
	
	public BinaryOperator getOperator() {
		return operator;
	}

	public KBSentence getOperandLeft() {
		return operandLeft;
	}

	public KBSentence getOperandRight() {
		return operandRight;
	}

	public CompositeSentence(String sentenceString){
		int operatorStartIndex = getIndexOfTopLevelOperator(sentenceString);
		String leftSentence = sentenceString.substring(0,operatorStartIndex);
		char operatorFirstChar = sentenceString.charAt(operatorStartIndex);
		Class<? extends Operator> operatorClass = Operator.getBinaryOperatorForFirstChar(operatorFirstChar);
		try {
			this.operator = (BinaryOperator) operatorClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int operatorEndIndex = operatorStartIndex + operator.getOperatorSymbol().length();
		String rightSentence = sentenceString.substring(operatorEndIndex);
		operandLeft = new KBSentence(leftSentence);
		operandRight = new KBSentence(rightSentence);
	}
	
	private int getIndexOfTopLevelOperator(String sentenceString) {
		int openedBrackets = 0;
		for(int i = 0; i < sentenceString.length(); i++){
			char currentChar = sentenceString.charAt(i);
			if(currentChar == '('){
				openedBrackets++;
			}
			else if(currentChar == ')'){
				openedBrackets--;
			}
			else{
				//This char might be a part of operator or operand
				if(openedBrackets == 0){
					//This is a top level operator, if an operator
					if(Operator.isOperatorFirstChar(currentChar)){
						//This is an operator or 1st character of it
						return i;
//						operator1.ge
					}
				}
			}
		}
		return -1;
	}

	public CompositeSentence(BinaryOperator operator, KBSentence operandLeft, KBSentence operandRight) {
		this.operator = operator;
		this.operandLeft = operandLeft;
		this.operandRight = operandRight;
	}
	
	public String toString(){
		return operandLeft.toString() + operator.toString() + operandRight.toString();
	}
	
	@Override
	public void eliminateImplication(){
		if(operator instanceof ImpliesOperator){
			//This is of the form P=>Q
			//Convert it to form ~P|Q
			operator = new OrOperator();
			operandLeft = new KBSentence(new UnarySentence(new NotOperator(), operandLeft));
		}
		operandLeft.eliminateImplication();
		operandRight.eliminateImplication();
	}
	
	@Override
	public Sentence reduceScopeOfNegation(){
		operandLeft.reduceScopeOfNegation();
		operandRight.reduceScopeOfNegation();
		return this;
	}

	@Override
	public Sentence convertToCNF(){
		eliminateImplication();
//		System.out.println(this);
		Sentence reducedScope = reduceScopeOfNegation();
		return reducedScope.convertToConjunctionOfDisjunctions();
	}
	
	private boolean sentenceIsUnit(Sentence sentence){
		return sentence instanceof Predicate || sentence instanceof UnarySentence;
	}
	
	@Override
	public ConjunctionSentence convertToConjunctionOfDisjunctions() {
		Sentence leftSentence = operandLeft.getSentence();
		Sentence rightSentence = operandRight.getSentence();
		if(sentenceIsUnit(leftSentence) && sentenceIsUnit(rightSentence)){
			//Is already Conj of Disj
			if(operator instanceof AndOperator){
				//is of the form A&B or ~A&B or A&~b or ~A&~B
				ConjunctionSentence leftConjSentence = leftSentence.convertToConjunctionOfDisjunctions();
				ConjunctionSentence rightConjSentence = rightSentence.convertToConjunctionOfDisjunctions();
				leftConjSentence.concatSentence(rightConjSentence);
				return leftConjSentence;
			}
			else if(operator instanceof OrOperator){
				//is of the form A|B or ~A|B or ...
				DisjunctionSentence disjunctionSentence = new DisjunctionSentence(operandLeft);
				disjunctionSentence.addSentence(operandRight);
				ConjunctionSentence conjunctionSentence = new ConjunctionSentence(new KBSentence(disjunctionSentence));
				return conjunctionSentence;
			}
		}
		else{
			//One or both of the subsentences are composite
			if(operator instanceof AndOperator){
				ConjunctionSentence leftConjSentence = leftSentence.convertToConjunctionOfDisjunctions();
				ConjunctionSentence rightConjSentence = rightSentence.convertToConjunctionOfDisjunctions();
				leftConjSentence.concatSentence(rightConjSentence);
				return leftConjSentence;
			}
			else if(operator instanceof OrOperator){
				ConjunctionSentence leftConjSentence = leftSentence.convertToConjunctionOfDisjunctions();
				ConjunctionSentence rightConjSentence = rightSentence.convertToConjunctionOfDisjunctions();
				return leftConjSentence.crossSentences(rightConjSentence);
			}
		}
		return new ConjunctionSentence();
	}
}
