/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { S3TransferView.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */

package views;

import application.Main;
import controllers.MainAccessKeyController;
import controllers.S3TransferController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import models.AccessKeyDaoImpl;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

//import com.mysql.cj.util.StringUtils;

/**
 * This class has the responsibility to create a view for new account creation.
 */
public class S3TransferView extends Application {

    File file;
    String friendlyKeyIdFromComboBox;
    S3TransferController s3TransferController;


    MainAccessKeyController mainController;
    AccessKeyDaoImpl accessKeyDao;

    public void addMainController(final MainAccessKeyController mainController, AccessKeyDaoImpl accessKeyDao, S3TransferController s3TransferController){
        this.mainController = mainController;
        this.accessKeyDao = accessKeyDao;
        this.s3TransferController = s3TransferController;

    }

    public ComboBox<String> addToComboBox() throws SQLException {

        ObservableList<String> options = FXCollections.observableArrayList();

        ResultSet rs = this.accessKeyDao.displayAccessKeysInfo();

        while (rs.next()) {  // loop

            // Now add the comboBox addAll statement
            options.add(rs.getString(2));

        }

        if (options.size() == 0) {
            options.add("No Access Key Found");
        }

        return new ComboBox<>(options);

    }

    @Override
    public void start(final Stage primaryStage) {
        try {

            // Grid and Scene
            final GridPane grid = new GridPane();

            Text scenetitle = new Text("Welcome");
            Scene scene = new Scene(grid, 700, 700);
            primaryStage.setScene(scene);

            // sets title for window
            primaryStage.setTitle("S3Interlock Project");
            primaryStage.show();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));
            scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            grid.add(scenetitle, 0, 0, 5, 1);

            /* * * * * * * * * * * * * * * * * *
             Custom Stylesheet
             * * * * * * * * * * * * * * * * * */
            grid.getStylesheets().add(Main.RESOURCES_PATH + "main.css");
            grid.getStyleClass().add("grid");


            /* * * * * * * * * * * * * * * * * *
             Drop down for friendly key Id
             * * * * * * * * * * * * * * * * * */
            Label friendlyKId = new Label("Friendly Key Identifier");
            grid.add(friendlyKId, 0, 4);
            ComboBox<String> comboBox = addToComboBox();
            comboBox.getSelectionModel().selectedItemProperty()
                    .addListener(new ChangeListener<String>() {
                        public void changed(ObservableValue<? extends String> observable,
                                            String oldValue, String newValue) {
                            friendlyKeyIdFromComboBox = newValue;
                            System.out.println("Value is: " + newValue + " from drop down");
                        }
                    });

            grid.add(comboBox, 1, 4);

            /* * * * * * * * * * * * * * * * * *
             Bucket Name
             * * * * * * * * * * * * * * * * * */
            Label bucketNameLbl = new Label("Bucket Name");
            grid.add(bucketNameLbl, 0, 5);
            TextField bucketNameLblField = new TextField();
            grid.add(bucketNameLblField, 1, 5);

            /* * * * * * * * * * * * * * * * * *
             File Upload
             * * * * * * * * * * * * * * * * * */
            Label uploadFilelbl = new Label("Upload File Name");
            grid.add(uploadFilelbl, 0, 6);
            TextField uploadFileField = new TextField();
            grid.add(uploadFileField, 1, 6);

            Button uploadFileBtn = new Button("Upload!!");
            HBox uploadFileHb = new HBox(10);
            uploadFileHb.setAlignment(Pos.TOP_RIGHT);
            uploadFileHb.getChildren().add(uploadFileBtn);
            grid.add(uploadFileBtn, 2, 6);
            uploadFileBtn.setTooltip(new Tooltip("This will send an HTTP request to the server"));

            Button browseBtn = new Button("Browse a File");
            HBox hbbrowseBtn = new HBox(10);
            hbbrowseBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbbrowseBtn.getChildren().add(browseBtn);
            grid.add(hbbrowseBtn, 1, 7);

            CheckBox cbPublicAccess = new CheckBox("Public Access");
            grid.add(cbPublicAccess, 3, 6);


            /* * * * * * * * * * * * * * * * * *
             File Download
             * * * * * * * * * * * * * * * * * */

            Label downloadFilelbl = new Label("Download File Name");
            grid.add(downloadFilelbl, 0, 10);
            TextField downloadFileField = new TextField();
            grid.add(downloadFileField, 1, 10);

            Button downloadFileBtn = new Button("Download!!");
            HBox downloadFilehb = new HBox(10);
            downloadFilehb.setAlignment(Pos.TOP_RIGHT);
            downloadFilehb.getChildren().add(downloadFileBtn);
            grid.add(downloadFilehb, 2, 10);


            /* * * * * * * * * * * * * * * * * *
             File Deletes
             * * * * * * * * * * * * * * * * * */
            Label deleteFilelbl = new Label("Delete File Name");
            grid.add(deleteFilelbl, 0, 11);
            TextField deleteFileField = new TextField();
            grid.add(deleteFileField, 1, 11);

            Button deleteFileBtn = new Button("Delete!!");
            HBox deleteFilehb = new HBox(10);
            deleteFilehb.setAlignment(Pos.TOP_RIGHT);
            deleteFilehb.getChildren().add(deleteFileBtn);
            grid.add(deleteFilehb, 2, 11);



            final TextArea textArea = new TextArea();
            final FileChooser fileChooser;
            fileChooser = new FileChooser();

            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files",
                    "*.txt", "*.pdf", "*.docx", "*.png", "*.jpg", "*.gif"));

            // get the file form the desktop
            textArea.setFont(Font.font("SanSerif", 12));
            textArea.setPromptText("Absolute Path of File to upload");
            textArea.setPrefSize(300, 50);
            textArea.setEditable(false);

            Image imgHome = new Image(Main.RESOURCES_PATH + "home.png");
            ImageView imgView = new ImageView(imgHome);
            imgView.setFitWidth(20);
            imgView.setFitHeight(20);
            imgView.setPreserveRatio(true);

            Button cancelBtn = new Button("Back");
            cancelBtn.setGraphic(imgView);

            HBox hbcancelBtn = new HBox(10);
            hbcancelBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbcancelBtn.getChildren().add(cancelBtn);
            grid.add(hbcancelBtn, 1, 15);

            final Text actiontarget = new Text();

            /* * * * * * * * * * * * * * * * * *
             Cancel Action
             * * * * * * * * * * * * * * * * * */
            cancelBtn.setOnAction(e -> S3TransferView.this.mainController.mainView(primaryStage));


            /* * * * * * * * * * * * * * * * * *
             Action Event for Upload
             * * * * * * * * * * * * * * * * * */
            uploadFileBtn.setOnAction(e -> {
                boolean publicAccess = false;

                try {
                    String bucketName = bucketNameLblField.getText();
                    String keyName = uploadFileField.getText();
                    if (StringUtils.isNullOrEmpty(bucketName)) {
                        showAlert(Alert.AlertType.ERROR, "Form Error!", "Please provide bucketName");
                        return;
                    }

                    if (StringUtils.isNullOrEmpty(keyName)) {
                        showAlert(Alert.AlertType.ERROR, "Form Error!", "Please provide keyName");
                        return;
                    }

                    if (file == null) {
                        showAlert(Alert.AlertType.ERROR, "Form Error!", "Please select a file to upload");
                        return;
                    }


                    String objectName = uploadFileField.getText();

                    if (StringUtils.isNullOrEmpty(objectName)) {
                        System.out.println("Missing object Name to upload");
                        showAlert(Alert.AlertType.ERROR, "Form Error!", "Missing object Name to upload");
                        return;

                    } else {
                        if (cbPublicAccess.isSelected()) {
                            publicAccess = true;
                        }

                        System.out.println("Uploading " + objectName + ". . . ");
                        this.s3TransferController.putObject(bucketName, keyName, friendlyKeyIdFromComboBox, file, publicAccess);

                    }
                } catch (NullPointerException | SQLException e1) {
                    e1.printStackTrace();
                    throwError(grid, actiontarget, "Unable to upload object to cloud");
                }
            });


            /* * * * * * * * * * * * * * * * * *
             Action Event for Download
             * * * * * * * * * * * * * * * * * */
            downloadFileBtn.setOnAction(e -> {
                try {
                    String bucketName = bucketNameLblField.getText();
                    String keyName = downloadFileField.getText();
                    if (StringUtils.isNullOrEmpty(bucketName)) {
                        showAlert(Alert.AlertType.ERROR, "Form Error!", "Please provide bucketName");
                        return;
                    }

                    if (StringUtils.isNullOrEmpty(keyName)) {
                        showAlert(Alert.AlertType.ERROR, "Form Error!", "Please provide keyName");
                        return;
                    } else {
                        System.out.println("Downloading " + keyName + ". . . ");
                        S3TransferView.this.s3TransferController.getObject(bucketName, keyName, friendlyKeyIdFromComboBox);

                    }
                } catch (NullPointerException | SQLException e1) {
                    e1.printStackTrace();
                    throwError(grid, actiontarget, "Unable to Download Object");
                }
            });


            /* * * * * * * * * * * * * * * * * *
             Action Event for Deletes
             * * * * * * * * * * * * * * * * * */

            deleteFileBtn.setOnAction(e -> {
                try {
                    String bucketName = bucketNameLblField.getText();
                    String keyName = deleteFileField.getText();
                    if (StringUtils.isNullOrEmpty(bucketName)) {
                        showAlert(Alert.AlertType.ERROR, "Form Error!", "Please provide bucketName");
                        return;
                    }

                    if (StringUtils.isNullOrEmpty(keyName)) {
                        showAlert(Alert.AlertType.ERROR, "Form Error!", "Please provide keyName");
                        return;
                    } else {
                        System.out.println("Downloading " + keyName + ". . . ");
                        S3TransferView.this.s3TransferController.deleteObject(bucketName, keyName, friendlyKeyIdFromComboBox);

                    }
                } catch (NullPointerException | SQLException e1) {
                    e1.printStackTrace();
                    throwError(grid, actiontarget, "Unable to Download Object");
                }
            });

            /* * * * * * * * * * * * * * * * * *
             Browse file to upload
             * * * * * * * * * * * * * * * * * */
            browseBtn.setOnAction(e -> {

                file = fileChooser.showOpenDialog(primaryStage);

                if (file != null) {

                    textArea.setText(file.getAbsolutePath());
                    grid.add(textArea, 1, 9);
                }

            });

        } catch (Exception e) {
            System.out.println("S3 Transfer closed unexpectedly." + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void throwError(GridPane grid, final Text actiontarget, String error) {
        try {
            actiontarget.setFill(Color.FIREBRICK);
            actiontarget.setText(error);
            grid.add(actiontarget, 1, 20);
        } catch (IllegalArgumentException e) {
        }
    }


}
