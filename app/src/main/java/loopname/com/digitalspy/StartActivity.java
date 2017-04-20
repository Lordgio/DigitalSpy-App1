package loopname.com.digitalspy;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import gestion.GestionPermisos;

public class StartActivity extends AppCompatActivity {

    Button bt;
    Intent intent;
    Intent intent2;
    String nombre;
    GestionPermisos gPermisos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        gPermisos=new GestionPermisos(this,this);
        bt=(Button)this.findViewById(R.id.BotonInicioFin);
        intent=this.getIntent();
        nombre=intent.getStringExtra("nombre");
        intent2=new Intent(this,ServicioLocalizacion.class);
        intent2.putExtra("nombre",nombre);

    }

    public void servicio(View v){
        if(bt.getText().equals("Activar servicio")){
            if(!gPermisos.comprobarPermiso(Manifest.permission.ACCESS_FINE_LOCATION)){
                gPermisos.pedirPermiso(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            bt.setText("Desactivar servicio");
            iniciarServicio();
        }else{
            pararServicio();
            bt.setText("Activar servicio");
        }
    }

    private void iniciarServicio(){
        this.startService(intent2);
    }

    private void pararServicio(){
        this.stopService(intent2);
    }
}
