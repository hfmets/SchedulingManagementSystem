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

// This class controls the screen that updates appointments.
public class UpdateAppointmentScreenController implements Initializable {
    
    ObservableList<Customer> allCustomers;
    ResourceBundle alertResources;
    CustomerDAO customerDAO;
    AppointmentDAO appointmentDAO;
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
    private Button update;
    
    private Appointment toBeUpdated;
    
    @FXML
    private void update(ActionEvent event) throws IOException {
        if (verifyInputs()) {
            Appointment newAppointment = new Appointment(toBeUpdated.getAppointmentId(), customerTbl.getSelectionModel().getSelectedItem().getCustomerId(), currentUserId,
            title.getText(), description.getText().isEmpty() ? "not needed" : description.getText(),
            location.getText().isEmpty() ? "not needed" : location.getText(), contact.getText().isEmpty() ? "not needed" : contact.getText(),
            type.getText().isEmpty() ? "not needed" : type.getText(), url.getText().isEmpty() ? "not needed" : url.getText(),
            ZonedDateTime.of(start.getValue(), startTime.valueProperty().get().toLocalTime(), ZoneId.of(startTime.valueProperty().get().getOffset().getId())),
            ZonedDateTime.of(start.getValue(), endTime.valueProperty().get().toLocalTime(), ZoneId.of(endTime.valueProperty().get().getOffset().getId())));
            appointmentDAO.update(newAppointment);
            closeScreen();
        } else {
            showAlert(Alert.AlertType.INFORMATION, "appscreen", "Possible errors with new appointment");
        }
    }
    
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        closeScreen();
    }
    
    public void loadAppointment(Appointment toBeUpdated) {
        this.toBeUpdated = toBeUpdated;
        title.setText(toBeUpdated.getTitle());
        location.setText(toBeUpdated.getLocation());
        type.setText(toBeUpdated.getType());
        contact.setText(toBeUpdated.getContact());
        url.setText(toBeUpdated.getUrl());
        description.setText(toBeUpdated.getDescription());
        customerTbl.getSelectionModel().select(allCustomers.filtered(customer -> customer.getCustomerId() == toBeUpdated.getCustomerId()).get(0));
        start.setValue(toBeUpdated.getStart().toLocalDate());
        startTime.setValue(OffsetTime.of(toBeUpdated.getStart().toLocalTime(), toBeUpdated.getStart().getOffset()));
        endTime.setValue(OffsetTime.of(toBeUpdated.getEnd().toLocalTime(), toBeUpdated.getEnd().getOffset()));
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
        customerDAO = new CustomerDAO();
        appointmentDAO = new AppointmentDAO();
        
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
