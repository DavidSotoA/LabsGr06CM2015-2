package proyectocm.guiame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Usuario on 27/09/2015.
 */
public class FavoritosSQL extends SQLiteOpenHelper {

    String SQLCreate="CREATE TABLE Lugares (nombre varchar(30), favorito varchar(10), longitud double(20),latitud double(20),PRIMARY KEY (nombre));" ;

    public FavoritosSQL(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLCreate);
        db.execSQL("INSERT INTO lugares (nombre,favorito,latitud,longitud) VALUES ('Biblioteca','false',6.26704, -75.56923) ");
        db.execSQL("INSERT INTO lugares (nombre,favorito,latitud,longitud) VALUES ('Piscina','false',6.26931, -75.56914) ");
        db.execSQL("INSERT INTO lugares (nombre,favorito,latitud,longitud) VALUES ('Cancha sintética','false',6.26916, -75.56828) ");
        db.execSQL("INSERT INTO lugares (nombre,favorito,latitud,longitud) VALUES ('EntradaMetro','false',6.26926, -75.56662) ");
        db.execSQL("INSERT INTO lugares (nombre,favorito,latitud,longitud) VALUES ('Admisiones y registros','false',6.2677, -75.5684) ");
        db.execSQL("INSERT INTO lugares (nombre,favorito,latitud,longitud) VALUES ('Teatro','false',6.26797, -75.56912)");
        db.execSQL("INSERT INTO lugares (nombre,favorito,latitud,longitud) VALUES ('Museo','false',6.26733, -75.56951) ");
        db.execSQL("INSERT INTO lugares (nombre,favorito,latitud,longitud) VALUES ('Cajero ATH 20','false',6.26829, -75.56883) ");
        db.execSQL("INSERT INTO lugares (nombre,favorito,latitud,longitud) VALUES ('Lis','false',6.26762, -75.56771) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS LUGARES");
        db.execSQL(SQLCreate);
    }
}
