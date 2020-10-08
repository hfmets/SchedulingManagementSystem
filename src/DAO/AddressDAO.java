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
import model.Address;
import static schedulingmanagementsystem.SchedulingManagementSystem.dbConnector;
import utils.TimeUtils;

/**
 *
 * @author longh
 */

// This is the DAO implementation for the Address type and most of the DAO implementations implement the same methods out
// of neccessity but some did not need to implement every single one therefore they did not implement the DAO interface.
// However, this is where the most use of try catch and try with resources will be found as it is heavily connected with the database.
public class AddressDAO  {
    
    final String GET = "SELECT * FROM address WHERE addressId = ?";
    final String INSERT = "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                          "VALUES (?, '', ?, ?, ?, ?, ?, ?, ?)";
    final String UPDATE = "UPDATE address SET address = ?, cityId = ?, postalCode = ?, phone = ?, lastUpdate = ?, lastUpdateBy = ? WHERE addressId = ?";

    public Address getById(int id) {
        Address newAddress = null;
        try (Connection connection = dbConnector.ds.getConnection();
            PreparedStatement prep = connection.prepareStatement(GET)) {
                prep.setInt(1, id);
                ResultSet rs = prep.executeQuery();
                if (rs.next()) {
                    newAddress = new Address(rs.getInt("addressId"), rs.getInt("cityId"), rs.getString("address"), rs.getString("postalCode"), rs.getString("phone"),
                    TimeUtils.sqlTimestampToZonedTime(rs.getTimestamp("createDate")), TimeUtils.sqlTimestampToZonedTime(rs.getTimestamp("lastUpdate")),
                    rs.getString("createdBy"), rs.getString("lastUpdateBy"));
                }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return newAddress;
    }

    public boolean create(Address obj) {
        boolean created = false;
        try (Connection connection = dbConnector.ds.getConnection();
            PreparedStatement prep = connection.prepareStatement(INSERT)) {
            prep.setString(1, obj.getAddress());
            prep.setInt(2, obj.getCityId());
            prep.setString(3, obj.getPostalCode());
            prep.setString(4, obj.getPhone());
            prep.setTimestamp(5, TimeUtils.zonedTimeToSqlDate(obj.getCreateDate()));
            prep.setString(6, obj.getCreatedBy());
            prep.setTimestamp(7, TimeUtils.zonedTimeToSqlDate(obj.getLastUpdate()));
            prep.setString(8, obj.getLastUpdateBy());
            created = prep.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return created;
    }
    
    public boolean update(Address obj) {
        boolean updated = false;
        try (Connection connection = dbConnector.ds.getConnection();
            PreparedStatement prep = connection.prepareStatement(UPDATE)) {
            prep.setString(1, obj.getAddress());
            prep.setInt(2, obj.getCityId());
            prep.setString(3, obj.getPostalCode());
            prep.setString(4, obj.getPhone());
            prep.setTimestamp(5, TimeUtils.zonedTimeToSqlDate(obj.getLastUpdate()));
            prep.setString(6, obj.getLastUpdateBy());
            prep.setInt(7, obj.getAddressId());
            updated = prep.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return updated;
    }
}
