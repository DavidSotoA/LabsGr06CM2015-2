package co.edu.udea.cmovil.gr06.yamba;

/**
 * Created by Usuario on 16/10/2015.
 */
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
}
