package methods;

public class Euler extends Method {
    @Override
    public double compute(double x, double y, double h, double x0, double y0) {
        return y + h*getDerivative(x, y);
    }
}