package com.etp.questforhealth.persistence.impl;

import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.persistence.DBConfigProperties;
import com.etp.questforhealth.persistence.UserDao;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserJdbcDao implements UserDao {

    static Connection questForHealthConn = null;

    @Override
    public List<User> getAll() {
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

    private static void makeJDBCConnection() {

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
            questForHealthConn = DriverManager.getConnection(DBConfigProperties.URL, DBConfigProperties.USERNAME, DBConfigProperties.PASSWORD);
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