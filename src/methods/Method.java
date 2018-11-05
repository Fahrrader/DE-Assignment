package methods;

public abstract class Method {
    abstract public double compute(double x, double y, double h, double x0, double y0);

    public static double getDerivative(double x, double y) {
        return (y*y)/(x*x) - 2;
    }
}