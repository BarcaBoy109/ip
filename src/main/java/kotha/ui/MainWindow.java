package kotha.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import kotha.KothaEngine;

/** Controller for Kotha's chatbot conversation window. */
public class MainWindow extends AnchorPane {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    private final KothaEngine chat = new KothaEngine();

    @FXML private void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        dialogContainer.getChildren()
                .add(DialogBox.getBotDialog("Hello! I’m Kotha. What can I do for you master?"));
    }

    /** Adds the user's message and Kotha's response to the conversation. */
    @FXML private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }
        dialogContainer.getChildren().addAll(DialogBox.getUserDialog(input), DialogBox.getBotDialog(chat.processCommand(input)));
        userInput.clear();
    }
}
