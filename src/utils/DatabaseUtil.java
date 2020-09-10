package utils;

import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.sql.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseUtil {

    private static final String DATABASE_PATH = "C:/java_utils/sqlite/db/";
    private static final String DATABASE_NAME = "java_sockets.db";
    private static final String DATABASE_URL = String.format("jdbc:sqlite:%s%s", DATABASE_PATH, DATABASE_NAME);

    private static void makeDatabasePath() {
        // create an abstract pathname (File object) 
        File f = new File(DATABASE_PATH);

        // check if the directory can be created 
        // using the abstract path name 
        if (f.mkdir()) {

            // display that the directory is created 
            // as the function returned true 
            System.out.println("Directory is created");
        } else {
            // display that the directory cannot be created 
            // as the function returned false 
            System.out.println("Directory cannot be created");
        }
    }

    private static boolean pathExists() {
        File file = new File(DATABASE_PATH);
        boolean pathExists = false;

        try {
            pathExists = file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(DatabaseUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pathExists;
    }

    /**
     * Connect to a sample database
     *
     * @return Connection
     */
    public static Connection openConnection() {
        //Fail safe to create directory if it doesn't exist
        if (!pathExists()) {
            makeDatabasePath();
        }

        Connection conn = null;
        System.out.printf("Database url %s \n", DATABASE_URL);

        try {
            // create a connection to the database
            conn = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, String.format("Error connecting to SQL database. Please check the DATABASE_PATH. See Stacktrace for further details", DATABASE_PATH), "Connection error", JOptionPane.ERROR_MESSAGE);
        }

        return conn;
    }

    public static void closeConnection(Connection con) {
        //close database connections
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public static void closeConnection(Connection con, PreparedStatement ps) {
        //close database connections
        try {
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public static int getInsertedId(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return (int) rs.getLong(1);
        } else {
            throw new SQLException("Creating user failed, no ID obtained.");
        }
    }

    public static int generateUniqueId(String table, String column) {
        Random randGenerator = new Random();
        int code;
        int numRows;
        int runs = 0;

        do {
            code = randGenerator.nextInt(900000) + 100000;

            String sql = String.format("SELECT COUNT(*) as numRows FROM %s WHERE %s = ?", table, column);
            System.out.println(sql);
            numRows = getNumRows(sql, code);

            runs++;

        } while (numRows > 0);

        return code;
    }

    private static int getNumRows(String sql, int value) {
        Connection conn = openConnection();
        PreparedStatement ps = null;
        int numRows = 0;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, value);
            ResultSet rs = ps.executeQuery();
            numRows = rs.getInt("numRows");
            System.out.printf("Number of rows = %s for id %s\n", numRows, value);

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection(conn, ps);
        }

        if (numRows > 0) {
            System.out.printf("A match was found for code: %s\n", value);
        }

        return numRows;
    }

}
