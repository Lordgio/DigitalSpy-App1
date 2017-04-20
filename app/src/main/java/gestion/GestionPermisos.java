package gestion;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.security.Permission;
import java.util.ArrayList;


public class GestionPermisos extends AppCompatActivity{

    private int PermissionCode=10;
    private Context ctx;
    private Activity act;

    public GestionPermisos(Context ctx,Activity act){
        this.ctx=ctx;
        this.act=act;
    }

    public boolean comprobarPermiso(String permiso){
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(ctx,permiso);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }

    public void pedirPermiso(String permiso){

        if (ActivityCompat.shouldShowRequestPermissionRationale(act,permiso)){
            AlertDialog.Builder cuadro=new AlertDialog.Builder(act);
            cuadro.setTitle("Permisos");
            cuadro.setMessage("Los permisos siguientes son necesarios para el funcionamiento de la app.");
            cuadro.setPositiveButton(android.R.string.ok,null);
            cuadro.show();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(act,new String[]{permiso}, PermissionCode);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == PermissionCode){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Displaying a toast
                Toast.makeText(act,"Permiso aceptado",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(act,"Permiso denegado",Toast.LENGTH_LONG).show();
            }
        }
    }
}
