
/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { LoginModel.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */


package models;

import application.Main;
import javafx.scene.image.Image;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginModel {

    private final static int NAME_INDEX = 1;
    private final static int DESC_INDEX = 3;
    private final static int IMAGE_INDEX = 4;

    public Image getImage(InputStream is) throws IOException {
        OutputStream os = new FileOutputStream(new File("photo.jpg"));
        byte[] content = new byte[1024];
        int size = 0;
        while ((size = is.read(content)) != -1) {
            os.write(content, 0, size);
        }
        os.close();
        is.close();
        return new Image("file:photo.jpg", 100, 150, true, true);
    }



    public FetchRecordFromDb login(String userName, String password) throws SQLException {

        Connection connect = null;
        FetchRecordFromDb dbAccountRecord = null;
        try {
            // Execute a query

            System.out.println("Checking " + userName + " exist in " + DBConnect.DB_ACCOUNT_TBL);
            connect = DBConnect.connect();

            String userHashedPwd = Main.hashString(password);
            Statement stmt = connect.createStatement();

            String sql = "Select * from " + DBConnect.DB_ACCOUNT_TBL + " where username = '" + userName + "' and password='" + userHashedPwd+ "'";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                dbAccountRecord = processResult(rs);
            }

        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            if (connect != null) {
                connect.close();
            }
        }
        return dbAccountRecord;

    }

    private FetchRecordFromDb processResult(ResultSet rs) throws SQLException {

        String name = (String) rs.getObject(NAME_INDEX);
        String desc = (String) rs.getObject(DESC_INDEX);

        InputStream binaryStream = rs.getBinaryStream(IMAGE_INDEX);
        Image image = null;

        try {
            image = getImage(binaryStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in loading image");
        }

        return new FetchRecordFromDb(name, desc, image);
    }


}
