import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage window;
    public static final int windowWidth = 800;
    public static final int windowHeight = 400;

    private static graphics.Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("graphics/graphics.fxml"));
        window = primaryStage;
        window.setTitle("Plot Builder");
        window.setMinWidth(700);
        window.setMinHeight(420);
        window.setScene(new Scene(loader.load(), windowWidth, windowHeight));
        controller = loader.getController();
        controller.anchor.setPrefWidth(windowWidth);
        controller.anchor.setPrefHeight(windowHeight);
        window.show();
        // todo chart: sizing, multiple plots, when hovering, show what values plot has (unnecessary)
        // todo make an element to print error
        // todo build every graph when button is pressed, or at least don't forget and show later
    }

    public static void main(String[] args) {
        launch(args);
    }

    //public static void display
}
