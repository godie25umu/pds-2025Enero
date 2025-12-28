package repositorio;

import aplicación.BloqueDeContenido;
import jakarta.persistence.EntityManager;

public class PersistenciaBloque {
    private final EntityManager em;

    public PersistenciaBloque(EntityManager em) {
        this.em = em;
    }

    public void guardar(BloqueDeContenido bloque) {
        em.getTransaction().begin();
        try {
            if (bloque.getId() == null) {
                em.persist(bloque);
            } else {
                em.merge(bloque);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public BloqueDeContenido buscarPorId(Long id) {
        return em.find(BloqueDeContenido.class, id);
    }
}