package proyectocm.guiame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favoritos, container, false);
        super.onCreate(savedInstanceState);
        agregar=(Button) v.findViewById(R.id.agregar);
        listOpciones= (ListView) v.findViewById(R.id.favoritosList);
        listOpciones.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        final FavoritosSQL usuarioBD=new FavoritosSQL(getActivity(), "Ussuaariio",null,1);
        verFavoritos(usuarioBD, v);

        listOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                String item=(String)listOpciones.getItemAtPosition(posicion);
                Intent act = new Intent(getActivity(), GpsActivity.class);
                act.putExtra("lugar",item);
                startActivity(act);
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
                                        } catch(SQLiteConstraintException e){
                                            Toast.makeText(getActivity(), "Nombre repetido", Toast.LENGTH_LONG).show();
                                        }
                                        dialog2.cancel();
                                        dialog.cancel();
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
                            String valor = edit.getText().toString();
                            SQLiteDatabase bd = usuarioBD.getWritableDatabase();
                            try{
                                bd.execSQL("INSERT INTO lugares (nombre,favorito) VALUES ('"+valor+"','true') ");
                                dialog.cancel();
                                verFavoritos(usuarioBD, v);
                            }catch (SQLiteConstraintException e){
                                Toast.makeText(getActivity(), "Nombre repetido", Toast.LENGTH_LONG).show();
                            }
                        } else
                            Toast.makeText(getActivity(), "Debes escribir un nombre", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return v;
    }

    private void verFavoritos(FavoritosSQL usuarioBD, View v) {
        ArrayList lista = new ArrayList<String>();
        SQLiteDatabase bd = usuarioBD.getWritableDatabase();
        Cursor c= bd.rawQuery("SELECT nombre FROM lugares WHERE favorito='true' ORDER BY nombre COLLATE NOCASE ASC", null);
        if (c.moveToFirst()) {
            do {
                String codigo = c.getString(0);
                lista.add(codigo);
            } while (c.moveToNext());
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lista);
        listOpciones.setAdapter(adaptador);
    }

    private void agregarFavoritos(FavoritosSQL usuarioBD, View v) {

    }
}
