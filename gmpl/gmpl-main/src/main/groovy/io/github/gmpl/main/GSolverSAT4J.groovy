package io.github.gmpl.main

import org.sat4j.core.Vec
import org.sat4j.core.VecInt
import org.sat4j.pb.ObjectiveFunction
import org.sat4j.pb.PseudoOptDecorator
import org.sat4j.pb.SolverFactory
import org.sat4j.pb.core.PBSolverResolution
import org.sat4j.specs.ContradictionException
import org.sat4j.specs.IVec
import org.sat4j.specs.IVecInt
import org.sat4j.specs.TimeoutException

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class GSolverSAT4J extends GProblem implements GSolver {

	def static newInstance(){
		new GSolverSAT4J();
	}

	protected int nConstraints = 0;


	protected final PBSolverResolution solver;
	protected int timeout = 3600 * 24;
	protected int variableCounter
	protected Map<GVariable,Integer> variablesEnc = new HashMap<>();
	protected Map<Integer,GVariable> variablesDec = new HashMap<>();

	public GSolverSAT4J() {
		solver = SolverFactory.newPBResMixedConstraintsObjective();
		solver.newVar(100);
	}



	def solve() {
		solver.setTimeout(timeout);
		println 'Number of constraints '+nConstraints

		boolean feasible = false;

		PseudoOptDecorator decorator = new PseudoOptDecorator(solver);
		decorator.newVar(varNum.get());
		try {
			if (decorator.hasNoObjectiveFunction()) {
				feasible = decorator.isSatisfiable();
			} else {
				while (decorator.admitABetterSolution()) {
					feasible = true;
					BigInteger rhs = toBigInteger(decorator.getObjectiveValue().intValue() - 1);
					ObjectiveFunction lhs = decorator.getObjectiveFunction();

					decorator.addPseudoBoolean(lhs.getVars(), lhs.getCoeffs(), false, rhs);
				}
			}

		} catch (ContradictionException e) {
		} catch (TimeoutException e) {
		}

		if (!feasible) {
			return false
		} else {
			for(Map.Entry<GVariable,Integer> entry in variablesEnc.entrySet()) {
				def value = decorator.model(entry.getValue()) ? 1 : 0
				entry.getKey().setValue(value)
			}
			return true
		}
	}

	def addConstraint(GCompare compare){
		updateVariables(compare)
		nConstraints++

		try {
			GLinear linear = GLinear.convert(compare.getLhs())
			linear.addAll(GLinear.convert(compare.getRhs()).invert())

			int constant = 0
			Map<GVariable,Integer> map = new HashMap<>()

			for(GLinear.GLinearTerm term in linear){
				if(term.isConstant()) {
					constant += term.getCoefficient()
				} else {
					GVariable variable = term.getLiteral().getVariable()
					if(!map.containsKey(variable)){
						map.put(variable,0);
					}
					if(term.getLiteral().isPositive()){
						map.put(variable, map.get(variable)+term.getCoefficient())
					} else {
						constant += term.getCoefficient()
						map.put(variable, map.get(variable)-term.getCoefficient())
					}
				}
			}

			//println ''+map+' '+constant

			PBExpr pbexpr = new PBExpr();
			for(Map.Entry<GVariable,Integer> entry in map){
				pbexpr.literals.push(variablesEnc.get(entry.getKey()))
				pbexpr.coeffs.push(new BigInteger(entry.getValue()))
			}

			def op = compare.getC()
			if (op == GCompare.Comparator.EQUAL || op == GCompare.Comparator.GREATEREQUAL) {
				solver.addPseudoBoolean(pbexpr.literals, pbexpr.coeffs, true, -constant)
			}
			if (op == GCompare.Comparator.EQUAL || op == GCompare.Comparator.LESSEQUAL ) {
				solver.addPseudoBoolean(pbexpr.literals, pbexpr.coeffs, false, -constant)
			}

		} catch(IllegalArgumentException e){
			throw new IllegalArgumentException('Constraint '+compare+' is not linear', e)
		}
	}



	def addClause(GClause clause){
		updateVariables(clause)
		nConstraints++

		IVecInt cl = new VecInt();

		for(GElement term in ((GClause)clause)){
			switch(term) {
				case {it instanceof GVariable}:
					GVariable variable = (GVariable) term
					cl.push(variablesEnc.get(term))
					break;
				case {it instanceof GLiteral}:
					GLiteral literal = (GLiteral) term
					cl.push((literal.getSign() ? 1 : -1) * variablesEnc.get(literal.getVariable()))
					break;
				default:
					throw new IllegalArgumentException()
			}
		}
		solver.addClause(cl);
	}

	def add(GProblem problem){
		for(GElement element in problem.getConstraints()){
			if(element instanceof GCompare){
				addConstraint((GCompare)element)
			} else if(element instanceof GClause){
				addClause((GClause)element)
			}
		}
	}

	protected void updateVariables(GElement element){
		for(GVariable variable in GProblem.getVariables(element)){
			if(!variablesEnc.containsKey(variable)) {
				int intVar = translate(variable)
				variablesEnc.put(variable,intVar)
				variablesDec.put(intVar,variable)
			}
		}
	}

	protected AtomicInteger var = new AtomicInteger(1);
	protected AtomicInteger varNum = new AtomicInteger(100);

	protected Integer translate(GVariable variable) {
		int i = var.getAndIncrement();
		if (i > varNum.get()) {
			varNum.addAndGet(100);
			solver.newVar(100);
		}
		return i;
	}

	protected AtomicInteger con = new AtomicInteger(1);

	protected class PBExpr {
		IVecInt literals = new VecInt();
		IVec<BigInteger> coeffs = new Vec<BigInteger>();
	}

	protected BigInteger toBigInteger(int value) {
		BigInteger bi = new BigInteger("" + value);
		return bi;
	}

	public GVariable getVar(GVariable var0){
		for(GVariable var1 in variablesEnc.keySet()){
			if(var1 == var0){
				return var1
			}
		}
	}

}
