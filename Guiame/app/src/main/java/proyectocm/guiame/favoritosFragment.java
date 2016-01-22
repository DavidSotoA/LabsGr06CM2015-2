package proyectocm.guiame;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class favoritosFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "2";
    Button agregar;
    ListView listOpciones;
    Dialog dialog;
    Button cancelar;
    Button aceptar;
    EditText edit;
    LocationManager locManager;
    String TAG="favoritos";
    LatLng[] limitesUdeA=Utilidades.limitesUdeA();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favoritos, container, false);
        super.onCreate(savedInstanceState);
        agregar=(Button) v.findViewById(R.id.agregar);
        listOpciones= (ListView) v.findViewById(R.id.favoritosList);
        listOpciones.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        final FavoritosSQL usuarioBD = new FavoritosSQL(getActivity(), "Usuario", null, 1);
        locManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        verFavoritos(usuarioBD, v);



        listOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    String item = (String) listOpciones.getItemAtPosition(posicion);
                    SQLiteDatabase bd = usuarioBD.getWritableDatabase();
                    Cursor c;
                    c = bd.rawQuery("SELECT nombre,latitud,longitud FROM lugares WHERE nombre='" + item + "' COLLATE NOCASE", null);
                    c.moveToFirst();
                    Intent act = new Intent(getActivity(), Main2Activity.class);
                    act.putExtra("nombre", c.getString(0));
                    act.putExtra("latitud", c.getDouble(1));
                    act.putExtra("longitud", c.getDouble(2));
                    startActivity(act);
                } else {
                    Utilidades.displayPromptForEnablingGPS(getActivity());
                }
            }
        });

        listOpciones.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, final View v, final int index, long arg3) {
                final String str = listOpciones.getItemAtPosition(index).toString();
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.opciones_fav);
                final ListView listaFavs = (ListView) dialog.findViewById(R.id.listaFavoritos);
                final String[] datos = new String[]{"Editar", "Eliminar"};
                ArrayAdapter<String> adap = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, datos);
                listaFavs.setAdapter(adap);
                dialog.show();

                listaFavs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = ((TextView) view).getText().toString();

                        if (item.equals("Editar")) {
                            final Dialog dialog2 = new Dialog(getActivity());
                            dialog2.setTitle("Editar nombre");
                            dialog2.setContentView(R.layout.popup);
                            edit = (EditText) dialog2.findViewById(R.id.editText);
                            aceptar = (Button) dialog2.findViewById(R.id.aceptar);
                            cancelar = (Button) dialog2.findViewById(R.id.cancelar);
                            aceptar.setText("Editar");
                            edit.setText(str);
                            dialog2.show();

                            cancelar.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    dialog2.cancel();
                                    dialog.cancel();
                                }
                            });

                            aceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (edit.length() > 0) {
                                        String valor = edit.getText().toString();
                                        SQLiteDatabase bd = usuarioBD.getWritableDatabase();
                                        try {
                                            bd.execSQL("UPDATE lugares SET nombre='" + valor + "' WHERE nombre='" + str + "'");
                                            dialog2.cancel();
                                            dialog.cancel();
                                        } catch (SQLiteConstraintException e) {
                                            Toast.makeText(getActivity(), "Nombre repetido", Toast.LENGTH_LONG).show();
                                        }
                                        verFavoritos(usuarioBD, v);
                                    } else
                                        Toast.makeText(getActivity(), "Debes escribir un nombre", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else if (item.equals("Eliminar")) {
                            final AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
                            alerta.setTitle("Advertencia");
                            alerta.setMessage("Desea eliminar " + str + "?");
                            alerta.setCancelable(false);
                            alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    SQLiteDatabase bd = usuarioBD.getWritableDatabase();
                                    bd.execSQL("DELETE FROM lugares WHERE nombre='" + str + "'");
                                    dialog.cancel();
                                    verFavoritos(usuarioBD, v);
                                }
                            });
                            alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    dialog.cancel();
                                }
                            });
                            alerta.show();
                        }
                    }
                });
                return true;
            }
        });


        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    dialog = new Dialog(getActivity());
                    dialog.setTitle("Agregar lugar");
                    dialog.setContentView(R.layout.popup);
                    dialog.show();
                    edit = (EditText) dialog.findViewById(R.id.editText);
                    cancelar = (Button) dialog.findViewById(R.id.cancelar);
                    aceptar = (Button) dialog.findViewById(R.id.aceptar);
                    cancelar.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    aceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (edit.length() > 0) {
                                LocationListener locListener = new LocationListener() {
                                    public void onLocationChanged(Location location) {
                                    }

                                    public void onProviderDisabled(String provider) {
                                    }

                                    public void onProviderEnabled(String provider) {
                                    }

                                    public void onStatusChanged(String provider, int status, Bundle extras) {

                                    }
                                };
                                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                locManager.removeUpdates(locListener);
                                final Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (loc != null) {
                                    if(!Recta.contains(limitesUdeA,new LatLng(loc.getLatitude(),loc.getLongitude()))){
                                        Toast.makeText(getActivity(),"el lugar debe de estar dentro de la UdeA",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        String valor = edit.getText().toString();
                                        SQLiteDatabase bd = usuarioBD.getWritableDatabase();
                                        try {
                                            bd.execSQL("INSERT INTO lugares (nombre,favorito,longitud,latitud) VALUES ('" + valor + "','true'," + loc.getLongitude() + "," + loc.getLatitude() + ")");
                                            dialog.cancel();
                                            verFavoritos(usuarioBD, v);
                                        } catch (SQLiteConstraintException e) {
                                            Toast.makeText(getActivity(), "Nombre repetido", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } else
                                    Toast.makeText(getActivity(), "Satelite deshabilitado, intente mas tarde", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getActivity(), "Debes escribir un nombre", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Utilidades.displayPromptForEnablingGPS(getActivity());
                }
            }
        });
        return v;
    }

    private void verFavoritos(FavoritosSQL usuarioBD, View v) {
        Log.w(TAG, "verFavoritos on");
        ArrayList lista = new ArrayList<String>();
        SQLiteDatabase bd = usuarioBD.getWritableDatabase();
        Cursor c= bd.rawQuery("SELECT nombre FROM lugares WHERE favorito='true' ORDER BY nombre COLLATE NOCASE ASC", null);
        if (c.moveToFirst()) {
            do {
                lista.add(c.getString(0));
            } while (c.moveToNext());
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lista);
        listOpciones.setAdapter(adaptador);
    }

}
