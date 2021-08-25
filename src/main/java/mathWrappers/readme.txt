mathWrappers wraps basic mathematical functions and operators, and allows them to be represented and manipulated

MathObject is the abstract superclass of all other mathematical functions contained, like Sine, Cosine, and Tangent.
MathObject also allows limited string parsing, converting a string like "(x+y)^2" into a usable MathObject.
MathObject, and all of it's child classes, are immutable and reusable -- x+y is x+y, no matter how many times it is reused

Arrays of EvalVars are used to evaluate math objects. EvalVars serve to pair up a variable and a value.
When a MathObject needs to be evaluated, the EvalVar array is passed down the chain of nested MathObjects until it reaches a variable
or a Constant. Once that happens, it is evaluated, and the valuation is passed back up the chain. The process is effectively recursion.

Vectors are just containers of math objects. They do not support coefficients at this time.

You can calculate the derivative or the gradient of any MathObject. In order to do so, you will need to specify with which
variable you are deriving with respect to.

Creating an Equation of MathObjects:
In order to represent an equation using MathObjects, you must begin with creating the lowest level components, and then use them like
building blocks in order to create a more complex equation.

Here is example code creating the expressions "(e^y + ycos(x))^z" and "xycos(x)":

    MathObject x = new Variable("x");
    MathObject y = new Variable("y");
    MathObject z = new Variable("z");
    MathObject cosX = new Cos(x);
    MathObject yCosX = new Mult(y, cosX);
    MathObject exp = new Power(y);
    MathObject complex = new Add(exp, yCosX);
    MathObject final = new Power(complex, z);

// If I then wanted to create the expression "xycos(x)":
    MathObject xyCosX = new Mult(x, yCosX);
