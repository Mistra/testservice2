package net.endu.enduscan.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import net.endu.enduscan.endpoint.abstracts.AbstractCrudController;
import net.endu.enduscan.model.Location;
import net.endu.enduscan.model.Operator;
import net.endu.enduscan.service.LocationService;
import net.endu.enduscan.service.OperatorService;
import net.endu.enduscan.validation.OperatorValidator;

@RestController
@RequestMapping(value = "/operators")
public class OperatorController extends AbstractCrudController<Operator, Long, OperatorService> {

    private LocationService locationService;

    @Autowired
    public OperatorController(OperatorService operatorService, LocationService locationService) {
        service = operatorService;
        this.locationService = locationService;
        this.validator = Optional.of(new OperatorValidator());
    }

    @GetMapping(value = "/search")
    public List<Operator> search(@RequestParam("edition-id") Optional<Long> editionId) {
        return editionId.map(service::findByEditionId).orElse(new ArrayList<>());
    }

    /**
     * Given an operator ID and a List of locationID, it binds the locations to the operator.
     */
    @PostMapping("/{id}/assign-locations")
    public void assignLocations(@PathVariable("id") Long operatorId,
            @RequestBody List<Long> locationIdList) {
        locationIdList.forEach(locationId -> service.assignToLocationById(operatorId, locationId));
    }

    /**
     * Given an operator ID and a List of locationID, it unbinds the locations from the operator. If
     * no body is found it unbinds all the locations from the operator
     */
    @PostMapping("/{id}/unassign-locations")
    public void unassignLocations(@PathVariable("id") Long operatorId,
            @RequestBody Optional<List<Long>> optionalLocationIdList) {
        if (optionalLocationIdList.isPresent()) {
            optionalLocationIdList.get()
                    .forEach(locationId -> service.unassignLocationById(operatorId, locationId));
        } else {
            locationService.findByOperatorId(operatorId).stream().map(Location::getId)
                    .forEach(locationId -> service.unassignLocationById(operatorId, locationId));
        }
    }

}
