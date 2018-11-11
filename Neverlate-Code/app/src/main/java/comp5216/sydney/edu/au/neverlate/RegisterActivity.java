package comp5216.sydney.edu.au.neverlate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText userNameInput;
    private EditText passwordInput;
    private EditText emailInput;
    private EditText SecurityQuestion1_input;
    private EditText SecurityQuestion2_input;
    private EditText SecurityQuestion3_input;
    private TextView userNameTitle;
    private TextView passWordTitle;
    private TextView emailTitle;
    private TextView securityQuestion1_Title;
    private TextView securityQuestion2_Title;
    private TextView securityQuestion3_Title;
    private Button submit;
    private Button cancel;

    private boolean valid = true;
    private boolean userExistence = false;
    private String invalidInfo = "Your register has failed due to providing invalid data, " +
            "please try again!";

    ArrayList<UserInformationFormat> items;
    UserInformationDB userDB;
    UserInformationDao userDao;
    public int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setRegisterFormViews();
        userDB = UserInformationDB.getDatabase(this.getApplication().getApplicationContext());
        userDao = userDB.userInfoDao();
        items = new ArrayList<>();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameTitle.setTextColor(Color.BLACK);
                passWordTitle.setTextColor(Color.BLACK);
                emailTitle.setTextColor(Color.BLACK);
                securityQuestion1_Title.setTextColor(Color.BLACK);
                securityQuestion2_Title.setTextColor(Color.BLACK);
                securityQuestion3_Title.setTextColor(Color.BLACK);

                String userName = userNameInput.getText().toString();
                String passWord = passwordInput.getText().toString();
                String email = emailInput.getText().toString();
                String securityAnswer1 = SecurityQuestion1_input.getText().toString();
                String securityAnswer2 = SecurityQuestion2_input.getText().toString();
                String securityAnswer3 = SecurityQuestion3_input.getText().toString();
                boolean emailValid = EmailValidator.getInstance().isValid(email);

                if (userName.length() == 0){
                    valid = false;
                    userNameTitle.setTextColor(Color.RED);
                    userNameInput.setText("");
                }
                if (passWord.length() < 8){
                    valid = false;
                    passWordTitle.setTextColor(Color.RED);
                    passwordInput.setText("");
                }
                if (emailValid == false || email.length() == 0){
                    valid = false;
                    emailTitle.setTextColor(Color.RED);
                    emailInput.setText("");
                }
                if (securityAnswer1.length() == 0){
                    valid = false;
                    securityQuestion1_Title.setTextColor(Color.RED);
                    SecurityQuestion1_input.setText("");
                }
                if (securityAnswer2.length() == 0){
                    valid = false;
                    securityQuestion2_Title.setTextColor(Color.RED);
                    SecurityQuestion2_input.setText("");
                }
                if (securityAnswer3.length() == 0){
                    valid = false;
                    securityQuestion3_Title.setTextColor(Color.RED);
                    SecurityQuestion3_input.setText("");
                }
                if (valid == true){
                    checkUsernameExistenceFromDatabase();
                    if (userExistence == false){
                        UserInformationFormat newUser = new UserInformationFormat(userName, passWord, email,securityAnswer1,securityAnswer2,securityAnswer3);
                        items.add(position,newUser);
                        saveNewUserToDatabase();
                        Toast.makeText(RegisterActivity.this,"You have successfully registered!",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else{
                        userNameInput.setText("");
                        passwordInput.setText("");
                        emailInput.setText("");
                        SecurityQuestion1_input.setText("");
                        SecurityQuestion2_input.setText("");
                        SecurityQuestion3_input.setText("");
                        Toast.makeText(RegisterActivity.this,"Username exists, Please enter a new value name",
                                Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(RegisterActivity.this,invalidInfo,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Cancel Register")
                        .setMessage("Are you sure to give up this registration?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setResult(RESULT_CANCELED);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User cancelled the dialog
                                // Nothing happens
                            }
                        });
                builder.create().show();
            }
        });
    }

    private void setRegisterFormViews(){
        userNameInput = (EditText)findViewById(R.id.edit_userName);
        passwordInput = (EditText)findViewById(R.id.edit_passWord);
        emailInput = (EditText)findViewById(R.id.edit_email);
        SecurityQuestion1_input = (EditText)findViewById(R.id.edit_security_question_1);
        SecurityQuestion2_input = (EditText)findViewById(R.id.edit_security_question_2);
        SecurityQuestion3_input = (EditText)findViewById(R.id.edit_security_question_3);
        userNameTitle = (TextView) findViewById(R.id.userName_title);
        passWordTitle = (TextView) findViewById(R.id.passWord_title);
        emailTitle = (TextView) findViewById(R.id.email_title);
        securityQuestion1_Title = (TextView) findViewById(R.id.securityQuestion_title1);
        securityQuestion2_Title = (TextView) findViewById(R.id.securityQuestion_title2);
        securityQuestion3_Title = (TextView) findViewById(R.id.securityQuestion_title3);
        submit = (Button)findViewById(R.id.btn_submit);
        cancel = (Button)findViewById(R.id.btn_cancel);
    }

    private void checkUsernameExistenceFromDatabase() {
        //Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    //read items from database
                    List<UserInformationItem> usersFromDB = userDao.listAll();
                    if (usersFromDB != null & usersFromDB.size() > 0) {
                        for (UserInformationItem item : usersFromDB) {
                            System.out.println(item.getUserName() + " " + item.getUserPassword());
                            if (userNameInput.getText().toString().equals(item.getUserName())){
                                userExistence = true;
                            }
                        }
                    }
                    return null;
                }
            }.execute().get();
        }
        catch(Exception ex) {
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }
    }

    private void saveNewUserToDatabase(){
        //Use asynchronous task to run query on the background to avoid locking UI
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<UserInformationItem> usersFromDB = userDao.listAll();
                outerLoop:
                for (UserInformationFormat todo : items) {
                    String editedUserName = todo.getUserName();
                    String editedPassWord = todo.getPassWord();
                    String editedEmail = todo.geteMail();
                    String editedSecurityAnswer1 = todo.getSecurityAnswer1();
                    String editedSecurityAnswer2 = todo.getSecurityAnswer2();
                    String editedSecurityAnswer3 = todo.getSecurityAnswer3();
                    UserInformationItem item = new UserInformationItem(editedUserName,editedPassWord, editedEmail,editedSecurityAnswer1,editedSecurityAnswer2,editedSecurityAnswer3);
                    userDao.insert(item);
                    Log.i("SQLite saved item", "ID: " + item.getUserID() + " Username: " + item.getUserName());
                }
                return null;
            }
        }.execute();
    }
}


