package comp5216.sydney.edu.au.neverlate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import android.widget.Button;
import android.widget.EditText;

public class ChangePassWordActivity extends AppCompatActivity {

    private EditText userNameInput;
    private EditText answer1Input;
    private EditText answer2Input;
    private EditText answer3Input;

    private Button validate;
    private Button cancel;

    private String inputUserName;
    private boolean userNameAligned = false;
    private String userQuestion1;
    private String userQuestion2;
    private String userQuestion3;

    UserInformationDB userDB;
    UserInformationDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_word);
        setViews();
        userDB = UserInformationDB.getDatabase(this.getApplication().getApplicationContext());
        userDao = userDB.userInfoDao();
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputUserName = userNameInput.getText().toString();
                readUserInformationBasedOnDatabase();
                if (userNameAligned == false){
                    Toast.makeText(ChangePassWordActivity.this,"Username doesn't exist," +
                            "please re-enter your username.",Toast.LENGTH_LONG).show();
                }else{
                    String userAnswer1 = answer1Input.getText().toString();
                    String userAnswer2 = answer2Input.getText().toString();
                    String userAnswer3 = answer3Input.getText().toString();
                    if (userAnswer1.equals(userQuestion1) && userAnswer2.equals(userQuestion2)
                            && userAnswer3.equals(userQuestion3)){
                        Intent resetPassAccess = new Intent(ChangePassWordActivity.this,ResetPasswordActivity.class);
                        resetPassAccess.putExtra("USER_NAME",inputUserName);
                        startActivity(resetPassAccess);
                    }else{
                        Toast.makeText(ChangePassWordActivity.this,"Your answer for security questions" +
                                " is not correct, " + "please re-enter your answers.",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassWordActivity.this);
                builder.setTitle("Cancel Password Change")
                        .setMessage("Are you sure to give up changing your password?")
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

    private void setViews(){
        userNameInput = (EditText)findViewById(R.id.inputUserName);
        answer1Input = (EditText)findViewById(R.id.inputAnswer_1);
        answer2Input = (EditText)findViewById(R.id.inputAnswer_2);
        answer3Input = (EditText)findViewById(R.id.inputAnswer_3);
        validate = (Button)findViewById(R.id.btn_validate);
        cancel = (Button)findViewById(R.id.btn_cancel2);
    }

    private void readUserInformationBasedOnDatabase() {
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
                            if (searchedUserName.equals(inputUserName)) {
                                userQuestion1 = item.getUserAnswer1();
                                userQuestion2 = item.getUserAnswer2();
                                userQuestion3 = item.getUserAnswer3();
                                userNameAligned = true;
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
}

