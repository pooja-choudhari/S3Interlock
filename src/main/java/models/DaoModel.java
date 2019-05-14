/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { DaoModel.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */


package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * This class has the responsibility to create the table if the table does not
 * exist, using the Create query.
 */
public class DaoModel {
    // Declare DB objects
    DBConnect conn = null;
    Statement statement = null;

    // constructor
    public DaoModel() { // create db object instance
        conn = new DBConnect();
    }

    public void createInitialTable() throws SQLException {

        try {
            // Open a connection
            System.out.println("Creating Initial table in 510labs database...");

            String cpoojaCreateAccountSQL = "CREATE TABLE IF NOT EXISTS `510labs`.`cpooja_account` (\n" +
                    "  `username` VARCHAR(60) NOT NULL,\n" +
                    "  `password` VARCHAR(150) NULL,\n" +
                    "  `description` VARCHAR(200) NULL,\n" +
                    "  `image` LONGBLOB NULL,\n" +
                    "  PRIMARY KEY (`username`));\n";

            String cpoojaCreateAccessKeyManagerSQL = "CREATE TABLE IF NOT EXISTS `510labs`.`cpooja_access_key_manager` (\n" +
                    "  `username` VARCHAR(60) NOT NULL,\n" +
                    "  `key_identifier` VARCHAR(100) NOT NULL,\n" +
                    "  `access_key` VARCHAR(100) NOT NULL,\n" +
                    "  `secret_key` VARCHAR(100) NOT NULL,\n" +
                    "  `csp_name` VARCHAR(45) NOT NULL,\n" +
                    "  PRIMARY KEY (`username`, `key_identifier`));";

            String cpoojaCreateCloudServiceProviderSQL = "CREATE TABLE IF NOT EXISTS `510labs`.`cpooja_cloud_service_provider` (\n" +
                    "  `csp_name` VARCHAR(60) NOT NULL,\n" +
                    "  `csp_endpoint` VARCHAR(100) NULL,\n" +
                    "  `csp_region` VARCHAR(45) NULL,\n" +
                    "  PRIMARY KEY (`csp_name`));\n";

            String cpoojaCreateCloudOpStatsSQL = "CREATE TABLE IF NOT EXISTS `510labs`.`cpooja_cloud_op_stats` (\n" +
                    "  `username` VARCHAR(60) NOT NULL,\n" +
                    "  `key_identifier` VARCHAR(100) NOT NULL,\n" +
                    "  `upload_count` INT NULL,\n" +
                    "  `download_count` INT NULL,\n" +
                    "  `delete_count` INT NULL,\n" +
                    "  `list_count` INT NULL,\n" +
                    "  PRIMARY KEY (`username`, `key_identifier`));";

            statement = conn.connect().createStatement();

            statement.executeUpdate(cpoojaCreateAccountSQL);
            statement.executeUpdate(cpoojaCreateAccessKeyManagerSQL);
            statement.executeUpdate(cpoojaCreateCloudServiceProviderSQL);
            statement.executeUpdate(cpoojaCreateCloudOpStatsSQL);

            System.out.println("Completed creating table in given database...");
            conn.connect().close(); // close db connection

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            conn.connect().close(); // close db connection

        }

    }

    public void setUpCspTable() throws SQLException {
        Connection connect = null;
        try {

            String csp = "INSERT INTO " + DBConnect.DB_CSP_TBL +
                    " (csp_name, csp_endpoint, csp_region) VALUES (?, ?, ?)";

            connect = DBConnect.connect();

            connect = DBConnect.connect();
            PreparedStatement ibm_preparedStmt = connect.prepareStatement(csp);
            ibm_preparedStmt.setString(1, "ibm");
            ibm_preparedStmt.setString(2, "https://s3.us-east.cloud-object-storage.appdomain.cloud");
            ibm_preparedStmt.setString(3, "chicago");
            ibm_preparedStmt.execute();

            PreparedStatement aws_preparedStmt = connect.prepareStatement(csp);
            aws_preparedStmt.setString(1, "aws");
            aws_preparedStmt.setString(2, "https://s3.us-west-2.amazonaws.com");
            aws_preparedStmt.setString(3, "us-west-2");
            aws_preparedStmt.execute();

        }
         catch (SQLException e) {
            e.printStackTrace();

        } finally {

            if (connect != null) {
                connect.close();
            }
        }

    }
}
