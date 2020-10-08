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
import model.Appointment;
import static schedulingmanagementsystem.SchedulingManagementSystem.dbConnector;
import utils.TimeUtils;

/**
 *
 * @author longh
 */

// This is the DAO implementation for the Appointment type and most of the DAO implementations implement the same methods out
// of neccessity but some did not need to implement every single one therefore they did not implement the DAO interface.
// However, this is where the most use of try catch and try with resources will be found as it is heavily connected with the database.
public class AppointmentDAO implements DAO<Appointment> {
    
    final String GET_ALL = "SELECT * FROM appointment";
    final String INSERT = "INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, " +
                          "createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    final String DELETE = "DELETE FROM appointment WHERE appointmentId = ?";
    final String UPDATE = "UPDATE appointment SET customerId = ?, title = ?, description = ?, location = ?, contact = ?, type = ?, url = ?, start = ?, " +
                          "end = ?, lastUpdate = ?, lastUpdateBy = ? WHERE appointmentId = ?";

    
    @Override
    public ObservableList<Appointment> getAll() {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try (Connection connection = dbConnector.ds.getConnection();
            PreparedStatement prep = connection.prepareStatement(GET_ALL);
            ResultSet rs = prep.executeQuery()) {
                while(rs.next()) {
                    appointmentList.add(new Appointment(rs.getInt("appointmentId"), rs.getInt("customerId"), rs.getInt("userId"), rs.getString("title"),
                    rs.getString("description"), rs.getString("location"), rs.getString("contact"), rs.getString("type"), rs.getString("url"),
                    TimeUtils.sqlTimestampToZonedTime(rs.getTimestamp("start")), TimeUtils.sqlTimestampToZonedTime(rs.getTimestamp("end")),
                    TimeUtils.sqlTimestampToZonedTime(rs.getTimestamp("createDate")),
                    TimeUtils.sqlTimestampToZonedTime(rs.getTimestamp("lastUpdate")), rs.getString("createdBy"), rs.getString("lastUpdateBy")));
                }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return appointmentList;
    }

    @Override
    public boolean create(Appointment obj) {
        boolean created = false;
        try (Connection connection = dbConnector.ds.getConnection();
            PreparedStatement prep = connection.prepareStatement(INSERT)) {
            prep.setInt(1, obj.getCustomerId());
            prep.setInt(2, obj.getUserId());
            prep.setString(3, obj.getTitle());
            prep.setString(4, obj.getDescription());
            prep.setString(5, obj.getLocation());
            prep.setString(6, obj.getContact());
            prep.setString(7, obj.getType());
            prep.setString(8, obj.getUrl());
            prep.setTimestamp(9, TimeUtils.zonedTimeToSqlDate(obj.getStart()));
            prep.setTimestamp(10, TimeUtils.zonedTimeToSqlDate(obj.getEnd()));
            prep.setTimestamp(11, TimeUtils.zonedTimeToSqlDate(obj.getCreateDate()));
            prep.setString(12, obj.getCreatedBy());
            prep.setTimestamp(13, TimeUtils.zonedTimeToSqlDate(obj.getLastUpdate()));
            prep.setString(14, obj.getLastUpdateBy());
            created = prep.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return created;
    }

    @Override
    public boolean update(Appointment obj) {
        boolean updated = false;
        try (Connection connection = dbConnector.ds.getConnection();
            PreparedStatement prep = connection.prepareStatement(UPDATE)) {
            prep.setInt(1, obj.getCustomerId());
            prep.setString(2, obj.getTitle());
            prep.setString(3, obj.getDescription());
            prep.setString(4, obj.getLocation());
            prep.setString(5, obj.getContact());
            prep.setString(6, obj.getType());
            prep.setString(7, obj.getUrl());
            prep.setTimestamp(8, TimeUtils.zonedTimeToSqlDate(obj.getStart()));
            prep.setTimestamp(9, TimeUtils.zonedTimeToSqlDate(obj.getEnd()));
            prep.setTimestamp(10, TimeUtils.zonedTimeToSqlDate(obj.getLastUpdate()));
            prep.setString(11, obj.getLastUpdateBy());
            prep.setInt(12, obj.getAppointmentId());
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
