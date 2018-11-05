package methods;

public class Exact extends Method {
    @Override
    public double compute(double x, double y, double h, double x0, double y0) {
        double c = (y0/x0 - 2)/(x0*x0*x0 * (y0/x0 + 1)); // compute constant for new x0 and y0
        return - x*(c*x*x*x + 2) / (c*x*x*x - 1);
    }
}