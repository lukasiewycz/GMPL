package io.github.gmpl.main

class GDefaultFunctions {
    static String _ = '_'

    static String SAT4J = 'SAT4J'

    protected static GSolver solver = null

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
            solver = new GSolverSAT4J()
            GProblem problem = GProblem.getDefaultProblem()
            solver.add(problem)
        }
    }

    static def solve(){
        solver.solve()
    }

    static def clause(GClause clause){
        GProblem.getDefaultProblem().constraints.add(clause)
    }

    static def clause(GElement clause){
        GClause cl = new GClause()
        cl.or(clause)
        GProblem.getDefaultProblem().constraints.add(cl)
    }

    static def constraint(GCompare compare){
        GProblem.getDefaultProblem().constraints.add(compare)
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

    def static disjunction(List list) {
        if (list.isEmpty()) {
            return false
        } else {
            def value = null;
            for (def element in list) {
                value = value == null ? element : value | element;
            }
            return value
        }
    }

    def static compare(GElement lhs, String c, GElement rhs) {
        new GCompare(lhs, c, rhs)
    }

    def static compare(GElement lhs, String c, Number rhs) {
        compare(lhs, c, new GNumber(rhs));
    }
}
