package co.edu.udea.cmovil.gr06.yamba;

import java.util.Date;

public class Post {
    private String nombreDelPosteador;
    private String post;
    private String hora;

    public Post(String nombreDelPosteador, String post, String hora) {
        this.nombreDelPosteador = nombreDelPosteador;
        this.post = post;
        this.hora = hora;
    }

    @Override
    public String toString(){
        return nombreDelPosteador+","+post+","+hora;
    }

    public String getNombreDelPosteador() {
        return nombreDelPosteador;
    }

    public void setNombreDelPosteador(String nombreDelPosteador) {
        this.nombreDelPosteador = nombreDelPosteador;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public static String calcularTiempoDePost(int fechaDePost){
        String TiempoDePost;
        Date fechaActual = new Date();
        int mili,minutos,segundos,horas,dias;
        mili=(int)fechaActual.getTime()-(fechaDePost);
        if(mili<0){
            mili=mili*-1;
        }
        segundos=mili/1000;
        minutos=segundos/60;
        horas=minutos/60;
        dias=horas/24;
        if(minutos<1) {
            TiempoDePost ="hace un instante";
        }
        else if(minutos<60) {
            if(minutos==1){
                TiempoDePost = "hace " + Integer.toString(minutos) + " minuto";
            }
            else {
                TiempoDePost = "hace " + Integer.toString(minutos) + " minutos";
            }
        }
        else if(horas<24) {
            if(horas==1){
                TiempoDePost = "hace " + Integer.toString(horas) + " hora";
            }
            else {
                TiempoDePost = "hace " + Integer.toString(horas) + " horas";
            }
        }
        else {
            if(dias==1) {
                TiempoDePost = "hace " + Integer.toString(dias) + " día";
            }
            else{
                TiempoDePost ="hace " + Integer.toString(dias) + " dias";
            }
        }
        return TiempoDePost;
    }
}
