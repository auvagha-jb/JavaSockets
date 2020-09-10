package database;

import models.Toy;
import utils.DatabaseUtil;

import java.sql.*;
import java.util.*;
import utils.FormUtil;

public class Toys {

    public static final String INSERT_SUCCESS_MSG = "New toy has been added";

    public Toys() {
        createTable();
    }

    private static void createTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS toys ("
                + "	toyCode integer PRIMARY KEY,"
                + "	name varchar NOT NULL,"
                + "	description varchar NOT NULL,"
                + "	price double NOT NULL,"
                + "	manufacturerId int NOT NULL,"
                + "	manufactureDate text NOT NULL,"//'2007-01-01 10:00:00'
                + "	batchNumber int NOT NULL,"
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

    public int insert(Toy toy) {
        Connection con = DatabaseUtil.openConnection();
        PreparedStatement ps = null;
        int id = 0;

        try {
            String sql = "INSERT INTO toys(name, description, price, manufacturerId, manufactureDate, batchNumber) VALUES(?,?,?,?,?,?) ";
            ps = con.prepareStatement(sql);
            ps.setString(1, toy.getName());
            ps.setString(2, toy.getDescription());
            ps.setDouble(3, toy.getPrice());
            ps.setInt(4, toy.getManufacturerId());
            ps.setString(5, toy.getManufactureDate());
            ps.setInt(6, toy.getBatchNumber());
            ps.execute();
            System.out.println("New toy has been inserted!");
            FormUtil.successFeedback(FormUtil.FORM_SUBMITTED_TITLE, INSERT_SUCCESS_MSG);
            id = DatabaseUtil.getInsertedId(ps.getGeneratedKeys());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DatabaseUtil.closeConnection(con, ps);
        }

        return id;
    }

    public static String[] selectToys() {
        String sql = "SELECT toyCode, name, description, price, manufactureDate, batchNumber, toys.createdAt,"
                + " manufacturers.companyName FROM toys INNER JOIN manufacturers"
                + " WHERE toys.manufacturerId = manufacturers.manufacturerId";

        Connection conn = DatabaseUtil.openConnection();
        String[] toys = null;

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs != null) {
                toys = getToys(rs);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            DatabaseUtil.closeConnection(conn);
        }

        return toys;
    }

    private static String[] getToys(ResultSet rs) throws SQLException {
        ArrayList<String> toysArrayList = new ArrayList<>();

        // loop through the result set
        while (rs.next()) {
            toysArrayList.add(new Toy(
                    rs.getInt("toyCode"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getString("companyName"),
                    rs.getString("manufactureDate"),
                    rs.getInt("batchNumber"),
                    rs.getString("createdAt")
            ).toyInformationToString()
            );
        }

        return toysArrayList.toArray(new String[0]);
    }

    public static String[] selectIdentificationDetails() {
        String sql = "SELECT toyCode, name FROM toys";

        Connection conn = DatabaseUtil.openConnection();
        String[] toys = null;

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs != null) {
                toys = getIdentificationDetails(rs);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            DatabaseUtil.closeConnection(conn);
        }

        return toys;
    }

    private static String[] getIdentificationDetails(ResultSet rs) throws SQLException {
        ArrayList<String> toysArrayList = new ArrayList<>();

        // loop through the result set
        while (rs.next()) {
            toysArrayList.add(new Toy(
                    rs.getInt("toyCode"),
                    rs.getString("name")
            ).toyIdentificationToString()
            );
        }

        return toysArrayList.toArray(new String[0]);
    }

//    public static void main(String[] args) {
//        new Toys().insert(new Toy("Mickey", "Popular Disney Character", 270.00, 1, "2007-01-01 10:00:00", 1234));
//        String[] toys = selectToys();
//        if (toys.length > 0) {
//            for (String toy : toys) {
//                System.out.println(toy);
//            }
//        } else {
//            System.out.println("No Matching results");
//        }
//    }
}
