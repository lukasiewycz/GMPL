package io.github.gmpl.main

import static io.github.gmpl.main.GDefaultFunctions.*

int n = 8

def x = var name: 'x', domain: [(1..n), (1..n)], type: boolean

for (i in (1..n)) {
    //constraint compare(sum(x[i, _]), '==', 1)
    //constraint compare(sum(x[_, i]), '==', 1)

    clause disjunction(x[i, _])
    clause disjunction(x[_, i])
    for (j in (1..n)) for (k in (1..n)) if(j != k) {
        clause ~x[i, j] | ~x[i, k]
        clause ~x[j, i] | ~x[k, i]
    }

}

for (i in (1..n)) for (j in (1..n)) for (k in (1..n)) for (l in (1..n)) if ((i - j == k - l || i + j == k + l) && (i != k || j != l)) {
    clause ~x[i, j] | ~x[k, l]
}

using SAT4J
solve()

println ' _' * n
for(i in (1..n)){
    for(j in (1..n)) print '|' + (x[i,j] as boolean?"X":"_"); println '|'
}