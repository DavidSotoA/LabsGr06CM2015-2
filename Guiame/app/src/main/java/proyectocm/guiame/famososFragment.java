package proyectocm.guiame;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */

public class famososFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "3";
    ListView listaFamosos;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_famosos, container, false);
        super.onCreate(savedInstanceState);
        listaFamosos = (ListView) v.findViewById(R.id.famosos);
        final FavoritosSQL usuarioBD = new FavoritosSQL(getActivity(), "Ussuaariio", null, 1);
        verFamosos(usuarioBD);

        listaFamosos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                String item = (String) listaFamosos.getItemAtPosition(posicion);
                Intent act = new Intent(getActivity(), GpsActivity.class);
                act.putExtra("lugar",item);
                startActivity(act);
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
