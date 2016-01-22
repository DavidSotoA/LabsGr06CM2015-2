package proyectocm.guiame;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ListView listBusqueda;
    String TAG="SearchActivity";
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listBusqueda= (ListView) findViewById(R.id.listaBusqueda);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final FavoritosSQL usuarioBD = new FavoritosSQL(this, "Usuario", null, 1);
        String query = getIntent().getStringExtra("query");
        ArrayList lista = new ArrayList<String>();
        SQLiteDatabase bd = usuarioBD.getWritableDatabase();
        Cursor c = bd.rawQuery("SELECT nombre FROM lugares WHERE nombre LIKE '%" + query + "%' ORDER BY nombre COLLATE NOCASE ASC", null);
        if(c.moveToFirst()) {
            do {
                lista.add(c.getString(0));
            } while (c.moveToNext());
            Bundle bundle=new Bundle();
            bundle.putIntegerArrayList("busqueda",lista);
            fragment = new BusquedaExitosaFragment();
            fragment.setArguments(bundle);
            fragmentTransaction.add(android.R.id.content, fragment, fragment.getClass().getSimpleName());
        }
        else{
            Log.d(TAG,"query==null");
            fragment = new BusquedaFallidaFragment();
            fragmentTransaction.add(android.R.id.content, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
