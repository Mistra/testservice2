package net.endu.enduscan.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.endu.enduscan.model.Operator;
import net.endu.enduscan.repository.OperatorRepository;
import net.endu.enduscan.service.abstracts.AbstractCrudService;
import net.endu.enduscan.validation.OperatorValidator;

@Service
public class OperatorService extends AbstractCrudService<Operator, Long, OperatorRepository> {

    private AssignmentHelper assignmentHelper;

    @Autowired
    public OperatorService(OperatorRepository operatorRepository,
            AssignmentHelper assignmentHelper) {
        this.repository = operatorRepository;
        this.assignmentHelper = assignmentHelper;

        this.validator = new OperatorValidator();
    }

    public List<Operator> findByEditionId(Long editionId) {
        return repository.findByEditionId(editionId);
    }

    @Transactional
    public void assignToLocationById(Long operatorId, Long locationId) {
        assignmentHelper.bindOperatorAndLocationById(operatorId, locationId);
    }

    @Transactional
    public void unassignLocationById(Long operatorId, Long locationId) {
        assignmentHelper.unbindOperatorAndLocationById(operatorId, locationId);
    }

    @Override
    @Transactional
    public void deleteById(Long operatorId) {
        assignmentHelper.deleteByOperatorId(operatorId);
        super.deleteById(operatorId);
    }

    public List<Operator> findByLocationId(Long locationId) {
        return assignmentHelper.findOperatorListByLocationId(locationId);
    }

}
