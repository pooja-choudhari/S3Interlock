/**
 * Pooja Choudhari Date - 05/08/2019 
 * Source FileName - { AccessKey.java}
 * S3Interlock
 * 
 * Description - S3 interlock facilitates cloud users to register and provides 
 * 				 CRUD operations for access key management within the application
 * 
 */

package models;

public class AccessKey {

    String cspName;
    String friendlyName;
    String accessKey;
    String secretKey;

    public AccessKey(String friendlyKeyId, String accessKey, String secretKey, String cspName) {
        this.friendlyName = friendlyKeyId;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.cspName = cspName;

    }

    public String getFriendlyKeyId() {
        return friendlyName;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getCspName() {
        return cspName;
    }


    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public void setAccessKey(String AccessKey) {
        this.accessKey = AccessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setCspName(String cspName) {
        this.cspName = cspName;
    }

}
