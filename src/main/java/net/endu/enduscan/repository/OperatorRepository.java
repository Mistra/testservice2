package net.endu.enduscan.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import net.endu.enduscan.model.Operator;

@Repository
public interface OperatorRepository extends CrudRepository<Operator, Long> {
    List<Operator> findByEditionId(Long editionId);
}
