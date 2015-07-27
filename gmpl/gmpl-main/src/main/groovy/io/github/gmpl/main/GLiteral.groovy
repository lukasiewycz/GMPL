package io.github.gmpl.main

class GLiteral extends GElement {
	
	def sign
	def variable

	public GLiteral(GVariable variable, boolean sign) {
		super();
		this.variable = variable;
	}
	
	GVariable bitwiseNegate(){
		if(type != boolean){
			throw new IllegalArgumentException("Variable ${name} is not a boolean negate (~) is not a valid operation")
		}
		
		new GLiteral(variable, !sign);
	}

	String toString() {
		(!sign?"~":"")+variable
	}
	
	
	
}
