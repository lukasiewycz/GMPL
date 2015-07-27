package io.github.gmpl.main

import groovy.lang.Closure;
import java.util.Map;

class GProblem implements GFunctions {

	static {
		GCommon.init()
	}
	
	def variables = [] as Set
	def constraints = [] as List<Object>
	def isinit = false


	
	def clause(GClause clause){
		constraints.add(clause)
	}

	def constraint(GCompare compare){
		constraints.add(compare)
	}

	def getConstraints(){
		initialize()
		constraints
	}

	def getVariables(){
		initialize()
		variables
	}

	def var(Map map = [:]){
		String name = map['name']
		Class type = map['type']
		List domain = map['domain']

		if(domain == null){
			new GVariable(name, type);
		} else {
			new GVariableDomain(name, type, domain);
		}
	}

	def initialize(){
		if(!isinit){
			isinit = true
			this.properties.collect{
				if(it.value instanceof GVariableDomain){
					if(it.value.name == null){
						it.value.name = it.key
					}
				}
			}
			
			this.properties.collect{
				if(it.value instanceof GVariableDomain){
					variables += it.value.retrieveAllDefinedVariables()
				}
			}
		}
	}



	def add(GProblem problem) {
		variables += problem.getVariables()
		constraints += problem.getConstraints()
	}
}
