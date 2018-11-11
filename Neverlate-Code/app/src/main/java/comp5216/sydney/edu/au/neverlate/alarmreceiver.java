package comp5216.sydney.edu.au.neverlate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class alarmreceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("We are in receiver","yay!");

        //fetch extra intent
        String get_your_string = intent.getExtras().getString("extra");
        Log.e("what is the key?",get_your_string);

        //create an intent to the ringtone services
        Intent service_intent = new Intent(context,RingtonePlayingService.class);


        //put the extra string from main to the ringtonepalying sercice

        service_intent.putExtra("extra",get_your_string);
        //start the ringtone service
        context.startService(service_intent);

    }
}
