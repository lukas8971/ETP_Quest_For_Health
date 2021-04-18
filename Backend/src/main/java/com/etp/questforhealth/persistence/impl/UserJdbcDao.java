package com.etp.questforhealth.persistence.impl;

import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.persistence.DBConfigProperties;
import com.etp.questforhealth.persistence.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserJdbcDao implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static Connection questForHealthConn = null;

    @Value("${spring.datasource.url}")
    String URL;
    @Value("${spring.datasource.username}")
    String USERNAME;
    @Value("${spring.datasource.password}")
    String PASSWORD;

    @Override
    public List<User> getAll() {
        LOGGER.trace("getAll()");
        makeJDBCConnection();
        List<User> users = new ArrayList<>();
        try{
            String query = "Select * from user";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            ResultSet rs = pstmnt.executeQuery();
            while (rs.next()){
                users.add(new User(rs.getInt("Id"), rs.getString("Firstname"), rs.getString("Lastname")));
            }
        }catch (SQLException e){
            System.out.println("MySQL Connection Failed!");
            e.printStackTrace();
        }
        return users;
    }

    private void makeJDBCConnection() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Congrats - Seems your MySQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            System.out.println("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
            e.printStackTrace();
            return;
        }

        try {
            // DriverManager: The basic service for managing a set of JDBC drivers.
            questForHealthConn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (questForHealthConn != null) {
                System.out.println("Connection Successful! Enjoy. Now it's time to push data");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.out.println("MySQL Connection Failed!");
            e.printStackTrace();
            return;
        }
    }
}
