package comp5216.sydney.edu.au.neverlate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PrepareActivity extends AppCompatActivity {

    EditText prepareTime;
    Button toClock;
    int wayMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare);

        prepareTime = (EditText)findViewById(R.id.prepareTime);
        toClock = (Button)findViewById(R.id.toClock);
    }

    public void toClock(View view){
        String wayTime = MapsActivity.durationResult;
        wayTime.trim();

        if(wayTime.length()<=7){
            int endNum = wayTime.indexOf("min");
            String toMin = wayTime.substring(0,endNum-1);
            toMin.trim();
            toMin.trim();
            wayMinutes = Integer.parseInt(toMin);
        }else if(wayTime.length()>7){
            int h = 0;
            int min = 0;
            int startNum = 0;
            int endNum = wayTime.indexOf("hour");
            String toH = wayTime.substring(0,endNum-1);
            toH.trim();
            startNum = endNum + 5;
            endNum = wayTime.indexOf("min");
            String toMin = null;
            if(wayTime.contains("hours")){
                toMin = wayTime.substring(startNum+1,endNum-1);
            }else {
                toMin = wayTime.substring(startNum, endNum - 1);
            }

            h = Integer.parseInt(toH);
            min = Integer.parseInt(toMin);
            wayMinutes = h*60 + min;
        }

        String preTime = prepareTime.getText().toString();
        if(preTime.isEmpty()){
            Toast.makeText(this,"Please input your estimated prepare time !",Toast.LENGTH_SHORT).show();
        }else {
            preTime.trim();
            Intent intent = new Intent(PrepareActivity.this, ClockActivity.class);
            intent.putExtra("PrepareTime", preTime);
            intent.putExtra("WayTime", wayMinutes + "");
            startActivity(intent);
        }
    }
}
