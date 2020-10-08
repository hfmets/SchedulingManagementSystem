/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import DAO.AddressDAO;
import DAO.CityDAO;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;

/**
 *
 * @author longh
 */

// This class houses the three report types and the methods that create them.
public class Reports {
    public static void customerReport(ObservableList<Customer> customers, ObservableList<Appointment> appointments) throws IOException {
        Path newCustReport = Paths.get(".", "reports", "CustomerReport" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM_dd_uuuu_hhmm_a")) + ".txt");
        Files.createFile(newCustReport);
        try (BufferedWriter bw = Files.newBufferedWriter(newCustReport, StandardOpenOption.APPEND)) {
            for (Customer customer : customers) {
                bw.write("Customer ID: " + customer.getCustomerId() + "    Customer Name: " + customer.getCustomerName());
                bw.newLine();
                for (Appointment appointment : appointments.filtered(appointment -> appointment.getCustomerId() == customer.getCustomerId())) {
                    bw.write("\t Appointment: " + appointment.getTitle() + "    Time: " +
                            appointment.getStart().toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/dd/uuuu hh:mm a")) + " - "
                            + appointment.getEnd().toLocalDateTime().format(DateTimeFormatter.ofPattern("hh:mm a")));
                    bw.newLine();
                }
            }
        }
    }
    
    public static void appointmentTypeReport(ObservableList<Appointment> appointments) throws IOException {
        Path newApptTypeReport = Paths.get(".", "reports", "AppointmentTypesReport" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM_dd_uuuu_hhmm_a")) + ".txt");
        Files.createFile(newApptTypeReport);
        try (BufferedWriter bw = Files.newBufferedWriter(newApptTypeReport, StandardOpenOption.APPEND)) {
            for (Month month : Month.values()) {
                bw.write(month.toString());
                bw.newLine();
                bw.write("\t Appointment Types");
                bw.newLine();
                for (Appointment appointment : appointments.filtered(appointment -> appointment.getStart().getMonth().equals(month) && !appointment.getType().equalsIgnoreCase("not needed"))) {
                    bw.write("\t " + appointment.getType());
                    bw.newLine();
                }
            }
        }
    }
    
    public static void matchingCityReport(Customer customer, ObservableList<Customer> customers) throws IOException {
        AddressDAO addressDAO = new AddressDAO();
        CityDAO cityDAO = new CityDAO();
        int cityId = addressDAO.getById(customer.getAddressId()).getCityId();
        String cityName = cityDAO.getAll().filtered(city -> city.getCityId() == cityId).get(0).getCityName();
        
        Path newMatchingCityReport = Paths.get(".", "reports", "MatchingCityReport" + customer.getCustomerName().replace(" ", "") + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM_dd_uuuu_hhmm_a")) + ".txt");;
        Files.createFile(newMatchingCityReport);
        try (BufferedWriter bw = Files.newBufferedWriter(newMatchingCityReport, StandardOpenOption.APPEND)) {
            bw.write(customer.getCustomerName() + " lives in " + cityName + " along with: ");
            bw.newLine();
            for (Customer cust : customers.filtered(cust -> addressDAO.getById(cust.getAddressId()).getCityId() == cityId && !cust.getCustomerName().equals(customer.getCustomerName()))) {
                bw.write("\t " + cust.getCustomerName());
                bw.newLine();
            }
        }
    }
}
