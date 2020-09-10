/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import models.ThankYouMessage;
import utils.DatabaseUtil;
import utils.RandomUtil;

/**
 *
 * @author Jerry Auvagha
 */
public class ThankYouMessages {
    private static void createTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS thankYouMessages ("
                + "	messageId String PRIMARY KEY,"
                + "	message varchar NOT NULL,"
                + "	createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + " );";
        

        Connection conn = null;
        try {
            conn = DatabaseUtil.openConnection();
            Statement stmt = conn.createStatement();
            // create a new table

            if (stmt.execute(sql)) {
                System.out.println("Table created successfully");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DatabaseUtil.closeConnection(conn);
        }
    }

    public static void insert(ThankYouMessage msg) {
        Connection con = DatabaseUtil.openConnection();
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO thankYouMessages(messageId, message) VALUES(?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, msg.getMessageId());
            ps.setString(2, msg.getMessage());
            ps.execute();
            System.out.println("New message has been inserted!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DatabaseUtil.closeConnection(con, ps);
        }
    }

    public static String[] selectMessages() {
        String sql = "SELECT * FROM thankYouMessages";
        Connection conn = DatabaseUtil.openConnection();
        ResultSet rs = null;
        String[] manufacturers = null;

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs!=null) manufacturers = getMessages(rs);
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            DatabaseUtil.closeConnection(conn);
        }

        return manufacturers;
    }

    public static String[] getMessages(ResultSet rs) throws SQLException {
        ArrayList<String> messagesArrayList = new ArrayList<>();

            // loop through the result set
            while (rs.next()) {
                messagesArrayList.add(new ThankYouMessage(
                        rs.getString("messageId"),
                        rs.getString("message")
                ).messageToString()
                );
            }
        

        return messagesArrayList.toArray(new String[0]);
    }

   

    public static void main(String[] args) {
        createTable();
        insert(new ThankYouMessage(RandomUtil.getRandomId(), "Thank You"));
        String[] messages = selectMessages();

        if (messages.length > 0) {
            for (String man : messages) {
                System.out.println(man);
            }
        } else {
            System.out.println("No Matching results");
        }

    }
}
