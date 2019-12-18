package com.example.vananaarbreda.Route;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.vananaarbreda.Map.MapHandler;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class RouteDB extends SQLiteOpenHelper {

    private Context context;
    private DatasetChangedListener listener;
    private static final String TAG = RouteDB.class.getSimpleName();
    private static RouteDB instance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DATABASE";
    private static final String TABLE_NAME = "sights";
    private static final String COL_ID = "ID";
    private static final String COL_NAME = "name";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_LATITUDE = "latitude";
    private static final String COL_LONGITUDE = "longitude";
    private static final String COL_PHOTOLINKS = "photolinks";
    private static final String COL_ISVISITED = "isVisited";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY," +
                    COL_NAME + " TEXT," +
                    COL_DESCRIPTION + " TEXT," +
                    COL_LATITUDE + " REAL," +
                    COL_LONGITUDE + " REAL, " +
                    COL_ISVISITED + " TEXT, " +
                    COL_PHOTOLINKS + " TEXT" +
                    ");";


    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static RouteDB getInstance(Context context){
        if (instance == null){
            instance = new RouteDB(context);
        }
        return instance;
    }

    private RouteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.i(TAG, "onCreate() called, creating table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        Log.i(TAG, "onUpgrade() called, dropping table");
        onCreate(db);
    }

    public void insertValue(Coordinate coord, Sight sight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_ID, sight.getID());
        values.put(COL_NAME, sight.getName());
        values.put(COL_DESCRIPTION, sight.getDescription());
        values.put(COL_LATITUDE, coord.getLatitude());
        values.put(COL_LONGITUDE, coord.getLongitude());
        values.put(COL_PHOTOLINKS, "");
        values.put(COL_ISVISITED, sight.isVisited() ? 1 : 0);

        db.insert(TABLE_NAME, null, values);
    }

    public void updateSight(Sight sight){
        Log.d(TAG, "üpdateSightVisited() called");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ID, sight.getID());
        values.put(COL_NAME, sight.getName());
        values.put(COL_DESCRIPTION, sight.getDescription());
        values.put(COL_PHOTOLINKS, "");
        values.put(COL_ISVISITED, sight.isVisited() ? 1 : 0);

        String whereClause = COL_ID + "=" + sight.getID();

        db.update(TABLE_NAME, values, whereClause, null);
        Log.d(TAG, "updated database record with ID: " + sight.getID());

        listener.onDataSetChanged();
    }

    public void resetTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(DROP_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    private String[] convertStringToArray(String s) {
        return s.split("\n");
    }

    public ArrayList<Coordinate> readValues() {
        String query = "SELECT * FROM " + TABLE_NAME + ";";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Coordinate> coordinates = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int ID = cursor.getInt(cursor.getColumnIndex(               COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(       COL_NAME));
                String description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION));
                double latitude = cursor.getDouble(cursor.getColumnIndex(   COL_LATITUDE));
                double longitude = cursor.getDouble(cursor.getColumnIndex(  COL_LONGITUDE));
                boolean isVisited = cursor.getString(cursor.getColumnIndex( COL_ISVISITED)).equals("1");
                String photoLinks = cursor.getString(cursor.getColumnIndex( COL_PHOTOLINKS));
                coordinates.add(new Coordinate(new LatLng(latitude, longitude), name, description, ID, isVisited, convertStringToArray(photoLinks)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return coordinates;
    }

    public void setDataSetChangedListener(DatasetChangedListener listener){
        this.listener = listener;
    }
}
