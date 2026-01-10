package controlador;

import aplicación.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Controlador {
    private EntityManagerFactory emf;
    private EntityManager em;
    private Usuario usuarioActual; // Para mantener la sesión del usuario que entró

    public Controlador() {
        // "AprendeDivertidoPU" debe coincidir con el nombre en tu persistence.xml
        this.emf = Persistence.createEntityManagerFactory("AprendeDivertido3000");
        this.em = emf.createEntityManager();
    }

    /**
     * Lógica de inicio de sesión consultando la base de datos.
     */
    public boolean iniciarSesion(String nickname, String contraseña) {
        try {
            // Buscamos al usuario por su ID (nickname)
            Usuario u = em.find(Usuario.class, nickname);
            
            if (u != null && u.getContraseña().equals(contraseña)) {
                this.usuarioActual = u; // Guardamos el usuario en sesión
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lógica de registro persistiendo el nuevo usuario en la BD.
     */
    public boolean registrarUsuario(String nickname, String contraseña) {
        try {
            em.getTransaction().begin();
            
            // Verificamos si ya existe
            if (em.find(Usuario.class, nickname) != null) {
                em.getTransaction().rollback();
                return false; 
            }
            
            // Creamos y persistimos
            Usuario nuevo = new Usuario(nickname, contraseña);
            em.persist(nuevo);
            
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener el usuario una vez logueado
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}