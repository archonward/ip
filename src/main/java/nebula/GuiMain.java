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

    /**
     * Starts and displays the primary JavaFX stage for the Nebula application.
     * <p>
     * This method loads the main window FXML, applies the application stylesheets,
     * injects the {@link Nebula} backend instance into the {@link MainWindow} controller,
     * and then shows the UI.
     * </p>
     *
     * @param stage the primary stage provided by the JavaFX runtime.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GuiMain.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);

            // Load both CSS files
            scene.getStylesheets().add(GuiMain.class.getResource("/styles/main.css").toExternalForm());
            scene.getStylesheets().add(GuiMain.class.getResource("/styles/dialog-box.css").toExternalForm());

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

    /**
     * Launches the Nebula JavaFX application.
     *
     * @param args command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
