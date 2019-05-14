/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - {LoginController.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */
package controllers;

import javafx.stage.Stage;
import models.FetchRecordFromDb;
import models.LoginModel;
import views.LoginView;

import java.sql.SQLException;

public class LoginController {

    private final LoginModel loginModel;
    private AccountController accountController;
    private MainAccessKeyController mainAccessKeyController;
    private LoginView loginView;



    public LoginController(LoginModel loginModel) {
        this.loginModel = loginModel;
    }

    public void openCreateAccountView(final Stage stage) {
        this.accountController.createAccountView(stage);
    }

    public void openLoginView(Stage primaryStage) {
        loginView.start(primaryStage);
    }

    public FetchRecordFromDb login(String userName, String password) throws SQLException {
        return this.loginModel.login(userName, password);
    }

    public void openMainAccessKeyView(Stage primaryStage, FetchRecordFromDb fetchRecordFromDb) {
        this.mainAccessKeyController.setFetchRecordFromDb(fetchRecordFromDb);
        this.mainAccessKeyController.mainView(primaryStage);
    }

    public void addAccountController(AccountController accountcontroller) {
        this.accountController = accountcontroller;
    }

    public void addMainAccessKeyController(MainAccessKeyController mainAccessKeyController) {
        this.mainAccessKeyController = mainAccessKeyController;
    }

    public void addLoginView(LoginView loginView) {
        this.loginView = loginView;
    }
}
