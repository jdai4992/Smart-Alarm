package comp5216.sydney.edu.au.neverlate;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class ClockActivity extends AppCompatActivity {

    //make alarm manager
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;
    Button settime, unset;
    String prepareTime, wayTime;
    int timeToDelay, wayToTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        this.context = this;
        prepareTime=wayTime="";
        //initialze alarmmanager
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);


        //initialize timepicker
        alarm_timepicker = (TimePicker)findViewById(R.id.timePicker);

        //initialize tect update box
        update_text = (TextView)findViewById(R.id.textView);

        //create an instance of a calendar
        final Calendar calendar = Calendar.getInstance();

        //creat an intent to alarmreceiver class
        final Intent my_intent = new Intent(this.context,alarmreceiver.class);

        //initialize start buttons

        settime = (Button)findViewById(R.id.settime);
        //initialize stop buttons
        unset = (Button)findViewById(R.id.unset);

        Intent intent = getIntent();
        prepareTime = intent.getStringExtra("PrepareTime");
        wayTime = intent.getStringExtra("WayTime");


        if(!prepareTime.equals(null)) {
            timeToDelay = Integer.parseInt(prepareTime);
        }else{
            timeToDelay = 0;
        }

        if(!wayTime.equals(null)){
            wayToTime = Integer.parseInt(wayTime);
        }else{
            wayToTime = 0;
        }

        //start button onclick listener
        settime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                calendar.set(Calendar.HOUR_OF_DAY,alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE,alarm_timepicker.getMinute());


                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();


                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);


                if(hour>12){
                    hour_string = String.valueOf(hour-12);

                }
                if(minute<10){
                    minute_string = "0"+String.valueOf(minute);
                }

                set_alarm_text("Alarm is set to "+hour_string+":"+minute_string);
                //put in extra string to my _intent
                my_intent.putExtra("extra","on");

                //create a pending intent that delay the intent until the specified calendar time

                pending_intent = PendingIntent.getBroadcast(ClockActivity.this,0,my_intent,PendingIntent.FLAG_UPDATE_CURRENT);


                //set the alarm manager
                alarm_manager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()-(timeToDelay+wayToTime)*60*1000,pending_intent);

                settime.setVisibility(View.GONE);
                unset.setVisibility(View.VISIBLE);


            }
        });




        //stop button onclick listener
        unset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_alarm_text("Alarm off!");

                //cancel the alarm

                alarm_manager.cancel(pending_intent);

                //put extra string into myintent tell off
                my_intent.putExtra("extra","off");

                //stop the ringtone
                sendBroadcast(my_intent);

                settime.setVisibility(View.VISIBLE);
                unset.setVisibility(View.GONE);

            }
        });


    }

    public void stop(View view){
        if(RingtonePlayingService.media_song.isPlaying() ){
            RingtonePlayingService.media_song.stop();
        }
    }
    public void onClickLogOut(View v){
        setResult(RESULT_OK);
        Intent myIntent = new Intent(ClockActivity.this,MainActivity.class);
        startActivity(myIntent);
    }

    private void set_alarm_text(String output) {
        update_text.setText(output);
    }
}

