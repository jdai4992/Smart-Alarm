package comp5216.sydney.edu.au.neverlate;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetDirectionsData extends AsyncTask<Object,String,String> {

    GoogleMap mMap;
    String googleDirectionsData;
    String url;
    String duration="",distance="";


    @Override
    protected String doInBackground(Object... objects) {
        mMap=(GoogleMap)objects[0];
        url=(String)objects[1];

        DownloadUrl downloadUrl=new DownloadUrl();

        try {
            googleDirectionsData=downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {
        HashMap<String,String> directionsList=null;
        DataParser parser= new DataParser();
        String[] routeList;


        directionsList=parser.parseDirections(s);
        duration=directionsList.get("duration");
        distance=directionsList.get("distance");
        Log.e("duration",duration);
        Log.e("distance",distance);


        MapsActivity.distanceResult=distance;
        MapsActivity.durationResult=duration;

        //for route
        routeList=parser.parseRoute(s);
        displayDirections(routeList);

    }

    public void displayDirections(String[] routeList){
        int count=routeList.length;
        for(int i=0;i<count;i++){
            PolylineOptions options=new PolylineOptions();
            options.color(Color.rgb(51,51,255));
            options.width(10);
            options.addAll(PolyUtil.decode(routeList[i]));
            mMap.addPolyline(options);
            Log.e("test","123");
        }
    }
}
