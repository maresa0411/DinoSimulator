package de.ossenbeck.dinosimulator.view;

import de.ossenbeck.dinosimulator.util.Notifier;
import javafx.scene.control.Label;

public class MessagePane implements Notifier {
    private Label messageLabel;

    public MessagePane(){
        this.messageLabel = new Label("Willkommen");
    }
    @Override
    public void post(String message) {
        messageLabel.setText(message);
    }
}
