package net.endu.enduscan.repository;

import java.util.List;
import net.endu.enduscan.model.IntermediatePoint;

/**
 * Custom repository needed, at DB level the intermediate point doesn't have a locationId, matching
 * and saving so is custom. Also, this layer is used for retrocompatibility issues. START, FINISH,
 * SPLIT1 etc used to be ORAPARTENZA, ORAARRIVO, ORAIN1 etc
 */
public interface CustomIntermediatePointRepository<T> {
    <S extends T> S save(S entity);

    <S extends IntermediatePoint> Iterable<S> saveAll(Iterable<S> entities);

    List<T> findByEditionId(Long editionId);

    List<T> findByLocationId(Long locationId);

    List<T> findByPathId(Long pathId);
}
