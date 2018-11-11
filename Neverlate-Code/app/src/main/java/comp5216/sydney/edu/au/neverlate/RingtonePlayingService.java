package comp5216.sydney.edu.au.neverlate;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class RingtonePlayingService extends Service{

    public static MediaPlayer media_song;
    int satrt_id;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);


        //fetch the extra string value
        String satte = intent.getExtras().getString("extra");

        assert satte !=null;
        switch (satte) {
            case "on":
                satrt_id = 1;
                media_song = MediaPlayer.create(this,R.raw.snowdown);
                media_song.start();
                Log.e("test","xiangle");
                break;
            case "off":
                satrt_id = 0;
                break;
            default:
                satrt_id = 0;
                break;
        }



        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {

        // Tell the user we stopped.
        Toast.makeText(this, "On Destroy called", Toast.LENGTH_SHORT).show();
    }

}
