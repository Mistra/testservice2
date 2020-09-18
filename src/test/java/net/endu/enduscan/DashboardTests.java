package net.endu.enduscan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import net.endu.enduscan.model.Dashboard;

@SpringBootTest
class DashboardTests {

    @Test
	void startDate() {
        Dashboard dashboard = new Dashboard();
        String dateTimeString = "5/2/2020 9:30";
        dashboard.setStartEvent(dateTimeString);
        assertEquals(dashboard.getStartEvent(), dateTimeString);
    }

    @Test
    void endDate() {
        Dashboard dashboard = new Dashboard();
        String dateTimeString = "15/11/2020 10:30";
        dashboard.setEndEvent(dateTimeString);
        assertEquals(dashboard.getEndEvent(), dateTimeString);
    }

}
