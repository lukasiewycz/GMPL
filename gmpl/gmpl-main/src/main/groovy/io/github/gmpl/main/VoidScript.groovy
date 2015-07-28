package io.github.gmpl.main

/**
 * Created by martin.lukasiewycz on 28/7/2015.
 */
/*for(GVariable var in queensProblem.x[_,_]){
    println '' + var + ' ' + (var as double)
}*/

//queensProblem.constraint(compare(sum(j: 1..8, {j -> queensProblem.x[2, j]}),'==',1))
/*println queensProblem.getVariables()
for (constraint in queensProblem.constraints) {
    println constraint
}*/

//println problem.getConstraints().join('\n')

//println sum(a: 1..2, b: [2,4,7], {a,b -> z[a,b]})


/*def queensProblem = new GProblem() {

    {
        for (i in (1..8)) {
            constraint compare(sum(x[i, _]), '==', 1)
            constraint compare(sum(x[_, i]), '==', 1)
            //clause disjunction(x[i, _])
            //clause disjunction(x[_, i])
            //constraint sum(x[_, i]) == 1
            //constraint sum(x[i,_]) == 1
            //constraint compare(sum(j: 1..8, {j -> x[j, i]}),'==',1)
        }

        for (i in (1..8)) for (j in (1..8)) for (k in (1..8)) for (l in (1..8)) if ((i - j == k - l || i + j == k + l) && (i != k || j != l)) {
            clause ~x[i, j] | ~x[k, l]
        }
    }
}*/