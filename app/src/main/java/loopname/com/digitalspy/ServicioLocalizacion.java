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

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import gestion.GestionPosicion;
import gestion.Posicion;

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
        String nombre=intent.getStringExtra("nombre");
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10f, this);
        }catch(SecurityException ex){
            ex.printStackTrace();
        }


        Log.d(DEBUG_TAG, lm.toString());
        String latitud=latitude+"";
        String longitud=longitude+"";
        new EnviarLocalizacion().execute(nombre,latitud,longitud);
    }

}

    class EnviarLocalizacion extends AsyncTask<String, Void, Posicion> {

        @Override
        protected void onPreExecute() {
            try {
                Thread.sleep(20000);
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
        @Override
        protected Posicion doInBackground(String... params) {
            Posicion p=new Posicion(params[0],Double.parseDouble(params[1]),Double.parseDouble(params[2]));
            GestionPosicion gpos=new GestionPosicion();
            gpos.enviar(p);
            return p;
        }
        @Override
        protected void onPostExecute(Posicion p) {
            System.out.println(p.toString());
            System.out.println("Proceso completado");
        }


    }
