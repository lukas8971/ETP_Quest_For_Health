package com.etp.questforhealth.persistence.impl;

import com.etp.questforhealth.entity.Equipment;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.entity.enums.EquipmentType;
import com.etp.questforhealth.entity.enums.mapper.EquipmentTypeMapper;
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
        List<Equipment> equipmentList = new ArrayList<>();
        try{
            String query = "SELECT * FROM "+ USER_WEARS_EQUIPMENT_TABLE_NAME + " u" +
                    " INNER JOIN " + TABLE_NAME + " e ON u.equipment = e.id" +
                    " WHERE u.user = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setInt(1, userId);
            ResultSet rs = pstmnt.executeQuery();
            while (rs.next()){
                equipmentList.add(mapRow(rs));
            }

        } catch (SQLException e){
            throw new PersistenceException(e.getMessage(),e);
        }
        return equipmentList;
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
