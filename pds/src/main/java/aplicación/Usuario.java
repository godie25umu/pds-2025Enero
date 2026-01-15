package aplicación;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id 
    private String nickname; 
    
    private String contraseña;
    
    @ElementCollection
    @CollectionTable(
        name = "usuario_progresos", 
        joinColumns = @JoinColumn(name = "usuario_nickname")
    )
    @MapKeyColumn(name = "curso_id")
    private Map<Long, ProgresoCurso> progresosPorCurso = new HashMap<>(); 
    
    @Embedded 
    private Estadísticas estadisticas = new Estadísticas();
    
    @ManyToMany
    @JoinTable(
        name = "usuario_logros",
        joinColumns = @JoinColumn(name = "usuario_nickname"), 
        inverseJoinColumns = @JoinColumn(name = "logro_id")
    )
    private List<Logro> logros = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "usuario_cursos",
        joinColumns = @JoinColumn(name = "usuario_nickname"),
        inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private List<Curso> cursos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        name = "usuario_bloques_completados",
        joinColumns = @JoinColumn(name = "usuario_nickname")
    )
    @Column(name = "bloque_key")
    private Set<String> bloquesCompletados = new HashSet<>();

    @Transient 
    private Estrategia estrategia;

    public Usuario() {}
    
    public Usuario(String nickname, String contraseña) {
        this.nickname = nickname;
        this.contraseña = contraseña;
        this.estadisticas = new Estadísticas();
        this.estrategia = new EstrategiaSecuencial();
    }
    
    public void inscribirseEnCurso(Curso curso) {
        if (!cursos.contains(curso)) {
            cursos.add(curso);
            progresosPorCurso.put(curso.getId(), new ProgresoCurso(0, 0));
        }
    }
    
    public void desinscribirseDeCurso(Curso curso) {
        cursos.remove(curso);
        progresosPorCurso.remove(curso.getId());
    }
    
    public ProgresoCurso getProgresoDeCurso(Curso curso) {
        return progresosPorCurso.get(curso.getId());
    }
    
    public void actualizarProgreso(Curso curso, int bloqueActual, int preguntaActual) {
        ProgresoCurso progreso = progresosPorCurso.get(curso.getId());
        if (progreso == null) {
            progreso = new ProgresoCurso(bloqueActual, preguntaActual);
            progresosPorCurso.put(curso.getId(), progreso);
        } else {
            progreso.setBloqueActual(bloqueActual);
            progreso.setPreguntaActual(preguntaActual);
        }
    }
    
    private String generarClaveBloque(String nombreCurso, String nombreBloque) {
        return nombreCurso + ":" + nombreBloque;
    }
    
    public void marcarBloqueCompletado(String nombreCurso, String nombreBloque) {
        if (nombreCurso != null && nombreBloque != null) {
            String clave = generarClaveBloque(nombreCurso, nombreBloque);
            bloquesCompletados.add(clave);
        }
    }

    public boolean isBloqueCompletado(String nombreCurso, String nombreBloque) {
        if (nombreCurso == null || nombreBloque == null) {
            return false;
        }
        String clave = generarClaveBloque(nombreCurso, nombreBloque);
        return bloquesCompletados.contains(clave);
    }
    
    public Set<String> getBloquesCompletados() {
        return bloquesCompletados;
    }
    
    public void resetearProgresoCurso(Curso curso) {
        if (curso != null && curso.getNombre() != null) {
            bloquesCompletados.removeIf(clave -> clave.startsWith(curso.getNombre() + ":"));
        }
    }

    public String getNickname() { 
        return nickname; 
    }
    
    public void setNickname(String nickname) { 
        this.nickname = nickname; 
    }
    
    public String getContraseña() { 
        return contraseña; 
    }
    
    public void setContraseña(String contraseña) { 
        this.contraseña = contraseña; 
    }
    
    public Map<Long, ProgresoCurso> getProgresosPorCurso() { 
        return progresosPorCurso; 
    }
    
    public void setProgresosPorCurso(Map<Long, ProgresoCurso> progresosPorCurso) { 
        this.progresosPorCurso = progresosPorCurso; 
    }
    
    public Estadísticas getEstadisticas() { 
        return estadisticas; 
    }
    
    public void setEstadisticas(Estadísticas estadisticas) { 
        this.estadisticas = estadisticas; 
    }
    
    public List<Logro> getLogros() { 
        return logros; 
    }
    
    public void setLogros(List<Logro> logros) { 
        this.logros = logros; 
    }
    
    public List<Curso> getCursos() {
        return cursos;
    }
    
    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    public Estrategia getEstrategia() { 
        return estrategia; 
    }
    
    public void setEstrategia(Estrategia estrategia) { 
        this.estrategia = estrategia; 
    }
    
    public void setBloquesCompletados(Set<String> bloquesCompletados) {
        this.bloquesCompletados = bloquesCompletados;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "nickname='" + nickname + '\'' +
                ", cursosInscritos=" + cursos.size() +
                ", logros=" + logros.size() +
                ", bloquesCompletados=" + bloquesCompletados.size() +
                '}';
    }
}