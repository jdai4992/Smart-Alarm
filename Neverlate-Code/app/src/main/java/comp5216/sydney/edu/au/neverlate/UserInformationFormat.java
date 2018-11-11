package comp5216.sydney.edu.au.neverlate;

public class UserInformationFormat {
    public String userName;
    public String passWord;
    public String eMail;
    public String securityAnswer1;
    public String securityAnswer2;
    public String securityAnswer3;

    public UserInformationFormat(String username, String password, String email, String answer1,
                                 String answer2, String answer3){
        this.userName = username;
        this.passWord = password;
        this.eMail = email;
        this.securityAnswer1 = answer1;
        this.securityAnswer2 = answer2;
        this.securityAnswer3 = answer3;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String newName) {
        this.userName = newName;
    }

    //retrieve users' create time
    public String getPassWord(){
        return passWord;
    }

    public void setPassWord(String newPassWord){
        this.passWord = newPassWord;
    }

    public String geteMail(){
        return eMail;
    }

    public void seteMail(String newEmail){
        this.eMail = newEmail;
    }

    public String getSecurityAnswer1(){
        return securityAnswer1;
    }

    public void setSecurityAnswer1(String newAnswer1){
        this.securityAnswer1 = newAnswer1;
    }

    public String getSecurityAnswer2(){
        return securityAnswer2;
    }

    public void setSecurityAnswer2(String newAnswer2){
        this.securityAnswer2 = newAnswer2;
    }

    public String getSecurityAnswer3(){
        return securityAnswer3;
    }

    public void setSecurityAnswer3(String newAnswer3){
        this.securityAnswer3 = newAnswer3;
    }
}

