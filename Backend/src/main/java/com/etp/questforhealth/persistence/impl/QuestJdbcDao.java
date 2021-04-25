package com.etp.questforhealth.persistence.impl;

import com.etp.questforhealth.entity.AcceptedQuest;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.persistence.QuestDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class QuestJdbcDao implements QuestDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static Connection questForHealthConn = null;
    @Value("${spring.datasource.url}")
    String URL;
    @Value("${spring.datasource.username}")
    String USERNAME;
    @Value("${spring.datasource.password}")
    String PASSWORD;


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

    @Override
    public boolean acceptQuest(int userId, int questId) {
        LOGGER.trace("acceptQuest({},{})", userId,questId);
        makeJDBCConnection();
        try{
            String query = "INSERT INTO user_accepted_quest (user, quest, accepted_on) " +
                    "values (?,?,CURRENT_DATE);";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, userId);
            pstmnt.setInt(2, questId);
            int val = pstmnt.executeUpdate();
            return val >0;
        } catch (SQLException e){
            throw new PersistenceException("Could not assign Quest to user: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Quest> getNewQuestsForUserId(int userId) {
        LOGGER.trace("getNewQuestsForUserId({})", userId);
        makeJDBCConnection();
        List<Quest> questList = new ArrayList<>();
        try{
            String query = "select * from quest q " +
                    "where q.id not in ( " +
                    "  select d.id  " +
                    "  from doctor_quest d) " +
                    "  and q.id not in ( " +
                    "    select u.quest " +
                    "    from user_accepted_quest u " +
                    "    where u.user = ? " +
                    "    );";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, userId);
            ResultSet rs = pstmnt.executeQuery();
            while (rs.next()){
                questList.add(mapRow(rs));
            }
            return questList;
        } catch (SQLException e){
            throw new PersistenceException("Could not fetch new quests for user " + userId, e);
        }
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

    @Override
    public List<Quest> getAllUserAvailableDoctorQuests(int user, int doctor) {
        LOGGER.trace("getAllUserAvailableDoctorQuests({}, {})", user, doctor);
        makeJDBCConnection();
        try {
            String query = "SELECT dq.id AS id, q.name AS name, q.description AS description " +
                    "FROM doctor_quest dq INNER JOIN quest q ON (dq.id = q.id) " +
                    "WHERE dq.doctor = ? AND dq.id NOT IN " +
                    "(SELECT uaq.quest AS id FROM user_accepted_quest uaq " +
                    "WHERE uaq.user = ? AND uaq.quest IN " +
                    "(SELECT d.id AS id FROM doctor_quest d));";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, doctor);
            pstmnt.setInt(2, user);
            ResultSet rs = pstmnt.executeQuery();
            if (rs == null) return null;
            List<Quest> quests = new ArrayList<>();
            while(rs.next()){
                quests.add(mapRowDoctorUserQuest(rs));
            }
            return quests;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Quest> getAllUserAssignedDoctorQuests(int user, int doctor) {
        LOGGER.trace("getAllUserAssignedDoctorQuests({}, {})", user, doctor);
        makeJDBCConnection();
        try {
            String query = "SELECT dq.id AS id, q.name AS name, q.description AS description " +
                    "FROM doctor_quest dq INNER JOIN quest q ON (dq.id = q.id) " +
                    "WHERE dq.doctor = ? AND dq.id IN " +
                    "(SELECT uaq.quest AS id FROM user_accepted_quest uaq " +
                    "WHERE uaq.user = ? AND uaq.quest IN " +
                    "(SELECT d.id AS id FROM doctor_quest d));";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, doctor);
            pstmnt.setInt(2, user);
            ResultSet rs = pstmnt.executeQuery();
            if (rs == null) return null;
            List<Quest> quests = new ArrayList<>();
            while(rs.next()){
                quests.add(mapRowDoctorUserQuest(rs));
            }
            return quests;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    /**
     * A mapping for all doctor user quests (assigned and available)
     * @param rs resultset from the query
     * @return a quest object with id, name and description
     * @throws SQLException if something is wrong
     */
    private Quest mapRowDoctorUserQuest(ResultSet rs) throws SQLException {
        LOGGER.trace("mapRowDoctorUserQuest({})", rs);
        final Quest quest = new Quest();
        quest.setId(rs.getInt("id"));
        quest.setName(rs.getString("name"));
        quest.setDescription(rs.getString("description"));
        return quest;
    }

    @Override
    public boolean deleteAssignedDoctorQuestForUser(int quest, int user){
        LOGGER.trace("deleteAssignedDoctorQuestForUser({}, {})", quest, user);
        makeJDBCConnection();
        try {
            String query = "DELETE FROM user_accepted_quest WHERE quest=? AND user=?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, quest);
            pstmnt.setInt(2, user);
            int val = pstmnt.executeUpdate();
            return val > 0;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean addAssignedDoctorQuestForUser(AcceptedQuest acceptedQuest){
        LOGGER.trace("addAssignedDoctorQuestForUser({})", acceptedQuest);
        makeJDBCConnection();
        try {
            String query = "INSERT INTO user_accepted_quest(quest,user,accepted_on) VALUES (?,?,?);";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, acceptedQuest.getQuest());
            pstmnt.setInt(2, acceptedQuest.getUser());
            pstmnt.setObject(3, acceptedQuest.getAcceptedOn());
            int val = pstmnt.executeUpdate();
            return val > 0;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
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
