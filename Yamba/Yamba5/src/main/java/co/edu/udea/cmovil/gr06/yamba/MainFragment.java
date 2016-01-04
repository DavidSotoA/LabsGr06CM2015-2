package co.edu.udea.cmovil.gr06.yamba;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
        */
public class MainFragment extends Fragment {
    ListView listaDePosts;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        super.onCreate(savedInstanceState);
        listaDePosts=(ListView) v.findViewById(R.id.listaDePosts);
        final DbHelper PostBD = new DbHelper(getActivity());
        listarPosts(PostBD);
        return v;
    }

    public void listarPosts(DbHelper PostBD){
        Post post=null;
        ArrayList lista = new ArrayList<Post>();
        int mili,minutos,segundos,horas,dias;
        SQLiteDatabase bd = PostBD.getWritableDatabase();
        Cursor c= bd.rawQuery("SELECT user,message,created_at FROM status ORDER BY created_at DESC", null);
        //c.moveToFirst();
        if (c.moveToFirst()) {
            do {
                Date fechaActual = new Date();
                mili=(int)fechaActual.getTime()-(c.getInt(2));
                segundos=mili/1000;
                minutos=segundos/60;
                horas=minutos/60;
                dias=horas/24;
                if(minutos<1) {
                    post = new Post(c.getString(0), c.getString(1), "hace un instante");
                }
                else if(minutos<60) {
                    if(minutos==1){
                        post = new Post(c.getString(0), c.getString(1), "hace " + Integer.toString(minutos) + " minuto");
                    }
                    else {
                        post = new Post(c.getString(0), c.getString(1), "hace " + Integer.toString(minutos) + " minutos");
                    }
                }
                else if(horas<24) {
                    if(horas==1){
                        post = new Post(c.getString(0), c.getString(1), "hace " + Integer.toString(horas) + " hora");
                    }
                    else {
                        post = new Post(c.getString(0), c.getString(1), "hace " + Integer.toString(horas) + " horas");
                    }
                }
                else {
                    if(dias==1) {
                        post = new Post(c.getString(0), c.getString(1), "hace " + Integer.toString(dias) + " día");
                    }
                    else{
                        post = new Post(c.getString(0), c.getString(1), "hace " + Integer.toString(dias) + " dias");
                    }
                }
                lista.add(post);
            } while (c.moveToNext());
        }
        PostArrayAdapter adaptador = new PostArrayAdapter<Post>(getActivity(), lista);
        listaDePosts.setAdapter(adaptador);
    }
}
