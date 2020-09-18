package net.endu.enduscan.endpoint;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import net.endu.enduscan.endpoint.abstracts.AbstractCrudController;
import net.endu.enduscan.model.Location;
import net.endu.enduscan.service.LocationService;
import net.endu.enduscan.validation.LocationValidator;

@RestController
@RequestMapping(value = "/locations")
public class LocationController extends AbstractCrudController<Location, Long, LocationService> {

    @Autowired
    public LocationController(LocationService locationService) {
        this.service = locationService;
        this.validator = Optional.of(new LocationValidator());
    }

    @GetMapping(value = "/search")
    public List<Location> search(@RequestParam("edition-id") Optional<Long> editionId,
            @RequestParam("operator-id") Optional<Long> operatorId) {

        return service.findByOptional(editionId, operatorId);
    }
}
