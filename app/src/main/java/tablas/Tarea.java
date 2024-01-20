package tablas;


public class Tarea {
    private int id;
    private String modulo;
    private String tarea;
    private String fechaEntrega;
    private String usuario;

    public Tarea() {
    }

    public Tarea(int id, String modulo, String tarea, String fechaEntrega, String usuario) {
        this.id = id;
        this.modulo = modulo;
        this.tarea = tarea;
        this.fechaEntrega = fechaEntrega;
        this.usuario = usuario;
    }

    public Tarea(String programacion, String tarea) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}