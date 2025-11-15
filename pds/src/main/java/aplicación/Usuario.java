package aplicación;

import java.util.List;

/**
 * Representa un usuario dentro del sistema.
 * Contiene un nombre de usuario (nickname), una contraseña,
 * una lista de cursos a los que está inscrito, estadísticas
 * asociadas al rendimiento del usuario y una estrategia asignada.
 */
public class Usuario {
	
    /** 
     * El nombre elegido por el usuario. 
     */
    private String nickname;
    
    /** 
     * La contraseña del usuario. 
     */
    private String contraseña;
    
    /** 
     * Lista de cursos a los que el usuario está inscrito. 
     */
    private List<Curso> cursos;

    /** 
     * Estadísticas de rendimiento del usuario. 
     */
    private Estadísticas estadisticas;
    
    /** 
     * Estrategia actual asignada al usuario. 
     */
    private Estrategia estrategia;
    

    /**
     * Obtiene el nickname del usuario.
     * 
     * @return el nickname del usuario
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Establece el nickname del usuario.
     * 
     * @param nickname el nombre de usuario a asignar
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Obtiene la contraseña del usuario.
     * 
     * @return la contraseña del usuario
     */
    public String getContraseña() {
        return contraseña;
    }

    /**
     * Establece la contraseña del usuario.
     * 
     * @param contraseña la contraseña a asignar
     */
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    /**
     * Obtiene la lista de cursos a los que el usuario está inscrito.
     * 
     * @return una lista de objetos {@code Curso}
     */
    public List<Curso> getCursos() {
        return cursos;
    }

    /**
     * Establece la lista de cursos a los que el usuario estará inscrito.
     * 
     * @param cursos la lista de cursos a asignar al usuario
     */
    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    /**
     * Obtiene las estadísticas del usuario.
     * 
     * @return un objeto {@code Estadísticas} con la información de rendimiento del usuario
     */
    public Estadísticas getEstadisticas() {
        return estadisticas;
    }

    /**
     * Establece las estadísticas del usuario.
     * 
     * @param estadisticas el objeto de estadísticas a asignar
     */
    public void setEstadisticas(Estadísticas estadisticas) {
        this.estadisticas = estadisticas;
    }

    /**
     * Obtiene la estrategia que está utilizando el usuario.
     * 
     * @return la estrategia del usuario
     */
    public Estrategia getEstrategia() {
        return estrategia;
    }

    /**
     * Establece la estrategia del usuario.
     * 
     * @param estrategia la estrategia a asignar
     */
    public void setEstrategia(Estrategia estrategia) {
        this.estrategia = estrategia;
    }
}

