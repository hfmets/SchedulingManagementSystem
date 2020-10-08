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
import model.City;
import static schedulingmanagementsystem.SchedulingManagementSystem.dbConnector;
import utils.TimeUtils;

/**
 *
 * @author longh
 */

// This is the DAO implementation for the City type and most of the DAO implementations implement the same methods out
// of neccessity but some did not need to implement every single one therefore they did not implement the DAO interface.
// However, this is where the most use of try catch and try with resources will be found as it is heavily connected with the database.
public class CityDAO {
    public ObservableList<City> getAll(){
        ObservableList<City> cityList = FXCollections.observableArrayList();
        try (Connection connection = dbConnector.ds.getConnection();
            PreparedStatement prep = connection.prepareStatement("SELECT * FROM city");
            ResultSet rs = prep.executeQuery()) {
                while (rs.next()) {
                    cityList.add(new City(rs.getInt("cityId"), rs.getInt("countryId"), rs.getString("city"),
                    TimeUtils.sqlTimestampToZonedTime(rs.getTimestamp("createDate")),
                    TimeUtils.sqlTimestampToZonedTime(rs.getTimestamp("lastUpdate")),
                    rs.getString("createdBy"), rs.getString("lastUpdateBy")));
                }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return cityList;
    }
}
