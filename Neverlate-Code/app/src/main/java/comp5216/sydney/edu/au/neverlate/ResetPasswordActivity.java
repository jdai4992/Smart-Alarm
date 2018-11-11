package comp5216.sydney.edu.au.neverlate;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText resetPassword;
    private EditText confirmPassword;
    private Button confirm;

    private String passwordReset;
    private String passwordConfirmed;
    private String userName;

    UserInformationDB userDB;
    UserInformationDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        //set views
        resetPassword = (EditText)findViewById(R.id.reset_password);
        confirmPassword = (EditText)findViewById(R.id.confirm_password);
        confirm = (Button)findViewById(R.id.confirm);
        //set Database
        userDB = UserInformationDB.getDatabase(this.getApplication().getApplicationContext());
        userDao = userDB.userInfoDao();
        //get User Name
        Intent resetUserNameAccess = getIntent();
        userName = resetUserNameAccess.getStringExtra("USER_NAME");

        //set confirm onClickListener
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordReset = resetPassword.getText().toString();
                passwordConfirmed = confirmPassword.getText().toString();
                if (!passwordReset.equals(passwordConfirmed) || passwordReset.length() < 8){
                    Toast.makeText(ResetPasswordActivity.this,"The password you reset and confirm" +
                            " doesn't match, or your password has less than 8 characters" +
                            "please reset your password again.",Toast.LENGTH_LONG).show();
                }
                else{
                    saveChangedPasswordInDatabase();
                    Toast.makeText(ResetPasswordActivity.this,"Your password has successfully changed!",
                            Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    Intent returnLogin = new Intent(ResetPasswordActivity.this,MainActivity.class);
                    startActivity(returnLogin);
                }
            }
        });
    }

    private void saveChangedPasswordInDatabase() {
        //Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    //read items from database
                    List<UserInformationItem> usersFromDB = userDao.listAll();
                    if (usersFromDB != null & usersFromDB.size() > 0) {
                        for (UserInformationItem item : usersFromDB) {
                            if (item.getUserName().equals(userName)){
                                item.setUserPassword(passwordConfirmed);
                            }
                            Log.i("SQLite change item", "Changed ID: " + item.getUserID());
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
}
