package io.github.gmpl.main

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(excludes=["type","value"])
class GVariable extends GElement {

	static {
		GCommon.init()
	}
	
	def name
	def domain
	def type
	def value
	
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

	def asType(Class type){
		if(value == null){
			throw new IllegalArgumentException('Value of '+toString()+' is null')
		}

		switch(type){
			case boolean:
			case Boolean.class:
				return (value as boolean)
			case int:
			case Integer.class:
				return (value as int)
			case double:
			case Double.class:
				return (value as double)
			case float:
			case Float.class:
				return (value as float)
			case short:
			case Short.class:
				return (value as short)
			case byte:
			case Byte.class:
				return (value as byte)
			default:
				throw new IllegalArgumentException("Unknown type "+type)
		}
	}

}
