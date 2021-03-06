package com.etp.questforhealth.persistence.impl;

import com.etp.questforhealth.entity.Equipment;
import com.etp.questforhealth.entity.UserEquipment;
import com.etp.questforhealth.entity.enums.EquipmentType;
import com.etp.questforhealth.entity.enums.mapper.EquipmentTypeMapper;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.persistence.EquipmentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EquipmentJdbcDao implements EquipmentDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static Connection questForHealthConn = null;
    static String TABLE_NAME = "equipment";
    static String USER_WEARS_EQUIPMENT_TABLE_NAME ="user_wears_equipment";
    static String USER_HAS_EQUIPMENT_TABLE_NAME = "user_has_equipment";
    @Value(value = "${spring.datasource.url}")
    String URL;
    @Value("${spring.datasource.username}")
    String USERNAME;
    @Value("${spring.datasource.password}")
    String PASSWORD;
    @Value("${spring.datasource.autoCommit}")
    String AUTOCOMMIT;

    @Override
    public List<Equipment> getWornEquipmentFromUserId(int userId) {
        LOGGER.trace("getWornEquipmentFromUserId({})", userId);
        makeJDBCConnection();
        try{
            String query = "SELECT * FROM "+ USER_WEARS_EQUIPMENT_TABLE_NAME + " u" +
                    " INNER JOIN " + TABLE_NAME + " e ON u.equipment = e.id" +
                    " WHERE u.user = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, userId);
            ResultSet rs = pstmnt.executeQuery();
            if (rs == null) return null;
            List<Equipment> equipmentList = new ArrayList<>();
            while (rs.next()){
                equipmentList.add(mapRow(rs));
            }
            return equipmentList;
        } catch (SQLException e){
            throw new PersistenceException(e.getMessage(),e);
        }
    }

    public Equipment getEquipmentOfTypeWornByUserId(EquipmentType type, int id){
        LOGGER.trace("getEquipmentOfTypeWornByUserId({}, {})", type, id);
        makeJDBCConnection();
        try{
            String query = "SELECT * FROM "+ USER_WEARS_EQUIPMENT_TABLE_NAME + " u" +
                    " INNER JOIN " + TABLE_NAME + " e ON u.equipment = e.id" +
                    " WHERE u.user = ? AND e.type = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, id);
            pstmnt.setObject(2, EquipmentTypeMapper.enumToString(type));
            ResultSet rs = pstmnt.executeQuery();
            if (rs != null && rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e){
            throw new PersistenceException(e.getMessage(),e);
        }
    }

    public List<Equipment> getAvailableEquipmentToEquip(EquipmentType type, int id){
        LOGGER.trace("getAvailableEquipmentToEquip({}, {})", type, id);
        makeJDBCConnection();
        try{
            String query = "SELECT * FROM "+ USER_HAS_EQUIPMENT_TABLE_NAME + " u" +
                    " INNER JOIN " + TABLE_NAME + " e ON u.equipment = e.id" +
                    " WHERE u.user = ? AND e.type = ? AND u.equipment NOT IN" +
                    " (SELECT x.equipment FROM " + USER_WEARS_EQUIPMENT_TABLE_NAME + " x WHERE x.user = ?)" +
                    " ORDER BY e.strength DESC;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, id);
            pstmnt.setObject(2, EquipmentTypeMapper.enumToString(type));
            pstmnt.setInt(3, id);
            ResultSet rs = pstmnt.executeQuery();
            List<Equipment> equipment = new ArrayList<>();
            while(rs.next()) {
                equipment.add(mapRow(rs));
            }
            if (equipment.size() > 0) return equipment;
            return null;
        } catch (SQLException e){
            throw new PersistenceException(e.getMessage(),e);
        }
    }

    private Equipment mapRow(ResultSet rs) throws SQLException {
        LOGGER.trace("mapRow({})", rs);
        return new Equipment(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("price"),
                rs.getInt("strength"),
                EquipmentTypeMapper.stringToEnum(rs.getString("type"))
        );
    }

    private Equipment mapRowUserHasEquipment(ResultSet rs) throws SQLException {
        LOGGER.trace("mapRow({})", rs);
        return new Equipment(rs.getInt("e.id"),
                rs.getString("e.name"),
                rs.getString("e.description"),
                rs.getInt("e.price"),
                rs.getInt("e.strength"),
                EquipmentTypeMapper.stringToEnum(rs.getString("e.type"))
        );
    }

    @Override
    public List<Equipment> getAvailableEquipmentByTypeAndId(int user, String type){
        LOGGER.trace("getAvailableEquipmentByTypeAndId({}, {})", user, type);
        makeJDBCConnection();
        try {
            String query = "SELECT e.* FROM " + TABLE_NAME + " e " +
                    "WHERE e.id NOT IN (SELECT uhe.equipment AS id FROM " + USER_HAS_EQUIPMENT_TABLE_NAME + " uhe WHERE uhe.user = ?) AND " +
                    "e.type = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, user);
            pstmnt.setObject(2, type);
            ResultSet rs = pstmnt.executeQuery();
            if (rs == null) return null;
            List<Equipment> equipment = new ArrayList<>();
            while(rs.next()){
                equipment.add(mapRowUserHasEquipment(rs));
            }
            return equipment;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    @Override
    public Equipment getOneById(int id){
        LOGGER.trace("getOneById({})", id);
        makeJDBCConnection();
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?;";
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
    public boolean buyNewEquipment(UserEquipment userEquipment){
        LOGGER.trace("buyNewEquipment({})", userEquipment);
        LOGGER.debug("buyNewEquipment({})", userEquipment);
        makeJDBCConnection();
        LOGGER.debug("makeJDBCConnection done");
        try {
            String sql = "INSERT INTO " + USER_HAS_EQUIPMENT_TABLE_NAME + "(user, equipment) " +
                    " VALUES(?,?);";
            LOGGER.debug("sql: " + sql);
            PreparedStatement stmt = questForHealthConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            LOGGER.debug("stmt done");
            stmt.setInt(1, userEquipment.getUserId());
            LOGGER.debug("setInt user done");
            stmt.setInt(2, userEquipment.getEquipmentId());
            LOGGER.debug("setInt equipment done");
            int rowsAffected = stmt.executeUpdate();
            LOGGER.debug("rowsAffected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    @Override
    public List<UserEquipment> getAllUserEquipments(int id){
        LOGGER.trace("getAllUserEquipments({})", id);
        makeJDBCConnection();
        try {
            final String sql = "SELECT * FROM " + USER_HAS_EQUIPMENT_TABLE_NAME + " WHERE user = ?;";
            PreparedStatement stmt = questForHealthConn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs == null) return null;
            List<UserEquipment> userEquipment = new ArrayList<>();
            while(rs.next()){
                userEquipment.add(new UserEquipment(rs.getInt("equipment"), rs.getInt("user")));
            }
            return userEquipment;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean checkIfUserOwnsEquipment(int usereId, int equipmentId) {
        LOGGER.trace("checkIfUserOwnsEquipment({}, {})", usereId, equipmentId);
        makeJDBCConnection();
        try{
            final String sql = "SELECT * FROM " + USER_HAS_EQUIPMENT_TABLE_NAME + " WHERE user = ? AND equipment = ?;";
            PreparedStatement stmt = questForHealthConn.prepareStatement(sql);
            stmt.setInt(1, usereId);
            stmt.setInt(2, equipmentId);
            ResultSet rs = stmt.executeQuery();
            if (rs != null && rs.next()) return true;
            return false;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    @Override
    public Equipment getWornEquipmentByTypeAndUser(int user, EquipmentType type){
        LOGGER.trace("getWornEquipmentByTypeAndUser({}, {})", user, type);
        List<Equipment> eqs = getWornEquipmentFromUserId(user);
        if (eqs.size() > 0) {
            for (Equipment e : eqs) {
                if (e.getType() == type) return e;
            }
        }
        return null;
    }

    @Override
    public boolean unequipItem(int userId, EquipmentType type) {
        LOGGER.trace("unequipItem({}, {})", userId, type);
        makeJDBCConnection();
        try{
            Equipment currWorn = getWornEquipmentByTypeAndUser(userId, type);
            if (currWorn == null) return true;
            final String sql = "DELETE FROM " + USER_WEARS_EQUIPMENT_TABLE_NAME + " WHERE user = ? AND equipment = ?";
            PreparedStatement stmt = questForHealthConn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, currWorn.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean unequipItem(int userId, int equipmentId) {
        LOGGER.trace("unequipItem({}, {})", userId, equipmentId);
        makeJDBCConnection();
        try{
            final String sql = "DELETE FROM " + USER_WEARS_EQUIPMENT_TABLE_NAME + " WHERE user = ? AND equipment = ?";
            PreparedStatement stmt = questForHealthConn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, equipmentId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows == 0 || affectedRows == 1;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    @Override
    public Equipment equipItem(int userId, int equipmentId){
        LOGGER.trace("equipItem({}, {})", userId, equipmentId);
        makeJDBCConnection();
        try{
            final String sql = "INSERT INTO " + USER_WEARS_EQUIPMENT_TABLE_NAME + "(user, equipment) VALUES " +
                    " (?, ?);";
            PreparedStatement stmt = questForHealthConn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, equipmentId);
            int change = stmt.executeUpdate();
            if (change > 0) {
                return getOneById(equipmentId);
            }
            else throw new PersistenceException("Could not equip item!");
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    @Override
    public Equipment createNewEquipment(Equipment equipment) {
        LOGGER.trace("createNewEquipment({})", equipment);
        makeJDBCConnection();
        try {
            String query = "Insert into " + TABLE_NAME + " (name, description, price, strength, type)" +
                    " values (?,?,?,?,?)";
            PreparedStatement pstmtnt = questForHealthConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmtnt.setString(1, equipment.getName());
            pstmtnt.setString(2, equipment.getDescription());
            pstmtnt.setInt(3, equipment.getPrice());
            pstmtnt.setInt(4, equipment.getStrength());
            pstmtnt.setObject(5, EquipmentTypeMapper.enumToString(equipment.getType()));


            pstmtnt.executeUpdate();
            ResultSet rs = pstmtnt.getGeneratedKeys();
            rs.next();
            equipment.setId(rs.getInt(1));
        }
        catch (SQLException e){
            throw new PersistenceException("Error while inserting equipment in database", e);
        }
        return equipment;
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
