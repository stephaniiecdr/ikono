package _UASS2;

import java.util.List;

public interface GenericDAO<T, ID> {
    
    void save(T entity);
    
    void update(T entity);
    
    void delete(T entity);
    
    void deleteById(ID id);
    
    T findById(ID id);
    
    List<T> findAll();
    
    List<T> findByProperty(String propertyName, Object value);
}