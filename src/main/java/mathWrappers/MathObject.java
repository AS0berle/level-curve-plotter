package mathWrappers;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.HashMap;



public abstract class MathObject {

	public abstract double evaluate(EvalVar vals[]);

	public abstract MathObject derivative(String varName);

	public abstract String toString();

	public Vector gradient(Variable[] vars) {
		MathObject components[] = new MathObject[vars.length];
		for (int i = 0; i < components.length; i++) {
			components[i] = this.derivative(vars[i].toString());
		}

		return new Vector(components);
	}

	/**
	 * 
	 * parseString only supports equations that contain the following symbols '+', '-', '*', '/', '^'
	 * Any isolated occurence of 'e' is assumed to mean Euler's number
	 * Any isolated occurence of 'pi' (case insensitive) is assumed to mean 3.1415...
	 * All operations must be enclosed in parenthesis:
	 * 		"x+y" will fail, while "(x+y)" will succeed
	 * 
	 * @param eqStr A mathematical expression written as a string, like 'x+y'
	 * @return A MathObject that represents eqStr
	 */
	public static MathObject parseString(String eqStr) {

		char[] OPS = {'+', '-', '*', '/', '^'};
		
		ArrayDeque<Pair<Integer, Integer>> parensPair = getParenPairs(eqStr);
		
		HashMap<Integer, MathObject> subObjs = new HashMap<Integer, MathObject>();

		Iterator<Pair<Integer, Integer>> it = parensPair.iterator();
		while(it.hasNext()) {
			Pair<Integer, Integer> parens = it.next();
			
			String expr = eqStr.substring((int)parens.getFirst() + 1, (int)parens.getSecond());
			int opIndex = findBivariateOp(expr) + (int)parens.getFirst() + 1;
			
			int key = cantorPair(parens.getFirst(), parens.getSecond());
			// Addition
			if(eqStr.charAt(opIndex) == OPS[0]) {
				
				Pair<MathObject, MathObject> objs = findMathObj(eqStr, opIndex, subObjs);
				subObjs.put(key, new Add(objs.getFirst(), objs.getSecond()));

			// Subtraction
			} else if (eqStr.charAt(opIndex) == OPS[1]) {

				Pair<MathObject, MathObject> objs = findMathObj(eqStr, opIndex, subObjs);
				subObjs.put(key, new Sub(objs.getFirst(), objs.getSecond()));
			
			// Multiplication
			} else if (eqStr.charAt(opIndex) == OPS[2]) {

				Pair<MathObject, MathObject> objs = findMathObj(eqStr, opIndex, subObjs);
				subObjs.put(key, new Mult(objs.getFirst(), objs.getSecond()));
				
			// Division
			} else if (eqStr.charAt(opIndex) == OPS[3]) {
				
				Pair<MathObject, MathObject> objs = findMathObj(eqStr, opIndex, subObjs);
				subObjs.put(key, new Div(objs.getFirst(), objs.getSecond()));

			// Exponentiation
			}else if (eqStr.charAt(opIndex) == OPS[4]) {
				
				Pair<MathObject, MathObject> objs = findMathObj(eqStr, opIndex, subObjs);
				subObjs.put(key, new Power(objs.getFirst(), objs.getSecond()));

			}

		}
		
		return subObjs.get(cantorPair(0, eqStr.length() - 1));
	}

	private static Pair<MathObject, MathObject> findMathObj(String eqStr, int opIndex, HashMap<Integer, MathObject> kObjects) {
		// Check to the left
		int move = 0;
		char val = eqStr.charAt(opIndex);
		MathObject left = null;
		while(!(Character.isLetterOrDigit(val) || val == '(' ||val == ')' )) {
			move -= 1;
			val = eqStr.charAt(opIndex + move);

			if (Character.isLetter(val)) {
				if (val == 'e') {
					left = new Constant(Math.E);
				} else {
					String letters = "";
					char endStr = val;
					int move2 = move;
					while(Character.isLetter(endStr)) {
						letters = Character.toString(endStr) + letters;
						move2 -= 1;
						endStr = eqStr.charAt(opIndex + move2);
					}

					if (letters.toUpperCase().equals("PI")) {
						left = new Constant(Math.PI);
					} else {
						left = new Variable(letters);
					}
				}
			} else if (Character.isDigit(val) || val == '.') {

				String digits = "";
				char endStr = val;
				int move2 = move;
				while(Character.isDigit(endStr) || endStr == '.') {
					digits = Character.toString(endStr) + digits;
					move2 -= 1;
					endStr = eqStr.charAt(opIndex + move2);
				}

				left = new Constant(Double.parseDouble(digits));
			} else if (val == '(' || val == ')') {
				for (int key : kObjects.keySet()) {
					Pair<Integer, Integer> p = inverseCantor(key);
					if (p.getFirst() == opIndex + move || p.getSecond() == opIndex + move)
						left = kObjects.get(key);
				}

			}

		}

		// Check to the right
		move = 0;
		val = eqStr.charAt(opIndex);
		MathObject right = null;;
		while(!(Character.isLetterOrDigit(val) || val == '(' ||val == ')' )) {
			move += 1;
			val = eqStr.charAt(opIndex + move);


			if (Character.isLetter(val)) {
				if (val == 'e') {
					right = new Constant(Math.E);
				} else {
					String letters = "";
					char endStr = val;
					int move2 = move;
					while(Character.isLetter(endStr)) {
						letters = letters + Character.toString(endStr);
						move2 += 1;
						endStr = eqStr.charAt(opIndex + move2);
					}

					if (letters.toUpperCase().equals("PI")) {
						right = new Constant(Math.PI);
					} else {
						// Handle parsing for trig/log like this chain, just checking what 'letters' spells out
						right = new Variable(letters);
					}
				}
			} else if (Character.isDigit(val) || val == '.') {

				String digits = "";
				char endStr = val;
				int move2 = move;
				while(Character.isDigit(endStr) || endStr == '.') {
					digits = digits + Character.toString(endStr);
					move2 += 1;
					endStr = eqStr.charAt(opIndex + move2);
				}

				right = new Constant(Double.parseDouble(digits));
			} else if (val == '(' || val == ')') {
				for (int key : kObjects.keySet()) {
					Pair<Integer, Integer> p = inverseCantor(key);
					if (p.getFirst() == opIndex + move || p.getSecond() == opIndex + move)
						right = kObjects.get(key);
				}

			}

		}

		//System.out.println(new Pair(left, right));
		return new Pair<MathObject, MathObject>(left, right);
	}

	private static ArrayDeque<Pair<Integer, Integer>> getParenPairs(String eqStr) {
		char eqSeq[] = eqStr.toCharArray();

		ArrayDeque<Integer> openParens = new ArrayDeque<Integer>();
		ArrayDeque<Pair<Integer, Integer>> parensPair = new ArrayDeque<Pair<Integer, Integer>>();
		for (int i = 0; i < eqSeq.length; i++) {
			if (eqSeq[i] == '(')
				// leftParens should be read last in, first out
				openParens.add(i);
			else if (eqSeq[i] == ')') {
				parensPair.add(new Pair<Integer, Integer>(openParens.getLast(), i));
				openParens.removeLast();
			}
		}

		return parensPair;
	}

	private static int findBivariateOp(String expression) {
		int openCount = 0;
		int startSearchIndex = 0;
		char[] OPS = {'+', '-', '*', '/', '^'};
		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) == '('){
				openCount += 1;
			}
			else if(expression.charAt(i) == ')') {
				openCount -= 1;
			}
			if (openCount == 0) {
				startSearchIndex = i;
				break;
			}
		}

		for(int i = startSearchIndex; i < expression.length(); i++) {
			for (int j = 0; j < OPS.length; j++) {
				if (expression.charAt(i) == OPS[j]) 
					return i;
			}
		}
		
		return -1;
	}

	private static int cantorPair(int x, int y) {
		int w = x + y;
		return w*(w+1)/2 + y;
	}

	private static Pair<Integer, Integer> inverseCantor(int z) {
		int w = (int)Math.floor((Math.sqrt(8*z+1)-1) /2);
		int t = (int)((Math.pow(w, 2) + w) / 2);

		return new Pair<Integer, Integer>(w -(z - t), z-t);
	}
}

