package aplicación;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {
	
     * El nombre elegido por el usuario. 
     */
    private String nickname;
    
    private String contraseña;
    
    @Embedded 
    private Estadísticas estadisticas = new Estadísticas();
    
    @ManyToMany
    @JoinTable(
        name = "usuario_logros",
        joinColumns = @JoinColumn(name = "usuario_id"),
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

    @Transient 
    private Estrategia estrategia;

    public Usuario() {}

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
    public List<Curso> getCursos() { return cursos; }
    public void setCursos(List<Curso> cursos) { this.cursos = cursos; }
    public Estadísticas getEstadisticas() { return estadisticas; }
    public void setEstadisticas(Estadísticas estadisticas) { this.estadisticas = estadisticas; }
    public Estrategia getEstrategia() { return estrategia; }
    public void setEstrategia(Estrategia estrategia) { this.estrategia = estrategia; }
    public List<Logro> getLogros() {return logros;}
    public void setLogros(List<Logro> logros)  {this.logros = logros;}
}