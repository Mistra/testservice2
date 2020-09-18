package net.endu.enduscan.endpoint.abstracts;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import net.endu.enduscan.CrudService;
import net.endu.enduscan.model.Identifiable;
import net.endu.enduscan.validation.Validator;

public abstract class AbstractCrudController<E extends Identifiable<ID>, ID, S extends CrudService<E, ID>>
        implements CrudService<E, ID> {

    protected S service;
    protected Optional<Validator<E>> validator;

    @Override
    @PostMapping()
    public E create(@RequestBody E entity) {
        validator.ifPresent(val -> val.throwIfInvalid(entity));
        return service.create(entity);
    }

    @Override
    @PutMapping("/{id}")
    public E update(@RequestBody E entity, @PathVariable("id") ID id) {
        validator.ifPresent(val -> val.throwIfInvalid(entity));
        return service.update(entity, id);
    }

    @Override
    @GetMapping()
    public List<E> findAll() {
        return service.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public Optional<E> findById(@PathVariable("id") ID id) {
        return service.findById(id);
    }

    @Override
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") ID id) {
        service.deleteById(id);
    }
}
