package net.endu.enduscan.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import net.endu.enduscan.model.IntermediatePoint;

@Repository
public interface IntermediatePointRepository
        extends CustomIntermediatePointRepository<IntermediatePoint>,
        CrudRepository<IntermediatePoint, Long> {

    List<IntermediatePoint> findByPathId(Long pathId);

    List<IntermediatePoint> findByLocationId(Long locationId);

    List<IntermediatePoint> findByEditionId(Long editionId);
}
