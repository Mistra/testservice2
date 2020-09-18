package net.endu.enduscan;

import java.util.List;
import java.util.Optional;

public interface CrudService<E, ID> {
    E create(E entity);

    E update(E entity, ID id);

    List<E> findAll();

    Optional<E> findById(ID id);

    void deleteById(ID id);
}
