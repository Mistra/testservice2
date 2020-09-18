package net.endu.enduscan.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.endu.enduscan.exception.UnboundEntityException;
import net.endu.enduscan.model.Location;
import net.endu.enduscan.model.Operator;
import net.endu.enduscan.model.OperatorAssignment;
import net.endu.enduscan.repository.LocationRepository;
import net.endu.enduscan.repository.OperatorAssignmentRepository;
import net.endu.enduscan.repository.OperatorRepository;

@Service
public class AssignmentHelper {

    private OperatorAssignmentRepository assignmentRepository;
    private OperatorRepository operatorRepository;
    private LocationRepository locationRepository;

    @Autowired
    AssignmentHelper(OperatorAssignmentRepository assignmentRepository,
            OperatorRepository operatorRepository, LocationRepository locationRepository) {
        this.assignmentRepository = assignmentRepository;
        this.operatorRepository = operatorRepository;
        this.locationRepository = locationRepository;
    }

    void bindOperatorAndLocationById(Long operatorId, Long locationId) {
        checkThatEntitiesAreBound(operatorId, locationId);

        OperatorAssignment assignment = new OperatorAssignment(locationId, operatorId);
        assignmentRepository.save(assignment);
    }

    void unbindOperatorAndLocationById(Long operatorId, Long locationId) {
        checkThatEntitiesAreBound(operatorId, locationId);

        assignmentRepository.findByOperatorId(operatorId).stream()
                .filter(assignment -> assignment.getLocationId().equals(locationId))
                .forEach(assignmentRepository::delete);
    }

    @Transactional
    void deleteByOperatorId(Long operatorId) {
        assignmentRepository.findByOperatorId(operatorId).forEach(assignmentRepository::delete);
    }

    void deleteByLocationId(Long locationId) {
        assignmentRepository.findByLocationId(locationId).forEach(assignmentRepository::delete);
    }

    List<Location> findLocationListByOperatorId(Long operatorId) {
        return assignmentRepository.findByOperatorId(operatorId).stream()
                .map(OperatorAssignment::getLocationId).map(locationRepository::findById)
                .map(Optional::get).collect(Collectors.toList());
    }

    List<Operator> findOperatorListByLocationId(Long locationId) {
        return assignmentRepository.findByLocationId(locationId).stream()
                .map(OperatorAssignment::getOperatorId).map(operatorRepository::findById)
                .map(Optional::get).collect(Collectors.toList());
    }

    private void checkThatEntitiesAreBound(Long operatorId, Long locationId) {
        if (operatorId == null || operatorId.equals(Long.valueOf(0))) {
            throw new UnboundEntityException(
                    "Cannot operate assignements over an unbound operator");
        }

        if (locationId == null || locationId.equals(Long.valueOf(0))) {
            throw new UnboundEntityException(
                    "Cannot operate assignements over an unbound location");
        }
    }
}
