package nebula;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import nebula.DialogBox;
import nebula.Nebula;

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
    private Image nebulaImage = new Image(this.getClass().getResourceAsStream("/images/Nebula.png")); // Fixed: camelCase naming

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Nebula instance */
    public void setNebula(Nebula n) { // Fixed: renamed from setDuke
        nebula = n;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Nebula's reply,
     * then appends them to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = nebula.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBotDialog(response, nebulaImage) // Note: DialogBox method name unchanged (we'll fix next)
        );
        userInput.clear();
    }
}