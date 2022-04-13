package mathWrappers;
import java.util.ArrayDeque;
import java.util.Arrays;
public class MathParser{

    private ArrayDeque<String> tokenize(String input){
        input = input.replaceAll("\\s", "");
        ArrayDeque<String> tokens;
        final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
        String delimiters = "[+|-|*|/|^|(|)]";
        tokens = new ArrayDeque<String>(Arrays.asList(input.split(String.format(WITH_DELIMITER, delimiters))));
        //System.out.println(tokens.toString());
        //System.out.println("Done!");
        return tokens;
    }

    private MathObject expr(ArrayDeque<String> tokens) {
        MathObject term1 = term(tokens);
        if (safePeek(tokens).equals("+")) {
            tokens.pop();
            MathObject term2 = term(tokens);
            return new Add(term1, term2);
        }
        else if(safePeek(tokens).equals("-")) {
            tokens.pop();
            MathObject term2 = term(tokens);
            return new Sub(term1, term2);
        }
        
        return term1;
    }

    private MathObject term(ArrayDeque<String> tokens) {
        MathObject factor1 = factor(tokens);
        if (safePeek(tokens).equals("*")) {
            tokens.pop();
            MathObject factor2 = factor(tokens);
            return new Mult(factor1, factor2);
        }
        else if (safePeek(tokens).equals("/")) {
            tokens.pop();
            MathObject factor2 = factor(tokens);
            return new Div(factor1, factor2);
        }
        return factor1;
    }

    private MathObject factor(ArrayDeque<String> tokens) {
        if (safePeek(tokens).equals("sin") || safePeek(tokens).equals("cos")
                || safePeek(tokens).equals("tan") || safePeek(tokens).equals("ln")) {
            MathObject funct = func(tokens);
            return funct;
        }


        MathObject base = operand(tokens);
        
        if (safePeek(tokens) != null && safePeek(tokens).equals("^")) {
            tokens.pop();
            MathObject expo = operand(tokens);
            return new Power(base, expo);
        }
        
        return base;
    }

    private MathObject func(ArrayDeque<String> tokens){
        if (safePeek(tokens).toLowerCase().equals("sin")) {
            tokens.pop();
            return new Sin(operand(tokens));
        }
        else if (safePeek(tokens).toLowerCase().equals("cos")) {
            tokens.pop();
            return new Cos(operand(tokens));
        }
        else if (safePeek(tokens).toLowerCase().equals("tan")) {
            tokens.pop();
            return new Tan(operand(tokens));
        }
        else if (safePeek(tokens).toLowerCase().equals("ln")) {
            tokens.pop();
            return new Log(operand(tokens));
        }
        // throw an exception, figure it out later
        System.out.println("Exception should be thrown but im lazy1");
        return null;
    }

    private MathObject operand(ArrayDeque<String> tokens) {
        if (safePeek(tokens).equals("(")) {
            tokens.pop();
            MathObject expr1 = expr(tokens);
            if (safePeek(tokens).equals(")")) {
                tokens.pop();
                return expr1;
            } else {
                // throw an error
                System.out.println("Exception should be thrown but im lazy2");
                return null;
            }
        } else {
            return constant(tokens);
        }
    }

    private MathObject constant(ArrayDeque<String> tokens) {
        int sign = 1;
        if (safePeek(tokens).equals("-")) {
            tokens.pop();
            sign = -1;
        }
        if (safePeek(tokens).toLowerCase().equals("pi")) {
            MathObject pi = new Constant(sign * Math.PI);
            tokens.pop();
            return pi;
        }
        else if (safePeek(tokens).toLowerCase().equals("e")) {
            MathObject e = new Constant(sign * Math.E);
            tokens.pop();
            return e;
        }
        else {
            MathObject next;
            try {
                next = new Constant(sign * Double.parseDouble(safePeek(tokens)));
                tokens.pop();
                return next;
            } catch(Exception e) {
                next = new Variable(safePeek(tokens));
                tokens.pop();
                if (sign == -1) {
                    return new Mult(-1, next);
                }
                return next;
            }
        }
    }

    private String safePeek(ArrayDeque<String> tokens) {
        if (tokens.peekFirst() == null) {
            return "";
        }
        return tokens.peekFirst();
    }

    public MathObject parse(String input) {
        return expr(tokenize(input));
    }

    public static void main(String[] args) {
        MathParser p = new MathParser();
        MathObject result = p.parse("x*(2+3)^y");
        MathObject result2 = p.parse(result.toString());
        System.out.println(result2.toString());
    }
}