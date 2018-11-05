package methods;

// an abstract class meant to be ancestor to other method classes
public abstract class Method {
    abstract public double compute(double x, double y, double h, double x0, double y0);

    // get results of the function y' = f(x, y) = y^2/x^2 - 2
    public static double getDerivative(double x, double y) {
        return (y*y)/(x*x) - 2;
    }
}