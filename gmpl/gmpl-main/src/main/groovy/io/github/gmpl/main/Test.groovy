package io.github.gmpl.main


def sum(List list){
	println list
}

sum([1,2])

def queensProblem = new GProblem() {
	def x = var type: boolean, domain: [(1..8), (1..8)]
	
	/*constraints*/ {
		for(i in (1..8)) {
			constraint compare(sum(j: 1..8, {j -> x[i, j]}),'==',1)
			constraint compare(sum(j: 1..8, {j -> x[j, i]}),'==',1)
		}
		
		for(i in (1..8)) for(j in (1..8)) for(k in (1..8)) for(l in (1..8)) if((i-j == k-l || i+j == k+l) && (i!=k || j!=l))  {
			clause ~x[i, j] | ~x[k, l]
		}
	}
}

//queensProblem.constraint(compare(sum(j: 1..8, {j -> queensProblem.x[2, j]}),'==',1))


println queensProblem.getVariables()
for(constraint in queensProblem.constraints){
	println constraint
}

def solver = GSolverSAT4J.newInstance();
solver.add(queensProblem)
solver.solve()

println solver.getVariables()
for(constraint in solver.getConstraints()){
	//println constraint
}




//println problem.getConstraints().join('\n')


//println sum(a: 1..2, b: [2,4,7], {a,b -> z[a,b]})
