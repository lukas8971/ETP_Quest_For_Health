package com.etp.questforhealth.persistence.impl;

import com.etp.questforhealth.entity.Doctor;
import com.etp.questforhealth.exception.InvalidLoginException;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.persistence.DoctorDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.*;

@Repository
public class DoctorJdbcDao implements DoctorDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static Connection questForHealthConn = null;
    @Value("${spring.datasource.url}")
    String URL;
    @Value("${spring.datasource.username}")
    String USERNAME;
    @Value("${spring.datasource.password}")
    String PASSWORD;

    @Override
    public Doctor checkLogin(String email, String password) {
        LOGGER.trace("checkLogin({}, {})", email, password);
        makeJDBCConnection();
        try {
            String query = "SELECT * FROM quest WHERE email = ? AND password = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(query);
            pstmnt.setString(1, email);
            pstmnt.setString(2, password);
            ResultSet rs = pstmnt.executeQuery();
            if (rs == null) throw new InvalidLoginException("Wrong email or password!");
            return mapRow(rs);
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    private Doctor mapRow(ResultSet rs) throws SQLException {
        LOGGER.trace("mapRow({})", rs);
        final Doctor doctor = new Doctor();
        doctor.setId(rs.getInt("id"));
        doctor.setFirstname(rs.getString("firstname"));
        doctor.setLastname(rs.getString("lastname"));
        doctor.setEmail(rs.getString("email"));
        return doctor;
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
