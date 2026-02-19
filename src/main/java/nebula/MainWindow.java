package nebula;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nebula.ui.Ui;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Nebula nebula;
    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/Arthur.jpeg"));
    private Image nebulaImage = new Image(this.getClass().getResourceAsStream("/images/Nebula.png"));

    @FXML
    public void initialize() {
        // REMOVED showWelcome() call - nebula not injected yet
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Shows a welcome message from Nebula */
    public void showWelcome() {
        String greeting = nebula.ui.getWelcomeMessage();
        dialogContainer.getChildren().add(
                DialogBox.getNebulaDialog(greeting, nebulaImage)
        );
    }

    /** Injects the Nebula instance */
    public void setNebula(Nebula n) {
        nebula = n;
        String greeting = new Ui().getWelcomeMessage();
        dialogContainer.getChildren().add(
                DialogBox.getNebulaDialog(greeting, nebulaImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing
     * Nebula's reply and then appends them to the dialog container. Clears the
     * user input after processing. Handles application exit on "bye".
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = nebula.getResponse(input);
        addDialogBox(input, response);
        userInput.clear();
        handleExit(input);
    }

    /**
     * Creates dialog container containing user and Nebula input/response
     * @param input    user's input text
     * @param response Nebula's response to the text
     */
    private void addDialogBox(String input, String response) {
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getNebulaDialog(response, nebulaImage)
        );
    }

    /**
     * Handles exiting of application when user input is "bye"
     * @param input command input by user
     */
    private void handleExit(String input) {
        if (input.trim().equalsIgnoreCase("bye")) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
            PauseTransition wait = new PauseTransition(Duration.seconds(1.5));
            wait.setOnFinished(event -> Platform.exit());
            wait.play();
        }
    }
}