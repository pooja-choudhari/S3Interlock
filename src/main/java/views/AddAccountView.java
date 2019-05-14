/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { AddAccountView.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */


package views;

import application.Main;
import controllers.AccountController;
import controllers.LoginController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import models.UserProfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

//import com.mysql.cj.util.StringUtils;


public class AddAccountView extends Application {
    public AccountController accountController;
    public LoginController loginController;
    public File file;

    public void addController(final AccountController controller, final LoginController loginController) {
        this.accountController = controller;
        this.loginController = loginController;

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
            primaryStage.setTitle("Add a New Account");
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
            Label userName = new Label("User Name");
            grid.add(userName, 0, 1);
            final TextField userTextField = new TextField();
            grid.add(userTextField, 1, 1);

            // Label and Text field for password
            Label pw = new Label("Password");
            grid.add(pw, 0, 2);
            final PasswordField pwBox = new PasswordField();
            grid.add(pwBox, 1, 2);

            // Label and TextField for Description
            Label description = new Label("Description");
            grid.add(description, 0, 3);
            final TextField userTextField2 = new TextField();
            grid.add(userTextField2, 1, 3);

            // Label for Profile Image
            Label image = new Label("Select Profile Image");
            grid.add(image, 0, 4);

            // Browse Button
            Button ProfileBtn = new Button("Browse");
            HBox hbProfileBtn = new HBox(10);
            hbProfileBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbProfileBtn.getChildren().add(ProfileBtn);
            grid.add(hbProfileBtn, 1, 4);

            // Create Account Button
            Button createAccountbtn = new Button("Create Account");
            HBox hbBtn = new HBox(10);
            hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbBtn.getChildren().add(createAccountbtn);
            grid.add(hbBtn, 0, 6);


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
            grid.add(hbcancelBtn, 1, 6);

            final Text actiontarget = new Text();

            // browse button handle
            ProfileBtn.setOnAction(new EventHandler<ActionEvent>() {

                public void handle(ActionEvent e) {
                    // object to aid us in the image view
                    ImageView imageView = new ImageView();
                    Image profileImage;
                    BorderPane layout = new BorderPane();

                    FileChooser fileChooser = new FileChooser();
                    fileChooser.getExtensionFilters()
                            .addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg"));
                    // get the file form the desktop
                    // set the font of the file path

                    // single file selection of images
                    file = fileChooser.showOpenDialog(primaryStage);
                    TextArea textArea = new TextArea();
                    if (file != null) {
                        // desktop.open(file);
                        textArea.setPromptText("Path of Selected File or Files");
                        textArea.setFont(Font.font("SanSerif", 12));
                        textArea.setPrefSize(300, 50);
                        textArea.setEditable(false);

                        textArea.setText(file.getAbsolutePath());
                        // path, prefWidth, prefHeight, preserveRatio, smooth
                        profileImage = new Image(file.toURI().toString(), 100, 150, true, true);

                        imageView = new ImageView(profileImage);
                        imageView.setFitWidth(100);
                        imageView.setFitHeight(150);
                        imageView.setPreserveRatio(true);
                        layout.setCenter(imageView);
                        BorderPane.setAlignment(imageView, Pos.TOP_LEFT);
                    }
                    grid.add(imageView, 1, 5);
                    grid.add(textArea, 1, 4);

                    final Text actiontarget1 = new Text();
                }
            });

            // Create Account Button Handle
            createAccountbtn.setOnAction(e -> {

                String userName1 = userTextField.getText();
                String userPwd = pwBox.getText();
                String description1 = userTextField2.getText();
                String hashedPassword = null;

                if (StringUtils.isNullOrEmpty(userName1)) {
                    showAlert(Alert.AlertType.ERROR, "Form Error!", "Username is required to update");
                    return;

                }
                if (StringUtils.isNullOrEmpty(userPwd)) {
                    showAlert(Alert.AlertType.ERROR, "Form Error!", "Password is required to update");
                    return;
                }

                if (file == null) {
                    showAlert(Alert.AlertType.ERROR, "Form Error!",
                            "Picture Identifier is required");
                    return;
                }
                else {
                    try {
                        hashedPassword = Main.hashString(userPwd);
                    } catch (NoSuchAlgorithmException e1) {
                        e1.printStackTrace();
                    }

                    UserProfile userProfile = new UserProfile(userName1, hashedPassword, description1, file);
                    final Text blankActiontarget = new Text();
                    final Text actiontarget1 = new Text();

                    grid.add(blankActiontarget, 1, 7);
                    try {                      

                        if (AddAccountView.this.accountController.doesAccountExist(userProfile)) {
                        	showAlert(Alert.AlertType.ERROR, "Form Error!",
                        			"Username not Available, Already in use");
                            return;
                            
                        } else {
                            AddAccountView.this.accountController.addAccount(userProfile);
                            showAlert(Alert.AlertType.INFORMATION, "Form Successful!", "New Account " + userName1 + " Created");
                            return;
                            
                         
                        }
                    } catch (NullPointerException| IllegalArgumentException|
                            FileNotFoundException| SQLException e1) {
                        e1.printStackTrace();
                        throwError(grid, actiontarget, "Something went wrong in Account Creation.");
                    }
                }
            });

            // Create Account Button Handle
            cancelBtn.setOnAction(e -> AddAccountView.this.loginController.openLoginView(primaryStage));


        } catch (Exception e) {
            System.out.println("Create Account view crashed " + e.getMessage());
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
