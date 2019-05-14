/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { AccessKeyDaoInterface.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */


package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface AccessKeyDaoInterface {

    void addAccessKeyToDb(AccessKey accessKey) throws SQLException;

    boolean deleteAccessKeyFromdb(AccessKey accessKey) throws SQLException;

    boolean updateAccessKeyInDb(AccessKey accessKey) throws SQLException;

    boolean doesAccessKeyExist(AccessKey accessKey) throws SQLException;

    ResultSet displayAccessKeysInfo() throws SQLException;

    AccessKey getFriendlyKeyInfo(AccessKey accessKey) throws SQLException;

    ResultSet getCspInfo() throws SQLException;

    void addFriendlyIdtoStats(String username, String friendlyKeyId) throws SQLException;
}
