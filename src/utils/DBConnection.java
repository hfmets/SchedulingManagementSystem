/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;


import com.mysql.cj.jdbc.MysqlDataSource;
import javax.sql.DataSource;

/**
 *
 * @author longh
 */

// This class is instantiated once when the app is started and made static. The static variable is used then on out by
// using the DBConnections DataSource value to get connections to the database
public class DBConnection {
    private final String protocol;
    private final String vendor;
    private final String serverName;
    private final String dbName;
    private final String userName;
    private final String password;
    
    public DataSource ds;
    
    public DBConnection(String protocol, String vendor, String serverName, String dbName, String userName, String password) {
        MysqlDataSource tempDS = new MysqlDataSource();
        this.protocol = protocol;
        this.vendor = vendor;
        tempDS.setServerName(serverName);
        tempDS.setDatabaseName(dbName);
        tempDS.setUser(userName);
        tempDS.setPassword(password);
        this.ds = (DataSource) tempDS;
        this.serverName = serverName;
        this.dbName = dbName;
        this.userName = userName;
        this.password = password;
    }
}
