package proyectocm.guiame;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Utilidades {

    public static final LatLng COORDENADAS_UDEA=new LatLng(6.2668882812220135,-75.56892332620919);
    public static final String DESCRIBABLE_KEY = "lugar";

    public static void animacionDeCamara(GoogleMap googleMap, LatLng mover_camara, int zoom, int bearing){
        if (mover_camara != null) {
            CameraPosition camPos = new CameraPosition.Builder().target(mover_camara).zoom(zoom).bearing(bearing).tilt(70).build();
            CameraUpdate cam = CameraUpdateFactory.newCameraPosition(camPos);
            googleMap.animateCamera(cam);
        }
    }

    public static void displayPromptForEnablingGPS(final Activity activity){
        final android.support.v7.app.AlertDialog.Builder builder =  new android.support.v7.app.AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Debes activar el GPS";
        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }

    public static LatLng[] limitesUdeA(){
        ArrayList <LatLng> limites= new ArrayList<>();
        limites.add(new LatLng(6.27145, -75.57006));//27
        limites.add(new LatLng(6.27107, -75.56975));//28
        limites.add(new LatLng(6.27078, -75.56944));//29
        limites.add(new LatLng(6.27057, -75.5691));//30
        limites.add(new LatLng(6.27041, -75.56879));//31
        limites.add(new LatLng(6.2703, -75.56841));//32
        limites.add(new LatLng(6.26957, -75.56644));//33
        limites.add(new LatLng(6.26622, -75.56724));//19
        limites.add(new LatLng(6.2646, -75.56759));//20
        limites.add(new LatLng(6.26497, -75.57011));//21
        limites.add(new LatLng(6.26618, -75.57108));//22
        limites.add(new LatLng(6.26639, -75.57113));//23
        limites.add(new LatLng(6.26679, -75.57129));//24
        limites.add(new LatLng(6.26773, -75.57107));//25
        limites.add(new LatLng(6.2713, -75.57025));//26
        limites.add(new LatLng(6.27145, -75.57006));//27
        LatLng[] limit = new LatLng[limites.size()];
        limit = limites.toArray(limit);
        return limit;
    }
}
