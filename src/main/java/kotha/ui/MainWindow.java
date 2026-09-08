package kotha.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import kotha.KothaEngine;

/** Controller for Kotha's chatbot conversation window. */
public class MainWindow extends AnchorPane {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    @FXML private Button profileButton;
    private final KothaEngine chat = new KothaEngine();
    private Image userImage = createDefaultImage(Color.STEELBLUE);
    private final Image botImage = new Image(
            MainWindow.class.getResource("/images/kotha-icon.png").toExternalForm());

    @FXML private void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        dialogContainer.getChildren()
                .add(DialogBox.getBotDialog("Hello! I’m Kotha. What can I do for you master?", botImage));
    }

    /** Adds the user's message and Kotha's response to the conversation. */
    @FXML private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }
        String response = chat.processCommand(input);
        dialogContainer.getChildren().addAll(DialogBox.getUserDialog(input, userImage),
                DialogBox.getBotDialog(response, botImage, chat.getLastResponseStyle()));
        userInput.clear();
    }

    /** Lets the user choose the icon shown beside their messages. */
    @FXML private void chooseProfilePicture() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose profile picture");
        chooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        var file = chooser.showOpenDialog(profileButton.getScene().getWindow());
        if (file != null) userImage = new Image(file.toURI().toString());
    }

    private static Image createDefaultImage(Color color) {
        WritableImage image = new WritableImage(36, 36);
        for (int x = 0; x < 36; x++) {
            for (int y = 0; y < 36; y++) image.getPixelWriter().setColor(x, y, color);
        }
        return image;
    }
}
