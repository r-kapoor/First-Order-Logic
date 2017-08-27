package hw3;

public class Variable extends Parameter{
	
	private String name;
	private int standardizedValue = -1;
	private static int maxStandardizedValue = -1;
	
	public static int getMaxStandardizedValue() {
		return maxStandardizedValue;
	}

	public Variable(String name){
		this.name = name;
	}
	
	public Variable(Variable variable, int value){
		this.name = variable.getName();
		if(value > maxStandardizedValue){
			maxStandardizedValue = value;
		}
		this.standardizedValue = value;
	}

	public String getName() {
		return name;
	}
	
	public int getStandardizedValue(){
		return standardizedValue;
	}
	
	public void setStandardizedValue(int value){
		if(value > maxStandardizedValue){
			maxStandardizedValue = value;
		}
		standardizedValue = value;
	}
	
	public boolean isStandardized(){
		if(standardizedValue == -1){
			return false;
		}
		return true;
	}

	public String toString(){
		String text = name;
		if(isStandardized()){
			text += standardizedValue;
		}
		return text;
	}

	public void setName(String name){
		this.name = name;
	}

	@Override
	public boolean isSameAs(Parameter other) {
		if(other instanceof Variable){
			Variable otherVariable = (Variable)other;
			if(this.equals(otherVariable)){
				return true;
			}
			if(this.toString().equals(other.toString())){
				return true;
			}
		}
		return false;
	}

}
