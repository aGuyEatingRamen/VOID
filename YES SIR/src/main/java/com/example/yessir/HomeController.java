package com.example.yessir;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomeController {
    @FXML private Text timeDisplay;
    @FXML private Text dateDisplay;
    @FXML private Button settingsButton;
    @FXML private HBox titleBar;
    private String workspaceName;
    private double xOffset = 0;
    private double yOffset = 0;
    
    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }
    
    @FXML
    public void initialize() {
        // Initialize title bar drag functionality
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        
        titleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        // Initialize clock
        if (timeDisplay != null && dateDisplay != null) {
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                LocalTime time = LocalTime.now();
                LocalDate date = LocalDate.now();
                timeDisplay.setText(time.format(DateTimeFormatter.ofPattern("HH:mm")));
                dateDisplay.setText(date.format(DateTimeFormatter.ofPattern("MMM dd")));
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }
        
        settingsButton.setOnAction(e -> openSettings());
    }
    
    @FXML
    private void minimizeWindow() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void maximizeWindow() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void openSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/yessir/settings-view.fxml"));
            Parent settingsView = loader.load();
            
            Stage stage = (Stage) timeDisplay.getScene().getWindow();
            Scene settingsScene = new Scene(settingsView);
            stage.setScene(settingsScene);
            stage.setTitle("Settings");
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleGlobeClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("web-view.fxml"));
            Scene webScene = new Scene(loader.load());
            Stage stage = (Stage) timeDisplay.getScene().getWindow();
            stage.setScene(webScene);
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showCalendar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("calendar-view.fxml"));
            Parent calendarView = loader.load();
            Stage stage = (Stage) timeDisplay.getScene().getWindow();
            Scene calendarScene = new Scene(calendarView);
            stage.setScene(calendarScene);
            stage.setTitle("Calendar");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}