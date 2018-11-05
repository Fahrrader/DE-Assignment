package methods;

public class Exact extends Method {
    @Override
    public double compute(double x, double y, double h, double x0, double y0) {
        return getGeneralSolution(x, x0, y0);
    }

    public static double getConstant(double x0, double y0) {
        return (y0/x0 - 2)/(x0*x0*x0 * (y0/x0 + 1));
    }

    public static double getGeneralSolution(double x, double x0, double y0) {
        double c = getConstant(x0, y0);
        return x*(c*x*x*x + 2) / (1 - c*x*x*x);
    }
}