
/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { FetchRecordFromDb.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */

package models;

import javafx.scene.image.Image;

public class FetchRecordFromDb {

    public final String dbUserName;
    public final String dbDescription;
    public final Image image;

    /*
     * Constructor
     */
    public FetchRecordFromDb(String dbUserName, String dbDescription, Image image) {
        this.dbUserName = dbUserName;
        this.dbDescription = dbDescription;
        this.image = image;

    }

}
