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
import models.Manufacturer;
import utils.DatabaseUtil;
import utils.FormUtil;

/**
 *
 * @author Jerry Auvagha
 */
public class Manufacturers {

    public static final String INSERT_SUCCESS_MSG = "New manufacturer has been added";
    
    public Manufacturers() {
        createTable();
    }
    
    private void createTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS manufacturers ("
                + "	manufacturerId integer PRIMARY KEY,"
                + "	companyName varchar NOT NULL,"
                + "	streetAddress varchar NOT NULL,"
                + "	zipCode int NOT NULL,"
                + "	country varchar NOT NULL,"
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

    public int insert(Manufacturer man) {
        Connection con = DatabaseUtil.openConnection();
        PreparedStatement ps = null;
        int id = 0;
        
        try {
            String sql = "INSERT INTO manufacturers(companyName, streetAddress, zipCode, country) VALUES(?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, man.getCompanyName());
            ps.setString(2, man.getStreetAddress());
            ps.setInt(3, man.getZipCode());
            ps.setString(4, man.getCountry());
            ps.execute();
            id = DatabaseUtil.getInsertedId(ps.getGeneratedKeys());
            System.out.println("New manufacturer has been inserted!");
            
            FormUtil.successFeedback(FormUtil.FORM_SUBMITTED_TITLE, INSERT_SUCCESS_MSG);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DatabaseUtil.closeConnection(con, ps);
        }
        
        return id;
    }

    public static String[] selectManufacturers() {
        String sql = "SELECT * FROM manufacturers";
        Connection conn = DatabaseUtil.openConnection();
        ResultSet rs = null;
        String[] manufacturers = null;

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs!=null) manufacturers = getManufacturerDetails(rs);
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            DatabaseUtil.closeConnection(conn);
        }

        return manufacturers;
    }

    public static String[] getManufacturerDetails(ResultSet rs) throws SQLException {
        ArrayList<String> manufacturersArrayList = new ArrayList<>();

            // loop through the result set
            while (rs.next()) {
                manufacturersArrayList.add(new Manufacturer(
                        rs.getInt("manufacturerId"),
                        rs.getString("companyName"),
                        rs.getString("streetAddress"),
                        rs.getInt("zipCode"),
                        rs.getString("country"),
                        rs.getString("createdAt")
                ).manufacturerInfoToString()
                );
            }
        

        return manufacturersArrayList.toArray(new String[0]);
    }

    
    public static String[] selectIdentificationDetails() {
        String sql = "SELECT * FROM manufacturers";
        Connection conn = DatabaseUtil.openConnection();
        ResultSet rs = null;
        String[] identificationDetails = null;

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs!=null) identificationDetails = getidentificationDetails(rs);
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            DatabaseUtil.closeConnection(conn);
        }

        return identificationDetails;
    }

    
    public static String[] getidentificationDetails(ResultSet rs) throws SQLException {
        ArrayList<String> manufacturersArrayList = new ArrayList<>();

            // loop through the result set
            while (rs.next()) {
                manufacturersArrayList.add(new Manufacturer(
                        rs.getInt("manufacturerId"),
                        rs.getString("companyName"),
                        rs.getString("streetAddress"),
                        rs.getInt("zipCode"),
                        rs.getString("country"),
                        rs.getString("createdAt")
                ).identificationDetailsToString());
            }
        
        return manufacturersArrayList.toArray(new String[0]);
    }
   

    public static void main(String[] args) {
        
        new Manufacturers().insert(new Manufacturer("Kids Co", "Malibu, CA", 12345, "USA"));
        String[] manufacturers = selectIdentificationDetails();

        if (manufacturers.length > 0) {
            for (String man : manufacturers) {
                System.out.println(man);
            }
        } else {
            System.out.println("No Matching results");
        }

    }

}
