/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.AppointmentDAO;
import DAO.CustomerDAO;
import java.io.IOException;
import java.net.URL;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Appointment;
import model.Customer;
import static schedulingmanagementsystem.SchedulingManagementSystem.appointmentTimes;
import static schedulingmanagementsystem.SchedulingManagementSystem.currentLocale;
import static schedulingmanagementsystem.SchedulingManagementSystem.currentUserId;
import utils.OffsetTimeConverter;
import utils.TimeUtils;

/**
 * FXML Controller class
 *
 * @author longh
 */

// This class controls the screen that adds appointments.
public class AddAppointmentScreenController implements Initializable {
    
    CustomerDAO customerDAO;
    AppointmentDAO appointmentDAO;
    ObservableList<Customer> allCustomers;
    ResourceBundle alertResources;
    ObservableList<Appointment> linkedDateAppointments;

    
    
    @FXML
    private TextField title;
            
    @FXML
    private TextField location;
            
    @FXML
    private TextField type;
            
    @FXML
    private TextField contact;
            
    @FXML
    private TextField url;
            
    @FXML
    private TextArea description;
    
    @FXML
    private TableView<Customer> customerTbl;
    
    @FXML
    private TableColumn customerId;
    
    @FXML
    private TableColumn customerName;
    
    @FXML
    private DatePicker start;
    
    @FXML
    private ChoiceBox<OffsetTime> startTime;
    
    @FXML
    private ChoiceBox<OffsetTime> endTime;
    
    @FXML
    private Button cancel;
    
    @FXML
    private Button add;
    
    
    @FXML
    private void add(ActionEvent event) throws IOException {
        if (verifyInputs()) {
            Appointment newAppointment = new Appointment(0, customerTbl.getSelectionModel().getSelectedItem().getCustomerId(), currentUserId,
            title.getText(), description.getText().isEmpty() ? "not needed" : description.getText(),
            location.getText().isEmpty() ? "not needed" : location.getText(), contact.getText().isEmpty() ? "not needed" : contact.getText(),
            type.getText().isEmpty() ? "not needed" : type.getText(), url.getText().isEmpty() ? "not needed" : url.getText(),
            ZonedDateTime.of(start.getValue(), startTime.valueProperty().get().toLocalTime(), ZoneId.of(startTime.valueProperty().get().getOffset().getId())),
            ZonedDateTime.of(start.getValue(), endTime.valueProperty().get().toLocalTime(), ZoneId.of(endTime.valueProperty().get().getOffset().getId())));
            appointmentDAO.create(newAppointment);
            closeScreen();
        } else {
            showAlert(AlertType.INFORMATION, "appscreen", "Possible errors with new appointment");
        }
    }
    
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        closeScreen();
    }
    
    private boolean verifyInputs() {
        boolean flag1 = title.getText().isEmpty();
        boolean flag2 = startTime.valueProperty().get().toString().isEmpty() || endTime.valueProperty().get().toString().isEmpty();
        boolean flag3 = true;
        boolean flag4 = customerTbl.getSelectionModel().isEmpty();
        if (!flag2) {
            flag3 = !startTime.valueProperty().get().isBefore(endTime.valueProperty().get());
        }
        boolean flag5 = !linkedDateAppointments.filtered(appointment -> TimeUtils.overlaps(startTime.getValue().toLocalTime(), endTime.getValue().toLocalTime(),
                appointment.getStart().toLocalTime(), appointment.getEnd().toLocalTime())).isEmpty();
        
        return !(flag1 || flag2 || flag3 || flag4 || flag5);
    }
    
    private void closeScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserControlScreen.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        Stage currentStage = (Stage) title.getScene().getWindow();
        currentStage.setScene(newScene);
    }
    
    private void showAlert(Alert.AlertType alertType, String key, String title) {
        Alert alert = new Alert(alertType, alertResources.getString(key));
        alert.setTitle(title);
        alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> alert.hide());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        alertResources = ResourceBundle.getBundle("localization.Login", currentLocale);
        StringConverter<OffsetTime> OffsetTimeConverter = new OffsetTimeConverter();
        appointmentDAO = new AppointmentDAO();
        customerDAO = new CustomerDAO();
        allCustomers = customerDAO.getAll();
        
        customerTbl.setItems(allCustomers);
        customerId.setCellValueFactory(new PropertyValueFactory("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory("customerName"));
       
        startTime.setItems(appointmentTimes.localAppTimes);
        startTime.setConverter(OffsetTimeConverter);
        endTime.setItems(appointmentTimes.localAppTimes);
        endTime.setConverter(OffsetTimeConverter);
        
        start.valueProperty().addListener((obs, oldDate, newDate) -> {
            linkedDateAppointments = appointmentDAO.getAll().filtered(appointment -> appointment.getStart().toLocalDate().equals(newDate));
        });
    }    
    
}
