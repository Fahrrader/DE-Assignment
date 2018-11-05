package methods;

public class ImprovedEuler extends Method {
    @Override
    public double compute(double x, double y, double h, double x0, double y0) {
        double k1 = getDerivative(x, y);
        double k2 = getDerivative(x + h, y + h*k1);
        return y + (h/2)*(k1 + k2);
    }
}