import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/** Class that handles dynamic hints appearing on the plot */
class ValueNode extends StackPane {

    public ValueNode(double xVal, double yVal) {
        setPrefSize(15, 15);

        Label label = createLabel(xVal, yVal);

        setOnMouseEntered(e -> {
            setStyle("-fx-background-color: white;" +
                    "-fx-border-color: #fbcf61;-fx-border-width: 2.5;-fx-border-radius: 25;-fx-border-style: solid outside;");
            getChildren().setAll(label);
            toFront();
        });
        setOnMouseExited(e -> {
            setStyle("");
            getChildren().clear();
        });
    }

    private Label createLabel(double xVal, double yVal) {
        Label label = new Label("x = " + xVal + "\ny = " + yVal);
        label.setOpacity(0.9);
        label.setPadding(new Insets(2));
        label.setStyle("-fx-border-color: RED;");
        label.setTextFill(Color.RED);
        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        return label;
    }
}