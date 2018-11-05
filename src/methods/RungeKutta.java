package methods;

public class RungeKutta extends Method {
    @Override
    public double compute(double x, double y, double h, double x0, double y0) {
        double k1 = getDerivative(x, y);
        double k2 = getDerivative(x + h/2, y + k1*h/2);
        double k3 = getDerivative(x + h/2, y + k2*h/2);
        double k4 = getDerivative(x + h, y + k3*h);
        return y + (h/6)*(k1 + 2*k2 + 2*k3 + k4);
    }
}