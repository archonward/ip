package nebula;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main entry point for the Nebula GUI application.
 * Launches the JavaFX interface connected to the Nebula backend.
 */
public class GuiMain extends Application {
    private final Nebula nebula = new Nebula();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GuiMain.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = fxmlLoader.load();

            Scene scene = new Scene(root);
            stage.setTitle("Nebula Chatbot");
            stage.setScene(scene);

            MainWindow controller = fxmlLoader.getController();
            controller.setNebula(nebula);  // Inject backend into controller

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load MainWindow.fxml. Ensure it exists in src/main/resources/view/");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}