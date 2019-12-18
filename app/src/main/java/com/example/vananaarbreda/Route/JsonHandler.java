package com.example.vananaarbreda.Route;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonHandler {
    private final String PATH = "ConvertedData.json";
    private Context context;

    public JsonHandler(Context context) {
        this.context = context;

        RouteDB.getInstance(this.context).resetTable();
        try {
            insertJsonIntoDatabase();
        } catch (JSONException e) {
            e.getStackTrace();
        }
    }

    private void insertJsonIntoDatabase() throws JSONException {
        JSONArray array = new JSONArray(loadJSONFromAsset());

        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);

            int id = jsonObject.getInt("ID");
            String name = jsonObject.getString("Name");
            String desc = jsonObject.getString("Desc");
            LatLng coords = new LatLng(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"));
            boolean isVisited = jsonObject.getBoolean("isVisited");
            JSONArray photoLinks = jsonObject.getJSONArray("Photos");

            String[] photos = new String[photoLinks.length()];

            for (int j = 0; j < photoLinks.length(); j++) {
                photos[j] = photoLinks.getString(j);
            }
            Sight s = new Sight(id, name, desc, isVisited);
            Coordinate coord = new Coordinate(coords.latitude, coords.longitude, s);
            RouteDB.getInstance(this.context).insertValue(coord, s);
        }

    }

    // https://stackoverflow.com/questions/13814503/reading-a-json-file-in-android

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getResources().getAssets().open(PATH);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, StandardCharsets.UTF_8);


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }
}
