package io.github.gmpl.main

class GDefaultFunctions {
    static String _ = '_'

    static GSolverName SAT4J = GSolverName.SAT4J

    static enum GSolverName {
        SAT4J
    }

    protected static GProblem problem = new GProblem()
    protected static GSolver solver = null

    static Number infinity = Double.MAX_VALUE

    static def var(Map map){
        String name = map['name']
        Class type = map['type']
        List domain = map['domain']

        if(domain == null){
            new GVariable(name, type);
        } else {
            new GVariableDomain(name, type, domain);
        }
    }

    static def using(String type){
        if(type == SAT4J){
            solver = new GSolverSAT4J();
            solver.add(problem)
        }
    }

    static def solver(GSolverName name){
        if(name == GSolverName.SAT4J){
            solver = new GSolverSAT4J();
            solver.add(problem)
        }
    }

    static def solve(){
        solver.solve()
    }

    static def clause(GClause clause){
        if(solver == null){
            problem.addClause(clause)
        } else {
            solver.addClause(clause)
        }

    }

    static def clause(GElement clause){
        GClause cl = new GClause()
        cl.or(clause)
        GDefaultFunctions.clause(cl)
    }

    static def constraint(GCompare compare){
        if(solver == null){
            problem.addConstraint(compare)
        } else {
            solver.addConstraint(compare)
        }
    }

    def static sum(List list) {
        if (list.isEmpty()) {
            return 0
        } else {
            def value = null;
            for (def element in list) {
                value = value == null ? element : value + element;
            }
            return value
        }
    }

    def static disjunction(Iterable list) {
        def value = false
        for (def element in list) {
            value = value == false ? element : value | element
        }
        return value
    }

    def static disjunction(GElement[] list){
        return disjunction(Arrays.asList(list))
    }

    def static compare(GElement lhs, String c, GElement rhs) {
        new GCompare(lhs, c, rhs)
    }

    def static compare(GElement lhs, String c, Number rhs) {
        compare(lhs, c, new GNumber(rhs));
    }
}
