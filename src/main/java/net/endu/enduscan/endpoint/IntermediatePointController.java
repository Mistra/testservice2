package net.endu.enduscan.endpoint;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import net.endu.enduscan.endpoint.abstracts.AbstractCrudController;
import net.endu.enduscan.model.IntermediatePoint;
import net.endu.enduscan.service.IntermediatePointService;
import net.endu.enduscan.validation.IntermediatePointValidator;

@RestController
@RequestMapping(value = "/intermediate-points")
public class IntermediatePointController
        extends AbstractCrudController<IntermediatePoint, Long, IntermediatePointService> {

    @Autowired
    public IntermediatePointController(IntermediatePointService intermediatePointService) {
        this.service = intermediatePointService;
        this.validator = Optional.of(new IntermediatePointValidator());
    }

    @GetMapping(value = "/search")
    public List<IntermediatePoint> search(@RequestParam("edition-id") Optional<Long> editionId,
            @RequestParam("path-id") Optional<Long> pathId,
            @RequestParam("location-id") Optional<Long> locationId) {

        return service.findByOptional(editionId, pathId, locationId);
    }

    @PostMapping("/bulk")
    public List<IntermediatePoint> saveAll(
            @RequestBody List<IntermediatePoint> intermediatePointList) {
        return service.saveAll(intermediatePointList);
    }
}
