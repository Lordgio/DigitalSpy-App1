package loopname.com.digitalspy;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import gestion.Posicion;

public class ServicioLocalizacion extends Service implements LocationListener {

    private LocationManager lm;
    private double latitude;
    private double longitude;
    private Timer tm;

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
    @Override
    public void onProviderDisabled(String provider) {}
    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onCreate() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String nombre=intent.getStringExtra("nombre");
        lm = (LocationManager) ServicioLocalizacion.this.getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100, ServicioLocalizacion.this);
        }catch(SecurityException ex){
            ex.printStackTrace();
        }
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                Posicion p=null;
                if(latitude==0 && longitude==0){
                    try {
                        Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(l==null){
                            p=null;
                        }else{
                            p=new Posicion(nombre,l.getLongitude(),l.getLatitude());
                        }
                    }catch(SecurityException ex){
                        ex.printStackTrace();
                    }
                }else{
                    p=new Posicion(nombre,latitude,longitude);
                }
                String host = "51.254.214.165";
                int puerto = 8080;
                try {
                    Socket sc = new Socket(host, puerto);
                    PrintStream salida = new PrintStream(sc.getOutputStream());
                    //Envía código de aplicacion
                    salida.println("aaa");
                    //Envía JSON
                    System.out.println(p.toString());
                    ObjectOutputStream oos = new ObjectOutputStream(sc.getOutputStream());
                    oos.writeObject(p);
                    sc.close();
                    System.out.println("!!!!!!Envio correcto¡¡¡¡¡¡");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        };
        tm=new Timer();
        tm.schedule(task,0,30000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        tm.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
         return null;
    }

}
