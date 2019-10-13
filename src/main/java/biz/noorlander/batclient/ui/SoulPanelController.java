package biz.noorlander.batclient.ui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import javax.swing.*;
import java.io.IOException;

public class SoulPanelController {
    @FXML
    public Label hpValue;
    @FXML
    private Label soulPoints;
    @FXML
    public Label mountProgress;
    @FXML
    public Label mountLevel;
    @FXML
    public ProgressBar hpBar;

    public void setSoulPoints(int newSoulPoints) {
        Platform.runLater(() -> soulPoints.setText(String.valueOf(newSoulPoints)));
    }

    public void setMountLevel(String newMountLevel) {
        Platform.runLater(() -> mountLevel.setText(newMountLevel));
    }

    public void setMountProgress(String newMountProgress) {
        Platform.runLater(() -> mountProgress.setText(newMountProgress));
    }

    public void setSoulHealth(int newSoulPoints) {
        Platform.runLater(() -> {
            hpBar.setProgress(100.0 / newSoulPoints);
            hpValue.setText(newSoulPoints + "%");
        });
    }

}
