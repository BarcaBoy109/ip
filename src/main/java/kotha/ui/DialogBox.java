package kotha.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/** A speech bubble displayed in the chatbot conversation. */
public final class DialogBox extends HBox {
    private DialogBox(String text, boolean fromBot) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(320);
        label.getStyleClass().add(fromBot ? "bot-bubble" : "user-bubble");
        setAlignment(fromBot ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT);
        getChildren().add(label);
    }
    /** The user text dialog */
    public static DialogBox getUserDialog(String text) {
        return new DialogBox(text, false);
    }

    /** The bot text dialog */
    public static DialogBox getBotDialog(String text) {
        return new DialogBox(text, true);
    }
}
