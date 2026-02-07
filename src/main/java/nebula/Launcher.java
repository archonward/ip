package nebula;

import javafx.application.Application;

/**
 * Launcher class to workaround JavaFX classpath issues in Gradle.
 * This allows Gradle's 'run' task to launch the JavaFX GUI application.
 */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(GuiMain.class, args);  // Launches YOUR JavaFX app
    }
}