package net.endu.enduscan.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import net.endu.enduscan.model.Location;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {
    List<Location> findByEditionId(Long editionId);

    @Query("SELECT l FROM Location l INNER JOIN Path p ON l.editionId = p.editionId WHERE p.id = :pathId AND l.name = :field")
    List<Location> findByPathIdAndName(@Param("pathId") Long pathId, @Param("field") String field);

}
