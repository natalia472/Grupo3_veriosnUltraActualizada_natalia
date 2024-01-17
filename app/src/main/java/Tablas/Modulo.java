package Tablas;

public class Modulo {


    private String modulo;
    private String ciclo;
    private String usuario;


    public Modulo() {
    }


    public Modulo(String modulo, String ciclo, String usuario) {
        this.modulo = modulo;
        this.ciclo = ciclo;
        this.usuario = usuario;
    }

    public String getModulo() {
        return modulo;
    }



    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getCiclo() {
        return ciclo;
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}