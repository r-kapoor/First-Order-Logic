package hw3;

public class OrOperator extends BinaryOperator {
	
	private String operatorSymbol = "|";
	private OperatorEnum operatorEnum = OperatorEnum.OR;

	@Override
	public String getOperatorSymbol() {
		return operatorSymbol;
	}
	
	@Override
	public Enum<OperatorEnum> getOperatorEnum() {
		return operatorEnum;
	}

}
