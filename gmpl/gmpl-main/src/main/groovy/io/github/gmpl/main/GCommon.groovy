package io.github.gmpl.main

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
		}
	}


}
