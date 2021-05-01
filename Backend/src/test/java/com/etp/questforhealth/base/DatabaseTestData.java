package com.etp.questforhealth.base;

import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
public class DatabaseTestData {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static Connection questForHealthConn = null;
    //@Value("${spring.datasource.url}")
    static String URL = "jdbc:mysql://localhost:3306/Quest_For_Health_Test";
    //@Value("${spring.datasource.username}")
    static String USERNAME = "lukas";
    //@Value("${spring.datasource.password}")
    static String PASSWORD = "lukas";
    //@Value("${spring.datasource.autoCommit}")
    static String AUTOCOMMIT = "true";

    public static void main(String[] args) {
        insertTestData();
    }

    /**
     * Executed once when the component is instantiated. Inserts some dummy data.
     */
    public static void insertTestData() {
        makeJDBCConnection();
        try {
            ClassPathResource empty = new ClassPathResource("emptyTest.sql");
            ScriptUtils.executeSqlScript(questForHealthConn, empty);
            ClassPathResource create = new ClassPathResource("createSchemeTest.sql");
            ScriptUtils.executeSqlScript(questForHealthConn, create);
            ClassPathResource fill = new ClassPathResource("insertTest.sql");
            ScriptUtils.executeSqlScript(questForHealthConn, fill);
        } catch (Exception e){
            LOGGER.error("Error inserting test data", e);
        }
    }

    public static void makeJDBCConnection() {
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
                LOGGER.debug("URL,USERNAME,PASSWORD");
                LOGGER.debug(URL);
                LOGGER.debug(USERNAME);
                LOGGER.debug(PASSWORD);
                LOGGER.trace("URL,USERNAME,PASSWORD");
                LOGGER.trace(URL);
                LOGGER.trace(USERNAME);
                LOGGER.trace(PASSWORD);
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
