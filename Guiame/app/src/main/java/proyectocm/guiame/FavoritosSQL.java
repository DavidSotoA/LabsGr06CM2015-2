package proyectocm.guiame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Usuario on 27/09/2015.
 */
public class FavoritosSQL extends SQLiteOpenHelper {

    String SQLCreate="CREATE TABLE Lugares (nombre varchar(30), favorito varchar(10), coordenada varchar(20),PRIMARY KEY (nombre));" ;

    public FavoritosSQL(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLCreate);
        db.execSQL("INSERT INTO lugares (nombre,favorito) VALUES ('Biblioteca','false') ");
        db.execSQL("INSERT INTO lugares (nombre,favorito) VALUES ('Aeropuerto','false') ");
        db.execSQL("INSERT INTO lugares (nombre,favorito) VALUES ('Teatro','false') ");
        db.execSQL("INSERT INTO lugares (nombre,favorito) VALUES ('EntradaMetro','false') ");
        db.execSQL("INSERT INTO lugares (nombre,favorito) VALUES ('Bloque 19','false') ");
        db.execSQL("INSERT INTO lugares (nombre,favorito) VALUES ('Lis','false') ");
        db.execSQL("INSERT INTO lugares (nombre,favorito) VALUES ('Telematica','false') ");
        db.execSQL("INSERT INTO lugares (nombre,favorito) VALUES ('Museo','false') ");
        db.execSQL("INSERT INTO lugares (nombre,favorito) VALUES ('Bloque 20','false') ");
        db.execSQL("INSERT INTO lugares (nombre,favorito) VALUES ('Bloque 21','false') ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS LUGARES");

        db.execSQL(SQLCreate);
    }
}
