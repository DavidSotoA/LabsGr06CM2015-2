package proyectocm.guiame;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Dijkstra {
    int[][] matrizDeCostos;
    int[][] costoMinimo;
    int[][] rutas;
    ArrayList<Arista> aristas;
    int tam;
   // SQLRutas rutas_bd;
    int n;
    String TAG="Dijkstra";

    public Dijkstra(ArrayList<Arista> aristasArray,int tam,/*SQLRutas rutas_bd*/Arista[] aristas, int num_puntos_nuevos){
 //       this.rutas_bd=rutas_bd;
        this.aristas=aristasArray;
        this.tam=tam;
        if(num_puntos_nuevos!=0) {
            LlenarMatrices(aristas,num_puntos_nuevos);
        }else
            LlenarMatrices();
        imprimirMatriz(matrizDeCostos, "MATRIZ DE COSTOS ANTES");
        for(int i=1;i<=n;i++){
            rutaParaVertice(i);
        }
        //imprimirMatriz(matrizDeCostos,"MATRIZ DE COSTOS");
        //imprimirMatriz(rutas,"MATRIZ DE RUTAS");
    }

    public void LlenarMatrices( ) {
        Cursor c;
        int puntoB;
        int costo;
    //    SQLiteDatabase bd = rutas_bd.getWritableDatabase();
        /*
        c = bd.rawQuery("SELECT MAX(idPunto) FROM punto", null);
        c.moveToFirst();
        */
        n = tam;
        matrizDeCostos = new int[n+1][n+1];
        rutas=new int[n+1][n+1];
        costoMinimo= new int[n+1][n+1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                matrizDeCostos[i][j]=100000;
                costoMinimo[i][j]=100000;
                rutas[i][j]=j;
            }
        }
        for (int i = 1; i <= n; i++) {
            matrizDeCostos[i][i] = 0;
            costoMinimo[i][i] = 0;
      //      c = bd.rawQuery("SELECT distancia,puntoB FROM arista WHERE puntoA=" + (i), null);
            for(int k=0;k<aristas.size();k++){
                if(aristas.get(k).getIdPuntoA()==i){
                    costo = aristas.get(k).getCosto();
                    puntoB = aristas.get(k).getIdPuntoB();
                    matrizDeCostos[i][puntoB] = costo;
                    matrizDeCostos[puntoB][i] = costo;
                    costoMinimo[i][puntoB] = costo;
                    costoMinimo[puntoB][i] = costo;
                }

            }
            /*
            if (c.moveToFirst()) {
                do {
                    costo = c.getInt(0);
                    puntoB = c.getInt(1);
                    matrizDeCostos[i][puntoB] = costo;
                    matrizDeCostos[puntoB][i] = costo;
                    costoMinimo[i][puntoB] = costo;
                    costoMinimo[puntoB][i] = costo;
                } while (c.moveToNext());
            }
            */
        }
    }

    public void LlenarMatrices(Arista[] aristas, int num_puntos_nuevos) {
        Cursor c;
        int puntoB;
        int costo;
//        SQLiteDatabase bd = rutas_bd.getWritableDatabase();
        /*
        c = bd.rawQuery("SELECT MAX(idPunto) FROM punto", null);
        c.moveToFirst();
        */
        n = tam+num_puntos_nuevos;
        matrizDeCostos = new int[n+1][n+1];
        rutas=new int[n+1][n+1];
        costoMinimo= new int[n+1][n+1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                matrizDeCostos[i][j]=100000;
                costoMinimo[i][j]=100000;
                rutas[i][j]=j;
            }
        }
        for (int i = 1; i <= n; i++) {
            matrizDeCostos[i][i] = 0;
            costoMinimo[i][i] = 0;
            if(i>n-num_puntos_nuevos){
                for (int j = 0; j <aristas.length; j++) {
                    if(aristas[j].getIdPuntoA()==i) {
                        System.out.println("");
                        costo = aristas[j].getCosto();
                        puntoB = aristas[j].getIdPuntoB();
                        matrizDeCostos[i][puntoB] = costo;
                        matrizDeCostos[puntoB][i] = costo;
                        costoMinimo[i][puntoB] = costo;
                        costoMinimo[puntoB][i] = costo;
                    }else if(aristas[j].getIdPuntoB()==i) {
                        costo = aristas[j].getCosto();
                        puntoB = aristas[j].getIdPuntoA();
                        matrizDeCostos[i][puntoB] = costo;
                        matrizDeCostos[puntoB][i] = costo;
                        costoMinimo[i][puntoB] = costo;
                        costoMinimo[puntoB][i] = costo;
                    }
                }
            }else {
                for(int k=0;k<this.aristas.size();k++){
                    if(this.aristas.get(k).getIdPuntoA()==i){
                        costo = this.aristas.get(k).getCosto();
                        puntoB = this.aristas.get(k).getIdPuntoB();
                        matrizDeCostos[i][puntoB] = costo;
                        matrizDeCostos[puntoB][i] = costo;
                        costoMinimo[i][puntoB] = costo;
                        costoMinimo[puntoB][i] = costo;
                    }
                }/*
                c = bd.rawQuery("SELECT distancia,puntoB FROM arista WHERE puntoA=" + (i), null);
                if (c.moveToFirst()) {
                    do {
                        costo = c.getInt(0);
                        puntoB = c.getInt(1);
                        matrizDeCostos[i][puntoB] = costo;
                        matrizDeCostos[puntoB][i] = costo;
                        costoMinimo[i][puntoB] = costo;
                        costoMinimo[puntoB][i] = costo;
                    } while (c.moveToNext());
                }
                */
            }
        }
    }

    public void imprimirMatriz(int mat[][], String name){
        System.out.println("//////////////////////////"+name+"////////////////////////////////////");
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                System.out.print(mat[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("//////////////////////////////////////////////////////////////////////////");
    }

    public void rutaParaVertice(int v){
        int i,j,w,paso;
        int s[]= new int[n+1];
        for(int k=1;k<=n;k++){
            s[k]=0;
        }
        s[v]=1;
        i=1;
        while(i<n-1){
            j=1;
            while(s[j]==1){
                j++;
            }
            w=j;
            for(j=w+1; j<=n;j++){
                if(s[j]==0 && costoMinimo[v][j]<costoMinimo[v][w]){
                    w=j;
                }
            }
            s[w]=1;
            i=i+1;
            for (j=1;j<=n;j++){
                if(s[j]==0) {
                    paso = costoMinimo[v][w] + matrizDeCostos[w][j];
                    if (paso < costoMinimo[v][j]) {
                        costoMinimo[v][j] = paso;
                        rutas[v][j] = w;
                    }
                }
            }
        }
    }

    public ArrayList<Integer> consultarRuta(int punto_de_origen,int punto_de_destino, ArrayList<Integer> ruta){
        ruta.add(punto_de_destino);
        if(punto_de_destino==rutas[punto_de_origen][punto_de_destino]){
            ruta.add(punto_de_origen);
            return ruta;
        }
         punto_de_destino = rutas[punto_de_origen][punto_de_destino];
         return consultarRuta(punto_de_origen, punto_de_destino, ruta);
    }

    public int costoMinimo(int punto_de_origen,int punto_de_destino){
       return costoMinimo[punto_de_origen][punto_de_destino];
    }

}
