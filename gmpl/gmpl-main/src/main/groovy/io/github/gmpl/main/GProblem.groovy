package io.github.gmpl.main

class GProblem implements GFunctions {

	static {
		GCommon.init()
	}

	def static GProblem defaultProblem = new GProblem();

	def static getDefaultProblem(){
		return defaultProblem
	}

	def constraints = [] as List<GElement>

	def clause(GClause clause){
		constraints.add(clause)
	}

	def constraint(GCompare compare){
		constraints.add(compare)
	}

	def getConstraints(){
		constraints
	}

	def getVariables(){
		def variables = [] as Set
		for(def constraint in getConstraints()){
			variables += getVariables(constraint)
		}
		return variables as List
	}

	def getVariables(GElement element){
		switch(element) {
			case { it instanceof GVariable }:
				return [element]
			case { it instanceof GLiteral}:
				return getVariables(((GLiteral)element).getVariable())
			case { it instanceof Iterable}:
				Iterable iterable = (Iterable)element
				def result = [] as Set
				for(def it in iterable){
					result += getVariables(it)
				}
				return result
			case { it instanceof GCompare}:
				return getVariables(((GCompare)element).getLhs()) +
						getVariables(((GCompare)element).getRhs())
			default:
				return []
		}
	}

	def add(GProblem problem) {
		constraints.addAll(problem.getConstraints())
	}
}
