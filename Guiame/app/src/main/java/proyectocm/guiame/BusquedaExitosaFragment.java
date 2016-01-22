package proyectocm.guiame;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */

public class BusquedaExitosaFragment extends Fragment {
    ListView listaBusqueda;
    String TAG ="BusquedaExitosaFragment";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_busqueda_exitosa, container, false);
        super.onCreate(savedInstanceState);
        Log.w(TAG, "showResults on");
        listaBusqueda = (ListView) v.findViewById(R.id.listaBusqueda);
        final FavoritosSQL usuarioBD = new FavoritosSQL(getActivity(), "Usuario", null, 1);
        String query = this.getArguments().getString("query");
        ArrayList lista = this.getArguments().getParcelableArrayList("busqueda");
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lista);
        listaBusqueda.setAdapter(adaptador);

        final LocationManager locManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        listaBusqueda.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    String item = (String) listaBusqueda.getItemAtPosition(posicion);
                    Intent act = new Intent(getActivity(), GpsActivity.class);
                    act.putExtra("lugar", item);
                    startActivity(act);
                } else {
                    Toast.makeText(getActivity(), "Debes activar el gps", Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }
}
