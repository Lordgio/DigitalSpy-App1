package loopname.com.digitalspy;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import java.util.Timer;
import java.util.TimerTask;
import gestion.GestionPosicion;

public class ServicioLocalizacion extends Service {

    Timer tm=new Timer();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String nombre=intent.getStringExtra("nombre");
        tm.schedule(new EnviarPosicion(nombre,this),0,60000);

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

class EnviarPosicion extends TimerTask{

    String nombre;
    Context ctx;

    public EnviarPosicion(String nombre,Context ctx){
        this.nombre=nombre;
        this.ctx=ctx;
    }
    @Override
    public void run() {
        GestionPosicion gestion=new GestionPosicion(nombre,ctx);
        gestion.enviarDatos();
    }
}
