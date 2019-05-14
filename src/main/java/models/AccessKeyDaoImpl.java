/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { AccessKeyDaoImpl.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */

package models;

import controllers.MainAccessKeyController;
import javafx.scene.control.Alert;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccessKeyDaoImpl implements AccessKeyDaoInterface {

    String username;
    MainAccessKeyController mainAccessKeyController;

    public void setMainAccessKeyController(MainAccessKeyController mainAccessKeyController) {
        this.mainAccessKeyController = mainAccessKeyController;
    }

    @Override
    public void addAccessKeyToDb(AccessKey accessKey) throws SQLException {
        Connection connect = null;
        username = mainAccessKeyController.getFetchRecordFromDb().dbUserName;
        try {
            // // Execute a query
            System.out.println("Adding Access Key to the Table..." + accessKey);
            connect = DBConnect.connect();

            String sql =
                    "INSERT INTO " + DBConnect.DB_ACCESS_KEY_MGR_TBL +
                    " (username, key_identifier, access_key, secret_key, csp_name) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = connect.prepareStatement(sql);
            preparedStmt.setString(1, username);
            preparedStmt.setString(2, accessKey.getFriendlyKeyId());
            preparedStmt.setString(3, accessKey.getAccessKey());
            preparedStmt.setString(4, accessKey.getSecretKey());
            preparedStmt.setString(5, accessKey.getCspName());

            preparedStmt.execute();

            addFriendlyIdtoStats(username, accessKey.friendlyName);

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (connect != null) {
                connect.close();
            }
        }

    }

    @Override
    public boolean deleteAccessKeyFromdb(AccessKey accessKey) throws SQLException {
        Connection connect = null;
        boolean accessKeyDeleted = false;

        try {
            username = this.mainAccessKeyController.getFetchRecordFromDb().dbUserName;
            deleteFriendlyIdtoStats(username, accessKey);

            // // Execute a query
            System.out.println("Deleting record " + accessKey.getFriendlyKeyId());
            connect = DBConnect.connect();

            Statement stmt = DBConnect.connect().createStatement();

            if (doesAccessKeyExist(accessKey)) {
                System.out.println("Access Key exists in DB to delete:" + accessKey.getFriendlyKeyId());
                String delete_sql =
                        "DELETE FROM " + DBConnect.DB_ACCESS_KEY_MGR_TBL +
                                " WHERE username=? AND key_identifier=?";

                PreparedStatement ps = connect.prepareStatement(delete_sql);
                ps.setString(1, username);
                ps.setString(2, accessKey.getFriendlyKeyId());
                ps.executeUpdate();

                accessKeyDeleted = true;

            } else {
                return accessKeyDeleted;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (connect != null) {
                connect.close();
            }
        }

        return accessKeyDeleted;
    }

    @Override
    public boolean updateAccessKeyInDb(AccessKey accessKey) throws SQLException {
        Connection connect = null;
        boolean accessKeyExist = false;

        try {
            username = mainAccessKeyController.getFetchRecordFromDb().dbUserName;
            // // Execute a query
            System.out.println("Inserting records into the table...");
            Statement stmt = DBConnect.connect().createStatement();

            String sql =
                    "UPDATE " + DBConnect.DB_ACCESS_KEY_MGR_TBL +
                    " set access_key='" + accessKey.getAccessKey() + "', secret_key='" + accessKey.getSecretKey() +
                    "' WHERE username='" + username + "' and key_identifier='" + accessKey.getFriendlyKeyId() +"'";

            if (doesAccessKeyExist(accessKey)) {
                stmt.executeUpdate(sql);
                accessKeyExist = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (connect != null) {
                connect.close();
            }
        }

        return accessKeyExist;
    }

    public boolean doesAccessKeyExist(AccessKey accessKey) throws SQLException {
        Connection connect = null;
        boolean accessKeyExist = false;
        try {
            username = mainAccessKeyController.getFetchRecordFromDb().dbUserName;
            connect = DBConnect.connect();

            Statement stmt = DBConnect.connect().createStatement();

            String sql =
                    "SELECT * FROM " + DBConnect.DB_ACCESS_KEY_MGR_TBL +
                    " WHERE username='" + username + "' " +
                    "AND key_identifier='" + accessKey.getFriendlyKeyId() + "'";

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                System.out.println("Friendly Key Id exists in DB:" + accessKey.getFriendlyKeyId());
                accessKeyExist = true;

            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (connect != null) {
                connect.close();
            }
        }

        return accessKeyExist;
    }

    @Override
    public ResultSet displayAccessKeysInfo() throws SQLException {
        Statement stmt = null;
        Connection connect = null;
        ResultSet rs = null;
        try {
            username = mainAccessKeyController.getFetchRecordFromDb().dbUserName;
            // // Execute a query
            System.out.println("Updating records into the table...");
            stmt = DBConnect.connect().createStatement();

            String sql = "SELECT * FROM " + DBConnect.DB_ACCESS_KEY_MGR_TBL + " WHERE username='" +username +"'";
            rs = stmt.executeQuery(sql);

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();

        } finally {
            if (connect != null) {
                connect.close();
            }
        }
        return rs;
    }

    @Override
    public AccessKey getFriendlyKeyInfo(AccessKey buildaccessKeyFromKId) throws SQLException {
        Statement stmt = null;
        Connection connect = null;
        ResultSet rs = null;

        try {
            username = mainAccessKeyController.getFetchRecordFromDb().dbUserName;
            if (doesAccessKeyExist(buildaccessKeyFromKId))
            {
                System.out.println("Updating records into the table...");
                stmt = DBConnect.connect().createStatement();

                String sql = "SELECT * FROM " + DBConnect.DB_ACCESS_KEY_MGR_TBL +
                        " WHERE username='" + username + "' and key_identifier='" +
                        buildaccessKeyFromKId.getFriendlyKeyId() + "'";

                rs = stmt.executeQuery(sql);

                if (rs.next())
                {
                    buildaccessKeyFromKId.setFriendlyName(rs.getString(2));
                    buildaccessKeyFromKId.setAccessKey(rs.getString(3));
                    buildaccessKeyFromKId.setSecretKey(rs.getString(4));
                    buildaccessKeyFromKId.setCspName(rs.getString(5));
                }
                else {
                    System.out.println("Could not find key/secret: " + buildaccessKeyFromKId.getFriendlyKeyId());
                }

            }

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();

        } finally {
            if (connect != null) {
                connect.close();
            }
        }
        return buildaccessKeyFromKId;

    }

    @Override
    public ResultSet getCspInfo() throws SQLException {
        Statement stmt = null;
        Connection connect = null;
        ResultSet rs = null;
        try {
            // // Execute a query
            System.out.println("Updating records into the table...");
            stmt = DBConnect.connect().createStatement();

            String sql = "SELECT * FROM " + DBConnect.DB_CSP_TBL;
            rs = stmt.executeQuery(sql);

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();

        } finally {
            if (connect != null) {
                connect.close();
            }
        }
        return rs;
    }

    @Override
    public void addFriendlyIdtoStats(String username, String friendlyKeyId) throws SQLException {
        Connection connect = null;
        try {
            // // Execute a query
            System.out.println("Adding Friendly KeyId to Stats tbl");
            connect = DBConnect.connect();

            String sql =
                    "INSERT INTO " + DBConnect.DB_CLOUD_OP_STAT_TBL +
                    " (username, key_identifier, upload_count, download_count, delete_count, list_count) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = connect.prepareStatement(sql);
            preparedStmt.setString(1, username);
            preparedStmt.setString(2, friendlyKeyId);
            preparedStmt.setInt(3, 0);
            preparedStmt.setInt(4, 0);
            preparedStmt.setInt(5, 0);
            preparedStmt.setInt(6, 0);

            preparedStmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (connect != null) {
                connect.close();
            }
        }


    }

    private void deleteFriendlyIdtoStats(String username, AccessKey accessKey) throws SQLException {
        Connection connect = null;
        try {
            // // Execute a query
            System.out.println("Deleting record " + accessKey.getFriendlyKeyId() + " from the stats table");
            connect = DBConnect.connect();

            if (doesAccessKeyExist(accessKey)) {
                System.out.println("Access Key exists in DB:" + accessKey.getFriendlyKeyId());

                String delete_sql =
                        "DELETE FROM " + DBConnect.DB_CLOUD_OP_STAT_TBL+ " " +
                        "WHERE username=? and key_identifier=?";

                PreparedStatement preparedStmt = connect.prepareStatement(delete_sql);
                preparedStmt.setString(1, username);
                preparedStmt.setString(2, accessKey.getFriendlyKeyId());

                preparedStmt.execute();

            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (connect != null) {
                connect.close();
            }
        }

    }

    public boolean deleteAccountFromDb(String accountIdentifier) throws SQLException {
        Connection connect = null;
        boolean accessKeyDeleted = true;
        try {

            // // Execute a query
            System.out.println("Deleting account " + accountIdentifier);
            connect = DBConnect.connect();

            String delete_sql =
                    "DELETE FROM " + DBConnect.DB_ACCOUNT_TBL + " WHERE username=?";

            PreparedStatement ps = connect.prepareStatement(delete_sql);
            ps.setString(1, accountIdentifier);
            ps.executeUpdate();

            String accessKeyDeleteSql = "DELETE from " + DBConnect.DB_ACCESS_KEY_MGR_TBL  + " where username=?";

            PreparedStatement keyPs = connect.prepareStatement(accessKeyDeleteSql);
            keyPs.setString(1, accountIdentifier);
            keyPs.executeUpdate();

            String DeleteSqlOpsTbl = "DELETE from " + DBConnect.DB_CLOUD_OP_STAT_TBL  + " where username=?";

            PreparedStatement opsPs = connect.prepareStatement(DeleteSqlOpsTbl);
            opsPs.setString(1, accountIdentifier);
            opsPs.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (connect != null) {
                connect.close();
            }
        }

        return accessKeyDeleted;
    }

    public void exportStatsToCsv(boolean allUsers, String username) throws SQLException {

        Connection connect = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
        Date date = new Date();
        String dateStr = dateFormat.format(date);

        String filename = "cloud-stat-ops-" + dateStr + ".csv";

        try {
                String sql;
                connect = DBConnect.connect();
                if (allUsers) {
                    sql = "SELECT * FROM " + DBConnect.DB_CLOUD_OP_STAT_TBL;
                }
                else
                {
                    sql = "SELECT * FROM " + DBConnect.DB_CLOUD_OP_STAT_TBL + " where username='" + username + "'";
                }

                Statement stmt = connect.createStatement();
                FileWriter fw = new FileWriter(filename);
                ResultSet rs = stmt.executeQuery(sql);
                fw.append("username, key_identifier, upload_count, download_count, delete_count");
                fw.append('\n');
                while (rs.next()) {
                    fw.append(rs.getString(1));
                    fw.append(',');
                    fw.append(rs.getString(2));
                    fw.append(',');
                    fw.append(rs.getString(3));
                    fw.append(',');
                    fw.append(rs.getString(4));
                    fw.append(',');
                    fw.append(rs.getString(5));
                    fw.append('\n');
                }

                    fw.flush();
                    fw.close();
                System.out.println("CSV File is created successfully.");
                showAlert(Alert.AlertType.INFORMATION, "Success", filename + " exported successfully");

        } catch (SQLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connect != null) {
                connect.close();
            }
        }

    }

        private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

}
