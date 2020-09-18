package net.endu.enduscan.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.endu.enduscan.exception.InvalidEntityException;
import net.endu.enduscan.model.IntermediatePoint;
import net.endu.enduscan.model.Location;
import net.endu.enduscan.repository.LocationRepository;
import net.endu.enduscan.service.abstracts.AbstractCrudService;
import net.endu.enduscan.validation.LocationValidator;

@Service
public class LocationService extends AbstractCrudService<Location, Long, LocationRepository> {

    IntermediatePointService intermediatePointService;
    AssignmentHelper assignmentHelper;

    @Autowired
    public LocationService(LocationRepository locationRepository,
            IntermediatePointService intermediatePointService, AssignmentHelper assignmentHelper) {
        this.repository = locationRepository;
        this.intermediatePointService = intermediatePointService;
        this.assignmentHelper = assignmentHelper;

        this.validator = new LocationValidator();
    }

    @Override
    @Transactional
    public Location create(Location location) {
        if (location.getName().startsWith("SPLIT")) {
            location.setName(fetchAvailableName(location.getEditionId()));
        }
        return super.create(location);
    }

    @Override
    @Transactional
    public Location update(Location location, Long locationId) {
        String oldName = fetchOldName(locationId);
        if ((oldName.startsWith("SPLIT") || oldName.startsWith("ORAIN"))
                && location.getName().equals("SPLIT")) {
            location.setName(oldName);
        }
        if (location.getName().equals("SPLIT")) {
            location.setName(fetchAvailableName(location.getEditionId()));
        }
        return super.update(location, locationId);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        intermediatePointService.findByLocationId(id).stream().map(this::unbindLocationFromPoint)
                .forEach(point -> intermediatePointService.update(point, point.getId()));
        assignmentHelper.deleteByLocationId(id);
        super.deleteById(id);
    }

    @Transactional
    public void assignToOperatorById(Long locationId, Long operatorId) {
        assignmentHelper.bindOperatorAndLocationById(operatorId, locationId);
    }

    public List<Location> findByOptional(Optional<Long> editionId, Optional<Long> operatorId) {

        if (editionId.isPresent() && operatorId.isPresent()) {
            return findByEditionId(editionId.get()).stream()
                    .filter(findByOperatorId(operatorId.get())::contains)
                    .collect(Collectors.toList());
        }

        if (editionId.isPresent()) {
            return findByEditionId(editionId.get());
        }

        if (operatorId.isPresent()) {
            return findByOperatorId(operatorId.get());
        }

        return new ArrayList<>();
    }

    public List<Location> findByOperatorId(Long operatorId) {
        return assignmentHelper.findLocationListByOperatorId(operatorId);
    }

    private IntermediatePoint unbindLocationFromPoint(IntermediatePoint point) {
        point.setLocationId(Long.valueOf(0));
        return point;
    }

    public List<Location> findByEditionId(Long editionId) {
        return repository.findByEditionId(editionId);
    }

    private String fetchAvailableName(Long editionId) {
        Integer max = repository.findByEditionId(editionId).stream().map(Location::getName)
                .map(name -> name.replace("ORAIN", "SPLIT"))
                .filter(name -> name.startsWith("SPLIT")).map(name -> name.replace("SPLIT", ""))
                .map(Integer::parseInt).mapToInt(v -> v).max().orElse(0);

        Integer next = max + 1;

        return "SPLIT" + String.valueOf(next);
    }

    private String fetchOldName(Long locationId) {
        return repository.findById(locationId).map(Location::getName)
                .orElseThrow(InvalidEntityException::new);
    }
}
