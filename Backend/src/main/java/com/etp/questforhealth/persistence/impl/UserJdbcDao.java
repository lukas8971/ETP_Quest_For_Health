package com.etp.questforhealth.persistence.impl;

import com.etp.questforhealth.entity.User;
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
    static String TABLE_NAME = "user";
    @Value("${spring.datasource.url}")
    String URL;
    @Value("${spring.datasource.username}")
    String USERNAME;
    @Value("${spring.datasource.password}")
    String PASSWORD;
    @Value("${spring.datasource.autoCommit}")
    String AUTOCOMMIT;

    @Override
    public List<User> getAll() {
        LOGGER.trace("getAll()");
        makeJDBCConnection();
        List<User> users = new ArrayList<>();
        try{
            String query = "Select * from " + TABLE_NAME;
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = pstmnt.executeQuery();
            while (rs.next()){
                users.add(new User(rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("character_name"), rs.getInt("character_strength"), rs.getInt("character_level"), rs.getInt("character_exp"), rs.getString("password"), rs.getString("email"), rs.getInt("story_chapter")));
            }
        }catch (SQLException e){
            System.out.println("MySQL Connection Failed!");
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User createUser(User user) {
        makeJDBCConnection();
        try {
            String query = "Insert into " + TABLE_NAME + " (firstname, lastname, character_name, password, email, story_chapter, character_level,character_strength, character_exp)" +
                    " values (?,?,?,?,?,1,1,0,0)";
            PreparedStatement pstmtnt = questForHealthConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmtnt.setString(1, user.getFirstname());
            pstmtnt.setString(2, user.getLastname());
            pstmtnt.setString(3, user.getCharacterName());
            pstmtnt.setString(4, user.getPassword());
            pstmtnt.setString(5, user.getEmail());

            pstmtnt.executeUpdate();
            ResultSet rs = pstmtnt.getGeneratedKeys();
            rs.next();
            user.setId(rs.getInt(1));
        }
        catch (SQLException e){
            System.out.println("MySQL Connection Failed!");
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void rollbackChanges() {
        try {
            questForHealthConn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void makeJDBCConnection() {
        if (questForHealthConn == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("Congrats - Seems your MySQL JDBC Driver Registered!");
            } catch (ClassNotFoundException e) {
                System.out.println("Sorry, couldn't find JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
                e.printStackTrace();
                return;
            }

            try {
                // DriverManager: The basic service for managing a set of JDBC drivers.
                questForHealthConn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
                if (!(Boolean.parseBoolean(AUTOCOMMIT))) questForHealthConn.setAutoCommit(false);
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
}
