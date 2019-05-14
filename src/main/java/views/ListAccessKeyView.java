/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { ListAccessKeyView.java}
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.AccessKeyDaoImpl;

import java.sql.ResultSet;

public class ListAccessKeyView extends Application {

    //TABLE VIEW AND DATA
    private ObservableList<ObservableList> data;

    private TableView tableview;
    AccessKeyDaoImpl accessKeyDaoImpl;
    MainAccessKeyController mainController;

    public void addMainController(final MainAccessKeyController mainAccessKeyController, AccessKeyDaoImpl accessKeyDao){
        this.mainController = mainAccessKeyController;
        this.accessKeyDaoImpl = accessKeyDao;
    }

    //CONNECTION DATABASE
    public void buildData() {

        data = FXCollections.observableArrayList();
        try {
            ResultSet rs = this.accessKeyDaoImpl.displayAccessKeysInfo();

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableview.getColumns().addAll(col);
                System.out.println("ListAccessKey View: Column [" + i + "] >>" + col);
            }


            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("ListAccessKeyView Row: " + row);
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            tableview.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }

    @Override
    public void start(Stage primaryStage) {

            //TableView
            tableview = new TableView();
            tableview.setPlaceholder(new Label("No Entries found. . . Please add Friendly Key Id"));

            buildData();

            //Main Scene
            BorderPane root = new BorderPane();
            final Scene scene = new Scene(root, 700, 700, Color.GRAY);

//            final TextField userTextField = new TextField();
//            userTextField.setPrefWidth(10);
//            root.setTop(userTextField);

            root.setCenter(tableview);

            Image imgHome = new Image(Main.RESOURCES_PATH + "home.png");
            ImageView imgView = new ImageView(imgHome);
            imgView.setFitWidth(20);
            imgView.setFitHeight(20);
            imgView.setPreserveRatio(true);

            Button cancelBtn = new Button("Back");
            cancelBtn.setGraphic(imgView);

            HBox hbcancelBtn = new HBox(10);
            hbcancelBtn.setPadding(new Insets(20, 12, 20, 12));
            hbcancelBtn.setSpacing(10);
            hbcancelBtn.getChildren().add(cancelBtn);
            root.setBottom(hbcancelBtn);
            hbcancelBtn.setAlignment(Pos.CENTER);

            /* * * * * * * * * * * * * * * * * *
             Custom Stylesheet
             * * * * * * * * * * * * * * * * * */
            root.getStylesheets().add(Main.RESOURCES_PATH + "main.css");
            root.getStyleClass().add("grid");

            tableview.getStylesheets().add(Main.RESOURCES_PATH + "main.css");
            tableview.getStyleClass().add("grid");

            primaryStage.setScene(scene);

            // Create Account Button Handle
            cancelBtn.setOnAction(e -> ListAccessKeyView.this.mainController.mainView(primaryStage));

            primaryStage.show();


    }
}
