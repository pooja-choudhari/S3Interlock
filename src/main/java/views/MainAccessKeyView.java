/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { MainAccessKeyView.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */

package views;

import application.Main;
import controllers.AccountController;
import controllers.MainAccessKeyController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.sql.SQLException;

//import com.mysql.cj.util.StringUtils;

/**
 * This class has the responsibility to create a view for new account creation.
 */
public class MainAccessKeyView extends Application {

    MainAccessKeyController mainAccessKeyController;
    AccountController accountController;

    MenuItem menuItemDelete;
    MenuItem menuItemAdd;
    MenuItem menuItemRead;
    MenuItem menuItemUpdate;
    MenuItem uploadFile;
    MenuItem s3Stats;
    MenuItem updateAccount;
    MenuItem deleteAccount;
    MenuItem s3AllStats;

    public void addController(final MainAccessKeyController mainAccessKeyController, final AccountController accountController) {
        this.mainAccessKeyController = mainAccessKeyController;
        this.accountController = accountController;
    }

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(final Stage primaryStage) {
        try {

            BorderPane root = new BorderPane();

            GridPane grid = new GridPane();
            final Scene scene = new Scene(root, 700, 700, Color.GRAY);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Main Access Key View");
            primaryStage.show();

            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);

            /* * * * * * * * * * * * * * * * * *
             Custom Stylesheet
             * * * * * * * * * * * * * * * * * */
            root.getStylesheets().add(Main.RESOURCES_PATH + "main.css");
            root.getStyleClass().add("grid");

//            final TextField userTextField = new TextField();
//            userTextField.setPrefWidth(10);
//            grid.add(userTextField, 1, 2);

            /* * * * * * * * * * * * * * * * * *
             Welcome the Logged In User
             * * * * * * * * * * * * * * * * * */
            final Label lblUserName =
                    new Label("Welcome, " + this.mainAccessKeyController.getFetchRecordFromDb().dbUserName);
            lblUserName.setFont(new Font("Arial", 20));
            lblUserName.setTextFill(Color.web("black"));
            grid.add(lblUserName, 1, 1);

            /* * * * * * * * * * * * * * * * * *
             Logged In User Image Description
             * * * * * * * * * * * * * * * * * */

            Image img = new Image("file:photo.jpg");
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(300);
            imgView.setFitHeight(300);
            imgView.setPreserveRatio(true);

            grid.add(imgView, 1, 2);


            Text text = new Text();
            text.setFont(new Font(20));
            text.setTextAlignment(TextAlignment.JUSTIFY);
            text.setText(
                    "Users can create/read/update/delete access keys from different Cloud Providers.\n" +
                    "Users can upload/download/delete files to/from cloudn\n" +
                    "Users can view their cloud usage on a per key basis");

//            grid.add(text, 1, 3);

            /* * * * * * * * * * * * * * * * * *
             Top Menu Bar
             * * * * * * * * * * * * * * * * * */
            MenuBar menuBar = new MenuBar();
            menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
            root.setTop(menuBar);
            menuBar.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY)));

            Menu accessKeyMenu = new Menu("Access Key");
            Menu s3TransferMenu = new Menu("S3 Transfer");
            Menu settingMenu = new Menu("Settings");

            /* * * * * * * * * * * * * * * * * *
             Top Menu Bar End
             * * * * * * * * * * * * * * * * * */


            /* * * * * * * * * * * * * * * * * *
             Check if user is root user
             * * * * * * * * * * * * * * * * * */
            if (this.mainAccessKeyController.getFetchRecordFromDb().dbUserName.equals("admin")) {
                deleteAccount = new MenuItem("Delete Account");
                settingMenu.getItems().add(deleteAccount);

                s3AllStats = new MenuItem("Display S3 Stats");
                s3TransferMenu.getItems().add(s3AllStats);

                s3AllStats.setOnAction(e -> {
                    try {
                        this.mainAccessKeyController.displayAllS3StatView(primaryStage);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                });

                deleteAccount.setOnAction(event -> this.mainAccessKeyController.displayDeleteAccountView(primaryStage));


            /* * * * * * * * * * * * * * * * * *
             If user is regular user
             * * * * * * * * * * * * * * * * * */

            } else {

                menuItemAdd = new MenuItem("Add Access Key");
                menuItemRead = new MenuItem("List Access Keys");
                menuItemUpdate = new MenuItem("Update Access Key");
                menuItemDelete = new MenuItem("Delete Access Key");

                accessKeyMenu.getItems().addAll(menuItemAdd, menuItemRead, menuItemUpdate, menuItemDelete);

                menuItemAdd.setOnAction(e -> this.mainAccessKeyController.displayAddAccessKey(primaryStage));

                menuItemRead.setOnAction(e -> {
                    try {
                        this.mainAccessKeyController.displayListAccessKeys(primaryStage);

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });

                menuBar.getMenus().add(accessKeyMenu);

                menuItemUpdate.setOnAction(e -> this.mainAccessKeyController.displayUpdateAccessKeyInfo(primaryStage));

                menuItemDelete.setOnAction(e -> this.mainAccessKeyController.displayDeleteAccessKeyView(primaryStage));


            /* * * * * * * * * * * * * * * * * *
             S3 Transfer DropDown
             * * * * * * * * * * * * * * * * * */
                uploadFile = new MenuItem("S3 Operations");
                s3Stats = new MenuItem("Display S3 Stats");
                s3TransferMenu.getItems().add(uploadFile);
                s3TransferMenu.getItems().add(s3Stats);

                uploadFile.setOnAction(e -> this.mainAccessKeyController.displayS3TransferView(primaryStage));

                s3Stats.setOnAction(e -> {
                    try {
                        this.mainAccessKeyController.displayS3StatView(primaryStage);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                });

            /* * * * * * * * * * * * * * * * * *
             Settings
             * * * * * * * * * * * * * * * * * */

                MenuItem csp = new MenuItem("Supported Cloud Service Providers");
                updateAccount = new MenuItem("Update Account Password");

//                settingMenu.getItems().add(csp);
                settingMenu.getItems().add(updateAccount);

                updateAccount.setOnAction(event ->
                        this.mainAccessKeyController.updateAccountView(primaryStage));
            }

            MenuItem logout = new MenuItem("Logout");
            MenuItem exit = new MenuItem("Exit");
            settingMenu.getItems().add(logout);
            settingMenu.getItems().add(exit);

            menuBar.getMenus().addAll(s3TransferMenu, settingMenu);

            root.setCenter(grid);


        /* * * * * * * * * * * * * * * * * *
         Event Handlers for DropDown MenuItems
         * * * * * * * * * * * * * * * * * */

            exit.setOnAction(e -> System.exit(0));

            logout.setOnAction(e -> this.accountController.showLoginScreen(primaryStage));

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }

}

