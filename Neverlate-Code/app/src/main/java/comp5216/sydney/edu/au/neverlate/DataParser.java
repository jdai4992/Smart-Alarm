package comp5216.sydney.edu.au.neverlate;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DataParser {

    public HashMap<String,String> parseDirections(String jsonData){
        JSONObject jsonObject;
        JSONArray jsonArray=null;

        try {
            jsonObject=new JSONObject(jsonData);
            //legs array
            jsonArray=jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getDuration(jsonArray);
    }

    private HashMap<String,String> getDuration(JSONArray googleDirectionsJson) {

        HashMap<String,String> googleDirectionsMap=new HashMap<>();
        String duration="";
        String distance="";

        Log.e("JSON response",googleDirectionsJson.toString());

        try {
            duration=googleDirectionsJson.getJSONObject(0).getJSONObject("duration").getString("text");
            distance=googleDirectionsJson.getJSONObject(0).getJSONObject("distance").getString("text");
            googleDirectionsMap.put("duration",duration);
            googleDirectionsMap.put("distance",distance);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  googleDirectionsMap;
    }

    //Directions
    public String[] parseRoute(String jsonData){
        JSONObject jsonObject;
        JSONArray jsonArray=null;

        try {
            jsonObject=new JSONObject(jsonData);
            //legs array
            jsonArray=jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPaths(jsonArray);
    }

    public String[] getPaths(JSONArray googleStepsJson){
        int count =googleStepsJson.length();
        String[] polylines= new String[count];

        for (int i=0; i<count;i++){
            try {
                polylines[i]=getPath(googleStepsJson.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return polylines;
    }

    //Directions
    public String getPath(JSONObject googlePathJson){

        String polyline="";
        try {
            polyline=googlePathJson.getJSONObject("polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }
}
