package net.endu.enduscan.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import net.endu.enduscan.model.Path;

@Repository
public interface PathRepository extends CrudRepository<Path, Long> {
    List<Path> findByEditionId(Long editionId);
}
