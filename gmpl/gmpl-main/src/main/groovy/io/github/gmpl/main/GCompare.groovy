package io.github.gmpl.main

class GCompare extends GElement {

	enum Comparator {
		LESS('<'),LESSEQUAL('<='),EQUAL('=='),GREATER('>'),GREATEREQUAL('>=');
	
		def final name
		def static final map
	
		static {
			map = [:] as TreeMap
			values().each{ comparator ->
				map.put(comparator.name, comparator)
			}
		}
		
		Comparator(String s) {
			name = s;
		}

		String toString(){
		   name;
		}
		
		static get( comparator ) {
			map[comparator]
		}
	}

	GElement lhs;
	Comparator c;
	GElement rhs;

	def GCompare(GElement lhs, String c, GElement rhs) {
		super();
		this.lhs = lhs;		
		this.c = Comparator.get(c);
		this.rhs = rhs;
	}

	String toString() {
		lhs.toString() + c.toString() + rhs.toString()
	}
}
