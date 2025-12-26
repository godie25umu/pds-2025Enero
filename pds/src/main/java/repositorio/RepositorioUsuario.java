package repositorio;

import aplicación.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class RepositorioUsuario {
    private final EntityManager em;

    public RepositorioUsuario(EntityManager em) {
        this.em = em;
    }

    public void guardarUsuario(Usuario usuario) {
        em.getTransaction().begin();
        try {
            if (buscarPorNombre(usuario.getNickname()) == null) {
                em.persist(usuario); 
            } else {
                em.merge(usuario); 
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public Usuario buscarPorNombre(String nombre) {
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.nickname = :nombre", Usuario.class);
        query.setParameter("nombre", nombre);
        return query.getResultStream().findFirst().orElse(null); //
    }
}