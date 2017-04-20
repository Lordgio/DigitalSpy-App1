package loopname.com.digitalspy;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import gestion.GestionPermisos;

public class StartActivity extends AppCompatActivity {

    Button bt;
    Intent intent;
    Intent intent2;
    String nombre;
    ProgressBar progress;
    GestionPermisos gPermisos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        gPermisos=new GestionPermisos(this,this);
        bt=(Button)this.findViewById(R.id.BotonInicioFin);
        progress=(ProgressBar)this.findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);
        intent=this.getIntent();
        nombre=intent.getStringExtra("nombre");
        intent2=new Intent(this,ServicioLocalizacion.class);
        intent2.putExtra("nombre",nombre);

    }

    public void servicio(View v){
        if(bt.getText().equals("Activar servicio")){
            if(!gPermisos.comprobarPermiso(Manifest.permission.ACCESS_FINE_LOCATION)) {
                gPermisos.pedirPermiso(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            progress.setVisibility(View.VISIBLE);
            iniciarServicio();
            bt.setText("Desactivar servicio");
            progress.setVisibility(View.GONE);
        }else{
            progress.setVisibility(View.VISIBLE);
            pararServicio();
            bt.setText("Activar servicio");
            progress.setVisibility(View.GONE);
        }
    }

    private void iniciarServicio(){
        this.startService(intent2);
    }

    private void pararServicio(){
        this.stopService(intent2);
    }
}
