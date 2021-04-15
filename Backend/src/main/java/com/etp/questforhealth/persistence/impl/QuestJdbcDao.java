package com.etp.questforhealth.persistence.impl;

import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.persistence.DBConfigProperties;
import com.etp.questforhealth.persistence.QuestDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.time.format.DateTimeParseException;

@Repository
public class QuestJdbcDao implements QuestDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static Connection questForHealthConn = null;


    @Override
    public Quest getOneById(int id) throws NotFoundException {
        LOGGER.trace("getOneById({})",id);
        makeJDBCConnection();
        try {
            String query = "SELECT * FROM quest WHERE id = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, id);
            ResultSet rs = pstmnt.executeQuery();
            if (rs == null || !rs.next()) throw new NotFoundException("Could not find quest with id " + id);
            return mapRow(rs);

        } catch (SQLException e) {
            System.out.println("MySQL Connection Failed!");
            e.printStackTrace();
        }
        return null;
    }

    private Quest mapRow(ResultSet rs) throws SQLException {
        LOGGER.trace("mapRow({})", rs);
        final Quest quest = new Quest();
        quest.setId(rs.getInt("id"));
        quest.setName(rs.getString("name"));
        quest.setDescription(rs.getString("description"));
        quest.setExp_reward(rs.getInt("exp_reward"));
        quest.setGold_reward(rs.getInt("gold_reward"));
        String mysqlTime = rs.getString("repetition_cycle");
        if (mysqlTime == null) mysqlTime = "00:00:00";
        quest.setRepetition_cycle(parseRepetitionCycle(mysqlTime));
        return quest;
    }

    /**
     * All Java Libraries refuse to parse MySQL Time because it accepts
     * vales greater than 23:59:59 which is not a valid time.
     * java.time.Duration only accepts time in ISO-8601 format.
     *
     * @param sqlTime The String to parse in MySQL Time format
     * @return a java.time.Duration Object with the period
     */
    private Duration parseRepetitionCycle(String sqlTime) throws DateTimeParseException {
        LOGGER.trace("parseRepetitionCycle({})",sqlTime);
        int index1 = sqlTime.indexOf(':');
        int index2 = sqlTime.lastIndexOf(':');
        String hours = sqlTime.substring(0, index1);
        String minutes = sqlTime.substring(index1 + 1, index2);
        String seconds = sqlTime.substring(index2 + 1, sqlTime.length());
        String parseString = "PT" + hours + "H" + minutes + "M" + seconds + "S";
        Duration d = Duration.parse(parseString);
        return d;
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
