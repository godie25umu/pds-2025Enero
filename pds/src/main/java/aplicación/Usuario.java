package aplicación;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @MapKeyJoinColumn(name = "curso_id") 
    private Map<Curso, ProgresoCurso> cursos = new HashMap<>(); 
    
    @Embedded 
    private Estadísticas estadisticas = new Estadísticas();
    
    @ManyToMany
    @JoinTable(
        name = "usuario_logros",
        joinColumns = @JoinColumn(name = "usuario_nickname"), 
        inverseJoinColumns = @JoinColumn(name = "logro_id")
    )
    private List<Logro> logros = new ArrayList<>();

    @Transient 
    private Estrategia estrategia;

    public Usuario() {}

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
    
    public Map<Curso, ProgresoCurso> getCursos() { return cursos; }
    public void setCursos(Map<Curso, ProgresoCurso> cursos) { this.cursos = cursos; }
    
    public Estadísticas getEstadisticas() { return estadisticas; }
    public void setEstadisticas(Estadísticas estadisticas) { this.estadisticas = estadisticas; }
    
    public List<Logro> getLogros() { return logros; }
    public void setLogros(List<Logro> logros) { this.logros = logros; }

    public Estrategia getEstrategia() { return estrategia; }
    public void setEstrategia(Estrategia estrategia) { this.estrategia = estrategia; }
}