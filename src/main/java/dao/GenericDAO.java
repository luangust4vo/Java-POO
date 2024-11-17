package dao;

import java.util.List;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class GenericDAO<T> {
    private final static EntityManagerFactory factory;
    private final Class<T> entityClass;

    static {
        factory = Persistence.createEntityManagerFactory("nomePU");
    }

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public static <T> GenericDAO<T> getInstance(Class<T> entityClass) {
        return new GenericDAO<>(entityClass);
    }

    private EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    protected <R> R executeWithinTransaction(Function<EntityManager, R> action) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();
            R result = action.apply(em);
            em.getTransaction().commit();
            return result;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    protected <R> R execute(Function<EntityManager, R> action) {
        EntityManager em = getEntityManager();

        try {
            return action.apply(em);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public T store(T entity) {
        return executeWithinTransaction(em -> {
            em.persist(entity);
            return entity;
        });
    }

    public T update(T entity) {
        return executeWithinTransaction(em -> em.merge(entity));
    }

    public void delete(Object id) {
        executeWithinTransaction(em -> {
            T entity = em.find(this.entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }

            return null;
        });
    }

    public T findById(Object id) {
        return execute(em -> em.find(this.entityClass, id));
    }

    public List<T> listAll() {
        return execute(em -> 
            em.createQuery("FROM " + this.entityClass.getSimpleName(), this.entityClass).getResultList());
    }
}
