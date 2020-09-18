package net.endu.enduscan.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.endu.enduscan.model.IntermediatePoint;
import net.endu.enduscan.repository.IntermediatePointRepository;
import net.endu.enduscan.service.abstracts.AbstractCrudService;

@Service
public class IntermediatePointService
        extends AbstractCrudService<IntermediatePoint, Long, IntermediatePointRepository> {

    @Autowired
    public IntermediatePointService(IntermediatePointRepository intermediatePointRepository) {
        this.repository = intermediatePointRepository;
    }

    // Messy implementation, can't use specification on Repo layer due to problems with locationId
    public List<IntermediatePoint> findByOptional(Optional<Long> editionId, Optional<Long> pathId,
            Optional<Long> locationId) {

        Predicate<IntermediatePoint> filterByPathId = intermediatePoint -> pathId
                .map(pId -> intermediatePoint.getPathId().equals(pId)).orElse(true);

        Predicate<IntermediatePoint> filterByLocationId = intermediatePoint -> locationId
                .map(lId -> intermediatePoint.getLocationId().equals(lId)).orElse(true);

        if (editionId.isPresent()) {
            return repository.findByEditionId(editionId.get()).stream().filter(filterByPathId)
                    .filter(filterByLocationId).collect(Collectors.toList());
        }

        if (pathId.isPresent()) {
            return repository.findByPathId(pathId.get()).stream().filter(filterByLocationId)
                    .collect(Collectors.toList());
        }

        if (locationId.isPresent()) {
            return repository.findByLocationId(locationId.get());
        }

        return new ArrayList<>();
    }

    public List<IntermediatePoint> findByPathId(Long pathId) {
        return repository.findByPathId(pathId);
    }

    public List<IntermediatePoint> findByLocationId(Long locationId) {
        return repository.findByLocationId(locationId);
    }

    @Transactional
    public List<IntermediatePoint> saveAll(List<IntermediatePoint> intermediatePointList) {
        return StreamSupport.stream(repository.saveAll(intermediatePointList).spliterator(), false)
                .collect(Collectors.toList());
    }
}
