package hw3;

abstract class Operator {
	
	public abstract String getOperatorSymbol();
	public abstract Enum<OperatorEnum> getOperatorEnum();
	
	public boolean isOperatorSymbol(String operatorSymbol) {
		if(this.getOperatorSymbol().equals(operatorSymbol)){
			return true;
		}
		return false;
	}
	
	public boolean isCharBegOfOperatorSymbol(char operatorChar){
		if(this.getOperatorSymbol().charAt(0) == operatorChar){
			return true;
		}
		return false;
	}
	
	public static boolean isOperator(String operatorString){
		//Implementing with reflections better
		if(new NotOperator().isOperatorSymbol(operatorString) || new AndOperator().isOperatorSymbol(operatorString) 
				|| new ImpliesOperator().isOperatorSymbol(operatorString)
				|| new OrOperator().isOperatorSymbol(operatorString)){
			return true;
		}
		return false;
	}
	
	public static boolean isOperatorFirstChar(char operatorChar){
		//Implementing with reflections better
		if(new NotOperator().isCharBegOfOperatorSymbol(operatorChar) || new AndOperator().isCharBegOfOperatorSymbol(operatorChar) 
				|| new ImpliesOperator().isCharBegOfOperatorSymbol(operatorChar)
				|| new OrOperator().isCharBegOfOperatorSymbol(operatorChar)){
			return true;
		}
		return false;
	}
	public static Class<? extends BinaryOperator> getBinaryOperatorForFirstChar(char currentChar) {
		//Implementing with reflections better
		if(new AndOperator().isCharBegOfOperatorSymbol(currentChar)){
			return AndOperator.class;
		}
		else if(new ImpliesOperator().isCharBegOfOperatorSymbol(currentChar)){
			return ImpliesOperator.class;
		}
		else if(new OrOperator().isCharBegOfOperatorSymbol(currentChar)){
			return OrOperator.class;
		}
		return null;
	}
	
	public String toString(){
		return getOperatorSymbol();
	}

}
