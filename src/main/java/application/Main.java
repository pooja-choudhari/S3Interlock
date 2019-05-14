package application;

import controllers.AccountController;
import controllers.LoginController;
import controllers.MainAccessKeyController;
import controllers.S3TransferController;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import models.AccessKeyDaoImpl;
import models.LoginModel;
import models.NewAccountModel;
import views.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { main.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */

public class Main {

    public static final String RESOURCES_PATH = "/resources/";

    public static void main(String[] args) {
        final AddAccountView accountviewCreation = new AddAccountView();
        final NewAccountModel model = new NewAccountModel();
        final LoginModel loginModel = new LoginModel();

        final LoginController loginController = new LoginController(loginModel);
        final AccountController accountcontroller = new AccountController(model, accountviewCreation,loginController);
        MainAccessKeyController mainAccessKeyController = new MainAccessKeyController();

        loginController.addAccountController(accountcontroller);
        loginController.addMainAccessKeyController(mainAccessKeyController);

        LoginView loginView = new LoginView();
        loginController.addLoginView(loginView);
        MainAccessKeyView mainAccessKeyView = new MainAccessKeyView();
        mainAccessKeyController.addMainAccessView(mainAccessKeyView);


        AccessKeyDaoImpl accessKeyDao = new AccessKeyDaoImpl();

        accessKeyDao.setMainAccessKeyController(mainAccessKeyController);

        mainAccessKeyView.addController(mainAccessKeyController, accountcontroller);
        loginView.addLoginController(loginController);
        accountviewCreation.addController(accountcontroller, loginController);

        AddAccessKeyView addAccessKeyView = new AddAccessKeyView();
        addAccessKeyView.addMainAccessKeyController(mainAccessKeyController, accessKeyDao);

        DeleteAccessKeyView deleteAccessKeyView = new DeleteAccessKeyView();
        ListAccessKeyView listAccessKeyView = new ListAccessKeyView();
        S3OperationCountChartView s3OperationCountChartView = new S3OperationCountChartView();
        S3AllUserOperationView s3AllUserOperationView = new S3AllUserOperationView();
        S3TransferView s3TransferView = new S3TransferView();

        UpdateAccessKeyView updateAccessKeyView = new UpdateAccessKeyView();

        UpdateAccountView updateAccountView = new UpdateAccountView();
        DeleteAccountView deleteAccountView = new DeleteAccountView();

        S3TransferController s3TransferController = new S3TransferController();

        deleteAccessKeyView.addMainController(mainAccessKeyController, accessKeyDao);
        listAccessKeyView.addMainController(mainAccessKeyController, accessKeyDao);
        s3OperationCountChartView.addMainController(mainAccessKeyController, accessKeyDao, s3TransferController);
        s3AllUserOperationView.addMainController(mainAccessKeyController, accessKeyDao, s3TransferController);

        updateAccessKeyView.addMainController(mainAccessKeyController, accessKeyDao);

        updateAccountView.addMainController(mainAccessKeyController, model);
        deleteAccountView.addMainController(mainAccessKeyController, accessKeyDao);

        mainAccessKeyController.addViews(
                addAccessKeyView,
                deleteAccessKeyView,
                listAccessKeyView,
                updateAccessKeyView,

                s3OperationCountChartView,
                s3TransferView,
                s3AllUserOperationView,

                updateAccountView,
                deleteAccountView);

        s3TransferController.addAccessKeyDao(accessKeyDao);
        s3TransferView.addMainController(mainAccessKeyController, accessKeyDao, s3TransferController);

        // Error - This operation is permitted on the event thread only; currentThread =
        // main
        // create JavaFX runnable thread - to show view.

        new JFXPanel();
        Platform.setImplicitExit(false);

        Platform.runLater(() -> {
            try {
                // Application starts here with Login View
                loginView.start(new Stage());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });

    }


    public static String hashString(String stringToHash) throws NoSuchAlgorithmException
    {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(stringToHash.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 2
        StringBuilder hexString = new StringBuilder();
        for (int i=0;i<byteData.length;i++) {
            String hex=Integer.toHexString(0xff & byteData[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        System.out.println("Hex format : " + hexString.toString());

        return hexString.toString();
    }

}


