package proyectocm.guiame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */

public class famososFragment extends Fragment{

    ListView listaFamosos;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_famosos, container, false);
        super.onCreate(savedInstanceState);
        listaFamosos = (ListView) v.findViewById(R.id.famosos);
        final FavoritosSQL usuarioBD = new FavoritosSQL(getActivity(), "Usuario", null, 1);
        verFamosos(usuarioBD);
        final LocationManager locManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        listaFamosos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    String item = (String) listaFamosos.getItemAtPosition(posicion);
                    SQLiteDatabase bd = usuarioBD.getWritableDatabase();
                    Cursor c;
                    c = bd.rawQuery("SELECT nombre,latitud,longitud FROM lugares WHERE nombre='" + item + "' COLLATE NOCASE", null);
                    c.moveToFirst();
                    Intent act = new Intent(getActivity(), Main2Activity.class);
                    act.putExtra("nombre", c.getString(0));
                    act.putExtra("latitud", c.getDouble(1));
                    act.putExtra("longitud", c.getDouble(2));
                    startActivity(act);
                }else{
                    Utilidades.displayPromptForEnablingGPS(getActivity());
                }
            }
        });
        return v;
    }

    public void verFamosos(FavoritosSQL usuarioBD){
        ArrayList lista = new ArrayList<String>();
        SQLiteDatabase bd = usuarioBD.getWritableDatabase();
        Cursor c= bd.rawQuery("SELECT nombre FROM lugares WHERE favorito='false' ORDER BY nombre COLLATE NOCASE ASC", null);
        if (c.moveToFirst()) {
            do {
                String codigo = c.getString(0);
                lista.add(codigo);
            } while (c.moveToNext());
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lista);
        listaFamosos.setAdapter(adaptador);
    }
}
