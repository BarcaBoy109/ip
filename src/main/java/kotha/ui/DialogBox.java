package kotha.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/** A speech bubble displayed in the chatbot conversation. */
public final class DialogBox extends HBox {
    private DialogBox(String text, boolean fromBot, Image image) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(320);
        label.getStyleClass().add(fromBot ? "bot-bubble" : "user-bubble");
        setAlignment(fromBot ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT);
        ImageView icon = new ImageView(image);
        icon.setFitWidth(36);
        icon.setFitHeight(36);
        icon.setPreserveRatio(true);
        setMaxWidth(Double.MAX_VALUE);
        setSpacing(6);
        if (fromBot) {
            getChildren().addAll(icon, label);
        } else {
            getChildren().addAll(label, icon);
        }
    }
    /** Creates a user speech bubble with an icon. */
    public static DialogBox getUserDialog(String text, Image image) {
        return new DialogBox(text, false, image);
    }

    /** Creates a bot speech bubble with an icon. */
    public static DialogBox getBotDialog(String text, Image image) {
        return new DialogBox(text, true, image);
    }
}
