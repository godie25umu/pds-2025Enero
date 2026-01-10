package aplicación;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String dominio;
    
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BloqueDeContenido> bloques = new ArrayList<>();
    
    @ManyToMany(mappedBy = "cursos")
    private List<Usuario> usuarios = new ArrayList<>();

    public Curso() {
    }

    public Curso(String nombre, String dominio) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del curso no puede estar vacío.");
        }
        this.nombre = nombre;
        this.dominio = dominio;
    }

    /**
     * Añade un bloque al curso y establece la relación inversa en el bloque.
     */
    public void agregarBloque(BloqueDeContenido bloque) {
        this.bloques.add(bloque);
        bloque.setCurso(this);
    }

    /**
     * Elimina un bloque y rompe la relación inversa.
     */
    public void eliminarBloque(BloqueDeContenido bloque) {
        this.bloques.remove(bloque);
        bloque.setCurso(null);
    }
    
    public void agregarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        usuario.getProgresosPorCurso().put(this.getId(), new ProgresoCurso(0, 0));
    }

    public void eliminarUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
        usuario.getCursos().remove(this);
    }

    public Long getId() { return id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDominio() { return dominio; }
    public void setDominio(String dominio) { this.dominio = dominio; }

    public List<BloqueDeContenido> getBloques() { return bloques; }
    
    public List<Usuario> getUsuarios() { return usuarios; }
}