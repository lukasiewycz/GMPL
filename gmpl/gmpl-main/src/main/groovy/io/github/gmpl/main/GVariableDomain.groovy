package io.github.gmpl.main

class GVariableDomain {
	
	static {
		GCommon.init()
	}
	
	def name;
	def type;
	def domain;
	def map = [:]
	
	
	public GVariableDomain(String name, Class type, List domain) {
		super();
		this.name = name;
		this.type = type;
		this.domain = domain;
	}
	
	GVariable getAt(i) {
		assert i.size() == domain.size()
		[domain, i].transpose().collect { 
			if(!it[0].contains(it[1])){
				throw new IllegalArgumentException("Illegal argument for domain ${name}, value ${it[1]} is not in domain ${it[0]}");
			}
		}
		
		if(!map.containsKey(i)){
			map[i] = new GVariable(this,"${i}", type)
		}
		
		return map[i]
	}
	
	def retrieveAllDefinedVariables(){
		def list = []
		map.each{ k, v -> list.add(v) }
		list
	}
	
	

}
