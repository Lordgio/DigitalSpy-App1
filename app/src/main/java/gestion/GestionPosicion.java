package gestion;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class GestionPosicion {
    private double latitud = 0;
    private double longitud = 0;
    private String nombre;
    private Context ctx;
    private LocationManager lm;
    private LocationListener listener;

    public GestionPosicion(String nombre, Context ctx) {
        lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        this.nombre = nombre;
        this.ctx = ctx;
        /*listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                GestionPosicion.latitud = location.getLatitude();
                GestionPosicion.longitud = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };*/
    }

    public void enviarDatos() {
        Posicion p = obtenerPosicion();
        System.out.println(p.toString());
        enviar(p);
    }

    private Posicion obtenerPosicion() {
        Looper.prepare();
        Location l=null;
        try {
            l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitud=l.getLatitude();
            longitud=l.getLongitude();
            //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }catch (SecurityException ex){
            ex.printStackTrace();
        }
        if (longitud != 0 && latitud != 0) {
            return new Posicion(nombre, latitud, longitud);
        } else {
            System.out.println("¡¡¡¡¡¡¡Problema con la localizacion!!!!!!!");
            return new Posicion(nombre,l.getLatitude(),l.getLongitude());
        }

    }

    private void enviar(Posicion p) {
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
}