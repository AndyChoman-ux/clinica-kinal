package main.java.com.vyorg.clinica.kinal0.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static Connection connection;
    
    private DatabaseConnection(){
        
    }
    
    public static Connection getDatabaseConnection() throws SQLException{
        if (connection == null || connection.isClosed()){
            connection= DriverManager.getConnection(Credentials.URL_DB,Credentials.USER_DB,Credentials.PASS_DB);
        }
    return connection;
    }
    
    
    
}
