package repositorio;

import aplicación.Usuario;
import aplicación.Curso;
import aplicación.ProgresoCurso;
import jakarta.persistence.EntityManager;

public class PersistenciaProgreso {
    private final EntityManager em;

    public PersistenciaProgreso(EntityManager em) {
        this.em = em;
    }

    public void actualizarProgreso(String nickname, Curso curso, ProgresoCurso nuevoProgreso) {
        em.getTransaction().begin();
        try {
            Usuario usuario = em.find(Usuario.class, nickname);
            if (usuario != null) {
                usuario.getProgresosPorCurso().put(curso.getId(), nuevoProgreso);
                em.merge(usuario);
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
                throw new IllegalArgumentException("Usuario no encontrado: " + nickname);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }
}