package co.edu.udea.cmovil.gr06.yamba;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
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
    String TAG= "MainFragment";
    ListView listaDePosts;
    int count=1;
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
        SQLiteDatabase bd = PostBD.getWritableDatabase();
        Cursor c= bd.rawQuery("SELECT user,message,created_at FROM status ORDER BY created_at DESC", null);
        if (c.moveToFirst()) {
            do {
                post = new Post(c.getString(0), c.getString(1), Post.calcularTiempoDePost(c.getInt(2)));
                lista.add(post);
                count++;
            } while (c.moveToNext() && count<20);
        }
        PostArrayAdapter adaptador = new PostArrayAdapter<Post>(getActivity(), lista);
        listaDePosts.setAdapter(adaptador);
    }
}
