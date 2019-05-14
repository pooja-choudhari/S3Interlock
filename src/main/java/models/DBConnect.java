/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { DBConnect.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */


package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    // Code database URL

    static final String DB_URL = "jdbc:mysql://www.papademas.net:3307/510labs?autoReconnect=true&useSSL=false";
//    static final String DB_URL = "jdbc:mysql://127.0.0.1/510labs?autoReconnect=true&useSSL=false" +
//        "&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    // Database credentials
    static final String DB_USER = "db510";
    static final String DB_PASS = "510";

    static final String DB_ACCOUNT_TBL = "cpooja_account";
    static final String DB_ACCESS_KEY_MGR_TBL = "cpooja_access_key_manager";
    static final String DB_CSP_TBL = "cpooja_cloud_service_provider";
    public static final String DB_CLOUD_OP_STAT_TBL = "cpooja_cloud_op_stats";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

    }

    public static void main(String[] args) throws SQLException {
        connect();
    }
}