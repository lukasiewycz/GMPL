package io.github.gmpl.main

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(excludes=["map"])
class GVariableDomain {
	
	static {
		GCommon.init()
	}
	
	def name
	def domain
	def type
	Map<List,GVariable> map = new HashMap<>()
	
	
	public GVariableDomain(String name, Class type, List domain) {
		super()
		this.name = name
		this.type = type
		this.domain = domain
	}

	Object getAt(String str){
		getAtInternal(str)
	}
	
	Object getAt(i) {
		getAtInternal(i)
	}

	private getAtInternal(i){
		if(!(i instanceof Collection)){
			List l = new ArrayList()
			l.add(i)
			i = l
		}

		assert i.size() == domain.size()
		List key = new ArrayList(i)

		[domain, i].transpose().collect {
			if(!it[0].contains(it[1]) && it[1] != '_'){
				throw new IllegalArgumentException("Illegal argument for domain ${name} with input $i, value ${it[1]} is not in domain ${it[0]}")
			}
		}



		def wildcards = [] as List
		for(def v=0; v<i.size; v++){
			if(i[v] == '_'){
				wildcards << v
			}
		}

		if(wildcards.isEmpty()){
			if(!map.containsKey(key)){
				map[key] = new GVariable(this,"${i}", type)
			}

			return map[key]
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
