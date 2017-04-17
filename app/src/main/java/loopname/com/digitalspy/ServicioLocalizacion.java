package loopname.com.digitalspy;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import gestion.GestionPosicion;

public class ServicioLocalizacion extends Service implements LocationListener {

    private final String DEBUG_TAG = "[GPS Ping]";

    private LocationManager lm;
    private double latitude;
    private double longitude;

    @Override
    public void onLocationChanged(Location location) {
        Log.d(DEBUG_TAG, "onLocationChanged");
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(DEBUG_TAG, "onProviderDisabled");
        Toast.makeText(
                getApplicationContext(),
                "Attempted to ping your location, and GPS was disabled.",
                Toast.LENGTH_LONG).show();
    }
    @Override
    public void onProviderEnabled(String provider) {
        Log.d(DEBUG_TAG, "onProviderEnabled");
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10f, this);
        }catch(SecurityException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(DEBUG_TAG, "onStatusChanged");

    }

    @Override
    public void onCreate() {
        Log.d(DEBUG_TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.d(DEBUG_TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(DEBUG_TAG, "onBind");

        return null;
    }
    @Override
    public void onStart(Intent intent, int startid) {
        Log.d(DEBUG_TAG, "onStart");

        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10f, this);
        }catch(SecurityException ex){
            ex.printStackTrace();
        }


        Log.d(DEBUG_TAG, lm.toString());

        new SubmitLocationTask(ServicioLocalizacion.this).execute();
    }

    private void locationTimer() {

        new Handler().postDelayed(new Runnable() {
            // @Override
            @Override
            public void run() {
                locationTimeExpired = true;
            }
        }, 12000);
    }
    private class SubmitLocationTask extends AsyncTask<String, Void, Boolean> {

        /** application context. */
        private Context context;

        private Service service;

        public SubmitLocationTask(Service service) {
            this.service = service;
            context = service;
        }

        @Override
        protected void onPreExecute() {
            locationTimer(); // Start 12 second timer
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success && xmlSuccessful) {
                lm.removeUpdates(ServicePingLocation.this);
                onDestroy();
            } else {
                if (!GlobalsUtil.DEBUG_ERROR_MSG.equals(""))
                    Toast.makeText(getBaseContext(),
                            GlobalsUtil.DEBUG_ERROR_MSG, Toast.LENGTH_SHORT)
                            .show();
                GlobalsUtil.DEBUG_ERROR_MSG = "";
            }
        }
        @Override
        protected Boolean doInBackground(final String... args) {
            try {

                DateFormat df = null;
                df = new SimpleDateFormat("M/d/yy h:mm a");
                Date todaysDate = new Date();// get current date time with
                // Date()
                String currentDateTime = df.format(todaysDate);

                while ((accuracy > 100f || accuracy == 0.0)
                        && !locationTimeExpired) {
                    // We just want it to sit here and wait.
                }

                return xmlSuccessful = SendToServerUtil.submitGPSPing(
                        0, longitude,
                        latitude, accuracy, currentDateTime);
            } catch (Exception e) {

                return false;
            }
        }
    }