/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - {AccountController.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */

package controllers;


import javafx.stage.Stage;
import models.NewAccountModel;
import models.UserProfile;
import views.AddAccountView;
import views.UpdateAccountView;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class AccountController {

    private LoginController loginController;
    private AddAccountView addAccountView;
    private NewAccountModel newAccountModel;

    public AccountController(NewAccountModel newAccountModel, AddAccountView addAccountView, LoginController loginController) {
        this.newAccountModel = newAccountModel;
        this.addAccountView = addAccountView;
        this.loginController = loginController;
    }

    public static void updateAccountView(Stage primaryStage) {
        new UpdateAccountView().start(primaryStage);
    }

    public  void showLoginScreen(Stage primaryStage) {
        this.loginController.openLoginView(primaryStage);
    }

    public void createAccountView(final Stage stage) {
        System.out.println("Here to the Add Account View");
        AccountController.this.addAccountView.start(stage);
    }

    public boolean doesAccountExist(UserProfile userProfile) {
        try {
            boolean userExists = this.newAccountModel.doesAccountExist(userProfile.getName());
            return userExists;
        } catch (SQLException e) {
            return false;
        }

    }


    public void addAccount(UserProfile userProfile) throws FileNotFoundException, SQLException {
        this.newAccountModel.insertRecords(userProfile);
    }
}
