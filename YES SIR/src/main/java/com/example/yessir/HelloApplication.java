package com.example.yessir;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HelloApplication extends Application {



    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        
        // Set minimum window size
        stage.setMinWidth(1280);
        stage.setMinHeight(720);
        
        // Remove default window decorations
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setFullScreenExitHint("");

        // Disable exiting full-screen mode with the ESC key
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        
        // Make stage full screen
        stage.setMaximized(true);
        stage.setTitle("VOID");
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {



        launch(args);



    }
}