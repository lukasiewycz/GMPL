package io.github.gmpl.main


class GLinear implements Iterable<GLinearTerm> {


    public static class GLinearTerm {
        Number coefficient;
        GLiteral literal;

        public GLinearTerm(Number coefficient, GLiteral literal){
            this.coefficient = coefficient
            this.literal = literal
        }

        public boolean isConstant(){
            return literal == null
        }

        @Override
        public String toString() {
            return '{' +  coefficient + '*' + literal + '}';
        }
    }

    protected List<GLinearTerm> terms = new ArrayList<>();

    public void add(GLinearTerm term){
        terms.add(term)
    }

    public void addAll(GLinear linear){
        this.terms.addAll(linear.terms)
    }

    Iterator<GLinearTerm> iterator() {
        return terms.iterator()
    }

    public static GLinear convert(GElement element){
        GLinear linear = new GLinear();
        switch(element){
            case {it instanceof GNumber}:
            case {it instanceof GVariable}:
            case {it instanceof GLiteral}:
            case {it instanceof GProduct}:
                linear.add(convertToLinearTerm(element))
                break;
            case {it instanceof GSum}:
                GSum sum = (GSum)element
                for(GElement summand in sum){
                    linear.add(convertToLinearTerm(summand))
                }
                break;
            default:
                throw new IllegalArgumentException("Element "+element+" cannot be converted to linear term (no rule for type "+element.getClass()+')')
        }
        return linear
    }

    protected static GLinearTerm convertToLinearTerm(GElement element){
        switch(element){
            case {it instanceof GVariable}:
                return new GLinearTerm(1, ((GVariable)element).toPositiveLiteral())
            case {it instanceof GLiteral}:
                return new GLinearTerm(1, (GLiteral)element)
            case {it instanceof GNumber}:
                return new GLinearTerm(((GNumber)element).getValue(), null);
            case {it instanceof GProduct}:
                GProduct product = (GProduct)element
                Double coefficient = 1.0
                GLiteral literal = null

                for(GElement factor in product){
                    if(factor instanceof GNumber){
                        coefficient *= ((GNumber)factor).getValue();
                    } else if(factor instanceof GVariable || factor instanceof GLiteral){
                        if(literal != null){
                            throw new IllegalArgumentException("Element "+element+" is not linear for term "+product);
                        } else if(factor instanceof GVariable){
                            literal = ((GVariable)factor).toPositiveLiteral();
                        } else {
                            literal = (GLiteral)factor;
                        }
                    }
                }
                return new GLinearTerm(isInteger(coefficient)?coefficient.intValue():coefficient, literal);
        }
    }

    protected static isInteger(double value){
        return ((value == Math.floor(value)) && !Double.isInfinite(value))
    }

    @Override
    public String toString() {
        return terms;
    }

    public GLinear invert(){
        for(GLinearTerm term in terms){
            term.setCoefficient(-term.getCoefficient())
        }
        return this
    }

}
