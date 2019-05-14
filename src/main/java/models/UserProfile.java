/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { UserProfile.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */


package models;

import java.io.File;

/**
 * This class has the responsibility to take information from user in order to
 * create an account.
 */
public class UserProfile {

    final String username;
    final String password;
    final String description;
    final File file;


    public UserProfile(String name, String pwd, String desc, File file) {
        this.username = name;
        this.password = pwd;
        this.description = desc;
        this.file = file;
    }

    public String getName() {
        return username;
    }

    public String getPwd() {
        return password;
    }

    public String getDesc() {
        return description;
    }

    public File getFile() {
        return file;
    }


}
