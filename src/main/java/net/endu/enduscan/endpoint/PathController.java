package net.endu.enduscan.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import net.endu.enduscan.endpoint.abstracts.AbstractCrudController;
import net.endu.enduscan.model.Path;
import net.endu.enduscan.service.PathService;
import net.endu.enduscan.validation.PathValidator;

@RestController
@RequestMapping(value = "/paths")
public class PathController extends AbstractCrudController<Path, Long, PathService> {

    @Autowired
    public PathController(PathService pathService) {
        service = pathService;
        this.validator = Optional.of(new PathValidator());
    }

    @GetMapping(value = "/search")
    public List<Path> search(@RequestParam("edition-id") Optional<Long> editionId) {
        if (editionId.isPresent()) {
            return service.findByEditionId(editionId.get());
        }

        return new ArrayList<>();
    }
}
