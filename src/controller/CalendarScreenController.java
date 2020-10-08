/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.AppointmentDAO;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Appointment;

/**
 * FXML Controller class
 *
 * @author longh
 */

// This class controls the screen that shows the calendar view
// There is a lot of UI code with JavaFX here because if the user wants to 
// switch views between month view and week view the components are scrapped and rebuilt
public class CalendarScreenController implements Initializable {
    String currentView = "month";
    LocalDateTime currentDate;
    ObservableList<LocalDate> currentMonthDays;
    ObservableList<FlowPane> days;
    ObservableList<Appointment> appointmentList;
    DateTimeFormatter formatter;
    AppointmentDAO appointmentDAO;
    
    @FXML
    private Button next;
    
    @FXML
    private Button previous;
            
    @FXML
    private Button userControl;
    
    @FXML
    private Button week;
    
    @FXML
    private Label month;
    
    @FXML
    private GridPane calendar;
    
    @FXML
    private void switchView(ActionEvent event) {
        if (currentView.equalsIgnoreCase("month")) {
            currentView = "week";
            loadWeek();
        } else if (currentView.equalsIgnoreCase("week")) {
            currentView = "month";
            loadMonth();
        }
    }
    
    @FXML
    private void next(ActionEvent event) {
        if (currentView.equalsIgnoreCase("month")){
            currentDate = currentDate.plusMonths(1);
            month.setText(currentDate.getMonth().toString());
            currentMonthDays.clear();
            currentMonthDays = getDates(currentDate.getMonthValue(), currentDate.getYear());
            loadMonth();
        } else if (currentView.equalsIgnoreCase("week")) {
            currentDate = currentDate.plusWeeks(1);
            month.setText(currentDate.getMonth().toString());
            currentMonthDays.clear();
            currentMonthDays = getDates(currentDate.getMonthValue(), currentDate.getYear());
            loadWeek();
        }
    }
    
    @FXML
    private void previous(ActionEvent event) {
        if (currentView.equalsIgnoreCase("month")){
            currentDate = currentDate.minusMonths(1);
            month.setText(currentDate.getMonth().toString());
            currentMonthDays.clear();
            currentMonthDays = getDates(currentDate.getMonthValue(), currentDate.getYear());
            loadMonth();
        } else if (currentView.equalsIgnoreCase("week")) {
            currentDate = currentDate.minusWeeks(1);
            month.setText(currentDate.getMonth().toString());
            currentMonthDays.clear();
            currentMonthDays = getDates(currentDate.getMonthValue(), currentDate.getYear());
            loadWeek();
        } 
    }
    
    @FXML
    private void userControl(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserControlScreen.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        Stage currentStage = (Stage) userControl.getScene().getWindow();
        currentStage.setScene(newScene);
    }
    
    private ObservableList<LocalDate> getDates(int month, int year) {
        ObservableList<LocalDate> dates = FXCollections.observableArrayList();
        
        for (LocalDate date = LocalDate.of(year, month, 1); date.getMonth() == Month.of(month); date = date.plusDays(1)) {
            dates.add(date);
        }
        return dates;
    }
    
    private void loadWeek() {
        LocalDate workingDate = currentDate.toLocalDate();
        while (!workingDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            workingDate = workingDate.minusDays(1);
        }
        if (calendar.getChildren().size() > 0) {
            calendar.getChildren().clear();
        }
        
        HBox weekBox = new HBox();
        weekBox.setFillHeight(Boolean.TRUE);
        GridPane.setVgrow(weekBox, Priority.ALWAYS);
        GridPane.setRowSpan(weekBox, 2);
        GridPane.setColumnSpan(weekBox, 7);
        calendar.add(weekBox, 0, 0);
        for (int i = 0; i < 7; i++) {
            Text date = new Text(String.valueOf(workingDate.getDayOfMonth()));
            VBox dateBox = new VBox(date);
            dateBox.borderProperty().set(new Border(new BorderStroke(Paint.valueOf("black"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            HBox.setHgrow(dateBox, Priority.ALWAYS);
            dateBox.setPrefWidth(weekBox.getWidth()/7);
            VBox.setMargin(date, new Insets(5.0, 0, 0, 5.0));
            VBox apptBox = new VBox();
            LocalDate dateToMatch = workingDate;
            ObservableList<Appointment> matchingAppt = appointmentList.filtered(appointment -> appointment.getStart().toLocalDate().equals(dateToMatch));
            for (Appointment a : matchingAppt) {
                Text appt = new Text(a.getTitle() + ":    " + a.getStart().toLocalTime().format(formatter) + " - " + a.getEnd().toLocalTime().format(formatter));
                apptBox.getChildren().add(appt);
                VBox.setMargin(appt, new Insets(10.0, 0, 0, 10));
                appt.setFont(new Font("Segoe UI", 12.0));
            }
            if (matchingAppt.size() > 0) {
                ScrollPane dateScrollPane = new ScrollPane(apptBox);
                dateBox.getChildren().add(dateScrollPane);
                VBox.setVgrow(dateScrollPane, Priority.ALWAYS);
            }  
            weekBox.getChildren().add(dateBox);
            workingDate = workingDate.plusDays(1);
        }
    }
    
    private void loadMonth() {
        LocalDate workingDate = currentDate.toLocalDate();
        while (workingDate.getDayOfMonth() != 1) {
            workingDate = workingDate.minusDays(1);
        }
        while (!workingDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            workingDate = workingDate.minusDays(1);
        }
        if (calendar.getChildren().size() > 0) {
            calendar.getChildren().clear();
        }
        
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                Text date = new Text(String.valueOf(workingDate.getDayOfMonth()));
                VBox dateBox = new VBox(date);
                dateBox.borderProperty().set(new Border(new BorderStroke(Paint.valueOf("black"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                GridPane.setFillWidth(dateBox, Boolean.TRUE);
                VBox.setMargin(date, new Insets(5.0, 0, 0, 5.0));
                calendar.add(dateBox, j, i);
                LocalDate dateToMatch = workingDate;
                VBox apptBox = new VBox();
                ObservableList<Appointment> matchingAppt = appointmentList.filtered(appointment -> appointment.getStart().toLocalDate().equals(dateToMatch));
                for (Appointment a :matchingAppt) {
                    Text appt = new Text(a.getTitle() + ":    " + a.getStart().toLocalTime().format(formatter) + " - " + a.getEnd().toLocalTime().format(formatter));
                    VBox.setMargin(appt, new Insets(10.0, 0, 0, 10));
                    appt.setFont(new Font("Segoe UI", 12.0));
                    apptBox.getChildren().add(appt);
                }
                if (matchingAppt.size() > 0) {
                    ScrollPane dateScrollPane = new ScrollPane(apptBox);
                    dateBox.getChildren().add(dateScrollPane);
                    VBox.setVgrow(dateScrollPane, Priority.ALWAYS);
                }
                workingDate = workingDate.plusDays(1);
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        appointmentDAO = new AppointmentDAO();
        appointmentList = appointmentDAO.getAll();
        currentDate = LocalDateTime.now();
        formatter = DateTimeFormatter.ofPattern("hh:mm a");
        month.setText(currentDate.getMonth().toString());
        currentMonthDays = getDates(currentDate.getMonthValue(), currentDate.getYear());
        loadMonth();
    }    
    
}
