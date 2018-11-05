import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;

/** The class in charge of graphics and user interface */
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

    ArrayList<Pair<Double, Double>> coordinates[];
    XYChart.Series plots[];
    public int selectedMethodNum;

    // happens automatically upon successful loading of controlled .fxml file
    @FXML public void initialize() {
        selectedMethodNum = 0;
        styleSelectedMethod();

        forceNumberic(x0);
        forceNumberic(y0);
        forceNumberic(X);
        forceNumberic(N);

        coordinates = new ArrayList[7];
        plots = new XYChart.Series[7];
        for (int i = 0; i < plots.length; i++) {
            coordinates[i] = new ArrayList<>();
            plots[i] = new XYChart.Series();
        }
        chart.getData().add(plots[0]);
    }

    // force the text field to accept numbers only
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

    // set some highlight to currently displayed plot's method
    private void styleSelectedMethod() {
        methodButtons.getChildren().get(selectedMethodNum + 1).setStyle(
                "-fx-font-weight: bold;" +
                "-fx-background-color: linear-gradient(steelblue, skyblue);"
        );
    }

    // function that executes when user clicks on a method button
    @FXML public void selectMethod(Event e) {
        if (methodButtons.getChildren().indexOf(e.getSource()) - 1 == selectedMethodNum) {
            e.consume();
            return; // if the same method
        }
        methodButtons.getChildren().get(selectedMethodNum + 1).setStyle("");
        chart.getData().remove(plots[selectedMethodNum]);
        selectedMethodNum = methodButtons.getChildren().indexOf(e.getSource()) - 1;
        styleSelectedMethod();
        chart.getData().add(plots[selectedMethodNum]);
    }

    // occurs when button "build plots" is pressed
    @FXML public void buildPlots() {
        for (int i = 0; i < plots.length; i++)
            plots[i].getData().clear();

        if (x0.getText().isEmpty() || x0.getText().equals("-")) x0.setText("0");
        if (y0.getText().isEmpty() || y0.getText().equals("-")) y0.setText("0");
        if (X.getText().isEmpty() || X.getText().equals("-")) X.setText("0");
        if (N.getText().isEmpty() || N.getText().equals("-")) N.setText("0");

        double x0 = Double.parseDouble(this.x0.getText()),
                y0 = Double.parseDouble(this.y0.getText()),
                b = Double.parseDouble(this.X.getText()),
                n = Double.parseDouble(this.N.getText());

        XYChart.Data<Number, Number> data;
        for (int i = 0; i < 4; i++) { // run first through main methods
            ArrayList<Pair<Double, Double>> results = Calculator.computeByMethod(x0, y0, b, n, i);
            coordinates[i] = results;
            for (Pair<Double, Double> p : results) {
                data = new XYChart.Data(p.getKey(), p.getValue());
                data.setNode(new ValueNode(p.getKey(), p.getValue()));
                plots[i].getData().add(data);
            }
        }

        for (int i = 1; i < 4; i++) { // then through their errors.
            ArrayList<Pair<Double, Double>> results = Calculator.computeError(x0, y0, b, n, coordinates[i]);
            coordinates[i + 3] = results;
            for (Pair<Double, Double> p : results) {
                data = new XYChart.Data(p.getKey(), p.getValue());
                data.setNode(new ValueNode(p.getKey(), p.getValue()));
                plots[i + 3].getData().add(data);
            }
        }
    }

    // function that displays numeric solution table for all methods
    @FXML public void openTable() {
        Stage window = new Stage();
        window.setTitle("Numerical Solution Table");
        window.setMinWidth(560);
        window.setMinHeight(450);

        ObservableList<NumericTable> list = FXCollections.observableArrayList();
        for (int i = 0; i < coordinates[0].size(); i++) {
            list.add(new NumericTable(
                    coordinates[0].get(i).getKey(),
                    coordinates[1].get(i).getValue(),
                    coordinates[2].get(i).getValue(),
                    coordinates[3].get(i).getValue(),
                    coordinates[0].get(i).getValue())
            );
        }

        TableColumn<NumericTable, Double> column1 = new TableColumn<>("x");
        column1.setPrefWidth(75);
        column1.setCellValueFactory(new PropertyValueFactory<>("x"));

        TableColumn<NumericTable, Double> column2 = new TableColumn<>("Euler");
        column2.setPrefWidth(120);
        column2.setCellValueFactory(new PropertyValueFactory<>("euler"));

        TableColumn<NumericTable, Double> column3 = new TableColumn<>("Improved Euler");
        column3.setPrefWidth(120);
        column3.setCellValueFactory(new PropertyValueFactory<>("improvEuler"));

        TableColumn<NumericTable, Double> column4 = new TableColumn<>("Runge-Kutta");
        column4.setPrefWidth(120);
        column4.setCellValueFactory(new PropertyValueFactory<>("rungeKutta"));

        TableColumn<NumericTable, Double> column5 = new TableColumn<>("Exact");
        column5.setPrefWidth(120);
        column5.setCellValueFactory(new PropertyValueFactory<>("exact"));

        TableView<NumericTable> table = new TableView<>();
        table.setItems(list);
        table.getColumns().setAll(column1, column2, column3, column4, column5);

        window.setScene(new Scene(table, 560, 420));
        window.showAndWait();
    }
}
