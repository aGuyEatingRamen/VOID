package com.example.yessir;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class HelloController {

    @FXML
    private Button btnClick;
    @FXML
    private ChoiceBox<String> myChoiceBox;
    @FXML
    private HBox mainContainer;
    private final ObservableList<String> folders = FXCollections.observableArrayList("New Workspace");
    @FXML
    private Label welcomeText;
    @FXML
    private HBox titleBar;
    private String Ultpath;

    private boolean Run = true;
    private double xOffset = 0;
    private double yOffset = 0;

    private Stage currentStage;

    @FXML
    public void initialize() {
        auto();
        // Make the window draggable by the title bar
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        
        titleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    public void auto() {
        if (Run) {
            String file = "/Users/nitin/IdeaProjects/YES SIR/data";
            File mainFolder = new File(file);
            String[] folderList = mainFolder.list();
            if (folderList != null) {
                for (String folder : folderList) {
                    if (!folders.contains(folder)) {
                        folders.add(folder);
                    }
                }
            }
            System.out.println(folders);
            myChoiceBox.setItems(folders);
            Run = false;
        }
    }

    public void display(ActionEvent event) {
        String file = "/Users/nitin/IdeaProjects/YES SIR/data";
        String choice = myChoiceBox.getValue() != null ? myChoiceBox.getValue() : "";

        switch (choice) {
            case "New Workspace":
                TextInputDialog dialog = new TextInputDialog("default");
                dialog.setTitle("New Workspace");
                dialog.setHeaderText("Create New Workspace");
                dialog.setContentText("Please enter workspace name:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(workspaceName -> {
                    String fileSpec = file + "/" + workspaceName;
                    File theDir = new File(fileSpec);
                    boolean created = theDir.mkdir();
                    System.out.println("Workspace created: " + created);

                    if (created) {
                        folders.add(workspaceName);
                        myChoiceBox.setValue(workspaceName);
                    }
                });
                break;

            default:
                Ultpath = file + "/" + choice;
                System.out.println("Workspace path: " + Ultpath);
                loadHomeView(choice);
                break;
        }
    }

    private void loadHomeView(String workspaceName) {
        try {
            // Store the current stage reference
            currentStage = (Stage) mainContainer.getScene().getWindow();
            
            // Load the loading screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loading-view.fxml"));
            Parent loadingView = loader.load();
            Scene loadingScene = new Scene(loadingView);
            currentStage.setScene(loadingScene);
            currentStage.setFullScreen(true);
            currentStage.show();

            // Get the loading bar rectangle
            Rectangle loadingBarProgress = (Rectangle) loadingView.lookup("#loadingBarProgress");

            // Simulate loading with smooth animation
            new Thread(() -> {
                long startTime = System.currentTimeMillis();
                for (int i = 0; i <= 100; i++) {
                    final int progress = i;
                    Platform.runLater(() -> {
                        double progressWidth = (progress / 100.0) * 400; // 400 is the total width
                        loadingBarProgress.setWidth(progressWidth);
                        loadingBarProgress.setTranslateX((progressWidth - 400) / 2); // Center the progress bar
                    });
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Ensure minimum 3 seconds duration
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime < 3000) {
                    try {
                        Thread.sleep(3000 - elapsedTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Switch to home view
                Platform.runLater(() -> {
                    try {
                        loadHomeScene(workspaceName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadHomeScene(String workspaceName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Parent homeView = loader.load();
        
        HomeController homeController = loader.getController();
        homeController.setWorkspaceName(workspaceName);
        
        Scene homeScene = new Scene(homeView);
        currentStage.setScene(homeScene);
        currentStage.setFullScreen(true);

        currentStage.show();
    }

    @FXML
    private void minimizeWindow() {
        System.out.println("asdasdasdad");
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
}
