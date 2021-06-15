package com.etp.questforhealth.persistence.impl;

import com.etp.questforhealth.entity.Picture;
import com.etp.questforhealth.entity.StoryChapter;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.persistence.StoryChapterDao;
import com.etp.questforhealth.persistence.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StoryChapterJdbcDao implements StoryChapterDao {

    @Autowired
    UserDao userDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static Connection questForHealthConn = null;
    static String TABLE_NAME = "story_chapter";
    @Value("${spring.datasource.url}")
    String URL;
    @Value("${spring.datasource.username}")
    String USERNAME;
    @Value("${spring.datasource.password}")
    String PASSWORD;
    @Value("${spring.datasource.autoCommit}")
    String AUTOCOMMIT;

    @Override
    public StoryChapter getOneById(Integer id) {
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
        throw new NotFoundException("Could not find chapter with id " + id);
    }

    private StoryChapter mapRow(ResultSet rs) throws SQLException {
        LOGGER.trace("mapRow({})", rs);
        return new StoryChapter(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("strength_requirement"),
                rs.getObject("prev_chapter", Integer.TYPE),
                rs.getObject("next_chapter", Integer.TYPE)
        );
    }

    @Override
    public StoryChapter getNextChapter(StoryChapter storyChapter) {
        LOGGER.trace("getNextChapter({}, {})", storyChapter);
        makeJDBCConnection();
        try {
            StoryChapter sc = getOneById(storyChapter.getId());
            final String sql = "select * from " + TABLE_NAME + " where id = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(sql);
            pstmnt.setInt(1, sc.getNext_chapter());
            ResultSet rs = pstmnt.executeQuery();
            if (rs != null && rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public StoryChapter getPrevChapter(StoryChapter storyChapter) {
        LOGGER.trace("getPrevChapter({})", storyChapter);
        makeJDBCConnection();
        try {
            StoryChapter sc = getOneById(storyChapter.getId());
            final String sql = "select * from " + TABLE_NAME + " where id = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(sql);
            pstmnt.setInt(1, sc.getPrev_chapter());
            ResultSet rs = pstmnt.executeQuery();
            if (rs != null && rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public StoryChapter getPrevStoryOfUser(User user) {
        LOGGER.trace("getPrevStoryOfUser({})", user);
        makeJDBCConnection();
        try {
            User u = userDao.getOneById(user.getId());
            StoryChapter sc = getOneById(u.getStoryChapter());
            final String sql = "select * from " + TABLE_NAME + " where id = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(sql);
            pstmnt.setInt(1, sc.getPrev_chapter());
            ResultSet rs = pstmnt.executeQuery();
            if (rs != null && rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public StoryChapter getNextStoryOfUser(User user) {
        LOGGER.trace("getNextStoryOfUser({})", user);
        makeJDBCConnection();
        try {
            User u = userDao.getOneById(user.getId());
            StoryChapter sc = getOneById(u.getStoryChapter());
            final String sql = "select * from " + TABLE_NAME + " where id = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(sql);
            pstmnt.setInt(1, sc.getNext_chapter());
            ResultSet rs = pstmnt.executeQuery();
            if (rs != null && rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<StoryChapter> getAllPrevChapters(User user) {
        LOGGER.trace("getAllPrevChapters({})", user);
        makeJDBCConnection();
        try {
            User u = userDao.getOneById(user.getId());
            final String sql = "with recursive chaps (id,name,description,strength_requirement,prev_chapter,next_chapter) as "
                    + "(select id,name,description,strength_requirement,prev_chapter,next_chapter "
                    + "from " + TABLE_NAME + " where id = ? "
                    + "union all "
                    + "select sc.id,sc.name,sc.description,sc.strength_requirement,sc.prev_chapter,sc.next_chapter "
                    + "from story_chapter sc inner join chaps on sc.next_chapter = chaps.id) "
                    + "select * from chaps order by id;";
            PreparedStatement pstmtnt = questForHealthConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmtnt.setInt(1, u.getStoryChapter());
            ResultSet rs = pstmtnt.executeQuery();
            List<StoryChapter> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    @Override
    public List<StoryChapter> getAllNextChapters(User user) {
        LOGGER.trace("getAllNextChapters({})", user);
        makeJDBCConnection();
        try {
            User u = userDao.getOneById(user.getId());
            Integer next = getOneById(u.getStoryChapter()).getNext_chapter();
            if (next == null) throw new ValidationException("Already on the last chapter!");
            final String sql = "with recursive chaps (id,name,description,strength_requirement,prev_chapter,next_chapter) as "
                    + "(select id,name,description,strength_requirement,prev_chapter,next_chapter "
                    + "from " + TABLE_NAME + " where id = ? "
                    + "union all "
                    + "select sc.id,sc.name,sc.description,sc.strength_requirement,sc.prev_chapter,sc.next_chapter "
                    + "from story_chapter sc inner join chaps on sc.prev_chapter = chaps.id) "
                    + "select * from chaps order by id;"; // maybe prev_chapter -- depends on how the data is stored
            PreparedStatement pstmtnt = questForHealthConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmtnt.setInt(1, next);
            ResultSet rs = pstmtnt.executeQuery();
            List<StoryChapter> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    @Override
    public Picture getPicture(int id) {
        LOGGER.trace("getPicture({})", id);
        makeJDBCConnection();
        try {
            final String sql = "select picture from " + TABLE_NAME + " where id = ?;";
            PreparedStatement pstmnt = questForHealthConn.prepareStatement(sql);
            pstmnt.setInt(1, id);
            ResultSet rs = pstmnt.executeQuery();
            if (rs != null && rs.next()) {
                return new Picture(rs.getString("picture"));
            } else {
                throw new PersistenceException("Error while retrieving picture");
            }
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
