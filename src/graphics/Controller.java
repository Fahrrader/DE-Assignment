package graphics;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Controller {
    // binding elements of this .java file to ones of .fxml
    @FXML public AnchorPane anchor;
    @FXML public Button buildButton;
    @FXML public Button exactButton;
    @FXML public Button eulerButton;
    @FXML public Button improvEulerButton;
    @FXML public Button rungeKuttaButton;
    @FXML public Button eulerErrorButton;
    @FXML public Button improvErrorButton;
    @FXML public Button rungeErrorButton;
    @FXML public GridPane methodButtons;
    @FXML public TextField x0;
    @FXML public TextField y0;
    @FXML public TextField X;
    @FXML public TextField N;
    @FXML public LineChart<Number, Number> chart;

    XYChart.Series plots[];
    XYChart.Series series;
    public int selectedMethodNum;

    // happens automatically upon successful loading of controlled .fxml file
    @FXML public void initialize() {
        selectedMethodNum = 1;
        styleSelectedMethod();

        forceNumberic(x0);
        forceNumberic(y0);
        forceNumberic(X);
        forceNumberic(N);

        plots = new XYChart.Series[7];
        for (int i = 0; i < plots.length; i++) {
            plots[i] = new XYChart.Series();
        }
        series = plots[0];
        chart.getData().add(series);
    }

    // force the field to be numeric only
    private void forceNumberic(TextField tf) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\-?\\d{0,16}([\\.]\\d{0,16})?")) {
                    tf.setText(oldValue);
                }
            }
        });
    }

    private void styleSelectedMethod() {
        methodButtons.getChildren().get(selectedMethodNum).setStyle(
                "-fx-font-weight: bold;" +
                "-fx-background-color: linear-gradient(steelblue, skyblue);"
        );
    }

    @FXML public void selectMethod(Event e) {
        methodButtons.getChildren().get(selectedMethodNum).setStyle("");
        selectedMethodNum = methodButtons.getChildren().indexOf(e.getSource());
        System.out.println(selectedMethodNum);
        styleSelectedMethod();
    }

    @FXML public void zoomChart(ScrollEvent e) {
        final NumberAxis axisX = (NumberAxis)chart.getXAxis();
        final NumberAxis axisY = (NumberAxis)chart.getYAxis();
        //final double lowerX = axisX.getLowerBound(), upperX = axisX.getUpperBound();
        final double minX = axisX.getLowerBound(), maxX = axisX.getUpperBound();
        //final double lowerY = axisY.getLowerBound(), upperY = axisY.getUpperBound();
        final double minY = axisY.getLowerBound(), maxY = axisY.getUpperBound();
        double threshold = minX + (maxX - minX) / 2d;//, thresholdY = minY + (maxY - minY) / 2d;
        double x = e.getX(), y = e.getY();
        double value = axisX.getValueForDisplay(x).doubleValue();
        double mod = 0.025;
        if (e.getDeltaY() > 0) {
            axisX.setUpperBound(maxX - mod*(maxX - minX));
            axisX.setLowerBound(minX + mod*(maxX - minX));
            /*axisY.setUpperBound(maxY - mod*(maxY - minY));
            axisY.setLowerBound(minY + mod*(maxY - minY));*/
            /*if (maxX - minX <= 1) {
                return;
            }*/
            System.out.println(e.getX());
            /*if (value > threshold) { // if(value > threshold)
                axisX.setLowerBound(minX + mod*(value - threshold));
            } else {
                axisX.setUpperBound(maxX + mod*(value - threshold));
            }*/

        } else {
            axisX.setUpperBound(maxX + mod*(maxX - minX));
            axisX.setLowerBound(minX - mod*(maxX - minX));
            /*axisY.setUpperBound(maxY + mod*(maxY - minY));
            axisY.setLowerBound(minY - mod*(maxY - minY));*/
            /*if (value < thresholdX) {
                double nextBound = Math.min(lowerX, minX - 1);
                axisX.setLowerBound(nextBound);
            } else {
                double nextBound = Math.max(upperX, maxX + 1);
                axisX.setUpperBound(nextBound);
            }*/
        }
        axisX.setTickUnit((axisX.getUpperBound()-axisX.getLowerBound())/10);
        //axisY.setTickUnit((axisY.getUpperBound()-axisY.getLowerBound())/10);
    }

    @FXML public void scrollChart() {
    }

    @FXML public void defaultChart(MouseEvent e) {
        //if (e.getClickCount() >= 2) centerGraph();
    }

    public void centerGraph(double minX, double minY, double maxX, double maxY) {
        final NumberAxis axisX = (NumberAxis)chart.getXAxis();
        final NumberAxis axisY = (NumberAxis)chart.getYAxis();

        axisX.setUpperBound(Math.ceil(maxX + (maxX - minX)*0.05));
        axisX.setLowerBound(Math.floor(minX - (maxX - minX)*0.05));
        axisY.setUpperBound(Math.ceil(maxY + (maxY - minY)*0.05));
        axisY.setLowerBound(Math.floor(minY - (maxY - minY)*0.05));

        axisX.setTickUnit((axisX.getUpperBound()-axisX.getLowerBound())/10);
        axisY.setTickUnit((axisY.getUpperBound()-axisY.getLowerBound())/10);
    }

    @FXML public void buildGraph() {
        if (x0.getText().isEmpty() || x0.getText().equals("-")) x0.setText("0");
        if (y0.getText().isEmpty() || y0.getText().equals("-")) y0.setText("0");
        if (X.getText().isEmpty() || X.getText().equals("-")) X.setText("0");
        if (N.getText().isEmpty() || N.getText().equals("-")) N.setText("0");

        series.getData().clear();
        XYChart.Data<Number, Number> data;
        double y;
        double minX, minY, maxX, maxY;
        minX = maxX = Double.parseDouble(x0.getText());
        minY = maxY = Math.sin(minX);
        for (double x = Double.parseDouble(x0.getText()); x < 10; x+=0.1) {
            x = (double) Math.round(x * 10000) / 10000;
            y = Math.sin(x);
            if (x > maxX) maxX = x;
            else if (x < minX) minX = x;
            if (y > maxY) maxY = y;
            else if (y < minY) minY = y;
            data = new XYChart.Data(x, y);
            data.setNode(new ValueNode(x, y));
            series.getData().add(data);
        }
        centerGraph(minX, minY, maxX, maxY);
    }
}
