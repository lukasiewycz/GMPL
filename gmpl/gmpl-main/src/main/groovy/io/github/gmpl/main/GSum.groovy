package io.github.gmpl.main

import java.util.Iterator;

class GSum extends GElement implements Iterable {
	
	def elements = []
	
	GSum(GElement... elements){
		this.elements.addAll(elements.collect())
	}

	Iterator iterator() {
		elements.iterator()
	}
	
	String toString(){
		'('+elements.join(' + ')+')'
	}
	
	GElement plus(GElement other) {
		elements.add(other)
		this
	}

	GElement minus(GElement other) {
		elements.add(other*-1)
		this
	}
	
	GElement plus(Number other){
		elements += new GNumber(other);
		this
	}

}
