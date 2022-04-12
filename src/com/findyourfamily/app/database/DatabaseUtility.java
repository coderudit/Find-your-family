package com.findyourfamily.app.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton type for getting and closing database connection.
 */
public class DatabaseUtility {
    //Stores the connection with the database
    private static Connection connection = null;

    //Constant for storing url for the connection.
    private static final String fileName = "config.properties";

    //Constant for storing url property for the connection.
    private static final String urlProperty = "url";

    //Constant for storing username property for the connection.
    private static final String userProperty = "user";

    //Constant for storing password property for the connection.
    private static final String passwordProperty = "password";

    private DatabaseUtility() {
    }

    /**
     * Gets the connection for a database. Creates the connection only 1 time and reuse it.
     *
     * @return database connection
     */
    public static Connection getConnection() {
        //Used for getting credentials from config.properties file.
        var properties = new Properties();

        InputStream inputStream;
        try {

            //Loads the jdbc driver
            var driver = Class.forName("com.mysql.cj.jdbc.Driver");

            //Creates a new connection when there is no existing connection
            if (connection == null) {
                inputStream = new FileInputStream(fileName);
                properties.load(inputStream);
                connection = DriverManager.getConnection(properties.getProperty(urlProperty),
                        properties.getProperty(userProperty), properties.getProperty(passwordProperty));
            }

        } catch (ClassNotFoundException | SQLException | FileNotFoundException exception) {
            System.out.println("Error while creating connection: " + exception.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * Closes the database connection.
     *
     * @return true if connection was closed.
     */
    public static boolean closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException exception) {
                System.out.println("Error while closing connection: " + exception.getMessage());
                return false;
            }
        }
        return true;
    }

}
