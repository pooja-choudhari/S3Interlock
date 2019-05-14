/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { S3AllUserOperationView.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */

package views;

import application.Main;
import controllers.MainAccessKeyController;
import controllers.S3TransferController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.AccessKeyDaoImpl;

import java.sql.SQLException;


public class S3AllUserOperationView extends Application {

    MainAccessKeyController mainController;
    AccessKeyDaoImpl accessKeyDao;
    S3TransferController s3TransferController;

    public void addMainController(final MainAccessKeyController mainController, AccessKeyDaoImpl accessKeyDao, S3TransferController s3TransferController){
        this.mainController = mainController;
        this.accessKeyDao = accessKeyDao;
        this.s3TransferController = s3TransferController;
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 700, 700);

        BarChart<String, Number> barChart = this.s3TransferController.displayCloudTransactionStats(this.mainController, true);
        root.setTop(barChart);

        Image imgHome = new Image(Main.RESOURCES_PATH + "home.png");
        ImageView imgView = new ImageView(imgHome);
        imgView.setFitWidth(20);
        imgView.setFitHeight(20);
        imgView.setPreserveRatio(true);

        GridPane gridLower = new GridPane();
        gridLower.setAlignment(Pos.CENTER);
        gridLower.setHgap(30);
        gridLower.setVgap(30);


        Button cancelBtn = new Button("Back");
        cancelBtn.setGraphic(imgView);

        HBox hbcancelBtn = new HBox(10);
        hbcancelBtn.setPadding(new Insets(20, 12, 40, 12));
        hbcancelBtn.setSpacing(10);
        hbcancelBtn.getChildren().add(cancelBtn);
        hbcancelBtn.setAlignment(Pos.CENTER);
        gridLower.add(hbcancelBtn, 0, 1);


        Image csvLogo = new Image(Main.RESOURCES_PATH + "csvlogo.jpeg");
        ImageView csvLogoView = new ImageView(csvLogo);
        csvLogoView.setFitWidth(20);
        csvLogoView.setFitHeight(20);
        csvLogoView.setPreserveRatio(true);

        Button csvBtn = new Button("Export To CSV");
        csvBtn.setGraphic(csvLogoView);
        HBox hbcsvBtn = new HBox(10);
        hbcsvBtn.setPadding(new Insets(20, 12, 40, 12));
        hbcsvBtn.setSpacing(10);
        hbcsvBtn.getChildren().add(csvBtn);
        hbcsvBtn.setAlignment(Pos.CENTER);
        gridLower.add(hbcsvBtn, 1, 1);

        root.setBottom(gridLower);

        /* * * * * * * * * * * * * * * * * *
             Custom Stylesheet
             * * * * * * * * * * * * * * * * * */
        root.getStylesheets().add(Main.RESOURCES_PATH + "main.css");
        root.getStyleClass().add("root");

        primaryStage.setScene(scene);
        primaryStage.show();

        cancelBtn.setOnAction(e -> S3AllUserOperationView.this.mainController.mainView(primaryStage));

        csvBtn.setOnAction(e -> {
            try {
                S3AllUserOperationView.this.accessKeyDao.exportStatsToCsv(
                        false,
                        this.mainController.getFetchRecordFromDb().dbUserName);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}