package com.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AlertBox {

    @FXML
    private Label message;

    public void setLabelText(String messageAlert) {
        message.setText(messageAlert);
    }
}
