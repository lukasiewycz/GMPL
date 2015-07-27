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
	
	Object getAt(i) {
		assert i.size() == domain.size()
		[domain, i].transpose().collect { 
			if(!it[0].contains(it[1]) && it[1] != '_'){
				throw new IllegalArgumentException("Illegal argument for domain ${name}, value ${it[1]} is not in domain ${it[0]}");
			}
		}

		def wildcards = [] as List
		for(def v=0; v<i.size; v++){
			if(i[v] == '_'){
				wildcards << v
			}
		}

		if(wildcards.isEmpty()){
			if(!map.containsKey(i)){
				map[i] = new GVariable(this,"${i}", type)
			}

			return map[i]
		} else {
			def result = [] as List

			for(def v in domain[wildcards[0]]){
				i[wildcards[0]] = v
				result += getAt(i)
			}
			i[wildcards[0]] = '_'

			return result
		}
	}
	
	def retrieveAllDefinedVariables(){
		def list = []
		map.each{ k, v -> list.add(v) }
		list
	}
	
	

}
