package io.github.gmpl.main.test

import io.github.gmpl.main.GProblem


class QueensProblem extends GProblem {
	
	static int n
	
	static QueensProblem newInstance(int n){
		this.n = n
		return new QueensProblem()
	}
	
	def x = var name: 'x', type: boolean, domain: [(1..n),(1..n)]

	{
		for(i in (1..n)) {
			constraint compare(sum(j: 1..n, {j -> x[i, j]}),'==',1)
			constraint compare(sum(j: 1..n, {j -> x[j, i]}),'==',1)
		}
		
		for(i in (1..n)) for(j in (1..n)) for(k in (1..n)) for(l in (1..n)) if((i-j == k-l || i+j == k+l) && (i!=k || j!=l))  {
			clause ~x[i, j] | ~x[k, l]
		}
	}

}
