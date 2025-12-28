package repositorio;

import aplicación.Pregunta;
import jakarta.persistence.EntityManager;

public class PersistenciaPregunta {
    private final EntityManager em;

    public PersistenciaPregunta(EntityManager em) {
        this.em = em;
    }

    public void guardar(Pregunta pregunta) {
        em.getTransaction().begin();
        try {
            if (pregunta.getId() == null) {
                em.persist(pregunta);
            } else {
                em.merge(pregunta);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public Pregunta buscarPorId(Long id) {
        return em.find(Pregunta.class, id);
    }
}