package net.endu.enduscan.service.abstracts;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import net.endu.enduscan.CrudService;
import net.endu.enduscan.model.Identifiable;
import net.endu.enduscan.validation.Validator;

public abstract class AbstractCrudService<E extends Identifiable<ID>, ID, R extends CrudRepository<E, ID>>
        implements CrudService<E, ID> {

    protected R repository;
    protected Validator<E> validator;

    @Override
    @Transactional
    public E create(E entity) {
        entity.setId(null);
        if (validator != null)
            validator.throwIfInvalid(entity);
        return repository.save(entity);
    }

    @Override
    @Transactional
    public E update(E entity, ID id) {
        entity.setId(id);
        if (validator != null)
            validator.isValid(entity);
        return repository.save(entity);
    }

    @Override
    public List<E> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<E> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        this.findAll().forEach(entity -> deleteById(entity.getId()));
    }
}
