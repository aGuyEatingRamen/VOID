package com.example.yessir;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.DayOfWeek;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CalendarController implements Initializable {
    @FXML private GridPane calendarGrid;
    @FXML private GridPane daysHeader;
    @FXML private ListView<String> calendarList;
    private LocalDate currentDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentDate = LocalDate.now();
        Platform.runLater(this::populateCalendar);
    }

    private void populateCalendar() {
        calendarGrid.getChildren().clear();
        
        // Get the start of the week
        LocalDate startOfWeek = currentDate.with(DayOfWeek.SUNDAY);
        
        // Set up the grid columns
        for (int i = 0; i < 7; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            column.setPercentWidth(14.28);
            calendarGrid.getColumnConstraints().add(column);
        }
        
        // Add day headers
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.getStyleClass().add("day-header");
            daysHeader.add(dayLabel, i, 0);
        }
        
        // Add week days
        for (int i = 0; i < 7; i++) {
            VBox cell = new VBox();
            cell.getStyleClass().add("calendar-cell");
            
            LocalDate date = startOfWeek.plusDays(i);
            Label dayLabel = new Label(String.valueOf(date.getDayOfMonth()));
            dayLabel.getStyleClass().add("calendar-cell-number");
            
            if (date.equals(LocalDate.now())) {
                dayLabel.getStyleClass().add("today");
            }
            
            cell.getChildren().add(dayLabel);
            calendarGrid.add(cell, i, 0);
        }
    }


    @FXML
    public void previousMonth(ActionEvent event) {
        currentDate = currentDate.minusMonths(1);
        populateCalendar();
    }

    @FXML
    public void nextMonth(ActionEvent event) {
        currentDate = currentDate.plusMonths(1);
        populateCalendar();
    }

    @FXML
    public void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
            Parent homeView = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene homeScene = new Scene(homeView);
            stage.setScene(homeScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToMonthView(ActionEvent event) {
        // Implementation for switching to month view
    }

    @FXML
    public void goToToday(ActionEvent event) {
        currentDate = LocalDate.now();
        populateCalendar();
    }

    


    


}