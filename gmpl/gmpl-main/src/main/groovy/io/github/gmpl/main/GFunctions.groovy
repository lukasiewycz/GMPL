package io.github.gmpl.main

trait GFunctions {

    static def sum(Map map, Closure closure) {
        def list = []
        map.each { k, v -> list.add(v) }
        def combinations = GroovyCollections.combinations(list)

        def sum = new GSum();
        for (e in combinations) {
            def value = (e instanceof List && e.size() == 1) ? e[0] : e
            sum += closure.call(value)
        }
        sum
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


    def static compare(GElement lhs, String c, GElement rhs) {
        new GCompare(lhs, c, rhs)
    }

    def static compare(GElement lhs, String c, Number rhs) {
        compare(lhs, c, new GNumber(rhs));
    }

    static def sum(n, closure) {
        def sum = new GSum();
        n.collect { sum += closure(it) }
        sum
    }

    static def or(n, closure) {
        def clause = new GClause();
        n.collect { clause |= closure(it) }
        clause
    }

    static def or(Map map, Closure closure) {
        def list = []
        map.each { k, v -> list.add(v) }
        def combinations = GroovyCollections.combinations(list)

        def clause = new GClause();
        for (e in combinations) {
            def value = (e instanceof List && e.size() == 1) ? e[0] : e
            clause |= closure.call(value)
        }
        clause
    }


}
