/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { DeleteAccountView.java}
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
import models.AccessKeyDaoImpl;

import java.sql.SQLException;


public class DeleteAccountView extends Application {

    AccessKeyDaoImpl accessKeyDaoImpl;
    MainAccessKeyController mainController;

    public void addMainController(final MainAccessKeyController mainController, AccessKeyDaoImpl accessKeyDao) {
        this.mainController = mainController;
        this.accessKeyDaoImpl = accessKeyDao;
    }

    @Override
    public void start(final Stage primaryStage) {
        try {

            final GridPane grid = new GridPane();
            Scene scene = new Scene(grid, 700, 700);
            primaryStage.setScene(scene);
            primaryStage.setTitle("S3Interlock");
            primaryStage.show();
            Text scenetitle = new Text("Welcome");
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
            Label deleteAccountNote = new Label("WARN: This action will permanently delete the account");
            deleteAccountNote.setTextFill(Color.FIREBRICK);
            grid.add(deleteAccountNote, 0, 1);

            Label accountId = new Label("Delete Account");
            grid.add(accountId, 0, 2);
            final TextField accountIdField = new TextField();
            grid.add(accountIdField, 1, 2);

            Button deleteAccessKeyBtn = new Button("Delete");
            HBox hbdeleteAccessKeyBtn = new HBox(10);
            hbdeleteAccessKeyBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbdeleteAccessKeyBtn.getChildren().add(deleteAccessKeyBtn);
            grid.add(hbdeleteAccessKeyBtn, 0, 6);

            Image imgHome = new Image(Main.RESOURCES_PATH + "home.png");
            ImageView imgView = new ImageView(imgHome);
            imgView.setFitWidth(20);
            imgView.setFitHeight(20);
            imgView.setPreserveRatio(true);

            Button cancelBtn = new Button("Back");
            HBox cancelBtnHb = new HBox(10);
            cancelBtnHb.setAlignment(Pos.BOTTOM_LEFT);
            cancelBtnHb.getChildren().add(cancelBtn);
            grid.add(cancelBtnHb, 1, 6);

            final Text actiontarget = new Text();

            deleteAccessKeyBtn.setOnAction(e -> {
                try {
                    String accountIdentifier = accountIdField.getText();

                    if (StringUtils.isNullOrEmpty(accountIdentifier)) {
                        System.out.println("Missing Account Username to delete");
                        showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter your user account to delete");
                        return;

                    } else {

                        System.out.println("Are you sure you want to permanently delete: " + accountIdentifier);

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                                "Delete Account ? ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

                        alert.showAndWait();

                        if (alert.getResult() == ButtonType.YES) {

                            System.out.println("Deleting User account " + accountIdentifier);
                            if (accessKeyDaoImpl.deleteAccountFromDb(accountIdentifier)) {
                                throwError(grid, actiontarget, "Deleted " + accountIdentifier + " successfully to DB");
                            } else {
                                throwError(grid, actiontarget, accountIdentifier + " does not exist");
                            }
                        }
                    }
                } catch (NullPointerException | SQLException e1) {
                    showAlert(Alert.AlertType.ERROR, "Form Error!", "Unable to delete Access Key");
                    return;
                }
            });

            cancelBtn.setOnAction(e -> DeleteAccountView.this.mainController.mainView(primaryStage));

        } catch (Exception e) {
            System.out.println("Delete Account view exited unexpectedly." + e.getMessage());
        }

    }

    private Alert showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();

        return alert;
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

