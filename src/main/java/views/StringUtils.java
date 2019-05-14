/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { StringUtils.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */

package views;

public class StringUtils {
    public static boolean isNullOrEmpty(String stringToMatch){

        return stringToMatch==null || stringToMatch.equals("");
    }

}
