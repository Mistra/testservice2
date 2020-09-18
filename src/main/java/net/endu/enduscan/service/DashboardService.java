package net.endu.enduscan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.endu.enduscan.model.Dashboard;


@Service
public class DashboardService {
    private PathService pathService;
    private LocationService locationService;
    private OperatorService operatorService;

    @Autowired
    public DashboardService(PathService pathService, LocationService locationService,
            OperatorService operatorService) {
        this.pathService = pathService;
        this.locationService = locationService;
        this.operatorService = operatorService;
    }

    public Dashboard generateDashboard(Long editionId) {
        Dashboard dashboard = new Dashboard();

        dashboard.setConfiguredPaths(pathService.findByEditionId(editionId).size());
        dashboard.setHandledLocations(locationService.findByEditionId(editionId).size());
        dashboard.setEnabledUsers(operatorService.findByEditionId(editionId).size());

        return dashboard;
    }

}
