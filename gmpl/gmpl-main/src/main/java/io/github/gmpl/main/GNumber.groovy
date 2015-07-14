package io.github.gmpl.main



class GNumber extends GElement {

	Number value;

	static {
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
	}

	public GNumber(Number value) {
		super();
		this.value = value;
	}

	String toString() {
		return value.toString()
	}
}

