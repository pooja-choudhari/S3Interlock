/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { AddAccessKeyView.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */


package views;

//import com.mysql.cj.util.StringUtils;
import application.Main;
import controllers.MainAccessKeyController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.Stage;
import models.AccessKey;
import models.AccessKeyDaoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class has the responsibility to create a view for new account creation.
 */
public class AddAccessKeyView extends Application {

    String cloudServiceProviderName;

    AccessKeyDaoImpl accessKeyDaoImpl = new AccessKeyDaoImpl();

    private MainAccessKeyController mainAccessKeyController;

     public void addMainAccessKeyController(MainAccessKeyController mainAccessKeyController, AccessKeyDaoImpl accessKeyDaoImpl){
        this.mainAccessKeyController = mainAccessKeyController;
        this.accessKeyDaoImpl = accessKeyDaoImpl;
    }

    public ComboBox<String> addCSPIdComboBox() throws SQLException, ClassNotFoundException
    {

        return getStringComboBox(this.accessKeyDaoImpl);

    }

    static ComboBox<String> getStringComboBox(AccessKeyDaoImpl accessKeyDaoImpl) throws SQLException {
        ObservableList<String> options = FXCollections.observableArrayList();

        ResultSet rs = accessKeyDaoImpl.getCspInfo();

        while (rs.next()) {  // loop

            // Now add the comboBox addAll statement
            options.add(rs.getString("csp_name"));

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
            primaryStage.setTitle("S3Interlock");
            primaryStage.show();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);

            grid.setPadding(new Insets(25, 25, 25, 25));
            scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            grid.add(scenetitle, 0, 0, 2, 1);

            /* * * * * * * * * * * * * * * * * *
             Custom Stylesheet
             * * * * * * * * * * * * * * * * * */
            scene.getStylesheets().add(Main.RESOURCES_PATH + "main.css");
            grid.getStyleClass().add("grid");

            // Label and TextField for UserName
            Label friendlyKeyId = new Label("Friendly Key Identifier");
            grid.add(friendlyKeyId, 0, 2);
            final TextField friendlyKeyIdField = new TextField();
            grid.add(friendlyKeyIdField, 1, 2);

            Label AccessKeyId = new Label("Access Key");
            grid.add(AccessKeyId, 0, 3);
            final TextField accessKeyIdField = new TextField();
            grid.add(accessKeyIdField, 1, 3);

            // Label and Text field for password
            Label secretKey = new Label("Secret Key");
            grid.add(secretKey, 0, 4);
            final TextField secretKeyField = new TextField();
            grid.add(secretKeyField, 1, 4);

            Label friendlyKId = new Label("Cloud Service Provider");
            grid.add(friendlyKId, 0, 5);
            ComboBox<String> comboBox = addCSPIdComboBox();

            comboBox.getSelectionModel().selectedItemProperty()
                    .addListener(new ChangeListener<String>() {
                        public void changed(ObservableValue<? extends String> observable,
                                            String oldValue, String newValue) {
                            cloudServiceProviderName = newValue;
                            System.out.println("Value is: "+newValue + " from drop down");
                        }
                    });

            grid.add(comboBox, 1,5);

            // Create Account Button
            Button addAccessKeyBtn = new Button("Add Access Key");
            HBox hbaddAccessKeyBtn = new HBox(10);
            hbaddAccessKeyBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbaddAccessKeyBtn.getChildren().add(addAccessKeyBtn);
            grid.add(hbaddAccessKeyBtn, 0, 10);

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
            grid.add(hbcancelBtn, 1, 10);

            final Text actiontarget = new Text();

            // browse button handle
            addAccessKeyBtn.setOnAction(new EventHandler<ActionEvent>() {

                public void handle(ActionEvent e) {
                    try {
                        String friendlyKeyName = friendlyKeyIdField.getText();
                        String accessKey = accessKeyIdField.getText();
                        String secretKey = secretKeyField.getText();

                        if (StringUtils.isNullOrEmpty(friendlyKeyName))
                        {
                            showAlert(Alert.AlertType.ERROR, "Form Error!", "Friendly Key Name Field is missing ");
                            return;
                        }
                        if (StringUtils.isNullOrEmpty(accessKey))
                        {
                            showAlert(Alert.AlertType.ERROR, "Form Error!", "Access Key Field is missing ");
                            return;
                        }

                        if (StringUtils.isNullOrEmpty(secretKey))
                        {
                            showAlert(Alert.AlertType.ERROR, "Form Error!", "Secret Key Field is missing ");

                        }

                        if (StringUtils.isNullOrEmpty(cloudServiceProviderName))
                        {
                            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please select a CSP");

                        }

                        else {

                            AccessKey accessKeyModel = new AccessKey(friendlyKeyName, accessKey, secretKey, cloudServiceProviderName);
                            System.out.println("Adding Access Key " + accessKey + " for with KId " + " " + friendlyKeyName);

                            if (accessKeyDaoImpl.doesAccessKeyExist(accessKeyModel))
                            {
                                showAlert(Alert.AlertType.WARNING, "Form Error!",
                                        "Friendly Key Identifier: " + accessKeyModel.getFriendlyKeyId()+ " exists.");
                            }
                            else {

                                accessKeyDaoImpl.addAccessKeyToDb(accessKeyModel);
                                showAlert(Alert.AlertType.INFORMATION, "Form Successful!",
                                        "Friendly Key Identifier: " + accessKeyModel.getFriendlyKeyId()+ " added.");
                            }
                        }
                    } catch (NullPointerException | SQLException e1) {
                        throwError(grid, actiontarget, "Unable to Add Access Key to Db, Please retry");

                    }
                }

            });

            // Create Account Button Handle
            cancelBtn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent e) {
                        AddAccessKeyView.this.mainAccessKeyController.mainView(primaryStage);
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Add Access Key View crashed " + e.getMessage());
        }

    }

    private void throwError(GridPane grid, final Text actiontarget, String error) {
        try {
            actiontarget.setFill(Color.FIREBRICK);
            actiontarget.setText(error);
            grid.add(actiontarget, 1, 20);
        } catch (IllegalArgumentException e) {
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
