/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import static schedulingmanagementsystem.SchedulingManagementSystem.dbConnector;
import utils.TimeUtils;

/**
 *
 * @author longh
 */

// This is the DAO implementation for the Customer type and most of the DAO implementations implement the same methods out
// of neccessity but some did not need to implement every single one therefore they did not implement the DAO interface.
// However, this is where the most use of try catch and try with resources will be found as it is heavily connected with the database.
public class CustomerDAO implements DAO<Customer> {
    
    final String GET_ALL = "SELECT * FROM customer";
    final String INSERT = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                          "VALUES (?, ?, 1, ?, ?, ?, ?)";
    final String DELETE = "DELETE FROM customer WHERE customerId = ?";
    final String UPDATE = "UPDATE customer SET customerName = ?, lastUpdate = ?, lastUpdateBy = ? WHERE customerId = ?";

    @Override
    public ObservableList<Customer> getAll() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try (Connection connection = dbConnector.ds.getConnection();
            PreparedStatement prep = connection.prepareStatement(GET_ALL);
            ResultSet rs = prep.executeQuery()) {
                while (rs.next()) {
                    customerList.add(new Customer(rs.getInt("customerId"), rs.getInt("addressId"), rs.getString("customerName"), rs.getInt("active") == 1,
                    TimeUtils.sqlTimestampToZonedTime(rs.getTimestamp("createDate")), TimeUtils.sqlTimestampToZonedTime(rs.getTimestamp("lastUpdate")),
                    rs.getString("createdBy"), rs.getString("lastUpdateBy")));
                }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return customerList;
    }

    @Override
    public boolean create(Customer obj) {
        boolean created = false;
        try (Connection connection = dbConnector.ds.getConnection();
            PreparedStatement prep = connection.prepareStatement(INSERT)) {
            prep.setString(1, obj.getCustomerName());
            prep.setInt(2, obj.getAddressId());
            prep.setTimestamp(3, TimeUtils.zonedTimeToSqlDate(obj.getCreateDate()));
            prep.setString(4, obj.getCreatedBy());
            prep.setTimestamp(5, TimeUtils.zonedTimeToSqlDate(obj.getLastUpdate()));
            prep.setString(6, obj.getLastUpdateBy());
            created = prep.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return created;
    }

    @Override
    public boolean update(Customer obj) {
        boolean updated = false;
        try (Connection connection = dbConnector.ds.getConnection();
            PreparedStatement prep = connection.prepareStatement(UPDATE)) {
            prep.setString(1, obj.getCustomerName());
            prep.setTimestamp(2, TimeUtils.zonedTimeToSqlDate(obj.getLastUpdate()));
            prep.setString(3, obj.getLastUpdateBy());
            prep.setInt(4, obj.getCustomerId());
            updated = prep.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return updated;
    }

    @Override
    public boolean delete(int id) {
        boolean deleted = false;
        try (Connection connection = dbConnector.ds.getConnection();
            PreparedStatement prep = connection.prepareStatement(DELETE)) {
            prep.setInt(1, id);
            deleted = prep.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return deleted;
    }
    
}
