package net.endu.enduscan.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.endu.enduscan.model.IntermediatePoint;
import net.endu.enduscan.model.Path;
import net.endu.enduscan.repository.PathRepository;
import net.endu.enduscan.service.abstracts.AbstractCrudService;
import net.endu.enduscan.validation.PathValidator;

@Service
@Transactional
public class PathService extends AbstractCrudService<Path, Long, PathRepository> {
    private IntermediatePointService intermediatePointService;

    @Autowired
    public PathService(PathRepository pathRepository,
            IntermediatePointService intermediatePointService) {
        this.repository = pathRepository;
        this.intermediatePointService = intermediatePointService;

        this.validator = new PathValidator();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        intermediatePointService.findByPathId(id).stream().map(IntermediatePoint::getId)
                .forEach(intermediatePointService::deleteById);
        super.deleteById(id);
    }

    public List<Path> findByEditionId(Long editionId) {
        return repository.findByEditionId(editionId);
    }
}
