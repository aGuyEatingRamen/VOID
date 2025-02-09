package com.example.yessir;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

public class WebViewController {
    @FXML private StackPane webViewContainer; // To display the active WebView
    @FXML private TextField urlBar;
    @FXML private Button backButton;
    @FXML private Button forwardButton;
    @FXML private HBox tabBar;
    @FXML private Text clockDisplay;
    @FXML private HBox bookmarkBar; // Bookmark bar
    @FXML private Button bookmarkButton; // Bookmark button

    private List<WebView> webViews = new ArrayList<>();
    private WebView currentWebView;
    private List<String> bookmarks = new ArrayList<>(); // List to store bookmarks

    @FXML
    public void initialize() {
        createNewTab();
        startClock();   // Start the clock

        // Set styles for the main components
        webViewContainer.setStyle("-fx-background-color: #000000;"); // Black background for the WebView container
        urlBar.setStyle("-fx-background-color: #424242; -fx-text-fill: white; -fx-prompt-text-fill: #999999; -fx-background-radius: 25; -fx-padding: 8 15;"); // Dark gray for URL bar
        clockDisplay.setStyle("-fx-fill: white; -fx-font-size: 24;"); // White text for clock display

        // Set styles for the tab bar
        tabBar.setStyle("-fx-background-color: #2B2A33; -fx-padding: 5;"); // Dark gray for tab bar
    }

    private void startClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateClock()));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    private void updateClock() {
        LocalTime time = LocalTime.now();
        clockDisplay.setText(time.format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
    }

    @FXML
    public void createNewTab() {
        // Create a new WebView
        WebView newWebView = new WebView();
        webViews.add(newWebView);
        WebEngine webEngine = newWebView.getEngine();
        webEngine.load("https://www.google.com"); // Default to Google

        // Create the tab button
        Button tabButton = new Button("New Tab");
        tabButton.setPrefWidth(100); // Set a fixed width for the tab
        tabButton.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: white; " +  // Light gray text for better contrast in dark theme
            "-fx-font-size: 14px; " +
            "-fx-font-family: 'Arial'; " +
            "-fx-padding: 5px 10px; " +
            "-fx-background-radius: 0;" // Ensure seamless appearance
        );
        tabButton.setOnAction(e -> switchToTab(newWebView));

        // Create the close button
        Button closeButton = new Button("X");
        closeButton.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-family: 'Arial'; " +
            "-fx-padding: 5px; " +
            "-fx-background-radius: 0;"
        );

        // Close button action
        

        // Hover effects for close button
        closeButton.setOnMouseEntered(e -> closeButton.setStyle(
            "-fx-background-color: #3b3b3b; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-family: 'Arial'; " +
            "-fx-padding: 5px; " +
            "-fx-background-radius: 0;"
        ));
        closeButton.setOnMouseExited(e -> closeButton.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-family: 'Arial'; " +
            "-fx-padding: 5px; " +
            "-fx-background-radius: 0;"
        ));

        // Create the HBox for the tab
        HBox tab = new HBox(tabButton, closeButton);
        tab.setStyle(
            "-fx-background-color: transparent; " +  // Dark background for the tab
            "-fx-border-color: white; " +      // Dark border color
            "-fx-border-width: 1px; " +
            "-fx-padding: 5px; " +              // Padding inside the tab
            "-fx-background-radius: 5px;" +
            "-fx-border-width: 2px 0 0 0;" // Rounded corners for the tabs
        );
        tab.setSpacing(5);
        tab.setAlignment(Pos.CENTER_LEFT);
        
        tabBar.setSpacing(10);
        // Ensure the tabBar has no padding or spacing and aligns to the left
        tabBar.setStyle("-fx-padding: 0; -fx-spacing: 0;");
        tabBar.setAlignment(Pos.CENTER_LEFT);  // Align tabs to the left

        // Add the tab to the tabBar
        tabBar.getChildren().add(tab);
        
        closeButton.setOnAction(e -> closeTab(newWebView, tab));

        // Update the URL bar and tab title when the page changes
        webEngine.locationProperty().addListener((obs, oldUrl, newUrl) -> {
            if (newUrl != null && newWebView == currentWebView) {
                urlBar.setText(newUrl);
                updateTabTitleAndIcon(tabButton, newUrl); // Update tab title and icon
                updateTabBorderColor(tab, newUrl); // Update tab border color
            }
        });

        // Switch to the new tab
        switchToTab(newWebView);
    }

    private void switchToTab(WebView newWebView) {
        if (currentWebView != null) {
            webViewContainer.getChildren().remove(currentWebView);
        }
        currentWebView = newWebView;
        webViewContainer.getChildren().add(currentWebView);
        urlBar.setText(newWebView.getEngine().getLocation());
    }

    private void updateTabTitleAndIcon(Button tabButton, String url) {
        String iconPath = "https://www.google.com/s2/favicons?domain=" + url; // Favicon URL

        // Fetch the title from the web page
        currentWebView.getEngine().titleProperty().addListener((obs, oldTitle, newTitle) -> {
            if (newTitle != null && !newTitle.isEmpty()) {
                tabButton.setText(newTitle); // Update the tab title
            }
        });

        // Set the favicon
        ImageView icon = new ImageView(new Image(iconPath));
        icon.setFitHeight(16);
        icon.setFitWidth(16);
        tabButton.setGraphic(icon); // Set the icon for the tab
    }

    private void closeTab(WebView webViewToClose, HBox tab) {
        int index = webViews.indexOf(webViewToClose);
        if (index != -1) {
            webViews.remove(index);
            tabBar.getChildren().remove(tab); // Remove the entire HBox representing the tab
            webViewContainer.getChildren().remove(webViewToClose);
            if (!webViews.isEmpty()) {
                switchToTab(webViews.get(Math.max(0, index - 1)));
            } else {
                currentWebView = null;
                urlBar.clear();
            }
        }
    }
    

    @FXML
    public void handleBackButton() {
        if (currentWebView != null && currentWebView.getEngine().getHistory().getCurrentIndex() > 0) {
            currentWebView.getEngine().getHistory().go(-1);
        }
    }

    @FXML
    public void handleForwardButton() {
        if (currentWebView != null && currentWebView.getEngine().getHistory().getCurrentIndex() <
                currentWebView.getEngine().getHistory().getEntries().size() - 1) {
            currentWebView.getEngine().getHistory().go(1);
        }
    }

    @FXML
    public void loadURL() {
        String url = urlBar.getText().trim();

        // Check if the URL is empty
        if (url.isEmpty()) {
            return; // Do nothing if the URL is empty
        }

        // Check if the URL starts with "http://" or "https://"
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            // If it doesn't, prepend "https://" if it looks like a domain
            if (url.matches("^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                url = "https://" + url; // Prepend https:// if it looks like a valid domain
            } else {
                // Treat input as a search query
                String searchUrl = "https://www.google.com/search?q=" + url;
                currentWebView.getEngine().load(searchUrl);
                return; // Exit the method after loading the search
            }
        }

        // Load the URL in the WebView
        currentWebView.getEngine().load(url);
    }

    @FXML
    public void handleRefreshButton() {
        if (currentWebView != null) {
            currentWebView.getEngine().reload(); // Reload the current page
        }
    }

    @FXML
    public void handleBookmarkButton() {
        if (currentWebView != null) {
            String currentUrl = currentWebView.getEngine().getLocation();
            String currentTitle = currentWebView.getEngine().getTitle();
            String faviconUrl = "https://www.google.com/s2/favicons?domain=" + currentUrl;
            Image favicon = new Image(faviconUrl);  
            
            
            if (bookmarks.contains(currentUrl)) {
                bookmarks.remove(currentUrl);
                removeBookmarkButton(currentTitle); // Remove the bookmark button
                System.out.println("Unbookmarked: " + currentUrl);
            } else {
                Bookmark newBookmark = new Bookmark(currentTitle, currentUrl, favicon);
                bookmarks.add(currentUrl);
                addBookmarkButton(newBookmark);
                System.out.println("Bookmarked: " + currentUrl);
            }
            updateBookmarkButtonAppearance(currentUrl); // Update appearance after toggling
        }
    }

    private void updateBookmarkButtonAppearance(String currentUrl) {
        if (bookmarks.contains(currentUrl)) {
            bookmarkButton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: black;"); // Change style to indicate bookmarked
        } else {
            bookmarkButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        }
    }

    private void addBookmarkButton(Bookmark bookmark) {
        Button bookmarkButton = new Button(bookmark.getTitle());
        bookmarkButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        bookmarkButton.setPrefWidth(100); // Set a fixed width for the bookmark button
        bookmarkButton.setMaxWidth(100); // Set a maximum width for the bookmark button
        bookmarkButton.setOnAction(e -> currentWebView.getEngine().load(bookmark.getUrl()));

        ImageView icon = new ImageView(bookmark.getFavicon());
        icon.setFitHeight(16);
        icon.setFitWidth(16);
        bookmarkButton.setGraphic(icon);

        bookmarkBar.getChildren().add(bookmarkButton);
    }

    private void removeBookmarkButton(String currentUrl) {
        for (int i = 0; i < bookmarkBar.getChildren().size(); i++) {
            Button button = (Button) bookmarkBar.getChildren().get(i);
            if (button.getText().equals(currentUrl)) {
                bookmarkBar.getChildren().remove(i);
                break; // Exit after removing the button
            }
        }
    }

    // Method to get the dominant color from the favicon
    private Color getDominantColor(String iconPath) {
        try {
            InputStream input = new URL(iconPath).openStream();
            BufferedImage image = ImageIO.read(input);
            // Simple logic to get the average color (you can implement a more sophisticated method)
            int width = image.getWidth();
            int height = image.getHeight();
            long r = 0, g = 0, b = 0;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int rgb = image.getRGB(x, y);
                    r += (rgb >> 16) & 0xFF;
                    g += (rgb >> 8) & 0xFF;
                    b += rgb & 0xFF;
                }
            }
            int pixelCount = width * height;
            return Color.rgb((int)(r / pixelCount), (int)(g / pixelCount), (int)(b / pixelCount));
        } catch (Exception e) {
            e.printStackTrace();
            return Color.WHITE; // Default color in case of error
        }
    }

    // Update the tab's border color based on the favicon
    private void updateTabBorderColor(HBox tab, String url) {
        String iconPath = "https://www.google.com/s2/favicons?domain=" + url; // Favicon URL
        Color dominantColor = getDominantColor(iconPath);
        tab.setStyle(tab.getStyle() + "-fx-border-color: " + toRgbString(dominantColor) + ";");
    }

    // Helper method to convert Color to RGB string
    private String toRgbString(Color color) {
        return "rgb(" + (int)(color.getRed() * 255) + "," + (int)(color.getGreen() * 255) + "," + (int)(color.getBlue() * 255) + ")";
    }
}
