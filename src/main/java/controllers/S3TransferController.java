/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - {S3TransferController.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */
package controllers;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import models.AccessKey;
import models.AccessKeyDaoImpl;
import models.DBConnect;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;

import static com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;


public class S3TransferController {
    private static EndpointConfiguration endpointConfiguration = null;
    private AccessKeyDaoImpl accessKeyDao;

    public void addAccessKeyDao(AccessKeyDaoImpl accessKeyDao) {
        this.accessKeyDao = accessKeyDao;
    }

    public AmazonS3 createS3Client(String friendlyKeyId) throws SQLException {

        AccessKey accessKeyForClient = new AccessKey(friendlyKeyId, null, null, null);
        AccessKey accessKey = this.accessKeyDao.getFriendlyKeyInfo(accessKeyForClient);

        HashMap<String, String> cspEndpointMap = new HashMap<String, String>();
        cspEndpointMap.put("aws", "https://s3.us-west-2.amazonaws.com");
        cspEndpointMap.put("ibm", "https://s3.us-east.cloud-object-storage.appdomain.cloud");
        cspEndpointMap.put("ibm-local", "https://192.168.22.101");

        HashMap<String, String> cspRegionMap = new HashMap<String, String>();
        cspRegionMap.put("aws", "us-west-2");
        cspRegionMap.put("ibm", "chicago");
        cspRegionMap.put("ibm-local", "chicago");

        ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
        clientConfig.setUseTcpKeepAlive(true);

        endpointConfiguration = new EndpointConfiguration(cspEndpointMap.get(accessKey.getCspName()),
                                                          cspRegionMap.get(accessKey.getCspName()));

        AWSCredentials credentials = new BasicAWSCredentials(accessKey.getAccessKey(), accessKey.getSecretKey());

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(endpointConfiguration)
                .build();
    }

    public void putObject(String bucketName,
                                 String keyName,
                                 String friendlyKeyId,
                                 File file,
                                 boolean publicUpload) throws SQLException {

        AmazonS3 cos = createS3Client(friendlyKeyId);
        PutObjectResult putObj = null;
        System.out.println("Initiating upload with " + friendlyKeyId + " to " + bucketName);

        try {
            if (publicUpload){
                putObj =
                        cos.putObject(new PutObjectRequest(bucketName, keyName, file)
                                .withCannedAcl(CannedAccessControlList.PublicRead));

                showAlert(Alert.AlertType.INFORMATION, "Upload Successful",
                        "Public URL "+ keyName + ": " + endpointConfiguration.getServiceEndpoint()+"/"+bucketName+"/"+keyName);

            }
            else {
                putObj = cos.putObject(bucketName, keyName, file);
                showAlert(Alert.AlertType.INFORMATION, "Upload Successful", keyName + " uploaded" );
            }

            incrementCloudCounter(friendlyKeyId, "upload_count");

            System.out.println("Successfully Uploaded " + keyName + " with ETag " + putObj.getETag());

        }
        catch (AmazonS3Exception e){
            if (e.getErrorCode().equals("AccessDenied") | e.getErrorCode().equals("InvalidBucketName"))
            {
                showAlert(Alert.AlertType.ERROR, "Upload Error", "Bucket not owned by you!!");

            }

        }

    }

    public  void getObject(String bucketName, String keyName, String friendlyKeyId) throws SQLException {

        S3Object fullObject = null;
        AmazonS3 cos = createS3Client(friendlyKeyId);

        try {
            fullObject = cos.getObject(new GetObjectRequest(bucketName, keyName));

            incrementCloudCounter(friendlyKeyId, "download_count");

            System.out.println("Content-Type: " + fullObject.getObjectMetadata().getContentType());
            System.out.println("Content: ");

        }
        catch (AmazonS3Exception e){
            if (e.getErrorCode().equals("NoSuchKey"))
            {
                showAlert(Alert.AlertType.ERROR, "Download Error", "Key does not exist");
            }

        }

    }

    public  void deleteObject(String bucketName, String keyName, String friendlyKeyId) throws SQLException {
        System.out.println("Deleting object " + bucketName);
        AmazonS3 cos = createS3Client(friendlyKeyId);

        try{
            cos.deleteObject(bucketName, keyName);
            incrementCloudCounter(friendlyKeyId, "delete_count");
        }
        catch (AmazonS3Exception e){
            if (e.getErrorCode().equals("AccessDenied"))
            {
                showAlert(Alert.AlertType.ERROR, "Upload Error", "Bucket not owned!!");

            }
        }

    }

    public static void listObjects(String bucketName, AmazonS3 cos)
    {

        System.out.println("Listing objects in bucket " + bucketName);
        ObjectListing objectListing = cos.listObjects(new ListObjectsRequest().withBucketName(bucketName));
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
        }
        System.out.println();
    }

    public static void incrementCloudCounter(String friendlyName, String opName) throws SQLException {
        Connection connect = null;
        try {
            // // Execute a query
            System.out.println("Inserting records into the table...");
            connect = DBConnect.connect();
            Statement stmt = DBConnect.connect().createStatement();

            String sql =
                    "UPDATE " + DBConnect.DB_CLOUD_OP_STAT_TBL +
                    " SET " +  opName + " = " + opName + " + 1 " +
                    "WHERE key_identifier ='" + friendlyName + "'";

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (connect != null) {
                connect.close();
            }
        }

    }

    public ResultSet getCloudOperationCount(String username, boolean allUsers) throws SQLException {
        Statement stmt = null;
        Connection connect = null;
        ResultSet rs = null;
        try {

            // // Execute a query
            System.out.println("Updating records into the table...");
            stmt = DBConnect.connect().createStatement();
            String sql;
            if (allUsers){
                sql = "SELECT * FROM " + DBConnect.DB_CLOUD_OP_STAT_TBL;
            }
            else
            {
                sql = "SELECT * FROM " + DBConnect.DB_CLOUD_OP_STAT_TBL + " where username='" + username + "'";
            }

            rs = stmt.executeQuery(sql);

        } catch (SQLException e) {
            e.printStackTrace();

        }
        catch(NullPointerException e1)
        {
        	e1.printStackTrace();
        }
        
        finally {
            if (connect != null) {
                connect.close();
            }
        }
        return rs;
    }

    public BarChart<String, Number> displayCloudTransactionStats(MainAccessKeyController mainAccessKeyController, boolean allUser) throws SQLException {
        //Defining the x axis

        CategoryAxis xAxis = new CategoryAxis();

        xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList("Upload", "Download", "Delete")));
        xAxis.setLabel("Operations");

        //Defining the y axis
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Count");

        BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
        barChart.setTitle("Operation Count Per Friendly Key Id");

        ResultSet rs = getCloudOperationCount(mainAccessKeyController.getFetchRecordFromDb().dbUserName, allUser);
        String userLegend;
        while (rs.next()){
            System.out.println(rs.getString(1) + "  " + rs.getString(2));
            if (allUser)
            {
                userLegend = rs.getString(1) + "-" + rs.getString(2);
            }
            else
            {
                userLegend = rs.getString(2);
            }

            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
            series.setName(userLegend);
            series.getData().add(new XYChart.Data<String, Number>("Upload", rs.getInt(3)));
            series.getData().add(new XYChart.Data<String, Number>("Download", rs.getInt(4)));
            series.getData().add(new XYChart.Data<String, Number>("Delete", rs.getInt(5)));
            series.getData().add(new XYChart.Data<String, Number>("List", rs.getInt(6)));

            barChart.getData().add(series);

        }
        return barChart;
    }

    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }


}
