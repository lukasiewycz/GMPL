package io.github.gmpl.main.test



QueensProblem p2 = QueensProblem.newInstance(8);

println p2.getVariables()
for(constraint in p2.getConstraints()){
	println constraint
}