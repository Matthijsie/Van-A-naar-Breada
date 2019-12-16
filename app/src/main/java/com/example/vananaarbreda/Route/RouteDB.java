package com.example.vananaarbreda.Route;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.vananaarbreda.Activities.SightActivity;
import com.google.android.gms.common.internal.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import static android.content.ContentValues.TAG;
import static android.database.sqlite.SQLiteDatabase.create;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class RouteDB{
    private DatabaseHelper dtb;
    public RouteDB(Context context) throws IOException {
        dtb = new DatabaseHelper(context);
    }

    public DatabaseHelper getDatabaseHelper(){
        return this.dtb;
    }

    public class DatabaseHelper extends SQLiteOpenHelper {
        // LOGtag
        private static final String TAG = "DatabaseHelper";

        //Database stuff
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "dtb_vanAnaarBreda";

        //Database table names
        private static final String TABLE_ROUTE = "tbl_routes";
        private static final String TABLE_COORDS = "tbl_coords";
        private static final String TABLE_SIGHTS = "tbl_sights";
        private static final String TABLE_LINK_CS = "tbl_link_coordssights";

        //Table columns (in common)
        private static final String KEY_ID = "ID";
        private static final String KEY_NAME = "Name";
        private static final String KEY_DESCRIPTION = "Description";

        //Table columns(Sights)
        private static final String KEY_INFORMATION = "Information";

        //Table columns(Coords)
        private static final String KEY_LATITUDE = "Latitude";
        private static final String KEY_LONGITUDE = "Longitude";
        private static final String KEY_SIGHTID = "SightID";

        //Table columns(Routes)
        private static final String KEY_DURATION = "Duration";

        //Table columns(CoordRoute Link)
        private static final String KEY_ROUTEID = "RouteID";
        private static final String KEY_COORDINATEID = "CoordinateID";

        //Table creation
        private static final String CREATE_TABLE_ROUTE =
                "CREATE TABLE " + TABLE_ROUTE + " (" + KEY_ID + " INTEGER PRIMARY KEY, " +
                        KEY_NAME + " VARCHAR(45), " + KEY_DESCRIPTION + " VARCHAR(255), " +
                        KEY_DURATION + " INTEGER )";

        private static final String CREATE_TABLE_SIGHT =
                "CREATE TABLE " + TABLE_SIGHTS + " (" + KEY_ID + " INTEGER PRIMARY KEY, " +
                        KEY_NAME + " VARCHAR(45), " + KEY_DESCRIPTION + " VARCHAR(255), " +
                        KEY_INFORMATION + " VARCHAR(255) )";

        private static final String CREATE_TABLE_COORD =
                "CREATE TABLE " + TABLE_COORDS + " (" + KEY_ID + " INTEGER PRIMARY KEY, " +
                        KEY_LATITUDE + " DECIMAL, " + KEY_LONGITUDE + " DECIMAL, " +
                        KEY_SIGHTID + " INTEGER )";

        private static final String CREATE_LINK_TABLE_COORD_SIGHT =
                "CREATE TABLE " + TABLE_LINK_CS + " (" + KEY_ROUTEID + " INTEGER, " +
                        KEY_SIGHTID + " INTEGER )";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_ROUTE);
            Log.i(TAG, "onCreate: Route");
            db.execSQL(CREATE_TABLE_SIGHT);
            Log.i(TAG, "onCreate: Sight");
            db.execSQL(CREATE_TABLE_COORD);
            Log.i(TAG, "onCreate: Coord");
            db.execSQL(CREATE_LINK_TABLE_COORD_SIGHT);
            Log.i(TAG, "onCreate: Coord-Sight");


            Sight sight = new Sight();
            sight.setName("test1");
            sight.setID(1);
            createSight(sight);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGHTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COORDS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINK_CS);

            onCreate(db);
        }

        // region Coordinate CRUD

        public void createCoordinateEntry(Coordinate c)
        {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_LATITUDE, c.getLatitude());
            values.put(KEY_LONGITUDE, c.getLongitude());
            // TODO Hieronder moet nog veranderd worden naar het ID van de Sight.
            values.put(KEY_SIGHTID, c.getSight().getName());

            db.insert(TABLE_COORDS, null, values);
        }

        public Coordinate getCoordinate(long coord_id) {
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT * FROM " + TABLE_COORDS + " WHERE " + KEY_ID + " = " + coord_id;

            Cursor c = db.rawQuery(query, null);

            if(c != null) {
                c.moveToFirst();
            }

            Coordinate coord = new Coordinate();
            coord.setLatitude(c.getDouble(c.getColumnIndex(KEY_LATITUDE)));
            coord.setLongitude(c.getDouble(c.getColumnIndex(KEY_LONGITUDE)));
            return coord;
        }

        public void deleteCoordinate(long coord_id) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_COORDS, KEY_ID + " = ?", new String[] { String.valueOf(coord_id)});
        }

        // endregion

        //region Route CRUD

        public void createRoute(Route route) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_ID, route.getID());
            values.put(KEY_NAME, route.getName());
            values.put(KEY_DESCRIPTION, route.getDesc());
            values.put(KEY_DURATION, route.getDuration());

            db.insert(TABLE_ROUTE, null, values);
        }

        public Route getRoute(long route_id) {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_ROUTE + " WHERE " + KEY_ID + " = " + route_id;

            Cursor c = db.rawQuery(query, null);

            if(c != null) {
                c.moveToFirst();
            }

                Coordinate coord = new Coordinate();
                coord.setLatitude(c.getDouble(c.getColumnIndex(KEY_LATITUDE)));
                coord.setLongitude(c.getDouble(c.getColumnIndex(KEY_LONGITUDE)));

                Route route = new Route();
                route.setID(c.getInt(c.getColumnIndex(KEY_ID)));
                route.setDesc(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                route.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                route.setDuration(c.getInt(c.getColumnIndex(KEY_DURATION)));

                //TODO Geef nog een lijst met coords mee aan de route.

                return route;
        }

        public void deleteRoute(Route route) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_ROUTE, KEY_ID + " = ?", new String[] { String.valueOf(route.getID())});
        }

        public ArrayList<Route> getRoutes(){
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_ROUTE;

            Cursor c = db.rawQuery(query, null);

            if(c != null) {
                c.moveToFirst();
            }

            
            //let op de query response moet nog worden opgedeeld in individuele routes!
            return null;
        }

        //endregion

        //region Sight CRUD

        public void createSight(Sight s)
        {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_ID, s.getID());
            values.put(KEY_NAME, s.getName());
            values.put(KEY_DESCRIPTION, s.getDescription());
            values.put(KEY_INFORMATION, s.getInformation());

            db.insert(TABLE_SIGHTS, null, values);
        }

        public Sight getSight(long sight_id) {
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT * FROM " + TABLE_SIGHTS + " WHERE " + KEY_ID + " = " + sight_id;

            Cursor c = db.rawQuery(query, null);

            if(c != null) {
                c.moveToFirst();
            }

            Sight sight = new Sight();
            sight.setID(c.getInt(c.getColumnIndex(KEY_ID)));
            sight.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            sight.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
            sight.setInformation(c.getString(c.getColumnIndex(KEY_INFORMATION)));

            return sight;
        }

        public void deleteSight(long sight_id) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_SIGHTS, KEY_ID + " = ?", new String[] { String.valueOf(sight_id)});
        }
        //endregion

        //region CSLink CRUD

        public void createCoordSightLink(long coord_id, long sight_id) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_COORDINATEID, coord_id);
            values.put(KEY_SIGHTID, sight_id);

            long id = db.insert(TABLE_LINK_CS, null, values);
        }


        //endregion

        // closing database
        public void closeDB() {
            SQLiteDatabase db = this.getReadableDatabase();
            if (db != null && db.isOpen())
                db.close();
        }
    }


}
