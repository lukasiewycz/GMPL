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

	def asType(Class type){
		boolean result = false

		for(e in elements){
			result = result | (e as boolean)
		}

		return result.asType(type)
	}
	
}
