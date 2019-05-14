
/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { NewAccountModel.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */


package models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;

public class NewAccountModel {

    // Insert User's personal Details
    public void insertRecords(UserProfile userProfile) throws SQLException, FileNotFoundException {
        Connection connect = null;
        try {
            // // Execute a query
            System.out.println("Inserting records into the table...");
            connect = DBConnect.connect();

            String sql = "INSERT INTO " + DBConnect.DB_ACCOUNT_TBL + " (username, password, description, image) VALUES (?,?,?,?)";
            PreparedStatement preparedStmt = connect.prepareStatement(sql);

            preparedStmt.setString(1, userProfile.getName());
            preparedStmt.setString(2, userProfile.getPwd());
            preparedStmt.setString(3, userProfile.getDesc());
            int length = (int) userProfile.getFile().length();
            preparedStmt.setBinaryStream(4, new FileInputStream(userProfile.getFile()), length);

            preparedStmt.execute();
        }
        catch (Exception e){
            e.printStackTrace();

        } finally {
            if (connect != null) {
                connect.close();
            }
        }

    }

    public boolean doesAccountExist(String username) throws SQLException {
        Connection connect = null;
        boolean accountExists = false;
        try {

            connect = DBConnect.connect();

            Statement stmt = DBConnect.connect().createStatement();

            String sql =
                    "SELECT * FROM " + DBConnect.DB_ACCOUNT_TBL +
                            " WHERE username='" + username + "'";

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                System.out.println("Account exists in DB:" + username);
                accountExists = true;

            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (connect != null) {
                connect.close();
            }
        }

        return accountExists;
    }

    public void updateAccountPassword(String username, String password) throws SQLException {
        Connection connect = null;
        try {
            // // Execute a query
            System.out.println("Inserting records into the table...");
            connect = DBConnect.connect();

            Statement stmt = DBConnect.connect().createStatement();
            String sql = "update " + DBConnect.DB_ACCOUNT_TBL + " set password='" +password+ "' where username='" + username +"'";

            stmt.executeUpdate(sql);
        }
        catch (Exception e){
            e.printStackTrace();

        } finally {
            if (connect != null) {
                connect.close();
            }
        }

    }

}

