package comp5216.sydney.edu.au.neverlate;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "userInformationRecord")
public class UserInformationItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "userID")
    private int userID;

    @ColumnInfo(name = "userName")
    private String userName;

    @ColumnInfo(name = "userPassword")
    private String userPassword;

    @ColumnInfo(name = "userEmail")
    private String userEmail;

    @ColumnInfo(name = "userAnswer1")
    private String userAnswer1;

    @ColumnInfo(name = "userAnswer2")
    private String userAnswer2;

    @ColumnInfo(name = "userAnswer3")
    private String userAnswer3;

    public UserInformationItem(String userName, String userPassword, String userEmail,
                               String userAnswer1,String userAnswer2, String userAnswer3){
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userAnswer1 = userAnswer1;
        this.userAnswer2 = userAnswer2;
        this.userAnswer3 = userAnswer3;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserAnswer1() {
        return userAnswer1;
    }

    public void setUserAnswer1(String userAnswer1) { this.userAnswer1 = userAnswer1; }

    public String getUserAnswer2() {
        return userAnswer2;
    }

    public void setUserAnswer2(String userAnswer2) { this.userAnswer2 = userAnswer2; }

    public String getUserAnswer3() {
        return userAnswer3;
    }

    public void setUserAnswer3(String userAnswer3) { this.userAnswer3 = userAnswer3; }
}
