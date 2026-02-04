package repository.interfaces;

import java.util.List;

public interface CrudRepository<T> {
    void create(T obj);
    T findById(int id);
    List<T> findAll();
    void update(T obj);
    void delete(int id);
}
