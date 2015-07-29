package io.github.gmpl.main

class GLiteral extends GElement {
	
	def sign
	def variable

	protected GLiteral(GVariable variable){
		this.variable = variable
		this.sign = variable as boolean
	}

	public GLiteral(GVariable variable, boolean sign) {
		super();
		this.variable = variable
		this.sign = sign
	}

	GLiteral bitwiseNegate(){
		if(variable.type != boolean){
			throw new IllegalArgumentException("Variable ${variable.name} is not a boolean negate (~) is not a valid operation")
		}
		
		new GLiteral(variable, !sign);
	}

	String toString() {
		(!sign?"~":"")+variable
	}
	
	
	
}
