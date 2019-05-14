/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - {CreateTblController.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */
package controllers;

import models.DaoModel;

import java.sql.SQLException;

/**
 * This class is responsible to Create the Table if it does not exist.
 */
public class CreateTblController {

    public static void main(String[] args) throws SQLException {

        DaoModel dao = new DaoModel();
        dao.createInitialTable();

        dao.setUpCspTable();



    }

}
