package co.edu.udea.cmovil.gr06.yamba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Usuario on 16/10/2015.
 */
public class PostArrayAdapter<Post> extends ArrayAdapter<Post> {

    public PostArrayAdapter(Context context, List<Post> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo con two_line_list_item.xml
            listItemView = inflater.inflate(R.layout.post, parent, false);
        }

        //Obteniendo instancias de los text views
        TextView nombreDelPosteador = (TextView)listItemView.findViewById(R.id.nombre);
        TextView post = (TextView)listItemView.findViewById(R.id.post);
        TextView hora = (TextView)listItemView.findViewById(R.id.hora);

        //Obteniendo instancia de la Tarea en la posición actual
        Post item = (Post) getItem(position);

        //Dividir la cadena en Nombre y Hora
        String cadenaBruta;
        String subCadenas [];
        String delimitador = ",";

        cadenaBruta = item.toString();
        subCadenas = cadenaBruta.split(delimitador,3);

        nombreDelPosteador.setText(subCadenas[0]+": ");
        post.setText(subCadenas[1]);
        hora.setText(subCadenas[2]);

        //Devolver al ListView la fila creada
        return listItemView;

    }
}


