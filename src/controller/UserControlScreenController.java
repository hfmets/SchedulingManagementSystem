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
import java.time.LocalDate;
import java.time.LocalTime;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import static schedulingmanagementsystem.SchedulingManagementSystem.currentUserId;
import utils.Reports;

/**
 * FXML Controller class
 *
 * @author longh
 */

// This class controls the main screen for the app where users can create, update, delete customers and appointments.
// A lot of these methods show a second form of error management that is used throughout the app
// which is the throws clause.
public class UserControlScreenController implements Initializable {
    
    // These are variables needed throughout this class and its various methods that get initialized in the initialize method when 
    // this screen is created
    ObservableList<Customer> allCustomers;
    ObservableList<Appointment> allAppointments;
    CustomerDAO customerDAO;
    AppointmentDAO appointmentDAO;
    
    @FXML
    private Button addCustomerBtn;
    
    @FXML
    private Button addAppointmentBtn;
    
    @FXML
    private Button calendarBtn;
    
    @FXML
    private Button updateCustomerBtn;
    
    @FXML
    private Button deleteCustomerBtn;
    
    @FXML 
    private Button updateAppointmentBtn;
    
    @FXML
    private Button deleteAppointmentBtn;
    
    @FXML
    private Button customerReportBtn;
    
    @FXML
    private Button apptReportBtn;
    
    @FXML
    private Button cityReportBtn;
    
    @FXML
    private TableColumn customerId;
    
    @FXML
    private TableColumn customerName;
    
    @FXML
    private TableColumn appointmentId;
    
    @FXML
    private TableColumn appointmentTitle;
    
    @FXML
    private TableView<Customer> customerTbl;
    
    @FXML
    private TableView<Appointment> appointmentTbl;
    
    // This method is triggered when a user clicks the calendar button and takes them to the calendar screen
    @FXML
    private void calendar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CalendarScreen.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        Stage currentStage = (Stage) calendarBtn.getScene().getWindow();
        currentStage.setScene(newScene);
    }
    
    // This method is triggered when a user clicks either add button and it will determine whether they want to add an appointment or
    // a customer. After that it will bring up the corresponding screen for the user.
    @FXML
    private void add(ActionEvent event) throws IOException {
        FXMLLoader loader;
        Parent root;
        Scene newScene;
        Stage currentStage = (Stage) addCustomerBtn.getScene().getWindow();
        if (event.getSource().equals(addCustomerBtn)){
            loader = new FXMLLoader(getClass().getResource("/view/AddCustomerScreen.fxml"));
            root = loader.load();
            newScene = new Scene(root);
            currentStage.setScene(newScene);
        } else {
            loader = new FXMLLoader(getClass().getResource("/view/AddAppointmentScreen.fxml"));
            root = loader.load();
            newScene = new Scene(root);
            currentStage.setScene(newScene);
        }
    }
    
    // This method checks which delete button was clicked and then makes sure that an item is selected in the corresponding table
    // to delete. If a customer is being deleted, all of the customers appointments are deleted as well.
    @FXML
    private void delete(ActionEvent event) {
        if (event.getSource().equals(deleteCustomerBtn) && !customerTbl.getSelectionModel().isEmpty()) {
            int selectedId = customerTbl.getSelectionModel().getSelectedItem().getCustomerId();
            allAppointments.filtered(appointment -> appointment.getCustomerId() == selectedId).forEach((filtered) -> {
                appointmentDAO.delete(filtered.getAppointmentId());
                //allAppointments.remove(filtered);
            });
            allAppointments.removeAll(allAppointments.filtered(appointment -> appointment.getCustomerId() == selectedId));
            customerDAO.delete(customerTbl.getSelectionModel().getSelectedItem().getCustomerId());
            allCustomers.remove(customerTbl.getSelectionModel().getSelectedItem());
        } else if (event.getSource().equals(deleteAppointmentBtn) && !appointmentTbl.getSelectionModel().isEmpty()) {
            appointmentDAO.delete(appointmentTbl.getSelectionModel().getSelectedItem().getAppointmentId());
            allAppointments.remove(appointmentTbl.getSelectionModel().getSelectedItem());
        }
    }
    
    // This method checks which update button is pressed and then makes sure that the corresponding table has a selected item
    // if it does then it will load the correct update screen with the relevant information
    @FXML
    private void update(ActionEvent event) throws IOException {
        FXMLLoader loader;
        Parent root;
        Scene newScene;
        Stage currentStage = (Stage) updateCustomerBtn.getScene().getWindow();
        if (event.getSource().equals(updateCustomerBtn) && !customerTbl.getSelectionModel().isEmpty()) {
            loader = new FXMLLoader(getClass().getResource("/view/UpdateCustomerScreen.fxml"));
            root = loader.load();
            UpdateCustomerScreenController updateCustomerController = loader.getController();
            updateCustomerController.loadCustomer(customerTbl.getSelectionModel().getSelectedItem());
            
            newScene = new Scene(root);
            
            currentStage.setScene(newScene);
        } else if (event.getSource().equals(updateAppointmentBtn) && !appointmentTbl.getSelectionModel().isEmpty()) {
            loader = new FXMLLoader(getClass().getResource("/view/UpdateAppointmentScreen.fxml"));
            root = loader.load();
            newScene = new Scene(root);
            UpdateAppointmentScreenController updateAppointmentController = loader.getController();
            updateAppointmentController.loadAppointment(appointmentTbl.getSelectionModel().getSelectedItem());
            currentStage.setScene(newScene);
        }
    }
    
    // This method determines which report button was pressed and then calls the specified method to generate the correct report
    @FXML
    private void generate(ActionEvent event) throws IOException {
        if (event.getSource().equals(customerReportBtn)) {
            Reports.customerReport(allCustomers, allAppointments);
        } else if (event.getSource().equals(apptReportBtn)) {
            Reports.appointmentTypeReport(allAppointments);
        } else if (!customerTbl.getSelectionModel().isEmpty() && event.getSource().equals(cityReportBtn)) {
            Reports.matchingCityReport(customerTbl.getSelectionModel().getSelectedItem(), allCustomers);
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerDAO = new CustomerDAO();
        appointmentDAO = new AppointmentDAO();
        allCustomers = customerDAO.getAll();
        allAppointments = appointmentDAO.getAll();
                            
        customerTbl.setItems(allCustomers);
        customerId.setCellValueFactory(new PropertyValueFactory("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory("customerName"));
        
        appointmentTbl.setItems(allAppointments);
        appointmentId.setCellValueFactory(new PropertyValueFactory("appointmentId"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory("title"));
        
        // This code alerts the user if they have an upcoming appointment in the next 15 minutes
        if (allAppointments.stream().filter(appointment -> appointment.getUserId() == currentUserId && appointment.getStart().toLocalDate().equals(LocalDate.now())).filter(appt -> appt.getStart().toLocalTime().toSecondOfDay() - LocalTime.now().toSecondOfDay() < Integer.valueOf(15 * 60)).count() > 0) {
            Alert alert = new Alert(AlertType.INFORMATION, "You have an appointment scheduled sometime in the next 15 minutes.");
            alert.setTitle("Upcoming Appointment");
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> alert.hide());
        }
    }    
    
}
