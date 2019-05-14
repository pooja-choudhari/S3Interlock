# S3Interlock
 -	Cloud Storage simplified
     
### Developed - Pooja Choudhari (poojachoudhari0792@gmail.com)

### Abstract
S3 interlock facilitates cloud users to register and provides CRUD operations for access key management within the application. Users can further use the access keys to upload, download and delete files to Cloud Object Storage. Currently the application supports files transfer to Amazon Web Services and IBM Cloud. 

Users can register to the application with a unique username, password and a profile image. The password is then hashed and stored in the database and the user on successful login is able to see his profile picture. From the Access Key View the user can add, list, update and delete the access keys with a unique key Identifier to perform file transfer operation. The users can create multiple access keys for a given cloud provider. The users can select the key Identifier from a drop down and select an operation such as upload/download/delete to perform with that key. The application also provides an option to view the statistics of the upload/download/delete operations in the form of a bar chart displayed for different access key to operations performed with an added ability to export the stats to csv format for further analysis.

Admin has different view than the user and is responsible to delete an account for the user. He can see the progress of application by viewing overall statistics of the users combined per user per access key vs upload/download/delete operations.


![Image of ERD](https://github.com/poojachoudhari/S3Interlock/blob/master/loginView.png)


### Run the application
1.	Run Main.java to start with the application
2.	Path > java -jar p_chou_finalProjFX.jar

### DB Table
- cpooja_account
- cpooja_access_key_manager
- cpooja_cloud_service_provider
- cpooja_cloud_op_stats


![Image of ERD](https://github.com/poojachoudhari/S3Interlock/blob/master/ERD.png)

![Image of Stats View](https://github.com/poojachoudhari/S3Interlock/blob/master/statsView.png)
