package xyz.jmatt.dialogbox;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class DialogController {
    @FXML
    private Text message;

    public void setMessage(String message) {
        this.message.setText(message);
    }
}
