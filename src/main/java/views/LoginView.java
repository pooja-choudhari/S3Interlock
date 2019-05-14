/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { LoginView.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */

package views;

import application.Main;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import models.FetchRecordFromDb;

import java.sql.SQLException;
import java.util.Random;


public class LoginView extends Application {

    private LoginController loginController;

    public void addLoginController(final LoginController loginController) {
        this.loginController = loginController;
    }

    @Override
    public void start(Stage primaryStage) {
        try {

            final GridPane grid = new GridPane();
            Scene scene = new Scene(grid, 700, 700, Color.DARKGRAY);
            scene.setFill(null);

            primaryStage.setScene(scene);
            primaryStage.setTitle("S3Interlock - Cloud Data Management");
            primaryStage.show();
            Text scenetitle = new Text("Login");
            scenetitle.setTextAlignment(TextAlignment.JUSTIFY);
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
            Label userName = new Label("User Name:");
            final TextField userTextField = new TextField();
            grid.add(userName, 0, 1);
            grid.add(userTextField, 1, 1);

            // Label and Password Field for Password
            Label pw = new Label("Password:");
            final PasswordField pwBox = new PasswordField();
            grid.add(pw, 0, 2);
            grid.add(pwBox, 1, 2);

            /* * * * * * * * * * * * * * * * * *
             Simple Captcha Calculation
             * * * * * * * * * * * * * * * * * */

            int num1 = new Random().nextInt(10);
            int num2 = 0;

            while ((num1 > num2)) {
                num2 = new Random().nextInt(20);
            }

            Label captchalbl = new Label("Captcha Verification: (" + num2 + " + " + num1 + ")");
            final TextField captchaField = new TextField();
            grid.add(captchalbl, 0, 3);
            grid.add(captchaField, 1, 3);

            /* * * * * * * * * * * * * * * * * *
             Button Action Listeners
             * * * * * * * * * * * * * * * * * */

            // Sign In Button
            Button signInBtn = new Button("Sign in");
            HBox hbBtn = new HBox();
            hbBtn.getChildren().add(signInBtn);
            grid.add(hbBtn, 1, 8);

            // Create New Account Button
            Button createAccountbtn = new Button("Register New Account");
            HBox hbBtn1 = new HBox();
            hbBtn1.getChildren().add(createAccountbtn);
            grid.add(hbBtn1, 0, 8);

            Button resetBtn = new Button("Reset");
            HBox hbresetBtn = new HBox();
            hbresetBtn.getChildren().add(resetBtn);
            grid.add(resetBtn, 0, 9);

            Image ibmImg = new Image(Main.RESOURCES_PATH + "ibmlogo.png");
            ImageView imageIbm = new ImageView(ibmImg);
            imageIbm.setFitWidth(150);
            imageIbm.setFitHeight(150);
            imageIbm.setPreserveRatio(true);
            grid.add(imageIbm, 0, 12);

            Image image = new Image(Main.RESOURCES_PATH + "logo.jpg");
            ImageView imageView1 = new ImageView(image);
            imageView1.setFitWidth(150);
            imageView1.setFitHeight(150);
            imageView1.setPreserveRatio(true);
            grid.add(imageView1, 1, 12);

            Image awsImg = new Image(Main.RESOURCES_PATH + "awslogo.png");
            ImageView imageAws = new ImageView(awsImg);
            imageAws.setFitWidth(150);
            imageAws.setFitHeight(150);
            imageAws.setPreserveRatio(true);
            grid.add(imageAws, 2, 12);

            int finalNum = num2;
            signInBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    try {
                        String name = userTextField.getText();
                        String pwd = pwBox.getText();
                        String captchaAns = captchaField.getText();

                        if (StringUtils.isNullOrEmpty(name)) {
                            showAlert(Alert.AlertType.CONFIRMATION, "Form Error!", "Please enter your username");
                            return;
                        } else if (StringUtils.isNullOrEmpty(pwd)) {
                            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter your password");
                            return;

                        } else if (StringUtils.isNullOrEmpty(captchaAns)) {
                            showAlert(Alert.AlertType.ERROR, "Form Error!", "Captcha answer is required");
                            return;
                        } else if (!(captchaAns.matches("[0-9]*"))) {
                            showAlert(Alert.AlertType.ERROR, "Form Error!", "Captcha answer must be numeric");
                            return;

                        } else {
                            System.out.println("LoginView: Verifying user '" + name + "' exists in the database");
                            int captchaAnsInt = Integer.parseInt(captchaAns);
                            int calculatedAns = num1 + finalNum;
                            if (calculatedAns != captchaAnsInt) {
                                showAlert(Alert.AlertType.ERROR, "Form Error!", "Captch Answer did not match. . Please retry");

                                return;
                            }

                            FetchRecordFromDb fetchRecordFromDb = LoginView.this.loginController.login(name, pwd);
                            if (fetchRecordFromDb != null) {
                                LoginView.this.loginController.openMainAccessKeyView(primaryStage, fetchRecordFromDb);
                            } else {
                                showAlert(Alert.AlertType.ERROR, "Form Error!", userTextField.getText() + " does not Exists");
                                return;
                            }
                        }
                    } catch (NullPointerException | SQLException e1) {
                        e1.printStackTrace();

                    }
                }

            });

            // Handle for Create Account Button
            createAccountbtn.setOnAction(e -> {
                try {
                    LoginView.this.loginController.openCreateAccountView(primaryStage);
                } catch (Exception e1) {
                    System.out.println(e1);
                    showAlert(Alert.AlertType.ERROR, "Form Error!", "Please restart application");
                    return;
                }
            });


            // Handle for Create Account Button
            resetBtn.setOnAction(e -> {
                userTextField.clear();
                pwBox.clear();
                captchaField.clear();
            });
        } catch (Exception e) {
            System.out.println("Login view exited unexpectedly" + e.getMessage());
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