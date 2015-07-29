package io.github.gmpl.main

import org.codehaus.groovy.runtime.DefaultGroovyMethods

class GCommon {

	static def isinit = false;

	static init(){
		if(!isinit){
			isinit = true
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

			boolean.metaClass.'static'.getAt = { Object i ->
				println i
				println i.getClass()
			}

			def defArrayListAsType = ArrayList.metaClass.getMetaMethod("asType", [Class] as Class[])
			ArrayList.metaClass.asType = { Class c ->
				if (c == GLiteral[]) {
					if(delegate instanceof GLiteral){
						return [delegate]
					} else if(delegate instanceof GVariable){
						return [delegate as GLiteral]
					} else if(delegate instanceof List){
						def list = []
						for(Object element in (List)delegate){
							if(element instanceof GLiteral){
								list << element
							} else if(element instanceof GVariable){
								GLiteral literal = ((GVariable)element) as GLiteral;
								list << literal
							} else {
								throw IllegalArgumentException()
							}
						}
						GLiteral[] array = list.toArray(new GLiteral[list.size()])
						array
					} else {
						throw IllegalArgumentException()
					}
				} else {
					defArrayListAsType.invoke(delegate, c)
				}
			}

			GLiteral[].metaClass.bitwiseNegate = {
				GLiteral[] array = (GLiteral[])delegate
				for(def i=0; i<array.size();i++){
					array[i] = ~array[i]
				}
				return array
			}

		}
	}


}
