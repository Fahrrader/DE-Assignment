/** Class that handles numeric solution table creation */
public class NumericTable {
    private double x;
    private double euler;
    private double improvEuler;
    private double rungeKutta;
    private double exact;

    public NumericTable(double x, double euler, double improvEuler, double rungeKutta, double exact) {
        this.x = x;
        this.euler = euler;
        this.improvEuler = improvEuler;
        this.rungeKutta = rungeKutta;
        this.exact = exact;
    }

    public double getX() {
        return x;
    }

    public double getEuler() {
        return euler;
    }

    public double getImprovEuler() {
        return improvEuler;
    }

    public double getRungeKutta() {
        return rungeKutta;
    }

    public double getExact() {
        return exact;
    }
}