package comp5216.sydney.edu.au.neverlate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {
    private Button logout;
    private Button gotoMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logoutActivity = new Intent(WelcomeActivity.this,MainActivity.class);
                Toast.makeText(WelcomeActivity.this,"You have successfully logout!",Toast.LENGTH_LONG).show();
                startActivity(logoutActivity);
            }
        });

        gotoMap = (Button)findViewById(R.id.toMap);
        gotoMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(WelcomeActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
}
