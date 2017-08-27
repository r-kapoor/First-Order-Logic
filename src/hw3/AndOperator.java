package hw3;

public class AndOperator extends BinaryOperator {

	private String operatorSymbol = "&";
	private OperatorEnum operatorEnum = OperatorEnum.AND;
	@Override
	public String getOperatorSymbol() {
		return operatorSymbol;
	}

	@Override
	public Enum<OperatorEnum> getOperatorEnum() {
		return operatorEnum;
	}

}
