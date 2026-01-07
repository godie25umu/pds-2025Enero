package aplicación;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un logro que los usuarios pueden desbloquear.
 */
@Entity
@Table(name = "logros")
public class Logro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private int puntos;
    
    @ManyToMany(mappedBy = "logros")
    private List<Usuario> usuarios = new ArrayList<>();

    public Logro() {}
    
    public Logro(String nombre, String descripcion, int puntos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.puntos = puntos;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    @Override
    public String toString() {
        return "Logro{" +
                "nombre='" + nombre + '\'' +
                ", puntos=" + puntos +
                '}';
    }
}