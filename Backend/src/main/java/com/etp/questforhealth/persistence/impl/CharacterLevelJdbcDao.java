package com.etp.questforhealth.persistence.impl;

import com.etp.questforhealth.entity.CharacterLevel;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.persistence.CharacterLevelDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.*;

@Repository
public class CharacterLevelJdbcDao implements CharacterLevelDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static Connection questForHealthConn = null;
    static String TABLE_NAME = "character_level";
    @Value(value = "${spring.datasource.url}")
    String URL;
    @Value("${spring.datasource.username}")
    String USERNAME;
    @Value("${spring.datasource.password}")
    String PASSWORD;
    @Value("${spring.datasource.autoCommit}")
    String AUTOCOMMIT;
    @Override
    public CharacterLevel getCharacterLevelById(int id) {
        LOGGER.trace("getCharacterLevelById({})", id);
        makeJDBCConnection();
        try{
            String query = "SELECT * FROM " +TABLE_NAME + " WHERE id = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, id);
            ResultSet rs = pstmnt.executeQuery();
            if (rs != null && rs.next()) return mapRow(rs);
        } catch (SQLException e){
            throw new PersistenceException(e.getMessage(),e);
        }
        throw new NotFoundException("Could not find level with id  " + id +" in the database.");
    }

    @Override
    public CharacterLevel getCharacterLevelByLevel(int id) {
        LOGGER.trace("getCharacterLevelByLevel({})", id);
        makeJDBCConnection();
        try{
            String query = "SELECT * FROM " +TABLE_NAME + " WHERE level = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, id);
            ResultSet rs = pstmnt.executeQuery();
            if (rs != null && rs.next()) return mapRow(rs);
        } catch (SQLException e){
            throw new PersistenceException(e.getMessage(),e);
        }
        throw new NotFoundException("Could not find level " + id +" in the database.");
    }

    @Override
    public CharacterLevel getCharacterLevelByExp(int exp) {
        LOGGER.trace("getCharacterLevelByExp({})", exp);
        makeJDBCConnection();
        try{
            String query = "select * from character_level where needed_exp <= ? " +
                    "order by level desc LIMIT 1 ;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, exp);
            ResultSet rs = pstmnt.executeQuery();
            if (rs != null && rs.next()) return mapRow(rs);
        } catch (SQLException e){
            throw new PersistenceException(e.getMessage(),e);
        }
        throw new NotFoundException("Could not find level with " + exp + " or less exp in the database.");
    }

    private CharacterLevel mapRow(ResultSet rs) throws SQLException {
        LOGGER.trace("mapRow({})", rs);
        return new CharacterLevel(rs.getInt("id"),
                rs.getInt("total_strength"),
                rs.getInt("needed_exp"),
                rs.getInt("level"),
                rs.getString("rank")
        );
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
