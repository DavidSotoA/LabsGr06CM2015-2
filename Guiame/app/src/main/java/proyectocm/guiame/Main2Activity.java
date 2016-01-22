package proyectocm.guiame;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import android.util.Log;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,GpsElegirDestino.onSomeEventListener,GpsRuta.onSomeEventListener {

    MenuItem previusItem;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    String TAG="Main2Activity";
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FavoritosSQL usuarioBD;
    Fragment fragment;
    Bundle context;
    Destino destino;

    public void agregarLogo() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        context= savedInstanceState;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        agregarLogo();
        usuarioBD = new FavoritosSQL(this, "Usuario", null, 1);
        Bundle b = getIntent().getExtras();
        try {
            if(null == context) {
                destino = new Destino("lista",b.getString("nombre"),b.getDouble("latitud"),b.getDouble("longitud"));
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = GpsRuta.newInstance(destino);
                fragmentTransaction.replace(R.id.fragment, fragment);
                fragmentTransaction.commit();
            }
        }catch (NullPointerException e){
            System.out.println("null");
            if(context == null){
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new GpsDefault();
                fragmentTransaction.replace(R.id.fragment, fragment);
                fragmentTransaction.commit();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int i;
        Cursor c = null;
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            SQLiteDatabase bd = usuarioBD.getWritableDatabase();
            final CharSequence[] items = new CharSequence[matches.size()];
            for (i = 0; i < matches.size(); i++) {
                items[i] = matches.get(i);
                System.out.println(items[i]);
                c = bd.rawQuery("SELECT nombre,latitud,longitud FROM lugares WHERE nombre='" + items[i] + "' COLLATE NOCASE", null);
                if (c.moveToFirst()) {
                    i = matches.size();
                }
            }
            if (!c.moveToFirst()) {
                Toast.makeText(this, "No se han encontrado resultados", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "VAMO PA LA " + c.getString(0)+ ",LAT: "+ c.getDouble(1)+",LTG: "+ c.getDouble(2));
                destino = new Destino("gpsDefault", c.getString(0), c.getDouble(1), c.getDouble(2));
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = GpsRuta.newInstance(destino);
                fragmentManager.beginTransaction().replace(R.id.fragment, fragment).commitAllowingStateLoss();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(previusItem!=null) {
            previusItem.setChecked(false);
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragment = GpsRuta.newInstance(this.destino);
            fragmentTransaction.replace(R.id.fragment, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(previusItem!=null && previusItem!=item){
            previusItem.setChecked(false);
        }
        if (id == R.id.lugares) {
            Intent act = new Intent(this, MainActivity.class);
            startActivity(act);
        }
        else if (id == R.id.destino) {
            item.setChecked(true);
            if(null == context) {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new GpsElegirDestino();
                fragmentTransaction.replace(R.id.fragment, fragment);
                fragmentTransaction.commit();
            }
        }
       /*
        else if(id==R.id.satelite){
            item.setChecked(true);
          //  googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else if(id==R.id.normal){
            item.setChecked(true);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        */
        previusItem=item;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void cancelarDestino() {
        if(null == context) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragment = new GpsDefault();
            fragmentTransaction.replace(R.id.fragment, fragment);
            fragmentTransaction.commit();
            previusItem.setChecked(false);
        }
    }

    @Override
    public void aceptarDestino(LatLng destino) {
        previusItem.setChecked(false);
        Log.i(TAG, "latitud:" + destino.latitude + ", longitud" + destino.longitude);
        this.destino= new Destino("gpsElegirDestino",destino.latitude,destino.longitude);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment = GpsRuta.newInstance(this.destino);
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void cancelarDestinoRuta() {
        if(null == context) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragment = new GpsDefault();
            fragmentTransaction.replace(R.id.fragment, fragment);
            fragmentTransaction.commit();
            if(previusItem!=null) {
                previusItem.setChecked(false);
            }
        }
    }

    @Override
    public void rutaTerminada() {
        if(null == context) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragment = new GpsDefault();
            fragmentTransaction.replace(R.id.fragment, fragment);
            fragmentTransaction.commit();
            if (previusItem != null) {
                previusItem.setChecked(false);
            }
        }
    }
}
