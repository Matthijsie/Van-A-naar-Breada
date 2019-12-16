package com.example.vananaarbreda.Map;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

public class GeofenceTransitionsIntentService extends JobIntentService {

    //statics
    private static final String TAG = GeofenceTransitionsIntentService.class.getSimpleName();
    private static final int JOB_ID = 500;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork() called");
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);

        if (event.hasError()){
            Log.e("Error", "Error in geofencingevent int onHandleIntent()");
            return;
        }

        int geofenceTransition = event.getGeofenceTransition();

        //Succesfully detected geofence entering
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
            List<Geofence> triggeringGeofences = event.getTriggeringGeofences();
            String geofenceTransitionDetails = getGeofenceTransitionDetails(geofenceTransition, triggeringGeofences);

            Log.d(TAG, "Entered the geofence " + geofenceTransitionDetails);


            handleEvent();


        //Error with geofence entering
        }else{
            Log.e(TAG, "onHandleWork() called, error thrown");
        }
    }

    private void handleEvent(){
        Log.d(TAG, "handleEvent() called");

    }

    public static void enqueueWork(Context context, Intent intent) {
        Log.d(TAG, "enqueueWork() called");
        enqueueWork(context, GeofenceTransitionsIntentService.class, JOB_ID, intent);
    }

    private String getGeofenceTransitionDetails(int geofenceTransition, List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }

        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType    A transition type constant defined in Geofence
     * @return                  A String indicating the type of transition
     */
    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "Entered Geofence";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "Exited Geofence";
            default:
                return "Unknown Geofence transition";
        }
    }
}
