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

public class ComparisonEvaluator extends AbstractEvaluator<String> {

    private final static Operator LESS_OR_EQUAL = new Operator("<=", 2, Operator.Associativity.LEFT, 2);
    private final static Operator GREATER_OR_EQUAL = new Operator(">=", 2, Operator.Associativity.LEFT, 2);

    private final static Operator LESS = new Operator("<", 2, Operator.Associativity.LEFT, 1);
    private final static Operator GREATER = new Operator(">", 2, Operator.Associativity.LEFT, 1);

    private static final Parameters PARAMETERS;

    private final Map<String, Integer> numberVariableMap;

    static {
        // Create the evaluator's parameters
        PARAMETERS = new Parameters();
        // Add the supported operators
        PARAMETERS.add(GREATER);
        PARAMETERS.add(LESS);
        PARAMETERS.add(LESS_OR_EQUAL);
        PARAMETERS.add(GREATER_OR_EQUAL);
        // Add the parentheses
        PARAMETERS.addExpressionBracket(BracketPair.PARENTHESES);
    }

    public ComparisonEvaluator(final Map<String, Integer> numberVariableMap) {
        super(PARAMETERS);
        this.numberVariableMap = new HashMap<>(numberVariableMap);
    }

    @Override
    protected String toValue(final String literal, final Object evaluationContext) {
        return literal;
    }

    private Integer getValue(final String literal) {
        if (numberVariableMap.containsKey(literal)) {
            return numberVariableMap.get(literal);
        }

        try {
            return Integer.parseInt(literal);
        } catch (final NumberFormatException e) {
            throw new RuntimeException("OMG", e);
        }
    }

    @Override
    protected String evaluate(final Operator operator, final Iterator<String> operands, final Object evaluationContext) {
        final List<String> tree = (List<String>) evaluationContext;

        final String o1 = operands.next();
        final String o2 = operands.next();

        final String operatorSymbol = operator.getSymbol().toUpperCase();
        final Boolean result;
        if (LESS.getSymbol().equals(operatorSymbol)) {
            result = getValue(o1) < getValue(o2);
        } else if (GREATER.getSymbol().equals(operatorSymbol)) {
            result = getValue(o1) > getValue(o2);
        } else if (GREATER_OR_EQUAL.getSymbol().equals(operatorSymbol)) {
            result = getValue(o1) >= getValue(o2);
        } else if (LESS_OR_EQUAL.getSymbol().equals(operatorSymbol)) {
            result = getValue(o1) <= getValue(o2);
        } else {
            throw new IllegalArgumentException("Unknown operator" + operator);
        }
        final String eval = "(" + o1 + " " + operator.getSymbol() + " " + o2 + ")=" + result;
        tree.add(eval);
        return eval;
    }

    public static void main(final String[] args) {
        final Map<String, Integer> argumentMap = new HashMap<>();
        argumentMap.put("HELLO", 1);
        argumentMap.put("WLD", 1);

        ComparisonEvaluator evaluator = new ComparisonEvaluator(argumentMap);
        doIt(evaluator, "(HELLO > WLD)");
        doIt(evaluator, "(HELLO < WLD)");
        doIt(evaluator, "(HELLO >= WLD)");
        doIt(evaluator, "(HELLO <= WLD)");

    }

    private static void doIt(final ComparisonEvaluator evaluator, final String expression) {
        List<String> sequence = new ArrayList<String>();
        evaluator.evaluate(expression, sequence);
        System.out.println("Evaluation sequence for: " + expression);
        for (String string : sequence) {
            System.out.println(string);
        }
        System.out.println();
    }
}
