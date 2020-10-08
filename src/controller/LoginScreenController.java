/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static schedulingmanagementsystem.SchedulingManagementSystem.currentLocale;
import static schedulingmanagementsystem.SchedulingManagementSystem.dbConnector;
import static schedulingmanagementsystem.SchedulingManagementSystem.currentUser;
import static schedulingmanagementsystem.SchedulingManagementSystem.currentUserId;

/**
 * FXML Controller class
 *
 * @author longh
 */

// This class manages and controls the login screen
public class LoginScreenController implements Initializable {
    
    // Resource bundle to hold the messages for any alerts that may need to pop up
    ResourceBundle loginResources;
    
    @FXML
    private TextField userNameField; 
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginBtn;
    
    // Method that is invoked whenever a user clicks login. It will query the database against the user input to
    // look for matching login info and try to login if they have entered valid input. If not, then a
    // useful alert is shown.
    // This method shows one of the first error management variations with the try with resources.
    // I used this method a lot through out the program to make sure that my database connections were closed along
    // with any other resources that they created or needed
    @FXML
    private void login(ActionEvent event) throws SQLException, IOException {
        if (userNameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            showAlert(AlertType.INFORMATION, "emptyfields", "Empty Login Fields");
            return;
        }
                
        String userName = userNameField.getText();
        String password = passwordField.getText();
        
        try (Connection conn = dbConnector.ds.getConnection();
             PreparedStatement prep = conn.prepareStatement("SELECT password, userId FROM user WHERE userName = ?")) {
            prep.setString(1, userName);
            try (ResultSet results = prep.executeQuery()) {
                if (results.next()) {
                    int equality = password.compareTo(results.getString("password"));
                    if (equality != 0) {
                        showAlert(AlertType.INFORMATION, "userpass", "Username or password error");
                    } else {
                        currentUser = userName;
                        currentUserId = results.getInt("userId");
                        logUserSignIn();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserControlScreen.fxml"));
                        Parent root = loader.load();
                        Scene newScene = new Scene(root);
                        Stage currentStage = (Stage) userNameField.getScene().getWindow();
                        currentStage.setScene(newScene);
                    }
                } else {
                    showAlert(AlertType.INFORMATION, "userpass", "Username or password error");
                }
            }
        }
    }
    
    // This method is called when a user successfully logs in and it logs to a file which user has signed in and on what date
    // and at what time
    private void logUserSignIn() throws IOException {
        Path loginLog = Paths.get(".", "reports", "login.txt").toAbsolutePath().normalize();
        if (Files.exists(loginLog)) {
            try (BufferedWriter bw = Files.newBufferedWriter(loginLog, StandardOpenOption.APPEND)) {
                bw.write(currentUser + " logged in on " + LocalDateTime.now().toString());
                bw.newLine();
            }
        } else {
            Files.createFile(loginLog);
            try (BufferedWriter bw = Files.newBufferedWriter(loginLog, StandardOpenOption.APPEND)) {
                bw.write(currentUser + " logged in on " + LocalDateTime.now().toString());
                bw.newLine();
            }
        }
    }
    
    // This is the method to show the alert
    // This method includes one of the many uses of lambdas throughout the code
    // I used them to shorten a lot of the necessary pieces of code into single lines and to make many
    // parts much more readable
    private void showAlert(Alert.AlertType alertType, String key, String title) {
        Alert alert = new Alert(alertType, loginResources.getString(key));
        alert.setTitle(title);
        alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> alert.hide());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginResources = ResourceBundle.getBundle("localization.Login", currentLocale);
        // TODO
    }    
    
}
