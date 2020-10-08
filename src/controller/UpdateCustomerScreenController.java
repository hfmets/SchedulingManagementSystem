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
import utils.CityConverter;

/**
 * FXML Controller class
 *
 * @author longh
 */

// This class controls the screen that updates customers.
public class UpdateCustomerScreenController implements Initializable {
    
    AddressDAO addressDAO;
    CustomerDAO customerDAO;
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
    private Button update;
    
    @FXML
    private Button cancel;
    
    private Customer toBeUpdated;
    private Address linkedAddress;
    
    @FXML
    private void update(ActionEvent event) throws IOException {
        if (verifyInputs()) {
            Address newAddress = new Address(linkedAddress.getAddressId(), city.getValue().getCityId(), address.getText(), postCode.getText(), phone.getText(),
                                             linkedAddress.getCreateDate(), ZonedDateTime.now(ZoneId.systemDefault()),
                                             currentUser, currentUser);
            addressDAO.update(newAddress);
            Customer newCustomer = new Customer(toBeUpdated.getCustomerId(), toBeUpdated.getAddressId(), fName.getText() + " " + lName.getText(), true, ZonedDateTime.now(ZoneId.systemDefault()),
                                                ZonedDateTime.now(ZoneId.systemDefault()), currentUser, currentUser);
            customerDAO.update(newCustomer);
            closeScreen();
        } else {
            showAlert(Alert.AlertType.INFORMATION, "emptyfields", "Empty fields present");
        }
    }
    
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        closeScreen();
    }
    
    public void loadCustomer(Customer toBeUpdated) {
        this.toBeUpdated = toBeUpdated;
        this.linkedAddress = addressDAO.getById(toBeUpdated.getAddressId());
        fName.setText(toBeUpdated.getCustomerName().substring(0, toBeUpdated.getCustomerName().indexOf(" ")));
        lName.setText(toBeUpdated.getCustomerName().substring(toBeUpdated.getCustomerName().indexOf(" ") + 1));
        address.setText(linkedAddress.getAddress());
        city.setValue(city.getItems().filtered(values -> values.getCityId() == linkedAddress.getCityId()).get(0));
        postCode.setText(linkedAddress.getPostalCode());
        phone.setText(linkedAddress.getPhone());
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
    
    private void closeScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserControlScreen.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        Stage currentStage = (Stage) fName.getScene().getWindow();
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
        addressDAO = new AddressDAO();
        customerDAO = new CustomerDAO();
        
        CityDAO cityDAO = new CityDAO();
        city.setItems(cityDAO.getAll());
        StringConverter<City> CityConverter = new CityConverter();
        city.setConverter(CityConverter);
        
        textFieldList = FXCollections.observableArrayList(fName, lName, address, postCode, phone);
        choiceBoxList = FXCollections.observableArrayList(city);
        
        alertResources = ResourceBundle.getBundle("localization.Login", currentLocale);
    }    
    
}
