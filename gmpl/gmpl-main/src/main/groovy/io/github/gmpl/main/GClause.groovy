package io.github.gmpl.main

import java.util.Iterator;

class GClause extends GElement implements Iterable {
	
	def elements = []
	
	GClause(GElement... elements){
		this.elements.addAll(elements.collect())
	}

	Iterator iterator() {
		elements.iterator()
	}
	
	String toString(){
		'('+elements.join(' | ')+')'
	}
	
	GClause or(GElement other){
		elements.add(other)
		this
	}
	
}
