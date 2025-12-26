package repositorio;

import aplicación.Curso;
import jakarta.persistence.EntityManager;

public class RepositorioCurso {
    private final EntityManager em;

    public RepositorioCurso(EntityManager em) {
        this.em = em;
    }

    public void guardar(Curso curso) {
        em.getTransaction().begin();
        try {
            if (curso.getId() == null) {
                em.persist(curso); 
            } else {
                em.merge(curso);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }
}
