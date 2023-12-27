package com.cgvsu.terminal;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Terminal {
    private TextArea textArea;

    public Terminal(TextArea textArea) {
        this.textArea = textArea;
    }

    public void logModelLoading(String modelName) {
        log("Model " + "'" + modelName + "'" +" is loaded.");
    }

    public void logTextureLoading(String textureName) {
        log("Texture " + "'" + textureName + "'" +" is loaded.");
    }

    public void logCameraLoading(String cameraName, String x, String y, String z) {
        log("Сamera " + "'" +cameraName + "'" + " is loaded. Coordinates: X: " + x + " Y: " + y + " Z: " + z);
    }

    private void log(String message) {
        Platform.runLater(() -> textArea.appendText(message + "\n"));
    }
}