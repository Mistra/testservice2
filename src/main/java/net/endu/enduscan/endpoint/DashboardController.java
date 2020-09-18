package net.endu.enduscan.endpoint;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.endu.enduscan.model.Dashboard;
import net.endu.enduscan.service.DashboardService;

@Data
class Checked {
    boolean isChecked = false;
}

@Slf4j
@RestController
@RequestMapping(value = "/dashboard")
public class DashboardController {
    private DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping(value = "/search")
    public Dashboard search(@RequestParam("edition-id") Optional<Long> editionId) {
        return editionId.map(dashboardService::generateDashboard).orElse(new Dashboard());

    }

    @GetMapping("/operator-is-allowed")
    public Boolean operatorIsAllowed() {
        return false;
    }


    @PostMapping("/allow-operator")
    public void allowOperator(@RequestBody Checked checked) {
        log.info("Setting checked state to {}", checked.isChecked());
    }
}