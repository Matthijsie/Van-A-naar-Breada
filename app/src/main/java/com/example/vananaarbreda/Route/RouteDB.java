package com.example.vananaarbreda.Route;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import static android.database.sqlite.SQLiteDatabase.create;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class RouteDB{

    public RouteDB(Context context) {
        //SQLiteDatabase mydatabase = openOrCreateDatabase("Routes.db",null);
        SQLiteDatabase mydatabase = create(new SQLiteDatabase.CursorFactory() {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
                return null;
            }
        });
        RouteDBConfig config = new RouteDBConfig(context);
        config.onCreate(mydatabase);
    }


    private class RouteDBConfig extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Routes.db";

        public RouteDBConfig(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_ROUTES);
            Log.e("ONCREATE",db.getPath());
//        db.execSQL(TABLE_SIGHTS);
//        db.execSQL(TABLE_COORDINATE);
//        db.execSQL(LINKTABLE_ROUTE_COORDINATES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }


         String TABLE_ROUTES = "" +
                "CREATE TABLE tbl_routes (\n" +
                "  `ID` INT NOT NULL,\n" +
                "  `Name` VARCHAR(45) ,\n" +
                "  `Desc` VARCHAR(255) ,\n" +
                "  `Duration` INT,\n" +
                "  PRIMARY KEY (`ID`));";

         String TABLE_SIGHTS = "" +
                "CREATE TABLE `pt2`.`tbl_sights` (\n" +
                "  `ID` INT NOT NULL,\n" +
                "  `Name` VARCHAR(255) NULL,\n" +
                "  `Description` VARCHAR(255) NULL,\n" +
                "  `Information` VARCHAR(255) NULL,\n" +
                "  PRIMARY KEY (`ID`));";

         String TABLE_COORDINATE = "" +
                "CREATE TABLE `pt2`.`tbl_coordinates` (\n" +
                "  `ID` INT NOT NULL,\n" +
                "  `Latitude` VARCHAR(255) NULL,\n" +
                "  `Longitude` VARCHAR(255) NULL,\n" +
                "  `SightID` INT NULL,\n" +
                "  PRIMARY KEY (`ID`),\n" +
                "  CONSTRAINT `SightID`\n" +
                "    FOREIGN KEY (`ID`)\n" +
                "    REFERENCES `pt2`.`tbl_sights` (`ID`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION);" ;

         String TABLE_IMAGES = "" +
                "CREATE TABLE `pt2`.`tbl_images` (\n" +
                "  `ID` INT NOT NULL,\n" +
                "  `Data` BLOB NULL,\n" +
                "  PRIMARY KEY (`ID`));";
         String LINKTABLE_ROUTE_COORDINATES = "" +
                "CREATE TABLE `pt2`.`tbl_route_coordinate` (\n" +
                "  `RouteID` INT NOT NULL,\n" +
                "  `CoordinateID` INT NULL,\n" +
                "  INDEX `RouteID_idx` (`RouteID` ASC) VISIBLE,\n" +
                "  INDEX `CoordinateID_idx` (`CoordinateID` ASC) VISIBLE,\n" +
                "  CONSTRAINT `RouteID`\n" +
                "    FOREIGN KEY (`RouteID`)\n" +
                "    REFERENCES `pt2`.`tbl_routes` (`ID`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION,\n" +
                "  CONSTRAINT `CoordinateID`\n" +
                "    FOREIGN KEY (`CoordinateID`)\n" +
                "    REFERENCES `pt2`.`tbl_coordinates` (`ID`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION);";
    }
}
