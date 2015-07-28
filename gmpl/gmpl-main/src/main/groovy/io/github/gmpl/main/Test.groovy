package io.github.gmpl.main


String _ = '_'

def var(Map map){
    String name = map['name']
    Class type = map['type']
    List domain = map['domain']

    if(domain == null){
        new GVariable(name, type);
    } else {
        new GVariableDomain(name, type, domain);
    }
}

def clause(GClause clause){
    GProblem.getDefaultProblem().constraints.add(clause)
}

def clause(GElement clause){
    GClause cl = new GClause()
    cl.or(clause)
    GProblem.getDefaultProblem().constraints.add(cl)
}

def constraint(GCompare compare){
    GProblem.getDefaultProblem().constraints.add(compare)
}

def static sum(List list) {
    if (list.isEmpty()) {
        return 0
    } else {
        def value = null;
        for (def element in list) {
            value = value == null ? element : value + element;
        }
        return value
    }
}

def static compare(GElement lhs, String c, GElement rhs) {
    new GCompare(lhs, c, rhs)
}

def static compare(GElement lhs, String c, Number rhs) {
    compare(lhs, c, new GNumber(rhs));
}

// START TO ENCODE YOUR PROBLEM HERE :


def x = var name: 'x', domain: [(1..8), (1..8)], type: boolean

for (i in (1..8)) {
    constraint compare(sum(x[i, _]), '==', 1)
    constraint compare(sum(x[_, i]), '==', 1)
}

for (i in (1..8)) for (j in (1..8)) for (k in (1..8)) for (l in (1..8)) if ((i - j == k - l || i + j == k + l) && (i != k || j != l)) {
    clause ~x[i, j] | ~x[k, l]
}



def solver = GSolverSAT4J.newInstance()
solver.add(GProblem.getDefaultProblem())
solver.solve()

println ' _' * 8
for(def i in (1..8)){
    for(def j in (1..8)) {
        print '' + (solver.getVar(x[i,j]) as boolean?"|X":"|_")
    }
    println '|'
}

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