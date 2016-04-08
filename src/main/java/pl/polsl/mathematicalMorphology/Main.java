package pl.polsl.mathematicalMorphology;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static final int WINDOW_WIDTH = 1200;
	public static final int WINDOW_HEIGHT = 680;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main_view.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("WKiRO - morfologia matematyczna dla obrazów kolorowych");
        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
