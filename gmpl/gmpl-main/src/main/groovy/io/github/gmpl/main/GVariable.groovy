package io.github.gmpl.main

class GVariable extends GElement {

	static {
		GCommon.init()
	}
	
	def name
	def type
	def domain
	
	GVariable(GVariableDomain domain, String indices, Class type){
		this(indices,type)
		this.domain = domain
	}
	
	GVariable(Object name, Class type) {
		super()
		this.name = name
		this.type = type
	}

	String toString() {
		if(domain!=null){
			domain.name + name
		} else {
			name
		}
	}
	
	def bitwiseNegate(){
		if(type != boolean){
			throw new IllegalArgumentException("Variable ${this.toString()} is not a boolean negate (~) is not a valid operation")
		}
		
		new GLiteral(this, false)
	}

}
