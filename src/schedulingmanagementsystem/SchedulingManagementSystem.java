/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingmanagementsystem;

import java.io.IOException;
import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.AppointmentTimes;
import utils.DBConnection;

/**
 *
 * @author longh
 */

// This class starts the application and holds a few static references that are used throughout the rest of the application
// like the database connector, the locale that the app starts in, the current user info and the object representing the
// business hours for appointment times
public class SchedulingManagementSystem extends Application {
    
    public static DBConnection dbConnector;
    public static Locale currentLocale;
    public static String currentUser;
    public static int currentUserId;
    public static AppointmentTimes appointmentTimes;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("Appointment Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // This is where you might need to replace "localhost" with your servername, "U05HDN" with your DB username, and "53688501284" with your DB password
        dbConnector = new DBConnection("jdbc", "mysql", "localhost", "scheduler", "U05HDN", "53688501284");
        currentLocale = Locale.getDefault();
        appointmentTimes = new AppointmentTimes();
        launch(args);
    }
    
}
