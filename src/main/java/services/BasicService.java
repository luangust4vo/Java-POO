package services;

import dao.GenericDAO;

public interface BasicService<T> {
    GenericDAO<T> getDAO();

    default T store(T entity) {
        return getDAO().store(entity);
    }

    default T update(T entity) {
        return getDAO().update(entity);
    }

    default void delete(Long id) {
        getDAO().delete(id);
    }
}
