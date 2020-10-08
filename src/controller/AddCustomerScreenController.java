/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.AddressDAO;
import DAO.CityDAO;
import DAO.CustomerDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Address;
import model.City;
import model.Customer;
import static schedulingmanagementsystem.SchedulingManagementSystem.currentLocale;
import static schedulingmanagementsystem.SchedulingManagementSystem.currentUser;
import static schedulingmanagementsystem.SchedulingManagementSystem.dbConnector;
import utils.CityConverter;

/**
 * FXML Controller class
 *
 * @author longh
 */

// This class controls the screen for adding a new customer and shows another form of error control
// which is the try catch
public class AddCustomerScreenController implements Initializable {
    
    ObservableList<TextField> textFieldList;
    ObservableList<ChoiceBox> choiceBoxList;
    
    ResourceBundle alertResources;
    
    @FXML
    private TextField fName;
    
    @FXML
    private TextField lName;
    
    @FXML
    private TextField address;
    
    @FXML
    private ChoiceBox<City> city;
    
    @FXML
    private TextField postCode;
    
    @FXML
    private TextField phone;
    
    @FXML
    private Button add;
    
    @FXML
    private Button cancel;
    
    @FXML
    private void add(ActionEvent event) throws IOException {
        if (verifyInputs()) {
            Address newAddress = new Address(0, city.getValue().getCityId(), address.getText(), postCode.getText(), phone.getText(),
                                             ZonedDateTime.now(ZoneId.systemDefault()), ZonedDateTime.now(ZoneId.systemDefault()),
                                             currentUser, currentUser);
            AddressDAO addressDAO = new AddressDAO();
            addressDAO.create(newAddress);
            int newAddressId = getNewAddressId();
            Customer newCustomer = new Customer(0, newAddressId, fName.getText() + " " + lName.getText(), true, ZonedDateTime.now(ZoneId.systemDefault()),
                                                ZonedDateTime.now(ZoneId.systemDefault()), currentUser, currentUser);
            CustomerDAO customerDAO = new CustomerDAO();
            customerDAO.create(newCustomer);
            closeScreen();
        } else {
            showAlert(AlertType.INFORMATION, "emptyfields", "Empty fields present");
        }
    }
    
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        closeScreen();
    }
    
    private boolean verifyInputs() {
        boolean flag1 = textFieldList.stream().map((textField) -> {
                            return textField.getText().isEmpty();
                        }).anyMatch(value -> value == true);
        boolean flag2 = choiceBoxList.stream().map((choiceBox) -> {
                            return choiceBox.valueProperty().toString().isEmpty();
                        }).anyMatch(value -> value == true);
        
        return !(flag1 || flag2);
    }
    
    private int getNewAddressId() {
        int id = 0;
        try (Connection connection = dbConnector.ds.getConnection();
            PreparedStatement prep = connection.prepareStatement("SELECT addressId FROM address WHERE address = ?")) {
                prep.setString(1, address.getText());
                ResultSet rs = prep.executeQuery();
                while (rs.next()) {
                    id = rs.getInt("addressId");
                }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return id;
    }
    
    private void showAlert(Alert.AlertType alertType, String key, String title) {
        Alert alert = new Alert(alertType, alertResources.getString(key));
        alert.setTitle(title);
        alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> alert.hide());
    }
    
    private void closeScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserControlScreen.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        Stage currentStage = (Stage) fName.getScene().getWindow();
        currentStage.setScene(newScene);
    }
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CityDAO cityDAO = new CityDAO();
        city.setItems(cityDAO.getAll());
        StringConverter<City> CityConverter = new CityConverter();
        city.setConverter(CityConverter);
        
        
        textFieldList = FXCollections.observableArrayList(fName, lName, address, postCode, phone);
        choiceBoxList = FXCollections.observableArrayList(city);
        
        alertResources = ResourceBundle.getBundle("localization.Login", currentLocale);
    }    
    
}
