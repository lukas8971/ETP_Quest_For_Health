package com.etp.questforhealth.persistence.impl;

import com.etp.questforhealth.entity.AcceptedQuest;
import com.etp.questforhealth.entity.CompletedQuest;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class QuestJdbcDao implements QuestDao {

    private static final String QUEST_TABLE_NAME = "quest";
    private static final String DOCTOR_QUEST_TABLE_NAME = "doctor_quest";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static Connection questForHealthConn = null;
    @Value("${spring.datasource.url}")
    String URL;
    @Value("${spring.datasource.username}")
    String USERNAME;
    @Value("${spring.datasource.password}")
    String PASSWORD;
    @Value("${spring.datasource.autoCommit}")
    String AUTOCOMMIT;



    @Override
    public Quest getOneById(int id) throws NotFoundException {
        LOGGER.trace("getOneById({})", id);
        makeJDBCConnection();

        try {
            final String sql = "SELECT * FROM " + QUEST_TABLE_NAME + " LEFT JOIN doctor_quest ON quest.id = doctor_quest.id WHERE quest.id = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(sql);
            pstmnt.setInt(1, id);
            ResultSet rs = pstmnt.executeQuery();
            if (rs == null || !rs.next()) throw new NotFoundException("Could not find quest with id " + id);
            return mapRow(rs);
        } catch (SQLException e) {
           throw new PersistenceException(e.getMessage(),e);
        }
    }

    @Override
    public Quest createQuest(Quest quest) {
        LOGGER.trace("createQuest({})", quest);
        makeJDBCConnection();
        try {
            final String sql_quest = "INSERT INTO " + QUEST_TABLE_NAME + " (name, description, exp_reward, gold_reward, repetition_cycle) " +
                    " VALUES (?,?,?,?,?);";
            PreparedStatement stmt_quest = questForHealthConn.prepareStatement(sql_quest, Statement.RETURN_GENERATED_KEYS);
            stmt_quest.setString(1, quest.getName());
            stmt_quest.setString(2, quest.getDescription());
            stmt_quest.setInt(3, quest.getExp_reward());
            stmt_quest.setInt(4, quest.getGold_reward());
            stmt_quest.setString(5, parseRepetitionCycle(quest.getRepetition_cycle()));
            int added = stmt_quest.executeUpdate();
            if (added == 0) throw new PersistenceException("Could not add new Quest");
            ResultSet rs = stmt_quest.getGeneratedKeys();
            rs.next();
            quest.setId(rs.getInt(1));

            final String query_doctor_quest = "INSERT INTO " + DOCTOR_QUEST_TABLE_NAME + " (id, doctor, exp_penalty, gold_penalty) " +
                    " VALUES (?,?,?,?);";
            PreparedStatement stmt_doctor_quest = questForHealthConn.prepareStatement(query_doctor_quest, Statement.RETURN_GENERATED_KEYS);
            stmt_doctor_quest.setInt(1, quest.getId());
            stmt_doctor_quest.setInt(2, quest.getDoctor());
            stmt_doctor_quest.setInt(3, quest.getExp_penalty());
            stmt_doctor_quest.setInt(4, quest.getGold_penalty());
            added = stmt_doctor_quest.executeUpdate();
            if (added == 0) throw new PersistenceException("Could not add new Quest");


            return getOneById(quest.getId());
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new PersistenceException("Recently saved quest can't be accessed. \n");

        }
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
            String query = "select * from quest q left join doctor_quest d " +
                    "on q.id = d.id " +
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
        quest.setExp_penalty(rs.getInt("exp_penalty"));
        quest.setGold_penalty(rs.getInt("gold_penalty"));
        quest.setDoctor(rs.getInt("doctor"));
        return quest;
    }

    private Quest mapRowDueQuests (ResultSet rs) throws  SQLException{
        LOGGER.trace("mapRowDueQuests({})", rs);
        final Quest quest = new Quest();
        quest.setId(rs.getInt("id"));
        quest.setName(rs.getString("name"));
        quest.setDescription(rs.getString("description"));
        quest.setExp_reward(rs.getInt("exp_reward"));
        quest.setGold_reward(rs.getInt("gold_reward"));
        String mysqlTime = rs.getString("repetition_cycle");
        if (mysqlTime == null) mysqlTime = "00:00:00";
        quest.setRepetition_cycle(parseRepetitionCycle(mysqlTime));
        quest.setExp_penalty(rs.getInt("exp_penalty"));
        quest.setGold_penalty(rs.getInt("gold_penalty"));
        Date dueDate = rs.getDate("OldDueOn");
        if (dueDate == null) dueDate = rs.getDate("NewDueOn");
        quest.setDueDate(dueDate);
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
        LOGGER.trace("parseRepetitionCycle({})", sqlTime);
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
    public List<Quest> getAllQuestsDueForUser(int userId) {
        LOGGER.trace("getAllQuestsDueForUser({})", userId);
        makeJDBCConnection();
        try {
            List<Quest> questList = new ArrayList<>();
            //HOLY MOLY THATS A BIG QUERY
            String query = "select q.id, q.name, q.description, q.exp_reward, q.gold_reward, d.exp_penalty, d.gold_penalty, q.repetition_cycle, max(ADDTIME(uaq.accepted_on, q.repetition_cycle)) as NewDueOn, max(ADDTIME(ucq.completed_on, q.repetition_cycle)) as OldDueOn from user_accepted_quest uaq " +
                    "inner join quest q on q.id = uaq.quest " +
                    "left join user_completed_quest ucq on q.id = ucq.quest and uaq.user = ucq.user " +
                    "left join doctor_quest d on d.id = q.id " +
                    "where uaq.quest not in( " +
                    "  select ucq.quest from user_completed_quest ucq " +
                    "\tinner join quest q on q.id = ucq.quest " +
                    "\twhere ucq.user = ?  " +
                    "  \tAND ADDTIME(ucq.completed_on, q.repetition_cycle) > CURRENT_DATE " +
                    "  ) and uaq.user = ? " +
                    "  \tand q.repetition_cycle IS NOT NULL " +
                    "    group by q.id, q.name, q.description, q.exp_reward, q.gold_reward, q.repetition_cycle " +
                    "    ;";
            PreparedStatement preparedStatement = questForHealthConn.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                questList.add(mapRowDueQuests(rs));
            }
            return questList;
        } catch (SQLException e){
            throw new PersistenceException("Failed to get due quests: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Quest> getAllOpenOneTimeQuestsForUser(int userId) {
        LOGGER.trace("getAllOpenOneTimeQuestsForUser({})", userId);
        makeJDBCConnection();
        try{
            String query = "SELECT * FROM user_accepted_quest uaq " +
                    "INNER JOIN quest q ON uaq.quest = q.id " +
                    "LEFT JOIN doctor_quest d on d.id = q.id " +
                    "WHERE uaq.user = ? AND q.id NOT IN ( " +
                    "  SELECT ucq.quest FROM user_completed_quest ucq " +
                    "  WHERE ucq.user = ?) " +
                    "  AND q.repetition_cycle IS NULL; ";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1,userId);
            pstmnt.setInt(2,userId);
            ResultSet rs = pstmnt.executeQuery();
            List<Quest> quests = new ArrayList<>();
            while (rs.next()){
                quests.add(mapRow(rs));
            }
            return quests;
        } catch (SQLException e){
            throw new PersistenceException("Could not get Open one-time Quests for user " + userId + " :" + e.getMessage(),e);
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
    public List<Quest> getAllMissedQuestsForUser(int userId) {
        LOGGER.trace("getAllMissedQuestsForUser({})", userId);
        makeJDBCConnection();
        try{
            List<Quest> retVal = new ArrayList<>();
            String query = "select q.id, q.name, q.description, q.exp_reward, q.gold_reward, q.repetition_cycle, d.exp_penalty, d.gold_penalty, max(ADDTIME(uaq.accepted_on, q.repetition_cycle)) as NewDueOn, max(ADDTIME(ucq.completed_on, q.repetition_cycle)) as OldDueOn from user_accepted_quest uaq " +
                    "                    inner join quest q on q.id = uaq.quest " +
                    "                    left join user_completed_quest ucq on q.id = ucq.quest and uaq.user = ucq.user " +
                    "                    left join doctor_quest d on d.id = q.id " +
                    "                    where uaq.quest not in( " +
                    "                      select ucq.quest from user_completed_quest ucq " +
                    "                    inner join quest q on q.id = ucq.quest " +
                    "                    where ucq.user = ?  " +
                    "                      AND ADDTIME(ucq.completed_on, q.repetition_cycle) > CURRENT_DATE " +
                    "                      ) and uaq.user = ? " +
                    "                      and q.repetition_cycle IS NOT NULL " +
                    "                        group by q.id, q.name, q.description, q.exp_reward, q.gold_reward, q.repetition_cycle " +
                    "                       HAVING NewDueOn < CURRENT_DATE AND (OldDueOn IS NULL OR OldDueOn < CURRENT_DATE) " +
                    "                        ;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1,userId);
            pstmnt.setInt(2,userId);
            ResultSet rs = pstmnt.executeQuery();
            while(rs.next()){
                retVal.add(mapRowDueQuests(rs));
            }
            return retVal;
        } catch (SQLException e){
            throw new PersistenceException(e.getMessage(),e);
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

    /**
     * All Java Libraries refuse to parse MySQL Time because it accepts
     * vales greater than 23:59:59 which is not a valid time.
     * java.time.Duration only accepts time in ISO-8601 format.
     *
     * @param duration java.time.Duration Object with the period
     * @return The String to parse in MySQL Time format
     */
    private String parseRepetitionCycle(Duration duration) throws DateTimeParseException {
        LOGGER.trace("parseRepetitionCycle({})", duration);

        String sqlTime  ="00:00:00";
        long seconds = duration.getSeconds();
        if(seconds != 0) {
            sqlTime = String.format("%02d:%02d:%02d",
                    seconds / 3600,
                    (seconds % 3600) / seconds/60,
                    seconds % 60);
        }

        return sqlTime;
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

    @Override
    public boolean checkIfQuestAlreadyAccepted(AcceptedQuest acceptedQuest){
        LOGGER.trace("checkIfQuestAlreadyAccepted({})", acceptedQuest);
        makeJDBCConnection();
        try {
            String query = "SELECT a.accepted_on AS accepted_on, c.completed_on AS completed_on, q.repetition_cycle AS repetition_cycle " +
            "FROM user_accepted_quest a LEFT JOIN user_completed_quest c ON (a.quest = c.quest) JOIN quest q ON (a.quest = q.id) " +
            "WHERE a.user = ? AND a.quest = ? " +
            "ORDER BY c.completed_on DESC LIMIT 1;";

            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, acceptedQuest.getUser());
            pstmnt.setInt(2, acceptedQuest.getQuest());
            ResultSet rs = pstmnt.executeQuery();
            if (rs != null && rs.next()){
                if (rs.getObject("completed_on") == null) return true;
                LocalDate completedOn = rs.getDate("completed_on").toLocalDate();
                String rep = rs.getString("repetition_cycle");
                if (rep == null || rep.equals("")) return false;
                Duration repetition_cycle = parseRepetitionCycle(rep);
                LocalDate today = LocalDate.now();
                Duration between = Duration.between(today.atStartOfDay(), completedOn.atStartOfDay());
                if (between.compareTo(repetition_cycle) <= 0) return true;
            }
            return false;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    @Override
    public List<AcceptedQuest> getAllAcceptedQuestForUser(int user) {
        LOGGER.trace("getAllAcceptedQuestForUser({})",user);
        makeJDBCConnection();
        List<AcceptedQuest> list = new ArrayList<AcceptedQuest>();

        try {
            final String query = "SELECT * FROM user_accepted_quest WHERE user = ?";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1,user);
            ResultSet rs = pstmnt.executeQuery();
            if(rs != null){
                while(rs.next()){
                    list.add(mapRowAcceptedQuest(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<CompletedQuest> getAllCompletedQuestForUser(int user) {
        LOGGER.trace("getAllCompletedQuestForUser({})",user);
        makeJDBCConnection();
        List<CompletedQuest> list = new ArrayList<CompletedQuest>();

        try {
            final String query = "SELECT * FROM user_completed_quest WHERE user = ?";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1,user);
            ResultSet rs = pstmnt.executeQuery();
            if(rs != null){
                while(rs.next()){
                    list.add(mapRowCompletedQuest(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
        return list;
    }

    private AcceptedQuest mapRowAcceptedQuest(ResultSet rs) throws SQLException {
        LOGGER.trace("mapRowAcceptedQuest({})",rs);
        AcceptedQuest aq = new AcceptedQuest();
        aq.setQuest(rs.getInt("quest"));
        aq.setUser(rs.getInt("user"));
        aq.setAcceptedOn(rs.getDate("accepted_on").toLocalDate());
        return aq;
    }

    private CompletedQuest mapRowCompletedQuest(ResultSet rs) throws SQLException {
        LOGGER.trace("mapRowCompletedQuest({})",rs);
        CompletedQuest aq = new CompletedQuest();
        aq.setQuest(rs.getInt("quest"));
        aq.setUser(rs.getInt("user"));
        aq.setCompletedOn(rs.getDate("completed_on").toLocalDate());
        //aq.setCompleted(rs.getBoolean("completed"));
        return aq;
    }


}
