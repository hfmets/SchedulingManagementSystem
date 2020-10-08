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
import model.Country;
import static schedulingmanagementsystem.SchedulingManagementSystem.dbConnector;
import utils.TimeUtils;

/**
 *
 * @author longh
 */

// This is the DAO implementation for the Country type and most of the DAO implementations implement the same methods out
// of neccessity but some did not need to implement every single one therefore they did not implement the DAO interface.
// However, this is where the most use of try catch and try with resources will be found as it is heavily connected with the database.
public class CountryDAO {
    public ObservableList<Country> getAll(){
        ObservableList<Country> countryList = FXCollections.observableArrayList();
        try (Connection connection = dbConnector.ds.getConnection();
            PreparedStatement prep = connection.prepareStatement("SELECT * FROM country");
            ResultSet rs = prep.executeQuery()) {
                while (rs.next()) {
                    countryList.add(new Country(rs.getInt("countryId"), rs.getString("country"),
                    TimeUtils.sqlTimestampToZonedTime(rs.getTimestamp("createDate")),
                    TimeUtils.sqlTimestampToZonedTime(rs.getTimestamp("lastUpdate")),
                    rs.getString("createdBy"), rs.getString("lastUpdateBy")));
                }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return countryList;
    }
}
