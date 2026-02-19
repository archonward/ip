package nebula;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            // Load FXML BEFORE accessing @FXML fields (critical for injection)
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();  // @FXML fields injected HERE

            // NOW safe to access injected fields
            dialog.setText(text);
            displayPicture.setImage(img);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load DialogBox.fxml", e);
        }
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     * Used for bot responses (user dialogs appear on the right).
     */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        // Swap positions: move ImageView to left of Label
        javafx.collections.ObservableList<javafx.scene.Node> children = getChildren();
        if (children.size() >= 2) {
            javafx.scene.Node image = children.get(0);
            children.remove(0);
            children.add(image); // Move image to end (right side becomes left after alignment)
        }
    }

    public static DialogBox getUserDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.setAlignment(Pos.TOP_RIGHT); // User messages align to right
        return db;
    }

    public static DialogBox getNebulaDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip(); // Bot messages align to left
        return db;
    }
}