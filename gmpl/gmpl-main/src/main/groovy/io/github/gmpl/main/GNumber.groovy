package io.github.gmpl.main



class GNumber extends GElement {

	Number value;

	public GNumber(Number value) {
		super();
		this.value = value;
	}

	String toString() {
		return value.toString()
	}
}

