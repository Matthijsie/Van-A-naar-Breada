package com.example.vananaarbreda.Route;

public interface DatasetChangedListener {

    /**
     * Used to immediately update route/waypoint information if data changes in the database
     */
    void onDataSetChanged();
}
