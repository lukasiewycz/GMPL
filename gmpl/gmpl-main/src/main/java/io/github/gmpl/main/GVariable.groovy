package io.github.gmpl.main

class GVariable extends GElement {

	def name;
	def type;
	
	GVariable(Object name, Class type) {
		super();
		this.name = name;
		this.type = type;
	}

	String toString() {
		name
	}
	
	def bitwiseNegate(){
		if(type != boolean){
			throw new IllegalArgumentException("Variable ${name} is not a boolean negate (~) is not a valid operation")
		}
		
		new GLiteral(this, false)
	}

}
