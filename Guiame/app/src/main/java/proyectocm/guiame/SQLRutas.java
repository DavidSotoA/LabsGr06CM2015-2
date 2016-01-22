package proyectocm.guiame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Usuario on 01/12/2015.
 */
public class SQLRutas extends SQLiteOpenHelper {

    public SQLRutas(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Punto ( idPunto INTEGER PRIMARY KEY AUTOINCREMENT,longitud double(20),latitud double(20))");
        db.execSQL("CREATE TABLE  Arista(puntoA integer, puntoB integer, distancia double(4),PRIMARY KEY (puntoA, puntoB)," +
                    "FOREIGN KEY (puntoA) REFERENCES Punto(idPunto)," +
                    "FOREIGN KEY (puntoB) REFERENCES Punto(idPunto))");

        //INSERTAR PUNTOS

        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26919,-75.5667)");//1
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26897, -75.56668)");//2
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26865, -75.56675)");//3
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26854, -75.56689)");//4
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26827, -75.56693)");//5
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26755, -75.56713)");//6
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26757, -75.56779)");//7
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.2676, -75.56792)");//8
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26774, -75.56794)");//9
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26792, -75.56842)");//10
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26842, -75.56839)");//11
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26865, -75.56835)");//12
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26855, -75.56754)");//13
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26827, -75.56759)");//14

        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26792, -75.56764)");//15
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26783, -75.56724)");//16
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26737, -75.56708)");//17
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26566, -75.56752)");//18
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26543, -75.56765)");//19
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26525, -75.56789)");//20
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26524, -75.56843)");//21
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26551, -75.57032)");//22
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26561, -75.57054)");//23
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.2659, -75.57079)");//24
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26625, -75.57088)");//25
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26683, -75.57093)");//26
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26755, -75.57077)");//27
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.27013, -75.57015)");//28
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.27031, -75.56996)");//29
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.27039, -75.56973)");//30
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.2695, -75.56702)");//31
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26932, -75.56687)");//32
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26742, -75.56803)");//33
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26708, -75.5681)");//34
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26717, -75.56865)");//35
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.2678, -75.56858)");//36
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.2679, -75.56898)");//37
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26825, -75.56893)");//38
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26848, -75.56888)");//39
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26867, -75.56887)");//40
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26872, -75.56921)");//41
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26838, -75.56929)");//42
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26979, -75.56907)");//43
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.27009, -75.56914)");//44
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.27021, -75.56945)");//45
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.27025, -75.56964)");//46
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.2701, -75.56997)");//47
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26895, -75.57017)");//48
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26871, -75.57015)");//49
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.2686, -75.5696)");//50
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26765, -75.56909)");//51
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26775, -75.57023)");//52
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26837, -75.57016)");//53
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26824, -75.56926)");//54
        db.execSQL("INSERT INTO punto (latitud,longitud) VALUES (6.26687, -75.56868)");//55

        //INSERTAR ARISTAS
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (1,2,24)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (2,3,37)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (3,4,20)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (4,5,30)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (5,6,83)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (6,7,73)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (7,8,15)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (8,9,16)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (9,10,58)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (10,11,52)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (11,12,29)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (12,13,90)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (13,14,32)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (13,4,73)");

        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (14,15,40)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (15,16,46)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (16,6,34)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (17,6,21)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (18,17,196)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (19,18,30)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (20,19,33)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (20,21,60)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (21,22,211)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (22,23,27)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (23,24,46)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (24,25,40)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (25,26,64)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (26,27,84)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (27,28,293)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (28,29,28)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (29,30,27)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (30,31,316)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (31,32,27)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (32,3,76)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (33,9,23)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (34,33,39)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (35,34,63)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (36,35,70)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (36,10,23)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (37,36,45)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (38,37,40)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (39,38,26)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (39,11,55)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (40,39,21)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (41,40,38)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (42,41,39)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (42,38,43)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (43,41,121)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (44,43,34)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (45,44,37)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (46,45,22)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (47,46,41)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (48,47,129)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (49,48,27)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (50,49,62)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (50,41,45)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (51,37,30)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (52,51,126)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (53,52,69)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (54,53,101)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (54,42,15)");
        db.execSQL("INSERT INTO arista (puntoA,puntoB,distancia) VALUES (55,35,33)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
