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

import java.util.concurrent.atomic.AtomicInteger

class GSolverSAT4J extends GProblem implements GSolver {
	
	def static newInstance(){
		new GSolverSAT4J();
	}
	

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

		boolean feasible = false;

		for(GVariable variable in getVariables()){
			int intVar = translate(variable);
			variablesEnc.put(variable,intVar);
			variablesDec.put(intVar,variable);
		}

		println 'size of variables '+variablesEnc.size()

		for(GElement element in getConstraints()){
			if(element instanceof GCompare){
				GCompare compare = (GCompare)element;

				PBExpr lhs = new PBExpr();

				GSum sum = (GSum)compare.getLhs();
				for(GElement term in sum){
					switch(term){
						case {it instanceof GVariable}:
							GVariable variable = (GVariable)term
							lhs.coeffs.push(toBigInteger(1))
							lhs.literals.push(variablesEnc.get(term))
							break;
						case {it instanceof GLiteral}:
							GLiteral literal = (GLiteral)term
							lhs.coeffs.push(toBigInteger(1))
							lhs.literals.push((literal.getSign()?1:-1) * variablesEnc.get(literal.getVariable()))
							break;
						case {it instanceof GProduct}:
							throw new IllegalArgumentException();
						default:
							throw new IllegalArgumentException();
					}
				}

				def op = compare.getC()
				if (op == GCompare.Comparator.EQUAL || op == GCompare.Comparator.GREATEREQUAL) {
					solver.addPseudoBoolean(lhs.literals, lhs.coeffs, true, 1)
				}
				if (op == GCompare.Comparator.EQUAL || op == GCompare.Comparator.LESSEQUAL ) {
					solver.addPseudoBoolean(lhs.literals, lhs.coeffs, false, 1)
				}

			} else if(element instanceof GClause){
				IVecInt cl = new VecInt();


				for(GElement term in ((GClause)element)){
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
		}




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
			return null;
		} else {
			for(Map.Entry<GVariable,Integer> entry in variablesEnc.entrySet()) {
				def value = decorator.model(entry.getValue()) ? 1 : 0
				entry.getKey().setValue(value)
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
