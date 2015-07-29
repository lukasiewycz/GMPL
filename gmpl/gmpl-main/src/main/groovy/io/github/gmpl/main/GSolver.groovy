package io.github.gmpl.main

interface GSolver {
	def solve()
	def addClause(GClause clause)
	def addConstraint(GCompare compare)
}
