package io.github.gmpl.main


def oldPlus = Number.metaClass.getMetaMethod("plus", [Number] as Class[])
def oldMultiply = Number.metaClass.getMetaMethod("multiply", [Number] as Class[])

Number.metaClass.plus = { Object n ->
	if(n instanceof GElement){
		new GNumber(delegate).plus(n)
	} else {
		oldPlus.invoke(delegate, n)
	}
}

Number.metaClass.multiply = { Object n ->
	if(n instanceof GElement){
		new GNumber(delegate).multiply(n)
	} else {
		oldMultiply.invoke(delegate, n)
	}
}

def sum(n, closure){
	def sum = new GSum();
	n.collect{ sum += closure(it) }
	sum
}

def or(n, closure){
	def clause = new GClause();
	n.collect{ clause |= closure(it) }
	clause
}

def z = new GVariableDomain("z", boolean, [(1..8), (1..8)])
def v0 = new GVariable("x", boolean);
def v1 = new GVariable("y", boolean);
def v2 = 13.001 * z[1, 3] + 15 * z[1, 5]

println v0
println v1
println v2

for(i in (1..8)) {
	def constraint1 = or (1..8, {j -> z[i, j]})
	def constraint2 = or (1..8, {j -> z[j, i]})
	println("${constraint1}")
	println("${constraint2}")
}

for(i in (1..8)) for(j in (1..8)) for(k in (1..8)) for(l in (1..8)) if((i-j == k-l || i+j == k+l) && (i!=k || j!=l))  {
	def constraint = ~z[i, j] | ~z[k, l]
	println constraint
}
