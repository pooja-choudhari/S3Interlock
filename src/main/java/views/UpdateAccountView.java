/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { UpdateAccountView.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */

package views;

import application.Main;
import controllers.MainAccessKeyController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.NewAccountModel;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;


/**
 * This class create an update view and allows the user to update their profile
 */
public class UpdateAccountView extends Application {

    NewAccountModel accountModel;
    MainAccessKeyController mainController;

    public void addMainController(MainAccessKeyController mainController,
                                  NewAccountModel newAccountModel) {
        this.mainController = mainController;
        this.accountModel = newAccountModel;
    }

    @Override
    public void start(final Stage primaryStage) {

        final GridPane grid = new GridPane();

        Scene scene = new Scene(grid, 700, 700);
        Text scenetitle = new Text("Update Your Profile");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
        primaryStage.setScene(scene);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        primaryStage.setScene(scene);
        primaryStage.setTitle("S3Interlock");


        /* * * * * * * * * * * * * * * * * *
             Custom Stylesheet
        * * * * * * * * * * * * * * * * * */
        scene.getStylesheets().add(Main.RESOURCES_PATH + "main.css");
        grid.getStyleClass().add("grid");


        // Label and Text Field for Description
        final Label password = new Label("New Password");
        grid.add(password, 0, 2);
        final TextField passwordField = new TextField();
        grid.add(passwordField, 1, 2);

        // Click to Update Button
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
        grid.add(hbcancelBtn, 1, 9);

        Button btnUpdate = new Button("Update");
        HBox hbBtnUpdate = new HBox(10);
        hbBtnUpdate.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnUpdate.getChildren().add(btnUpdate);
        grid.add(hbBtnUpdate, 0, 9);

        // Update Button Handle
        btnUpdate.setOnAction(e -> {
            try {
                String inputPassword = passwordField.getText();
                if (StringUtils.isNullOrEmpty(inputPassword)) {
                    showAlert(Alert.AlertType.ERROR, "Form Error", "Please enter password to update");

                } else {
                    String newHashedPassword = Main.hashString(inputPassword);
                    String username = UpdateAccountView.this.mainController.getFetchRecordFromDb().dbUserName;

                    UpdateAccountView.this.accountModel.updateAccountPassword(username, newHashedPassword);
                    showAlert(Alert.AlertType.INFORMATION, "Account updated", "Password changed Success");
                }
            } catch (IllegalArgumentException | NoSuchAlgorithmException | SQLException e1) {

            }

        });

        // Create Account Button Handle
        cancelBtn.setOnAction(e -> UpdateAccountView.this.mainController.mainView(primaryStage));

        primaryStage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
