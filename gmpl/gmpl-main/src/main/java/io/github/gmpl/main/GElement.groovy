package io.github.gmpl.main

class GElement {
	
	GElement plus(GElement other) {
		//println "plus $this $other"
		new GSum(this, other);
	}
	
	GElement plus(Number other) {
		//println "plus $this $other"
		new GSum(this, new GNumber(other));
	}

	GElement minus(GElement other) {
		//println "sum $this $other"
		new GSum(this, new GProduct(new GNumber(-1), other));
	}
		
	GElement minus(Number other) {
		//println "sum $this $other"
		plus(-1 * other)
	}

	GElement multiply(GElement other){
		//println "multiply $this $other"
		new GProduct(this, other);
	}
	
	GElement multiply(Number other){
		//println "multiply $this $other"
		new GProduct(this, new GNumber(other));
	}
	
	GElement or(GElement other){
		new GClause(this, other);
	}
	
}
