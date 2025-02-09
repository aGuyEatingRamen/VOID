package com.example.yessir;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsController {
    @FXML
    private VBox settingsContainer;

    @FXML
    private StackPane contentArea;

    public void closeSettings() {
        Stage stage = (Stage) settingsContainer.getScene().getWindow();
        stage.close(); // Close the settings window
    }

    @FXML
    public void showBrowserSettings() {
        loadFXML("browser-settings.fxml");
    }

    @FXML
    public void showNotesSettings() {
        loadFXML("notes-settings.fxml");
    }

    @FXML
    public void showCalendarSettings() {
        loadFXML("calendar-settings.fxml");
    }

    @FXML
    public void showSecuritySettings() {
        loadFXML("security-settings.fxml");
    }

    @FXML
    public void showSupportSettings() {
        loadFXML("support-settings.fxml");
    }

    private void loadFXML(String fxmlFile) {
        try {
            System.out.println("Loading FXML from: " + getClass().getResource("/com/example/yessir/" + fxmlFile));
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/yessir/" + fxmlFile));
            Parent newContent = loader.load();
            contentArea.getChildren().clear(); // Clear existing content
            contentArea.getChildren().add(newContent); // Add new content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}