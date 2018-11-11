package comp5216.sydney.edu.au.neverlate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText userName;
    private EditText passWord;
    private Button login;
    private TextView register;
    private TextView findPassword;
    private String name;
    private String pass;
    private boolean userCorrect = false;
    private boolean userPasswordCorrect = false;


    //database items
    private UserInformationDB userDB;
    private UserInformationDao userDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setDatabase();
        EditText et_name = (EditText) findViewById(R.id.edit_name);
        EditText et_password = (EditText) findViewById(R.id.edit_password);

        et_name.setHintTextColor(Color.parseColor("#ef971f"));
        et_password.setHintTextColor(Color.parseColor("#ef971f"));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userName.getText().toString();
                pass = passWord.getText().toString();
                searchUserExistsFromDatabase();
                valid(userCorrect,userPasswordCorrect);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerAccess = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(registerAccess);
            }
        });

        findPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changePasswordAccess = new Intent(MainActivity.this,ChangePassWordActivity.class);
                startActivity(changePasswordAccess);
            }
        });
    }

    private void setViews(){
        userName = (EditText)findViewById(R.id.edit_name);
        passWord = (EditText)findViewById(R.id.edit_password);
        login = (Button) findViewById(R.id.login);
        register = (TextView)findViewById(R.id.registerView);
        findPassword = (TextView)findViewById(R.id.reset_password);
    }

    private void setDatabase(){
        userDB = UserInformationDB.getDatabase(this.getApplication().getApplicationContext());
        userDao = userDB.userInfoDao();
    }

    private void searchUserExistsFromDatabase() {
        //Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    //read items from database
                    List<UserInformationItem> usersFromDB = userDao.listAll();
                    if (usersFromDB != null & usersFromDB.size() > 0) {
                        for (UserInformationItem item : usersFromDB) {
                            String searchedUserName = item.getUserName();
                            System.out.println(item.getUserName() + "" + item.getUserPassword());
                            String searchedUserPassword = item.getUserPassword();
                            if (searchedUserName.equals(name)) {
                                userCorrect = true;
                            }
                            if (searchedUserPassword.equals(pass)){
                                userPasswordCorrect = true;
                            }
                            Log.i("SQLite read item", "Scanned ID: " + item.getUserID());
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

    private void valid(boolean correctName, boolean correctPassword){
        if (correctName == true && correctPassword == true){
            Intent functionAccess = new Intent(MainActivity.this,MapsActivity.class);
            startActivity(functionAccess);
        }else{
            //Set an warning text to remind users
            String errorMessage = "Your username or password may be not correct,please enter" +
                    " your username and password again.";
            Toast.makeText(this,errorMessage,Toast.LENGTH_SHORT).show();
        }

    }

    public void jump(View view){
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }
}

