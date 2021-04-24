package com.etp.questforhealth.persistence.impl;

import com.etp.questforhealth.entity.Credentials;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.InvalidLoginException;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;
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
    public User createUser(User user) throws PersistenceException {
        LOGGER.trace("createUser({})", user.toString());
        makeJDBCConnection();
        try {
            String query = "Insert into " + TABLE_NAME + " (firstname, lastname, character_name, password, email, story_chapter, character_level,character_strength, character_exp)" +
                    " values (?,?,?,?,?,?,?,?,?)";
            PreparedStatement pstmtnt = questForHealthConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmtnt.setString(1, user.getFirstname());
            pstmtnt.setString(2, user.getLastname());
            pstmtnt.setString(3, user.getCharacterName());
            pstmtnt.setString(4, user.getPassword());
            pstmtnt.setString(5, user.getEmail());
            pstmtnt.setInt(6, user.getStoryChapter());
            pstmtnt.setInt(7, user.getCharacterLevel());
            pstmtnt.setInt(8, user.getCharacterStrength());
            pstmtnt.setInt(9, user.getCharacterExp());


            pstmtnt.executeUpdate();
            ResultSet rs = pstmtnt.getGeneratedKeys();
            rs.next();
            user.setId(rs.getInt(1));
        }
        catch (SQLException e){
            throw new PersistenceException("Error while inserting user in Database", e);
        }
        return user;
    }

    @Override
    public void rollbackChanges() {
        LOGGER.trace("rollbackChanges()");
        try {
            questForHealthConn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsersFromDoctor(int doctor){
        LOGGER.trace("getAllUsersFromDoctor()");
        makeJDBCConnection();
        try {
            String query = "SELECT u.id AS u_id,u.firstname AS u_firstname,u.lastname AS u_lastname FROM doctor_has_patients dhp JOIN user u ON(dhp.user = u.id) WHERE dhp.doctor=?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, doctor);
            ResultSet rs = pstmnt.executeQuery();
            if (rs == null) return null;
            List<User> users = new ArrayList<>();
            while(rs.next()){
                users.add(mapUsersInTreatment(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    private User mapUsersInTreatment(ResultSet rs) throws SQLException {
        LOGGER.trace("mapUsersInTreatment({})", rs);
        final User user = new User();
        user.setId(rs.getInt("u_id"));
        user.setFirstname(rs.getString("u_firstname"));
        user.setLastname(rs.getString("u_lastname"));
        return user;
    }

    @Override
    public User getOneById(int id) {
        LOGGER.trace("getOneById({})",id);
        makeJDBCConnection();
        try {
            String query = "SELECT * FROM user WHERE id = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, id);
            ResultSet rs = pstmnt.executeQuery();
            if (rs != null && rs.next()) return new User(rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname"));
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
        throw new NotFoundException("Could not find user with id " + id);
    }

    @Override
    public User checkLogin(Credentials cred) {
        LOGGER.trace("checkLogin({})",cred.toString());
        makeJDBCConnection();
        try {
            String query = "SELECT * FROM user WHERE character_name = ? AND password = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setString(1, cred.getEmail() );
            pstmnt.setString(2, cred.getPassword());
            ResultSet rs = pstmnt.executeQuery();
            if (rs != null && rs.next()) return new User(rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("character_name"), rs.getInt("character_strength"), rs.getInt("character_level"), rs.getInt("character_exp"), rs.getString("password"), rs.getString("email"), rs.getInt("story_chapter"));
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
        throw new InvalidLoginException("Wrong username or password");
    }

    @Override
    public boolean checkUserNameExists(String userName) {
        LOGGER.trace("checkUserNameExists({})",userName);
        makeJDBCConnection();
        try {
            String query = "SELECT * FROM user WHERE character_name = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setString(1, userName );
            ResultSet rs = pstmnt.executeQuery();
            if (rs != null && rs.next()) return true;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
        return false;
    }

    public void makeJDBCConnection() {
        LOGGER.trace("makeJDBCConnection()");
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
