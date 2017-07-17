package services.utils;

import com.fathzer.soft.javaluator.AbstractEvaluator;
import com.fathzer.soft.javaluator.BracketPair;
import com.fathzer.soft.javaluator.Operator;
import com.fathzer.soft.javaluator.Parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SimpleBooleanEvaluator extends AbstractEvaluator<String> {
    /**
     * The logical AND operator.
     */
    private final static Operator AND = new Operator("AND", 2, Operator.Associativity.LEFT, 2);
    /**
     * The logical OR operator.
     */
    private final static Operator OR = new Operator("OR", 2, Operator.Associativity.LEFT, 1);

    private static final Parameters PARAMETERS;

    private final Map<String, Boolean> booleanVariableMap;

    static {
        // Create the evaluator's parameters
        PARAMETERS = new Parameters();
        // Add the supported operators
        PARAMETERS.add(AND);
        PARAMETERS.add(OR);
        // Add the parentheses
        PARAMETERS.addExpressionBracket(BracketPair.PARENTHESES);
    }

    public SimpleBooleanEvaluator(final Map<String, Boolean> booleanVariableMap) {
        super(PARAMETERS);
        this.booleanVariableMap = new HashMap<>(booleanVariableMap);
    }

    @Override
    protected String toValue(final String literal, final Object evaluationContext) {
        return literal;
    }

    private boolean getValue(final String literal) {
        if (booleanVariableMap.containsKey(literal)) {
            return booleanVariableMap.get(literal);
        }

        if ("T".equals(literal) || literal.endsWith("=true")) {
            return true;
        } else if ("F".equals(literal) || literal.endsWith("=false")) {
            return false;
        }
        return Boolean.parseBoolean(literal);
    }

    @Override
    protected String evaluate(final Operator operator, final Iterator<String> operands, final Object evaluationContext) {
        final List<String> tree = (List<String>) evaluationContext;

        final String o1 = operands.next();
        final String o2 = operands.next();

        final String operatorSymbol = operator.getSymbol().toUpperCase();
        final Boolean result;
        if (OR.getSymbol().equals(operatorSymbol)) {
            result = getValue(o1) || getValue(o2);
        } else if (AND.getSymbol().equals(operatorSymbol)) {
            result = getValue(o1) && getValue(o2);
        } else {
            throw new IllegalArgumentException("Unknown operator" + operator);
        }
        final String eval = "(" + o1 + " " + operator.getSymbol() + " " + o2 + ")=" + result;
        tree.add(eval);
        return eval;
    }

    public static void main(final String[] args) {
        final Map<String, Boolean> argumentMap = new HashMap<>();
        argumentMap.put("HELLO", true);
        argumentMap.put("WLD", false);

        SimpleBooleanEvaluator evaluator = new SimpleBooleanEvaluator(argumentMap);
        doIt(evaluator, "T AND ( F OR ( F AND T ) )");
        doIt(evaluator, "(T AND T) OR ( F AND T )");
        doIt(evaluator, "(HELLO OR WLD)");
    }

    private static void doIt(final SimpleBooleanEvaluator evaluator, final String expression) {
        List<String> sequence = new ArrayList<String>();
        evaluator.evaluate(expression, sequence);
        System.out.println("Evaluation sequence for: " + expression);
        for (String string : sequence) {
            System.out.println(string);
        }
        System.out.println();
    }
}
