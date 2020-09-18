package net.endu.enduscan.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import net.endu.enduscan.model.OperatorAssignment;

@Repository
public interface OperatorAssignmentRepository extends CrudRepository<OperatorAssignment, Long> {
    List<OperatorAssignment> findByLocationId(Long locationId);

    List<OperatorAssignment> findByOperatorId(Long operatorId);
}
