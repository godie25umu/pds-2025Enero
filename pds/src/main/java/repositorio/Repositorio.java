package repositorio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Repositorio {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("AprendeDivertido3000");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void cerrarFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}