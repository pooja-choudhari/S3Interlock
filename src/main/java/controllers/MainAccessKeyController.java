/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - {MainAccessKeyController.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */
package controllers;

import javafx.stage.Stage;
import models.FetchRecordFromDb;
import views.*;

import java.sql.SQLException;

public class MainAccessKeyController {

    MainAccessKeyView mainAccessKeyView;

    AddAccessKeyView addAccessKeyView;
    ListAccessKeyView listAccessKeyView;
    UpdateAccessKeyView updateAccessKeyView;
    DeleteAccessKeyView deleteAccessKeyView;

    S3OperationCountChartView s3OperationCountChartView;
    S3AllUserOperationView s3AllUserOperationView;
    S3TransferView s3TransferView;

    UpdateAccountView updateAccountView;
    FetchRecordFromDb fetchRecordFromDb;
    DeleteAccountView deleteAccountView;

    public void setFetchRecordFromDb( FetchRecordFromDb fetchRecordFromDb) {
        this.fetchRecordFromDb = fetchRecordFromDb;
    }
    public FetchRecordFromDb getFetchRecordFromDb() {
        return fetchRecordFromDb;
    }

    public void addMainAccessView(MainAccessKeyView mainAccessKeyView) {
        this.mainAccessKeyView = mainAccessKeyView;
    }

    /* * * * * * * * * * * * * * * * * *
   Main Access Key View Page
   * * * * * * * * * * * * * * * * * */
    public void mainView(Stage primaryStage) {
        this.mainAccessKeyView.start(primaryStage);
    }

    /* * * * * * * * * * * * * * * * * *
    Display CRUD Access Key Views
     * * * * * * * * * * * * * * * * * */
    public void displayAddAccessKey(Stage primaryStage) {
        MainAccessKeyController.this.addAccessKeyView.start(primaryStage);

    }

    /* * * * * * * * * * * * * * * * * *
    List Access Keys
     * * * * * * * * * * * * * * * * * */
    public void displayListAccessKeys(Stage primaryStage) {
       this.listAccessKeyView.start(primaryStage);
    }

    /* * * * * * * * * * * * * * * * * *
     Update User Access Key Information
     * * * * * * * * * * * * * * * * * */
    public void displayUpdateAccessKeyInfo(Stage primaryStage) {
       this.updateAccessKeyView.start(primaryStage);
    }

    /* * * * * * * * * * * * * * * * * *
     Delete User Access Key
     * * * * * * * * * * * * * * * * * */
    public void displayDeleteAccessKeyView(Stage primaryStage) {
       this.deleteAccessKeyView.start(primaryStage);
    }

    /* * * * * * * * * * * * * * * * * *
     S3 Transfer View
     * * * * * * * * * * * * * * * * * */
    public void displayS3TransferView(Stage primaryStage) {
       this.s3TransferView.start(primaryStage);
    }

    /* * * * * * * * * * * * * * * * * *
     S3 Stats
     * * * * * * * * * * * * * * * * * */
    public void displayS3StatView(Stage primaryStage) throws SQLException {
        MainAccessKeyController.this.s3OperationCountChartView.start(primaryStage);
    }

    public void displayAllS3StatView(Stage primaryStage) throws SQLException {
        MainAccessKeyController.this.s3AllUserOperationView.start(primaryStage);
    }

    public void addViews(AddAccessKeyView addAccessKeyView,
                         DeleteAccessKeyView deleteAccessKeyView,
                         ListAccessKeyView listAccessKeyView,
                         UpdateAccessKeyView updateAccessKeyView,

                         S3OperationCountChartView s3OperationCountChartView,
                         S3TransferView s3TransferView,
                         S3AllUserOperationView s3AllUserOperationView,

                         UpdateAccountView updateAccountView,
                         DeleteAccountView deleteAccountView) {

        this.addAccessKeyView = addAccessKeyView;
        this.listAccessKeyView = listAccessKeyView;
        this.updateAccessKeyView = updateAccessKeyView;
        this.deleteAccessKeyView = deleteAccessKeyView;
        this.s3OperationCountChartView = s3OperationCountChartView;
        this.s3TransferView = s3TransferView;
        this.updateAccountView = updateAccountView;
        this.s3AllUserOperationView = s3AllUserOperationView;
        this.deleteAccountView = deleteAccountView;
    }

    public void updateAccountView(Stage primaryStage) {
        MainAccessKeyController.this.updateAccountView.start(primaryStage);
    }

    public void displayDeleteAccountView(Stage primaryStage) {
        MainAccessKeyController.this.deleteAccountView.start(primaryStage);
    }
}