package de.ossenbeck.dinosimulator.view;

import de.ossenbeck.dinosimulator.util.Notifier;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MessagePane extends StackPane implements Notifier {
    private final Label messageLabel;

    public MessagePane(){
        this.messageLabel = new Label("Willkommen");
        this.getChildren().add(messageLabel);
    }

    @Override
    public void post(String message) {
        messageLabel.setText(message);
    }
}
