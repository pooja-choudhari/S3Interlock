/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { DeleteAccessKeyView.java}
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

import java.sql.SQLException;


/**
 * This class has the responsibility to create a view for the users where
 * existing users can login into the leader board system and new users can
 * create their profile.
 */
public class DeleteAccessKeyView extends Application {

    AccessKeyDaoImpl accessKeyDaoImpl;
    MainAccessKeyController mainController;

    public void addMainController(final MainAccessKeyController mainController, AccessKeyDaoImpl accessKeyDao){
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
//            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            grid.setPadding(new Insets(25, 25, 25, 25));
            scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            grid.add(scenetitle, 0, 0, 2, 1);

            /* * * * * * * * * * * * * * * * * *
             Custom Stylesheet
             * * * * * * * * * * * * * * * * * */
            scene.getStylesheets().add(Main.RESOURCES_PATH + "main.css");
            grid.getStyleClass().add("grid");


            // Label and TextField for UserName
            Label deleteAccessKeyNote = new Label("WARN: This action will fail all operation to Cloud");
            deleteAccessKeyNote.setTextFill(Color.FIREBRICK);
            grid.add(deleteAccessKeyNote, 0, 1);

            Label kId = new Label("Delete Friendly Key Identifier(KId)");
            grid.add(kId, 0, 2);
            final TextField kIdField = new TextField();
            grid.add(kIdField, 1, 2);

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
            cancelBtn.setGraphic(imgView);
            // Create New Account Button
            HBox cancelHb = new HBox(10);
            cancelHb.setAlignment(Pos.BOTTOM_LEFT);
            cancelHb.getChildren().add(cancelBtn);
            grid.add(cancelHb, 1, 6);

            final Text actiontarget = new Text();

            deleteAccessKeyBtn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent e) {
                    try {
                        String kIdentifier = kIdField.getText();

                        if (StringUtils.isNullOrEmpty(kIdentifier)) {
                            System.out.println("Missing Access Key to Delete ");
                            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter your Friendly Key Identifier (kId)");
                            return;

                        } else {

                            System.out.println("Are you sure you want to permanently delete: " + kIdentifier);

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                                    "Delete Access Key", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

                            alert.showAndWait();

                            if (alert.getResult() == ButtonType.YES) {
                                AccessKey accessKey = new AccessKey(kIdentifier, null, null, null);

                                System.out.println("Deleting Key Identifier " + kIdentifier);
                                if (accessKeyDaoImpl.deleteAccessKeyFromdb(accessKey))
                                {
                                    throwError(grid, actiontarget, "Deleted " + kIdentifier + " successfully to DB");
                                }
                                else{
                                    throwError(grid, actiontarget, kIdentifier + " does not exist");
                                }
                            }
                        }
                    } catch (NullPointerException | SQLException e1) {
                        showAlert(Alert.AlertType.ERROR, "Form Error!", "Unable to delete Access Key");
                        return;
                    }
                }

            });

            cancelBtn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent e) {
                    DeleteAccessKeyView.this.mainController.mainView(primaryStage);
                }

            });

        } catch (Exception e) {
            System.out.println("Delete Access Key View crashed " + e.getMessage());
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

