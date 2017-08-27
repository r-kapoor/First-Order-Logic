package hw3;

public class NotOperator extends UnaryOperator {

	private String operatorSymbol = "~";
	private OperatorEnum operatorEnum = OperatorEnum.NOT;

	@Override
	public String getOperatorSymbol() {
		return operatorSymbol;
	}
	
	@Override
	public Enum<OperatorEnum> getOperatorEnum() {
		return operatorEnum;
	}

}
