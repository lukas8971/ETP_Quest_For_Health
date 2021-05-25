package com.etp.questforhealth.persistence.impl;

import com.etp.questforhealth.entity.Credentials;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.InvalidLoginException;
import com.etp.questforhealth.exception.NotEnoughGoldException;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.persistence.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
                users.add(mapRow(rs));
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
            String query = "Insert into " + TABLE_NAME + " (firstname, lastname, character_name, password, email, story_chapter, character_level,character_strength, character_exp, character_gold)" +
                    " values (?,?,?,?,?,?,?,?,?,?)";
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
            pstmtnt.setInt(10, user.getCharacterGold());


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

    @Override
    public List<User> getAllNotUsersFromDoctor(int doctor){
        LOGGER.trace("getAllNotUsersFromDoctor({})", doctor);
        makeJDBCConnection();
        try {
            String query = "SELECT * FROM user WHERE id NOT IN (SELECT dhp.user AS id FROM doctor_has_patients dhp WHERE dhp.doctor=?);";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, doctor);
            ResultSet rs = pstmnt.executeQuery();
            if (rs == null) return null;
            List<User> users = new ArrayList<>();
            while(rs.next()){
                users.add(mapRow(rs));
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
            if (rs != null && rs.next()) return mapRow(rs);
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
            if (rs != null && rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
        throw new InvalidLoginException("Wrong username or password");
    }

    @Override
    public boolean completeQuest(int userId, int questId, boolean complete, LocalDate completed_on) {
        LOGGER.trace("completeQuest({},{})", userId,questId);
        makeJDBCConnection();
        try {
            String query ="insert into user_completed_quest(user,quest,completed_on,completed) "+
                    "values (?,?,?,?)";
            PreparedStatement preparedStatement = questForHealthConn.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2,questId);
            preparedStatement.setDate(3,java.sql.Date.valueOf(completed_on));
            preparedStatement.setBoolean(4,complete);
            int val  = preparedStatement.executeUpdate();
            return val > 0;
        } catch (SQLException e){
            throw new PersistenceException(e.getMessage(),e);
        }
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

    @Override
    public boolean updateUser(User user) {
        LOGGER.trace("updateUser({})", user.toString());
        makeJDBCConnection();
        try{
            String query = "update user set firstname = ?, lastname = ?, character_name = ?, character_strength = ?, character_level = ?, character_exp = ?, password =?, email= ?, story_chapter= ?, character_gold= ? where id = ?;";

            PreparedStatement preparedStatement = questForHealthConn.prepareStatement(query);
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getCharacterName());
            preparedStatement.setInt(4, user.getCharacterStrength());
            preparedStatement.setInt(5, user.getCharacterLevel());
            preparedStatement.setInt(6,user.getCharacterExp());
            preparedStatement.setString(7,user.getPassword());
            preparedStatement.setString(8, user.getEmail());
            preparedStatement.setInt(9, user.getStoryChapter());
            preparedStatement.setInt(10, user.getCharacterGold());
            preparedStatement.setInt(11, user.getId());
            int val = preparedStatement.executeUpdate();
            return val >0;
        } catch (SQLException e){
            throw new PersistenceException(e.getMessage(),e);
        }
    }

    @Override
    public User changeUserGoldAndExp(User user, int expChange, int goldChange) {
        LOGGER.trace("changeUserGoldAndExp({},{},{})", user.toString(), expChange,goldChange);
        makeJDBCConnection();
        try{
            String query = "UPDATE user SET character_exp = character_exp + ?, character_gold = character_gold + ? " +
                    "WHERE id = ?;";
            PreparedStatement pstmnt= questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, expChange);
            pstmnt.setInt(2, goldChange);
            pstmnt.setInt(3,user.getId());
            int val = pstmnt.executeUpdate();
            if (val <=0) throw new SQLException("Update: No rows altered!");
            user.setCharacterExp(user.getCharacterExp()+expChange);
            user.setCharacterGold(user.getCharacterGold()+goldChange);
            return user;
        } catch (SQLException e){
            throw new PersistenceException(e.getMessage(),e);
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        LOGGER.trace("mapRow({})", rs);
        return new User(rs.getInt("id"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("character_name"),
                rs.getInt("character_strength"),
                rs.getInt("character_level"),
                rs.getInt("character_exp"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getInt("story_chapter"),
                rs.getInt("character_gold")
        );
    }

    @Override
    public boolean changeUserGold(int id, int changeValue){
        LOGGER.trace("changeUserGold({}, {})", id, changeValue);
        makeJDBCConnection();
        try{
            User u = getOneById(id);
            int newGold = u.getCharacterGold() + changeValue;
            if (newGold < 0) throw new NotEnoughGoldException("The amount of gold can't get negative!");
            String sql = "UPDATE " + TABLE_NAME + " SET character_gold = ? WHERE id = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmnt.setInt(1, newGold);
            pstmnt.setInt(2, id);
            int rowsAffected = pstmnt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
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
