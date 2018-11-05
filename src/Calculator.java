import javafx.util.Pair;
import methods.*;

import java.util.ArrayList;

/** The class in charge of computations */
public class Calculator {
    // function for computing series
    public static ArrayList<Pair<Double, Double>> computeByMethod(double x0, double y0, double b, double n, int methodNum) {
        double x = x0,
                y = y0,
                h = (b - x0)/n;
        ArrayList<Pair<Double, Double>> results = new ArrayList<>();
        Method method;
        if (methodNum == 1) {
            method = new Euler();
        } else if (methodNum == 2) {
            method = new ImprovedEuler();
        } else if (methodNum == 3) {
            method = new RungeKutta();
        } else
            method = new Exact();

        do {
            results.add(new Pair<>(x, y));
            x += h;
            y = method.compute(x, y, h, x0, y0);
        } while (Math.abs(b - x) >= Math.abs(h));

        return results;
    }

    // compute error in case results of the method's work weren't presented
    public static ArrayList<Pair<Double, Double>> computeError(double x0, double y0, double b, double n, int methodNum) {
        return computeError(x0, y0, b, n, computeByMethod(x0, y0, b, n, methodNum));
    }

    // function for computing error, accepting results of a method
    public static ArrayList<Pair<Double, Double>> computeError(double x0, double y0, double b, double n,
                                    ArrayList<Pair<Double, Double>> methodResults) {
        // obtaining results for exact solution
        ArrayList<Pair<Double, Double>> exactResults = computeByMethod(x0, y0, b, n, 0);
        ArrayList<Pair<Double, Double>> results = new ArrayList<>();

        for (int i = 0; i < methodResults.size() && i < exactResults.size(); i++) {
            results.add(new Pair<>(
                    methodResults.get(i).getKey(),
                    Math.abs(methodResults.get(i).getValue() - exactResults.get(i).getValue())
            ));
        }

        return results;
    }
}