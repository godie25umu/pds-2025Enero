package aplicación;

import java.util.List;

/**
 * Representa un usuario dentro del sistema.
 * Contiene un nombre de usuario (nickname), una contraseña y
 * una lista de cursos a los que el usuario está inscrito.
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

	private Estadísticas estadisticas;
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

	public Estadísticas getEstadisticas() {
		return estadisticas;
	}

	public void setEstadisticas(Estadísticas estadisticas) {
		this.estadisticas = estadisticas;
	}
    
    
}

