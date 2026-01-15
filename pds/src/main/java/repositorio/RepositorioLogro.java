package repositorio;

import aplicación.Logro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class RepositorioLogro {
    private final EntityManager em;

    public RepositorioLogro(EntityManager em) {
        this.em = em;
    }

    public void guardar(Logro logro) {
        em.getTransaction().begin();
        try {
            if (logro.getId() == null) {
                em.persist(logro); 
            } else {
                em.merge(logro);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public Logro buscarPorId(Long id) {
        return em.find(Logro.class, id);
    }

    public Logro buscarPorNombre(String nombre) {
        TypedQuery<Logro> query = em.createQuery(
            "SELECT l FROM Logro l WHERE l.nombre = :nombre", Logro.class);
        query.setParameter("nombre", nombre);
        return query.getResultStream().findFirst().orElse(null);
    }

    public List<Logro> obtenerTodos() {
        TypedQuery<Logro> query = em.createQuery(
            "SELECT l FROM Logro l ORDER BY l.puntos ASC", Logro.class);
        return query.getResultList();
    }

    public void eliminar(Logro logro) {
        em.getTransaction().begin();
        try {
            Logro logroGestionado = em.merge(logro);
            em.remove(logroGestionado);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }
}