package loopname.com.digitalspy;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import gestion.GestionPermisos;

public class MainActivity extends AppCompatActivity {

    EditText emailInicio;
    EditText contraseñaInicio;
    CheckBox guardar;
    EditText emailRegistro;
    EditText contraseñaRegistro;
    EditText contraseñaRegistro2;
    EditText nombre;

    LinearLayout inicio;
    LinearLayout registro;
    LinearLayout paso1;
    LinearLayout paso2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        GestionPermisos gpers=new GestionPermisos(this,this);
        if(!gpers.comprobarPermiso(Manifest.permission.ACCESS_FINE_LOCATION)){
            gpers.pedirPermiso(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailInicio=(EditText)this.findViewById(R.id.emailInicio);
        contraseñaInicio=(EditText)this.findViewById(R.id.contraseñaInicio);
        guardar=(CheckBox)this.findViewById(R.id.cbGuardar);
        emailRegistro=(EditText)this.findViewById(R.id.emailRegistro);
        contraseñaRegistro=(EditText)this.findViewById(R.id.contraseñaRegistro);
        contraseñaRegistro2=(EditText)this.findViewById(R.id.contraseñaRegistro2);
        nombre=(EditText)this.findViewById(R.id.nombreDispositivo);

        paso1=(LinearLayout)this.findViewById(R.id.Paso1);
        paso1.setVisibility(View.VISIBLE);
        inicio=(LinearLayout)this.findViewById(R.id.LayoutInicio);
        registro=(LinearLayout)this.findViewById(R.id.LayoutRegistro);
        inicio.setVisibility(View.GONE);
        registro.setVisibility(View.GONE);
        paso2=(LinearLayout)this.findViewById(R.id.Paso2);
        paso2.setVisibility(View.GONE);
    }

    public void iniciarSesion(View v){
        registro.setVisibility(View.GONE);
        inicio.setVisibility(View.VISIBLE);
    }

    public void registro(View v){
        inicio.setVisibility(View.GONE);
        registro.setVisibility(View.VISIBLE);
    }

    public void siguientePaso(View v){
        paso1.setVisibility(View.GONE);
        paso2.setVisibility(View.VISIBLE);
    }

    public void saltarProceso(View v){

        Intent intent=new Intent(this,StartActivity.class);
        intent.putExtra("nombre",nombre.getText().toString());
        this.startActivity(intent);
    }

}
