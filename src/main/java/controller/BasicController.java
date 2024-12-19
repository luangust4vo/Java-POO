package controller;

import services.BasicService;

public interface BasicController<T> {
    BasicService<T> getService();

    default T store(T entity){
        return getService().store(entity);
    }

    default T update(T entity){
        return getService().update(entity);
    }
    
   default void delete(Long id){
        getService().delete(id);  
    }
}