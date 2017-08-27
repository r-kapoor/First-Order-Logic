package hw3;

public class Constant extends Parameter{
	
	private String name;

	public Constant(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public String toString(){
		return name;
	}
	@Override
	public boolean isSameAs(Parameter other) {
		if(other instanceof Constant){
			Constant otherConstant = (Constant)other;
			if(this.equals(otherConstant)){
				return true;
			}
			if(this.name.equals(otherConstant.name)){
				return true;
			}
		}
		return false;
	}

}
