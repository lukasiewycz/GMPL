package io.github.gmpl.main

import java.util.Iterator;

class GProduct extends GElement implements Iterable {
	
	def elements = []
	
	GProduct(GElement... elements){
		this.elements.addAll(elements.collect())
	}

	Iterator iterator() {
		elements.iterator()
	}
	
	String toString(){
		'('+elements.join(' * ')+')'
	}
	
	GElement multiply(GElement other){
		elements += other
		this
	}
	
	GElement multiply(Number other){
		elements += new GNumber(other);
		this
	}


}
